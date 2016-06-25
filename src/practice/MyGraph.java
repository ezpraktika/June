package practice;


import java.util.ArrayList;


public class MyGraph {
    private  boolean isVisited[];                     //массив посещенных
    private ArrayList<ArrayList<Integer>> adjLists; //список смежности

    public MyGraph(int n) {
        initGraph(n);
    }

    private void dfs_rec( int v){
        isVisited[v] = true;
        System.out.print(v + " ");
        for(int w : adjLists.get(v)){
            if(!isVisited[w]){
                dfs_rec(w);
            }
        }
    }

    public void initGraph(int n) {
        isVisited = new boolean[n];

        ArrayList<ArrayList<Integer>> adjLists = new ArrayList<ArrayList<Integer>>();

        for(int v=0; v<n; v++){
            adjLists.add(new ArrayList<Integer>());
        }
    }

    public void createEdge(int from, int to) {
        adjLists.get(from).add(to);
    }
}

