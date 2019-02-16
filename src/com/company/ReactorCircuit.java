package com.company;

import javax.swing.*;
import java.util.*;

public class ReactorCircuit {
    private ArrayList<reactor> circuit = new ArrayList<reactor>();
    public void setReactors(int reactorType, double spacetime) {
        if (reactorType == 1) {
            PFTR newReactor = new PFTR();
            newReactor.setSpacetime(spacetime);
            circuit.add(newReactor);
            System.out.println("Added PFTR.");
        } else if (reactorType == 2) {
            CSTR newReactor = new CSTR();
            newReactor.setSpacetime(spacetime);
            circuit.add(newReactor);
            System.out.println("Added CSTR.");
        }
    }
    public void getFinalConcentration(feedElement feed) {
        int numReactors = circuit.size();
        double intermediateconversion;
        double cA0 = feed.getInitialConentration();
        for (int i = 0; i < numReactors; i++) {
            intermediateconversion = circuit.get(i).findConversion(feed);
            feed.updateConcentration(intermediateconversion);
            System.out.println("Conversion for reactor "+ (i+1) + " is "+intermediateconversion);
            System.out.println("Concentration leaving this reactor is: "+feed.getInitialConentration());
        }
        double cA = feed.getInitialConentration();
        System.out.println("Final concentration is: "+feed.getInitialConentration());
        double conversion;
        conversion = (cA0-cA)/(cA*feed.geteA()+cA0);
        System.out.println("Final conversion is: "+conversion);

    }
    public void getFinalConcentration(feedElement feed, JTextArea text) {
        int numReactors = circuit.size();
        double intermediateconversion;
        double cA0 = feed.getInitialConentration();
        for (int i = 0; i < numReactors; i++) {
            intermediateconversion = circuit.get(i).findConversion(feed);
            feed.updateConcentration(intermediateconversion);
            text.append("Conversion for reactor "+ (i+1) + " is "+intermediateconversion +"\n");
            text.append("Concentration leaving this reactor is: "+feed.getInitialConentration()+"\n");
        }
        double cA = feed.getInitialConentration();
        text.append("Final concentration is: "+feed.getInitialConentration()+"\n");
        double conversion;
        conversion = (cA0-cA)/(cA*feed.geteA()+cA0);
        text.append("Final conversion is: "+conversion+"\n");
    }
}
