/* File:      FloraReasonerPreferencePage.java
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

package net.sourceforge.flora.eclipse.reasoner.preference;

import net.sourceforge.flora.eclipse.preferences.FloraPreferenceConstants;
import net.sourceforge.flora.eclipse.reasoner.FloraReasoner;
import net.sourceforge.flora.eclipse.reasoner.FloraReasonerPlugin;
import net.sourceforge.flora.eclipse.reasoner.action.RestartReasonerAction;

import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * The Preference Page for the {@link FloraReasoner}
 * 
 * @author Daniel Winkler
 */
public class FloraReasonerPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    private String fFloraDir;
    private String fEngine;
    private String fModuleName;

    /**
     * the Constructor
     */
    public FloraReasonerPreferencePage() {
	super(GRID);
	setPreferenceStore(FloraReasonerPlugin.getDefault().getPreferenceStore());
	setDescription("Flora Reasoner Preference Page");
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.preference.FieldEditorPreferencePage#createFieldEditors()
     */
    public void createFieldEditors() {
	addField(new DirectoryFieldEditor(FloraPreferenceConstants.REASONER_FLORA_DIRECTORY, "&Flora Directory", getFieldEditorParent()));
	addField(new RadioGroupFieldEditor(FloraPreferenceConstants.REASONER_ENGINE, "Flora &Engine", 1, new String[][] {
		{ "Subprocess", "Subprocess" }, { "Native", "Native" } }, getFieldEditorParent()));
	addField(new StringFieldEditor(FloraPreferenceConstants.REASONER_VISUALIZER_MODULE_NAME, "Visualizer &Module Name", getFieldEditorParent()));
	
	fFloraDir = getPreferenceStore().getString(FloraPreferenceConstants.REASONER_FLORA_DIRECTORY);
	fEngine = getPreferenceStore().getString(FloraPreferenceConstants.REASONER_ENGINE);
	fModuleName = getPreferenceStore().getString(FloraPreferenceConstants.REASONER_VISUALIZER_MODULE_NAME);
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
     */
    public void init(IWorkbench workbench) {

    }

    /** checks if the Preferences are changed. If so the {@link FloraReasoner}
     * is restarted.
     * 
     * @see org.eclipse.jface.preference.FieldEditorPreferencePage#performOk()
     * @see RestartReasonerAction
     * @see FloraReasoner
     */
    public boolean performOk() {
	boolean temp = super.performOk();

	String newFloraDir = getPreferenceStore().getString(FloraPreferenceConstants.REASONER_FLORA_DIRECTORY);
	String newEngine = getPreferenceStore().getString(FloraPreferenceConstants.REASONER_ENGINE);
	String newModuleName = getPreferenceStore().getString(FloraPreferenceConstants.REASONER_ENGINE);

	if (!fFloraDir.equals(newFloraDir))
	    FloraReasoner.getInstance().restart();
	else if (!fEngine.equals(newEngine))
	    FloraReasoner.getInstance().restart();
	else if (!fModuleName.equals(newModuleName))
	    FloraReasoner.getInstance().restart();

	fFloraDir = newFloraDir;
	fEngine = newEngine;
	fModuleName = newModuleName;

	return temp;
    }

}
