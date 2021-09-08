/* File:      VisualizerDoubleClickListener.java
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

package net.sourceforge.flora.eclipse.visualizer.dialog;

import net.sourceforge.flora.eclipse.reasoner.object.FloraModule;
import net.sourceforge.flora.eclipse.visualizer.graph.FloraGraphPane;
import net.sourceforge.flora.eclipse.visualizer.graph.element.FloraClassNode;
import net.sourceforge.flora.eclipse.visualizer.graph.element.FloraInstanceClusterNode;
import net.sourceforge.flora.eclipse.visualizer.graph.element.FloraInstanceNode;
import net.sourceforge.jpowergraph.Edge;
import net.sourceforge.jpowergraph.Legend;
import net.sourceforge.jpowergraph.Node;
import net.sourceforge.jpowergraph.manipulator.Manipulator;
import net.sourceforge.jpowergraph.manipulator.dragging.DraggingManipulator;
import net.sourceforge.jpowergraph.manipulator.selection.DoubleClickListener;
import net.sourceforge.jpowergraph.swtswinginteraction.listeners.JPowerGraphMouseEvent;


/**
 * DoubleClickListener for the Visualizer
 * <p>
 * opens popups depending on the selected Node
 * 
 * @author Daniel Winkler
 */
public class VisualizerDoubleClickListener implements DoubleClickListener {

    private FloraGraphPane fPane;

    /**
     * the Constructor
     * 
     * @param pane the current Pane
     */
    public VisualizerDoubleClickListener(FloraGraphPane pane) {
	fPane = pane;
    }

    public void doubleClick(JPowerGraphMouseEvent theMouseEvent) {

    }

    public void doubleClick(JPowerGraphMouseEvent theMouseEvent, Legend theLegend) {
    }

    /** opens a Popup depending on the selected Node
     * 
     * @see net.sourceforge.jpowergraph.manipulator.selection.DoubleClickListener#doubleClick(net.sourceforge.jpowergraph.swtswinginteraction.listeners.JPowerGraphMouseEvent, net.sourceforge.jpowergraph.Node)
     * @see NodePopupDialog#NodePopupDialog(org.eclipse.swt.widgets.Shell, Node, FloraModule)
     * @see ClusterPopupDialog#ClusterPopupDialog(org.eclipse.swt.widgets.Shell, Node, FloraModule)
     */
    public void doubleClick(JPowerGraphMouseEvent theMouseEvent, Node theNode) {

	if (theNode instanceof FloraClassNode) {
	    new NodeDialog(fPane.getShell(), theNode).open();
	}
	
	if (theNode instanceof FloraInstanceNode) {
	    new NodeDialog(fPane.getShell(), theNode).open();
	}
	
	if (theNode instanceof FloraInstanceClusterNode) {
	    new ClusterDialog(fPane.getShell(), theNode).open();
	}
	
	Manipulator manipulator = fPane.getManipulator("DraggingManipulator");
	if ((manipulator != null) && (manipulator instanceof DraggingManipulator)) {
		DraggingManipulator dManipulator = (DraggingManipulator) manipulator;
		dManipulator.mouseUp(theMouseEvent);
	}
    }

    public void doubleClick(JPowerGraphMouseEvent theMouseEvent, Edge theEdge) {
    }

}
