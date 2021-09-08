/* File:      FloraEditor.java
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

import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.editors.text.TextEditor;

/**
 * @author Daniel Winkler
 * 
 * A TextEditor for Flora Documents
 */
public class FloraEditor extends TextEditor {
	public FloraEditor() {
		super();
	}

	/**
	 * calls the super.initializeEditor() and sets the sourceViewerConfiguration
	 * 
	 * @see org.eclipse.ui.editors.text.TextEditor#initializeEditor()
	 * @see #setSourceViewerConfiguration(org.eclipse.jface.text.source.SourceViewerConfiguration)
	 * @see FloraSourceViewerConfiguration#FloraSourceViewerConfiguration()
	 */
	protected void initializeEditor() {
		super.initializeEditor();
		setSourceViewerConfiguration(new FloraSourceViewerConfiguration());
	}

	/**
	 * formats the content of the SourceViewer
	 * 
	 * @see #getSourceViewer()
	 */
	public void format() {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				((SourceViewer) getSourceViewer()).doOperation(SourceViewer.FORMAT);
			}
		});

		// IDocument document = getSourceViewer().getDocument();
		// getSourceViewerConfiguration().getContentFormatter(getSourceViewer()).format(document, new Region(0, document.getLength()));
	}

	/**
	 * returns the actual selection of the SourceViewer
	 * 
	 * @return the actual selection of the SourceViewer
	 */
	public String getTextSelection() {
		try {
			int offset = ((ITextSelection) getSourceViewer().getSelectionProvider().getSelection()).getOffset();
			;
			int length = ((ITextSelection) getSourceViewer().getSelectionProvider().getSelection()).getLength();
			;
			return getSourceViewer().getDocument().get(offset, length);
		} catch (Exception e) {
			return "";
		}
	}
}
