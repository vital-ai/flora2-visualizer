/* File:      ModelContentProvider.java
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

package net.sourceforge.flora.eclipse.module.provider;

import net.sourceforge.flora.eclipse.reasoner.object.FloraModel;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * The Content Provider for the TableViewer in the Flora-2 Module View
 * 
 * @author Daniel Winkler
 */
public class ModelContentProvider implements IStructuredContentProvider {

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
     */
    public Object[] getElements(Object inputElement) {
	if (inputElement instanceof FloraModel) {
	    FloraModel model = (FloraModel) inputElement;
	    return model.getModules().toArray(new Object[] {});
	}

	return null;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.IContentProvider#dispose()
     */
    public void dispose() {

    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
     */
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

    }

}
