package org.concordia.soen691.researchProject;


import java.util.ArrayList;
import java.util.Arrays;

import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;



public class FindingOracleApproximation extends VoidVisitorAdapter<ArrayList<String>>{
	public FindingOracleApproximation() {}
	
	
	public void visit(MethodCallExpr OAMethodExpr, ArrayList<String> OAcollector) {

		String[] functionFormats = {">","<",">=","<="};
		String getType = "";
		boolean addToList = false;
		if(OAMethodExpr.toString().contains("assert") && OAMethodExpr.getArguments().size() >= 3) {

			boolean isOA = checkOracleApproximationFunction(OAMethodExpr.getNameAsString().toString()); 
			if(isOA) {
				
				if(OAMethodExpr.toString().contains("assert") && OAMethodExpr.getArguments().size() == 3) {
					try {
					 // removing some unwanted function which looks like Oracle Approximation functions 	
					 getType = OAMethodExpr.getArgument(0).calculateResolvedType().describe();
					 if(!getType.contains("String")) {
						 addToList = true;
						 }
					 else addToList = false;
					 
					}catch (Exception e) {
						// removing some unwanted function which looks like Oracle Approximation functions 
						if(OAMethodExpr.getArgument(0).toString().contains("getCount")) {
							addToList = false;
						}
						else addToList=true;
					}
					
					if(addToList) {
						OAcollector.add(OAMethodExpr.getNameAsString().toString() +","+ OAMethodExpr.getBegin().get().line);
						}
					
				}
				// if it contains more than 3 Arguments, it certainly Oracle approximation with proper DELTA value 
				else if(OAMethodExpr.toString().contains("assert") && OAMethodExpr.getArguments().size() > 3) {
					OAcollector.add(OAMethodExpr.getNameAsString().toString() +","+ OAMethodExpr.getBegin().get().line);
				}
			 }
			
		}
		//Handle assertTrue differently because this functions contains one Argument and compare the value with constant 
		//value using this expression ">","<",">=","<="
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
	
	//Removing some unwanted functions which looks like Oracle Approximations
	private boolean checkOracleApproximationFunction(String functionsName) {
			boolean flag = false; 
			String[] unwantedFunctions = {"assertCharacterCategories","assertExpectedNumberMachineIdsJvmIdsThreadIds","assertMultiThreadedTokenizedStreamEquals",
					"assertNInNOutSet","assertTokenizedStreamEquals","assertSupported"};
			if((Arrays.asList(unwantedFunctions).contains(functionsName))) {
				flag = false;
			}
			else flag = true;

			return flag;
		}
}
