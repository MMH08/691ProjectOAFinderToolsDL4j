package org.concordia.soen691.researchProject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/*
 * @author Manik Hossain
 * @version 1.0
 * <p>
 * This class stores the bug pattern matches for a file. The results are stored in a map where the key is the pattern and the object is an array of strings with the occurrences.
 * </p>
 */

public class OracleApproximationFinderResponse {
	String fileName = "";
	/*
	 * The resultMap has as key the bug pattern name and the object is an array of strings of all the occurrences.
	 */
	Map<String, ArrayList<String>> resultMap = null;
	
	public OracleApproximationFinderResponse() {
		resultMap = new HashMap<>();
	}
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Map<String, ArrayList<String>> getResultMap() {
		return resultMap;
	}
	
	public boolean isEmpty() {
		return resultMap.isEmpty();
	}

}
