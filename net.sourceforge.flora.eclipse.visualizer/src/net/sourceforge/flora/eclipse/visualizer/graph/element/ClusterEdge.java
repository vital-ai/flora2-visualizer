/* File:      ClusterEdge.java
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

import net.sourceforge.flora.eclipse.visualizer.graph.FloraGraph;

/**
 * A Class that represents a Edge from a {@link FloraNode} to a {@link FloraInstanceClusterNode} in a {@link FloraGraph}
 */
public class ClusterEdge extends FloraEdge {

    /**
     * the Constructor
     * 
     * @param from the <code>FloraNode</code> from which the <code>ClusterEdge</code> comes from
     * @param to the <code>FloraNode</code> to which the <code>ClusterEdge</code> goes to
     */
    public ClusterEdge(FloraNode from, FloraNode to) {
	super(from, to);
    }

    /* (non-Javadoc)
     * @see net.sourceforge.jpowergraph.defaults.DefaultEdge#getLength()
     */
    public double getLength() {
	    return super.getLength();
    }

}
