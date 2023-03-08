# criticality

This program implements an algorithm to efficiently calculate criticality.

# Requirements

This program calls IBM ILOG CPLEX Optimization Studio internally.
To download CPLEX, see [here](https://www.ibm.com/id-en/products/ilog-cplex-optimization-studio).

# Usage

If you do not have JDK installed, install it from
[here](https://www.oracle.com/java/technologies/downloads/#jdk19-windows) and insert the path
so that the &quot;java&quot; command can be used. The path is &quot;criticality/bin&quot;.

Download all class files (files with &quot;class&quot; extension) in the &quot;criticality/bin&quot; folder.

Place the file with the edge list under &quot;... /DATA&quot;.

The network data should be written using an edge list with tab-delimited as follows

v1 v2

v2 v4

v2 v3

v2 v5

v3 v1

v6 v1

Go to the folder containing the class files and enter the following command:

java Main

If it works correctly, you will see &quot;Please enter DATA folder path. Then, enter the path of &quot;...
/DATA&quot;.

The program will then execute the folder path in &quot;... /DATA&quot; and output the results in a folder
named RESULT in the same hierarchy as the DATA.

# Author
Wataru Someya

Toho University

6522009s@st.toho-u.jp
