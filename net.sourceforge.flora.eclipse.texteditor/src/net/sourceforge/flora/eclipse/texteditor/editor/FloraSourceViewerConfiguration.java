/* File:      FloraSourceViewerConfiguration.java
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

import java.util.ArrayList;

import net.sourceforge.flora.eclipse.preferences.FloraPreferenceConstants;
import net.sourceforge.flora.eclipse.texteditor.FloraSourceEditorPlugin;
import net.sourceforge.flora.eclipse.texteditor.editor.scanners.FloraCodeScanner;
import net.sourceforge.flora.eclipse.texteditor.editor.scanners.FloraCommentScanner;
import net.sourceforge.flora.eclipse.texteditor.editor.scanners.FloraPartitionScanner;
import net.sourceforge.flora.eclipse.texteditor.format.FloraAutoIndentStrategy;
import net.sourceforge.flora.eclipse.texteditor.format.FloraFormattingStrategy;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.jface.text.IAutoEditStrategy;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.formatter.IContentFormatter;
import org.eclipse.jface.text.formatter.MultiPassContentFormatter;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;

/**
 * @author Daniel Winkler
 * 
 * the SourceViewerConfiguration for the Flora-2 Editor
 * 
 * @see SourceViewerConfiguration
 */
public class FloraSourceViewerConfiguration extends SourceViewerConfiguration {

    /**
     * empty constructor
     */
    public FloraSourceViewerConfiguration() {
    }

    /**
     * Returns the presentation reconciler ready to be used with the given source viewer.
	 *
	 * @param sourceViewer the source viewer
	 * @return the presentation reconciler
     * 
     * @see org.eclipse.jface.text.source.SourceViewerConfiguration#getPresentationReconciler(org.eclipse.jface.text.source.ISourceViewer)
     */
    public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {

	PresentationReconciler reconciler = new PresentationReconciler();

	DefaultDamagerRepairer dr = new DefaultDamagerRepairer(new FloraCodeScanner());
	reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
	reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);

	dr = new DefaultDamagerRepairer(new FloraCommentScanner());
	reconciler.setDamager(dr, FloraPartitionScanner.FLORA_SINGLE_LINE_COMMENT);
	reconciler.setRepairer(dr, FloraPartitionScanner.FLORA_SINGLE_LINE_COMMENT);

	reconciler.setDamager(dr, FloraPartitionScanner.FLORA_MULTI_LINE_COMMENT);
	reconciler.setRepairer(dr, FloraPartitionScanner.FLORA_MULTI_LINE_COMMENT);

	return reconciler;
    }

    /**
     * Returns the visual width of the tab character. The value is looked up in the
     * {@link PreferenceStore}
	 *
	 * @param sourceViewer the source viewer to be configured by this configuration
	 * @return the tab width
     * 
     * @see org.eclipse.jface.text.source.SourceViewerConfiguration#getTabWidth(org.eclipse.jface.text.source.ISourceViewer)
     */
    public int getTabWidth(ISourceViewer sourceViewer) {
	IPreferenceStore store = FloraSourceEditorPlugin.getDefault().getPreferenceStore();

	return store.getInt(FloraPreferenceConstants.EDITOR_INDENTATION_SIZE);
    }

    /**
	 * Returns the content formatter ready to be used with the given source viewer.
	 *
	 * @param sourceViewer the source viewer to be configured by this configuration
	 * @return a content formatter
     * 
     * @see org.eclipse.jface.text.source.SourceViewerConfiguration#getContentFormatter(org.eclipse.jface.text.source.ISourceViewer)
     */
    public IContentFormatter getContentFormatter(ISourceViewer sourceViewer) {
	MultiPassContentFormatter contentFormatter = new MultiPassContentFormatter(getConfiguredDocumentPartitioning(sourceViewer),
		IDocument.DEFAULT_CONTENT_TYPE);

	contentFormatter.setMasterStrategy(new FloraFormattingStrategy());
	// contentFormatter.setSlaveStrategy(new CommentFormattingStrategy, FloraPartitionScanner.FLORA_SINGLE_LINE_COMMENT);

	return contentFormatter;
    }

    /**
	 * Returns the auto edit strategies ready to be used with the given source viewer
	 * when manipulating text of the given content type.
	 *
	 * @param sourceViewer the source viewer to be configured by this configuration
	 * @param contentType the content type for which the strategies are applicable
	 * @return the auto edit strategies
     * 
     * @see FloraAutoIndentStrategy
     * @see org.eclipse.jface.text.source.SourceViewerConfiguration#getAutoEditStrategies(org.eclipse.jface.text.source.ISourceViewer, java.lang.String)
     */
    public IAutoEditStrategy[] getAutoEditStrategies(ISourceViewer sourceViewer, String contentType) {
	// if (contentType.equals(IDocument.DEFAULT_CONTENT_TYPE))
	return new IAutoEditStrategy[] { new FloraAutoIndentStrategy() };
	// return new IAutoEditStrategy[] { new DefaultIndentLineAutoEditStrategy() };
    }

    /**
	 * Returns the double-click strategy ready to be used in this viewer when double clicking
	 * onto text of the given content type. This implementation always returns a new instance of
	 * <code>FloraDoubleClickSelector</code>.
	 *
	 * @param sourceViewer the source viewer to be configured by this configuration
	 * @param contentType the content type for which the strategy is applicable
	 * @return a double-click strategy
     * 
     * @see FloraDoubleClickSelector
     * @see org.eclipse.jface.text.source.SourceViewerConfiguration#getDoubleClickStrategy(org.eclipse.jface.text.source.ISourceViewer, java.lang.String)
     */
    public ITextDoubleClickStrategy getDoubleClickStrategy(ISourceViewer sourceViewer, String contentType) {
	return new FloraDoubleClickSelector();
    }
    
    
    /**
     * returns all content types
     * 
     * @return all content types
     * 
     * @see 
     * @see org.eclipse.jface.text.source.SourceViewerConfiguration#getConfiguredContentTypes(org.eclipse.jface.text.source.ISourceViewer)
     */
    public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
    	ArrayList<String> contentTypes = new ArrayList<String>();
    	
    	for (String contentType : super.getConfiguredContentTypes(sourceViewer))
    		contentTypes.add(contentType);
    	
    	contentTypes.add(FloraPartitionScanner.FLORA_SINGLE_LINE_COMMENT);
    	contentTypes.add(FloraPartitionScanner.FLORA_MULTI_LINE_COMMENT);
    	
    	return contentTypes.toArray(new String[contentTypes.size()]);
    }
}
