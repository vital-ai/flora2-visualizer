/* File:      ClusterNodePainter.java
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

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;

import net.sourceforge.flora.eclipse.visualizer.graph.element.FloraInstanceClusterNode;
import net.sourceforge.flora.eclipse.visualizer.graph.element.NullNode;
import net.sourceforge.jpowergraph.Node;
import net.sourceforge.jpowergraph.SubGraphHighlighter;
import net.sourceforge.jpowergraph.defaults.DefaultNode;
import net.sourceforge.jpowergraph.defaults.DefaultSubGraphHighlighter;
import net.sourceforge.jpowergraph.manipulator.dragging.DraggingManipulator;
import net.sourceforge.jpowergraph.manipulator.selection.HighlightingManipulator;
import net.sourceforge.jpowergraph.manipulator.selection.SelectionManipulator;
import net.sourceforge.jpowergraph.painters.NodePainter;
import net.sourceforge.jpowergraph.painters.node.ShapeNodePainter;
import net.sourceforge.jpowergraph.pane.JGraphPane;
import net.sourceforge.jpowergraph.swtswinginteraction.JPowerGraphGraphics;
import net.sourceforge.jpowergraph.swtswinginteraction.color.JPowerGraphColor;
import net.sourceforge.jpowergraph.swtswinginteraction.geometry.JPowerGraphDimension;
import net.sourceforge.jpowergraph.swtswinginteraction.geometry.JPowerGraphPoint;
import net.sourceforge.jpowergraph.swtswinginteraction.geometry.JPowerGraphRectangle;

/**
 * The painter drawing the node.
 */
public class ClusterNodePainter implements NodePainter {

    private JPowerGraphColor backgroundColor;
    private JPowerGraphColor borderColor;
    private JPowerGraphColor textColor;
    private JPowerGraphColor instanceBackgroundColor;

    private NodePainter<Node> nodePainter;

    /**
     * the Constructor
     * 
     * @param theShape the shape of the nodes drawn in the cluster
     * @param theBackgroundColor the background color of the nodes drawn in the cluster
     * @param theBorderColor the border color of the nodes drawn in the cluster
     * @param theTextColor the text color of the nodes drawn in the cluster
     * @param theShowNodes draws nodes in the cluster if <code>true</code>
     */
    public ClusterNodePainter(int theShape, JPowerGraphColor theBackgroundColor, JPowerGraphColor theBorderColor, JPowerGraphColor theTextColor) {
	this.nodePainter = new ShapeNodePainter(theShape, theBackgroundColor, theBorderColor, theTextColor);

	this.instanceBackgroundColor = JPowerGraphColor.RED;

	this.backgroundColor = new JPowerGraphColor(246, 246, 246);
	this.borderColor = new JPowerGraphColor(197, 197, 197);
	this.textColor = JPowerGraphColor.BLACK;

    }

    /**
     * Paints the supplied node.
     * 
     * @param graphPane the graph pane
     * @param g the graphics
     * @param node the node to paint
     * @param size the size of the node
     * @param theSubGraphHighlighter the SubGraphHighlighter
     */
    public void paintNode(JGraphPane graphPane, JPowerGraphGraphics g, Node node, int size, SubGraphHighlighter theSubGraphHighlighter) {
	JPowerGraphPoint nodePoint = graphPane.getScreenPointForNode(node);
	paintNode(graphPane, g, node, size, theSubGraphHighlighter, nodePoint);
    }

    /**
     * Paints the supplied node.
     * 
     * @param graphPane the graph pane
     * @param g the graphics
     * @param node the node to paint
     * @param size the size of the node
     * @param theSubGraphHighlighter the SubGraphHighlighter
     * @param thePoint the JPowerGraphPoint where to draw the node
     */
    public void paintNode(JGraphPane graphPane, JPowerGraphGraphics g, Node node, int size, SubGraphHighlighter theSubGraphHighlighter,
	    JPowerGraphPoint thePoint) {
	paintNode(graphPane, g, node, size, theSubGraphHighlighter, thePoint, 1);
    }

    /**
     * Paints the supplied node.
     * 
     * @param graphPane the graph pane
     * @param g the graphics
     * @param node the node to paint
     * @param size the size of the node
     * @param theSubGraphHighlighter the SubGraphHighlighter
     * @param thePoint the JPowerGraphPoint where to draw the node
     * @param theScale the Scale for the node
     */
    public void paintNode(JGraphPane graphPane, JPowerGraphGraphics g, Node node, int size, SubGraphHighlighter theSubGraphHighlighter,
	    JPowerGraphPoint thePoint, double theScale) {
	if (!(node instanceof FloraInstanceClusterNode)) {
	    return;
	}

	HighlightingManipulator highlightingManipulator = (HighlightingManipulator) graphPane.getManipulator(HighlightingManipulator.NAME);
	boolean isHighlighted = highlightingManipulator != null && highlightingManipulator.getHighlightedNode() == node;
	SelectionManipulator selectionManipulator = (SelectionManipulator) graphPane.getManipulator(SelectionManipulator.NAME);
	boolean isSelected = selectionManipulator != null && selectionManipulator.getNodeSelectionModel().isNodeSelected(node);
	DraggingManipulator draggingManipulator = (DraggingManipulator) graphPane.getManipulator(DraggingManipulator.NAME);
	boolean isDragging = draggingManipulator != null && draggingManipulator.getDraggedNode() == node;

	int numInstances = ((FloraInstanceClusterNode) node).getNumberOfInstances();

	Node dummyNode = new NullNode();
	JPowerGraphRectangle dummyRectangle = new JPowerGraphRectangle(0, 0, 0, 0);
	nodePainter.getNodeScreenBounds(graphPane, dummyNode, ShapeNodePainter.SMALL, getInstanceScale(numInstances), dummyRectangle);
	dummyRectangle.width += 2;
	dummyRectangle.height += 2;

	int numOuterCircles = countOuterCircles(numInstances);
	int numRows = (numOuterCircles * 2) + 1;
	int[] rowSizes = getRowSizes(numInstances, numRows);
	int numCols = Integer.MIN_VALUE;
	for (int i = 0; i < rowSizes.length; i++) {
	    numCols = Math.max(numCols, rowSizes[i]);
	}
	String label = "" + numInstances;

	int width = 0;
	int height = 0;
    width = 20;
    height = 5;
    int stringWidth = stringWidth(g, label);
    int numlines = countLines(label);
    width += (stringWidth + (stringWidth / 4));
    height += ((g.getAscent() + g.getDescent() + 10) * numlines);
    width = (int) Math.ceil(width * theScale);
    height = (int) Math.ceil(height * theScale);

	JPowerGraphColor oldBGColor = g.getBackground();
	JPowerGraphColor oldFGColor = g.getForeground();
	g.setBackground(getBackgroundColor(node, isHighlighted, isSelected, isDragging, theSubGraphHighlighter));
	g.fillOval(thePoint.x - width / 2, thePoint.y - width / 2, width, width);

	g.setForeground(getBorderColor(node, isHighlighted, isSelected, isDragging, theSubGraphHighlighter));
	g.drawOval(thePoint.x - width / 2, thePoint.y - width / 2, width, width);

    int textX = (thePoint.x - stringWidth / 2) + 1;
    int textY = thePoint.y - (7 * numlines);

    g.storeFont();
    g.setFontFromJGraphPane(graphPane);
    g.setForeground(getTextColor(node, isHighlighted, isSelected, isDragging, theSubGraphHighlighter));
    ArrayList<String> lines = getLines(label);
    for (int i = 0; i < lines.size(); i++) {
	int offset = (g.getAscent() + g.getDescent() + 2) * i;
	g.drawString(lines.get(i), textX, textY + offset, lines.size());
    }
    g.restoreFont();

	g.setBackground(oldBGColor);
	g.setForeground(oldFGColor);
    }

    /**
     * calculates the sizes of the rows
     * 
     * @param numInstances the number of Instances
     * @param numRows the number of Rows
     * @return a Array containing the sizes of each row
     */
    private int[] getRowSizes(int numInstances, int numRows) {
	int[] result = new int[numRows];
	for (int i = 0; i < result.length; i++) {
	    int rowIndex = ((numRows - 1) / 2) - i;
	    if (rowIndex < 0) {
		rowIndex = rowIndex * -1;
	    }
	    result[i] = numRows - rowIndex;
	}

	int numCurrentNodes = 0;
	for (int i = 0; i < result.length; i++) {
	    numCurrentNodes += result[i];
	}

	int numOverNodes = numCurrentNodes - numInstances;
	while (numOverNodes > 0) {
	    while ((result[result.length - 1] > 0 || result[0] > 0) && numOverNodes != 0) {
		if (result[result.length - 1] > 0 && numOverNodes != 0) {
		    result[result.length - 1] -= 1;
		    numOverNodes -= 1;
		}
		if (result[0] > 0 && numOverNodes != 0) {
		    result[0] -= 1;
		    numOverNodes -= 1;
		}
	    }

	    for (int i = result.length - 2; i >= 0; i--) {
		if (numOverNodes != 0 && result[i] > 0) {
		    result[i] -= 1;
		    numOverNodes -= 1;
		}
	    }
	}

	return result;
    }

    /**
     * returns the number of circles needed to host the Instances
     * 
     * @param numInstances the number of Instances
     * @return the number of circles needed to host the Instances
     */
    private int countOuterCircles(int numInstances) {
	int numOuterCircles = 0;

	int temp = numInstances - 1;
	int nextCircle = 1;
	while (temp > 0) {
	    numOuterCircles++;
	    temp -= nextCircle * 6;
	    nextCircle++;
	}

	return numOuterCircles;
    }

    /**
         * Returns the border color of the node.
         * 
         * @param isHighlighted
         *                <code>true</code> if the node is highlighted
         * @param isSelected
         *                <code>true</code> if the node is selected
         * @param isDragging
         *                <code>true</code> if the node is being dragged
         * @return the border color
         */
    public JPowerGraphColor getBorderColor(Node theNode, boolean isHighlighted, boolean isSelected, boolean isDragging,
	    SubGraphHighlighter theSubGraphHighlighter) {
	return borderColor;
    }

    /**
         * Returns the background color of the node.
         * 
         * @param isHighlighted
         *                <code>true</code> if the node is highlighted
         * @param isSelected
         *                <code>true</code> if the node is selected
         * @param isDragging
         *                <code>true</code> if the node is being dragged
         * @return the background color
         */
    public JPowerGraphColor getBackgroundColor(Node theNode, boolean isHighlighted, boolean isSelected, boolean isDragging,
	    SubGraphHighlighter theSubGraphHighlighter) {
	return backgroundColor;
    }

    /**
         * Returns the text color of the node
         * 
         * @param isHighlighted
         *                <code>true</code> if the node is highlighted
         * @param isSelected
         *                <code>true</code> if the node is selected
         * @param isDragging
         *                <code>true</code> if the node is being dragged
         * @return the background color
         */
    public JPowerGraphColor getTextColor(Node theNode, boolean isHighlighted, boolean isSelected, boolean isDragging,
	    SubGraphHighlighter theSubGraphHighlighter) {
	return textColor;
    }

    /**
         * Checks whether given point is inside the node.
         * 
         * @param graphPane
         *                the graph pane
         * @param node
         *                the node
         * @param point
         *                the point
         * @return <code>true</code> if the point is in the node
         */
    public boolean isInNode(JGraphPane graphPane, Node node, JPowerGraphPoint point, int size, double theScale) {
	JPowerGraphRectangle nodeScreenRectangle = new JPowerGraphRectangle(0, 0, 0, 0);
	getNodeScreenBounds(graphPane, node, size, theScale, nodeScreenRectangle);
	return nodeScreenRectangle.contains(point);
    }

    /**
         * Returns the outer rectangle of the node on screen.
         * 
         * @param graphPane
         *                the graph pane
         * @param node
         *                the node
         * @param nodeScreenRectangle
         *                the rectangle receiving the node's coordinates
         */
    public void getNodeScreenBounds(JGraphPane graphPane, Node node, int size, double theScale, JPowerGraphRectangle nodeScreenRectangle) {
	JPowerGraphPoint nodePoint = graphPane.getScreenPointForNode(node);

	int numInstances = ((FloraInstanceClusterNode) node).getNumberOfInstances();

	Node dummyNode = new NullNode();
	JPowerGraphRectangle dummyRectangle = new JPowerGraphRectangle(0, 0, 0, 0);
	nodePainter.getNodeScreenBounds(graphPane, dummyNode, ShapeNodePainter.SMALL, getInstanceScale(numInstances), dummyRectangle);
	dummyRectangle.width += 2;
	dummyRectangle.height += 2;

	int numOuterCircles = countOuterCircles(numInstances);
	int numRows = (numOuterCircles * 2) + 1;
	int[] rowSizes = getRowSizes(numInstances, numRows);
	int numCols = Integer.MIN_VALUE;
	for (int i = 0; i < rowSizes.length; i++) {
	    numCols = Math.max(numCols, rowSizes[i]);
	}
	String label = "" + numInstances;

	int width = 0;
    width = 20;
    int stringWidth = stringWidth(graphPane.getJPowerGraphGraphics(), label);
    width += (stringWidth + (stringWidth / 4));
    width = (int) Math.ceil(width * theScale);

	nodeScreenRectangle.x = nodePoint.x - width / 2;
	nodeScreenRectangle.y = nodePoint.y - width / 2;
	nodeScreenRectangle.width = width;
	nodeScreenRectangle.height = width;
    }

    /**
     * returns a scale depending on the number of Instances
     * 
     * @param numInstances the number of Instances
     * @return a scale depending on the number of Instances
     */
    private double getInstanceScale(int numInstances) {
	double scale = 0.5;
	if (numInstances > 100) {
	    scale -= 0.1;
	}
	if (numInstances > 200) {
	    scale -= 0.1;
	}
	if (numInstances > 400) {
	    scale -= 0.1;
	}
	if (numInstances > 800) {
	    scale -= 0.1;
	}
	return scale;
    }

    /**
     * returns the maximal length of a line in a given String
     * 
     * @param g the Graphics
     * @param s the String to test
     * @return the maximal length of a line in a given String
     */
    private int stringWidth(JPowerGraphGraphics g, String s) {
	int max = Integer.MIN_VALUE;
	for (String line : getLines(s)) {
	    max = Math.max(max, g.getStringWidth(line));
	}
	return max;
    }

    /**
     * returns the number of lines in a given String
     * 
     * @param s the String to test
     * @return the number of lines in a given String
     */
    private int countLines(String s) {
	return getLines(s).size();
    }

    /**
     * returns a ArrayList containing the given String. Each element hosts one line
     * 
     * @param s the String to split
     * @return a ArrayList containing the given String. Each element hosts one line
     */
    private ArrayList<String> getLines(String s) {
	ArrayList<String> result = new ArrayList<String>();
	BufferedReader br = new BufferedReader(new StringReader(s));
	try {
	    String line = "";
	    while ((line = br.readLine()) != null) {
		result.add(line);
	    }
	} catch (Exception e) {
	}
	return result;
    }

    /* (non-Javadoc)
     * @see net.sourceforge.jpowergraph.painters.NodePainter#paintLegendItem(net.sourceforge.jpowergraph.swtswinginteraction.JPowerGraphGraphics, net.sourceforge.jpowergraph.swtswinginteraction.geometry.JPowerGraphPoint, java.lang.String)
     */
    public void paintLegendItem(JPowerGraphGraphics g, JPowerGraphPoint thePoint, String legendText) {
	int padding = 2;
	int imageWidth = 8;
	int imageHeight = 8;
	int imageX = thePoint.x + (padding * 2);
	int imageY = thePoint.y;
	int textX = imageX + imageWidth + (padding * 6);
	int textY = imageY - 3;

	JPowerGraphColor bgColor = getBackgroundColor(new DefaultNode(), false, false, false, new DefaultSubGraphHighlighter());
	JPowerGraphColor boColor = getBorderColor(new DefaultNode(), false, false, false, new DefaultSubGraphHighlighter());
	JPowerGraphColor teColor = getTextColor(new DefaultNode(), false, false, false, new DefaultSubGraphHighlighter());

	JPowerGraphColor oldFGColor = g.getForeground();
	JPowerGraphColor oldBGColor = g.getBackground();

	g.setBackground(bgColor);
	g.fillOval(imageX, imageY, imageWidth, imageHeight);
	g.setForeground(boColor);
	g.drawOval(imageX, imageY, imageWidth, imageHeight);

	g.setBackground(instanceBackgroundColor);
	g.fillOval(imageX + imageWidth / 2 - 1, imageY + imageHeight / 2 - 1, 1, 1);
	g.fillOval(imageX + imageWidth / 2 - 1, imageY + imageHeight / 2 + 1, 1, 1);
	g.fillOval(imageX + imageWidth / 2 + 1, imageY + imageHeight / 2 - 1, 1, 1);
	g.fillOval(imageX + imageWidth / 2 + 1, imageY + imageHeight / 2 + 1, 1, 1);
	g.fillOval(imageX + imageWidth / 2 + 1, imageY + imageHeight / 2, 1, 1);
	g.fillOval(imageX + imageWidth / 2 - 1, imageY + imageHeight / 2, 1, 1);

	g.setBackground(oldBGColor);
	g.setForeground(teColor);
	g.drawString(legendText, textX, textY, 1);
	g.setForeground(oldFGColor);
	g.setBackground(oldBGColor);
    }

    /* (non-Javadoc)
     * @see net.sourceforge.jpowergraph.painters.NodePainter#getLegendItemSize(net.sourceforge.jpowergraph.pane.JGraphPane, java.lang.String)
     */
    public JPowerGraphDimension getLegendItemSize(JGraphPane graphPane, String legendText) {
	int padding = 2;
	int imageWidth = 18;
	int imageHeight = 8;
	int stringWidth = stringWidth(graphPane.getJPowerGraphGraphics(), legendText);
	int width = imageWidth + stringWidth + (padding * 3);
	int height = Math.max(imageHeight, graphPane.getJPowerGraphGraphics().getAscent() + graphPane.getJPowerGraphGraphics().getDescent() + 4);
	return new JPowerGraphDimension(width, height);
    }

    /**
     * returns the Border Color of the Cluster
     * 
     * @return the Border Color of the Cluster
     */
    public JPowerGraphColor getBorderColor() {
	return borderColor;
    }

    /**
     * returns the Background Color of the Cluster
     * 
     * @return the Background Color of the Cluster
     */
    public JPowerGraphColor getBackgroundColor() {
	return backgroundColor;
    }
}
