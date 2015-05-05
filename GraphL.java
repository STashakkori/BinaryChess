package binarychess;

import java.util.*;
import java.lang.*;

public class GraphL 
{ 
    private ArrayList <LinkedList<Integer> > adjList;
    private int numVertices;
    private int numEdges;

    public GraphL()
    {
         numVertices = 0;
         numEdges = 0;
         adjList = null;
    }

    public GraphL (int num)
    {
         numVertices = num;
         numEdges = 0;
         adjList = new ArrayList <LinkedList<Integer> >();
         for (int i=0; i < num; i++)
             adjList.add(new LinkedList<Integer>());
    }

    public void resize(int num)
    {
         if (num > numVertices)
         {
           ArrayList <LinkedList<Integer> > oldList = adjList;
           adjList = new ArrayList <LinkedList<Integer> >();
           for (int i=0; i < numVertices; i++) {
                adjList.add(oldList.get(i));
           }
           for (int i=numVertices; i < num; i++) {
             adjList.add(new LinkedList<Integer>());
           }
           numVertices = num;
           oldList = null;
         }
    }
    
    public int order()
    {
        return numVertices;
    }

    public int size()
    {
        return numEdges;
    }

    public void addEdge(int i,  int j)
    {
          adjList.get(i).add(j);
          adjList.get(j).add(i);
    }

    public ListIterator<Integer> neighbor(int i)
    {
        return adjList.get(i).listIterator();
    }
}
