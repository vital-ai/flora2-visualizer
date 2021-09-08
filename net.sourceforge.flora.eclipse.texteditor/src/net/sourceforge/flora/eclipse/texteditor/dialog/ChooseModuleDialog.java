/* File:      ChooseModuleDialog.java
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

package net.sourceforge.flora.eclipse.texteditor.dialog;

import java.util.ArrayList;

import net.sourceforge.flora.eclipse.reasoner.object.FloraModel;
import net.sourceforge.flora.eclipse.reasoner.object.FloraModule;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
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
 * @author Daniel Winkler
 * 
 * A dialog to select a Flora-2 module
 */
public class ChooseModuleDialog extends Dialog {
	private String moduleName;

	/**
	 * The constructor
	 * 
	 * @param parentShell the parent Shell
	 */
	public ChooseModuleDialog(Shell parentShell) {
		super(parentShell);
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

	/**
	 * creates a {@link TableViewer} containing all currently loaded modules
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);

		area.setLayout(new FillLayout());

		final TableViewer viewer = new TableViewer(area, SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
		// Set up the table
		Table table = viewer.getTable();

		TableColumn tc = new TableColumn(table, SWT.LEFT, 0);
		tc.setWidth(200);

		table.setHeaderVisible(false);
		table.setLinesVisible(true);

		viewer.setContentProvider(new FloraElementContentProvider());
		viewer.setLabelProvider(new FloraElementLabelProvider());
		viewer.setInput(FloraModel.getInstance());

		viewer.addSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent event) {
				moduleName = ((IStructuredSelection) viewer.getSelection()).getFirstElement().toString();
			}

		});

		return area;
	}

	/**
	 * @author Daniel Winkler
	 * 
	 * The content provider for the {@link ChooseModuleDialog} <code>TableViewer</code>
	 */
	private class FloraElementContentProvider implements IStructuredContentProvider {

		public void dispose() {
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

		}

		/**
		 * returns all currently loaded modules
		 * 
		 * @return all currently loaded modules
		 * 
		 * @see FloraModel#getModules()
		 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
		 */
		public Object[] getElements(Object inputElement) {
			ArrayList<FloraModule> modules = ((FloraModel) inputElement).getModules();

			return modules.toArray(new Object[] {});
		}

	}

	/**
	 * @author Daniel Winkler
	 * 
	 * The label provider for the {@link ChooseModuleDialog} <code>TableViewer</code>
	 */
	private class FloraElementLabelProvider extends LabelProvider implements ITableLabelProvider {

		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		/**
		 * returns the name of a element
		 * 
		 * @return the name of a element
		 * 
		 * @see FloraModule#toString()
		 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
		 */
		public String getColumnText(Object element, int columnIndex) {
			return ((FloraModule) element).toString();
		}

	}

	/**
	 * @return the name of the selected module 
	 */
	public String getModuleName() {
		return moduleName;
	}
}
