package com.company;

public class CSTR extends reactor {
    @Override
    //method findConversion is reasonably accurate using the Newton-Raphson method =)
    //May need to check code again to ensure
    public double findConversion(feedElement feed) {
        double conversion = CSTRNewtonRaphsonMethod(feed, Math.random());
        int count = 0;
        while (conversion>1 || conversion <0) {
            conversion = CSTRNewtonRaphsonMethod(feed, Math.random());
            count++;
            if (count>100) {
                System.out.println("Cannot find answer. Using approximate value.");
                break;
            }
        }
        //updates feed concentration as well.
        //feed.updateConcentration(conversion);
        return conversion;
    }
    @Override
    public void findSpacetime(feedElement feed, double conversion) {
        double tau, eA = feed.geteA(), n = feed.getReactionOrder(),
                k = feed.getReactionConstant(), cA0 = feed.getInitialConentration();
        /*
        Python Code:
        tau = (xA*(1+eA*xA)**rxnOrder)/((1-xA)**rxnOrder)
        tau = tau/k
        tau = tau/(cA0**(rxnOrder-1))
         */
        tau = (Math.pow((conversion *(1+eA* conversion)), n)) / (Math.pow(1- conversion, n));
        tau = tau/k;
        tau = tau/(Math.pow(cA0, n-1));
        setSpacetime(tau);
    }
    public double CSTRNewtonRaphsonMethod (feedElement feed, double initialGuess) {
        double tau = getSpacetime(), xA = initialGuess, eA = feed.geteA(), n = feed.getReactionOrder(),
                k = feed.getReactionConstant(), cA0 = feed.getInitialConentration();
        double f, df, dx = 0.000000001, xA1, fprime;
        double lhs;
        int count = 0;
        lhs = k*tau*Math.pow(cA0,n-1);
        f = xA*Math.pow((1+eA*xA)/(1-xA),n)-lhs;
        df = (xA+dx)*Math.pow((1+eA*(xA+dx))/(1-(xA+dx)),n)-lhs-f;
        fprime = df/dx;
        xA1 = xA -f/fprime;
        while (Math.abs(xA1 - xA)>=0.000000001) {
            xA=xA1;
            f = xA*Math.pow((1+eA*xA)/(1-xA),n)-lhs;
            df = (xA+dx)*Math.pow((1+eA*(xA+dx))/(1-(xA+dx)),n)-lhs-f;
            fprime = df/dx;
            count++;
            xA1 = xA - f/fprime;
            //System.out.println("Step: "+count+" x:"+xA+" Value:"+f);
            if (count>10000) {
                System.out.println("No convergence! Returning approximate value.");
                break;
            }
        }
        return xA;
    }
}
