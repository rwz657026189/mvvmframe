package com.rwz.commonmodule.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

/**
 * Created by zhy on 15/5/3.
 * 鸿洋大神自动生层dimens文件（用于dp格式的适配）
 * https://github.com/hongyangAndroid/Android_Blog_Demos/tree/master/blogcodes/src/main/java/com/zhy/blogcodes/genvalues
 *  采用最小宽度适配 dp
 */
class GenerateValueFilesForDp {

    //支持的资源目录
    private static final String SUPPORT_DP = "240;280;320;360;375;400;450;480;520;560";
    //支持的所有字体尺寸; ~ 表示区间，可多个
    private static final String SUPPORT_FONT = "8~26;36;50";
    //支持的所有尺寸
    private static final String SUPPORT_SIZE = "0~200;0.5;210;220;230;240;250;260;270;280;290;300;320;350;375;400;450;600;667;800;-10~-1;-20";
    //生成文件保存路径
    private static final String dirStr = "./res/baseDimen";
    //生成的资源文件命名
    private static final String fileNameForWidth = "base_width.xml";
    private static final String fileNameForHeight = "base_height.xml";
    private static final String fileNameForFont = "base_font.xml";
    private static final boolean isSupportHeight = true;//是否支持纵向
    //以xxhdpi为基础(设计的基准)
    private final static float baseDp = 375f;
    //横向单位模板
    private final static String WTemplate = "<dimen name=\"h_{0}\">{1}dp</dimen>\n";
    //纵向单位模板
    private final static String HTemplate = "<dimen name=\"v_{0}\">{1}dp</dimen>\n";
    //字体模板
    private final static String FONT_TEMPLATE = "<dimen name=\"text_{0}\">{1}dp</dimen>\n";
    //空行
    private static final String SPACE = "\t";
    //资源文件夹命名 e.g. values-mdip
    private final static String DEFAULT_VALUE_TEMPLATE = "values";
    private final static String VALUE_TEMPLATE = "values-sw%sdp";


    /**
     * mdpi : 120dpi-160dpi
     * hdpi : 160dpi-240dpi
     * xhdpi : 240dpi-320dpi
     * xxhdpi : 320dpi-480dpi
     * xxxhdpi : 480dpi-640dpi
     */
    private final String supportStr = SUPPORT_DP;

    private GenerateValueFilesForDp() {
        System.out.println(supportStr);
        File dir = new File(dirStr);
        if (!dir.exists()) {
            dir.mkdir();
        }
        System.out.println(dir.getAbsoluteFile());
    }

    private void generate() {
        String[] vals = supportStr.split(";");
        for (String val : vals) {
            generateXmlFile(val);
        }
    }

    /**
     * @param val e.g. 240;320;360...
     */
    private void generateXmlFile(String val) {
        //与基数的换算倍率
        float cell = Integer.parseInt(val) / baseDp;
        System.out.println("generateXmlFile" + " , val = " + val + " , cell = " + cell);

        //横向
        StringBuffer sbForWidth = applyMode(val, WTemplate, cell);

        //纵向
        StringBuffer sbForHeight = applyMode(val, HTemplate, cell);

        //字体
        StringBuffer sbForFont = applyFont(cell);

        //写入到文件
        String pathname = dirStr + File.separator + String.format(VALUE_TEMPLATE, val);
        System.out.println("generateXmlFile, " + "pathname = " + pathname);
        writeToFile(sbForWidth, sbForHeight, sbForFont, pathname);
        if (cell == 1f) {
            //写一份到默认目录下
            pathname = dirStr + File.separator + DEFAULT_VALUE_TEMPLATE;
            writeToFile(sbForWidth, sbForHeight, sbForFont, pathname);
        }
    }

    private void writeToFile(StringBuffer sbForWidth, StringBuffer sbForHeight, StringBuffer sbForFont, String pathname) {
        File fileDir = new File(pathname);//
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }

        try {
            //写x
            File layXFile = new File(fileDir.getAbsolutePath(), fileNameForWidth);
            PrintWriter pw = new PrintWriter(new FileOutputStream(layXFile));
            pw.print(sbForWidth.toString());
            pw.close();
            //写y
            if (isSupportHeight) {
                File layYFile = new File(fileDir.getAbsolutePath(), fileNameForHeight);
                pw = new PrintWriter(new FileOutputStream(layYFile));
                pw.print(sbForHeight.toString());
                pw.close();
            }
            //写common
            File fontFile = new File(fileDir.getAbsolutePath(), fileNameForFont);
            pw = new PrintWriter(new FileOutputStream(fontFile));
            pw.print(sbForFont.toString());
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private StringBuffer applyMode(String value, String template, float cell) {
        StringBuffer valueSb = new StringBuffer();
        valueSb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
        valueSb.append("<resources>\n");
        System.out.println("applyMode, value = " + value + " , baseDp = " + baseDp + ", cell = " + cell);
        String space = SPACE;
        //最大统计到200dp
        String[] split = SUPPORT_SIZE.split(";");
        boolean isFirst = true;
        for (String s : split) {
            String[] sp = s.split("~");
            float start = Float.parseFloat(sp[0]);
            float end = sp.length > 1 ? Float.parseFloat(sp[1]) : -1 * Integer.MIN_VALUE;
            if (start < end) {
                if (!isFirst) {
                    valueSb.append("\n");
                }
                isFirst = false;
                for (float i = start; i <= end; i++) {
                    String key = i < 0 ? ((int)(-1 * i) + "_") : (String.valueOf((int)i));
                    valueSb.append(space).append(template.replace("{0}", key).replace("{1}", change(cell * i) + ""));
                }
                valueSb.append("\n");
            } else {
                isFirst = false;
                String key = start < 0 ? (-1 * start + "_") : (String.valueOf((int)start));
                if ((start+"").startsWith("0.")) {
                    key = (start + "").replace("0.", "0_");
                }
                valueSb.append(space).append(template.replace("{0}", key).replace("{1}", change(cell * start) + ""));
            }
        }
        valueSb.append("</resources>");
        return valueSb;
    }

    private StringBuffer applyFont(float cell) {
        //字体
        StringBuffer sbForFont = new StringBuffer();
        sbForFont.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
        sbForFont.append("<resources>\n");
        String[] split = SUPPORT_FONT.split(";");
        for (String s : split) {
            String[] value = s.split("~");
            int start = Integer.valueOf(value[0]);
            int end = value.length > 1 ? Integer.valueOf(value[1]) : -1;
            if (end > start) {
                for (int i = start; i <= end; i++) {
                    sbForFont.append(SPACE)
                            .append(FONT_TEMPLATE.replace("{0}", String.valueOf(i))
                                    .replace("{1}",change(cell * i) + ""));
                }
            } else {
                sbForFont.append(SPACE)
                        .append(FONT_TEMPLATE.replace("{0}", String.valueOf(start))
                                .replace("{1}",change(cell * start) + ""));
            }
        }
        sbForFont.append("</resources>");
        return sbForFont;
    }

    private static float change(float a) {
        int temp = (int) (a * 100);
        return temp / 100f;
    }

    public static void main(String[] args) {
        new GenerateValueFilesForDp().generate();
    }

}
