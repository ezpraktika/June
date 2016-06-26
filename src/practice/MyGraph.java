package practice;


import javafx.util.Pair;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;


public class MyGraph {
    private  boolean isVisited[];                     //массив посещенных
    private ArrayList<Integer> history;             //порядок посещения
    private ArrayList<Integer> used;                //порядок использования
    private int[] renumbered;                       //перенумированы
    private int[] topSorted;                        //отсортированы
    private ArrayList<ArrayList<Integer>> adjLists; //список смежности
    private Integer[][] data;
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
        System.out.print(v + " ");
        for(int w : adjLists.get(v)){
            if(!isVisited[w]){
                edgesDfs.add(new Pair(v,w));
                dfs(w);
            }
        }
        used.add(v);

    }

    public void startDfs(){
        for (int i = 0; i < adjLists.size(); i++) {
            if(!isVisited[i]){
                dfs(i);
            }
        }
    }

    public void initGraph(int n) {
        isVisited = new boolean[n];
        history = new ArrayList<Integer>(n);
        used = new ArrayList<Integer>(n);
        renumbered = new int[n];
        topSorted = new int[n];
        adjLists = new ArrayList<ArrayList<Integer>>();
        data=new Integer[5][n];
        edgesDfs = new ArrayList<Pair<Integer, Integer>>();


        for(int v=0; v<n; v++){
            adjLists.add(new ArrayList<Integer>());
        }
        System.out.println("init, size: "+adjLists.size());
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

    public void topSorting(){
        System.out.println("");
        for (int i=0; i<adjLists.size();i++) {
            System.out.print(used.indexOf(i)+" ");
            data[2][i]=used.indexOf(i)+1;
        }

        for (int i = 0; i < adjLists.size(); i++) {
            renumbered[i]=adjLists.size()-used.indexOf(i)-1;
            data[3][i]=renumbered[i]+1;
        }
        for (int i = 0; i < adjLists.size(); i++) {
            topSorted[renumbered[i]]=i;
            data[4][renumbered[i]]=i+1;
        }
        System.out.println("");
        for (int v :
                renumbered) {
            System.out.print(v+ " ");
        }
        System.out.println("");
        for (int v :
                topSorted) {
            System.out.print(v+ " ");
        }
    }

    public JTable makeJTable(){
        //useless shit
//        String[] headers = {
//                "v", "nv","fn","sn","tn"
//        };
//        Integer[] help=new Integer[adjLists.size()];
//        for (int i = 0; i < adjLists.size(); i++) {
//            help[i]=i+1;
//        }
//
//
//        JTable table=new JTable(data,help);
//        table.setPreferredSize(new Dimension(10000,600));
//        return table;
    }

}

