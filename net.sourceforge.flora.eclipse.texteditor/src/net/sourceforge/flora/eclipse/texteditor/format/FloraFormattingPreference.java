/* File:      FloraFormattingPreference.java
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

package net.sourceforge.flora.eclipse.texteditor.format;

import java.util.ArrayList;

import net.sourceforge.flora.eclipse.preferences.FloraPreferenceConstants;

import org.eclipse.jface.text.formatter.FormattingContext;

/**
 * @author Daniel Winkler
 *
 * Formatting context for the formatter. 
 */
public class FloraFormattingPreference extends FormattingContext {

    /* (non-Javadoc)
     * @see org.eclipse.jface.text.formatter.FormattingContext#getPreferenceKeys()
     */
    public String[] getPreferenceKeys() {
	ArrayList<String> preferenceKeys = new ArrayList<String>();
	preferenceKeys.add(FloraPreferenceConstants.EDITOR_INDENTATION_SIZE);
	preferenceKeys.add(FloraPreferenceConstants.EDITOR_INDENTATION_TYPE);

	return preferenceKeys.toArray(new String[] {});
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.text.formatter.FormattingContext#isIntegerPreference(java.lang.String)
     */
    public boolean isIntegerPreference(String key) {
	return key.equals(FloraPreferenceConstants.EDITOR_INDENTATION_SIZE);
    }

}
