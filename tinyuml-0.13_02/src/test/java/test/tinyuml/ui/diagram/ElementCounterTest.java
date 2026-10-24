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

import junit.framework.TestCase;
import org.tinyuml.draw.CompositeElement;
import org.tinyuml.model.ElementType;
import org.tinyuml.model.RelationType;
import org.tinyuml.model.UmlModelImpl;
import org.tinyuml.ui.diagram.ElementCounter;
import org.tinyuml.umldraw.shared.UmlConnection;
import org.tinyuml.umldraw.shared.UmlNode;
import org.tinyuml.umldraw.structure.DiagramElementFactory;
import org.tinyuml.umldraw.structure.StructureDiagram;

/**
 * A test class for ElementCounter (RF-001).
 * @author RafaCH1906
 * @version 1.0
 */
public class ElementCounterTest extends TestCase {

  private StructureDiagram diagram;
  private DiagramElementFactory factory;

  /**
   * {@inheritDoc}
   */
  @Override
  protected void setUp() {
    diagram = new StructureDiagram(new UmlModelImpl());
    factory = diagram.getElementFactory();
  }

  /**
   * Adds a new node of the specified type to the diagram.
   * @param type the element type
   * @return the new node
   */
  private UmlNode addNode(ElementType type) {
    UmlNode node = factory.createNode(type);
    diagram.addChild(node);
    return node;
  }

  /**
   * An empty diagram shows zero in every category.
   */
  public void testEmptyDiagram() {
    ElementCounter counter = new ElementCounter(diagram);
    assertEquals(0, counter.getTotalCount());
    assertEquals("Total Items: 00 | Package: 00 | Class: 00 | Component: 00"
      + " | Note: 00", counter.getStatusText());
  }

  /**
   * A null diagram is handled like an empty one.
   */
  public void testNullDiagram() {
    assertEquals(0, new ElementCounter(null).getTotalCount());
  }

  /**
   * Each element type is counted in its category.
   */
  public void testCountByCategory() {
    addNode(ElementType.PACKAGE);
    addNode(ElementType.PACKAGE);
    addNode(ElementType.CLASS);
    addNode(ElementType.CLASS);
    addNode(ElementType.COMPONENT);
    addNode(ElementType.COMPONENT);
    ElementCounter counter = new ElementCounter(diagram);
    assertEquals(2, counter.getPackageCount());
    assertEquals(2, counter.getClassCount());
    assertEquals(2, counter.getComponentCount());
    assertEquals(0, counter.getNoteCount());
    assertEquals("Total Items: 06 | Package: 02 | Class: 02 | Component: 02"
      + " | Note: 00", counter.getStatusText());
  }

  /**
   * Removing an element updates the count.
   */
  public void testRemoveElement() {
    UmlNode note = addNode(ElementType.NOTE);
    addNode(ElementType.CLASS);
    assertEquals(1, new ElementCounter(diagram).getNoteCount());
    diagram.removeChild(note);
    ElementCounter counter = new ElementCounter(diagram);
    assertEquals(0, counter.getNoteCount());
    assertEquals(1, counter.getTotalCount());
  }

  /**
   * Connections are not counted as elements.
   */
  public void testConnectionsAreNotCounted() {
    UmlNode class1 = addNode(ElementType.CLASS);
    UmlNode class2 = addNode(ElementType.CLASS);
    UmlConnection conn = factory.createConnection(RelationType.ASSOCIATION,
      class1, class2);
    diagram.addChild(conn);
    assertEquals(2, new ElementCounter(diagram).getTotalCount());
  }

  /**
   * Elements nested in a package are counted.
   */
  public void testNestedElements() {
    UmlNode pkg = addNode(ElementType.PACKAGE);
    UmlNode clss = factory.createNode(ElementType.CLASS);
    ((CompositeElement) pkg).addChild(clss);
    ElementCounter counter = new ElementCounter(diagram);
    assertEquals(1, counter.getPackageCount());
    assertEquals(1, counter.getClassCount());
    assertEquals(2, counter.getTotalCount());
  }
}
