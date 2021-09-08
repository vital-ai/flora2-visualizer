/* File:      FloraVisualizer.java
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

package net.sourceforge.flora.eclipse.visualizer;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import net.sourceforge.flora.eclipse.reasoner.FloraReasoner;
import net.sourceforge.flora.eclipse.reasoner.object.FloraModel;
import net.sourceforge.flora.eclipse.reasoner.object.FloraModule;
import net.sourceforge.flora.eclipse.visualizer.dialog.RegisterDialog;
import net.sourceforge.flora.eclipse.visualizer.dialog.UnregisterDialog;
import net.sourceforge.flora.eclipse.visualizer.graph.FloraGraph;
import net.sourceforge.flora.eclipse.visualizer.graph.FloraGraphPane;
import net.sourceforge.flora.eclipse.visualizer.graph.factory.GraphFactory;
import net.sourceforge.jpowergraph.lens.LensSet;
import net.sourceforge.jpowergraph.manipulator.selection.DefaultNodeSelectionModel;
import net.sourceforge.jpowergraph.manipulator.selection.NodeSelectionModel;
import net.sourceforge.jpowergraph.pane.JGraphPane;
import net.sourceforge.jpowergraph.swt.SWTJGraphScrollPane;
import net.sourceforge.jpowergraph.swt.SWTJGraphViewPane;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

/**
 * A editor which displays the currently registered Flora-2 Objects of a module as graph
 * 
 * @author Daniel Winkler
 */
public class FloraVisualizer extends EditorPart implements Observer {

    private FloraGraphPane fPane = null;

    private FloraGraph fGraph = null;

	private FloraModule fModule;

    /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.ui.part.EditorPart#doSave(org.eclipse.core.runtime.IProgressMonitor)
         */
    public void doSave(IProgressMonitor monitor) {
    }

    /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.ui.part.EditorPart#doSaveAs()
         */
    public void doSaveAs() {
    }

    /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.ui.part.EditorPart#init(org.eclipse.ui.IEditorSite, org.eclipse.ui.IEditorInput)
         */
    public void init(IEditorSite site, IEditorInput input) throws PartInitException {
	setSite(site);
	setInput(input);
    }

    /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.ui.part.EditorPart#isDirty()
         */
    public boolean isDirty() {
	return false;
    }

    /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.ui.part.EditorPart#isSaveAsAllowed()
         */
    public boolean isSaveAsAllowed() {
	return false;
    }

    /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
         */
    public void createPartControl(Composite parent) {
	if ((getEditorInput() != null) && (getEditorInput() instanceof FloraModule)) {
		setModule((FloraModule) getEditorInput());
		fGraph = GraphFactory.getGraph(fModule);
	} else {
	    this.dispose();
	    if (getEditorInput() == null)
		throw new NullPointerException("Editor input must not be null");
	    else if (!(getEditorInput() instanceof FloraModule))
		throw new IllegalArgumentException("Editor input must be of type net.sourceforge.flora.eclipse.reasoner.object.FloraModule");
	}
	
	FloraModel.getInstance().addObserver(this);

	setPartName("Visualizer: " + getModule().toString());

	FillLayout layout = new FillLayout();
	layout.type = SWT.VERTICAL;
	parent.setLayout(layout);

	fPane = new FloraGraphPane(parent, fGraph, getNodeSelectionModel());
	fPane.addKeyListener(new KeyAdapter() {
	    public void keyReleased(KeyEvent e) {
		if (e.keyCode == SWT.F5) {
		    refresh();
		}
	    }
	});

	SWTJGraphScrollPane scroll = new SWTJGraphScrollPane(parent, fPane, (LensSet) fPane.getLens());
	scroll.setParent(parent);

	SWTJGraphViewPane view = new SWTJGraphViewPane(parent, scroll, (LensSet) fPane.getLens());

	view.getEndContributeComposite().setLayoutData(new GridData());
	Composite c = new Composite(view.getEndContributeComposite(), SWT.NONE);
	GridLayout cgl = new GridLayout(5, false);
	cgl.marginWidth = 0;
	cgl.marginHeight = 0;
	cgl.marginLeft = 5;
	c.setLayout(cgl);

	Button reloadButton = new Button(c, SWT.PUSH);
	reloadButton.setImage(new Image(parent.getDisplay(), FloraVisualizer.class.getResourceAsStream("refresh.gif")));
	reloadButton.setToolTipText("Redraw the Visualizer");
	reloadButton.addSelectionListener(new SelectionAdapter() {
	    public void widgetDefaultSelected(SelectionEvent arg0) {
		refresh();
	    }

	    public void widgetSelected(SelectionEvent arg0) {
		refresh();
	    }
	});
	reloadButton.setEnabled(true);

	Button clusterAllButton = new Button(c, SWT.PUSH);
	clusterAllButton.setImage(new Image(parent.getDisplay(), FloraVisualizer.class.getResourceAsStream("clusterAll.gif")));
	clusterAllButton.setToolTipText("Cluster all Classes of the Visualizer");
	clusterAllButton.addSelectionListener(new SelectionAdapter() {
	    public void widgetDefaultSelected(SelectionEvent arg0) {
		doStuff();
	    }

	    public void widgetSelected(SelectionEvent arg0) {
		doStuff();
	    }
	    
	    public void doStuff(){
	    	while (fGraph.getUnclusterNodes().size() > 0)
	    		fGraph.clusterNode(fGraph.getUnclusterNodes().get(0));
	    	refresh();
	    }
	});
	clusterAllButton.setEnabled(true);

	Button clusterDefaultButton = new Button(c, SWT.PUSH);
	clusterDefaultButton.setImage(new Image(parent.getDisplay(), FloraVisualizer.class.getResourceAsStream("clusterDefault.gif")));
	clusterDefaultButton.setToolTipText("Cluster all Classes of the Visualizer when exceeding Default Size");
	clusterDefaultButton.addSelectionListener(new SelectionAdapter() {
	    public void widgetDefaultSelected(SelectionEvent arg0) {
		doStuff();
	    }

	    public void widgetSelected(SelectionEvent arg0) {
		doStuff();
	    }
	    
	    public void doStuff(){
	    	fGraph.getClusterNodes().clear();
	    	fGraph.getUnclusterNodes().clear();
	    	refresh();
	    }
	});
	clusterDefaultButton.setEnabled(true);

	Button registerButton = new Button(c, SWT.PUSH);
	registerButton.setImage(new Image(parent.getDisplay(), FloraVisualizer.class.getResourceAsStream("register.png")));
	registerButton.setToolTipText("Open a Pop-Up to register new Objects for the Visualizer");
	registerButton.addSelectionListener(new SelectionAdapter() {
	    public void widgetDefaultSelected(SelectionEvent arg0) {
		doStuff();
	    }

	    public void widgetSelected(SelectionEvent arg0) {
		doStuff();
	    }
	    
	    public void doStuff(){
		RegisterDialog addDialog = new RegisterDialog(Display.getCurrent().getActiveShell());
		if (addDialog.open() == Dialog.OK) {
		    FloraReasoner.getInstance().register(fGraph.getModule().toString(), addDialog.getList());
		    refresh();
		}
	    }
	});
	registerButton.setEnabled(true);

	Button unRegisterButton = new Button(c, SWT.PUSH);
	unRegisterButton.setImage(new Image(parent.getDisplay(), FloraVisualizer.class.getResourceAsStream("unregister.png")));
	unRegisterButton.setToolTipText("Open a Pop-Up to unregister Objects from the Visualizer");
	unRegisterButton.addSelectionListener(new SelectionAdapter() {
	    public void widgetDefaultSelected(SelectionEvent arg0) {
		doStuff();
	    }

	    public void widgetSelected(SelectionEvent arg0) {
		doStuff();
	    }

	    private void doStuff() {
		FloraModule module = fGraph.getModule();
		UnregisterDialog unregisterDialog = new UnregisterDialog(Display.getCurrent().getActiveShell(), module);
		if (unregisterDialog.open() == Dialog.OK) {
			boolean refresh = false;
			
		    ArrayList<String> list = unregisterDialog.getManRegList();
		    if (list.size() > 0) {
			FloraReasoner.getInstance().unregisterManually(module.toString(), list);
			refresh = true;
			}
		    
			list = unregisterDialog.getProgRegList();
			if (list.size() > 0) {
			FloraReasoner.getInstance().unregister(module.toString(), list);
			refresh = true;
		    }
			if (refresh)
				refresh();
		}
	    }
	});
	unRegisterButton.setEnabled(true);

	view.setParent(parent);
    }


    public void dispose() {
    	super.dispose();
    	FloraModel.getInstance().deleteObserver(this);
    }
    
    /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
         */
    public void setFocus() {
	if (fPane != null)
	    fPane.setFocus();
    }

    /**
         * returns the {@link NodeSelectionModel}
         * 
         * @return the <code>NodeSelectionModel</code>
         */
    private NodeSelectionModel getNodeSelectionModel() {
	NodeSelectionModel nodeSelectionModel = new DefaultNodeSelectionModel(fGraph);

	return nodeSelectionModel;
    }

    /**
         * returns the current {@link FloraGraph}
         * 
         * @return the current <code>FloraGraph</code>
         */
    public FloraGraph getGraph() {
	return fGraph;
    }

    /**
         * returns the current {@link FloraGraphPane}
         * 
         * @return the current <code>FloraGraphPane</code>
         */
    public JGraphPane getPane() {
	return fPane;
    }

    /**
         * refreshs the Graph and redraws it
         * 
         * @see GraphFactory#updateGraph(FloraGraph)
         */
    public void refresh() {
    	Cursor cursor = new Cursor(Display.getCurrent(), SWT.CURSOR_WAIT);
		Display.getCurrent().getActiveShell().setCursor(cursor);

		GraphFactory.updateGraph(fGraph);
		fPane.redraw();
		
		Display.getCurrent().getActiveShell().setCursor(null);
    }

	private FloraModule getModule() {
		return fModule;
	}

	private void setModule(FloraModule module) {
		this.fModule = module;
	}

	public void update(Observable observable, Object object) {
		boolean refresh = false;
		
		if (getSite().getPage().getActiveEditor() == this)
			refresh = true;
		if (observable != null && observable instanceof FloraModel) {
			if (object != null && fGraph.getModule().toString().equals(object))
				refresh = true;
			if (refresh)
				refresh();
		}
	}
}
