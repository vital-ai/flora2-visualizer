/* File:      RegisterDialog.java
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

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * A Dialog to register Objects of a Module
 * 
 * @author Daniel Winkler
 */
public class RegisterDialog extends Dialog {
    private String list;

    /**
     * the Constructor
     * 
     * @param parentShell
     *                the parent shell, or <code>null</code> to create a top-level shell
     * @see org.eclipse.jface.dialogs.Dialog
     */
    public RegisterDialog(Shell parentShell) {
	super(parentShell);
    }

	/**
	 * trims the window to a maximal size of 800x600
	 * @see org.eclipse.jface.dialogs.Dialog#getInitialSize()
	 */
	protected Point getInitialSize() {
		Point point = super.getInitialSize();
		point.x = ((point.x > 800) ? 800 : point.x); 
		point.y = ((point.y > 600) ? 800 : point.y);
		return point;
	}

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
	description.setText("Please enter a comma, space, or TAB-separated list of objects to register");

	Label nameLabel = new Label(area, SWT.NONE);
	gridData = new GridData();
	gridData.horizontalSpan = 1;
	gridData.horizontalAlignment = GridData.BEGINNING;
	nameLabel.setLayoutData(gridData);
	nameLabel.setText("Objects: ");

	final Text nameText = new Text(area, SWT.SINGLE | SWT.BORDER);
	gridData = new GridData();
	gridData.horizontalSpan = 1;
	gridData.horizontalAlignment = GridData.FILL;
	gridData.grabExcessHorizontalSpace = true;
	nameText.setLayoutData(gridData);
	nameText.addModifyListener(new ModifyListener() {

	    public void modifyText(ModifyEvent e) {
		list = nameText.getText();
	    }

	});

	return area;
    }

    /**
     * Returns the String of the Text-Field splitted as <code>ArrayList<String></code>
     * 
     * @return the String of the Text-Field splitted as <code>ArrayList<String></code>
     */
    public ArrayList<String> getList() {
	ArrayList<String> returnList = new ArrayList<String>();
	String tempString = list.replaceAll("[[\\s]*,*]++", ",");
	String[] splitList = tempString.split(",");
	for (String element : splitList){
	    if (element.trim().length() > 0)
		returnList.add(element.trim());
	}
	return returnList;
    }
}
