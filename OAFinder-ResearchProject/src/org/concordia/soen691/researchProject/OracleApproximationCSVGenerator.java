package org.concordia.soen691.researchProject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;

/*
 * @author Manik Hossain
 * @version 1.0
 */

public class OracleApproximationCSVGenerator {

	List<OracleApproximationFinderResponse> resultArray = null;
	ASTParser parser = null;
	String[] encodings = {"UTF-8"};
	String rtJAR = "";
	
	int numberOfFilesParsed = 0;
	int numberOfOA = 0;

	public OracleApproximationCSVGenerator() {
		resultArray = new ArrayList<>();

		// define JavaParser parser and resolver
		// we need to define a reflection type resolver to detect java.lang object
		// types.
		TypeSolver typeSolver = new CombinedTypeSolver();
		((CombinedTypeSolver) typeSolver).add(new ReflectionTypeSolver());

		String sourceDir = ConfigurationLoader.getConfigurationProperties().getProperty("source_folder", "/tmp");
		((CombinedTypeSolver) typeSolver).add(new JavaParserTypeSolver(sourceDir));

		JavaSymbolSolver symbolSolver = new JavaSymbolSolver(typeSolver);
		JavaParser.getStaticConfiguration().setSymbolResolver(symbolSolver);

		rtJAR = ConfigurationLoader.getConfigurationProperties().getProperty("rt_jar","");
		if (rtJAR.equals("")) {
			System.out.println("JRE Runtime JAR missing or not properly set in configuration file. Please set rt_jar property and try again...");
			System.exit(-1);
		}		
	}

	@SuppressWarnings("deprecation")
	public void parseOneFile(File in) throws FileNotFoundException {

		// JavaParser
		com.github.javaparser.ast.CompilationUnit cu = JavaParser.parse(in);

		parser = ASTParser.newParser(AST.JLS8);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);

		Hashtable<String, String> options = JavaCore.getOptions();
		JavaCore.setComplianceOptions(JavaCore.VERSION_1_8, options);
		parser.setCompilerOptions(options);

		String encoding = null;
		try {
			parser.setSource(FileUtils.readFileToString(in, encoding).toCharArray());
		} catch (IOException e) {
			System.out.println("\nFile read error: " + in.getPath());
		}

		String[] sources = {in.getParent()+"/"};
		String[] classpath = {".", rtJAR};
		parser.setEnvironment(classpath, sources, encodings, true);
		parser.setUnitName(in.getName());
		
		//the 3 below need to be set each time for a new file otherwise if you had an error it will never recover from it!
		parser.setResolveBindings(true);
		parser.setBindingsRecovery(true);
		parser.setStatementsRecovery(true);

		boolean jdtParserOK = true;
		org.eclipse.jdt.core.dom.CompilationUnit cuJDT = null;
		try {
			cuJDT = (org.eclipse.jdt.core.dom.CompilationUnit) parser
				.createAST(null);
		} catch (Exception e) {
			System.out.println("JDT parser error on file:" + in.getName());
			jdtParserOK = false;
		}
		
		//System.out.println("\nParsing file: " + in.getPath());
		OracleApproximationFinderResponse bfResponse = new OracleApproximationFinderResponse();
		bfResponse.setFileName(in.getPath());

		// iterate through JavaParser objects
		for (VoidVisitor<ArrayList<String>> pattern : ConfigurationLoader.getEnabledVisitors()) {
			ArrayList<String> responseElement = new ArrayList<String>();

			String patternName = pattern.getClass().getSimpleName();
			//System.out.println("Searching for Oracle Approximation " + patternName);

			pattern.visit(cu, responseElement);

			if (responseElement.isEmpty() == false) {
				//System.out.println("\t Oracle Approximation found");
				bfResponse.getResultMap().put(patternName, responseElement);
			} else {
				//System.out.println("\t Oracle Approximation not found");
			}
		}

		// iterate through Eclipse JDT AST parser objects
		if (jdtParserOK) {
			for (SOEN691OACollector pattern : ConfigurationLoader.getJDTASTEnabledVisitors()) {
	
				String patternName = pattern.getClass().getSimpleName();
				//System.out.println("Searching for Oracle Approximation " + patternName);
				pattern.setCompilationUnit(cuJDT);
				cuJDT.accept(pattern);
				
				if (pattern.getCollector().isEmpty() == false) {
					//System.out.println("\t Oracle Approximation found");
					bfResponse.getResultMap().put(patternName, new ArrayList<String>(pattern.getCollector()));
					pattern.getCollector().clear();
				} else {
					//System.out.println("\t Oracle Approximation not found");
				}
			}
		}

		if (bfResponse.isEmpty() == false) {
			resultArray.add(bfResponse);
		}
		cu = null; // force garbage collection
		cuJDT = null;
		parser = null;
		numberOfFilesParsed++;
	}

	public void parseDirectoryStructure() {

		String sourceDir = ConfigurationLoader.getConfigurationProperties().getProperty("source_folder", "/tmp");
		File projectDir = new File(sourceDir);
		explore(projectDir);
	}

	/*
	 * Recursively parse the folder structure and for each Java file apply parsing.
	 */
	private void explore(File file) {
		if (file.isDirectory()) {
			for (File child : file.listFiles()) {
				explore(child);
			}
		} else {
			if (file.getName().endsWith(".java")) {
				try {
					parseOneFile(file);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void printResults() throws IOException {
		System.out.println("\n\nPrinting Founded Oracle Approximation :");
		 FileWriter writer = new FileWriter("C:\\Users\\Manik\\OADL4j.csv");
		for (OracleApproximationFinderResponse resultElement : resultArray) {
			Map<String, ArrayList<String>> innerMap = resultElement.getResultMap();
			for (Map.Entry<String, ArrayList<String>> entry : innerMap.entrySet()) {
				ArrayList<String> getAllOA = entry.getValue();
				
				for (String OAFunctions: getAllOA) {
					//System.out.println(resultElement.getFileName() +"--" + OAFunctions);
					
				        List<String> OAFunction = new ArrayList<>();
				        OAFunction.add(resultElement.getFileName() +"," + OAFunctions);
				        String collect = OAFunction.stream().collect(Collectors.joining(","));
				        writer.write("\n");
				        writer.write(collect);
				       				
					numberOfOA++;
				}
			}
		}
		System.out.println("\nTotal number of files parsed: " + numberOfFilesParsed);
		System.out.println("\nTotal number of Oracle Approximation Found: " + numberOfOA);
		writer.close();
	}
}
