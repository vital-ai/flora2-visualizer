/* File:      FloraPreferenceInitializer.java
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
import net.sourceforge.flora.eclipse.texteditor.FloraSourceEditorPlugin;
import net.sourceforge.flora.eclipse.texteditor.editor.FloraColorManager;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;

/**
 * Class used to initialize default preference values.
 */
public class FloraPreferenceInitializer extends AbstractPreferenceInitializer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
	 */
	public void initializeDefaultPreferences() {
		IPreferenceStore store = FloraSourceEditorPlugin.getDefault()
				.getPreferenceStore();

		store.setDefault(FloraPreferenceConstants.EDITOR_INDENTATION_SIZE, 4);
		store.setDefault(FloraPreferenceConstants.EDITOR_INDENTATION_TYPE, FloraPreferenceConstants.EDITOR_INDENTATION_TYPE_TAB);
		
		PreferenceConverter.setDefault(store, FloraPreferenceConstants.EDITOR_COLOR_NUMBER, FloraColorManager.NUMBER_DEFAULT);
		PreferenceConverter.setDefault(store, FloraPreferenceConstants.EDITOR_COLOR_STRING, FloraColorManager.STRING_DEFAULT);
		PreferenceConverter.setDefault(store, FloraPreferenceConstants.EDITOR_COLOR_KEYWORD, FloraColorManager.KEYWORD_DEFAULT);
		PreferenceConverter.setDefault(store, FloraPreferenceConstants.EDITOR_COLOR_DEFAULT, FloraColorManager.DEFAULT);
		PreferenceConverter.setDefault(store, FloraPreferenceConstants.EDITOR_COLOR_SIGN, FloraColorManager.SIGNS_DEFAULT);
		PreferenceConverter.setDefault(store, FloraPreferenceConstants.EDITOR_COLOR_ATSIGN, FloraColorManager.ATSIGN_DEFAULT);
		PreferenceConverter.setDefault(store, FloraPreferenceConstants.EDITOR_COLOR_VARIABLE, FloraColorManager.VARIABLE_DEFAULT);
		PreferenceConverter.setDefault(store, FloraPreferenceConstants.EDITOR_COLOR_OBJECT, FloraColorManager.OBJECT_DEFAULT);
		PreferenceConverter.setDefault(store, FloraPreferenceConstants.EDITOR_COLOR_OBJECT, FloraColorManager.INSTANCE_DEFAULT);
		PreferenceConverter.setDefault(store, FloraPreferenceConstants.EDITOR_COLOR_SINGLE_LINE_COMMENT, FloraColorManager.SINGLE_LINE_COMMENT_DEFAULT);
		PreferenceConverter.setDefault(store, FloraPreferenceConstants.EDITOR_COLOR_MULTI_LINE_COMMENT, FloraColorManager.MULTI_LINE_COMMENT_DEFAULT);
		
		
	}

}
