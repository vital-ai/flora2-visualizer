/* File:      FloraSourceEditorPlugin.java
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

package net.sourceforge.flora.eclipse.texteditor;

import java.io.File;
import java.io.IOException;

import net.sourceforge.flora.eclipse.texteditor.editor.FloraColorManager;
import net.sourceforge.flora.eclipse.texteditor.editor.scanners.FloraPartitionScanner;

import org.eclipse.jface.text.rules.IPartitionTokenScanner;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class FloraSourceEditorPlugin extends AbstractUIPlugin {
    private File bufferFile = null;

    // The plug-in ID
    public static final String PLUGIN_ID = "net.sourceforge.flora.eclipse.texteditor.texteditor";

    // The shared instance
    private static FloraSourceEditorPlugin plugin;

    private FloraPartitionScanner partitionScanner = null;
    private FloraColorManager colorManager = null;

    /**
         * The constructor
         */
    public FloraSourceEditorPlugin() {
	plugin = this;
    }

    /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
         */
    public void start(BundleContext context) throws Exception {
	super.start(context);
    }

    /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
         */
    public void stop(BundleContext context) throws Exception {
	plugin = null;
	super.stop(context);
    }

    /**
         * Returns the shared instance
         * 
         * @return the shared instance
         */
    public static FloraSourceEditorPlugin getDefault() {
	return plugin;
    }

    public IPartitionTokenScanner getFloraPartitionScanner() {
	if (this.partitionScanner == null)
	    this.partitionScanner = new FloraPartitionScanner();
	return partitionScanner;
    }

    public FloraColorManager getFloraColorManager() {
	if (colorManager == null)
	    colorManager = new FloraColorManager();
	return colorManager;
    }

    public File getBufferFile() {
	try {
	    if (bufferFile == null)
		bufferFile = File.createTempFile("buffer", ".flr");
	} catch (IOException e) {
	    e.printStackTrace();
	}
	return bufferFile;
    }

    public File getCleanBufferFile() {
	bufferFile = null;
	return getBufferFile();
    }

}