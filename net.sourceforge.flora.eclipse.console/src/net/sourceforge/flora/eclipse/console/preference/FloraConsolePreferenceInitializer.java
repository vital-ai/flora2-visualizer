/* File:      FloraConsolePreferenceInitializer.java
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

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.swt.graphics.RGB;

/**
 * A Initializer for the Flora-2 Console Preferences
 * 
 * @author Daniel Winkler
 */
public class FloraConsolePreferenceInitializer extends AbstractPreferenceInitializer {

    /* (non-Javadoc)
     * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
     */
    public void initializeDefaultPreferences() {
	IPreferenceStore store = FloraConsolePlugin.getDefault().getPreferenceStore();

	PreferenceConverter.setDefault(store, FloraPreferenceConstants.CONSOLE_COLOR_DEFAULT, new RGB(0,0,0));
	PreferenceConverter.setDefault(store, FloraPreferenceConstants.CONSOLE_COLOR_RESPONSE, new RGB(0,0,255));
	PreferenceConverter.setDefault(store, FloraPreferenceConstants.CONSOLE_COLOR_ERROR, new RGB(255,0,0));
    }

}
