/* File:      FloraFormattingStrategy.java
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

import java.util.LinkedList;
import java.util.Map;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextUtilities;
import org.eclipse.jface.text.TypedPosition;
import org.eclipse.jface.text.formatter.ContextBasedFormattingStrategy;
import org.eclipse.jface.text.formatter.FormattingContextProperties;
import org.eclipse.jface.text.formatter.IFormattingContext;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;

/**
 * @author Daniel Winkler
 *
 * the formatting strategy for the Flora-2 formatter
 */
public class FloraFormattingStrategy extends ContextBasedFormattingStrategy {
    private final LinkedList<IDocument> fDocuments = new LinkedList<IDocument>();
    private final LinkedList<TypedPosition> fPartitions = new LinkedList<TypedPosition>();

    /**
     * the constructor</br>
     * @see ContextBasedFormattingStrategy
     */
    public FloraFormattingStrategy() {
	super();
    }

    /**
     * Formats the text of the current document
     * 
     * @see FloraDocumentFormatter#format(String, int, int)
     * @see org.eclipse.jface.text.formatter.ContextBasedFormattingStrategy#format()
     */
    public void format() {
	super.format();

	final IDocument document = (IDocument) fDocuments.removeFirst();
	final TypedPosition partition = (TypedPosition) fPartitions.removeFirst();

	if (document != null && partition != null) {
	    try {

		FloraDocumentFormatter sourceFormatter = new FloraDocumentFormatter(document);
		
		final TextEdit edit = sourceFormatter.format(document.get(), partition.getOffset(), partition.getLength()); 
		if (edit != null) {
		    Map partitioners = null;
		    if (edit.getChildrenSize() > 20)
			partitioners = TextUtilities.removeDocumentPartitioners(document);

		    edit.apply(document);

		    if (partitioners != null)
			TextUtilities.addDocumentPartitioners(document, partitioners);
		}

	    } catch (MalformedTreeException exception) {
		exception.printStackTrace();
	    } catch (BadLocationException exception) {
		exception.printStackTrace();
	    }
	}
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.text.formatter.ContextBasedFormattingStrategy#formatterStarts(org.eclipse.jface.text.formatter.IFormattingContext)
     */
    public void formatterStarts(final IFormattingContext context) {
	super.formatterStarts(context);
	fPartitions.addLast((TypedPosition)context.getProperty(FormattingContextProperties.CONTEXT_PARTITION));
	fDocuments.addLast((IDocument)context.getProperty(FormattingContextProperties.CONTEXT_MEDIUM));
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.text.formatter.ContextBasedFormattingStrategy#formatterStops()
     */
    public void formatterStops() {
	super.formatterStops();

	fPartitions.clear();
	fDocuments.clear();
    }

}
