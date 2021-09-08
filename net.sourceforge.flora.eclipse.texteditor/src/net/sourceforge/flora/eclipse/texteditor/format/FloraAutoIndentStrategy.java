/* File:      FloraAutoIndentStrategy.java
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
import java.io.StringReader;

import net.sourceforge.flora.eclipse.FloraKeywords;
import net.sourceforge.flora.eclipse.texteditor.format.elements.FloraSourceText;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DefaultIndentLineAutoEditStrategy;
import org.eclipse.jface.text.DocumentCommand;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextUtilities;

/**
 * @author Daniel Winkler
 *
 * A <code>DefaultIndentLineAutoEditStrategy</code> for automatic indentation and
 * adding closing parenthesis
 */
public class FloraAutoIndentStrategy extends DefaultIndentLineAutoEditStrategy {
	public void customizeDocumentCommand(IDocument document, DocumentCommand command) {
		if ((command.text == null) || (command.text.length() <= 0))
			return;

		if (command.text.equals("{")) {
			append(command, "}", 1);
			return;
		}
		if (command.text.equals("(")) {
			append(command, ")", 1);
			return;
		}
		if (command.text.equals("[")) {
			append(command, "]", 1);
			return;
		}
		if ("})]".contains(command.text)) {
			try {
				if (command.text.equals(Character.toString(document.getChar(command.offset)))) {
					command.text = "";
					command.offset += 1;
					return;
				}
			} catch (BadLocationException e) {
				// do nothing
			}
		}
		if (command.text.equals("\"")) {
			try {
				if (document.getChar(command.offset) == '\"') {
					command.text = "";
					command.offset += 1;
					return;
				}
				else
					append(command, "\"", 1);
			} catch (BadLocationException e) {
				append(command, "\"", 1);
			}
			return;
		}
		if (command.text.equals("'")) {
			try {
				if (document.getChar(command.offset) == '\'') {
					command.text = "";
					command.offset += 1;
					return;
				}
				else
					append(command, "'", 1);
			} catch (BadLocationException e) {
				append(command, "'", 1);
			}
			return;
		}
		try {
			if (command.text.equals("*") && command.offset > 0 && document.getChar(command.offset - 1) == '/') {
				append(command, " */", 2);
				return;
			}
		} catch (BadLocationException e) {
			return;
		}
		if (command.length == 0 && endsWithDelimiter(document, command.text)) {
			smartIndentAfterNewLine(document, command);
			return;
		}
		if ("]}).".contains(command.text)) { //$NON-NLS-1$
			smartIndentAfterDecreaser(document, command);
			return;
		}
		if ("\t".equals(command.text)) {
			smartIndentAfterTab(document, command);
			return;
		}
		super.customizeDocumentCommand(document, command);
	}

	/**
	 * appends text to the <code>DocumentCommand</code>
	 * 
	 * @param command the command
	 * @param append the text to append
	 * @param position the length to add to the {@link DocumentCommand#caretOffset}
	 */
	private void append(DocumentCommand command, String append, int position) {
		command.text = command.text + append;
		command.doit = false;
		command.shiftsCaret = false;
		command.caretOffset = command.offset + position;
	}

	/**
	 * Returns whether or not the given text ends with one of the documents legal line delimiters.
	 * 
	 * @param d
	 *            the document
	 * @param txt
	 *            the text
	 * @return <code>true</code> if <code>txt</code> ends with one of the document's line delimiters, <code>false</code> otherwise
	 */
	private boolean endsWithDelimiter(IDocument d, String txt) {
		String[] delimiters = d.getLegalLineDelimiters();
		if (delimiters != null)
			return TextUtilities.endsWith(delimiters, txt) > -1;
		return false;
	}

	/**
	 * finds the start of the command by searching backwards for the end of a command or
	 * the start of the document
	 * 
	 * @param document the document to search in
	 * @param position the current position
	 * @return the position of the start of the command
	 * @throws BadLocationException
	 */
	protected int findStartOfCommand(IDocument document, int position) throws BadLocationException {

		int length = document.getLength();
		char ch;
		if (position == length)
			--position;

		// searching backwards to find a DOT
		while (position > 0) {
			ch = document.getChar(position);
			if (ch == FloraKeywords.DOT.charAt(0)) {
				if ((position+1 >= length)||(Character.isWhitespace(document.getChar(position + 1)))) {
					if (document.getContentType(position).equals(IDocument.DEFAULT_CONTENT_TYPE))
						return position;
				}
			}
			--position;
		}

		return 0;
	}


	/**
	 * returns the position of the next non-whitespace character
	 * 
	 * @param document to search in
	 * @param position the current position
	 * @return the position of the next non-whitespace character
	 * @throws BadLocationException
	 */
	private int getWhitespaceEnd(IDocument document, int position) throws BadLocationException {
		int currentPosition = position;
		char currentCharacter = (char) -1;
		do {
			if (currentPosition < document.getLength())
				currentCharacter = document.getChar(currentPosition);
			else
				return document.getLength();

			currentPosition++;
			if (currentCharacter == '\\') {
				// ignore escaped characters
				currentPosition++;
			}
		} while (FloraDocumentFormatter.isIndentChar(currentCharacter));
		return currentPosition - 1;
	}

	/**
	 * returns the position of the next non-whitespace character
	 * 
	 * @param document to search in
	 * @param position the current position
	 * @return the position of the next non-whitespace character
	 * @throws BadLocationException
	 */
	private int getWhitespaceStart(IDocument document, int currentPosition) throws BadLocationException {
		char currentCharacter = (char) -1;
		
		if (currentPosition == document.getLength())
			--currentPosition;
		do {
			if (currentPosition > 0)
				currentCharacter = document.getChar(currentPosition);
			else
				return 0;

			--currentPosition;
		} while (FloraDocumentFormatter.isIndentChar(currentCharacter));
		return currentPosition + 2;
	}

	/**
	 * Set the indent of a new line based on the command provided in the supplied document.
	 * 
	 * @param document -
	 *            the document being parsed
	 * @param command -
	 *            the command being performed
	 */
	protected void smartIndentAfterNewLine(IDocument document, DocumentCommand command) {
		int end = command.offset;
		
		try {
			end = getWhitespaceStart(document, command.offset - 1);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		
		command.text += processIndentation(document, end);
	}

	/**
	 * Set the indent of a line based on the command provided in the supplied document.
	 * 
	 * @param document -
	 *            the document being parsed
	 * @param command -
	 *            the command being performed
	 */
	protected void smartIndentAfterTab(IDocument document, DocumentCommand command) {

		int docLength = document.getLength();
		if (command.offset == -1 || docLength == 0)
			return;

		try {
			int line = document.getLineOfOffset(command.offset);
			int lineOffset = document.getLineOffset(line);
			int end = getWhitespaceEnd(document, lineOffset);
			if (end == document.getLength())
				end -= 1;

			// change indentation
			String oldIndentation = document.get(lineOffset, getWhitespaceEnd(document, lineOffset) - lineOffset);
			String newIndentation = processIndentation(document, end);

			if (!oldIndentation.equals(newIndentation)) {
				document.replace(lineOffset, getWhitespaceEnd(document, lineOffset) - lineOffset, newIndentation);
				command.offset += newIndentation.length() - oldIndentation.length();
			}
			command.text = "";

		} catch (BadLocationException excp) {
			excp.printStackTrace();
		}
	}
	
	/**
	 * Set the indent of a bracket based on the command provided in the supplied document.
	 * 
	 * @param document -
	 *            the document being parsed
	 * @param command -
	 *            the command being performed
	 */
	protected void smartIndentAfterDecreaser(IDocument document, DocumentCommand command) {
		if (command.offset == -1 || document.getLength() == 0)
			return;

		try {
			int end = (command.offset == document.getLength() ? command.offset - 1 : command.offset);
			int line = document.getLineOfOffset(end);
			int lineOffset = document.getLineOffset(line);
			int start = document.getLineOffset(line);
			int whiteend = findEndOfWhiteSpace(document, start, command.offset);

			// shift only when line does not contain any text up to the closing bracket
			if (whiteend == command.offset) {


				// just change indentation (if needed)
				String oldIndentation = document.get(lineOffset, getWhitespaceEnd(document, lineOffset) - lineOffset);
				String newIndentation = processIndentation(document, end);

				if (!oldIndentation.equals(newIndentation)) {
					document.replace(lineOffset, getWhitespaceEnd(document, lineOffset) - lineOffset, newIndentation);
					command.offset += newIndentation.length() - oldIndentation.length();
				}
			}
		} catch (BadLocationException excp) {
			excp.printStackTrace();
		}
	}
	
	
	protected String processIndentation(IDocument document, int end) {
		String newIndentation = "";
		if (end < 0)
			return "";

		try {
			int start = findStartOfCommand(document, end);

			String text;
			text = document.get(start, end - start);

			FloraLexer lexer = new FloraLexer(new StringReader(text));
			FloraSourceText lexText = null;
			try {
				lexText = lexer.lex();

			} catch (IOException e) {
				lexText = new FloraSourceText();
			}

			String nextChar = null;
			try {
				nextChar = Character.toString(document.getChar(end));
			} catch (BadLocationException e)
			{
				// do nothing
			}
			
			newIndentation = lexText.getIndentation(nextChar);

		} catch (BadLocationException excp) {
			excp.printStackTrace();
		}
		
		return newIndentation;
	}
}
