/* File:      FloraCodeScanner.java
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

import net.sourceforge.flora.eclipse.FloraKeywords;
import net.sourceforge.flora.eclipse.preferences.FloraPreferenceConstants;
import net.sourceforge.flora.eclipse.texteditor.editor.FloraColorManager;
import net.sourceforge.flora.eclipse.texteditor.editor.rules.FloraCharSequence;
import net.sourceforge.flora.eclipse.texteditor.editor.rules.FloraInstance;
import net.sourceforge.flora.eclipse.texteditor.editor.rules.FloraNumber;
import net.sourceforge.flora.eclipse.texteditor.editor.rules.FloraObject;
import net.sourceforge.flora.eclipse.texteditor.editor.rules.FloraString;
import net.sourceforge.flora.eclipse.texteditor.editor.rules.FloraVariable;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.jface.text.rules.WordRule;
import org.eclipse.swt.SWT;

/**
 * @author Daniel Winkler
 *
 * the Scanner for Flora-2 source code
 */
public class FloraCodeScanner extends RuleBasedScanner {
    private IToken defaultToken = null;
    private IToken keywordToken = null;
    private IToken signToken = null;
    private IToken atSignToken = null;
    private IToken stringToken = null;
    private IToken numberToken = null;
    private IToken variableToken = null;
    private IToken objectToken = null;
    private IToken instanceToken = null;

    /**
     * the constructor </br>
     * creates new instances of the tokens and rules
     * 
     * @see #createRules()
     */
    public FloraCodeScanner() {
	defaultToken = new Token(new TextAttribute(FloraColorManager.getColor(FloraPreferenceConstants.EDITOR_COLOR_DEFAULT)));
	keywordToken = new Token(new TextAttribute(FloraColorManager.getColor(FloraPreferenceConstants.EDITOR_COLOR_KEYWORD), null, SWT.BOLD));
	signToken = new Token(new TextAttribute(FloraColorManager.getColor(FloraPreferenceConstants.EDITOR_COLOR_SIGN)));
	atSignToken = new Token(new TextAttribute(FloraColorManager.getColor(FloraPreferenceConstants.EDITOR_COLOR_ATSIGN)));
	stringToken = new Token(new TextAttribute(FloraColorManager.getColor(FloraPreferenceConstants.EDITOR_COLOR_STRING)));
	numberToken = new Token(new TextAttribute(FloraColorManager.getColor(FloraPreferenceConstants.EDITOR_COLOR_NUMBER)));
	variableToken = new Token(new TextAttribute(FloraColorManager.getColor(FloraPreferenceConstants.EDITOR_COLOR_VARIABLE)));
	objectToken = new Token(new TextAttribute(FloraColorManager.getColor(FloraPreferenceConstants.EDITOR_COLOR_OBJECT)));
	instanceToken = new Token(new TextAttribute(FloraColorManager.getColor(FloraPreferenceConstants.EDITOR_COLOR_INSTANCE)));

	createRules();
    }

    /**
     * creates the rules to scan the source code
     */
    private void createRules() {
	ArrayList<IRule> rules = new ArrayList<IRule>();

	// Generic whitespace rule
	rules.add(new WhitespaceRule(new FloraWhitespaceDetector()));

	// Strings
	rules.add(new FloraString(stringToken));
	rules.add(new FloraCharSequence(stringToken));

	// Numbers
	rules.add(new FloraNumber(numberToken));

	// Methods
	//rules.add(new FloraMethod(methodToken));

	// Variables
	rules.add(new FloraVariable(variableToken));

	// Objects
	rules.add(new FloraObject(objectToken));

	// Instances
	rules.add(new FloraInstance(instanceToken));

	// Keywords
	WordRule wordRule = new WordRule(new WordDetector(), defaultToken);
	for (String keyword : FloraKeywords.getAllKeywords()) {
	    wordRule.addWord(keyword, keywordToken);
	}
	rules.add(wordRule);

	// @ - Sign (must be befor signRule)
	WordRule atSignRule = new WordRule(new AtSignDetector(), defaultToken);
	atSignRule.addWord("@", atSignToken);
	rules.add(atSignRule);
	
	// VRConnectives
	WordRule vrcRule = new WordRule(new ValueReferenceDetector(), defaultToken);
	for (String keyword : FloraKeywords.getValueReferenceConectives()) {
		vrcRule.addWord(keyword, signToken);
	}
	rules.add(vrcRule);

	// Signs
	ArrayList<String> signs = FloraKeywords.getSigns();
	signs.removeAll(FloraKeywords.getValueReferenceConectives());
	WordRule signRule = new WordRule(new SignDetector(), defaultToken);
	for (String keyword : signs) {
	    signRule.addWord(keyword, signToken);
	}
	rules.add(signRule);

	setRules(rules.toArray(new IRule[] {}));
    }

    /**
     * @author Daniel Winkler
     * 
     * the detector to recognize Words 
     */
    private class WordDetector implements IWordDetector {
	public boolean isWordPart(char character) {
	    return Character.isLetterOrDigit(character) || (character == '_');
	}

	public boolean isWordStart(char character) {
	    return Character.isLetter(character);
	}
    }
    
    /**
     * @author Daniel Winkler
     *
     * the detector to recognize Signs
     */
    private class SignDetector implements IWordDetector {
	String chars = "#_%:=?";
	
	public boolean isWordPart(char character) {
	    return (chars.indexOf(character) >= 0);
	}

	public boolean isWordStart(char character) {
	    return (chars.indexOf(character) >= 0);
	}
    }

    /**
     * @author Daniel Winkler
     *
     * the detector to recognize value reference connectives
     */
    private class ValueReferenceDetector implements IWordDetector {
    	String chars = "*-+>";
    	
    	public boolean isWordPart(char character) {
    	    return (chars.indexOf(character) >= 0);
    	}

    	public boolean isWordStart(char character) {
    	    return (chars.indexOf(character) >= 0);
    	}
        }

    /**
     * @author Daniel Winkler
     *
     * the detector to recognize \@ signs
     */
    private class AtSignDetector implements IWordDetector {
	String chars = "@";
	
	public boolean isWordPart(char character) {
	    return (chars.indexOf(character) >= 0);
	}

	public boolean isWordStart(char character) {
	    return (chars.indexOf(character) >= 0);
	}
    }
}
