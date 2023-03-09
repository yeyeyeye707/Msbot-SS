package com.badeling.msbot.infrastructure.util;


import com.badeling.msbot.infrastructure.maplegg.entity.GraphData;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.CategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.OptionalLong;
import java.util.UUID;

public class JfreeChartUtil {
    public static String createExpBarImg(List<GraphData> expData, String folder) {

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
//        expData.stream()
////                .filter(e -> e.getValue() != null && e.getValue() > 0)
////                .sorted(Comparator.comparing(Map.Entry::getKey))
//                .forEach(e -> {
//                    dataset.setValue(e.getExp(), "exp", e.getDate());
//                });
        GraphData data;
        Iterator<GraphData> i = expData.iterator();
        long lastExp = i.next().getTotalExp();
        while(i.hasNext()){
            data = i.next();
            dataset.setValue(data.getTotalExp() - lastExp, "exp", data.getDate());
            lastExp = data.getTotalExp();
        }

        OptionalLong max = expData.stream()
                .mapToLong(GraphData::getExp)
                .max();
        JFreeChart chart = createChart(dataset, max);


        String imageName = UUID.randomUUID().toString().replaceAll("-", "") + ".png";
        String path = folder + imageName;
        boolean r = saveAsFile(chart, path, 315, 100 + 20 * expData.size());
        return r ? imageName : null;
//        return imageName;
    }


    static class ExpLabelGenerator implements CategoryItemLabelGenerator {
        @Override
        public String generateRowLabel(CategoryDataset categoryDataset, int i) {
            return null;
        }

        @Override
        public String generateColumnLabel(CategoryDataset categoryDataset, int i) {
            return null;
        }

        @Override
        public String generateLabel(CategoryDataset categoryDataset, int i, int i1) {
            Number number = categoryDataset.getValue(i, i1);
            return NumberSuffixesUtil.getExpStr(number.longValue());
        }
    }

    private static JFreeChart createChart(CategoryDataset dataset, OptionalLong max) {
        JFreeChart jfreechart = ChartFactory.createBarChart(
                null,
                null,//                "日期",
                null,//"经验",
                dataset,
                PlotOrientation.HORIZONTAL,
                false, false, false);

        // 设置外层图片 无边框 无背景色 背景图片透明
//        jfreechart.setBackgroundPaint(null);
//        jfreechart.setBackgroundImageAlpha(0.0f);
        jfreechart.setBorderVisible(false);

        jfreechart.setBackgroundPaint(Color.BLACK);

        TextTitle title = new TextTitle("努力程度");
        title.setPaint(Color.WHITE);
//        title.setBackgroundPaint(Color.RED);
        jfreechart.setTitle(title);



        //柱状图显示数值
        CategoryPlot categoryplot = jfreechart.getCategoryPlot();
        categoryplot.setOutlinePaint(null);
//        categoryplot.setBackgroundPaint(null);
//        categoryplot.setBackgroundAlpha(0.0f);
        categoryplot.setBackgroundPaint(Color.BLACK);
        CategoryItemRenderer categoryitemrenderer = categoryplot.getRenderer();
        categoryitemrenderer.setBaseItemLabelsVisible(true);
        categoryitemrenderer.setBaseItemLabelGenerator(new ExpLabelGenerator());
        categoryitemrenderer.setBaseItemLabelPaint(Color.WHITE);

//        ItemLabelPosition labelPosition = new ItemLabelPosition(ItemLabelAnchor.OUTSIDE1, TextAnchor.BASELINE_LEFT);
//        categoryitemrenderer.setBasePositiveItemLabelPosition(labelPosition);

        //数值轴 y轴
        ValueAxis valueAxis = categoryplot.getRangeAxis();
        if (max.isPresent()) {
            valueAxis.setUpperBound(max.getAsLong() * 1.3);
        }
        valueAxis.setTickLabelsVisible(false);

        //分类轴 x轴
        CategoryAxis categoryAxis = categoryplot.getDomainAxis();
        categoryAxis.setTickLabelPaint(Color.WHITE);

        return jfreechart;
    }

    public static boolean saveAsFile(JFreeChart chart, String outputPath, int weight, int height) {
        boolean result = true;
        FileOutputStream out = null;
        try {
            File outFile = new File(outputPath);
            if (!outFile.getParentFile().exists()) {
                outFile.getParentFile().mkdirs();
            }
            out = new FileOutputStream(outputPath);
            ChartRenderingInfo renderingInfo = new ChartRenderingInfo();

            // 保存为PNG
            ChartUtilities.writeChartAsPNG(out, chart, weight, height);
            // 保存为JPEG
            // ChartUtilities.writeChartAsJPEG(out, chart, weight, height);
            out.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            result = false;
        } catch (IOException e) {
            e.printStackTrace();
            result = false;
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    result = false;
                }
            }
        }

        return result;
    }
}
