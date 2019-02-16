package com.company;
// object class reactor
public abstract class reactor {
    private double spacetime;
    //TODO: setSpacetime second function may be wrong for non-constant density systems?
    public void setSpacetime(double spacetime) {
        this.spacetime = spacetime;
    }
    public void setSpacetime(double volume, double v0) {
        this.spacetime = volume/v0;
    }
    public double getSpacetime() {
        return spacetime;
    }
    public double getVolume(double v0) {
        return getSpacetime()*v0;
    }
    public abstract double findConversion(feedElement feed);
    public abstract void findSpacetime (feedElement feed, double conversion);
}
