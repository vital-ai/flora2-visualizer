/* File:      FloraModelDropTarget.java
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

package net.sourceforge.flora.eclipse.module.view;

import java.io.File;

import net.sourceforge.flora.eclipse.reasoner.FloraReasoner;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.TableItem;

/**
 * The <code>DropTargetAdapter</code> for the TableViewer in the Flora-2 Module View
 * 
 * @author Daniel Winkler
 */
public class FloraModelDropTarget extends DropTargetAdapter {

	private TableViewer viewer;

	private Menu menu;

	/**
	 * the Constructor
	 * 
	 * @param viewer the TableViewer for which the drop support should be added
	 */
	public FloraModelDropTarget(TableViewer viewer) {
		this.viewer = viewer;

		DropTarget target = new DropTarget(viewer.getControl(), DND.DROP_MOVE
				| DND.DROP_COPY);

		target.setTransfer(new Transfer[] { FileTransfer.getInstance() });

		target.addDropListener(this);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.dnd.DropTargetAdapter#dragOver(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	public void dragOver(DropTargetEvent event) {
		if (event.item == null)
			event.detail = DND.DROP_NONE;
		else if (event.item instanceof TableItem)
			event.detail = DND.DROP_MOVE;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.dnd.DropTargetAdapter#dragOperationChanged(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	public void dragOperationChanged(DropTargetEvent event) {
		if (event.item == null)
			event.detail = DND.DROP_NONE;
		else if (event.item instanceof TableItem)
			event.detail = DND.DROP_MOVE;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.dnd.DropTargetAdapter#drop(org.eclipse.swt.dnd.DropTargetEvent)
	 */
	public void drop(final DropTargetEvent event) {
		if (FileTransfer.getInstance().isSupportedType(event.currentDataType)
				&& (event.data instanceof String[]) && (event.item != null)) {

			final String module = ((TableItem) event.item).getText();

			if (module == null)
				return;

			menu = new Menu(viewer.getControl().getShell(), SWT.POP_UP);
			MenuItem loadEraseRegistryItem = new MenuItem(menu, SWT.PUSH);
			loadEraseRegistryItem.setText("load to module and erase registered objects");
			loadEraseRegistryItem.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
			    	Cursor cursor = new Cursor(Display.getCurrent(), SWT.CURSOR_WAIT);
					Display.getCurrent().getActiveShell().setCursor(cursor);

					boolean first = true; // if more Files are selected, then just erase one time
					for (String path : (String[]) event.data) {
						FloraReasoner.getInstance().loadFile(new File(path),
								module, first);
						first = false;
					}
					
					Display.getCurrent().getActiveShell().setCursor(null);
				}
			});
			MenuItem loadKeepRegistryItem = new MenuItem(menu, SWT.PUSH);
			loadKeepRegistryItem.setText("load to module and keep registered objects");
			loadKeepRegistryItem.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
			    	Cursor cursor = new Cursor(Display.getCurrent(), SWT.CURSOR_WAIT);
					Display.getCurrent().getActiveShell().setCursor(cursor);

					for (String path : (String[]) event.data)
						FloraReasoner.getInstance().loadFile(new File(path),
								module, false);
					
					Display.getCurrent().getActiveShell().setCursor(null);
				}
			});
			MenuItem addItem = new MenuItem(menu, SWT.PUSH);
			addItem.setText("add to module and keep registered objects");
			addItem.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
			    	Cursor cursor = new Cursor(Display.getCurrent(), SWT.CURSOR_WAIT);
					Display.getCurrent().getActiveShell().setCursor(cursor);

					for (String path : (String[]) event.data)
						FloraReasoner.getInstance().addFile(new File(path),
								module);
					
					Display.getCurrent().getActiveShell().setCursor(null);
				}
			});

			menu.setLocation(event.x, event.y);
			menu.setVisible(true);
		}
	}
}