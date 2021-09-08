/* File:      FloraModule.java
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

package net.sourceforge.flora.eclipse.reasoner.object;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.flora.eclipse.reasoner.FloraReasoner;
import net.sourceforge.flora.javaAPI.src.FloraObject;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

/**
 * models a Flora-2 module
 * 
 * @author Daniel Winkler
 */
public class FloraModule extends FloraElement implements  IEditorInput {

	private boolean fChanged;

	/**
	 * the Constructor
	 * 
	 * @param object
	 *            the <code>FloraObject</code> which should be wrapped
	 */
	public FloraModule(FloraObject object) {
		super(object);
		setChanged();
	}

	/**
	 * loads the registered root elements of the module by querying the Reasoner. The elements are then created as <code>FloraClass</code> of
	 * <code>FloraElement</code>
	 * 
	 * @see FloraClass#FloraClass(FloraObject, FloraModule)
	 * @see FloraInstance#FloraInstance(FloraObject, FloraModule)
	 */
	public void loadModule() {
		if (hasChanged()) {
			String query = null;

			getReasoner().executeCommand("abolish_all_tables.", false, false, false);
			super.getSubClasses().clear();
			super.getInstances().clear();

			query = getName() + "[rootClass->?R]@" + FloraReasoner.VISUALIZER_MODULE + ".";

			Iterator<FloraObject> topClasses = getReasoner().executeQuery(query, false, false);
			while (topClasses.hasNext()) {
				super.getSubClasses().add(new FloraClass(topClasses.next(), this));
			}

			query = getName() + "[rootInstance->?R]@" + FloraReasoner.VISUALIZER_MODULE + ".";

			Iterator<FloraObject> topInst = getReasoner().executeQuery(query, false, false);
			while (topInst.hasNext()) {
				super.getInstances().add(new FloraInstance(topInst.next(), this));
			}

			clearChanged();
		}
	}

	/**
	 * returns the SubClasses of this <code>FloraModule</code>
	 * 
	 * @return the SubClasses of this <code>FloraModule</code>
	 */
	public List<FloraClass> getSubClasses() {
		if (hasChanged())
			loadModule();
		return super.getSubClasses();
	}

	/**
	 * returns the Instances of this <code>FloraModule</code>
	 * 
	 * @return the Instances of this <code>FloraModule</code>
	 */
	public List<FloraInstance> getInstances() {
		if (hasChanged())
			loadModule();
		return super.getInstances();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IEditorInput#exists()
	 */
	public boolean exists() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IEditorInput#getImageDescriptor()
	 */
	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IEditorInput#getPersistable()
	 */
	public IPersistableElement getPersistable() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IEditorInput#getToolTipText()
	 */
	public String getToolTipText() {
		return getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(Class adapter) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (obj instanceof FloraModule) {
			FloraModule mod = (FloraModule) obj;
			return getName().equals(mod.getName());
		}
		return false;
	}

	/**
	 * sets the changed-status
	 */
	public void setChanged() {
		fChanged = true;
	}

	/**
	 * clears the changed-status
	 */
	protected void clearChanged() {
		fChanged = false;
	}

	/**
	 * returns the changed-status
	 * 
	 * @return the changed-status
	 */
	public boolean hasChanged() {
		return fChanged;
	}

}
