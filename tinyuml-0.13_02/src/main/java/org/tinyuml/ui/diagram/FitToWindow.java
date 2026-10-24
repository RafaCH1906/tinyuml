/**
 * Copyright 2007 Wei-ju Wu
 *
 * This file is part of TinyUML.
 *
 * TinyUML is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * TinyUML is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with TinyUML; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.tinyuml.ui.diagram;

import java.awt.geom.Rectangle2D;
import org.tinyuml.draw.AbstractCompositeNode;
import org.tinyuml.draw.DiagramElement;

/**
 * Calculations needed to fit all the elements of a diagram into the visible
 * area of the editor. Only the view is changed, the elements keep their
 * position and size.
 * Creado para cumplir con el requerimiento RF-005.
 *
 * @author RafaCH1906
 * @version 1.0
 */
public final class FitToWindow {

  /** Smallest scale factor that can be used. */
  public static final double MIN_SCALE = 0.1;

  /** Largest scale factor that can be used (the largest zoom of the menu). */
  public static final double MAX_SCALE = 1.5;

  /** Space in pixels left around the elements. */
  public static final double PADDING = 20;

  /**
   * Private constructor.
   */
  private FitToWindow() { }

  /**
   * Returns the rectangle that contains all the elements of the diagram.
   * Nested elements are inside their parents, so only the direct children
   * are considered.
   * @param diagram the diagram
   * @return the bounds, or null if the diagram has no elements
   */
  public static Rectangle2D getContentBounds(AbstractCompositeNode diagram) {
    Rectangle2D result = null;
    for (DiagramElement child : diagram.getChildren()) {
      Rectangle2D bounds = child.getAbsoluteBounds();
      if (result == null) {
        result = (Rectangle2D) bounds.clone();
      } else {
        result.add(bounds);
      }
    }
    return result;
  }

  /**
   * Returns the scale factor needed to display the content in the view.
   * @param content the bounds of the content
   * @param viewWidth the width of the visible area
   * @param viewHeight the height of the visible area
   * @return the scale factor, between MIN_SCALE and MAX_SCALE
   */
  public static double getScaleFactor(Rectangle2D content, double viewWidth,
    double viewHeight) {
    double contentWidth = content.getWidth() + 2 * PADDING;
    double contentHeight = content.getHeight() + 2 * PADDING;
    double scale = Math.min(viewWidth / contentWidth,
      viewHeight / contentHeight);
    return Math.max(MIN_SCALE, Math.min(MAX_SCALE, scale));
  }
}
