/* File:      FloraVisualizerPreferenceInitializer.java
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

package net.sourceforge.flora.eclipse.visualizer.preference;

import net.sourceforge.flora.eclipse.preferences.FloraPreferenceConstants;
import net.sourceforge.flora.eclipse.visualizer.FloraVisualizerPlugin;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.swt.graphics.RGB;

/**
 * Preference Initializer for the Vizualizer
 * 
 * @author Daniel Winkler
 */
public class FloraVisualizerPreferenceInitializer extends AbstractPreferenceInitializer {

    @Override
    public void initializeDefaultPreferences() {
	IPreferenceStore store = FloraVisualizerPlugin.getDefault().getPreferenceStore();

	store.setDefault(FloraPreferenceConstants.VISUALIZER_ZOOM_LEVEL, 75);
	store.setDefault(FloraPreferenceConstants.VISUALIZER_NODE_SIZE, FloraPreferenceConstants.VISUALIZER_SMALL_NODE_SIZE);
	store.setDefault(FloraPreferenceConstants.VISUALIZER_USE_INSTANCE_CLUSTERS, true);
	store.setDefault(FloraPreferenceConstants.VISUALIZER_MINIMUM_INSTANCE_CLUSTER_SIZE, 5);

	PreferenceConverter.setDefault(store, FloraPreferenceConstants.VISUALIZER_TEXT_COLOR, new RGB(0, 0, 0));

	PreferenceConverter.setDefault(store, FloraPreferenceConstants.VISUALIZER_MODULE_NODE_COLOR, new RGB(255, 255, 255));
	PreferenceConverter.setDefault(store, FloraPreferenceConstants.VISUALIZER_CLASS_NODE_COLOR, new RGB(0, 255, 0));
	PreferenceConverter.setDefault(store, FloraPreferenceConstants.VISUALIZER_INSTANCE_NODE_COLOR, new RGB(0, 0, 255));
	PreferenceConverter.setDefault(store, FloraPreferenceConstants.VISUALIZER_MODULE_NODE_COLOR_BORDER, new RGB(255, 255, 255));
	PreferenceConverter.setDefault(store, FloraPreferenceConstants.VISUALIZER_CLASS_NODE_COLOR_BORDER, new RGB(128, 255, 128));
	PreferenceConverter.setDefault(store, FloraPreferenceConstants.VISUALIZER_INSTANCE_NODE_COLOR_BORDER, new RGB(128, 128, 255));
    }

}
