package practice;


import java.util.ArrayList;


public class MyGraph {
    private  boolean isVisited[];                     //массив посещенных
    private ArrayList<Integer> history;             //порядок посещения
    private ArrayList<ArrayList<Integer>> adjLists; //список смежности

    public MyGraph(int n) {
        initGraph(n);
    }

    public void dfs( int v){
        isVisited[v] = true;
        history.add(v);
        System.out.print(v + " ");
        for(int w : adjLists.get(v)){
            if(!isVisited[w]){
                dfs(w);
            }
        }
    }

    public void initGraph(int n) {
        isVisited = new boolean[n];
        history = new ArrayList<Integer>(n);
        adjLists = new ArrayList<ArrayList<Integer>>();

        for(int v=0; v<n; v++){
            adjLists.add(new ArrayList<Integer>());
        }
    }

    public void createEdge(int from, int to) {
        adjLists.get(from).add(to);
    }


}

