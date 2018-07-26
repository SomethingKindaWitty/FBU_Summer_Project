package me.caelumterrae.fbunewsapp.math;


import android.util.Log;

import org.apache.commons.math3.distribution.BetaDistribution;

import java.util.ArrayList;

public class BetaDis {

    /* Takes in the user's political affiliation number to gives us the political category we should
     * draw from
     */

    private static final double ALPHA_CONST = 62.00051;
    private static final double BETA_CONST = 37.99949;
    public double alpha;
    public double beta;
    private BetaDistribution betaDis;

    /* Use user's 'affiliation' to calculate the Alpha and Beta values for our beta dist.
     * (Alpha & beta values determine the shape & skew of the distribution -- peak will be @
     * user's affiliation value). The beta distribution (bell-curve shape) is used to describe
     * the probabilities of each of the 5 political categories
     */
    public BetaDis(double affiliation) {
        alpha = getGaussianValue(affiliation, ALPHA_CONST);
        beta = getGaussianValue(affiliation, BETA_CONST);

        betaDis = new BetaDistribution(alpha, beta);

    }

    public double getAlpha() {
        return alpha;
    }

    public double getBeta() {
        return beta;
    }

    // Returns the probability density function (PDF) of this distribution evaluated at the specified point x.
    public double getPDF(double x) {
        return betaDis.density(x);
    }
    // returns a number 0, 25, 50, 75, 100 = which category to pick a news article from
    // corresponds to: [left (0), left-center (25), moderate (50), right-center (75), right (100)]
    public int getCategory() {
        return 25*(int)(betaDis.sample()*5);

    }

    // Use gaussian bell curve. a_value = 2.980524*e^(-(x - 62.00051)^2/(2*33.06239^2))
    // b_value = y = 2.980524*e^(-(x - 37.99949)^2/(2*33.06239^2))
    // Quartic regression 4th order polynomial. a_value = y = 0.65 - 0.01583333*x + 0.003043333*x^2 - 0.00004666667*x^3 + 1.866667e-7*x^4
    // b_value = y = 1.5 + 0.0605*x + 0.0002433333*x^2 - 0.000028*x^3 + 1.866667e-7*x^4

    private static double getGaussianValue(double affiliation, double offset) {
        return 2.980524*Math.exp(-Math.pow(affiliation - offset,2)/2186.24326502);
    }

}
