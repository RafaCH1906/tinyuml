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

import org.tinyuml.draw.AbstractCompositeNode;
import org.tinyuml.draw.DiagramElement;
import org.tinyuml.umldraw.shared.NoteElement;
import org.tinyuml.umldraw.structure.ClassElement;
import org.tinyuml.umldraw.structure.ComponentElement;
import org.tinyuml.umldraw.structure.PackageElement;

/**
 * Counts the elements of a diagram by category (Package, Class, Component
 * and Note). Connections are not counted. Nested elements (e.g. a class
 * inside a package) are counted as well.
 * Creado para cumplir con el requerimiento RF-001.
 *
 * @author RafaCH1906
 * @version 1.0
 */
public class ElementCounter {

  private int packages, classes, components, notes;

  /**
   * Constructor. Counts the elements contained in the specified diagram.
   * @param diagram the diagram to count, may be null
   */
  public ElementCounter(AbstractCompositeNode diagram) {
    if (diagram != null) countChildren(diagram);
  }

  /**
   * Recursively counts the children of the specified node.
   * @param parent the parent node
   */
  private void countChildren(AbstractCompositeNode parent) {
    for (DiagramElement child : parent.getChildren()) {
      if (child instanceof PackageElement) packages++;
      else if (child instanceof ClassElement) classes++;
      else if (child instanceof ComponentElement) components++;
      else if (child instanceof NoteElement) notes++;

      if (child instanceof AbstractCompositeNode) {
        countChildren((AbstractCompositeNode) child);
      }
    }
  }

  /**
   * Returns the number of packages.
   * @return the number of packages
   */
  public int getPackageCount() { return packages; }

  /**
   * Returns the number of classes.
   * @return the number of classes
   */
  public int getClassCount() { return classes; }

  /**
   * Returns the number of components.
   * @return the number of components
   */
  public int getComponentCount() { return components; }

  /**
   * Returns the number of notes.
   * @return the number of notes
   */
  public int getNoteCount() { return notes; }

  /**
   * Returns the total number of elements, without connections.
   * @return the total number of elements
   */
  public int getTotalCount() { return packages + classes + components + notes; }

  /**
   * Returns the text to display in the status bar.
   * @return the status text
   */
  public String getStatusText() {
    return String.format(
      "Total Items: %02d | Package: %02d | Class: %02d | Component: %02d | "
      + "Note: %02d", getTotalCount(), packages, classes, components, notes);
  }
}
