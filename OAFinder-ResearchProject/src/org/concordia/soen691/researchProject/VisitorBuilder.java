package org.concordia.soen691.researchProject;

import java.util.ArrayList;
import com.github.javaparser.ast.visitor.VoidVisitor;

/*
 * @author Manik Hossain
 * @version 1.0
 */

public class VisitorBuilder {
	private VisitorBuilder() {}
	public static VoidVisitor<ArrayList<String>> createVisitorForFindingOA() {
		return new FindingOracleApproximation();
	}
}
