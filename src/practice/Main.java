package practice;

import javafx.stage.FileChooser;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.*;
import java.io.*;


public class Main {

    private static MyGraph g;
    private static PaintGraph pg;
    private static State state;
    private static JFileChooser fc;

    enum State {
        Making, Searching, Sorting
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }


    private static void createAndShowGUI() {

        state = State.Making;

        final JFrame f = new JFrame("Topological sort");
        JTabbedPane tp = new JTabbedPane();             //окно состоит из 2х вкладок
        JPanel firstPanel = new JPanel();               //на первой граф и кнопки
        final JPanel secondPanel = new JPanel();        //на второй таблица
        secondPanel.setLayout(new BoxLayout(secondPanel, BoxLayout.PAGE_AXIS));

        final JPanel secondUpPanel = new JPanel();           //верхняя половина второй страницы
        secondUpPanel.setLayout(new BorderLayout(2, 0));


        /*
         * Кнопки с первой страницы
         */
        final JButton numOfVertexButton = new JButton("ok");            //ввести кол-во вершин
        final JButton createEdgeButton = new JButton("ok");           //ввести ребро
        final JButton importDateButton = new JButton("Import data");   //ввод данных из файла
        final JButton startButton = new JButton("Make graph");        //начать алгоритм
        final JButton saveButton = new JButton("Save");   //сохранить в файл
        final JButton restartButton = new JButton("Restart");   //заново

        numOfVertexButton.setEnabled(false);
        createEdgeButton.setEnabled(false);
        startButton.setEnabled(false);
        saveButton.setEnabled(false);
        restartButton.setEnabled(false);


        /*
         * Панель графа (первая страница)
         */
        pg = new PaintGraph();
        pg.setPreferredSize(new Dimension(650, 650));


        /*
         * Панель, отвечающая за ввод кол-ва вершин
         */
        JPanel numLine = new JPanel();
        numLine.setLayout(new BoxLayout(numLine, BoxLayout.LINE_AXIS));

        final JTextField number = new JTextField();
        number.setDocument(new MyPlainDocument());

        numLine.add(new JLabel("Number of vertex  "));
        numLine.add(Box.createHorizontalStrut(4));
        numLine.add(number);
        numLine.add(Box.createHorizontalStrut(4));
        numLine.add(numOfVertexButton);

        /*
         * Панель, отвечающая за ввод ребер
         */
        final JPanel edgeLine = new JPanel();
        edgeLine.setLayout(new BoxLayout(edgeLine, BoxLayout.LINE_AXIS));
        final JTextField vert1 = new JTextField();
        vert1.setDocument(new MyPlainDocument());
        vert1.setEnabled(false);
        final JTextField vert2 = new JTextField();
        vert2.setDocument(new MyPlainDocument());
        vert2.setEnabled(false);

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
        JPanel rightUpPanel = new JPanel();
        rightUpPanel.setLayout(new GridLayout(6, 1, 0, 3));
        rightUpPanel.add(numLine);
        rightUpPanel.add(edgeLine);
        rightUpPanel.add(importDateButton);
        rightUpPanel.add(saveButton);
        rightUpPanel.add(restartButton);
        rightUpPanel.add(startButton);

        rightUpPanel.setPreferredSize(new Dimension(200, 0));
        rightUpPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        /*
         * Список введенных ребер
         */
        final JTextArea listOfEdges = new JTextArea("Edges:");
        listOfEdges.setEditable(false);
        JScrollPane edgesScrollPane = new JScrollPane(listOfEdges,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        edgesScrollPane.setPreferredSize(new Dimension(200, 50));
        edgesScrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);

        /*
         * Сообщения о неверно введенных ребрах
         */
        final JLabel errorMessage = new JLabel();
        errorMessage.setAlignmentX(Component.LEFT_ALIGNMENT);
        errorMessage.setPreferredSize(new Dimension(200, 50));


        /*
         * Правая панель целиком
         */
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.PAGE_AXIS));
        rightPanel.add(rightUpPanel);
        rightPanel.add(Box.createVerticalStrut(20));
        rightPanel.add(edgesScrollPane);
        rightPanel.add(errorMessage);
        rightPanel.add(Box.createVerticalStrut(200));

        rightPanel.setPreferredSize(new Dimension(200, 600));

        /*
         * Первая страница - граф и кнопки
         */
        firstPanel.setLayout(new BorderLayout(5, 0));
        firstPanel.add(pg, BorderLayout.CENTER);
        firstPanel.add(rightPanel, BorderLayout.EAST);

         /*
         * Заголовки для строк таблицы (вторая страница)
         */
        final JPanel headers = new JPanel();
        headers.setLayout(new BoxLayout(headers, BoxLayout.PAGE_AXIS));
        headers.add(Box.createVerticalStrut(3));

        JLabel lab1 = new JLabel("Number");
        lab1.setAlignmentX(Component.CENTER_ALIGNMENT);
        headers.add(lab1);

        headers.add(Box.createVerticalStrut(15));

        JLabel lab2 = new JLabel("Visited");
        lab2.setAlignmentX(Component.CENTER_ALIGNMENT);
        headers.add(lab2);

        headers.add(Box.createVerticalGlue());

        JLabel lab3 = new JLabel("Used");
        lab3.setAlignmentX(Component.CENTER_ALIGNMENT);
        headers.add(lab3);

        headers.add(Box.createVerticalGlue());

        JLabel lab4 = new JLabel("Renumb");
        lab4.setAlignmentX(Component.CENTER_ALIGNMENT);
        headers.add(lab4);

        headers.add(Box.createVerticalGlue());

        JLabel lab5 = new JLabel("Sorted");
        lab5.setAlignmentX(Component.CENTER_ALIGNMENT);
        headers.add(lab5);

        headers.add(Box.createVerticalStrut(40));

        headers.setPreferredSize(new Dimension(50, 200));


        /*
         * Вкладки окна
         */
        tp.addTab("Graph", firstPanel);
        tp.addTab("Table", secondPanel);


        /*
         * Listener'ы для кнопок и полей с первой страницы
         */

        number.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {

                if (number.getText().equals("") || number.getText().equals("1")) {
                    numOfVertexButton.setEnabled(false);
                } else {
                    numOfVertexButton.setEnabled(true);
                }
                super.keyReleased(e);
            }
        });

        vert1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (!(vert1.getText().equals("") || vert2.getText().equals(""))) createEdgeButton.setEnabled(true);
                else createEdgeButton.setEnabled(false);
                super.keyReleased(e);
            }
        });

        vert2.addKeyListener(vert1.getKeyListeners()[0]);

        //кнопка ввода количества вершин
        numOfVertexButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int num = Integer.parseInt(number.getText());
                g = new MyGraph(num);

                vert1.setEnabled(true);
                vert2.setEnabled(true);
                restartButton.setEnabled(true);
                startButton.setEnabled(true);

                number.setEnabled(false);
                numOfVertexButton.setEnabled(false);
                importDateButton.setEnabled(false);

            }
        });

        //кнопка ввода ребра
        createEdgeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int e1 = Integer.parseInt(vert1.getText());
                int e2 = Integer.parseInt(vert2.getText());
                vert1.setText("");
                vert2.setText("");

                try {

                    g.checkEdge(e1 - 1, e2 - 1);
                    listOfEdges.append("\n" + e1 + " - " + e2);
                    if (!errorMessage.getText().equals("")) errorMessage.setText("");
                    createEdgeButton.setEnabled(false);

                } catch (IllegalArgumentException iae) {
                    errorMessage.setText("ERROR: " + iae.getMessage());
                    createEdgeButton.setEnabled(false);
                    System.out.println("ERROR: " + iae.getMessage());
                }

//                g.createEdge(0, 1);
//                g.createEdge(0, 2);
//                g.createEdge(0, 3);
//                g.createEdge(0, 5);
//                g.createEdge(0, 6);
//
//                g.createEdge(2, 3);
//
//                g.createEdge(3, 4);
//                g.createEdge(3, 5);
//
//                g.createEdge(4, 9);
//
//                g.createEdge(6, 4);
//                g.createEdge(6, 9);
//
//                g.createEdge(7, 6);
//
//                g.createEdge(8, 7);
//
//                g.createEdge(9, 10);
//                g.createEdge(9, 11);
//                g.createEdge(9, 12);
//
//                g.createEdge(11, 12);
            }
        });

        //кнопка ввода данных
        importDateButton.addActionListener(new ActionListener() {
                                               @Override
                                               public void actionPerformed(ActionEvent e) {
                                                   fc = new JFileChooser();
                                                   int returnVal = fc.showOpenDialog(null);
                                                   if (returnVal == JFileChooser.APPROVE_OPTION) {
                                                       try {
                                                           File file = fc.getSelectedFile();
                                                           if (!getFileExtension(file).equals("txt")) {
                                                               throw new IOException("Wrong type of file");
                                                           }
                                                           BufferedReader reader = new BufferedReader(new FileReader(file));
                                                           int numberOfVertex = Integer.parseInt(reader.readLine());
                                                           if (numberOfVertex >= 2) {
                                                               g = new MyGraph(numberOfVertex);
                                                           } else {
                                                               throw new NumberFormatException("Wrong number of vertex");
                                                           }
                                                           String s;
                                                           while ((s = reader.readLine()) != null) {
                                                               System.out.println(s);
                                                               String[] values = s.split(" ");
                                                               if (values.length == 2) {
                                                                   g.checkEdge(Integer.parseInt(values[0]) - 1, Integer.parseInt(values[1]) - 1);
                                                               } else {
                                                                   throw new IOException("Incorrect edge");
                                                               }
                                                           }
                                                           startButton.setEnabled(true);
                                                           number.setEnabled(false);
                                                           importDateButton.setEnabled(false);
                                                       } catch (IOException ex) {
                                                           //System.out.println(ex);
                                                           errorMessage.setText("FILE ERROR: " + ex.getMessage());
                                                       } catch (IllegalArgumentException ex) {
                                                           errorMessage.setText("ERROR: " + ex.getMessage());
                                                           //System.out.println(ex);
                                                       }
                                                   }

                                               }
                                           }

        );

        //кнопка начала работы
        startButton.addActionListener(new ActionListener() {
                                          @Override
                                          public void actionPerformed(ActionEvent e) {
                                              switch (Main.state) {
                                                  case Making:
                                                      vert1.setEnabled(false);
                                                      vert2.setEnabled(false);
                                                      createEdgeButton.setEnabled(false);
                                                      restartButton.setEnabled(false);
                                                      errorMessage.setText("");

                                                      pg.drawGraphDfsWithSteps(g);
                                                      startButton.setText("Start DFS");
                                                      state = State.Searching;
                                                      g.startDfs();
                                                      break;

                                                  case Searching:
                                                      if (pg.getStepNumber() == 0) {
                                                          startButton.setText("Next step");
                                                      }
                                                      pg.drawStep(g);
                                                      if (pg.getStepNumber() >= g.getAdjLists().size()) {
                                                          startButton.setText("Sort");
                                                          state = State.Sorting;
                                                      }
                                                      break;
                                                  case Sorting:

                                                      g.topSorting();
                                                      pg.drawSortedGraph(g);
                                                      errorMessage.setText("Clockwise orientation");
                                                      JScrollPane scrollPane = new JScrollPane(g.makeJTable(),
                                                              ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER,
                                                              ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
                                                      scrollPane.setPreferredSize(new Dimension(750, 200));
                                                      scrollPane.setColumnHeader(null);
                                                      secondUpPanel.add(headers, BorderLayout.WEST);
                                                      secondUpPanel.add(scrollPane, BorderLayout.CENTER);
                                                      secondPanel.add(secondUpPanel);
                                                      secondPanel.add(Box.createVerticalStrut(450));
                                                      restartButton.setEnabled(true);
                                                      saveButton.setEnabled(true);
                                                      startButton.setEnabled(false);
                                                      break;
                                              }

//                pg.drawGraph(g);
                                          }
                                      }

        );

        //кнопка сохранения данных
        saveButton.addActionListener(new ActionListener() {
                                         @Override
                                         public void actionPerformed(ActionEvent e) {
                                             try {
                                                 fc = new JFileChooser();
                                                 int returnVal = fc.showSaveDialog(null);
                                                 if (returnVal == JFileChooser.APPROVE_OPTION) {

                                                     File file = fc.getSelectedFile();
                                                     if (!getFileExtension(file).equals("txt")) {
                                                         throw new IOException("Wrong type of file");
                                                     }
                                                     if (!file.exists()) {
                                                         file.createNewFile();
                                                     }
                                                     if (file.canWrite()) {
                                                         BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                                                         writer.write("Количество вершин:" + g.getAdjLists().size());

                                                         writer.newLine();
                                                         writer.write("Список смежности:");
                                                         writer.newLine();
                                                         for (int i = 0; i < g.getAdjLists().size(); i++) {
                                                             writer.write((i + 1) + ": ");
                                                             for (int j = 0; j < g.getAdjLists().get(i).size(); j++) {
                                                                 writer.write((g.getAdjLists().get(i).get(j) + 1) + " ");
                                                             }
                                                             writer.newLine();
                                                         }
                                                         writer.write("Вершины в отсортированном порядке: ");
                                                         for (int i = 0; i < g.getTopSorted().size(); i++) {
                                                             writer.write((g.getTopSorted().get(i) + 1) + " ");
                                                         }
                                                         writer.flush();
                                                     }
                                                     else{
                                                         throw new IOException("Can't write to this file");
                                                     }

                                                 }
                                             } catch (IOException ex) {
                                                 errorMessage.setText("FILE ERROR: " + ex.getMessage());
                                                 //System.out.println(ex);
                                             }

                                         }
                                     }
        );

        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switch (state){
                    case Making:
                        g = null;
                        number.setEnabled(true);
                        number.setText("");
                        vert1.setEnabled(false);
                        vert2.setEnabled(false);
                        createEdgeButton.setEnabled(false);
                        startButton.setEnabled(false);
                        importDateButton.setEnabled(true);
                        restartButton.setEnabled(false);
                        listOfEdges.setText("Edges:");
                        errorMessage.setText("");
                        break;

                    case Sorting:
                        g = null;
                        pg.removeAll();
                        pg.setStepNumber(0);
                        f.repaint();
                        state = State.Making;
                        startButton.setText("Make graph");
                        number.setEnabled(true);
                        number.setText("");
                        vert1.setEnabled(false);
                        vert2.setEnabled(false);
                        createEdgeButton.setEnabled(false);
                        startButton.setEnabled(false);
                        importDateButton.setEnabled(true);
                        restartButton.setEnabled(false);
                        listOfEdges.setText("Edges:");
                        errorMessage.setText("");
                        saveButton.setEnabled(false);

                }
            }
        });
        f.add(tp);

        f.pack();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLocationRelativeTo(null);
        f.setResizable(false);
        f.setVisible(true);
    }


    /*
     * Класс для управления JTextField'ами
     */
    static class MyPlainDocument extends PlainDocument {

        String goodChars = "123456789";

        @Override
        public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
            if (goodChars.contains(str)) {
                super.insertString(offs, str, a);
            }
            if (str.equals("0") && getLength() > 0) super.insertString(offs, str, a);
        }
    }
    private static String getFileExtension(File file) {
        String fileName = file.getName();
        // если в имени файла есть точка и она не является первым символом в названии файла
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
            // то вырезаем все знаки после последней точки в названии файла, то есть ХХХХХ.txt -> txt
            return fileName.substring(fileName.lastIndexOf(".") + 1);
            // в противном случае возвращаем заглушку, то есть расширение не найдено
        else return "";
    }
}


