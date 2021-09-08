/* File:      FloraColorManager.java
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

package net.sourceforge.flora.eclipse.texteditor.editor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.sourceforge.flora.eclipse.texteditor.FloraSourceEditorPlugin;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

/**
 * @author Daniel Winkler
 * 
 * A manager for colors needed for syntax highlighting
 */
public class FloraColorManager {
	public static final RGB DEFAULT = new RGB(0, 0, 0);
	public static final RGB KEYWORD_DEFAULT = new RGB(128, 0, 128);
	public static final RGB SIGNS_DEFAULT = new RGB(128, 0, 128);
	public static final RGB ATSIGN_DEFAULT = new RGB(128, 0, 128);
	public static final RGB STRING_DEFAULT = new RGB(255, 0, 0);
	public static final RGB NUMBER_DEFAULT = new RGB(0, 0, 255);
	public static final RGB METHOD_DEFAULT = new RGB(0, 255, 0);
	public static final RGB VARIABLE_DEFAULT = new RGB(128, 128, 128);
	public static final RGB OBJECT_DEFAULT = new RGB(255, 128, 128);
	public static final RGB INSTANCE_DEFAULT = new RGB(20, 120, 220);
	
	public static final RGB MULTI_LINE_COMMENT_DEFAULT = new RGB(0, 128, 128);
	public static final RGB SINGLE_LINE_COMMENT_DEFAULT = new RGB(0, 128, 128);
	

	protected static Map<RGB, Color> fColorTable = new HashMap<RGB, Color>(10);
	private static IPreferenceStore store = null;

	/**
	 * disposes all colors
	 * 
	 * @see Color#dispose()
	 */
	public void dispose() {
		Iterator<Color> e = fColorTable.values().iterator();
		while (e.hasNext())
			(e.next()).dispose();
	}

	/**
	 * Returns a {@link Color} for a {@link RGB} value
	 * 
	 * @param rgb the <code>RGB</code> value
	 * @return the color for the specified <code>RGB</code> value
	 */
	public static Color getColor(RGB rgb) {
		Color color = (Color) fColorTable.get(rgb);
		if (color == null) {
			color = new Color(Display.getCurrent(), rgb);
			fColorTable.put(rgb, color);
		}
		return color;
	}

	/**
	 * Retruns the color for a identifier by looking up in the {@link PreferenceStore}
	 * 
	 * @param color the identifier
	 * @returnthe color for a identifier by looking up in the {@link PreferenceStore}
	 * @see FloraSourceEditorPlugin#getPreferenceStore()
	 */
	public static Color getColor(String color) {
		store = FloraSourceEditorPlugin.getDefault().getPreferenceStore();
		return getColor(PreferenceConverter.getColor(store, color));
		}
}
