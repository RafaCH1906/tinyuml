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

import java.util.List;
import org.tinyuml.draw.Diagram;
import org.tinyuml.draw.DiagramElement;
import org.tinyuml.model.UmlModelElement;
import org.tinyuml.umldraw.shared.NoteConnection;
import org.tinyuml.umldraw.shared.NoteElement;
import org.tinyuml.umldraw.shared.UmlDiagramElement;
import org.tinyuml.umldraw.structure.Association;
import org.tinyuml.umldraw.structure.ClassElement;
import org.tinyuml.umldraw.structure.ComponentElement;
import org.tinyuml.umldraw.structure.Dependency;
import org.tinyuml.umldraw.structure.Inheritance;
import org.tinyuml.umldraw.structure.PackageElement;

/**
 * Builds the status bar text that describes the current selection: the
 * category and the name of the selected element.
 * Creado para cumplir con el requerimiento RF-002.
 *
 * @author RafaCH1906
 * @version 1.0
 */
public final class SelectionInfo {

  /** Text displayed when nothing is selected. */
  public static final String NO_SELECTION = "Sin selección";

  /** Text displayed for an element without a name. */
  public static final String NO_NAME = "(sin nombre)";

  /**
   * Private constructor.
   */
  private SelectionInfo() { }

  /**
   * Returns the status text for the specified selected elements.
   * @param selected the selected elements, may be null
   * @return the status text
   */
  public static String getStatusText(List<DiagramElement> selected) {
    if (selected == null || selected.isEmpty()) return NO_SELECTION;
    if (selected.size() > 1) {
      return selected.size() + " elementos seleccionados";
    }
    DiagramElement element = selected.get(0);
    // clicking on an empty area selects the diagram itself, which is not an
    // element of the diagram
    if (element instanceof Diagram) return NO_SELECTION;
    return "Selección: " + getCategory(element) + " - " + getName(element);
  }

  /**
   * Returns the number of selected elements. Selecting the empty area of the
   * diagram selects the diagram itself, which is not counted.
   * Creado para cumplir con el requerimiento RF-003.
   * @param selected the selected elements, may be null
   * @return the number of selected elements
   */
  public static int getSelectedCount(List<DiagramElement> selected) {
    if (selected == null) return 0;
    int count = 0;
    for (DiagramElement element : selected) {
      if (!(element instanceof Diagram)) count++;
    }
    return count;
  }

  /**
   * Returns the status text with the number of selected elements.
   * Creado para cumplir con el requerimiento RF-003.
   * @param selected the selected elements, may be null
   * @return the status text
   */
  public static String getCountText(List<DiagramElement> selected) {
    return "Seleccionados: " + getSelectedCount(selected);
  }

  /**
   * Returns the category of the specified element.
   * @param element the element
   * @return the category name
   */
  public static String getCategory(DiagramElement element) {
    if (element instanceof PackageElement) return "Package";
    if (element instanceof ClassElement) return "Class";
    if (element instanceof ComponentElement) return "Component";
    if (element instanceof NoteElement) return "Note";
    if (element instanceof Association) return "Association";
    if (element instanceof Dependency) return "Dependency";
    if (element instanceof Inheritance) return "Inheritance";
    if (element instanceof NoteConnection) return "Note connection";
    return "Element";
  }

  /**
   * Returns the name of the specified element. For notes, the note text is
   * used as the name.
   * @param element the element
   * @return the name, or NO_NAME if the element has no name
   */
  public static String getName(DiagramElement element) {
    String name = null;
    if (element instanceof NoteElement) {
      name = ((NoteElement) element).getLabelText();
    } else if (element instanceof UmlDiagramElement) {
      UmlModelElement modelElement =
        ((UmlDiagramElement) element).getModelElement();
      if (modelElement != null) name = modelElement.getName();
    }
    if (name == null || name.trim().length() == 0) return NO_NAME;
    // multi-line notes are displayed in a single line
    return name.trim().replaceAll("\\s+", " ");
  }
}
