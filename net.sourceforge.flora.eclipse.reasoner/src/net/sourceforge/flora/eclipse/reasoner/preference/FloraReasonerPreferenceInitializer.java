/* File:      FloraReasonerPreferenceInitializer.java
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

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * The initializer for the {@link FloraReasoner} preferences
 * 
 * @author Daniel Winkler
 */
public class FloraReasonerPreferenceInitializer extends
		AbstractPreferenceInitializer {

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
	 */
	public void initializeDefaultPreferences() {
		IPreferenceStore store = FloraReasonerPlugin.getDefault().getPreferenceStore();

		store.setDefault(FloraPreferenceConstants.REASONER_ENGINE, "Subprocess");
		store.setDefault(FloraPreferenceConstants.REASONER_FLORA_DIRECTORY, "/home/flora2");
		store.setDefault(FloraPreferenceConstants.REASONER_VISUALIZER_MODULE_NAME, "visualizer");
	}

}
