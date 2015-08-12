package com.reporting.chart;

import java.awt.Color;
import java.text.DecimalFormat;

import org.jfree.chart.ChartColor;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

import com.reporting.enums.ReportLabels;

public class PieChart {
	public static JFreeChart generate2DPieChart(int paramInt1, int paramInt2, int paramInt3) {
		DefaultPieDataset localDefaultPieDataset = new DefaultPieDataset();
		localDefaultPieDataset.setValue(ReportLabels.PASS.getLabel(), paramInt1);
		localDefaultPieDataset.setValue(ReportLabels.FAIL.getLabel(), paramInt2);
		localDefaultPieDataset.setValue(ReportLabels.SKIP.getLabel(), paramInt3);
		JFreeChart localJFreeChart =
				ChartFactory.createPieChart(ReportLabels.PIE_CHART_LABEL.getLabel(), localDefaultPieDataset, true, true, true);
		PiePlot localPiePlot = (PiePlot) localJFreeChart.getPlot();
		localPiePlot.setCircular(true);
		localPiePlot.setForegroundAlpha(0.9F);
		localPiePlot.setBackgroundAlpha(0.9F);
		localPiePlot.setSectionPaint(ReportLabels.PASS.getLabel(), ChartColor.DARK_GREEN);
		localPiePlot.setSectionPaint(ReportLabels.FAIL.getLabel(), ChartColor.RED);
		localPiePlot.setSectionPaint(ReportLabels.SKIP.getLabel(), ChartColor.BLUE);
		localPiePlot.setExplodePercent(ReportLabels.PASS.getLabel(), 0.05D);
		localPiePlot.setExplodePercent(ReportLabels.FAIL.getLabel(), 0.05D);
		localPiePlot.setExplodePercent(ReportLabels.SKIP.getLabel(), 0.05D);
		localPiePlot.setOutlinePaint(Color.BLACK);
		localPiePlot.setOutlineVisible(false);
		Color localColor = new Color(255, 255, 255, 0);
		localJFreeChart.setBackgroundPaint(localColor);
		localPiePlot.setBackgroundPaint(localColor);
		StandardPieSectionLabelGenerator localStandardPieSectionLabelGenerator =
				new StandardPieSectionLabelGenerator("{2}", new DecimalFormat("0"), new DecimalFormat("0%"));
		localPiePlot.setLabelGenerator(localStandardPieSectionLabelGenerator);
		return localJFreeChart;
	}
}
