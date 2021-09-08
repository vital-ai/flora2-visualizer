/* File:      FloraVisualizerPlugin.java
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

package net.sourceforge.flora.eclipse.visualizer;

import net.sourceforge.flora.eclipse.preferences.FloraPreferenceConstants;
import net.sourceforge.jpowergraph.painters.NodePainter;
import net.sourceforge.jpowergraph.swtswinginteraction.color.JPowerGraphColor;

import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 */
public class FloraVisualizerPlugin extends AbstractUIPlugin {

    // The shared instance.
    private static FloraVisualizerPlugin plugin;

    /**
         * The constructor.
         */
    public FloraVisualizerPlugin() {
	plugin = this;
    }

    /**
         * This method is called upon plug-in activation
         */
    public void start(BundleContext context) throws Exception {
	super.start(context);
    }

    /**
         * This method is called when the plug-in is stopped
         */
    public void stop(BundleContext context) throws Exception {
	super.stop(context);
	plugin = null;
    }

    /**
         * Returns the shared instance.
         */
    public static FloraVisualizerPlugin getDefault() {
	return plugin;
    }

    /**
     * Returns an image descriptor for the image file at the given plug-in relative path.
     * 
     * @param path
     *                the path
     * @return the image descriptor
     */
    public static ImageDescriptor getImageDescriptor(String path) {
	return AbstractUIPlugin.imageDescriptorFromPlugin("net.sourceforge.flora.eclipse.visualizer", path);
    }

    /**
     * returns the {@link JPowerGraphColor} with the given key 
     * 
     * @param theKey <code>String</code> key which identifies the color
     * @return the <code>JPowerGraphColor</code> with the given key
     * @see #getPreferenceStore()
     * @see RGB
     * @see PreferenceConverter#getColor(org.eclipse.jface.preference.IPreferenceStore, String)
     * @see JPowerGraphColor#JPowerGraphColor(int, int, int)
     */
    public JPowerGraphColor getColor(String theKey) {
	RGB rgb = PreferenceConverter.getColor(getPreferenceStore(), theKey);
	return new JPowerGraphColor(rgb.red, rgb.green, rgb.blue);
    }

    /**
     * returns the {@link JPowerGraphColor} with the given key 
     * 
     * @param theKey <code>String</code> key which identifies the color
     * @return the <code>JPowerGraphColor</code> with the given key
     * @see #getPreferenceStore()
     * @see RGB
     * @see PreferenceConverter#getColor(org.eclipse.jface.preference.IPreferenceStore, String)
     * @see JPowerGraphColor#JPowerGraphColor(int, int, int)
     */
    public JPowerGraphColor getNodeMainColor(String theKey) {
	RGB rgb = PreferenceConverter.getColor(getPreferenceStore(), theKey);
	return new JPowerGraphColor(rgb.red, rgb.green, rgb.blue);
    }

    /**
     * returns the {@link JPowerGraphColor} with the given key 
     * 
     * @param theKey <code>String</code> key which identifies the color ("Border" is added to the key automatically, you must not add it manually)
     * @return the <code>JPowerGraphColor</code> with the given key
     * @see #getPreferenceStore()
     * @see RGB
     * @see PreferenceConverter#getColor(org.eclipse.jface.preference.IPreferenceStore, String)
     * @see JPowerGraphColor#JPowerGraphColor(int, int, int)
     */
    public JPowerGraphColor getNodeBorderColor(String theKey) {
	RGB rgb = PreferenceConverter.getColor(getPreferenceStore(), theKey + "Border");
	return new JPowerGraphColor(rgb.red, rgb.green, rgb.blue);
    }

    /**
     * returns the Zoom level stored in the <code>PreferenceStore</code>
     * 
     * @return the Zoom level stored in the <code>PreferenceStore</code> 
     * @see #getPreferenceStore()
     */
    public double getZoomLevel() {
	return getPreferenceStore().getInt(FloraPreferenceConstants.VISUALIZER_ZOOM_LEVEL) / 100d;
    }

    /**
     * returns the Node size stored in the <code>PreferenceStore</code>
     * 
     * @return the Node size stored in the <code>PreferenceStore</code> 
     * @see #getPreferenceStore()
     */
    public int getNodeSize() {
	String s = getPreferenceStore().getString(FloraPreferenceConstants.VISUALIZER_NODE_SIZE);
	if (s.equals(FloraPreferenceConstants.VISUALIZER_SMALL_NODE_SIZE)) {
	    return NodePainter.SMALL;
	}
	return NodePainter.LARGE;
    }

    /**
     * returns true if Instance Clusters should be used (value derived by the <code>PreferenceStore</code>)
     * 
     * @return true if Instance Clusters should be used (value derived by the <code>PreferenceStore</code>)
     * @see #getPreferenceStore()
     */
    public boolean getUseInstanceClusters() {
	return getPreferenceStore().getBoolean(FloraPreferenceConstants.VISUALIZER_USE_INSTANCE_CLUSTERS);
    }

    /**
     * returns the minimum number of Instances to create an Instance cluster (value derived by the <code>PreferenceStore</code>)
     * 
     * @return the minimum number of Instances to create an Instance cluster
     * @see #getPreferenceStore()
     */
    public int getMinimumInstanceClusterSize() {
	return getPreferenceStore().getInt(FloraPreferenceConstants.VISUALIZER_MINIMUM_INSTANCE_CLUSTER_SIZE);
    }
}
