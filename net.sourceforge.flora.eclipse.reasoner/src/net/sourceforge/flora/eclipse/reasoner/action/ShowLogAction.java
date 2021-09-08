/* File:      ShowLogAction.java
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

package net.sourceforge.flora.eclipse.reasoner.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import net.sourceforge.flora.eclipse.reasoner.FloraReasoner;

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
 * Action which opens a new FloraEditor containing the logFile
 * <p>
 * needs an open project to open the editor
 * 
 * @author Daniel Winkler
 */
public class ShowLogAction implements IWorkbenchWindowActionDelegate {

    public void dispose() {
    }

    public void init(IWorkbenchWindow window) {
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
     */
    public void run(IAction action) {
	try {

	    IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
	    File file = FloraReasoner.getInstance().getLogFile();
	    FileInputStream fis = new FileInputStream(file);
	    IProject[] projects = root.getProjects();
	    FileEditorInput input = null;
	    for (int i = 0; i < projects.length; i++) {
		IProject project = projects[i];
		if (project.isOpen()) {

		    try {
			if (!project.getFile(".log.flr").exists())
			    project.getFile(".log.flr").create(fis, true, null);
			else	
			    project.getFile(".log.flr").setContents(fis, true, false, null);
			IFile iFile = project.getFile(".log.flr");
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
