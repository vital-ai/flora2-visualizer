/* File:      FloraDocumentFormatter.java
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

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.eclipse.jface.text.Assert;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TypedPosition;
import org.eclipse.text.edits.ReplaceEdit;
import org.eclipse.text.edits.TextEdit;

/**
 * @author Daniel Winkler
 *
 * This class formats Flora-2 source code
 */
public class FloraDocumentFormatter {

    private StringBuffer formattedText;
    private IDocument fDocument;

    /**
     * The constructor </br>
     * Sets the document to the given document
     * 
     * @param document the document to set
     * @see #fDocument
     */
    public FloraDocumentFormatter(IDocument document) {
	fDocument = document;
    }

    /**
     * Formats the given text
     * 
     * @param partition the partition to format
     * @return the formatted text
     * @see #format(String, int, int)
     * @see IDocument#get()
     */
    public TextEdit format(TypedPosition partition) {
	return format(fDocument.get(), partition.offset, partition.length);
    }

    /**
     * formats the given text
     * 
     * @param string the text to format
     * @param offset the offset of the text to format in the given string
     * @param length the length of the text to format in the given string
     * @return the formatted text
     * @see #format(String)
     * @see String#substring(int, int)
     * @see ReplaceEdit
     */
    public TextEdit format(String string, int offset, int length) {
	String newText = format(string.substring(offset, length));
	return new ReplaceEdit(offset, length, newText);
    }

    /**
     * formats the given text
     * 
     * @param documentText the text to format
     * @return the formatted text
     */
    public String format(String documentText) {

	Assert.isNotNull(documentText);

	Reader stringReader = new StringReader(documentText);
	formattedText = new StringBuffer();

	try {
	    while (true) {
		stringReader.mark(1);
		int intChar = stringReader.read();
		stringReader.reset();

		if (intChar != -1) {
		    FloraLexer lexer = new FloraLexer(stringReader);
		    formattedText.append(lexer.lex().toString());
		} else {
		    break;
		}
	    }
	    stringReader.close();
	} catch (IOException e) {
	    e.printStackTrace();
	}
	return formattedText.toString();
    }

//    private void copyNode(Reader stringReader, StringBuffer out) throws IOException {
//
////	FloraReader commandReader = ModelReaderFactory.createReaderFor(stringReader);
////	out.append(commandReader.getFormattedText());
//	
//	FloraLexer lexer = new FloraLexer(stringReader);
//	out.append(lexer.lex().toString());
//    }

    /**
     * returns true if the given char is a whitespace
     * 
     * @param ch the char to test
     * @return <code>true</code> if the given char is a whitespace
     */
    public static boolean isWhitespace(char ch) {
	return Character.isWhitespace(ch);
    }

    /**
     *  returns <code>true</code> if the char is a space char but not a line delimiters.
     *  <code>== Character.isWhitespace(ch) && !isLineDelimiterChar(ch)</code>
     * 
     * @param ch the char to test
     * @return <code>true</code> if the char is a space char but not a line delimiters.
     */
    public static boolean isIndentChar(char ch) {
	return Character.isWhitespace(ch) && !isLineDelimiterChar(ch);
    }

    /**
     *  returns <code>true</code> if the char is a line delimiter char.
     * 
     * @param ch the char to test
     * @return <code>true</code> if the char is a line delimiter char.
     */
    public static boolean isLineDelimiterChar(char ch) {
	return ch == '\n' || ch == '\r';
    }
}
