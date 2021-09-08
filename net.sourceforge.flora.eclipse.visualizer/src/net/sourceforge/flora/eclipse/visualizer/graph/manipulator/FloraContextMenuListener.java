/* File:      FloraContextMenuListener.java
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

package net.sourceforge.flora.eclipse.visualizer.graph.manipulator;

import java.util.ArrayList;

import net.sourceforge.flora.eclipse.reasoner.FloraReasoner;
import net.sourceforge.flora.eclipse.reasoner.object.FloraInstance;
import net.sourceforge.flora.eclipse.visualizer.dialog.ClusterDialog;
import net.sourceforge.flora.eclipse.visualizer.dialog.NodeDialog;
import net.sourceforge.flora.eclipse.visualizer.graph.FloraGraph;
import net.sourceforge.flora.eclipse.visualizer.graph.element.FloraClassNode;
import net.sourceforge.flora.eclipse.visualizer.graph.element.FloraInstanceClusterNode;
import net.sourceforge.flora.eclipse.visualizer.graph.element.FloraInstanceNode;
import net.sourceforge.flora.eclipse.visualizer.graph.element.FloraNode;
import net.sourceforge.flora.eclipse.visualizer.graph.factory.GraphFactory;
import net.sourceforge.jpowergraph.Edge;
import net.sourceforge.jpowergraph.Graph;
import net.sourceforge.jpowergraph.Node;
import net.sourceforge.jpowergraph.lens.LensSet;
import net.sourceforge.jpowergraph.swt.manipulator.DefaultSWTContextMenuListener;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

public class FloraContextMenuListener extends DefaultSWTContextMenuListener {
	private FloraGraph floraGraph = null;

	public FloraContextMenuListener(Graph theGraph, LensSet theLensSet,
			Integer[] theZoomLevels, Integer[] theRotateAngles) {
		super(theGraph, theLensSet, theZoomLevels, theRotateAngles);
		if (theGraph instanceof FloraGraph) {
			floraGraph = (FloraGraph) theGraph;
		}
	}

	public void fillNodeContextMenu(final Node theNode, Menu theMenu) {
		if (floraGraph != null) {
			MenuItem it1 = new MenuItem(theMenu, SWT.CASCADE);
			it1.setText("Unregister " + theNode.getLabel());
			it1.addSelectionListener(new SelectionListener() {
				public void widgetSelected(SelectionEvent arg0) {
					doStuff();
				}

				public void widgetDefaultSelected(SelectionEvent arg0) {
					doStuff();
				}

				public void doStuff() {
					Job theJob = new Job("Unregistering") {
						protected IStatus run(IProgressMonitor monitor) {
							final ArrayList<String> elements = new ArrayList<String>();
							if (theNode instanceof FloraInstanceClusterNode) {
								FloraInstanceClusterNode clusterNode = (FloraInstanceClusterNode) theNode;
								for (FloraInstance instance : clusterNode
										.getInstances())
									elements.add(instance.toString());
							} else {
								elements.add(theNode.toString());
							}
							Display.getDefault().syncExec(new Runnable() {
								public void run() {
									FloraReasoner.getInstance().unregister(
											floraGraph.getModule().toString(),
											elements);
									GraphFactory.updateGraph(floraGraph);
								}
							});
							return new Status(IStatus.OK,
									"net.sourceforge.flora.eclipse.visualizer",
									IStatus.OK, "Unregistered successfully",
									null);
						}
					};
					theJob.setUser(true);
					theJob.schedule();

				}
			});

			it1 = new MenuItem(theMenu, SWT.CASCADE);
			it1.setText("Show details of " + theNode.getLabel());
			it1.addSelectionListener(new SelectionListener() {
				public void widgetSelected(SelectionEvent arg0) {
					doStuff();
				}

				public void widgetDefaultSelected(SelectionEvent arg0) {
					doStuff();
				}

				public void doStuff() {
					if (theNode instanceof FloraClassNode) {
						new NodeDialog(Display.getCurrent().getActiveShell(),
								theNode).open();
					}

					if (theNode instanceof FloraInstanceNode) {
						new NodeDialog(Display.getCurrent().getActiveShell(),
								theNode).open();
					}

					if (theNode instanceof FloraInstanceClusterNode) {
						new ClusterDialog(
								Display.getCurrent().getActiveShell(), theNode)
								.open();
					}

				}
			});

			if (theNode instanceof FloraClassNode) {
				final FloraClassNode floraClassNode = (FloraClassNode) theNode;
				try {
					if (floraGraph.getClusterNodes().contains(floraClassNode)) { // add
																					// Menu
																					// Item
																					// "Uncluster"
						it1 = new MenuItem(theMenu, SWT.CASCADE);
						it1.setText("Uncluster");
						it1.addSelectionListener(new SelectionListener() {
							public void widgetSelected(SelectionEvent arg0) {
								doStuff();
							}

							public void widgetDefaultSelected(
									SelectionEvent arg0) {
								doStuff();
							}

							public void doStuff() {
								Job theJob = new Job("Unclustering") {
									protected IStatus run(
											IProgressMonitor monitor) {
										floraGraph
												.unclusterNode(floraClassNode);
										Display.getDefault().syncExec(
												new Runnable() {
													public void run() {
														GraphFactory
																.updateGraph(floraGraph);
													}
												});
										return new Status(
												IStatus.OK,
												"net.sourceforge.flora.eclipse.visualizer",
												IStatus.OK,
												"Unclustered successfully",
												null);
									}
								};
								theJob.setUser(true);
								theJob.schedule();

							}
						});
					} else { // add Menu Item "Cluster"
						it1 = new MenuItem(theMenu, SWT.CASCADE);
						it1.setText("Cluster");
						it1.addSelectionListener(new SelectionListener() {
							public void widgetSelected(SelectionEvent arg0) {
								doStuff();
							}

							public void widgetDefaultSelected(
									SelectionEvent arg0) {
								doStuff();
							}

							public void doStuff() {
								Job theJob = new Job("Clustering") {
									protected IStatus run(
											IProgressMonitor monitor) {
										floraGraph.clusterNode(floraClassNode);
										Display.getDefault().syncExec(
												new Runnable() {
													public void run() {
														GraphFactory
																.updateGraph(floraGraph);
													}
												});
										return new Status(
												IStatus.OK,
												"net.sourceforge.flora.eclipse.visualizer",
												IStatus.OK,
												"Clustered successfully", null);
									}
								};
								theJob.setUser(true);
								theJob.schedule();

							}
						});
					}
				} catch (ClassCastException e) {
					return;
				}

			}

			if (theNode instanceof FloraInstanceClusterNode) {
				final FloraInstanceClusterNode floraInstanceNode = (FloraInstanceClusterNode) theNode;
				it1 = new MenuItem(theMenu, SWT.CASCADE);
				it1.setText("Uncluster");
				it1.addSelectionListener(new SelectionListener() {
					public void widgetSelected(SelectionEvent arg0) {
						doStuff();
					}

					public void widgetDefaultSelected(SelectionEvent arg0) {
						doStuff();
					}

					public void doStuff() {
						Job theJob = new Job("Unclustering") {
							protected IStatus run(IProgressMonitor monitor) {
								for (FloraNode parent : floraGraph.findParentsFor(floraInstanceNode))
									if (parent instanceof FloraClassNode) {
										floraGraph.unclusterNode((FloraClassNode) parent);
									}
								Display.getDefault().syncExec(new Runnable() {
									public void run() {
										GraphFactory.updateGraph(floraGraph);
									}
								});
								return new Status(
										IStatus.OK,
										"net.sourceforge.flora.eclipse.visualizer",
										IStatus.OK, "Unclustered successfully",
										null);
							}
						};
						theJob.setUser(true);
						theJob.schedule();

					}
				});

			}
		}
	}

	public void fillEdgeContextMenu(final Edge theEdge, Menu theMenu) {
	}
}
