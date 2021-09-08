/* File:      GraphFactory.java
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

import net.sourceforge.flora.eclipse.reasoner.object.FloraClass;
import net.sourceforge.flora.eclipse.reasoner.object.FloraModule;
import net.sourceforge.flora.eclipse.reasoner.object.IFloraElement;
import net.sourceforge.flora.eclipse.visualizer.FloraVisualizerPlugin;
import net.sourceforge.flora.eclipse.visualizer.graph.FloraGraph;
import net.sourceforge.flora.eclipse.visualizer.graph.element.FloraClassNode;
import net.sourceforge.flora.eclipse.visualizer.graph.element.FloraEdge;
import net.sourceforge.flora.eclipse.visualizer.graph.element.FloraNode;

/**
 * A Factory to create a <code>FloraGraph</code>
 * 
 * @author Daniel Winkler
 */
public class GraphFactory {

    /**
         * creates a new {@link FloraGraph} and returns it
         * 
         * @param module
         *                the module which should be represented by the Graph
         * @return a new <code>FloraGraph</code> which represents the given module
         */
    public static FloraGraph getGraph(FloraModule module) {

	FloraGraph graph = new FloraGraph(module);

	return updateGraph(graph);
    }

    /**
         * updates a given <code>FloraGraph</code> and returns it
         * 
         * @param graph
         *                the <code>FloraGraph</code> to update
         * @return the updated <code>FloraGraph</code>
         */
    public static FloraGraph updateGraph(final FloraGraph graph) {
		FloraModule module = graph.getModule();
		module.loadModule();

		for (IFloraElement child : module.getChildren())
		buildSubTree(graph, NodeFactory.getNode(child));

		graph.update();

		return graph;
	}

    /**
	 * builds the subTree of a <code>FloraNode</code> (recursively) and appends it to the <code>FloraGraph</code>
	 * 
	 * @param graph
	 *            the <code>FloraGraph</code> to extend
	 * @param parent
	 *            the <code>FloraNode</code> which is parent of the appended subGraph
	 */
    private static void buildSubTree(FloraGraph graph, FloraNode parent) {
	FloraNode childNode = null;
	FloraEdge theEdge = null;

	if (parent.getElement().getChildren() != null) {

	    if (parent.getElement() instanceof FloraClass) {
		FloraClass floraClass = (FloraClass) parent.getElement();
		if ((FloraVisualizerPlugin.getDefault().getUseInstanceClusters()) // if using instance clusters
			&& (floraClass.getInstances().size() > 0) // and the class has instances
			&& (graph.getClusterNodes().contains((FloraClassNode) parent)  // and (its a clustered node
				|| ((!graph.getUnclusterNodes().contains((FloraClassNode) parent)) // or (its not an unclustered node
				&& (floraClass.getInstances().size() >= FloraVisualizerPlugin.getDefault().getMinimumInstanceClusterSize())))) { // and has more instances than minInstanceClusterSize))
		    childNode = NodeFactory.getClusterNode(floraClass.getInstances());
		    theEdge = EdgeFactory.getEdge(parent, childNode);
		    graph.addEdge(theEdge);
		    graph.clusterNode((FloraClassNode) parent);

		    for (IFloraElement childElement : floraClass.getSubClasses()) {
			childNode = NodeFactory.getNode(childElement);
			theEdge = EdgeFactory.getEdge(parent, childNode);
			graph.addEdge(theEdge);
			buildSubTree(graph, childNode);
		    }
		} else {
		    graph.unclusterNode((FloraClassNode) parent);
		    for (IFloraElement childElement : parent.getElement().getChildren()) {
			childNode = NodeFactory.getNode(childElement);
			theEdge = EdgeFactory.getEdge(parent, childNode);
			graph.addEdge(theEdge);
			buildSubTree(graph, childNode);
		    }
		}
	    } else {
		for (IFloraElement childElement : parent.getElement().getChildren()) {
		    childNode = NodeFactory.getNode(childElement);
		    theEdge = EdgeFactory.getEdge(parent, childNode);
		    graph.addEdge(theEdge);
		    buildSubTree(graph, childNode);
		}
	    }
	} else { // draw just a node
		graph.addNode(parent);
	}
    }

}
