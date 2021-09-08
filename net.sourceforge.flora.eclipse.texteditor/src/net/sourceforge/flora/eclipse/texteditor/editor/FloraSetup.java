/* File:      FloraSetup.java
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

import net.sourceforge.flora.eclipse.texteditor.FloraSourceEditorPlugin;
import net.sourceforge.flora.eclipse.texteditor.editor.scanners.FloraPartitionScanner;

import org.eclipse.core.filebuffers.IDocumentSetupParticipant;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;

/**
 * @author Daniel Winkler
 * 
 * Sets up the Document partitioning
 */
public class FloraSetup implements IDocumentSetupParticipant {

    /**
     * Sets up the Document partitioning
     * 
     * @param document the <code>Document</code> to set up
     * 
     * @see org.eclipse.core.filebuffers.IDocumentSetupParticipant#setup(org.eclipse.jface.text.IDocument)
     */
    public void setup(IDocument document) {
	IDocumentPartitioner partitioner = new FastPartitioner(FloraSourceEditorPlugin.getDefault().getFloraPartitionScanner(), FloraPartitionScanner
		.getScannableScannerIDs());

	 document.setDocumentPartitioner(partitioner);
//	if (document instanceof IDocumentExtension3) {
//	    IDocumentExtension3 extension3 = (IDocumentExtension3) document;
//	    extension3.setDocumentPartitioner(FloraPartitionScanner.FLORA_PARTITIONING, partitioner);
//	} else {
//	    document.setDocumentPartitioner(partitioner);
//	}

	partitioner.connect(document);
    }

}
