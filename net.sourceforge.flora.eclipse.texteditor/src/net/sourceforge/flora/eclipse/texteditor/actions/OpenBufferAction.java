/* File:      OpenBufferAction.java
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import net.sourceforge.flora.eclipse.texteditor.FloraSourceEditorPlugin;
import net.sourceforge.flora.eclipse.texteditor.editor.FloraEditor;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

/**
 * @author Daniel Winkler
 * 
 * Opens a new {@link FloraEditor} with a clean file as buffer for
 * tests
 */
public class OpenBufferAction implements IWorkbenchWindowActionDelegate {

	public void dispose() {
	}

	public void init(IWorkbenchWindow window) {
	}

	/**
	 * Opens a new {@link FloraEditor} with a clean file as buffer.
	 * The file is stored in the first open project found.
	 * 
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	public void run(IAction action) {
		try {

			IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();

			File file = FloraSourceEditorPlugin.getDefault().getBufferFile();
			FileInputStream fis = new FileInputStream(file);
			IProject[] projects = root.getProjects();
			FileEditorInput input = null;
			for (int i = 0; i < projects.length; i++) {
				IProject project = projects[i];
				if (project.isOpen()) {

					try {
						if (project.getFile(".buffer.flr").exists())
							project.getFile(".buffer.flr").delete(true, null);
						project.getFile(".buffer.flr").create(fis, false, null);
						IFile iFile = project.getFile(".buffer.flr");
						input = new FileEditorInput(iFile);
					} catch (CoreException e) {
						e.printStackTrace();
					}

					IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
					IWorkbenchPage page = window.getActivePage();
					page.openEditor(input, "net.sourceforge.flora.eclipse.texteditor.texteditor");
					break;
				}
			}
		} catch (PartInitException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void selectionChanged(IAction action, ISelection selection) {
	}

}
