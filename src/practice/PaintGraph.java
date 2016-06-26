package practice;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxEdgeStyle;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxStylesheet;
import com.sun.org.apache.xerces.internal.dom.PSVIAttrNSImpl;
import javafx.util.Pair;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Hashtable;

public class PaintGraph extends JPanel {

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
//        for (int i = 0; i < adjacencyList.size(); i++) {
//            for (int j = 0; j < adjacencyList.get(i).size(); j++) {
//                graph.insertEdge(parent, null, null, points[i], points[adjacencyList.get(i).get(j)]);
//            }
//        }


        for (int i = 0; i < edgesDfs.size(); i++) {
            graph.insertEdge(parent, null, null, points[edgesDfs.get(i).getKey()], points[edgesDfs.get(i).getValue()], "strokeColor=red");
        }
        graph.getModel().endUpdate();

        mxGraphComponent graphComponent = new mxGraphComponent(graph);
        this.add(graphComponent);
        this.revalidate();
    }

    public void drawGraphDfsWithSteps(MyGraph g) {
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
            points[i] = graph.insertVertex(parent, null, i, 300 + r * Math.cos(phi0), 300 + r * Math.sin(phi0), 40, 40, "shape=ellipse;strokeColor=green;fillColor=white");
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

        for (int i = 0; i < edgesDfs.size(); i++) {

            graph.getModel().setValue(points[history.get(i)],graph.getModel().getValue(points[history.get(i)]).toString() + "(" + (i+1) + ")");
            graph.insertEdge(parent, null, null, points[edgesDfs.get(i).getKey()], points[edgesDfs.get(i).getValue()], "strokeColor=red");
            try {

                this.revalidate();
                Thread.sleep(2000);    //1000 milliseconds is one second.

            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }

    }


//    public static void main(String[] args) {
//
//        MyGraph g = new MyGraph(7);
//
//        g.createEdge(0, 1);
//        g.createEdge(0, 2);
//        g.createEdge(0, 3);
//
//        g.createEdge(1, 5);
//        g.createEdge(1, 6);
//
//        g.createEdge(2, 4);
//
//        g.createEdge(3, 2);
//        g.createEdge(3, 4);
//
//        g.createEdge(4, 1);
//
//        g.createEdge(6, 4);
//
//        g.dfs(0);
//
//        PaintGraph paintGraph = new PaintGraph();
//        JFrame frame = new JFrame();
//        frame.add(paintGraph);
//        frame.setSize(900, 900);
//        frame.setVisible(true);
//        System.out.println();
//        paintGraph.drawGraph(g);
//        try {
//            Thread.sleep(2000);                 //1000 milliseconds is one second.
//        } catch (InterruptedException ex) {
//            Thread.currentThread().interrupt();
//        }
//        paintGraph.drawGraphDfsWithSteps(g);
//
//    }
}
