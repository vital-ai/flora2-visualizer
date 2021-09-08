/* File:      FloraModelView.java
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

import java.util.Observable;
import java.util.Observer;

import net.sourceforge.flora.eclipse.module.dialog.AddModuleDialog;
import net.sourceforge.flora.eclipse.module.dialog.EraseModuleDialog;
import net.sourceforge.flora.eclipse.module.dialog.FilterModuleDialog;
import net.sourceforge.flora.eclipse.module.provider.ModelContentProvider;
import net.sourceforge.flora.eclipse.module.provider.ModelLabelProvider;
import net.sourceforge.flora.eclipse.reasoner.object.FloraModel;
import net.sourceforge.flora.eclipse.reasoner.object.FloraModule;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;

public class FloraModelView extends ViewPart implements Observer {
	private TableViewer viewer;

	private Object[] filter;

	private ModuleFilter moduleFilter;

	private FloraModel model;

	private Action addModuleAction;

	private Action eraseModuleAction;

	private Action openSelectionAction;

	private Action filterAction;

	/**
	 * The constructor.
	 */
	public FloraModelView() {
		model = FloraModel.getInstance();
		if (model != null)
			model.addObserver(this);
	}

	/**
	 * adds a TableViewer to the View
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createPartControl(Composite parent) {
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.setContentProvider(new ModelContentProvider());
		viewer.setLabelProvider(new ModelLabelProvider());
		// viewer.setSorter(new NameSorter());
		viewer.setInput(model);

		// moduleFilter = new ModuleFilter();
		//
		// viewer.addFilter(moduleFilter);

		makeActions();
		hookContextMenu();
		hookClickAction();
		hookDoubleClickAction();
		contributeToActionBars();

		new FloraModelDropTarget(viewer);
	}

	/**
	 * adds the context menu
	 */
	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	/**
	 * adds the Actions to the Bars/Menus
	 * 
	 * @see #fillLocalPullDown(IMenuManager)
	 * @see #fillLocalToolBar(IToolBarManager)
	 */
	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	/**
	 * adds the addModuleAction to the Pull Down Menu
	 * 
	 * @param manager
	 *            the MenuManager
	 */
	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(addModuleAction);
		manager.add(eraseModuleAction);
		manager.add(filterAction);
		manager.add(new Separator());
	}

	/**
	 * adds the addModuleAction to the Context Menu
	 * 
	 * @param manager
	 *            the MenuManager
	 */
	private void fillContextMenu(IMenuManager manager) {
		manager.add(addModuleAction);
		manager.add(eraseModuleAction);
		manager.add(filterAction);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	/**
	 * adds the addModuleAction to the ToolBar
	 * 
	 * @param manager
	 *            the MenuManager
	 */
	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(addModuleAction);
	}

	/**
	 * makes the actions for adding, hiding, erasing and opening Modules
	 */
	private void makeActions() {
		addModuleAction = new Action() {
			public void run() {
				AddModuleDialog dialog = new AddModuleDialog(Display.getCurrent().getActiveShell());
				if (dialog.open() != InputDialog.OK)
					return;
				model.addModule(dialog.getModuleName().trim());
			}
		};
		addModuleAction.setText("create new module");
		addModuleAction.setToolTipText("click here to create a new module");
		addModuleAction.setImageDescriptor(ImageDescriptor.createFromFile(FloraModelView.class, "new.gif"));

		eraseModuleAction = new Action() {
			public void run() {
				EraseModuleDialog dialog = new EraseModuleDialog(Display.getCurrent().getActiveShell());
				if (dialog.open() == Dialog.OK) {
					Object[] selection = dialog.getSelection();

					if (selection == null)
						return;
					else
						for (Object module : selection)
							if (module instanceof FloraModule) {
								model.deleteModule((FloraModule) module);
							}
				}
			}
		};
		eraseModuleAction.setText("show erase dialog");
		eraseModuleAction.setToolTipText("click here to open the dialog to erase module(s)");
		eraseModuleAction.setImageDescriptor(ImageDescriptor.createFromFile(FloraModelView.class, "erase.gif"));

		filterAction = new Action() {
			public void run() {
				FilterModuleDialog dialog = new FilterModuleDialog(Display.getCurrent().getActiveShell(), filter);
				if (dialog.open() == Dialog.OK) {
					filter = dialog.getFilter();

					viewer.resetFilters();
					moduleFilter = new ModuleFilter(filter);
					viewer.addFilter(moduleFilter);
				}
			}
		};
		filterAction.setText("show filter dialog");
		filterAction.setToolTipText("click here to open the dialog to filer module(s)");
		filterAction.setImageDescriptor(ImageDescriptor.createFromFile(FloraModelView.class, "filter.gif"));

		openSelectionAction = new Action() {
			public void run() {
				ISelection selection = viewer.getSelection();
				Object obj = ((IStructuredSelection) selection).getFirstElement();
				if (obj == null)
					return;
				IWorkbenchPage page = getSite().getPage();
				FloraModule module = (FloraModule) obj;
				try {
					page.openEditor(module, "net.sourceforge.flora.eclipse.visualizer.floraVisualizer");
				} catch (PartInitException e) {
					e.printStackTrace();
				}
			}
		};
	}

	/**
	 * adds a <code>ISelectionChangedListener</code> listener to the Viewer
	 */
	private void hookClickAction() {
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				openSelectionAction.run();
			}
		});
	}

	/**
	 * adds a DoubleClick listener to the Viewer
	 */
	private void hookDoubleClickAction() {
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				openSelectionAction.run();
			}
		});
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	public void update(Observable arg0, Object arg1) {
		viewer.refresh();
	}

	private class ModuleFilter extends ViewerFilter {
		Object[] fFilter;

		public ModuleFilter(Object[] filter) {
			super();
			if (filter != null)
				this.fFilter = filter;
			else
				this.fFilter = new Object[] {};
		}

		@Override
		public boolean select(Viewer viewer, Object parentElement, Object element) {
			for (Object object : fFilter) {
				if (object.toString().equals(element.toString()))
					return false;
			}
			return true;
		}

	}
}