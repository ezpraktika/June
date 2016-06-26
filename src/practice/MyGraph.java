package practice;

import com.mxgraph.view.mxGraph;
import javafx.util.Pair;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.text.html.ObjectView;
import java.util.ArrayList;

public class MyGraph {
    private ArrayList<ArrayList<Integer>> adjLists; //список смежности
    private  boolean isVisited[];                   //массив посещенных

    private ArrayList<Integer> history;             //порядок посещения
    private ArrayList<Integer> used;                //порядок использования
    private int[] renumbered;                       //перенумированы

    public mxGraph getMyGraph() {
        return myGraph;
    }

    public void setMyGraph(mxGraph myGraph) {
        this.myGraph = myGraph;
    }

    private mxGraph myGraph;

    public Object[] getPoints() {
        return points;
    }

    public void setPoints(Object[] points) {
        this.points = points;
    }

    private Object[] points;



    private int[] topSorted;                        //отсортированы

    private Integer[][] data;   //массив данных таблицы (состоит из 4х предыдущих массивов)

    private ArrayList<Pair<Integer,Integer>> edgesDfs;

    public MyGraph(int n) {
        initGraph(n);
    }

    public void initGraph(int n) {
        isVisited = new boolean[n];
        history = new ArrayList<Integer>(n);
        used = new ArrayList<Integer>(n);
        renumbered = new int[n];
        topSorted = new int[n];
        adjLists = new ArrayList<ArrayList<Integer>>();
        data=new Integer[4][n];
        edgesDfs = new ArrayList<Pair<Integer, Integer>>();

        for(int v=0; v<n; v++){
            adjLists.add(new ArrayList<Integer>());
        }

        System.out.println("init, size: "+adjLists.size());
    }

    public void createEdge(int from, int to) {
        adjLists.get(from).add(to);
    }

    public void startDfs(){
        for (int i = 0; i < adjLists.size(); i++) {
            if(!isVisited[i]){
                dfs(i);
            }
        }
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

    public void topSorting(){

        for (int i = 0; i < adjLists.size(); i++) {
            data[0][i]=history.indexOf(i)+1;
            data[1][i]=used.indexOf(i)+1;
            renumbered[i]=adjLists.size()-used.indexOf(i)-1;
            data[2][i]=renumbered[i]+1;
        }

        for (int i = 0; i < adjLists.size(); i++) {
            topSorted[renumbered[i]]=i;
            data[3][renumbered[i]]=i+1;
        }

    }

    public JTable makeJTable(){
        //массив заголовков (номера вершин)
        Integer[] help=new Integer[adjLists.size()];
        for (int i = 0; i < adjLists.size(); i++) {
            help[i]=i+1;
        }

        //для центрирования
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        JTable table=new JTable(data,help);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setRowHeight(40);
        for (int i = 0; i < adjLists.size(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        return table;
    }

    public ArrayList<ArrayList<Integer>> getAdjLists() {
        return adjLists;
    }

    public ArrayList<Pair<Integer, Integer>> getEdgesDfs() {
        return edgesDfs;
    }

    public ArrayList<Integer> getHistory() {
        return history;
    }

    public ArrayList<Integer> getUsed() {
        return used;
    }

    public int[] getTopSorted() {
        return topSorted;
    }
}

