/* File:      NullNode.java
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

import net.sourceforge.jpowergraph.defaults.DefaultNode;

/**
 * Class needed for temporary calculations
 */
public class NullNode extends DefaultNode {
    /**
     * empty Constructor
     */
    public NullNode() {
    }

    /* (non-Javadoc)
     * @see net.sourceforge.jpowergraph.defaults.DefaultNode#getLabel()
     */
    public String getLabel() {
	return "";
    }
}