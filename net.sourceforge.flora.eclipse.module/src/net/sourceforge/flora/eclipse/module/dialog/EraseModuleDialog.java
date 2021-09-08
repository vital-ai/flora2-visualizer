/* File:      EraseModuleDialog.java
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

package net.sourceforge.flora.eclipse.module.dialog;

import net.sourceforge.flora.eclipse.reasoner.object.FloraModel;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;

/**
 * A Dialog to choose the modules to erase
 * 
 * @author Daniel Winkler
 */
public class EraseModuleDialog extends Dialog {
	private Object[] fSelection;

	private CheckboxTableViewer tableViewer;

	/**
	 * the Constructor
	 * 
	 * @param parentShell
	 *            the parent shell, or <code>null</code> to create a top-level shell
	 * @see org.eclipse.jface.dialogs.Dialog
	 */
	public EraseModuleDialog(Shell parentShell) {
		super(parentShell);
	}
	
//	/**
//	 * trims the window to a maximal size of 800x600
//	 * @see org.eclipse.jface.dialogs.Dialog#getInitialSize()
//	 */
//	protected Point getInitialSize() {
//		Point point = super.getInitialSize();
//		point.x = ((point.x > 800) ? 800 : point.x); 
//		point.y = ((point.y > 600) ? 800 : point.y);
//		return point;
//	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);

		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		area.setLayout(layout);

		Label description = new Label(area, SWT.NONE);
		GridData gridData = new GridData();
		gridData.horizontalSpan = 1;
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		description.setLayoutData(gridData);
		description.setText("Please choose modules to erase");

		Table table = new Table(area, SWT.CHECK | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		tableViewer = new CheckboxTableViewer(table);
		gridData = new GridData();
		gridData.horizontalSpan = 1;
		gridData.horizontalAlignment = GridData.FILL;
		gridData.widthHint = 200;
		gridData.heightHint = 200;
		tableViewer.getControl().setLayoutData(gridData);
		tableViewer.setContentProvider(new TableViewerContentProvider());
		tableViewer.setLabelProvider(new TableViewerLabelProvider());
		tableViewer.setInput(0);

		if (fSelection != null)
			tableViewer.setCheckedElements(fSelection);

		tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent event) {
				fSelection = tableViewer.getCheckedElements();
			}

		});

		return area;
	}

	/**
	 * Returns the checked elements as <code>Object []</code>
	 * 
	 * @return the checked elements as <code>Object []</code>
	 */
	public Object[] getSelection() {
		if (fSelection != null)
			return fSelection;
		else
			return new Object[] {};
	}

	private class TableViewerContentProvider implements IStructuredContentProvider {

		public Object[] getElements(Object inputElement) {
			return FloraModel.getInstance().getModules().toArray();
		}

		public void dispose() {
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

	}

	private class TableViewerLabelProvider implements ITableLabelProvider {

		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			return element.toString();
		}

		public void addListener(ILabelProviderListener listener) {
		}

		public void dispose() {
		}

		public boolean isLabelProperty(Object element, String property) {
			return false;
		}

		public void removeListener(ILabelProviderListener listener) {
		}

	}
}
