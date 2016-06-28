package practice;

import com.mxgraph.view.mxGraph;
import javafx.util.Pair;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Класс, реализующий граф и методы работы с ним
 */
public class MyGraph {

    private ArrayList<ArrayList<Integer>> adjLists; //список смежности

    private  boolean isVisited[];       //массив посещенных

    private ArrayList<Integer> history; //порядок посещения вершин
    private ArrayList<Integer> used;    //порядок использования вершин
    private int[] renumbered;           //перенумерованные вершины
    private ArrayList<Integer> topSorted;     //отсортированные вершины

    private Integer[][] data;   //массив данных таблицы (состоит из 4х предыдущих массивов)

    private ArrayList<Pair<Integer,Integer>> edgesDfs; //ребра,составляющие ПВГ

    private mxGraph myGraph;
    private Object[] points;
    private Color[] coloredVertex;

    /**
     * для реализации поиска циклов:
     * white - непосещенные вершины,
     * grey - посещенные,
     * black - использованные
     */
    enum Color {
        White, Grey, Black
    }

    /**
     * Конструктор
     * @param n количество вершин в графе
     */
    public MyGraph(int n) {
        initGraph(n);
    }

    /**
     * инициализация массивов и коллекций используемых
     * @param n количество вершин в графе
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

        coloredVertex = new Color[n];
        for (int i = 0; i < n; i++) {
            coloredVertex[i] = Color.White;
        }

        for(int v=0; v<n; v++){
            adjLists.add(new ArrayList<Integer>());
        }
    }


    /**
     * Проверка корректности ребра
     * @param from вершина, из которой выходит ребро
     * @param to вершина, в которую входит ребро
     * @throws IllegalArgumentException в случае некорректного ребра
     */
    public void checkEdge(int from, int to) throws IllegalArgumentException{

        //проверка корректности вершин
        StringBuilder wrongVertex=new StringBuilder("");
        if(from==-1||from>=adjLists.size()) wrongVertex.append(" " + (from+1));
        if(to==-1||to>=adjLists.size()) wrongVertex.append(" " + (to+1));
        if(!wrongVertex.toString().equals("")) throw new IllegalArgumentException("Wrong vertex:" + wrongVertex.toString());

        //проверка наличия петли
        if(from==to) throw new IllegalArgumentException("Loops aren't allowed");

        //проверка ввода уже существующего ребра
        if(adjLists.get(from).contains(to)) throw new IllegalArgumentException("Edge " + (from+1) + "-" + (to+1)+" already exist");

        //проверка ввода ребра, обратного одному из ранее введенных
        if(adjLists.get(to).contains(from)) throw new IllegalArgumentException("Reverse edge");

        //проверка зацикливания
        if (isCycled(from, to)) throw new IllegalArgumentException("Edge " + (from+1) + " - " + (to+1) + " forms a loop");

        //если все проверки пройдены, создается ребро
        createEdge(from,to);
    }

    /**
     * Создание ребра
     * @param from вершина, из которой выходит ребро
     * @param to вершина, в которую входит ребро
     */
    public void createEdge(int from, int to) {
        adjLists.get(from).add(to);
    }

    /**
     * Проверка зацикливания графа после ввода ребра
     * @param from вершина, из которой выходит ребро
     * @param to вершина, в которую входит ребро
     * @return <tt>true</tt>, если граф зацикливается
     */
    private boolean isCycled(int from, int to){
        createEdge(from,to);                            //добавляем ребро
        for (ArrayList<Integer> adjList : adjLists) {     //сортируем список смежности
            Collections.sort(adjList);
        }
        for (int i = 0; i < adjLists.size(); i++) {     //для каждой
            if(!isVisited[i]){                          //непосещенной вершины
                if(coloredDfs(i)){                      //выполняем модифицированный ПВГ и если цикл найден
                    for (int j = 0; j < adjLists.size(); j++) {    //окрашиваем все вершины в белый (для последующих проверок)
                        coloredVertex[j]=Color.White;
                    }
                    adjLists.get(from).remove(new Integer(to)); //удаляем проверяемое ребро
                    return true;
                }
            }
        }

        //сюда попадаем если цикл не найден
        for (int j = 0; j < adjLists.size(); j++) {     //окрашиваем все вершины в белый (для последующих проверок)
            coloredVertex[j]=Color.White;
        }
        adjLists.get(from).remove(new Integer(to));     //удаляем проверяемое ребро
        return false;
    }

    /**
     * Поиск циклов (модифицированный ПВГ)
     * @param v - вершина, откуда начинается ПВГ
     * @return <tt>true</tt>, если найден цикл
     */
    private boolean coloredDfs(int v){
        coloredVertex[v] = Color.Grey;  //вершина посещена
        for(int w : adjLists.get(v)) {
            if (coloredVertex[w] == Color.White)    //если вершина непосещена, переходим в нее
                if(coloredDfs(w)) return true;
            if (coloredVertex[w] == Color.Grey)     //если наткнулись на посещенную вершину - цикл найден
                return true;              // вывод ответа
        }
        coloredVertex[v] = Color.Black;    //вершина использована
        return false;   //при ПВГ из данной верщины цикл не найден
    }

    /**
     * ПВГ
     */
    public void startDfs(){
        //printList();
        for (int i = 0; i < adjLists.size(); i++) {
            if(!isVisited[i]){
                dfs(i);
            }
        }
//        System.out.print("\nDFS: ");
//        for (int i = 0; i < history.size(); i++) {
//            System.out.print(history.indexOf(i) + " ");
//        }
    }

    /**
     * Шаг ПВГ
     * @param v вершина, из которой выполняется ПВГ
     */
    private void dfs(int v){
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

    /**
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

    /**
     * Создание таблицы данных
     * @return таблица, построенная по данным графа
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
    private void printList() {
        for (int i = 0; i < adjLists.size(); i++) {
            System.out.print(i + ": ");
            for (int j = 0; j < adjLists.get(i).size(); j++) {
                System.out.print(adjLists.get(i).get(j) + " ");
            }
            System.out.println("");
        }
    }

    /*
     * Getters/Setters
     */
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

