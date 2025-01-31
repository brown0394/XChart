package org.knowm.xchart;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.knowm.xchart.internal.Utils;
import org.knowm.xchart.internal.chartpart.AxisPair;
import org.knowm.xchart.internal.chartpart.Legend_Marker;
import org.knowm.xchart.internal.chartpart.Plot_Category;
import org.knowm.xchart.internal.series.Series;
import org.knowm.xchart.internal.series.Series.DataType;
import org.knowm.xchart.internal.style.SeriesColorMarkerLineStyle;
import org.knowm.xchart.style.CategoryStyler;
import org.knowm.xchart.style.Styler.ChartTheme;
import org.knowm.xchart.style.theme.Theme;

/** @author timmolter */
public class CategoryChart extends AbstractChart<CategoryStyler, CategorySeries> {

  /**
   * Constructor - the default Chart Theme will be used (XChartTheme)
   *
   * @param width
   * @param height
   */
  public CategoryChart(int width, int height) {

    super(width, height, new CategoryStyler());
    axisPair = new AxisPair<CategoryStyler, CategorySeries>(this);
    plot = new Plot_Category<CategoryStyler, CategorySeries>(this);
    legend = new Legend_Marker<CategoryStyler, CategorySeries>(this);
    paintTarget.addChartPart(axisPair);
    paintTarget.addChartPart(plot);
    paintTarget.addChartPart(chartTitle);
    paintTarget.addChartPart(legend);
  }

  /**
   * Constructor
   *
   * @param width
   * @param height
   * @param theme - pass in a instance of Theme class, probably a custom Theme.
   */
  public CategoryChart(int width, int height, Theme theme) {

    this(width, height);
    styler.setTheme(theme);
  }

  /**
   * Constructor
   *
   * @param width
   * @param height
   * @param chartTheme - pass in the desired ChartTheme enum
   */
  public CategoryChart(int width, int height, ChartTheme chartTheme) {

    this(width, height, chartTheme.newInstance(chartTheme));
  }

  /**
   * Constructor
   *
   * @param chartBuilder
   */
  public CategoryChart(CategoryChartBuilder chartBuilder) {

    this(chartBuilder.getWidth(), chartBuilder.getHeight(), chartBuilder.getChartTheme());
    setTitle(chartBuilder.getTitle());
    setXAxisTitle(chartBuilder.xAxisTitle);
    setYAxisTitle(chartBuilder.yAxisTitle);
  }

  /**
   * Add a series for a Category type chart using using double arrays
   *
   * @param seriesName
   * @param xData the X-Axis data
   * @param yData the Y-Axis data
   * @return A Series object that you can set properties on
   */
  public CategorySeries addSeries(String seriesName, double[] xData, double[] yData) {

    return addSeries(seriesName, xData, yData, null);
  }

  /**
   * Add a series for a Category type chart using using double arrays with error bars
   *
   * @param seriesName
   * @param xData the X-Axis data
   * @param yData the Y-Axis data
   * @param errorBars the error bar data
   * @return A Series object that you can set properties on
   */
  public CategorySeries addSeries(
      String seriesName, double[] xData, double[] yData, double[] errorBars) {

    return addSeries(
        seriesName,
        Utils.getNumberListFromDoubleArray(xData),
        Utils.getNumberListFromDoubleArray(yData),
        Utils.getNumberListFromDoubleArray(errorBars));
  }

  /**
   * Add a series for a Category type chart using using int arrays
   *
   * @param seriesName
   * @param xData the X-Axis data
   * @param yData the Y-Axis data
   * @return A Series object that you can set properties on
   */
  public CategorySeries addSeries(String seriesName, int[] xData, int[] yData) {

    return addSeries(seriesName, xData, yData, null);
  }

  /**
   * Add a series for a Category type chart using using int arrays with error bars
   *
   * @param seriesName
   * @param xData the X-Axis data
   * @param yData the Y-Axis data
   * @param errorBars the error bar data
   * @return A Series object that you can set properties on
   */
  public CategorySeries addSeries(String seriesName, int[] xData, int[] yData, int[] errorBars) {

    return addSeries(
        seriesName,
        Utils.getNumberListFromIntArray(xData),
        Utils.getNumberListFromIntArray(yData),
        Utils.getNumberListFromIntArray(errorBars));
  }

  /**
   * Add a series for a Category type chart using Lists
   *
   * @param seriesName
   * @param xData the X-Axis data
   * @param yData the Y-Axis data
   * @return A Series object that you can set properties on
   */
  public CategorySeries addSeries(String seriesName, List<?> xData, List<? extends Number> yData) {

    return addSeries(seriesName, xData, yData, null);
  }

  /**
   * Add a series for a Category type chart using Lists with error bars
   *
   * @param seriesName
   * @param xData the X-Axis data
   * @param yData the Y-Axis data
   * @param errorBars the error bar data
   * @return A Series object that you can set properties on
   */
  public CategorySeries addSeries(
      String seriesName,
      List<?> xData,
      List<? extends Number> yData,
      List<? extends Number> errorBars) {

    // Sanity checks
    sanityCheck(seriesName, xData, yData, errorBars);
    xData = generateXDataIfNullForSeries(xData, yData);
    CategorySeries series = new CategorySeries(seriesName, xData, yData, errorBars, getDataType(xData));
    seriesMap.put(seriesName, series);
    return series;
  }

  private List<?> generateXDataIfNullForSeries(List<?> xData, List<? extends Number> yData) {
	if (xData == null) {
    	xData = Utils.getGeneratedDataAsList(yData.size());
    }
	return xData;
  }

  private DataType getDataType(List<?> data) {
    Iterator<?> itr = data.iterator();
    if (!itr.hasNext()) {
        throw new IllegalArgumentException(
                "List of data should contain next Iteration to get data type!!!");
    }
 
    return getAxisType(itr.next());
  }

private DataType getAxisType(Object dataPoint) {
    DataType axisType;

    DataTypeFactory dataTypeFactory = new DataTypeFactory();
    axisType = dataTypeFactory.getType(dataPoint);
    
	return axisType;
}

  /**
   * Update a series by updating the X-Axis, Y-Axis and error bar data
   *
   * @param seriesName
   * @param newXData - set null to be automatically generated as a list of increasing Integers
   *     starting from 1 and ending at the size of the new Y-Axis data list.
   * @param newYData
   * @param newErrorBarData - set null if there are no error bars
   * @return
   */
  public CategorySeries updateCategorySeries(
      String seriesName,
      List<?> newXData,
      List<? extends Number> newYData,
      List<? extends Number> newErrorBarData) {

    CategorySeries series = getSeriesMap().get(seriesName);
    checkSeriesValidity(seriesName, series);
    
    series.replaceData(generateXDataIfNullForReplace(newXData, newYData), newYData, newErrorBarData);
    return series;
  }

  private List<?> generateXDataIfNullForReplace(List<?> newXData, List<? extends Number> newYData) {
	List<?> xData;
    
	if (newXData == null) {
      // generate X-Data
      List<Integer> generatedXData = new ArrayList<Integer>();
      for (int i = 1; i <= newYData.size(); i++) {
        generatedXData.add(i);
      }
      xData = generatedXData;
    } else {
      xData = newXData;
    }
	return xData;
  }

  /**
   * Update a series by updating the X-Axis, Y-Axis and error bar data
   *
   * @param seriesName
   * @param newXData - set null to be automatically generated as a list of increasing Integers
   *     starting from 1 and ending at the size of the new Y-Axis data list.
   * @param newYData
   * @param newErrorBarData - set null if there are no error bars
   * @return
   */
  public CategorySeries updateCategorySeries(
      String seriesName, double[] newXData, double[] newYData, double[] newErrorBarData) {

    return updateCategorySeries(
        seriesName,
        Utils.getNumberListFromDoubleArray(newXData),
        Utils.getNumberListFromDoubleArray(newYData),
        Utils.getNumberListFromDoubleArray(newErrorBarData));
  }
  ///////////////////////////////////////////////////
  // Internal Members and Methods ///////////////////
  ///////////////////////////////////////////////////

  private void sanityCheck(
      String seriesName,
      List<?> xData,
      List<? extends Number> yData,
      List<? extends Number> errorBars) {

    seriesNameDuplicateCheck(seriesName);
    new SanityXChecker(new SanityYChecker(yData), xData).checkSanity();
    if (xData.size() != yData.size()) {
  	  throw new IllegalArgumentException("X and Y-Axis sizes are not the same!!!");
    }
    if (errorBars != null && errorBars.size() != yData.size()) {
      throw new IllegalArgumentException("Error bars and Y-Axis sizes are not the same!!!");
    }
  }

  @Override
  public void paint(Graphics2D graphics, int width, int height) {

    settingPaint(width, height);

    doPaint(graphics);
  }

  @Override
  protected void specificSetting() {
	 // set the series render styles if they are not set. Legend and Plot need it.
	 for (CategorySeries seriesCategory : getSeriesMap().values()) {
	   // would be directly set
	   if (seriesCategory.getChartCategorySeriesRenderStyle() == null) { // wasn't overridden, use default from Style Manager
	     seriesCategory.setChartCategorySeriesRenderStyle(getStyler().getDefaultSeriesRenderStyle());
	   }
	 }
	 setSeriesStyles();
  }


  /** set the series color, marker and line style based on theme */
  @Override
  protected void setSeriesDefaultForNullPart(Series series, SeriesColorMarkerLineStyle seriesColorMarkerLineStyle) {
	  CategorySeries categorySeries = (CategorySeries) series;
	  if (categorySeries.getLineStyle() == null) { // wasn't set manually
		  categorySeries.setLineStyle(seriesColorMarkerLineStyle.getStroke());
	  }
	  if (categorySeries.getLineColor() == null) { // wasn't set manually
		  categorySeries.setLineColor(seriesColorMarkerLineStyle.getColor());
	  }
	  if (categorySeries.getFillColor() == null) { // wasn't set manually
		  categorySeries.setFillColor(seriesColorMarkerLineStyle.getColor());
	  }
	  if (categorySeries.getMarker() == null) { // wasn't set manually
		  categorySeries.setMarker(seriesColorMarkerLineStyle.getMarker());
	  }
	  if (categorySeries.getMarkerColor() == null) { // wasn't set manually
		  categorySeries.setMarkerColor(seriesColorMarkerLineStyle.getColor());
	  }
  }

  private void checkSeriesValidity(String seriesName, Series series) {
	if (series == null) {
      throw new IllegalArgumentException("Series name >" + seriesName + "< not found!!!");
    }
  }

}
