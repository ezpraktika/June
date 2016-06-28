package practice;

import com.mxgraph.swing.mxGraphComponent;

import com.mxgraph.view.mxGraph;

import javafx.util.Pair;

import javax.swing.*;

import java.awt.*;

import java.util.ArrayList;

/**
 * Класс, реализующий различные варианты изображения графа
 */
public class PaintGraph extends JPanel {


    private int stepNumber = 0;

    public PaintGraph() {

    }

    /**
     * Фукнция для построения графа на основе списка смежности.
     *
     * @param g используемый граф
     */
    public void drawGraph(MyGraph g) {
        removeAll();
        ArrayList<ArrayList<Integer>> adjacencyList = g.getAdjLists();
        this.setSize(600, 600);
        mxGraph graph = new mxGraph();
        Object parent = graph.getDefaultParent();
        graph.getModel().beginUpdate();
        int n = adjacencyList.size();
        Object points[] = new Object[n];

        double phi0 = 0;
        double phi = 2 * Math.PI / n;
        int r = 290;

        for (int i = 0; i < points.length; i++) {
            points[i] = graph.insertVertex(parent, null, i, 300 + r * Math.cos(phi0), 300 + r * Math.sin(phi0), 40, 40, "shape=ellipse;strokeColor=red;fillColor=white");
            phi0 += phi;
        }
        for (int i = 0; i < adjacencyList.size(); i++) {
            for (int j = 0; j < adjacencyList.get(i).size(); j++) {
                graph.insertEdge(parent, null, null, points[i], points[adjacencyList.get(i).get(j)]);
            }
        }
        graph.getModel().endUpdate();

        mxGraphComponent graphComponent = new mxGraphComponent(graph);
        this.add(graphComponent);
        this.revalidate();
    }

    /**
     * Построение финального графа поиска в глубину.
     *
     * @param g используемый граф
     */
    public void drawGraphWithDfs(MyGraph g) {
        removeAll();
        ArrayList<ArrayList<Integer>> adjacencyList = g.getAdjLists();
        ArrayList<Integer> history = g.getHistory();
        ArrayList<Pair<Integer, Integer>> edgesDfs = g.getEdgesDfs();
        this.setSize(600, 600);
        mxGraph graph = new mxGraph();
        Object parent = graph.getDefaultParent();
        graph.getModel().beginUpdate();
        int n = adjacencyList.size();
        Object points[] = new Object[n];

        double phi0 = 0;
        double phi = 2 * Math.PI / n;
        int r = 290;

        for (int i = 0; i < points.length; i++) {
            points[i] = graph.insertVertex(parent, null, i + "(" + (history.indexOf(i) + 1) + ")", 300 + r * Math.cos(phi0), 300 + r * Math.sin(phi0), 40, 40, "shape=ellipse;strokeColor=red;fillColor=white");
            phi0 += phi;
        }


        for (int i = 0; i < edgesDfs.size(); i++) {
            graph.insertEdge(parent, null, null, points[edgesDfs.get(i).getKey()], points[edgesDfs.get(i).getValue()], "strokeColor=red");
        }
        graph.getModel().endUpdate();

        mxGraphComponent graphComponent = new mxGraphComponent(graph);
        this.add(graphComponent);
        this.revalidate();
    }


    /**
     * Функция, которая строит изначальный граф и производит инициализацию для пошагового отображения поиска в глубину
     *
     * @param g используемый граф
     */
    public void drawGraphDfsWithSteps(final MyGraph g) {


        removeAll(); // Очистка панели для постороения нового графа
        this.setSize(600, 600);
        ArrayList<ArrayList<Integer>> adjacencyList = g.getAdjLists(); // Список смежности

        final mxGraph graph = new mxGraph();
        final Object parent = graph.getDefaultParent();

        graph.setCellsMovable(false);
        graph.setCellsSelectable(false);

        graph.getModel().beginUpdate(); // Начало создания модели графа

        int n = adjacencyList.size(); // Количество вершин
        final Object points[] = new Object[n]; // Массив вершин

        double phi0 = 0; // Начальный угол
        double phi = 2 * Math.PI / n; // Угол смещения
        int r = 290; // Радиус

        for (int i = 0; i < points.length; i++) {
            points[i] = graph.insertVertex(parent, null, i + 1, 300 + r * Math.cos(phi0), 300 + r * Math.sin(phi0), 40, 40, "shape=ellipse;fillColor=#f1f1f1;fontColor=#000000"); // Создание точек
            phi0 += phi;
        }

        g.setPoints(points); // Добавляем полученный точки в поле исходного графа

        for (int i = 0; i < adjacencyList.size(); i++) {
            for (int j = 0; j < adjacencyList.get(i).size(); j++) {
                graph.insertEdge(parent, null, null, points[i], points[adjacencyList.get(i).get(j)]); // Создание ребер между вершинами на основе списка смежности
            }
        }

        graph.getModel().endUpdate(); // Завершение создания модели графа

        g.setMyGraph(graph); // Добавляем полученную модель в поле исходного графа
        mxGraphComponent graphComponent = new mxGraphComponent(graph);
        graphComponent.setConnectable(false);
        this.add(graphComponent); // Добавляем граф в панель

        this.revalidate(); // Обновляем содержимое панели

    }

    /**
     * Функция для построения очередного шага поиска в глубину
     *
     * @param g используемый граф
     */
    public void drawStep(MyGraph g) {

        ArrayList<Integer> history = g.getHistory();
        ArrayList<Pair<Integer, Integer>> edgesDfs = g.getEdgesDfs();
        Object parent = g.getMyGraph().getDefaultParent();
        mxGraph graph = g.getMyGraph();
        graph.getModel().beginUpdate(); // Начинаем изменение графа
        if (stepNumber == 0) { // Условие для стартовой вершины
            graph.getModel().setValue(g.getPoints()[history.get(0)], graph.getModel().getValue(g.getPoints()[history.get(0)]).toString() + "(1)");
        } else {

            for (int i = 0; i < history.size(); i++) {
                if (edgesDfs.contains(new Pair<Integer, Integer>(i, history.get(stepNumber)))) {
                    graph.getModel().setStyle(graph.getEdgesBetween(g.getPoints()[i], g.getPoints()[history.get(stepNumber)])[0], "strokeColor=red");
                    break;
                }
            }

            graph.getModel().setValue(g.getPoints()[history.get(stepNumber)], graph.getModel().getValue(g.getPoints()[history.get(stepNumber)]).toString() + "(" + (stepNumber + 1) + ")");
        }

        graph.getModel().endUpdate(); // Заканчиваем изменение графа
        this.revalidate();
        stepNumber++;
    }

    /**
     * Функция построения топологически отсортированного графа
     *
     * @param g используемый граф
     */

    public void drawSortedGraph(MyGraph g) {
        removeAll();
        ArrayList<ArrayList<Integer>> adjacencyList = g.getAdjLists();
        this.setSize(600, 600);
        mxGraph graph = new mxGraph();
        Object parent = graph.getDefaultParent();

        graph.setCellsMovable(false);
        graph.setCellsSelectable(false);

        graph.getModel().beginUpdate();
        int n = adjacencyList.size();
        Object points[] = new Object[n];

        double phi0 = 0;
        double phi = 2 * Math.PI / n;
        int r = 290;

        points[0] = graph.insertVertex(parent, null, (g.getTopSorted().get(0) + 1), 300 + r * Math.cos(phi0), 300 + r * Math.sin(phi0), 40, 40, "shape=ellipse;fillColor=#6f8bbd;fontColor=#f1f1f1");
        phi0 += phi;
        for (int i = 1; i < points.length; i++) {
            points[i] = graph.insertVertex(parent, null, (g.getTopSorted().get(i) + 1), 300 + r * Math.cos(phi0), 300 + r * Math.sin(phi0), 40, 40, "shape=ellipse;fillColor=#f1f1f1;fontColor=#000000");
            phi0 += phi;
        }
        for (int i = 0; i < adjacencyList.size(); i++) {
            for (int j = 0; j < adjacencyList.get(g.getTopSorted().get(i)).size(); j++) {
                graph.insertEdge(parent, null, null, points[i], points[g.getTopSorted().indexOf(adjacencyList.get(g.getTopSorted().get(i)).get(j))]);
            }
        }
        graph.getModel().endUpdate();

        mxGraphComponent graphComponent = new mxGraphComponent(graph);
        graphComponent.setConnectable(false);
        this.add(graphComponent);
        this.revalidate();
    }

    public int getStepNumber() {
        return stepNumber;
    }

    public void setStepNumber(int stepNumber) {
        this.stepNumber = stepNumber;
    }
}
