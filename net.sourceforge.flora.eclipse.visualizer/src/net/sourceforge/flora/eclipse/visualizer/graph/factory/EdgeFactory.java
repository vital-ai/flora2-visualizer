/* File:      EdgeFactory.java
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

package net.sourceforge.flora.eclipse.visualizer.graph.factory;

import net.sourceforge.flora.eclipse.visualizer.graph.element.ClusterEdge;
import net.sourceforge.flora.eclipse.visualizer.graph.element.FloraEdge;
import net.sourceforge.flora.eclipse.visualizer.graph.element.FloraInstanceClusterNode;
import net.sourceforge.flora.eclipse.visualizer.graph.element.FloraNode;

/**
 * Factory to create {@link FloraEdge}s
 * 
 * @author Daniel Winkler
 */
public class EdgeFactory {
    /**
     * returns a new <code>FloraEdge</code> from the <code>from</code> node to the <code>to</code> node.
     * If the to node is a Instance Cluster a <code>ClusterEdge</code> is returned.
     * 
     * @param from the <code>FloraNode</code> the edge comes from
     * @param to the <code>FloraNode</code> the edge goes to
     * @return a new <code>FloraEdge</code> from the <code>from</code> node to the <code>to</code> node.
     * If the to node is a Instance Cluster a <code>ClusterEdge</code> is returned.
     * @see FloraEdge
     * @see ClusterEdge
     * @see FloraNode
     */
    public static FloraEdge getEdge(FloraNode from, FloraNode to) {
	if (to instanceof FloraInstanceClusterNode) {
	    return new ClusterEdge(from, to);
	} else {
	    return new FloraEdge(from, to);
	}
    }
}
