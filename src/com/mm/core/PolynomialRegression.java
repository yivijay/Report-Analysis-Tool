package com.mm.core;
import Jama.Matrix;
import Jama.QRDecomposition;

public class PolynomialRegression {
    private final int N;
    private final int degree;
    private final Matrix beta;
    private double SSE;
    private double SST;

    public PolynomialRegression(double[] x, double[] y, int degree) {
        this.degree = degree;
        N = x.length;

        // build Vandermonde matrix
        double[][] vandermonde = new double[N][degree+1];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j <= degree; j++) {
                vandermonde[i][j] = Math.pow(x[i], j);
            }
        }
        Matrix X = new Matrix(vandermonde);

        // create matrix from vector
        Matrix Y = new Matrix(y, N);

        // find least squares solution
        QRDecomposition qr = new QRDecomposition(X);
        beta = qr.solve(Y);


        // mean of y[] values
        double sum = 0.0;
        for (int i = 0; i < N; i++)
            sum += y[i];
        double mean = sum / N;

        // total variation to be accounted for
        for (int i = 0; i < N; i++) {
            double dev = y[i] - mean;
            SST += dev*dev;
        }

        // variation not accounted for
        Matrix residuals = X.times(beta).minus(Y);
        SSE = residuals.norm2() * residuals.norm2();

    }

    public double beta(int j) {
        return beta.get(j, 0);
    }

    public int degree() {
        return degree;
    }

    public double R2() {
        return 1.0 - SSE/SST;
    }

    // predicted y value corresponding to x
    public double predict(double x) {
        // horner's method
        double y = 0.0;
        for (int j = degree; j >= 0; j--)
            y = beta(j) + (x * y);
        return y;
    }

    public String toString() {
        String s = "";
        int j = degree;

        // ignoring leading zero coefficients
        while (Math.abs(beta(j)) < 1E-5)
            j--;

        // create remaining terms
        for (j = j; j >= 0; j--) {
            if      (j == 0) s += String.format("%.5f ", beta(j));
            else if (j == 1) s += String.format("%.5f N + ", beta(j));
            else             s += String.format("%.5f N^%d + ", beta(j), j);
        }
        return s + "  (R^2 = " + String.format("%.5f", R2()) + ")";
    }

    public static String Caller(double[] x,double[] y)
    {
     //   x = {0,4,9,19,29};
     //   y = {0.00,0.25,1.33,3.91,6.36};
        PolynomialRegression regression = new PolynomialRegression(x, y, 2);
        System.out.println(regression);
 /*       System.out.println(regression.beta(2));
        System.out.println(regression.beta(1));
        System.out.println(regression.beta(0));
        System.out.println(regression.R2());*/
        return regression.beta(2)+","+regression.beta(1);
    }
    
}


