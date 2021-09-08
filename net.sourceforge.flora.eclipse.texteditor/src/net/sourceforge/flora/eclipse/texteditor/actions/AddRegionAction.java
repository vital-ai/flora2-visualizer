/* File:      AddRegionAction.java
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

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.widgets.Display;

/**
 * @author Daniel Winkler
 * 
 * Adds the selected Text of the current FloraEditor to the <code>main</code>
 * module of the {@link FloraReasoner}
 */
public class AddRegionAction extends AbstractFloraEditorAction {

	private String fRegion;

	/**
	 * creates and returns a new {@link AddRegionAction}
	 * 
	 * @return a new <code>AddRegionAction<code>
	 * 
	 * @see net.sourceforge.flora.eclipse.texteditor.actions.AbstractFloraEditorAction#createAction()
	 */
	public AbstractFloraEditorAction createAction() {
		return new AddRegionAction();
	}

	/**
	 * adds the selected Text of the current FloraEditor to the <code>main</code>
	 * module of the {@link FloraReasoner}
	 * 
	 * @see FloraReasoner#addString(String)
	 * @see net.sourceforge.flora.eclipse.texteditor.actions.AbstractFloraEditorAction#run()
	 */
	public void run() {
		if (fEditor != null) {

			fRegion = fEditor.getTextSelection();
			Job theJob = new Job("Adding Region") {
				protected IStatus run(IProgressMonitor monitor) {
				    Display.getDefault().asyncExec(new Runnable() {
					    public void run() {
						FloraReasoner.getInstance().addString(fRegion);
					    }
					});
					return new Status(IStatus.OK, "net.sourceforge.flora.eclipse.texteditor", IStatus.OK, "Region added", null);
				}
			};
			theJob.setUser(true);
			theJob.schedule();
		}
	}

}
