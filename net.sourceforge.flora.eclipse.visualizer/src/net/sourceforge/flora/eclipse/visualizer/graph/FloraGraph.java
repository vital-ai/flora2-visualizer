/* File:      FloraGraph.java
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

package net.sourceforge.flora.eclipse.visualizer.graph;

import java.util.ArrayList;

import net.sourceforge.flora.eclipse.reasoner.object.FloraModule;
import net.sourceforge.flora.eclipse.visualizer.graph.element.FloraClassNode;
import net.sourceforge.flora.eclipse.visualizer.graph.element.FloraEdge;
import net.sourceforge.flora.eclipse.visualizer.graph.element.FloraInstanceClusterNode;
import net.sourceforge.flora.eclipse.visualizer.graph.element.FloraInstanceNode;
import net.sourceforge.flora.eclipse.visualizer.graph.element.FloraNode;
import net.sourceforge.jpowergraph.Edge;
import net.sourceforge.jpowergraph.Node;
import net.sourceforge.jpowergraph.defaults.DefaultGraph;

/**
 * A Graph representing a Module of the current Flora-2 Session
 * 
 * @author Daniel Winkler
 */
public class FloraGraph extends DefaultGraph {

	private ArrayList<FloraNode> fNodes = null;

	private ArrayList<FloraEdge> fEdges = null;
	
	private ArrayList<FloraClassNode> fClusterNodes = null;
	
	private ArrayList<FloraClassNode> fUnclusterNodes = null;

	private FloraModule module = null;

	/**
	 * the Constructor
	 * 
	 * @param module
	 *            the module which will be displayed by the <code>FloraGraph</code>
	 */
	public FloraGraph(FloraModule module) {
		fNodes = new ArrayList<FloraNode>();
		fEdges = new ArrayList<FloraEdge>();
		fClusterNodes = new ArrayList<FloraClassNode>();
		fUnclusterNodes = new ArrayList<FloraClassNode>();
		this.module = module;
	}

	/**
	 * adds a <code>FloraNode</code> to the <code>FloraGraph</code> and returns it. If the <code>FloraNode</code> already exists in the
	 * <code>FloraGraph</code>, the existing <code>FloraNode</code> is returned. Use the returned <code>FloraNode</code> for further operations
	 * on the <code>FloraNode</code>.
	 * 
	 * @param node
	 *            the <code>FloraNode</code> to add
	 * @return the added <code>FloraNode</code> if it is not in the <code>FloraGraph</code>, the existing <code>FloraNode</code> else
	 * @see FloraClassNode#equals(Object)
	 * @see FloraInstanceNode#equals(Object)
	 * @see FloraInstanceClusterNode#equals(Object)
	 */
	public FloraNode addNode(FloraNode node) {
		if (!fNodes.contains(node))
			fNodes.add(node);
		return fNodes.get(fNodes.indexOf(node));
	}

	/**
	 * adds a <code>FloraEdge</code> to the <code>FloraGraph</code> and returns it. If the <code>FloraEdge</code> already exists in the
	 * <code>FloraGraph</code>, the existing <code>FloraEdge</code> is returned. Use the returned <code>FloraEdge</code> for further operations
	 * on the <code>FloraEdge</code>.
	 * 
	 * @param edge
	 *            the <code>FloraEdge</code> to add
	 * @return the added <code>FloraEdge</code> if it is not in the <code>FloraGraph</code>, the existing <code>FloraEdge</code> else
	 */
	public FloraEdge addEdge(FloraEdge edge) {
		FloraNode from = addNode((FloraNode) edge.getFrom());
		edge.setFrom(from);
		FloraNode to = addNode((FloraNode) edge.getTo());
		edge.setTo(to);

		if (!fEdges.contains(edge)) {
			fEdges.add(edge);
			from.notifyEdgeAdded(edge);
			to.notifyEdgeAdded(edge);
		}

		return fEdges.get(fEdges.indexOf(edge));
	}

	/**
	 * updates the <code>FloraGraph</code>. Must be executed after changing the <code>FloraGraph</code>
	 */
	public void update() {
		clear();
		addElements(new ArrayList<Node>(fNodes), new ArrayList<Edge>(fEdges));
		fNodes.clear();
		fEdges.clear();
	}

	/**
	 * returns the module which is represented by the <code>FloraGraph</code>
	 * 
	 * @return the module which is represented by the <code>FloraGraph</code>
	 */
	public FloraModule getModule() {
		return module;
	}
	
	public void clusterNode(FloraClassNode theNode)
	{
	    if (fUnclusterNodes.contains(theNode))
		fUnclusterNodes.remove(theNode);
	    if (!fClusterNodes.contains(theNode))
		fClusterNodes.add(theNode);
	}
	public void unclusterNode(FloraClassNode theNode)
	{
	    if (fClusterNodes.contains(theNode))
		fClusterNodes.remove(theNode);
	    if (!fUnclusterNodes.contains(theNode))
		fUnclusterNodes.add(theNode);
	}

	public ArrayList<FloraClassNode> getClusterNodes() {
	    return fClusterNodes;
	}

	public ArrayList<FloraClassNode> getUnclusterNodes() {
	    return fUnclusterNodes;
	}

	public ArrayList<FloraNode> findParentsFor(FloraNode theNode) {
		ArrayList<FloraNode> resultSet = new ArrayList<FloraNode>();
	    for (Edge edge : getAllEdges())
	    	if (theNode.equals(edge.getTo()))
	    		resultSet.add((FloraNode) edge.getFrom());
	    return resultSet;
	}
}
