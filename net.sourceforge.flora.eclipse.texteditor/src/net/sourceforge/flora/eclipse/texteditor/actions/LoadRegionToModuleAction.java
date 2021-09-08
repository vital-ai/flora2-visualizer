/* File:      LoadRegionToModuleAction.java
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

import net.sourceforge.flora.eclipse.reasoner.FloraReasoner;
import net.sourceforge.flora.eclipse.texteditor.dialog.ChooseModuleDialog;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.swt.widgets.Display;

/**
 * @author Daniel Winkler
 * 
 * Loads the selected Text of the current FloraEditor to a specified
 * module of the {@link FloraReasoner}
 */
public class LoadRegionToModuleAction extends AbstractFloraEditorAction {

	private String fModule;

	private String fRegion;

	/**
	 * creates and returns a new {@link LoadRegionToModuleAction}
	 * 
	 * @return a new <code>LoadRegionToModuleAction<code>
	 * 
	 * @see net.sourceforge.flora.eclipse.texteditor.actions.AbstractFloraEditorAction#createAction()
	 */
	public AbstractFloraEditorAction createAction() {
		return new LoadRegionToModuleAction();
	}

	/**
	 * loads the selected Text of the current FloraEditor to a specified
	 * module of the {@link FloraReasoner}. The module is specified by a
	 * {@link ChooseModuleDialog}.
	 * 
	 * @see ChooseModuleDialog
	 * @see FloraReasoner#loadString(String, String)
	 * @see net.sourceforge.flora.eclipse.texteditor.actions.AbstractFloraEditorAction#run()
	 */
	public void run() {
		if (fEditor != null) {

			fRegion = fEditor.getTextSelection();
			fModule = "";
			ChooseModuleDialog dialog = new ChooseModuleDialog(fEditor.getEditorSite().getShell());
			if (dialog.open() != InputDialog.OK)
				return;
			fModule = dialog.getModuleName().trim();

			Job theJob = new Job("Loading region to " + fModule) {
				protected IStatus run(IProgressMonitor monitor) {
				    Display.getDefault().asyncExec(new Runnable() {
					    public void run() {
						FloraReasoner.getInstance().loadString(fRegion, fModule);
					    }
					});
					return new Status(IStatus.OK, "net.sourceforge.flora.eclipse.texteditor", IStatus.OK, "Region loaded", null);
				}
			};
			theJob.setUser(true);
			theJob.schedule();
		}
	}
}
