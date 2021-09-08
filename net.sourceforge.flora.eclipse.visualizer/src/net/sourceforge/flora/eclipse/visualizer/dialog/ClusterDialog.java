/* File:      ClusterPopupDialog.java
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

import net.sourceforge.flora.eclipse.reasoner.object.FloraInstance;
import net.sourceforge.flora.eclipse.reasoner.object.IFloraElement;
import net.sourceforge.flora.eclipse.visualizer.graph.element.FloraInstanceClusterNode;
import net.sourceforge.flora.eclipse.visualizer.graph.element.FloraNode;
import net.sourceforge.jpowergraph.Node;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

/**
 * A Popup Dialog that shows the elements of a <code>FloraInstanceClusterNode</code>
 * 
 * @author Daniel Winkler
 */
public class ClusterDialog extends Dialog {

	private FloraNode fNode;

	private TableViewer viewer;

	/**
	 * the Constructor
	 * 
	 * @param parent
	 *            the parent Shell
	 * @param node
	 *            the Node for which the information shall be displayed
	 * @param module
	 *            the module in which the Node exists
	 */
	public ClusterDialog(Shell parent, Node node) {
		super(parent);
		fNode = (FloraNode) node;
	}

	/**
	 * trims the window to a maximal size of 800x600
	 * @see org.eclipse.jface.dialogs.Dialog#getInitialSize()
	 */
	protected Point getInitialSize() {
		Point point = super.getInitialSize();
		point.x = ((point.x > 600) ? 600 : point.x); 
		point.y = ((point.y > 600) ? 600 : point.y);
		return point;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.PopupDialog#createDialogArea(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		composite.setLayout(new FillLayout());

		viewer = new TableViewer(composite);
		// Set up the table
		Table table = viewer.getTable();

		TableColumn tc = new TableColumn(table, SWT.LEFT, 0);
		tc.setWidth(100);

		table.setHeaderVisible(false);
		table.setLinesVisible(true);

		viewer.setContentProvider(new ClusterContentProvider());
		viewer.setLabelProvider(new ClusterLabelProvider());
		viewer.setInput(fNode);

		viewer.addDoubleClickListener(new IDoubleClickListener() {

			public void doubleClick(DoubleClickEvent event) {
				IFloraElement element = (IFloraElement) ((IStructuredSelection) event.getSelection()).getFirstElement();
				new NodeDialog(getParentShell(), element).open();

			}

		});

		return composite;
	}

	/**
	 * Content Provider for a {@link ClusterDialog}
	 * 
	 * @author Daniel Winkler
	 */
	private class ClusterContentProvider implements IStructuredContentProvider {

		public void dispose() {
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

		}

		public Object[] getElements(Object inputElement) {
			return ((FloraInstanceClusterNode) fNode).getInstances().toArray(new FloraInstance[] {});
		}

	}

	/**
	 * Label Provider for a {@link ClusterDialog}
	 * 
	 * @author Daniel Winkler
	 */
	private class ClusterLabelProvider extends LabelProvider implements ITableLabelProvider {

		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			return ((FloraInstance) element).toString();
		}

	}

}
