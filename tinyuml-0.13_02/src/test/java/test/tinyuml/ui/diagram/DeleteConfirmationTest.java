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
import java.util.List;
import junit.framework.TestCase;
import org.tinyuml.draw.DiagramElement;
import org.tinyuml.model.ElementType;
import org.tinyuml.model.UmlModelImpl;
import org.tinyuml.ui.diagram.DiagramEditor;
import org.tinyuml.umldraw.shared.UmlNode;
import org.tinyuml.umldraw.structure.StructureDiagram;

/**
 * A test class for the delete confirmation of DiagramEditor (RF-004).
 * @author RafaCH1906
 * @version 1.0
 */
public class DeleteConfirmationTest extends TestCase {

  /**
   * An editor with a fixed selection that simulates the answer of the
   * confirmation dialog.
   */
  static class TestEditor extends DiagramEditor {
    List<DiagramElement> selection = new ArrayList<DiagramElement>();
    boolean answer;
    int askedCount = -1;

    TestEditor(StructureDiagram diagram) { super(null, diagram); }

    @Override
    public List<DiagramElement> getSelectedElements() { return selection; }

    @Override
    protected boolean confirmDeletion(int count) {
      askedCount = count;
      return answer;
    }
  }

  private StructureDiagram diagram;
  private TestEditor editor;

  /**
   * {@inheritDoc}
   */
  @Override
  protected void setUp() {
    diagram = new StructureDiagram(new UmlModelImpl());
    editor = new TestEditor(diagram);
  }

  /**
   * Adds a new node to the diagram and to the selection.
   * @param type the element type
   */
  private void addSelected(ElementType type) {
    UmlNode node = diagram.getElementFactory().createNode(type);
    diagram.addChild(node);
    editor.selection.add(node);
  }

  /**
   * Deleting a single element does not ask for confirmation.
   */
  public void testSingleElementNoConfirmation() {
    addSelected(ElementType.CLASS);
    editor.deleteSelection();
    assertEquals(-1, editor.askedCount);
    assertEquals(0, diagram.getChildren().size());
  }

  /**
   * Deleting several elements asks with the number of elements, and deletes
   * them if the user confirms.
   */
  public void testSeveralElementsConfirmed() {
    addSelected(ElementType.CLASS);
    addSelected(ElementType.PACKAGE);
    addSelected(ElementType.NOTE);
    editor.answer = true;
    editor.deleteSelection();
    assertEquals(3, editor.askedCount);
    assertEquals(0, diagram.getChildren().size());
  }

  /**
   * If the user cancels, the elements stay in the diagram.
   */
  public void testSeveralElementsCancelled() {
    addSelected(ElementType.CLASS);
    addSelected(ElementType.COMPONENT);
    editor.answer = false;
    editor.deleteSelection();
    assertEquals(2, editor.askedCount);
    assertEquals(2, diagram.getChildren().size());
  }

  /**
   * Cutting (deleteSelection(false)) does not ask for confirmation.
   */
  public void testWithoutConfirmation() {
    addSelected(ElementType.CLASS);
    addSelected(ElementType.CLASS);
    editor.deleteSelection(false);
    assertEquals(-1, editor.askedCount);
    assertEquals(0, diagram.getChildren().size());
  }
}
