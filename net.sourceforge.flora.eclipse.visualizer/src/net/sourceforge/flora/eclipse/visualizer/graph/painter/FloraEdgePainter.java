/* File:      FloraEdgePainter.java
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



package net.sourceforge.flora.eclipse.visualizer.graph.painter;

import net.sourceforge.flora.eclipse.visualizer.graph.element.FloraModuleNode;
import net.sourceforge.jpowergraph.Edge;
import net.sourceforge.jpowergraph.Node;
import net.sourceforge.jpowergraph.SubGraphHighlighter;
import net.sourceforge.jpowergraph.manipulator.dragging.DraggingManipulator;
import net.sourceforge.jpowergraph.manipulator.selection.HighlightingManipulator;
import net.sourceforge.jpowergraph.painters.NodePainter;
import net.sourceforge.jpowergraph.painters.edge.LineEdgePainter;
import net.sourceforge.jpowergraph.painters.node.ShapeNodePainter;
import net.sourceforge.jpowergraph.pane.JGraphPane;
import net.sourceforge.jpowergraph.swtswinginteraction.JPowerGraphGraphics;
import net.sourceforge.jpowergraph.swtswinginteraction.color.JPowerGraphColor;
import net.sourceforge.jpowergraph.swtswinginteraction.geometry.JPowerGraphPoint;

/**
 * The painter to draw the Edges in a <code>FloraClass</code>
 */
public class FloraEdgePainter extends LineEdgePainter {

    private JGraphPane jgraphPane;

    /**
         * the Constructor
         * 
         * @param theJGraphPane
         *                the Pane to draw the Edge on
         */
    public FloraEdgePainter(JGraphPane theJGraphPane) {
	super();
	this.jgraphPane = theJGraphPane;
    }

    /*
         * (non-Javadoc)
         * 
         * @see net.sourceforge.jpowergraph.painters.LineEdgePainter#getEdgeColor(net.sourceforge.jpowergraph.Edge, boolean, boolean, boolean,
         *      net.sourceforge.jpowergraph.SubGraphHighlighter)
         */
    protected JPowerGraphColor getEdgeColor(Edge edge, boolean isHighlighted, boolean isDragging, boolean isShowBlackAndWhite,
	    SubGraphHighlighter theSubGraphHighlighter) {
	Node n;
	if (edge.getFrom() instanceof FloraModuleNode)
		n = edge.getFrom();
	else
		n = edge.getTo();
	
	NodePainter p = jgraphPane.getPainterForNode(n);

	if (p != null && p instanceof ShapeNodePainter) {
	    return ((ShapeNodePainter) p).getBorderColor(n, jgraphPane, theSubGraphHighlighter);
	} else if (p != null && p instanceof FloraNodePainter) {
	    return ((FloraNodePainter) p).getBorderColor(n, isHighlighted, isDragging, isShowBlackAndWhite, theSubGraphHighlighter);
	}
	return super.getEdgeColor(edge, jgraphPane, isShowBlackAndWhite, theSubGraphHighlighter);
    }

    /**
         * overrides
         * {@link net.sourceforge.jpowergraph.painters.LineEdgePainter#paintEdge(JGraphPane, JPowerGraphGraphics, Edge, SubGraphHighlighter)} <br>
         * draws the edge with an arrow pointing at the from node
         * 
         * @see net.sourceforge.jpowergraph.painters.LineEdgePainter#paintEdge(net.sourceforge.jpowergraph.pane.JGraphPane,
         *      net.sourceforge.jpowergraph.swtswinginteraction.JPowerGraphGraphics, net.sourceforge.jpowergraph.Edge,
         *      net.sourceforge.jpowergraph.SubGraphHighlighter)
         */
    public void paintEdge(JGraphPane graphPane, JPowerGraphGraphics g, Edge edge, SubGraphHighlighter theSubGraphHighlighter) {
	HighlightingManipulator highlightingManipulator = (HighlightingManipulator) graphPane.getManipulator(HighlightingManipulator.NAME);
	boolean isHighlighted = highlightingManipulator != null && highlightingManipulator.getHighlightedEdge() == edge;
	DraggingManipulator draggingManipulator = (DraggingManipulator) graphPane.getManipulator(DraggingManipulator.NAME);
	boolean isDragging = draggingManipulator != null && draggingManipulator.getDraggedEdge() == edge;
	JPowerGraphPoint from = graphPane.getScreenPointForNode(edge.getFrom());
	JPowerGraphPoint to = graphPane.getScreenPointForNode(edge.getTo());
	JPowerGraphColor oldFGColor = g.getForeground();
	JPowerGraphColor oldBGColor = g.getBackground();

	g.setForeground(getEdgeColor(edge, isHighlighted, isDragging, false, theSubGraphHighlighter));
	g.setBackground(getEdgeColor(edge, isHighlighted, isDragging, false, theSubGraphHighlighter));
	paintArrow(g, to.x, to.y, from.x, from.y);
	g.setForeground(oldFGColor);
	g.setBackground(oldBGColor);
    }
}
