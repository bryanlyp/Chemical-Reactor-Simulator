package com.company;

public class PFTR extends reactor{
    @Override
    public void findSpacetime(feedElement feed, double conversion) {
        /* Python Code
        if rxnOrder == 0:
            tau = cstr(0,k,cA0,eA,xA)
            elif rxnOrder == 1:
            tau = (1+eA)*math.log(1/(1-xA))-eA*xA
            tau = tau/k
            elif rxnOrder == 2:
            tau = 2*eA*(1+eA)*math.log(1-xA)
            tau += xA*eA**2
            tau += ((eA+1)**2)/(xA/(1-xA))
        else:
            tau = integratePftr(rxnOrder,k,cA0,eA,xA)
        */
        double tau, eA = feed.geteA(), n = feed.getReactionOrder(),
                k = feed.getReactionConstant(), cA0 = feed.getInitialConentration();
        if (n==0) {
            CSTR zeroOrderReactor = new CSTR();
            zeroOrderReactor.findSpacetime(feed, conversion);
            tau = zeroOrderReactor.getSpacetime();
        } else if (n==1) {
            tau = (1+eA)*Math.log(1/(1- conversion)) - eA* conversion;
            tau = tau/k;
        } else if (n==2) {
            tau = 2*eA*(1+eA)*Math.log(1- conversion);
            tau += conversion *eA* conversion *eA;
            tau += (Math.pow(eA+1, 2)/(conversion /(1- conversion)));
            tau = tau/(cA0*k);
        } else {
            tau = integratePFTR(feed, conversion);
        }
        setSpacetime(tau);
    }
    public double findConversion(feedElement feed) {
        double tau = getSpacetime(), eA = feed.geteA(), n = feed.getReactionOrder(),
                k = feed.getReactionConstant(), cA0 = feed.getInitialConentration();
        double xA = 0, rhs, dxA = 0.0000001, xA1 = xA + dxA, lhs;
        lhs = k * tau / cA0;
        rhs = ((Math.pow(((1 + eA * xA) / (cA0 * (1 - xA))), n) + Math.pow(((1 + eA * xA1) / (cA0 * (1 - xA1))), n)) / 2) * dxA;
        while (rhs < lhs) {
            xA += dxA;
            xA1 += dxA;
            rhs += ((Math.pow(((1 + eA * xA) / (cA0 * (1 - xA))), n) + Math.pow(((1 + eA * xA1) / (cA0 * (1 - xA1))), n)) / 2) * dxA;
        }
        return xA1;

    }
    public double integratePFTR(feedElement feed, double conversion) {
        /* Python Code:
        n = rxnOrder
        total = 0
        x0=0
        increment = (x0+xA)/10000
        x1 = x0+increment
        while (x1<=xA):
            total += ((((1+eA*x0)/(cA0*(1-x0)))**n + ((1+eA*x1)/(cA0*(1-x1)))**n)/2)*increment
            x0+=increment
            x1=x0+increment
        total *= (cA0/k)
        return total
        */
        double eA = feed.geteA(), n = feed.getReactionOrder(),
                k = feed.getReactionConstant(), cA0 = feed.getInitialConentration();
        double total = 0.0, x0 = 0.0, increment = (x0+ conversion)/10000, x1 = x0+increment;
        while (x1 <= conversion) {
            total += ((Math.pow(((1+eA*x0)/(cA0*(1-x0))), n) + Math.pow(((1+eA*x1)/(cA0*(1-x1))), n))/2)*increment;
            x0 += increment;
            x1 = x0 + increment;
        }
        total *= (cA0/k);
        return total;
    }
    /*
    @Override
    //Old code, using multiple iterations of CSTR to approximate PFTR behavior.
    //Not sure why it's not working, may need to check NR-method implementation.
    public double findConversion(feedElement feed) {
        CSTR mini = new CSTR();
        double cA0 = feed.getInitialConentration();
        double conversion;
        mini.setSpacetime(getSpacetime()/10);
        for (int i = 0; i<2; i++) {
            conversion = mini.findConversion(feed);
            System.out.println("Conversion: "+conversion+", concentration: "+feed.getInitialConentration());
        }
        double cA = feed.getInitialConentration();
        conversion = (1-cA/cA0)/(1+feed.geteA()*(cA/cA0));
        return conversion;
    }
    */

}
