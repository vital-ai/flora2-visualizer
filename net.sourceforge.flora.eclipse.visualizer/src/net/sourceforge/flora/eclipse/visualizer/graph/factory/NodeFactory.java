/* File:      NodeFactory.java
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

import java.util.List;

import net.sourceforge.flora.eclipse.reasoner.object.FloraClass;
import net.sourceforge.flora.eclipse.reasoner.object.FloraInstance;
import net.sourceforge.flora.eclipse.reasoner.object.FloraModule;
import net.sourceforge.flora.eclipse.reasoner.object.IFloraElement;
import net.sourceforge.flora.eclipse.visualizer.graph.element.FloraClassNode;
import net.sourceforge.flora.eclipse.visualizer.graph.element.FloraInstanceClusterNode;
import net.sourceforge.flora.eclipse.visualizer.graph.element.FloraInstanceNode;
import net.sourceforge.flora.eclipse.visualizer.graph.element.FloraModuleNode;
import net.sourceforge.flora.eclipse.visualizer.graph.element.FloraNode;


/**
 * Factory to create <code>FloraNodes</code>
 * 
 * @author Daniel Winkler
 */
public class NodeFactory {

	/**
	 * returns a {@link FloraNode} depending on the class of the given element
	 * 
	 * @param element the element that will be tested 
	 * @return a <code>FloraNode</code> depending on the class of the given element
	 */
	public static FloraNode getNode(IFloraElement element) {
		if (element instanceof FloraModule) {
			return new FloraModuleNode((FloraModule) element);
		} else if (element instanceof FloraClass) {
			return new FloraClassNode((FloraClass) element);
		} else if (element instanceof FloraInstance) {
			return new FloraInstanceNode((FloraInstance) element);
		}
		return null;
	}

	/**
	 * returns a new {@link FloraInstanceClusterNode}
	 * 
	 * @param instances the instances which will be represented by the <code>FloraInstanceClusterNode</code> 
	 * @return a new <code>FloraInstanceClusterNode</code>
	 */
	public static FloraInstanceClusterNode getClusterNode(List<FloraInstance> instances) {
	    return new FloraInstanceClusterNode(instances);
	}
}
