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
    private BetaDistribution betaDis;

    /* Use user's 'affiliation' to calculate the Alpha and Beta values for our beta dist.
     * (Alpha & beta values determine the shape & skew of the distribution -- peak will be @
     * user's affiliation value). The beta distribution (bell-curve shape) is used to describe
     * the probabilities of each of the 5 political categories
     */
    public BetaDis(double affiliation) {
        double alpha = getGaussianValue(affiliation, ALPHA_CONST);
        double beta = getGaussianValue(affiliation, BETA_CONST);

        betaDis = new BetaDistribution(alpha, beta);

    }

    public double getPDF(double x) {
        return betaDis.density(x);
    }
    // returns a number 0, 25, 50, 75, 100 = which category to pick a news article from
    // corresponds to: [left (0), left-center (25), moderate (50), right-center (75), right (100)]
    public int getCategory() {
        return 25*(int)(betaDis.sample()*5);


        // FOR DEBUGGING
        /* Takes in the user's political affiliation number to converts it to a size 5 array with
         * probabilities for each of the five political categories:
         * [left, left-center, moderate, right-center, right]
         *
         * Example output: [.23, .43, .19, .11, .04] <-- should add to 1
         */
//        int b1 = 0, b2 = 0, b3 = 0, b4 = 0, b5 = 0;
//
//        for (int i = 0; i < 10000; i++) {
//            double b = betaDis.sample();
//            if (b > 4.0/5) b1++;
//            else if (b > 3.0/5) b2++;
//            else if (b > 2.0/5) b3++;
//            else if (b > 1.0/5) b4++;
//            else b5++;
//        }
//        Log.e("BETA", "Affiliation of " + affiliation + ": " +
//                Double.toString(b5/10000.0) + " " +
//                Double.toString(b4/10000.0) + " " +
//                Double.toString(b3/10000.0) + " " +
//                Double.toString(b2/10000.0) + " " +
//                Double.toString(b1/10000.0));

    }

    // Use gaussian bell curve. a_value = 2.980524*e^(-(x - 62.00051)^2/(2*33.06239^2))
    // b_value = y = 2.980524*e^(-(x - 37.99949)^2/(2*33.06239^2))
    // Quartic regression 4th order polynomial. a_value = y = 0.65 - 0.01583333*x + 0.003043333*x^2 - 0.00004666667*x^3 + 1.866667e-7*x^4
    // b_value = y = 1.5 + 0.0605*x + 0.0002433333*x^2 - 0.000028*x^3 + 1.866667e-7*x^4

    private static double getGaussianValue(double affiliation, double offset) {
        return 2.980524*Math.exp(-Math.pow(affiliation - offset,2)/2186.24326502);
    }

}
