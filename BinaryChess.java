/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package binarychess;
import java.util.Random;

public class BinaryChess{
	Random rand = new Random();
	GraphReader read = new GraphReader();
	int[] xPositionList;
	int[] yPositionList;
	int[][] nodeArray;
	boolean[] seenBefore;
    public BinaryChess(){	
    	read.readGraph();
    	xPositionList = new int[read.getSize()];
    	yPositionList = new int[read.getSize()];
    	nodeArray = read.adjMatrix.clone();
    	seenBefore = new boolean[read.getSize()];
    	
    	//System.out.println("random test");
    	for(int i = 0; i < xPositionList.length; i++){
    		xPositionList[i] = rand.nextInt(601)+20;
    	}
    	
    	for(int i = 0; i < yPositionList.length; i++){
    		yPositionList[i] = rand.nextInt(441)+20;
    	}
    	
	}	 
    
    public void setSeenBefore(int index){
    	seenBefore[index] = true;
    }
}    