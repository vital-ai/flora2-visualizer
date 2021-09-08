/* File:      FormatAction.java
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

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

/**
 * @author Daniel Winkler
 * 
 * Formats the Text of the current <code>FloraEditor<code>
 */
public class FormatAction extends AbstractFloraEditorAction {

	/**
	 * Formats the Text of the current <code>FloraEditor<code></br>
	 * If no text is selected all text will be formatted  
	 * 
	 * @see FloraEditor#format()
	 * @see net.sourceforge.flora.eclipse.texteditor.actions.AbstractFloraEditorAction#run()
	 */
	public void run() {
		if (fEditor != null) {

			Job theJob = new Job("Formatting") {
				protected IStatus run(IProgressMonitor monitor) {
					fEditor.format();
					return new Status(IStatus.OK, "net.sourceforge.flora.eclipse.texteditor", IStatus.OK, "formatted Text", null);
				}
			};
			theJob.setUser(true);
			theJob.schedule();
		}
	}

	/**
	 * creates and returns a new {@link FormatAction}
	 * 
	 * @return a new <code>FormatAction<code>
	 * 
	 * @see net.sourceforge.flora.eclipse.texteditor.actions.AbstractFloraEditorAction#createAction()
	 */
	protected AbstractFloraEditorAction createAction() {
		return new FormatAction();
	}

}
