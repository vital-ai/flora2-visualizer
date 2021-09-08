/* File:      NodePopupDialog.java
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

import net.sourceforge.flora.eclipse.reasoner.object.FloraClass;
import net.sourceforge.flora.eclipse.reasoner.object.FloraInstance;
import net.sourceforge.flora.eclipse.reasoner.object.IFloraElement;
import net.sourceforge.flora.eclipse.visualizer.graph.element.FloraNode;
import net.sourceforge.jpowergraph.Node;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

/**
 * Popup Dialog to show the properties of a Node in a <code>FloraGraph</code>
 * 
 * @author Daniel Winkler
 */
public class NodeDialog extends Dialog {

    private IFloraElement fElement;

    /**
     * the Constructor
     * 
     * @param parent the parent Shell
     * @param node the Node 
     * @param module the <code>FloraModule</code> where the Node exists
     */
    public NodeDialog(Shell parent, Node node) {
	this(parent, ((FloraNode) node).getElement());
    }

    /**
     * the Constructor
     * 
     * @param parent the parent Shell
     * @param element the Element 
     * @param module the <code>FloraModule</code> where the Element exists
     */
    public NodeDialog(Shell parent, IFloraElement element) {
	super(parent);
	fElement = element;
    }

	/**
	 * trims the window to a maximal size of 800x600
	 * @see org.eclipse.jface.dialogs.Dialog#getInitialSize()
	 */
	protected Point getInitialSize() {
		Point point = super.getInitialSize();
		point.x = ((point.x > 800) ? 800 : point.x); 
		point.y = ((point.y > 600) ? 800 : point.y);
		return point;
	}

    /* (non-Javadoc)
     * @see org.eclipse.jface.dialogs.PopupDialog#createDialogArea(org.eclipse.swt.widgets.Composite)
     */
    protected Control createDialogArea(Composite parent) {
	Composite composite = (Composite) super.createDialogArea(parent);
	composite.setLayout(new FillLayout());

	CTabFolder theFolder = new CTabFolder(composite, SWT.NONE);

	// Value Tab
	CTabItem item = new CTabItem(theFolder, SWT.NONE);
	item.setText("Values");

	TableViewer viewer = new TableViewer(theFolder, SWT.H_SCROLL | SWT.V_SCROLL);
	// Set up the table
	Table table = viewer.getTable();
	
	TableColumn tc = new TableColumn(table, SWT.LEFT, 0);
	tc.setWidth(150);

	tc = new TableColumn(table, SWT.CENTER, 1);
	tc.setWidth(50);

	tc = new TableColumn(table, SWT.RIGHT, 2);
	tc.setWidth(150);

	table.setHeaderVisible(false);
	table.setLinesVisible(true);

	viewer.setContentProvider(new ValueContentProvider());
	viewer.setLabelProvider(new ElementLabelProvider());
	viewer.setInput(fElement);

	item.setControl(viewer.getControl());

	// Types
	item = new CTabItem(theFolder, SWT.NONE);
	item.setText("Types");

	viewer = new TableViewer(theFolder, SWT.H_SCROLL | SWT.V_SCROLL);
	// Set up the table
	table = viewer.getTable();

	tc = new TableColumn(table, SWT.LEFT, 0);
	tc.setWidth(150);

	tc = new TableColumn(table, SWT.CENTER, 1);
	tc.setWidth(50);

	tc = new TableColumn(table, SWT.RIGHT, 2);
	tc.setWidth(150);

	table.setHeaderVisible(false);
	table.setLinesVisible(true);

	viewer.setContentProvider(new TypeContentProvider());
	viewer.setLabelProvider(new ElementLabelProvider());
	viewer.setInput(fElement);

	item.setControl(viewer.getControl());

	return composite;
    }

    /**
     * Content Provider for {@link NodeDialog}
     * <p>
     * Shows the types of a element
     * 
     * @author Daniel Winkler
     */
    private class TypeContentProvider implements IStructuredContentProvider {

	public void dispose() {
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

	}

	public Object[] getElements(Object inputElement) {
		if (fElement instanceof FloraClass)
	    return ((FloraClass)fElement).getTypes().toArray();
		if (fElement instanceof FloraInstance)
		    return ((FloraInstance)fElement).getTypes().toArray();
		else
			return new Object[]{};
	}
    }

    /**
     * Content Provider for {@link NodeDialog}
     * <p>
     * Shows the values of a element
     * 
     * @author Daniel Winkler
     */
    private class ValueContentProvider implements IStructuredContentProvider {

	public void dispose() {
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

	}

	public Object[] getElements(Object inputElement) {
		if (fElement instanceof FloraClass)
	    return ((FloraClass)fElement).getValues().toArray();
		if (fElement instanceof FloraInstance)
		    return ((FloraInstance)fElement).getValues().toArray();
		else
			return new Object[]{};
	}
    }

    /**
     * Label Provider for {@link NodeDialog}
     * 
     * @author Daniel Winkler
     */
    private class ElementLabelProvider extends LabelProvider implements ITableLabelProvider {

	public Image getColumnImage(Object element, int columnIndex) {
	    return null;
	}

	public String getColumnText(Object line, int columnIndex) {
		Object element = ((Object[]) line)[columnIndex];
		if (element != null)
			return element.toString();
		else
			return "";
	}

    }

}
