package org.knowm.xchart;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import org.knowm.xchart.internal.chartpart.Annotation;
import org.knowm.xchart.internal.chartpart.Chart;

public abstract class AnnotationWithXY extends Annotation {
    protected double x;
    protected double y;

	public AnnotationWithXY(double x, double y, boolean isValueInScreenSpace) {
		// TODO Auto-generated constructor stub
		super(isValueInScreenSpace);
        this.x = x;
        this.y = y;
	}

	public void setX(double x) {
    this.x = x;
  }
    public void setY(double y) {
    this.y = y;
  }
}
