package practice;

import com.mxgraph.view.mxGraph;
import javafx.util.Pair;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.util.ArrayList;
import java.util.Collections;

public class MyGraph {
    private ArrayList<ArrayList<Integer>> adjLists; //список смежности
    private  boolean isVisited[];                   //массив посещенных

    private ArrayList<Integer> history;             //порядок посещения
    private ArrayList<Integer> used;                //порядок использования
    private int[] renumbered;                       //перенумированы
    private ArrayList<Integer> topSorted;           //отсортированы

    private Integer[][] data;   //массив данных таблицы (состоит из 4х предыдущих массивов)

    private ArrayList<Pair<Integer,Integer>> edgesDfs;
    private mxGraph myGraph;
    private Object[] points;

    public MyGraph(int n) {
        initGraph(n);
    }

    /*
     * Инициализация используемых массивов и коллекций
     */
    public void initGraph(int n) {
        isVisited = new boolean[n];

        history = new ArrayList<Integer>(n);
        used = new ArrayList<Integer>(n);
        renumbered = new int[n];
        topSorted = new ArrayList<Integer>(n);
        for(int i = 0; i < n; i++) topSorted.add(0);

        adjLists = new ArrayList<ArrayList<Integer>>();
        data=new Integer[4][n];
        edgesDfs = new ArrayList<Pair<Integer, Integer>>();

        for(int v=0; v<n; v++){
            adjLists.add(new ArrayList<Integer>());
        }

        System.out.println("init, size: "+adjLists.size());
    }


    /*
     * Проверить корректность ребра
     */
    public void checkEdge(int from, int to) throws IllegalArgumentException{

        StringBuilder wrongVertex=new StringBuilder("");
        if(from==-1||from>=adjLists.size()) wrongVertex.append(" " + (from+1));
        if(to==-1||to>=adjLists.size()) wrongVertex.append(" " + (to+1));
        if(!wrongVertex.toString().equals("")) throw new IllegalArgumentException("Wrong vertex:" + wrongVertex.toString());

        if(from==to) throw new IllegalArgumentException("Loops aren't allowed");

        if(adjLists.get(from).contains(to)) throw new IllegalArgumentException("Edge " + (from+1) + "-" + (to+1)+" already exist");

        if(adjLists.get(to).contains(from)) throw new IllegalArgumentException("Reverse edge");

        //if цикл

        createEdge(from,to);
    }

    /*
     * Создать ребро
     */
    public void createEdge(int from, int to) {
        adjLists.get(from).add(to);
        System.out.println("edge " + (from+1) + "-" + (to+1) + " created");
    }


    /*
     * Отсортировать список смежности и запустить ПВГ
     */
    public void startDfs(){
        for (int i = 0; i < adjLists.size(); i++) {
            Collections.sort(adjLists.get(i));
        }
        printList();
        for (int i = 0; i < adjLists.size(); i++) {
            if(!isVisited[i]){
                dfs(i);
            }
        }
        System.out.print("\nDFS: ");
        for (int i = 0; i < history.size(); i++) {
            System.out.print(history.indexOf(i) + " ");
        }
    }

    /*
     * ПВГ, начатый из вершины v
     */
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

    /*
     * Топологическая сортировка
     */
    public void topSorting(){

        for (int i = 0; i < adjLists.size(); i++) {
            data[0][i]=history.indexOf(i)+1;
            data[1][i]=used.indexOf(i)+1;
            renumbered[i]=adjLists.size()-used.indexOf(i)-1;
            data[2][i]=renumbered[i]+1;
        }

        for (int i = 0; i < adjLists.size(); i++) {
            topSorted.set(renumbered[i],i);
            data[3][renumbered[i]]=i+1;
        }

    }

    /*
     * Построить таблицу данных
     */
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

    /*
     * Вывести список смежности в консоль (вспомогательное)
     */
    public void printList() {
        for (int i = 0; i < adjLists.size(); i++) {
            System.out.print(i + ": ");
            for (int j = 0; j < adjLists.get(i).size(); j++) {
                System.out.print(adjLists.get(i).get(j) + " ");
            }
            System.out.println("");
        }
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

    public ArrayList<Integer> getTopSorted() {
        return topSorted;
    }

    public mxGraph getMyGraph() {
        return myGraph;
    }


    public Object[] getPoints() {
        return points;
    }

    public void setMyGraph(mxGraph myGraph) {
        this.myGraph = myGraph;
    }

    public void setPoints(Object[] points) {
        this.points = points;
    }

}

