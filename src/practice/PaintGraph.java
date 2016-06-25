package practice;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxStylesheet;
import com.sun.org.apache.xerces.internal.dom.PSVIAttrNSImpl;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Created by n on 25.06.2016.
 */
public class PaintGraph extends JPanel{
    public PaintGraph(MyGraph g) {
        ArrayList<ArrayList<Integer>> adjacencyList = g.getAdjLists();
        ArrayList<Integer> history = g.getHistory();
        this.setSize(600,600);
        mxGraph graph = new mxGraph();
        Object parent = graph.getDefaultParent();
        graph.getModel().beginUpdate();
        int n = adjacencyList.size();
        Object points[] = new Object[n];

        double phi0 = 0;
        double phi = 2 * Math.PI / n;
        int r = 290;

        for (int i = 0; i < points.length; i++) {
            points[i] = graph.insertVertex(parent, null, i+"(" + (history.indexOf(i)+1) + ")" , 300 + r * Math.cos(phi0), 300 + r * Math.sin(phi0), 40, 40,"shape=ellipse;strokeColor=red;fillColor=white");
            phi0 += phi;
        }
        for(int i = 0; i < adjacencyList.size(); i++){
            for(int j = 0; j < adjacencyList.get(i).size();j++){
                graph.insertEdge(parent, null, null, points[i], points[adjacencyList.get(i).get(j)]);
            }
        }
        graph.getModel().endUpdate();

        mxGraphComponent graphComponent = new mxGraphComponent(graph);
        this.add(graphComponent);
    }

    public static void main(String[] args) {

        MyGraph g =new MyGraph(7);

        g.createEdge(0,1);
        g.createEdge(0,2);
        g.createEdge(0,3);

        g.createEdge(1,5);
        g.createEdge(1,6);

        g.createEdge(2,4);

        g.createEdge(3,2);
        g.createEdge(3,4);

        g.createEdge(4,1);

        g.createEdge(6,4);
        g.dfs(0);

        PaintGraph paintGraph = new PaintGraph(g);
        paintGraph.setVisible(true);
        JFrame frame = new JFrame();
        frame.add(paintGraph);
        frame.setSize(900,900);
        frame.setVisible(true);
        System.out.println("");

        for (int w: g.getUsed()
             ) {
            System.out.print(w+" ");
        }

    }
}
