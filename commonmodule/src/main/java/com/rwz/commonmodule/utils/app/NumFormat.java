package com.rwz.commonmodule.utils.app;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by rwz on 2017/8/2.
 * 数据格式化
 */

public class NumFormat {

    double f = 111231.5585;

    private static double m1 (double num , int digit) {
        if (digit < 0) {
            return num;
        }
        BigDecimal bg = new BigDecimal(num);
        return bg.setScale(digit, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * DecimalFormat转换最简便
     */
    public static String m2(double num , int digit) {
        if (digit < 0) {
            return num + "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("#.");
        String end = "0";
        for (int i = 0; i < digit; i++) {
            sb.append(end);
        }
        DecimalFormat df = new DecimalFormat(sb.toString());
       return df.format(num);
    }

    /**
     * String.format打印最简便
     */
    private static String m3(double num , int digit) {
        if (digit < 0) {
            return num + "";
        }
        return String.format("%."+ digit +"f", num);
    }

    private static String m4(double num , int digit) {
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(digit);
        return nf.format(num);
    }

    /**
     * 获取有一定精度的Double
     * @param num 源数据
     * @param digit 位数
     * @return
     */
    public static Double getPrecisionDouble(Double num, int digit) {
        //method 1
        return m1(num, digit);
    }

    public static String getPrecisionString(Double num, int digit) {
        //method 2
//        return m2(num, digit);
        //method 3
//        return m3(num, digit);
        //method 4
        return m4(num, digit);
    }

}
