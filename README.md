# Oracle Approximation Finder Tools
This tools will find the oracle approximation function from java deep learning framework (DL4j)
For Example,assertEquals(double expected, double actual, double delta). Here, delta >= Math.abs( expected – actual )  E.g. assertEquals(expected, actual, 1e-5). Therefore, this tool will find this kind of oracle approximation function

# This tool findings
- Total number of Oracle Approximation Found: 964		
- Name of the different Oracle Approximation functions = [assertEquals, assertTrue, assertArrayEquals, assertNotEquals]		
- Total number of different Oracle Approximation functions = 4		
- Total #assertNotEquals functions founds = 7		
- Total #assertEquals functions= 789		
- Total #assertTrue functions= 133		
- Total #assertArrayEquals functions= 35		

In DL4j developers mostly used assertEquals functions using 3 or 4 parameters. so, we found 4 different types of oracle approximation functions for test cases, whereas other frameworks in Python such as TensorFlow, developer use around 14 different kind of oracle approximation functions for test cases.

In summary, this tool will find the oracle approximation function from java deep learning framework (DL4j) and finally export those oracle approximation functions into .csv file.


# Oracle Approximation Function Manual Labelling
- Online doc: 
https://docs.google.com/spreadsheets/d/1sDx5aXX83nEDAM78tQ-rgDYfjNQgxgYzgzxKXx8DaaA/edit#gid=38151563
