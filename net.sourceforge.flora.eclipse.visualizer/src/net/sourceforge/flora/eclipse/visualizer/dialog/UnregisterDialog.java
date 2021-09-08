/* File:      UnregisterDialog.java
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

package net.sourceforge.flora.eclipse.visualizer.dialog;

import java.util.ArrayList;

import net.sourceforge.flora.eclipse.reasoner.FloraReasoner;
import net.sourceforge.flora.eclipse.reasoner.object.FloraModule;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;

/**
 * A Dialog to unregister Objects of a Module
 * 
 * @author Daniel Winkler
 */
public class UnregisterDialog extends Dialog {
    private FloraModule fModule;

    private Object[] fElementsMan;
    private Object[] fElementsProg;

    private CheckboxTableViewer tableViewerMan;
    private CheckboxTableViewer tableViewerProg;

    /**
         * the Constructor
         * 
         * @param parentShell
         *                the parent shell, or <code>null</code> to create a top-level shell
         * @param module
         *                the <code>FloraModule</code> objects to unregistered
         * @see org.eclipse.jface.dialogs.Dialog
         */
    public UnregisterDialog(Shell parentShell, FloraModule module) {
	super(parentShell);
	fModule = module;
    }

//	/**
//	 * trims the window to a maximal size of 800x600
//	 * @see org.eclipse.jface.dialogs.Dialog#getInitialSize()
//	 */
//	protected Point getInitialSize() {
//		Point point = super.getInitialSize();
//		point.x = ((point.x > 800) ? 800 : point.x); 
//		point.y = ((point.y > 600) ? 800 : point.y);
//		return point;
//	}

    /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
         */
    protected Control createDialogArea(Composite parent) {
	Composite area = (Composite) super.createDialogArea(parent);

	GridLayout layout = new GridLayout();
	layout.numColumns = 2;
	area.setLayout(layout);

	Label description = new Label(area, SWT.NONE);
	GridData gridData = new GridData();
	gridData.horizontalSpan = 2;
	gridData.horizontalAlignment = GridData.FILL;
	gridData.grabExcessHorizontalSpace = true;
	description.setLayoutData(gridData);
	description.setText("Please choose objects to unregister");

	Label descriptionMan = new Label(area, SWT.NONE);
	gridData = new GridData();
	gridData.horizontalSpan = 2;
	gridData.horizontalAlignment = GridData.FILL;
	gridData.grabExcessHorizontalSpace = true;
	descriptionMan.setLayoutData(gridData);
	descriptionMan.setText("Manually added objects");

	Table tableMan = new Table(area, SWT.CHECK | SWT.BORDER);
	tableViewerMan = new CheckboxTableViewer(tableMan);
	gridData = new GridData();
	gridData.horizontalSpan = 1;
	gridData.horizontalAlignment = GridData.FILL;
	gridData.widthHint = 200;
	gridData.heightHint = 200;
	tableViewerMan.getControl().setLayoutData(gridData);
	tableViewerMan.setContentProvider(new ManTableViewerContentProvider());
	tableViewerMan.setLabelProvider(new TableViewerLabelProvider());
	tableViewerMan.setInput(fModule);

	tableViewerMan.addSelectionChangedListener(new ISelectionChangedListener() {

	    public void selectionChanged(SelectionChangedEvent event) {
		fElementsMan = tableViewerMan.getCheckedElements();
	    }

	});

	Composite buttonCompositeMan = new Composite(area, SWT.NONE);
	gridData = new GridData();
	gridData.horizontalSpan = 1;
	gridData.horizontalAlignment = GridData.FILL;
	gridData.verticalAlignment = GridData.BEGINNING;
	buttonCompositeMan.setLayoutData(gridData);
	layout = new GridLayout();
	layout.numColumns = 1;
	buttonCompositeMan.setLayout(layout);
	
	Button selectAllButtonMan = new Button(buttonCompositeMan, SWT.PUSH);
	selectAllButtonMan.setText("Select All");
	gridData = new GridData();
	gridData.horizontalSpan = 1;
	gridData.horizontalAlignment = GridData.FILL;
	gridData.verticalAlignment = GridData.BEGINNING;
	selectAllButtonMan.setLayoutData(gridData);
	selectAllButtonMan.addSelectionListener(new SelectionListener(){

	    public void widgetDefaultSelected(SelectionEvent e) {
		doStuff();
	    }

	    public void widgetSelected(SelectionEvent e) {
		doStuff();
	    }
	    
	    public void doStuff() {
		tableViewerMan.setAllChecked(true);
	    }
	});
	
	Button unselectAllButtonMan = new Button(buttonCompositeMan, SWT.PUSH);
	unselectAllButtonMan.setText("Unselect All");
	gridData = new GridData();
	gridData.horizontalSpan = 1;
	gridData.horizontalAlignment = GridData.FILL;
	gridData.verticalAlignment = GridData.BEGINNING;
	unselectAllButtonMan.setLayoutData(gridData);
	unselectAllButtonMan.addSelectionListener(new SelectionListener(){

	    public void widgetDefaultSelected(SelectionEvent e) {
		doStuff();
	    }

	    public void widgetSelected(SelectionEvent e) {
		doStuff();
	    }
	    
	    public void doStuff() {
		tableViewerMan.setAllChecked(false);
	    }
	});
	

	Label descriptionProg = new Label(area, SWT.NONE);
	gridData = new GridData();
	gridData.horizontalSpan = 2;
	gridData.horizontalAlignment = GridData.FILL;
	gridData.grabExcessHorizontalSpace = true;
	descriptionProg.setLayoutData(gridData);
	descriptionProg.setText("Programmatically added objects");

	Table tableProg = new Table(area, SWT.CHECK | SWT.BORDER);
	tableViewerProg = new CheckboxTableViewer(tableProg);
	gridData = new GridData();
	gridData.horizontalSpan = 1;
	gridData.horizontalAlignment = GridData.FILL;
	gridData.widthHint = 200;
	gridData.heightHint = 200;
	tableViewerProg.getControl().setLayoutData(gridData);
	tableViewerProg.setContentProvider(new ProgTableViewerContentProvider());
	tableViewerProg.setLabelProvider(new TableViewerLabelProvider());
	tableViewerProg.setInput(fModule);

	tableViewerProg.addSelectionChangedListener(new ISelectionChangedListener() {

	    public void selectionChanged(SelectionChangedEvent event) {
		fElementsProg = tableViewerProg.getCheckedElements();
	    }

	});

	Composite buttonCompositeProg = new Composite(area, SWT.NONE);
	gridData = new GridData();
	gridData.horizontalSpan = 1;
	gridData.horizontalAlignment = GridData.FILL;
	gridData.verticalAlignment = GridData.BEGINNING;
	buttonCompositeProg.setLayoutData(gridData);
	layout = new GridLayout();
	layout.numColumns = 1;
	buttonCompositeProg.setLayout(layout);
	
	Button selectAllButtonProg = new Button(buttonCompositeProg, SWT.PUSH);
	selectAllButtonProg.setText("Select All");
	gridData = new GridData();
	gridData.horizontalSpan = 1;
	gridData.horizontalAlignment = GridData.FILL;
	gridData.verticalAlignment = GridData.BEGINNING;
	selectAllButtonProg.setLayoutData(gridData);
	selectAllButtonProg.addSelectionListener(new SelectionListener(){

	    public void widgetDefaultSelected(SelectionEvent e) {
		doStuff();
	    }

	    public void widgetSelected(SelectionEvent e) {
		doStuff();
	    }
	    
	    public void doStuff() {
		tableViewerProg.setAllChecked(true);
	    }
	});
	
	Button unselectAllButtonProg = new Button(buttonCompositeProg, SWT.PUSH);
	unselectAllButtonProg.setText("Unselect All");
	gridData = new GridData();
	gridData.horizontalSpan = 1;
	gridData.horizontalAlignment = GridData.FILL;
	gridData.verticalAlignment = GridData.BEGINNING;
	unselectAllButtonProg.setLayoutData(gridData);
	unselectAllButtonProg.addSelectionListener(new SelectionListener(){

	    public void widgetDefaultSelected(SelectionEvent e) {
		doStuff();
	    }

	    public void widgetSelected(SelectionEvent e) {
		doStuff();
	    }
	    
	    public void doStuff() {
		tableViewerProg.setAllChecked(false);
	    }
	});
	
	
	return area;
    }

    /**
         * Returns all checked objects as <code>ArrayList<String></code>
         * 
         * @return all checked objects as <code>ArrayList<String></code>
         */
    public ArrayList<String> getList() {
	ArrayList<String> returnList = new ArrayList<String>();

	if (fElementsMan != null)
	    for (Object element : fElementsMan)
		returnList.add(element.toString());
	if (fElementsProg != null)
	    for (Object element : fElementsProg)
		returnList.add(element.toString());

	return returnList;
    }
    
    /**
	 * Returns the manually registered and checked objects as <code>ArrayList<String></code>
	 * 
	 * @return the manually registered and checked objects as <code>ArrayList<String></code>
	 */
	public ArrayList<String> getManRegList() {
		ArrayList<String> returnList = new ArrayList<String>();

		if (fElementsMan != null)
			for (Object element : fElementsMan)
				returnList.add(element.toString());

		return returnList;
	}

	/**
	 * Returns the programmatically registered and checked objects as <code>ArrayList<String></code>
	 * 
	 * @return the programmatically registered and checked objects as <code>ArrayList<String></code>
	 */
	public ArrayList<String> getProgRegList() {
		ArrayList<String> returnList = new ArrayList<String>();

		if (fElementsProg != null)
			for (Object element : fElementsProg)
				returnList.add(element.toString());

		return returnList;
	}

    public boolean close() {
	fElementsMan = tableViewerMan.getCheckedElements();
	fElementsProg = tableViewerProg.getCheckedElements();
	return super.close();
    }

    private class ProgTableViewerContentProvider implements IStructuredContentProvider {

	public Object[] getElements(Object inputElement) {
		return FloraReasoner.getInstance().getProgrammaticallyRegisteredObjects(inputElement.toString()).toArray();
	}

	public void dispose() {
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

    }

    private class ManTableViewerContentProvider implements IStructuredContentProvider {

	public Object[] getElements(Object inputElement) {
	    return FloraReasoner.getInstance().getManuallyRegisteredObjects(inputElement.toString()).toArray();
	}

	public void dispose() {
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

    }

    private class TableViewerLabelProvider implements ITableLabelProvider {

	public Image getColumnImage(Object element, int columnIndex) {
	    return null;
	}

	public String getColumnText(Object element, int columnIndex) {
	    return element.toString();
	}

	public void addListener(ILabelProviderListener listener) {
	}

	public void dispose() {
	}

	public boolean isLabelProperty(Object element, String property) {
	    return false;
	}

	public void removeListener(ILabelProviderListener listener) {
	}

    }
}
