package practice;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {

    private static MyGraph g;
    private static PaintGraph pg;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    private static void createAndShowGUI() {

        JFrame f=new JFrame("Topological sort");
        JTabbedPane tp = new JTabbedPane();         //окно состоит из 2х вкладок
        JPanel firstPanel=new JPanel();             //на первой граф и кнопки
        final JPanel secondPanel = new JPanel();    //на второй таблица
        secondPanel.setLayout(new BoxLayout(secondPanel,BoxLayout.PAGE_AXIS));

        final JPanel secondUpPanel=new JPanel();           //верхняя половина второй страницы
        secondUpPanel.setLayout(new BorderLayout(2,0));


        /*
         * Кнопки с первой страницы
         */
        final JButton numOfVertexButton = new JButton("ok");            //ввести кол-во вершин
        final JButton createEdgeButton = new JButton("ok");           //ввести ребро
        final JButton importDateButton = new JButton("Import data");   //ввод данных из файла
        final JButton startButton = new JButton("Make graph");        //начать алгоритм
        final JButton showResultsButton = new JButton("Show result");   //сразу показать результат

        /*
         * Панель графа (первая страница)
         */
        pg=new PaintGraph();
        pg.setPreferredSize(new Dimension(650,650));


        /*
         * Панель, отвечающая за ввод кол-ва вершин
         */
        JPanel numLine = new JPanel();
        numLine.setLayout(new BoxLayout(numLine,BoxLayout.LINE_AXIS));

        final JTextField number = new JTextField();
        numOfVertexButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int nu=Integer.parseInt(number.getText());
                createEdgeButton.setEnabled(true);
                g=new MyGraph(nu);
            }
        });
        numLine.add(new JLabel("Number of vertex  "));
        numLine.add(Box.createHorizontalStrut(4));
        numLine.add(number);
        numLine.add(Box.createHorizontalStrut(4));
        numLine.add(numOfVertexButton);

        /*
         * Панель, отвечающая за ввод ребер
         */
        JPanel edgeLine = new JPanel();
        edgeLine.setLayout(new BoxLayout(edgeLine,BoxLayout.LINE_AXIS));
        final JTextField vert1 = new JTextField();
        final JTextField vert2 = new JTextField();

        createEdgeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//
                int e1=Integer.parseInt(vert1.getText());
                int e2=Integer.parseInt(vert2.getText());
                vert1.setText("");
                vert2.setText("");
                System.out.println(e1+" "+e2);
                g.createEdge(e1-1,e2-1);
//                g.createEdge(0,1);
//                g.createEdge(0,2);
//                g.createEdge(0,3);
//                g.createEdge(0,5);
//                g.createEdge(0,6);
//
//                g.createEdge(2,3);
//
//                g.createEdge(3,4);
//                g.createEdge(3,5);
//
//                g.createEdge(4,9);
//
//                g.createEdge(6,4);
//                g.createEdge(6,9);
//
//                g.createEdge(7,6);
//
//                g.createEdge(8,7);
//
//                g.createEdge(9,10);
//                g.createEdge(9,11);
//                g.createEdge(9,12);
//
//                g.createEdge(11,12);
            }
        });

        edgeLine.add(new JLabel("New edge  "));
        edgeLine.add(Box.createHorizontalStrut(4));
        edgeLine.add(vert1);
        edgeLine.add(Box.createHorizontalStrut(4));
        edgeLine.add(vert2);
        edgeLine.add(Box.createHorizontalStrut(4));
        edgeLine.add(createEdgeButton);

        /*
         * Правая верхняя панель со всем интерфейсом
         */
        JPanel rightUpPanel= new JPanel();
        rightUpPanel.setLayout(new GridLayout(5,1,0,3));
        rightUpPanel.add(numLine);
        rightUpPanel.add(edgeLine);
        rightUpPanel.add(importDateButton);
        rightUpPanel.add(startButton);
        rightUpPanel.add(showResultsButton);
        rightUpPanel.setPreferredSize(new Dimension(200,300));


        /*
         * Правая панель целиком
         */
        JPanel rightPanel=new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel,BoxLayout.PAGE_AXIS));
        rightPanel.add(rightUpPanel);
        rightPanel.add(Box.createVerticalStrut(500));
        rightPanel.setPreferredSize(new Dimension(200,600));

        /*
         * Первая страница - граф и кнопки
         */
        firstPanel.setLayout(new BorderLayout(5,0));
        firstPanel.add(pg,BorderLayout.CENTER);
        firstPanel.add(rightPanel,BorderLayout.EAST);

         /*
         * Заголовки для строк таблицы (вторая страница)
         */
        final JPanel headers=new JPanel();
        headers.setLayout(new BoxLayout(headers,BoxLayout.PAGE_AXIS));
        headers.add(Box.createVerticalStrut(3));

        JLabel lab1=new JLabel("Number");
        lab1.setAlignmentX(Component.CENTER_ALIGNMENT);
        headers.add(lab1);

        headers.add(Box.createVerticalStrut(15));

        JLabel lab2=new JLabel("Visited");
        lab2.setAlignmentX(Component.CENTER_ALIGNMENT);
        headers.add(lab2);

        headers.add(Box.createVerticalGlue());

        JLabel lab3=new JLabel("Used");
        lab3.setAlignmentX(Component.CENTER_ALIGNMENT);
        headers.add(lab3);

        headers.add(Box.createVerticalGlue());

        JLabel lab4=new JLabel("Renumb");
        lab4.setAlignmentX(Component.CENTER_ALIGNMENT);
        headers.add(lab4);

        headers.add(Box.createVerticalGlue());

        JLabel lab5=new JLabel("Sorted");
        lab5.setAlignmentX(Component.CENTER_ALIGNMENT);
        headers.add(lab5);

        headers.add(Box.createVerticalStrut(40));

        headers.setPreferredSize(new Dimension(50,200));


        /*
         * Вкладки окна
         */
        tp.addTab("Graph",firstPanel);
        tp.addTab("Table",secondPanel);


        /*
         * ActionListener'ы для кнопок с первой страницы
         */
        importDateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("DOESN'T WORK");
            }
        });

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                g.startDfs();
                pg.drawGraph(g);
                g.topSorting();
                final JScrollPane scrollPane=new JScrollPane(g.makeJTable(),
                        ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER,
                        ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
                scrollPane.setPreferredSize(new Dimension(750,200));
                scrollPane.setColumnHeader(null);
                secondUpPanel.add(headers,BorderLayout.WEST);
                secondUpPanel.add(scrollPane,BorderLayout.CENTER);
                secondPanel.add(secondUpPanel);
                secondPanel.add(Box.createVerticalStrut(450));
            }
        });

        showResultsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("DOESN'T WORK");
            }
        });

        f.add(tp);

        f.pack();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLocationRelativeTo(null);
        f.setResizable(false);
        f.setVisible(true);
    }
}


