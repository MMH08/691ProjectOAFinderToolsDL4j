# Oracle Approximation Finder Tools

This tools will find the oracle approximation function from java deep learning framework (DL4j)
For Example,
 
assertEquals(double expected, double actual, double delta)
Here, delta >= Math.abs( expected – actual ) 
E.g. assertEquals(expected, actual, 1e-5);

Therefore, this tool will find this kind of oracle approximation function

In DL4j developers mostly used assertEquals functions using 3 or 4 parameters
Using this tool, we found 4 different types of oracle approximation functions for test cases, whereas other frameworks in Python such as TensorFlow, developer use around 14 different kind of oracle approximation functions for test cases.

In summary, this will find the oracle approximation function from java deep learning framework (DL4j) and finally export those oracle approximation functions into .csv file.


# Oracle Approximation Function labelling
- Online doc: 
https://docs.google.com/spreadsheets/d/1sDx5aXX83nEDAM78tQ-rgDYfjNQgxgYzgzxKXx8DaaA/edit#gid=38151563
