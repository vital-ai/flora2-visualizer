/* File:      FloraPartitionScanner.java
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

package net.sourceforge.flora.eclipse.texteditor.editor.scanners;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.flora.eclipse.texteditor.editor.rules.FloraMultiLineComment;
import net.sourceforge.flora.eclipse.texteditor.editor.rules.FloraSingleLineComment;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.Token;

/**
 * @author Daniel Winkler
 *
 * The partition scanner
 */
public class FloraPartitionScanner extends RuleBasedPartitionScanner {

    public static final String FLORA_SINGLE_LINE_COMMENT = "floraSingleLineComment";
    public static final String FLORA_MULTI_LINE_COMMENT = "floraMultiLineComment";

    /**
     * The constructor</br>
     * creates new rules and sets them
     * 
     * @see #setPredicateRules(IPredicateRule[])
     */
    public FloraPartitionScanner() {
	super();

	List<IRule> rules = new ArrayList<IRule>();

	// Comments
	rules.add(new FloraSingleLineComment(new Token(FLORA_SINGLE_LINE_COMMENT)));
	rules.add(new FloraMultiLineComment(new Token(FLORA_MULTI_LINE_COMMENT)));

	// Convert list to array
	IPredicateRule[] result = new IPredicateRule[rules.size()];
	rules.toArray(result);
	setPredicateRules(result);
    }

    /**
     * @return the scanner IDs
     */
    public static ArrayList<String> getScannerIDs() {
	ArrayList<String> result = new ArrayList<String>();
	result.add(FLORA_SINGLE_LINE_COMMENT);
	result.add(FLORA_MULTI_LINE_COMMENT);

	return result;
    }

    /**
     * @return the scanner IDs
     */
    public static String[] getScannableScannerIDs() {
	return getScannerIDs().toArray(new String[getScannerIDs().size()]);
    }

    /**
     * @return the scanner IDs plus {@link IDocument#DEFAULT_CONTENT_TYPE}
     */
    public static String[] getAllScannerIDs() {
	ArrayList<String> result = getScannerIDs();
	result.add(0, IDocument.DEFAULT_CONTENT_TYPE);
	return result.toArray(new String[] {});
    }
}
