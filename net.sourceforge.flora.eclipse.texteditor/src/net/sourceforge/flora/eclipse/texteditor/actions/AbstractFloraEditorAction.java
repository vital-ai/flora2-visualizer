/* File:      AbstractFloraEditorAction.java
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

package net.sourceforge.flora.eclipse.texteditor.actions;

import net.sourceforge.flora.eclipse.texteditor.editor.FloraEditor;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchSite;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

/**
 * @author Daniel Winkler
 * 
 * abstract acion for Flora-2 Editor actions </br>
 * the subclassed action needs to implement the {@link #createAction()} mehtod which returns
 * a new Action (by calling the Constructor) and the {@link #run()} method
 * which implements the stuff to do
 */
public abstract class AbstractFloraEditorAction extends Action implements IEditorActionDelegate, IWorkbenchWindowActionDelegate {

	protected AbstractFloraEditorAction fAction;

	protected FloraEditor fEditor;

	protected ISelection fSelection;

	protected IWorkbenchWindow fWindow;

	/**
	 * creates a new Action and returns it
	 * 
	 * @return a new Action
	 */
	protected abstract AbstractFloraEditorAction createAction();

	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public abstract void run();

	/**
	 * sets the variable and calls the {@link #runAction(FloraEditor)} method
	 * 
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	public void run(IAction action) {
		FloraEditor editor = null;

		IWorkbenchSite site = null;
		if (fWindow == null) {
			runAction(this.fEditor);
			return;
		}

		IWorkbenchPage page = fWindow.getActivePage();
		if (page == null) {
			runAction(this.fEditor);
			return;
		}

		IEditorPart part = page.getActiveEditor();
		if (part instanceof FloraEditor) {
			editor = (FloraEditor) part;
			site = editor.getSite();
		} else {
			editor = null;
		}

		if (editor == null) {
			runAction(this.fEditor);
			return;
		}

		if (site != null) {
			runAction(editor);
		}
	}

	/**
	 * creates a new action runs the {@link #run()} of the created action
	 * 
	 * @param editor the FloraEditor to run the Action on
	 */
	private void runAction(FloraEditor editor) {
		if (fAction == null) {
			fAction = createAction();
		}
		fAction.fEditor = editor;
		fAction.run();
	}

	public void setActiveEditor(IAction action, IEditorPart targetEditor) {
		if (targetEditor instanceof FloraEditor)
			fEditor = (FloraEditor) targetEditor;
	}

	public void selectionChanged(IAction action, ISelection selection) {
		this.fSelection = selection;
	}

	public void dispose() {
		fWindow = null;
	}

	public void init(IWorkbenchWindow window) {
		this.fWindow = window;
	}

}
