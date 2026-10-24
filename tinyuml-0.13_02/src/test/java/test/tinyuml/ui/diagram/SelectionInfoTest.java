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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import junit.framework.TestCase;
import org.tinyuml.draw.DiagramElement;
import org.tinyuml.model.ElementType;
import org.tinyuml.model.RelationType;
import org.tinyuml.model.UmlModelImpl;
import org.tinyuml.ui.diagram.SelectionInfo;
import org.tinyuml.umldraw.shared.NoteElement;
import org.tinyuml.umldraw.shared.UmlNode;
import org.tinyuml.umldraw.structure.DiagramElementFactory;
import org.tinyuml.umldraw.structure.StructureDiagram;

/**
 * A test class for SelectionInfo (RF-002).
 * @author RafaCH1906
 * @version 1.0
 */
public class SelectionInfoTest extends TestCase {

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
   * Returns a list with the specified elements.
   * @param elements the elements
   * @return the list
   */
  private List<DiagramElement> list(DiagramElement... elements) {
    return Arrays.asList(elements);
  }

  /**
   * Without a selection, "Sin selección" is shown.
   */
  public void testNoSelection() {
    assertEquals("Sin selección",
      SelectionInfo.getStatusText(new ArrayList<DiagramElement>()));
    assertEquals("Sin selección", SelectionInfo.getStatusText(null));
  }

  /**
   * Selecting the empty area of the diagram counts as no selection.
   */
  public void testDiagramSelected() {
    assertEquals("Sin selección", SelectionInfo.getStatusText(list(diagram)));
  }

  /**
   * The category and the name of each node type are shown.
   */
  public void testNodeCategoryAndName() {
    UmlNode clss = factory.createNode(ElementType.CLASS);
    clss.getModelElement().setName("Persona");
    assertEquals("Selección: Class - Persona",
      SelectionInfo.getStatusText(list(clss)));

    UmlNode pkg = factory.createNode(ElementType.PACKAGE);
    pkg.getModelElement().setName("modelo");
    assertEquals("Selección: Package - modelo",
      SelectionInfo.getStatusText(list(pkg)));

    UmlNode comp = factory.createNode(ElementType.COMPONENT);
    comp.getModelElement().setName("Servidor");
    assertEquals("Selección: Component - Servidor",
      SelectionInfo.getStatusText(list(comp)));
  }

  /**
   * The text of a note is used as its name, in a single line.
   */
  public void testNote() {
    NoteElement note = (NoteElement) factory.createNode(ElementType.NOTE);
    note.setLabelText("linea 1\nlinea 2");
    assertEquals("Selección: Note - linea 1 linea 2",
      SelectionInfo.getStatusText(list(note)));
  }

  /**
   * Elements without a name show "(sin nombre)".
   */
  public void testNoName() {
    UmlNode clss = factory.createNode(ElementType.CLASS);
    clss.getModelElement().setName("");
    assertEquals("Selección: Class - (sin nombre)",
      SelectionInfo.getStatusText(list(clss)));
  }

  /**
   * Connections show their relation type.
   */
  public void testConnection() {
    UmlNode class1 = factory.createNode(ElementType.CLASS);
    UmlNode class2 = factory.createNode(ElementType.CLASS);
    DiagramElement assoc = factory.createConnection(RelationType.ASSOCIATION,
      class1, class2);
    assertEquals("Association", SelectionInfo.getCategory(assoc));
    DiagramElement inheritance = factory.createConnection(
      RelationType.INHERITANCE, class1, class2);
    assertEquals("Inheritance", SelectionInfo.getCategory(inheritance));
  }

  /**
   * With several elements selected, the number of elements is shown.
   */
  public void testMultipleSelection() {
    UmlNode class1 = factory.createNode(ElementType.CLASS);
    UmlNode class2 = factory.createNode(ElementType.CLASS);
    assertEquals("2 elementos seleccionados",
      SelectionInfo.getStatusText(list(class1, class2)));
  }
}
