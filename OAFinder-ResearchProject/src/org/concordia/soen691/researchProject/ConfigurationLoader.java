package org.concordia.soen691.researchProject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.github.javaparser.ast.visitor.VoidVisitor;

/*
 * @author Manik Hossain
 * @version 1.0
 * <p>
 * This is the configuration loader and resource initializer class.
 * </p>
 */

public class ConfigurationLoader {

	static Properties props = null;
	static final String propFileName = "config.properties";
	static List<VoidVisitor<ArrayList<String>>> enabledVisitorList = null;
	static List<SOEN691OACollector> enabledJDTASTVisitorList = null;	
	
	private ConfigurationLoader() {
	}

	public static void init() throws FileNotFoundException, IOException, URISyntaxException {
		
		if (props == null) {
			props = new Properties();
		}

		String rootPath = new File(
				ConfigurationLoader.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent();
		String fullConfigName = rootPath + File.separatorChar + propFileName;

		FileInputStream is = new FileInputStream(fullConfigName);
		if (is != null) {
			props.load(is);
			is.close();
		}
		buildEnabledVisitorList();
	}

	public static Properties getConfigurationProperties() {
		return props;
	}

	public static void init(String optionValue) throws IOException {
		if (props == null) {
			props = new Properties();
		}
		FileInputStream is = new FileInputStream(optionValue);
		if (is != null) {
			props.load(is);
			is.close();
		}
		buildEnabledVisitorList();
	}
	
	public static List<VoidVisitor<ArrayList<String>>> getEnabledVisitors(){		
		return enabledVisitorList;
		
	}
	
	public static List<SOEN691OACollector> getJDTASTEnabledVisitors() {
		return enabledJDTASTVisitorList;
	}

	private static void buildEnabledVisitorList() {
		
		if (enabledVisitorList == null) {
			enabledVisitorList = new ArrayList<>();
			System.out.println("Creating enabledVisitor ArrayList from configuration file...");
		}
		
		if (enabledJDTASTVisitorList == null) {
			enabledJDTASTVisitorList = new ArrayList<>();
			System.out.println("Creating enabledJDTASTVisitor ArrayList from configuration file...");
		}
		
		String Find_OracleApproximation = props.getProperty("pattern.OracleApproximation","false");
		if(Find_OracleApproximation.equalsIgnoreCase("true")) {
			enabledVisitorList.add(VisitorBuilder.createVisitorForFindingOA());
			//System.out.println("\tEnabling OA " + "Finding Oracle Approximation");
		}
		
	}
}
