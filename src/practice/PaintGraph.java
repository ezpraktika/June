package practice;

import com.mxgraph.swing.mxGraphComponent;

import com.mxgraph.util.mxEventSource;
import com.mxgraph.view.mxGraph;

import javafx.util.Pair;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.concurrent.FutureTask;

public class PaintGraph extends JPanel {

    public int getStepNumber() {
        return stepNumber;
    }

    public void setStepNumber(int stepNumber) {
        this.stepNumber = stepNumber;
    }

    private int stepNumber = 0;

    public PaintGraph() {

    }

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

    public void drawGraphDfsWithSteps(final MyGraph g) {


        removeAll();
        this.setSize(600, 600);
        ArrayList<ArrayList<Integer>> adjacencyList = g.getAdjLists();

        final mxGraph graph = new mxGraph();
        final Object parent = graph.getDefaultParent();

        graph.setCellsMovable(false);
        graph.setCellsSelectable(false);

        graph.getModel().beginUpdate();

        int n = adjacencyList.size();
        final Object points[] = new Object[n];

        double phi0 = 0;
        double phi = 2 * Math.PI / n;
        int r = 290;

        for (int i = 0; i < points.length; i++) {
            points[i] = graph.insertVertex(parent, null, i + 1, 300 + r * Math.cos(phi0), 300 + r * Math.sin(phi0), 40, 40, "shape=ellipse;fillColor=#500050;fontColor=#FFFFFF");
            phi0 += phi;
        }

        g.setPoints(points);

        for (int i = 0; i < adjacencyList.size(); i++) {
            for (int j = 0; j < adjacencyList.get(i).size(); j++) {
                graph.insertEdge(parent, null, null, points[i], points[adjacencyList.get(i).get(j)]);
            }
        }

        graph.getModel().endUpdate();

        g.setMyGraph(graph);
        mxGraphComponent graphComponent = new mxGraphComponent(graph);
        graphComponent.setConnectable(false);
        this.add(graphComponent);

        this.revalidate();

    }

    public void drawStep(MyGraph g) {

        ArrayList<Integer> history = g.getHistory();
        ArrayList<Pair<Integer, Integer>> edgesDfs = g.getEdgesDfs();
        Object parent = g.getMyGraph().getDefaultParent();
        mxGraph graph = g.getMyGraph();
        graph.getModel().beginUpdate();
        if (stepNumber == 0) {
            graph.getModel().setValue(g.getPoints()[history.get(0)], graph.getModel().getValue(g.getPoints()[history.get(0)]).toString() + "(1)");
        } else {

            for (int i = 0; i < history.get(stepNumber); i++) {
                if (edgesDfs.contains(new Pair<Integer, Integer>(i, history.get(stepNumber)))) {
                    graph.getModel().setStyle(graph.getEdgesBetween(g.getPoints()[i], g.getPoints()[history.get(stepNumber)])[0], "strokeColor=red");
                    break;
                }
            }

            graph.getModel().setValue(g.getPoints()[history.get(stepNumber)], graph.getModel().getValue(g.getPoints()[history.get(stepNumber)]).toString() + "(" + (stepNumber + 1) + ")");
        }

        graph.getModel().endUpdate();
        this.revalidate();
        stepNumber++;
    }

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

        points[0] = graph.insertVertex(parent, null, (g.getTopSorted().get(0) + 1), 300 + r * Math.cos(phi0), 300 + r * Math.sin(phi0), 40, 40, "shape=ellipse;fillColor=#FF0000;fontColor=#FFFFFF");
        phi0 += phi;
        for (int i = 1; i < points.length; i++) {
            points[i] = graph.insertVertex(parent, null, (g.getTopSorted().get(i) + 1), 300 + r * Math.cos(phi0), 300 + r * Math.sin(phi0), 40, 40, "shape=ellipse;fillColor=#500050;fontColor=#FFFFFF");
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

}
