package com.company;
// For now, feed is restricted to a pure feed of a single species.
public class feedElement {
    // TODO: allow feed species identification with species names (maybe).
    // TODO: Multiple reactions?
    private double c0;
    private double k;
    private double eA;
    private double n;

    // Mutator methods, for now assume all values are valid, add exceptions later.
    public void setInitialConcentration(double initialConcentration) {
        this.c0 = initialConcentration;
    }
    public void setReactionOrder(double reactionOrder) {
        this.n = reactionOrder;
    }
    public void setReactionConstant(double reactionConstant) {
        this.k = reactionConstant;
    }
    public void setEA(double epsilon) {
        this.eA = epsilon;
    }
    public void setFeed(double initialConcentration, double reactionOrder, double reactionConstant,
                        double epsilon) {
        setInitialConcentration(initialConcentration);
        setReactionOrder(reactionOrder);
        setReactionConstant(reactionConstant);
        setEA(epsilon);
    }
    // Accessor methods
    public double getInitialConentration() {
        return c0;
    }
    public double getReactionOrder() {
        return n;
    }
    public double geteA() {
        return eA;
    }
    public double getReactionConstant() {
        return k;
    }

    public void updateConcentration(double conversion) {
        double cA, cA0 = getInitialConentration();
        cA = cA0*(1-conversion)/(1+geteA()*conversion);
        setInitialConcentration(cA);
    }
}