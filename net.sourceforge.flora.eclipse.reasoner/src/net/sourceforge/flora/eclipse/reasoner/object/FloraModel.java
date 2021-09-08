/* File:      FloraModel.java
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
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;

import net.sourceforge.flora.eclipse.reasoner.FloraReasoner;
import net.sourceforge.flora.javaAPI.src.FloraObject;

import org.eclipse.swt.widgets.Display;

/**
 * A model of the {@link FloraReasoner}s current state
 * 
 * @author Daniel Winkler
 */
public class FloraModel extends Observable implements IFloraElement, Observer {
    private FloraReasoner reasoner = null;

    private ArrayList<FloraModule> modules;

    /**
         * holds an singleton <code>Instance</code> of <code>FloraModel</code>
         */
    private static class InstanceHolder {
	private static FloraModel fInstance = null;

	public static FloraModel getInstance() {
		if (fInstance == null)
			fInstance = new FloraModel();
		
		return fInstance;
	}
    }

    /**
         * the <code>Consructor</code>
         */
    private FloraModel() {
	reasoner = FloraReasoner.getInstance();
	reasoner.addObserver(this);

	modules = new ArrayList<FloraModule>();

	loadModules();
    }

    /**
         * returns the singleton <code>Instance</code> of this Class
         * 
         * @return the singleton <code>Instance</code> of this Class
         */
    public static FloraModel getInstance() {
	return InstanceHolder.getInstance();
    }

    /*
         * (non-Javadoc)
         * 
         * @see net.sourceforge.flora.eclipse.reasoner.object.IFloraElement#getChildren()
         */
    public ArrayList<IFloraElement> getChildren() {
	ArrayList<IFloraElement> children = new ArrayList<IFloraElement>(modules);

	return children;
    }

    /**
         * Loads the modules ArrayList by querying the FloraReasoner
         */
    private void loadModules() {
	if (!reasoner.isSessionLoaded())
	    return;

	String moduleQuery = "_isloaded(?Mod).";
	Iterator<FloraObject> iterator = reasoner.executeQuery(moduleQuery, false, false);

	ArrayList<FloraModule> tempModules = new ArrayList<FloraModule>();

	while (iterator.hasNext()) {
	    FloraObject object = iterator.next();
	    if (!object.toString().equals(FloraReasoner.VISUALIZER_MODULE))
		tempModules.add(new FloraModule(object));
	}

	for (FloraModule module : tempModules) {
	    if (modules.contains(module))
		modules.get(modules.indexOf(module)).setChanged();
	    else
		modules.add(module);
	}

	// not marked elements are not part of the model any more
	tempModules.clear();
	for (FloraModule module : modules) {
	    if (!module.hasChanged())
		tempModules.add(module);
	}
	modules.removeAll(tempModules);
    }

    /*
         * (non-Javadoc)
         * 
         * @see net.sourceforge.flora.eclipse.reasoner.object.IFloraElement#getName()
         */
    public String getName() {
	return "Flora-2";
    }

    /**
         * loads the modules again and notifies all observers 
         * 
         * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
         * @see java.util.Observable#notifyObservers(Object)
         */
    public void update(Observable observable, final Object object) {
	this.setChanged();
	loadModules();

	Display.getDefault().syncExec(new Runnable() {
	    public void run() {
		notifyObservers(object);
	    }
	});
	clearChanged();
    }

    /**
         * returns the currently loaded modules of the {@link FloraReasoner}
         * 
         * @return the currently loaded modules of the {@link FloraReasoner}
         */
    public ArrayList<FloraModule> getModules() {
	return modules;
    }

    /**
         * adds a module to the {@link FloraReasoner}
         * 
         * @param moduleName
         *                the name of the new module
         */
    public void addModule(String moduleName) {
	reasoner.executeCommand("newmodule{" + moduleName + "}.", true, true);
    }

    /**
         * Deletes a module from the current <code>FloraModel</code>.
         * 
         * @param module
         *                the module to delete from the module
         */
    public void deleteModule(FloraModule module) {
	deleteModule(module.toString());
    }

    /**
         * Deletes a module from the current <code>FloraModel</code>.
         * 
         * @param moduleName
         *                the module to delete from the module
         */
    public void deleteModule(String moduleName) {
	reasoner.executeCommand("erasemodule{" + moduleName + "}.", true, true);
    }
}
