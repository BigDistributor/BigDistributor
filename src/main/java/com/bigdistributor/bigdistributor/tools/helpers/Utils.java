package net.preibisch.bigdistributor.tools.helpers;

public class Utils {

    public static <T>  String toString(double[] val) {
        String string = "{ ";
        for (int i = 0; i < val.length;i++) {
            string +=  String.valueOf(val[i]);
            if(i<val.length-1)  string +=  " - ";
        }
        string+= " }";
        return string;
    }

    public static String toString(int[] val) {
        String string = "{ ";
        for (int i = 0; i < val.length;i++) {
            string +=  String.valueOf(val[i]);
            if(i<val.length-1)  string +=  " - ";
        }
        string+= " }";
        return string;
    }
}

