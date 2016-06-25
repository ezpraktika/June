package practice;

import com.mxgraph.view.mxGraph;

import javax.swing.*;
import java.util.ArrayList;

/**
 * Created by n on 25.06.2016.
 */
public class PaintGraph extends JPanel{
    public PaintGraph(ArrayList<ArrayList<Integer>> adjacencyList) {

        this.setSize(800,800);
        mxGraph graph = new mxGraph();
        Object parent = graph.getDefaultParent();

        int n = adjacencyList.size();
        Object points[] = new Object[n];

        double phi0 = 0;
        double phi = 2 * Math.PI / n;
        int r = 300;
        for (int i = 0; i < points.length; i++) {
            points[i] = graph.insertVertex(parent, null, i+1, 400 + r * Math.cos(phi0), 400 + r * Math.sin(phi0), 20, 20);
            phi0 += phi;
        }
            /////f/qfqfq
//        for(int i = 0; i < adjacencyList.size(); i++){
//            for(int j = 0; j < adjacencyList[i].)
//        }
    }
}
