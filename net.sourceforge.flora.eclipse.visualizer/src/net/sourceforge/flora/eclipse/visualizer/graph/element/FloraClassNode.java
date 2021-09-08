/* File:      FloraClassNode.java
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

import net.sourceforge.flora.eclipse.reasoner.object.FloraClass;
import net.sourceforge.flora.eclipse.reasoner.object.IFloraElement;

/**
 * A Class to display Flora-2 class nodes in a <code>FloraGraph</code>
 * 
 * @author Daniel Winkler
 */
public class FloraClassNode extends FloraNode {

    /**
     * the Constructor
     * 
     * @param fClass the <code>FloraClass</code> to represent
     */
    public FloraClassNode(FloraClass fClass) {
	super(fClass);
    }

    /* (non-Javadoc)
     * @see net.sourceforge.jpowergraph.defaults.DefaultNode#getNodeType()
     */
    public String getNodeType() {
	return "Class Node";
    }

    /**
     * indicates equality depending on equality of the element
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     * @see IFloraElement#equals(Object)
     */
    public boolean equals(Object obj) {
	if (obj instanceof FloraNode) {
		if (toString().equals(obj.toString()))
		    return true;
	    }
	return false;
    }

}
