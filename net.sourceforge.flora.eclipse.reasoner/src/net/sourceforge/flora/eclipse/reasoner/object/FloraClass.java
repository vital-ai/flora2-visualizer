/* File:      FloraClass.java
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import net.sourceforge.flora.eclipse.reasoner.FloraReasoner;
import net.sourceforge.flora.javaAPI.src.FloraObject;

/**
 * models a Flora-2 class object
 * 
 * @author Daniel Winkler
 */
public class FloraClass extends FloraElement {

	private FloraModule fModule;

	/**
	 * the Constructor
	 * 
	 * @param object the <code>FloraObject</code> which should be wrapped
	 * @param module the module in which the <code>FloraObject</code> is located
	 */
	public FloraClass(FloraObject object, FloraModule module) {
		super(object);
		fModule = module;

		setSubClasses();
		setInstances();
	}

	/**
	 * queries the {@link FloraReasoner} for the registered Instances of this Class
	 */
	private void setInstances() {
		String query = getName() + "[instance(" + fModule.getName()	+ ") -> ?I]" +
				"@" + FloraReasoner.VISUALIZER_MODULE + ".";

		Iterator instances = getReasoner().executeQuery(query, false, false);
		while (instances.hasNext()) {
			getInstances().add(new FloraInstance((FloraObject) instances.next(),
					fModule));
		}
	}

	/**
	 * queries the {@link FloraReasoner} for the registered Subclasses of this Class
	 */
	private void setSubClasses() {
		String query = getName() + "[subclass(" + fModule.getName() + ") -> ?I]" +
				"@" + FloraReasoner.VISUALIZER_MODULE + ".";

		Iterator subClasses = getReasoner().executeQuery(query, false, false);
		while (subClasses.hasNext()) {
			getSubClasses().add(new FloraClass((FloraObject) subClasses.next(),
					fModule));
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (obj instanceof FloraClass) {
			FloraClass cla = (FloraClass) obj;
			if (getModule().equals(cla.getModule()))
				return getName().equals(cla.getName());
		}
		return false;
	}

	/**
	 * returns the <code>FloraModule</code> in which this class is located
	 * 
	 * @return the <code>FloraModule</code> in which this class is located
	 */
	public FloraModule getModule() {
		return fModule;
	}

	/**
	 * sets the <code>FloraModule</code> in which this class is located
	 * 
	 * @param module the <code>FloraModule</code> in which this class is located
	 */
	public void setModule(FloraModule module) {
		fModule = module;
	}

	/**
	 * Returns an <code>Object [3]</code> containing <code>FloraObjects</code> as index 0 and 2 and a
	 * <code>String</code> as index 1. 
	 * 
	 * @return an <code>Object []</code> containing the types of this class
	 */
	public List<Object []> getTypes()
	{
		String[] queryParts = new String[] { "=> ?Y", "*=> ?Y", "=> %?Y", "?X => ?Y", "?X *=> ?Y" };

	    List<Object[]> result = new ArrayList<Object []>();
	    String query = null;
	    Vector<String> vars = new Vector<String>();
	    vars.add("?X");
	    vars.add("?Y");

	    // boolean values
	    for (String queryPart : queryParts) {

		query = getName() + "[ " + queryPart + " ]@" + getModule().toString() + ".";

		Iterator<HashMap<String, FloraObject>> iterator = FloraReasoner.getInstance().executeQuery(query, vars, false, false);

		while (iterator.hasNext()) {
		    Object[] singleLine = new Object[3];
		    HashMap<String, FloraObject> line = iterator.next();
		    if (queryPart.contains("?X"))
			singleLine[0] = line.get("?X");
		    else
			singleLine[0] = null;

		    if (queryPart.contains("*=>"))
			singleLine[1] = "*=>";
		    else if (queryPart.contains("=>"))
			singleLine[1] = "=>";
		    else
			singleLine[1] = "";

		    if (queryPart.contains("?Y"))
			singleLine[2] = line.get("?Y");
		    else
			singleLine[2] = null;

		    result.add(singleLine);
		}
	    }

	    return result;

	}
	
	/**
	 * Returns an <code>Object [3]</code> containing <code>FloraObjects</code> as index 0 and 2 and a
	 * <code>String</code> as index 1. 
	 * 
	 * @return an <code>Object []</code> containing the types of this class
	 */
	public List<Object []> getValues()
	{
		String[] queryParts = new String[] { "?X", "?X -> ?Y", "?X *-> ?Y" };

	    List<Object[]> result = new ArrayList<Object []>();
	    String query = null;
	    Vector<String> vars = new Vector<String>();
	    vars.add("?X");
	    vars.add("?Y");

	    // boolean values
	    for (String queryPart : queryParts) {

		query = getName() + "[ " + queryPart + " ]@" + getModule().toString() + ".";

		Iterator<HashMap<String, FloraObject>> iterator = FloraReasoner.getInstance().executeQuery(query, vars, false, false);

		while (iterator.hasNext()) {
		    Object[] singleLine = new Object[3];
		    HashMap<String, FloraObject> line = iterator.next();
		    if (queryPart.contains("?X"))
			singleLine[0] = line.get("?X");
		    else
			singleLine[0] = null;

		    if (queryPart.contains("*->"))
			singleLine[1] = "*->";
		    else if (queryPart.contains("->"))
			singleLine[1] = "->";
		    else
			singleLine[1] = "";

		    if (queryPart.contains("?Y"))
			singleLine[2] = line.get("?Y");
		    else
			singleLine[2] = null;

		    result.add(singleLine);
		}
	    }

	    return result;

	}
}
