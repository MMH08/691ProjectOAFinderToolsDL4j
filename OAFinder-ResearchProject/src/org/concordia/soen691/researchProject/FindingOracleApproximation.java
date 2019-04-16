package org.concordia.soen691.researchProject;


import java.util.ArrayList;
import java.util.Arrays;

import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;



public class FindingOracleApproximation extends VoidVisitorAdapter<ArrayList<String>>{
	public FindingOracleApproximation() {}
	
	
	public void visit(MethodCallExpr OAMethodExpr, ArrayList<String> OAcollector) {

		String[] functionFormats = {">","<",">=","<="};
		if(OAMethodExpr.toString().contains("assert") && OAMethodExpr.getArguments().size() >= 3) {
			boolean isOA = checkOracleApproximationFunction(OAMethodExpr.getNameAsString().toString()); 
			if(isOA) {
			OAcollector.add(OAMethodExpr.getNameAsString().toString() +","+ OAMethodExpr.getBegin().get().line);
			}
		}
		else if(  OAMethodExpr.toString().contains("assertTrue")) {
			for (String s : functionFormats)
			{
			  if (OAMethodExpr.toString().contains(s))
			  {
				  OAcollector.add(OAMethodExpr.getNameAsString().toString() +","+ OAMethodExpr.getBegin().get().line);
				break;
			  }
			}
		}
	}
	
	//Remove some unwanted OA
	private boolean checkOracleApproximationFunction(String functionsName) {
			boolean flag = false; 
			String[] unwantedFunctions = {"assertCharacterCategories","assertExpectedNumberMachineIdsJvmIdsThreadIds","assertMultiThreadedTokenizedStreamEquals",
					"assertNInNOutSet","assertTokenizedStreamEquals"};
			if((Arrays.asList(unwantedFunctions).contains(functionsName))) {
				flag = false;
			}
			else flag = true;

			return flag;
		}
}
