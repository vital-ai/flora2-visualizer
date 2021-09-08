/* File:      FloraNode.java
 **
 ** Author(s): Daniel Winkler
 ** Contact:   flora-users@lists.sourceforge.net
 **
 ** Copyright (C) 2007 Digital Enterprise Research Insitute (DERI) Innsbruck
 **
 ** FLORA-2 Visualizer is free software; you can redistribute it and/or
 ** modify it under the terms of the GNU Lesser General Public License
 ** as published by the Free Software Foundation; either version 2
 ** of the License, or (at your option) any later version.
 ** This program is distributed in the hope that it will be useful,
 ** but WITHOUT ANY WARRANTY; without even the implied warranty of
 ** MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 ** GNU Lesser General Public License for more details.
 ** You should have received a copy of the GNU General Public License
 ** along with this program; if not, write to the Free Software
 ** Foundation, Inc., 51 Franklin Street, 5th Floor, Boston, MA  02110-1301, USA.
 */

package net.sourceforge.flora.eclipse.visualizer.graph.element;


import net.sourceforge.flora.eclipse.reasoner.object.IFloraElement;
import net.sourceforge.flora.eclipse.visualizer.graph.FloraGraph;
import net.sourceforge.jpowergraph.defaults.DefaultNode;

/**
 * A class which can be added to a {@link FloraGraph}
 * <p>
 * The Node represents an {@link IFloraElement}
 * 
 * @author Daniel Winkler
 */
public class FloraNode extends DefaultNode {
	
	private IFloraElement element;
	
	/**
	 * the Constructor
	 * 
	 * @param element the element which shall be represented
	 */
	public FloraNode(IFloraElement element)
	{
		setElement(element);
	}

	/**
	 * returns the <code>IFloraElement</code> that is represented
	 * 
	 * @return the <code>IFloraElement</code> that is represented
	 */
	public IFloraElement getElement() {
		return element;
	}

	/**
	 * sets the <code>IFloraElement</code> that shall be displayed
	 * 
	 * @param element the <code>IFloraElement</code> that should be displayed
	 */
	private void setElement(IFloraElement element) {
		this.element = element;
	}

	/**
	 * returns the name of the element
	 * 
	 * @return the name of the element
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return getElement().toString();
	}

}
