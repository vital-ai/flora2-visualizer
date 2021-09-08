/* File:      FloraGraphPane.java
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

import net.sourceforge.flora.eclipse.preferences.FloraPreferenceConstants;
import net.sourceforge.flora.eclipse.visualizer.FloraVisualizerPlugin;
import net.sourceforge.flora.eclipse.visualizer.dialog.VisualizerDoubleClickListener;
import net.sourceforge.flora.eclipse.visualizer.graph.element.ClusterEdge;
import net.sourceforge.flora.eclipse.visualizer.graph.element.FloraClassNode;
import net.sourceforge.flora.eclipse.visualizer.graph.element.FloraInstanceClusterNode;
import net.sourceforge.flora.eclipse.visualizer.graph.element.FloraInstanceNode;
import net.sourceforge.flora.eclipse.visualizer.graph.element.FloraModuleNode;
import net.sourceforge.flora.eclipse.visualizer.graph.element.FloraNode;
import net.sourceforge.flora.eclipse.visualizer.graph.manipulator.FloraContextMenuListener;
import net.sourceforge.flora.eclipse.visualizer.graph.painter.ClusterEdgePainter;
import net.sourceforge.flora.eclipse.visualizer.graph.painter.ClusterNodePainter;
import net.sourceforge.flora.eclipse.visualizer.graph.painter.FloraEdgePainter;
import net.sourceforge.flora.eclipse.visualizer.graph.painter.FloraNodePainter;
import net.sourceforge.jpowergraph.Graph;
import net.sourceforge.jpowergraph.NodeLegendItem;
import net.sourceforge.jpowergraph.layout.Layouter;
import net.sourceforge.jpowergraph.layout.spring.SpringLayoutStrategy;
import net.sourceforge.jpowergraph.lens.CursorLens;
import net.sourceforge.jpowergraph.lens.LegendLens;
import net.sourceforge.jpowergraph.lens.LensSet;
import net.sourceforge.jpowergraph.lens.NodeSizeLens;
import net.sourceforge.jpowergraph.lens.RotateLens;
import net.sourceforge.jpowergraph.lens.TooltipLens;
import net.sourceforge.jpowergraph.lens.TranslateLens;
import net.sourceforge.jpowergraph.lens.ZoomLens;
import net.sourceforge.jpowergraph.manipulator.dragging.DraggingManipulator;
import net.sourceforge.jpowergraph.manipulator.popup.PopupManipulator;
import net.sourceforge.jpowergraph.manipulator.selection.DoubleClickManipulator;
import net.sourceforge.jpowergraph.manipulator.selection.NodeSelectionModel;
import net.sourceforge.jpowergraph.manipulator.selection.SelectionManipulator;
import net.sourceforge.jpowergraph.painters.NodePainter;
import net.sourceforge.jpowergraph.painters.node.ShapeNodePainter;
import net.sourceforge.jpowergraph.swt.SWTJGraphPane;
import net.sourceforge.jpowergraph.swt.manipulator.SWTPopupDisplayer;
import net.sourceforge.jpowergraph.swt.viewcontrols.RotateControlPanel;
import net.sourceforge.jpowergraph.swt.viewcontrols.ZoomControlPanel;
import net.sourceforge.jpowergraph.swtswinginteraction.color.JPowerGraphColor;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

/**
 * The Pane on which the <code>FloraGraph</code> is drawn
 * 
 * @author Daniel Winkler
 */
public class FloraGraphPane extends SWTJGraphPane {

	private TranslateLens translateLens;

	private ZoomLens zoomLens;

	private RotateLens rotateLens;

	private CursorLens cursorLens;

	private TooltipLens tooltipLens;

	private LegendLens legendLens;

	private NodeSizeLens nodeSizeLens;

	private Layouter m_layouter;

	private NodeSelectionModel nodeSelectionModel;

	/**
	 * the constructor
	 * 
	 * @param parent
	 *            the parent <code>Composite</code> of the new
	 *            <code>FloraGraphPane</code>
	 * @param graph
	 *            the <code>FloraGraph</code> which should be diplayed
	 * @param theNodeSelectionModel
	 *            the <code>NodeSelectionModel</code> for the
	 *            <code>FloraGraph</code>
	 */
	public FloraGraphPane(Composite parent, Graph graph,
			NodeSelectionModel theNodeSelectionModel) {
		super(parent, graph);

		this.nodeSelectionModel = theNodeSelectionModel;

		refreshNodePainters();

		this.setEdgePainter(ClusterEdge.class, new ClusterEdgePainter());
		this.setDefaultEdgePainter(new FloraEdgePainter(this));

		m_layouter = new Layouter(new SpringLayoutStrategy(graph));
		m_layouter.start();

		translateLens = new TranslateLens();
		zoomLens = new ZoomLens();
		rotateLens = new RotateLens();
		cursorLens = new CursorLens();
		tooltipLens = new TooltipLens();
		legendLens = new LegendLens();
		nodeSizeLens = new NodeSizeLens();

		LensSet lensSet = new LensSet();
		lensSet.addLens(zoomLens);
		lensSet.addLens(rotateLens);
		lensSet.addLens(translateLens);
		lensSet.addLens(cursorLens);
		lensSet.addLens(tooltipLens);
		lensSet.addLens(legendLens);
		lensSet.addLens(nodeSizeLens);
		this.setLens(lensSet);

		nodeSizeLens.setNodeSize(FloraVisualizerPlugin.getDefault()
				.getNodeSize());
		zoomLens.setZoomFactor(FloraVisualizerPlugin.getDefault()
				.getZoomLevel());

		this.setPopupDisplayer(new SWTPopupDisplayer(Display.getCurrent(),
				null, new FloraContextMenuListener(getGraph(), lensSet,
						ZoomControlPanel.DEFAULT_ZOOM_LEVELS,
						RotateControlPanel.DEFAULT_ROTATE_ANGLES)));

		this.addManipulator(new SelectionManipulator(nodeSelectionModel));

		this.addManipulator(new DoubleClickManipulator(this,
				new VisualizerDoubleClickListener(this)));

		this.addManipulator(new DraggingManipulator(cursorLens));

		this.addManipulator(new PopupManipulator(this, tooltipLens));

		super.setAntialias(true);
		this.setEnabled(true);
	}

	/**
	 * @param theShape
	 *            the wanted shape for the Node (see {@link ShapeNodePainter}
	 *            for shapes)
	 * @param thePreferenceConstant
	 *            the <code>FloraPreferenceConstant</code> for the shape
	 *            colors
	 * @return a <code>ShapeNodePainter</code> for the given shape and the
	 *         preferences for the given PreferenceConstant
	 */
	private NodePainter buildShapeNodePainter(int theShape,
			String thePreferenceConstant) {
		JPowerGraphColor mainColor = FloraVisualizerPlugin.getDefault()
				.getNodeMainColor(thePreferenceConstant);
		JPowerGraphColor borderColor = FloraVisualizerPlugin.getDefault()
				.getNodeBorderColor(thePreferenceConstant);
		JPowerGraphColor textColor = FloraVisualizerPlugin.getDefault()
				.getColor(FloraPreferenceConstants.VISUALIZER_TEXT_COLOR);
		return new ShapeNodePainter(theShape, mainColor, borderColor, textColor);
	}

	/**
	 * @param theShape
	 *            the wanted shape for the Node (see {@link ShapeNodePainter}
	 *            for shapes)
	 * @param thePreferenceConstant
	 *            the <code>FloraPreferenceConstant</code> for the shape
	 *            colors
	 * @return a <code>FloraNodePainter</code> for the given shape and the
	 *         preferences for the given PreferenceConstant
	 */
	private NodePainter buildFloraNodePainter(int theShape,
			String thePreferenceConstant) {
		JPowerGraphColor mainColor = FloraVisualizerPlugin.getDefault()
				.getNodeMainColor(thePreferenceConstant);
		JPowerGraphColor borderColor = FloraVisualizerPlugin.getDefault()
				.getNodeBorderColor(thePreferenceConstant);
		JPowerGraphColor textColor = FloraVisualizerPlugin.getDefault()
				.getColor(FloraPreferenceConstants.VISUALIZER_TEXT_COLOR);
		return new FloraNodePainter(theShape, mainColor, borderColor, textColor);
	}

	/**
	 * @param theShape
	 *            the wanted shape for the subNodes of the ClusterNode (see
	 *            {@link ShapeNodePainter} for shapes)
	 * @param thePreferenceConstant
	 *            the <code>FloraPreferenceConstant</code> for the subNode
	 *            shape colors
	 * @return a <code>ClusterNodePainter</code> for the given shape and the
	 *         preferences for the given PreferenceConstant
	 */
	private ClusterNodePainter buildInstanceClusterNodePainter(int theShape,
			String thePreferenceConstant) {
		JPowerGraphColor mainColor = FloraVisualizerPlugin.getDefault()
				.getNodeMainColor(thePreferenceConstant);
		JPowerGraphColor borderColor = FloraVisualizerPlugin.getDefault()
				.getNodeBorderColor(thePreferenceConstant);
		JPowerGraphColor textColor = FloraVisualizerPlugin.getDefault()
				.getColor(FloraPreferenceConstants.VISUALIZER_TEXT_COLOR);
		return new ClusterNodePainter(theShape, mainColor, borderColor,
				textColor);
	}

	/**
	 * refreshs the NodePainters
	 * 
	 * @see #buildShapeNodePainter(int, String)
	 * @see #buildInstanceClusterNodePainter(int, String)
	 * @see #setNodePainter(Class,
	 *      net.sourceforge.jpowergraph.painters.NodePainter)
	 */
	public void refreshNodePainters() {

		this.setNodePainter(FloraModuleNode.class, buildShapeNodePainter(
				ShapeNodePainter.ELLIPSE,
				FloraPreferenceConstants.VISUALIZER_MODULE_NODE_COLOR));
		this.setNodePainter(FloraClassNode.class, buildShapeNodePainter(
				ShapeNodePainter.RECTANGLE,
				FloraPreferenceConstants.VISUALIZER_CLASS_NODE_COLOR));
		this.setNodePainter(FloraInstanceNode.class, buildFloraNodePainter(
				ShapeNodePainter.RECTANGLE,
				FloraPreferenceConstants.VISUALIZER_INSTANCE_NODE_COLOR));

		this
				.setNodePainter(
						FloraInstanceClusterNode.class,
						buildInstanceClusterNodePainter(
								ShapeNodePainter.ELLIPSE,
								FloraPreferenceConstants.VISUALIZER_INSTANCE_NODE_COLOR));
		super.redraw();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sourceforge.jpowergraph.swt.SWTJGraphPane#getGraph()
	 */
	public Graph getGraph() {
		return super.getGraph();
	}

	/**
	 * returns the <code>TranslateLens</code>
	 * 
	 * @return the <code>TranslateLens</code>
	 */
	public TranslateLens getTranslateLens() {
		return translateLens;
	}

	/**
	 * returns the <code>ZoomLens</code>
	 * 
	 * @return the <code>ZoomLens</code>
	 */
	public ZoomLens getZoomLens() {
		return zoomLens;
	}

	/**
	 * returns the <code>RotateLens</code>
	 * 
	 * @return the <code>RotateLens</code>
	 */
	public RotateLens getRotateLens() {
		return rotateLens;
	}

	/**
	 * returns the <code>CursorLens</code>
	 * 
	 * @return the <code>CursorLens</code>
	 */
	public CursorLens getCursorLens() {
		return cursorLens;
	}

	/**
	 * returns the <code>TooltipLens</code>
	 * 
	 * @return the <code>TooltipLens</code>
	 */
	public TooltipLens getTooltipLens() {
		return tooltipLens;
	}

	/**
	 * returns the <code>LegendLens</code>
	 * 
	 * @return the <code>LegendLens</code>
	 */
	public LegendLens getLegendLens() {
		return legendLens;
	}

	/**
	 * returns the <code>NodeSizeLens</code>
	 * 
	 * @return the <code>NodeSizeLens</code>
	 */
	public NodeSizeLens getNodeSizeLens() {
		return nodeSizeLens;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sourceforge.jpowergraph.swt.SWTJGraphPane#dispose()
	 */
	public void dispose() {
		m_layouter.stop();
		super.dispose();
	}

	/**
	 * returns the <code>Layouter</code>
	 * 
	 * @return the <code>Layouter</code>
	 */
	public Layouter getLayouter() {
		return m_layouter;
	}

	/**
	 * returns the <code>NodeSelectionModel</code>
	 * 
	 * @return the <code>NodeSelectionModel</code>
	 */
	public NodeSelectionModel getNodeSelectionModel() {
		return nodeSelectionModel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sourceforge.jpowergraph.swt.SWTJGraphPane#refreshLegend(net.sourceforge.jpowergraph.Graph)
	 */
	public void refreshLegend(Graph graph) {
		getLegend().clear();
		FloraNode node = new FloraClassNode(null);
		NodePainter nodePainter = getPainterForNode(node);
		getLegend().add(
				new NodeLegendItem(node.getClass(), nodePainter, node
						.getNodeType()));
		node = new FloraInstanceNode(null);
		nodePainter = getPainterForNode(node);
		getLegend().add(
				new NodeLegendItem(node.getClass(), nodePainter, node
						.getNodeType()));
	}
}
