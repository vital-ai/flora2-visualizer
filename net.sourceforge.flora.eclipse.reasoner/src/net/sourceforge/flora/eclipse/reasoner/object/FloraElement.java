/* File:      FloraElement.java
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

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.flora.eclipse.reasoner.FloraReasoner;
import net.sourceforge.flora.javaAPI.src.FloraObject;

/**
 * models a Flora-2 object
 * 
 * @author Daniel Winkler
 */
public class FloraElement implements IFloraElement {

	private FloraReasoner fReasoner = null;

	private List<FloraClass> fSubClasses = null;

	private List<FloraInstance> fInstances = null;
	
	private FloraObject fObject = null;
	
	private String fName = null;

	/**
	 * the Constructor
	 * 
	 * @param object the <code>FloraObject</code> which should be wrapped
	 * @param module the module in which the <code>FloraObject</code> is located
	 */
	public FloraElement(FloraObject object) {
		setReasoner(FloraReasoner.getInstance());

		setObject(object);
		setName(getObject().toString());
		
		setSubClasses(new ArrayList<FloraClass>());
		setInstances(new ArrayList<FloraInstance>());
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.flora.eclipse.reasoner.object.IFloraElement#getName()
	 */
	public String getName() {
		return fName;
	}
	
	protected void setName(String name) {
		fName = name;
	}

	/**
	 * @return {@link #getName()}
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return getName();
	}

	/**
	 * returns the Subclasses of this Class
	 * 
	 * @return the Subclasses of this Class
	 */
	public List<FloraClass> getSubClasses() {
		return fSubClasses;
	}
	
	protected void setSubClasses(List<FloraClass> subClasses) {
		fSubClasses = subClasses;
	}

	/**
	 * returns the Instances of this Class
	 * 
	 * @return the Instances of this Class
	 */
	public List<FloraInstance> getInstances() {
		return fInstances;
	}

	protected void setInstances(List<FloraInstance> instances) {
		fInstances = instances;
	}

	
	/* (non-Javadoc)
	 * @see net.sourceforge.flora.eclipse.reasoner.object.IFloraElement#getChildren()
	 */
	public ArrayList<IFloraElement> getChildren() {
		ArrayList<IFloraElement> children = new ArrayList<IFloraElement>();
		children.addAll(getSubClasses());
		children.addAll(getInstances());
		return children;
	}
	
	public FloraObject getObject() {
		return fObject;
	}

	protected void setObject(FloraObject object) {
		fObject = object;
	}

	public FloraReasoner getReasoner() {
		return fReasoner;
	}

	public void setReasoner(FloraReasoner reasoner) {
		fReasoner = reasoner;
	}
}
