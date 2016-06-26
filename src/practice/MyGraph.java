package practice;


import javafx.util.Pair;

import java.util.ArrayList;


public class MyGraph {
    private  boolean isVisited[];                     //массив посещенных
    private ArrayList<Integer> history;             //порядок посещения
    private ArrayList<Integer> used;                //порядок использования
    private ArrayList<ArrayList<Integer>> adjLists; //список смежности
    private ArrayList<Pair<Integer,Integer>> edgesDfs;

    public MyGraph(int n) {
        initGraph(n);
    }


    public ArrayList<Pair<Integer, Integer>> getEdgesDfs() {
        return edgesDfs;
    }

    public void dfs(int v){
        isVisited[v] = true;
        history.add(v);
        for(int w : adjLists.get(v)){
            if(!isVisited[w]){
                edgesDfs.add(new Pair(v,w));
                dfs(w);
            }
        }
        used.add(v);


    }

    public void initGraph(int n) {
        isVisited = new boolean[n];
        history = new ArrayList<Integer>(n);
        used = new ArrayList<Integer>(n);
        adjLists = new ArrayList<ArrayList<Integer>>();
        edgesDfs = new ArrayList<Pair<Integer, Integer>>();

        for(int v=0; v<n; v++){
            adjLists.add(new ArrayList<Integer>());
        }
    }

    public void createEdge(int from, int to) {
        adjLists.get(from).add(to);
    }

    public ArrayList<ArrayList<Integer>> getAdjLists() {
        return adjLists;
    }

    public ArrayList<Integer> getHistory() {
        return history;
    }

    public ArrayList<Integer> getUsed() {
        return used;
    }

}

