/* File:      FloraPreferencePage.java
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

package net.sourceforge.flora.eclipse.texteditor.preferences;

import net.sourceforge.flora.eclipse.preferences.FloraPreferenceConstants;
import net.sourceforge.flora.eclipse.preferences.PreferencePage;
import net.sourceforge.flora.eclipse.texteditor.FloraSourceEditorPlugin;

import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * @author Daniel Winkler
 *
 * A preference page for the Flora-2 TextEditor
 */
public class FloraPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    /**
     * the constructor</br>
     * sets the preference store and the description of the page
     * 
     * @see PreferencePage#setPreferenceStore(org.eclipse.jface.preference.IPreferenceStore)
     * @see PreferencePage#setDescription(String)
     */
    public FloraPreferencePage() {
	super(GRID);
	setPreferenceStore(FloraSourceEditorPlugin.getDefault().getPreferenceStore());
	setDescription("Flora Texteditor Preference Page");
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.preference.FieldEditorPreferencePage#createFieldEditors()
     */
    public void createFieldEditors() {

	// Indentation
	addField(new IntegerFieldEditor(FloraPreferenceConstants.EDITOR_INDENTATION_SIZE, "&Tab width:", getFieldEditorParent()));
	addField(new RadioGroupFieldEditor(FloraPreferenceConstants.EDITOR_INDENTATION_TYPE, "Indentation Type", 1,
		new String[][] { { "Tab", FloraPreferenceConstants.EDITOR_INDENTATION_TYPE_TAB },
			{ "Space", FloraPreferenceConstants.EDITOR_INDENTATION_TYPE_SPACE } }, getFieldEditorParent()));

	// Syntax highlighting
	addField(new ColorFieldEditor(FloraPreferenceConstants.EDITOR_COLOR_NUMBER, "&Number Color:", getFieldEditorParent()));
	addField(new ColorFieldEditor(FloraPreferenceConstants.EDITOR_COLOR_STRING, "&String Color:", getFieldEditorParent()));
	addField(new ColorFieldEditor(FloraPreferenceConstants.EDITOR_COLOR_KEYWORD, "&Keyword Color:", getFieldEditorParent()));
	addField(new ColorFieldEditor(FloraPreferenceConstants.EDITOR_COLOR_DEFAULT, "&Text Color:", getFieldEditorParent()));
	addField(new ColorFieldEditor(FloraPreferenceConstants.EDITOR_COLOR_SIGN, "&Sign Color:", getFieldEditorParent()));
	addField(new ColorFieldEditor(FloraPreferenceConstants.EDITOR_COLOR_ATSIGN, "&@-Sign Color:", getFieldEditorParent()));
	addField(new ColorFieldEditor(FloraPreferenceConstants.EDITOR_COLOR_VARIABLE, "&Variable Color:", getFieldEditorParent()));
	addField(new ColorFieldEditor(FloraPreferenceConstants.EDITOR_COLOR_OBJECT, "&Object Color:", getFieldEditorParent()));
	addField(new ColorFieldEditor(FloraPreferenceConstants.EDITOR_COLOR_INSTANCE, "&Instance Color:", getFieldEditorParent()));
	addField(new ColorFieldEditor(FloraPreferenceConstants.EDITOR_COLOR_SINGLE_LINE_COMMENT, "&Single line comment Color:",
		getFieldEditorParent()));
	addField(new ColorFieldEditor(FloraPreferenceConstants.EDITOR_COLOR_MULTI_LINE_COMMENT, "&Multi line comment Color:", getFieldEditorParent()));
    }

    /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
         */
    public void init(IWorkbench workbench) {
    }

}