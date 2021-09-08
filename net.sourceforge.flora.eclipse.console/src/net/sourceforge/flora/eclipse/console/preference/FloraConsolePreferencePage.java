/* File:      FloraConsolePreferencePage.java
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

package net.sourceforge.flora.eclipse.console.preference;

import net.sourceforge.flora.eclipse.console.FloraConsolePlugin;
import net.sourceforge.flora.eclipse.preferences.FloraPreferenceConstants;

import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * A Preference Page for the Flora-2 Console
 * 
 * @author Daniel Winkler
 */
public class FloraConsolePreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    /**
     * the Constructor
     */
    public FloraConsolePreferencePage() {
	super(GRID);
	setPreferenceStore(FloraConsolePlugin.getDefault().getPreferenceStore());
	setDescription("Flora Console Preference Page");
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.preference.FieldEditorPreferencePage#createFieldEditors()
     */
    public void createFieldEditors() {
	addField(new ColorFieldEditor(
		FloraPreferenceConstants.CONSOLE_COLOR_DEFAULT,
		"&Default color:", getFieldEditorParent()));
	addField(new ColorFieldEditor(
		FloraPreferenceConstants.CONSOLE_COLOR_RESPONSE,
		"&Response color:", getFieldEditorParent()));
	addField(new ColorFieldEditor(
		FloraPreferenceConstants.CONSOLE_COLOR_ERROR,
		"&Error color:", getFieldEditorParent()));
	
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
     */
    public void init(IWorkbench workbench) {
    }


}
