package com.company;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class gui{
    JPanel reactorSelectionPanel;
    JPanel feedInitializationPanel;
    JPanel mainPanel;
    JPanel calculatePanel;
    JTextArea text;
    JList list;
    String [] reactorList = {"CSTR", "PFTR"};
    JButton startButton;
    JButton feedInitializeButton;
    JButton calculateButton;
    JButton resetButton;
    JButton infoButton;
    JTextField spaceTimeEntry;
    JTextField nEntry;
    JTextField kEntry;
    JTextField eEntry;
    JTextField cA0Entry;
    int reactorType;
    double spacetime = 0;
    feedElement feed;
    ReactorCircuit circuit;
    boolean feedConfirmed = false;
    boolean atLeastOneReactor = false;

    public void go() {
        JFrame frame = new JFrame();
        JPanel textPanel = new JPanel();
        feedInitializationPanel = new JPanel();
        reactorSelectionPanel = new JPanel();
        calculatePanel = new JPanel();
        mainPanel = new JPanel();
        circuit = new ReactorCircuit();

        initializeList();
        initializeSpaceTimeButton();
        initializeCA0Button();
        initializeKButton();
        initializeNButton();
        initializeEButton();

        //Buttons
        startButton = new JButton("Add Reactor");
        startButton.addActionListener(new reactorAdder());
        reactorSelectionPanel.add(startButton);
        feedInitializeButton = new JButton("Set Feed");
        feedInitializeButton.addActionListener(new feedInitializer());
        feedInitializationPanel.add(feedInitializeButton);
        calculateButton = new JButton("Calculate Conversion");
        calculateButton.addActionListener(new conversionCalculator());
        calculatePanel.add(calculateButton);
        resetButton = new JButton("Reset All");
        calculatePanel.add(resetButton);
        resetButton.addActionListener(new reset());
        infoButton = new JButton("More Info");
        infoButton.addActionListener(new infoDisplay());

        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(feedInitializationPanel);
        mainPanel.add(reactorSelectionPanel);
        calculatePanel.add(calculateButton);
        calculatePanel.add(resetButton);
        calculatePanel.add(infoButton);
        //Text area initialization:
        text = new JTextArea(15,60);
        text.setLineWrap(true);
        text.setText("Enter feed parameters and click 'Set Feed'.\n" +
                "Then, select reactor type, click on list, and then set space time.\n" +
                "Then, click 'Add Reactor'.\n\n");
        text.setCaretPosition(text.getText().length());

        JScrollPane scroller = new JScrollPane(text);
        scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        textPanel.add(scroller);

        frame.getContentPane().add(BorderLayout.NORTH,textPanel);
        frame.getContentPane().add(BorderLayout.CENTER,mainPanel);
        frame.getContentPane().add(BorderLayout.SOUTH,calculatePanel);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(750,400);
        frame.setVisible(true);
    }

    public void initializeList() {
        JLabel reactorSelectionLabel = new JLabel();
        list = new JList<>(reactorList);
        reactorSelectionLabel.setText("Reactor Type:");
        list.setVisibleRowCount(1);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.addListSelectionListener(new reactorListener());
        reactorSelectionPanel.add(reactorSelectionLabel);
        reactorSelectionPanel.add(list);
        JScrollPane listScroller = new JScrollPane(list);
        listScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        listScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        reactorSelectionPanel.add(listScroller);
    }
    public void initializeSpaceTimeButton() {
        JLabel spaceTimeLabel = new JLabel();
        spaceTimeLabel.setText("Space-Time:");
        spaceTimeEntry = new JTextField(5);
        reactorSelectionPanel.add(spaceTimeLabel);
        reactorSelectionPanel.add(spaceTimeEntry);
    }
    public void initializeCA0Button() {
        cA0Entry = new JTextField(5);
        JLabel cA0Label = new JLabel();
        cA0Label.setText("Initial Concentration: ");
        feedInitializationPanel.add(cA0Label);
        feedInitializationPanel.add(cA0Entry);

    }
    public void initializeKButton() {
        JLabel kLabel = new JLabel();
        kLabel.setText("Rate Constant:");
        kEntry = new JTextField(5);
        feedInitializationPanel.add(kLabel);
        feedInitializationPanel.add(kEntry);
    }
    public void initializeNButton() {
        JLabel nLabel = new JLabel();
        nLabel.setText("Reaction Order:");
        nEntry = new JTextField(5);
        feedInitializationPanel.add(nLabel);
        feedInitializationPanel.add(nEntry);
    }
    public void initializeEButton() {
        JLabel eLabel = new JLabel();
        eLabel.setText("Epsilon:");
        eEntry = new JTextField(5);
        feedInitializationPanel.add(eLabel);
        feedInitializationPanel.add(eEntry);
    }

    class reactorListener implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (!e.getValueIsAdjusting()) {
                String selected = (String)list.getSelectedValue();
                if (selected == "CSTR") {
                    reactorType = 2;
                } else if (selected == "PFTR"){
                    reactorType = 1;
                }
            }
        }
    }
    class reactorAdder implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                spacetime = Double.parseDouble(spaceTimeEntry.getText());
            } catch (Exception NullPointerException) {
                text.append("Invalid spacetime input!\n");
            }
            if (spacetime > 0 && (reactorType == 1||reactorType == 2)) {
                circuit.setReactors(reactorType, spacetime);
                if (reactorType == 1) {
                    text.append("PFTR added with space-time: " + spacetime + ".\n");
                } else if (reactorType == 2){
                    text.append("CSTR added with space-time: " + spacetime + ".\n");
                }
                atLeastOneReactor = true;
            } else if (spacetime <= 0){
                text.append("Space-time must be more than zero.\n");
            } else {
                text.append("Select a reactor! Click on list box after selecting.\n");
            }
        }
    }
    class feedInitializer implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            feed = new feedElement();
            double cA0,n,k,eA;
            cA0 = Double.parseDouble(cA0Entry.getText());
            n = Double.parseDouble(nEntry.getText());
            k = Double.parseDouble(kEntry.getText());
            eA = Double.parseDouble(eEntry.getText());
            if (cA0 >= 0 && k >=0) {
                feed.setInitialConcentration(cA0);
                feed.setReactionOrder(n);
                feed.setReactionConstant(k);
                feed.setEA(eA);
                feedConfirmed = true;
                text.append("Updated feed parameters.\n");
            } else {
                text.append("Invalid feed input!\n");
            }
        }
    }
    class conversionCalculator implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (feedConfirmed && atLeastOneReactor) {
                circuit.getFinalConcentration(feed,text);
            } else {
                if (!feedConfirmed) {
                    text.append("(Feed parameters not set.)\n");
                }
                if (!atLeastOneReactor){
                    text.append("(No reactors in circuit.)\n");
                }
            }
        }
    }
    class infoDisplay implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            text.setText("This program takes a user-input to do the following: \n" +
                    "1. Build a series circuit of any number of reactors in any combination.\n" +
                    "2. Set reaction parameters (i.e. variable density, reaction order, etc.) \n" +
                    "3. Calculate the conversion and exit concentration leaving each reactor, " +
                    "including the final conversion and concentration.\n\n");
            text.append("Limitations (May be added in future builds): \n" +
                    "1. Simple rate laws only. \n" +
                    "2. Only one species in rate law. However, any reaction order is possible.\n" +
                    "3. Multiple reactions not supported. \n" +
                    "4. Non-isothermal reactions not supported.\n" +
                    "5. Non-ideal reactors not supported.\n");
        }
    }
    class reset implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            feed = new feedElement();
            circuit = new ReactorCircuit();
            atLeastOneReactor = false;
            feedConfirmed = false;
            text.setText("Circuit and feed parameters reset. \n");
        }
    }

}
