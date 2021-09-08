/* File:      FloraVisualizerPreferencePage.java
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

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * PreferencePage for the Visualizer
 * 
 * @author Daniel Winkler
 */
public class FloraVisualizerPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    /**
     * the Constructor
     */
    public FloraVisualizerPreferencePage() {
	super(GRID);
	setPreferenceStore(FloraVisualizerPlugin.getDefault().getPreferenceStore());
	setDescription("Flora Visualizer Preference Page");
    }

    @Override
    public void createFieldEditors() {

	addField(new ColorFieldEditor(FloraPreferenceConstants.VISUALIZER_MODULE_NODE_COLOR, "Module Color:", getFieldEditorParent()));
	addField(new ColorFieldEditor(FloraPreferenceConstants.VISUALIZER_MODULE_NODE_COLOR_BORDER, "Module Border Color:", getFieldEditorParent()));

	addField(new ColorFieldEditor(FloraPreferenceConstants.VISUALIZER_CLASS_NODE_COLOR, "Class Color:", getFieldEditorParent()));
	addField(new ColorFieldEditor(FloraPreferenceConstants.VISUALIZER_CLASS_NODE_COLOR_BORDER, "Class Border Color:", getFieldEditorParent()));

	addField(new ColorFieldEditor(FloraPreferenceConstants.VISUALIZER_INSTANCE_NODE_COLOR, "Instance Color:", getFieldEditorParent()));
	addField(new ColorFieldEditor(FloraPreferenceConstants.VISUALIZER_INSTANCE_NODE_COLOR_BORDER, "Instance Border Color:",
		getFieldEditorParent()));

	addField(new ColorFieldEditor(FloraPreferenceConstants.VISUALIZER_TEXT_COLOR, "Text Color:", getFieldEditorParent()));

	addField(new IntegerFieldEditor(FloraPreferenceConstants.VISUALIZER_ZOOM_LEVEL, "Zoom Level:", getFieldEditorParent(), 4));
	addField(new RadioGroupFieldEditor(FloraPreferenceConstants.VISUALIZER_NODE_SIZE, "Node Size:", 1, new String[][] {
		{ "Large", FloraPreferenceConstants.VISUALIZER_LARGE_NODE_SIZE }, { "Small", FloraPreferenceConstants.VISUALIZER_SMALL_NODE_SIZE } },
		getFieldEditorParent()));

	addField(new BooleanFieldEditor(FloraPreferenceConstants.VISUALIZER_USE_INSTANCE_CLUSTERS, "Use Instance Cluster:", getFieldEditorParent()));
	addField(new IntegerFieldEditor(FloraPreferenceConstants.VISUALIZER_MINIMUM_INSTANCE_CLUSTER_SIZE, "Minimum Cluster Size:",
		getFieldEditorParent(), 4));
    }

    public void init(IWorkbench workbench) {
    }

}
