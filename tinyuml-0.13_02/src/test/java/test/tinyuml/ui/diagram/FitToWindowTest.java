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
package test.tinyuml.ui.diagram;

import java.awt.Dimension;
import java.awt.geom.Rectangle2D;
import javax.swing.JScrollPane;
import junit.framework.TestCase;
import org.tinyuml.model.ElementType;
import org.tinyuml.model.UmlModelImpl;
import org.tinyuml.ui.diagram.DiagramEditor;
import org.tinyuml.ui.diagram.FitToWindow;
import org.tinyuml.umldraw.shared.UmlNode;
import org.tinyuml.umldraw.structure.StructureDiagram;

/**
 * A test class for FitToWindow and DiagramEditor.fitToWindow() (RF-005).
 * @author RafaCH1906
 * @version 1.0
 */
public class FitToWindowTest extends TestCase {

  private StructureDiagram diagram;

  /**
   * {@inheritDoc}
   */
  @Override
  protected void setUp() {
    diagram = new StructureDiagram(new UmlModelImpl());
    diagram.setSize(2000, 2000);
  }

  /**
   * Adds a node at the specified position.
   * @param x the x position
   * @param y the y position
   * @param w the width
   * @param h the height
   * @return the node
   */
  private UmlNode addNode(double x, double y, double w, double h) {
    UmlNode node = diagram.getElementFactory().createNode(ElementType.CLASS);
    diagram.addChild(node);
    node.setAbsolutePos(x, y);
    node.setSize(w, h);
    return node;
  }

  /**
   * An empty diagram has no content bounds.
   */
  public void testEmptyDiagram() {
    assertNull(FitToWindow.getContentBounds(diagram));
  }

  /**
   * The content bounds contain all the elements.
   */
  public void testContentBounds() {
    addNode(100, 100, 50, 50);
    addNode(400, 300, 100, 80);
    Rectangle2D bounds = FitToWindow.getContentBounds(diagram);
    assertEquals(100.0, bounds.getX(), 0.01);
    assertEquals(100.0, bounds.getY(), 0.01);
    assertEquals(500.0, bounds.getMaxX(), 0.01);
    assertEquals(380.0, bounds.getMaxY(), 0.01);
  }

  /**
   * Content larger than the view is zoomed out so it fits.
   */
  public void testScaleFactorZoomOut() {
    Rectangle2D content = new Rectangle2D.Double(0, 0,
      2000 - 2 * FitToWindow.PADDING, 1000 - 2 * FitToWindow.PADDING);
    assertEquals(0.4, FitToWindow.getScaleFactor(content, 800, 600), 0.001);
  }

  /**
   * The scale factor is limited to the allowed range.
   */
  public void testScaleFactorLimits() {
    Rectangle2D small = new Rectangle2D.Double(0, 0, 10, 10);
    assertEquals(FitToWindow.MAX_SCALE,
      FitToWindow.getScaleFactor(small, 800, 600), 0.001);
    Rectangle2D huge = new Rectangle2D.Double(0, 0, 100000, 100000);
    assertEquals(FitToWindow.MIN_SCALE,
      FitToWindow.getScaleFactor(huge, 800, 600), 0.001);
  }

  /**
   * Fitting the diagram does not change the position or the size of the
   * elements, and all of them end up in the visible area.
   */
  public void testFitToWindowKeepsElements() {
    UmlNode node1 = addNode(100, 100, 80, 60);
    UmlNode node2 = addNode(1500, 1200, 80, 60);
    DiagramEditor editor = new DiagramEditor(null, diagram);
    JScrollPane scrollPane = new JScrollPane(editor);
    scrollPane.getViewport().setExtentSize(new Dimension(800, 600));
    scrollPane.getViewport().setSize(800, 600);
    // the editor moves the diagram origin, so the positions are taken here
    double x1 = node1.getAbsoluteX1(), y1 = node1.getAbsoluteY1();
    double x2 = node2.getAbsoluteX1(), y2 = node2.getAbsoluteY1();

    editor.fitToWindow();

    assertEquals(x1, node1.getAbsoluteX1(), 0.01);
    assertEquals(y1, node1.getAbsoluteY1(), 0.01);
    assertEquals(x2, node2.getAbsoluteX1(), 0.01);
    assertEquals(y2, node2.getAbsoluteY1(), 0.01);
    assertEquals(80.0, node1.getSize().getWidth(), 0.01);
    assertEquals(60.0, node2.getSize().getHeight(), 0.01);

    // all the content is inside the visible area
    Rectangle2D content = FitToWindow.getContentBounds(diagram);
    double scale = FitToWindow.getScaleFactor(content, 800, 600);
    java.awt.Rectangle visible = scrollPane.getViewport().getViewRect();
    assertTrue(visible.contains(content.getMinX() * scale,
      content.getMinY() * scale));
    assertTrue(visible.contains(content.getMaxX() * scale,
      content.getMaxY() * scale));
  }
}
