/* File:      FloraPerspective.java
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

package net.sourceforge.flora.eclipse.perspective;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.PlatformUI;

/**
 * Class FloraPerspecive creates a perspective which contributes 3 views to the
 * Layout.
 * <p>
 * Top-Left: Resource Navigator<p>
 * Bottom-Left: Flora-Module View<p>
 * Bottom: Flora-Console View<p>
 * 
 * @author Daniel Winkler
 * @see IPerspectiveFactory
 * @see IPageLayout
 */
public class FloraPerspective implements IPerspectiveFactory {

    /* (non-Javadoc)
     * @see org.eclipse.ui.IPerspectiveFactory#createInitialLayout(org.eclipse.ui.IPageLayout)
     */
    public void createInitialLayout(IPageLayout layout) {
	String editorArea = layout.getEditorArea();
	IFolderLayout naviFolder = layout.createFolder("naviFolder", IPageLayout.LEFT, 0.25f, editorArea);
	naviFolder.addView(IPageLayout.ID_RES_NAV);

	if (PlatformUI.getWorkbench().getViewRegistry().find("net.sourceforge.flora.eclipse.module.moduleView") != null) {
	    IFolderLayout moduleFolder = layout.createFolder("moduleFolder", IPageLayout.BOTTOM, 0.5f, "naviFolder");
	    moduleFolder.addView("net.sourceforge.flora.eclipse.module.moduleView");
	}

	if (PlatformUI.getWorkbench().getViewRegistry().find("net.sourceforge.flora.eclipse.console.consoleView") != null) {
	    IFolderLayout bottomFolder = layout.createFolder("bottomFolder", IPageLayout.BOTTOM, 0.75f, editorArea);
	    bottomFolder.addView("net.sourceforge.flora.eclipse.console.consoleView");
	}

    }
}
