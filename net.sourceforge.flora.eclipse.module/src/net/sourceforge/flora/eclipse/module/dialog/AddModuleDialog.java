/* File:      AddModuleDialog.java
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

package net.sourceforge.flora.eclipse.module.dialog;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * A Dialog to add Modules to Flora-2
 * 
 * @author Daniel Winkler
 */
public class AddModuleDialog extends Dialog {
	private String moduleName;

	/**
	 * the Constructor
	 * 
	 * @param parentShell the parent shell, or <code>null</code> to create a top-level shell
	 * @see org.eclipse.jface.dialogs.Dialog
	 */
	public AddModuleDialog(Shell parentShell) {
		super(parentShell);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createDialogArea(Composite parent)
	{
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
 		description.setText("Please enter the name of the new module");
 		
 		Label nameLabel = new Label(area, SWT.NONE);
		gridData = new GridData();
		gridData.horizontalSpan = 1;
 		gridData.horizontalAlignment = GridData.BEGINNING;
 		nameLabel.setLayoutData(gridData);
 		nameLabel.setText("Name: ");
 		
 		final Text nameText = new Text(area, SWT.SINGLE | SWT.BORDER);
		gridData = new GridData();
		gridData.horizontalSpan = 1;
 		gridData.horizontalAlignment = GridData.FILL;
 		gridData.grabExcessHorizontalSpace = true;
 		nameText.setLayoutData(gridData);
 		nameText.addModifyListener(new ModifyListener(){

			public void modifyText(ModifyEvent e) {
				moduleName = nameText.getText();
			}
 			
 		});
		
		return area;
	}

	/**
	 * returns the specified name of the new Module
	 * 
	 * @return the moduleName
	 */
	public String getModuleName() {
		return moduleName;
	}
}
