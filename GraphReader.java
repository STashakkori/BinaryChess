/* To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package binarychess;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * @author Sina Tashakkori
 * -with use of graphical components of GraphPanel open source library
 */
public class GraphReader {	
	int[][] adjMatrix;
	int[][] tempAdjMat;
	Scanner sc = null;
	int numVertices;
    public GraphReader(){    
	    try {
	        sc = new Scanner(new File("textFile.txt"));
	    } 
	    catch (FileNotFoundException e) {
	        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
	    }
	    numVertices = sc.nextInt();
	    System.out.println("There are " + numVertices + " vertices in this graph.");
	    tempAdjMat = new int[(numVertices*(numVertices-1)/2)][2];
	    for(int i = 0; i < tempAdjMat.length; i++){
	    	for(int j = 0; j < tempAdjMat[i].length; j++){
	    		tempAdjMat[i][j] = -1;
	    	}
	    }
	}
	public void readGraph(){
	    int vertex = sc.nextInt();
	    int count = 0;
	    int index = 0;
	    System.out.println();
	    while (vertex!= -1){
	    	if(count%2==0){
	    		tempAdjMat[index][0] = vertex;
	    		vertex = sc.nextInt();
	    		count++;
	    	}
	    	else{
	    		tempAdjMat[index][1] = vertex;
	    		vertex = sc.nextInt();
	    		count++;
	    		index++;
	    	}
	    }
	    adjMatrix = new int[index][2];
	    for(int i = 0; i < index; i++){
	    	for(int j = 0; j < adjMatrix[i].length; j++){
	    		adjMatrix[i][j] = tempAdjMat[i][j];
	    	}
	    }
	    System.out.println();
		System.out.println("Testing Graph Reading..");
		System.out.print("------------------------");
		for(int i = 0; i < adjMatrix.length; i++){
			System.out.println();
		    for(int j = 0; j < adjMatrix[i].length; j++){
		    	System.out.print(adjMatrix[i][j] + " ");
		    }
		}
		System.out.println("\n\nGraph Reading passed");
	}
	public int getSize(){
		return numVertices;
	}	
}
