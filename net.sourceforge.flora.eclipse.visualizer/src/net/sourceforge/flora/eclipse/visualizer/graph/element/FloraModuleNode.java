/* File:      FloraModuleNode.java
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

import net.sourceforge.flora.eclipse.reasoner.object.FloraModule;

/**
 * A class to represent <code>FloraModules</code> in a <code>FloraGraph</code>
 * 
 * @author Daniel Winkler
 */
public class FloraModuleNode extends FloraNode {

	/**
	 * the Constructor
	 * 
	 * @param module the <code>FloraModule</code> to represent
	 */
	public FloraModuleNode(FloraModule module) {
		super(module);
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.jpowergraph.defaults.DefaultNode#getNodeType()
	 */
	public String getNodeType() {
		return "Module Node";
	}

	/**
	 * indicates equality depending on the equality of the representing <code>FloraModule</code>
	 * 
	 * @param obj the object to test
	 * @return true if the object is equal to the representing <code>FloraModule</code>
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 * @see FloraModule#equals(Object)
	 */
	public boolean equals(Object obj) {
		if (obj instanceof FloraModuleNode) {
			FloraModuleNode moduleNode = (FloraModuleNode) obj;
			return this.getElement().equals(moduleNode.getElement());
		}
		return false;
	}

	
	/**
	 * returns an empty String
	 * 
	 * @return an empty String
	 * @see net.sourceforge.flora.eclipse.visualizer.graph.element.FloraNode#toString()
	 */
	public String toString()
	{
	    return "";
	}
}
