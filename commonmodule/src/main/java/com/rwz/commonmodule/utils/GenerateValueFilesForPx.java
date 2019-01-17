package com.rwz.commonmodule.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;

/**
 * Created by zhy on 15/5/3.
 * 鸿洋大神自动生层dimens文件（用于dp格式的适配）
 * https://github.com/hongyangAndroid/Android_Blog_Demos/tree/master/blogcodes/src/main/java/com/zhy/blogcodes/genvalues
 *
 *  该方案采用分辨率来适配
 *
 */
class GenerateValueFilesForPx {

    //支持的资源目录
    private static final String SUPPORT_DP =
            "320,480;" +
            "375,667;" +
            "480,800;" +
            "480,854;" +
            "540,960;" +
            "600,1024;" +
            "720,1184;" +
            "720,1196;" +
            "720,1280;" +
            "750,1344;" +
            "768,1024;" +
            "768,1280;" +
            "800,1280;" +
            "1080,1776;" +
            "1080,1794;" +
            "1080,1812;" +
            "1080,1920;" +
            "1080,2030;" +
            "1440,2392;" +
            "1440,2560;";
    //生成文件保存路径
    private static final String dirStr = "./res/basePx";
    //生成的资源文件命名
    private static final String fileNameForWidth = "base_width.xml";
    private static final String fileNameForHeight = "base_height.xml";
    private static final String fileNameForFont = "base_font.xml";
    private static final boolean isSupportHeight = true;//是否支持纵向
    //连续统计到的最大dp
    private static final int MAX_VALUE = 200;
    //以xxhdpi为基础(设计的基准)
    private final static float baseH = 375f;
    private final static float baseV = 667f;
    //横向单位模板
    private final static String WTemplate = "<dimen name=\"h_{0}\">{1}px</dimen>\n";
    //纵向单位模板
    private final static String HTemplate = "<dimen name=\"v_{0}\">{1}px</dimen>\n";
    //字体模板
    private final static String FONT_TEMPLATE = "<dimen name=\"text_{0}\">{1}px</dimen>\n";
    //空行
    private static final String SPACE = "\t";
    //资源文件夹命名 e.g. values-mdip
    private final static String VALUE_TEMPLATE = "values-{0}x{1}";
    //支持的所有字体尺寸集合
    private static final LinkedHashMap<String, Float> font = new LinkedHashMap();

    static {
        //字体尺寸
        font.put("50", 50f);
        font.put("36", 36f);
        font.put("26", 26f);
        font.put("22", 22f);
        font.put("20", 20f);
        font.put("18", 18f);
        font.put("17", 17f);
        font.put("16", 16f);
        font.put("15", 15f);
        font.put("14", 14f);
        font.put("13", 13f);
        font.put("12", 12f);
        font.put("11", 11f);
        font.put("10", 10f);
        font.put("9" , 9f);
        font.put("8" , 8f);
    }

    private final String supportStr = SUPPORT_DP;

    private GenerateValueFilesForPx() {
        System.out.println(supportStr);
        File dir = new File(dirStr);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        System.out.println(dir.getAbsoluteFile());
    }

    private void generate() {
        String[] vals = supportStr.split(";");
        for (String val : vals) {
            String[] wh = val.split(",");
            generateXmlFile(wh[0], wh[1]);
        }
    }

    /**
     * @param
     */
    private void generateXmlFile(String h, String v) {
        //与基数的换算倍率
        float cellH = Integer.parseInt(h) / baseH;
        float cellV = Integer.parseInt(v) / baseV;
        System.out.println("generateXmlFile" + " , h = " + h + " , cellH = " + cellH + ", cellV = " + cellV);

        //横向
        StringBuffer sbForWidth = applyMode(h, WTemplate, cellH);

        //纵向
        StringBuffer sbForHeight = applyMode(v, HTemplate, cellV);

        //字体
        StringBuffer sbForFont = applyFont(cellH);

        //写入到文件
        String pathname = dirStr + File.separator + VALUE_TEMPLATE.replace("{0}", h + "").replace("{1}", v + "");
        System.out.println("generateXmlFile, " + "pathname = " + pathname);
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
        System.out.println("applyMode, value = " + value + ", cell = " + cell);
        String space = SPACE;
        //最大统计到200dp
        int max = MAX_VALUE;
        for (int i = 1; i < max; i++) {
            valueSb.append(space).append(template.replace("{0}", i + "").replace("{1}", change(cell * i) + ""));
        }
        valueSb.append(space).append(template.replace("{0}", max + "").replace("{1}", change(cell * max) + ""));

        //特殊情况-10dp、 -20dp
        //0.5dp
        valueSb.append(space).append(template.replace("{0}", "0").replace("{1}", change(cell * (0)) + ""));
        valueSb.append(space).append(template.replace("{0}", "0_5").replace("{1}", change(cell * 0.5f) + ""));
        valueSb.append(space).append(template.replace("{0}", "2_").replace("{1}", change(cell * (-2)) + ""));
        valueSb.append(space).append(template.replace("{0}", "4_").replace("{1}", change(cell * (-4)) + ""));
        valueSb.append(space).append(template.replace("{0}", "5_").replace("{1}", change(cell * (-5)) + ""));
        valueSb.append(space).append(template.replace("{0}", "6_").replace("{1}", change(cell * (-6)) + ""));
        valueSb.append(space).append(template.replace("{0}", "8_").replace("{1}", change(cell * (-8)) + ""));
        valueSb.append(space).append(template.replace("{0}", "10_").replace("{1}", change(cell * (-10)) + ""));
        valueSb.append(space).append(template.replace("{0}", "20_").replace("{1}", change(cell * (-20)) + ""));
        valueSb.append(space).append(template.replace("{0}", "240").replace("{1}", change(cell * (240)) + ""));
        valueSb.append(space).append(template.replace("{0}", "260").replace("{1}", change(cell * (260)) + ""));
        valueSb.append(space).append(template.replace("{0}", "270").replace("{1}", change(cell * (270)) + ""));
        valueSb.append(space).append(template.replace("{0}", "300").replace("{1}", change(cell * (300)) + ""));
        valueSb.append(space).append(template.replace("{0}", "320").replace("{1}", change(cell * (320)) + ""));
        valueSb.append(space).append(template.replace("{0}", "400").replace("{1}", change(cell * (400)) + ""));
        valueSb.append(space).append(template.replace("{0}", "450").replace("{1}", change(cell * (450)) + ""));
        valueSb.append(space).append(template.replace("{0}", "600").replace("{1}", change(cell * (600)) + ""));
        valueSb.append(space).append(template.replace("{0}", "800").replace("{1}", change(cell * (800)) + ""));
        valueSb.append("</resources>");

        return valueSb;
    }

    private StringBuffer applyFont(float cell) {
        //字体
        StringBuffer sbForFont = new StringBuffer();
        sbForFont.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
        sbForFont.append("<resources>\n");
        Set<String> strings = font.keySet();
        Iterator<String> iterator = strings.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            Float value = font.get(key);
            sbForFont.append(SPACE).append(FONT_TEMPLATE.replace("{0}", key).replace("{1}", change(cell * value) + ""));
        }
        sbForFont.append("</resources>");
        return sbForFont;
    }

    private static float change(float a) {
        int temp = (int) (a * 100);
        return temp / 100f;
    }

    public static void main(String[] args) {
        new GenerateValueFilesForPx().generate();
    }

}
