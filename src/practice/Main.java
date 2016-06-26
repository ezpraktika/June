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
        JTabbedPane tp = new JTabbedPane();     //окно состоит из 2х вкладок
        JPanel firstPanel=new JPanel();         //на первой граф и кнопки
        final JPanel secondPanel=new JPanel();  //на второй таблица

        /*
         * Используемые кнопки (определены в начале функции из-за ActionListener'ов
         */
        final JButton first = new JButton("ok");            //ввести кол-во вершин
        final JButton second = new JButton("ok");           //ввести ребро
        final JButton third = new JButton("Import data");   //ввод данных из файла
        final JButton fourth = new JButton("Start");        //начать алгоритм
        final JButton fifth = new JButton("Show result");   //сразу показать результат

        /*
         * Панель графа
         */
        pg=new PaintGraph();
        pg.setPreferredSize(new Dimension(650,650));


        /*
         * Панель, отвечающая за ввод кол-ва вершин
         */
        JPanel numLine = new JPanel();
        numLine.setLayout(new BoxLayout(numLine,BoxLayout.LINE_AXIS));

        final JTextField number = new JTextField();
        first.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int nu=Integer.parseInt(number.getText());
                second.setEnabled(true);
                g=new MyGraph(nu);
            }
        });
        numLine.add(new JLabel("Number of vertex  "));
        numLine.add(Box.createHorizontalStrut(4));
        numLine.add(number);
        numLine.add(Box.createHorizontalStrut(4));
        numLine.add(first);

        /*
         * Панель, отвечающая за ввод ребер
         */
        JPanel edgeLine = new JPanel();
        edgeLine.setLayout(new BoxLayout(edgeLine,BoxLayout.LINE_AXIS));
        final JTextField vert1 = new JTextField();
        final JTextField vert2 = new JTextField();

        second.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int e1=Integer.parseInt(vert1.getText());
                int e2=Integer.parseInt(vert2.getText());
                System.out.println(e1+" "+e2);
                g.createEdge(e1-1,e2-1);
            }
        });

        edgeLine.add(new JLabel("New edge  "));
        edgeLine.add(Box.createHorizontalStrut(4));
        edgeLine.add(vert1);
        edgeLine.add(Box.createHorizontalStrut(4));
        edgeLine.add(vert2);
        edgeLine.add(Box.createHorizontalStrut(4));
        edgeLine.add(second);

        /*
         * Правая верхняя панель со всем интерфейсом
         */
        JPanel rightUpPanel= new JPanel();
        rightUpPanel.setLayout(new GridLayout(5,1,0,3));


        third.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("DOESN'T WORK");
            }
        });

        fourth.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                g.startDfs();
                pg.drawGraph(g);
                g.topSorting();
                final JScrollPane scrollPane=new JScrollPane(g.makeJTable());
                secondPanel.add(scrollPane);


            }
        });

        fifth.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("DOESN'T WORK");
            }
        });

        rightUpPanel.add(numLine);
        rightUpPanel.add(edgeLine);
        rightUpPanel.add(third);
        rightUpPanel.add(fourth);
        rightUpPanel.add(fifth);
        rightUpPanel.setPreferredSize(new Dimension(200,300));

        /*
         * Правая панель
         */
        JPanel rightPanel=new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel,BoxLayout.PAGE_AXIS));
        rightPanel.add(rightUpPanel);
        rightPanel.add(Box.createVerticalStrut(500));
        rightPanel.setPreferredSize(new Dimension(200,600));

        /*
         * Первая вкладка - граф и кнопки
         */
        firstPanel.setLayout(new BorderLayout(5,0));
        firstPanel.add(pg,BorderLayout.CENTER);
        firstPanel.add(rightPanel,BorderLayout.EAST);

        /*
         * Вкладки
         */
        tp.addTab("Graph",firstPanel);
        tp.addTab("Table",secondPanel);

        f.add(tp);

        f.pack();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLocationRelativeTo(null);
        f.setResizable(true);
        f.setVisible(true);
    }
}


