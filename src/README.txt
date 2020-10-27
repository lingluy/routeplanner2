
## Project Description 

## Implementation
 
In this project, I use my own graph implementation. I have classes for both node and edges. Compared to a generic implementation, my
node classes also store a latitude and longitude, and my edge class uses the Haversine formula to calculate its own weight. As I was
familiar with Dijkstra's algorithm beforehand, implementing it was not too difficult. I used a PriorityQueue based on minimum distance
from the source node. What was difficult was the StackOverFlow errors I got from the PriorityQueue. I solved this by limiting the size of
my PriorityQueue to 100 so it would not overload. It would break out of the while loop if its size was larger than 100. 

Swing is used to draw maps. There are a few challenges.
  1. To convert langtitude and longitude to X and y. I have used a method called  Projection to implement the conversion. 
  2. shrink the map without losing precision.  Scaling Factor is used to shrink the size so it fits in computer screen.


## List of Files:
CoordinateConverter.java:  used to converts coordinates to point on map
DistanceComparator.java Used in the PriorityQueue to find the node with the minimum distance from the source.
Edge.java Edge implementation that also uses Haversine formula
Haversine.java Code from online using Haversine formula to convert latitude/longitude to a distance.
Intersection.java: object holds intersection information
MyMap.java: JFrame object
MyPanel.java:  Jpanel
Node.java Node implementation
PointXY.java:  point object for map
Road.java:  store road info for map
StreetMap.java Driver class that reads file, holds Dijkstra methods, and holds graph.

## Runtime Analysis:
O(V^2)

## How to run the program 
javac StreetMap.java
java StreetMap C:\Users\General\Downloads\project3\p3dataset\p4dataset\ur.txt  --directions GLEASON-HALL GILBERT-LONG  --show
java StreetMap C:\Users\General\Downloads\project3\p3dataset\p4dataset\ur.txt  --show --directions GLEASON-HALL GILBERT-LONG  


## Features:  
-Jframe can be resized and map will resize accordingly.
-map work for all input files.
-Shortest path works well with UR campus.
-out of memory error when input files are too big.
