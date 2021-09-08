/* File:      FloraInstanceNode.java
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

import net.sourceforge.flora.eclipse.reasoner.object.FloraInstance;

/**
 * A class to represent <code>FloraInstances</code> in a <code>FloraGraph</code>
 * 
 * @author Daniel Winkler
 */
public class FloraInstanceNode extends FloraNode {

	/**
	 * the Constructor
	 * 
	 * @param instance the Instance to represent in the <code>FloraGraph</code>
	 */
	public FloraInstanceNode(FloraInstance instance) {
		super(instance);
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.jpowergraph.defaults.DefaultNode#getNodeType()
	 */
	public String getNodeType() {
		return "Instance Node";
	}

	/**
	 * indicates equality depending on the equality of the representing <code>FloraInstance</code>
	 * 
	 * @param obj the object to test
	 * @return true if the object is equal to the representing <code>FloraInstance</code>
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 * @see FloraInstance#equals(Object)
	 */
	public boolean equals(Object obj) {
	    if (obj instanceof FloraNode) {
		if (toString().equals(obj.toString()))
		    return true;
	    }
		return false;
	}

}
