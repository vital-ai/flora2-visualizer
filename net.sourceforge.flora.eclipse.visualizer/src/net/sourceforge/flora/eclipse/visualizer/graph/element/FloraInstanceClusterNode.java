/* File:      FloraInstanceCluster.java
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

package net.sourceforge.flora.eclipse.visualizer.graph.element;

import java.util.List;

import net.sourceforge.flora.eclipse.reasoner.object.FloraInstance;

/**
 * A Class to represent <code>FloraInstances</code> in a cluster
 * 
 * @author Daniel Winkler
 */
public class FloraInstanceClusterNode extends FloraNode {

    private List<FloraInstance> instances;

    /**
     * the Constructor
     * 
     * @param theInstances the <code>FloraInstances</code> to add to the cluster
     */
    public FloraInstanceClusterNode(List<FloraInstance> theInstances) {
	super(null);
	setInstances(theInstances);
    }

    /* (non-Javadoc)
     * @see net.sourceforge.jpowergraph.defaults.DefaultNode#getNodeType()
     */
    public String getNodeType() {
	return "Instance Cluster";
    }

    /**
     * returns the number of <code>FloraInstances</code>
     * 
     * @return the number of <code>FloraInstances</code>
     */
    public int getNumberOfInstances() {
	return instances.size();
    }

    /**
     * @return a <code>List</code> containing all <code>FloraInstances</code>
     */
    public List<FloraInstance> getInstances() {
	return instances;
    }

    /**
     * sets the <code>FloraInstances</code>
     * 
     * @param theInstances the <code>FloraInstances</code> to represent
     */
    public void setInstances(List<FloraInstance> theInstances) {
	this.instances = theInstances;
    }
    
    /* (non-Javadoc)
     * @see net.sourceforge.flora.eclipse.visualizer.graph.element.FloraNode#toString()
     */
    public String toString()
    {
	return "" + getInstances().size() + " Instances";
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
    	boolean result = false;
	if (obj instanceof FloraInstanceClusterNode) {
	    FloraInstanceClusterNode instanceClusterNode = (FloraInstanceClusterNode) obj;
	    if (getInstances().size() == instanceClusterNode.getInstances().size()) {
	    	result = true;
	    	for (FloraInstance instance : getInstances()) {
	    		if (!instanceClusterNode.getInstances().contains(instance))
	    			result = false;
	    	}
	    }
	}
	return result;
    }

}
