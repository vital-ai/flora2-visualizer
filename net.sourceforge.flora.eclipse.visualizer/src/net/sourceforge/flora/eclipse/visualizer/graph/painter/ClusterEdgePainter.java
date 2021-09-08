/* File:      ClusterEdgePainter.java
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

package net.sourceforge.flora.eclipse.visualizer.graph.painter;

import net.sourceforge.jpowergraph.Edge;
import net.sourceforge.jpowergraph.SubGraphHighlighter;
import net.sourceforge.jpowergraph.painters.edge.AbstractEdgePainter;
import net.sourceforge.jpowergraph.pane.JGraphPane;
import net.sourceforge.jpowergraph.swtswinginteraction.JPowerGraphGraphics;
import net.sourceforge.jpowergraph.swtswinginteraction.color.JPowerGraphColor;
import net.sourceforge.jpowergraph.swtswinginteraction.geometry.JPowerGraphPoint;
import net.sourceforge.jpowergraph.swtswinginteraction.geometry.JPowerGraphRectangle;

/**
 * The painter that paints the edge as the arrow.
 */
public class ClusterEdgePainter extends AbstractEdgePainter {
    
    private JPowerGraphColor normal;
    private JPowerGraphColor highlighted;

    /**
     * the Constructor
     */
    public ClusterEdgePainter (){
        normal = new JPowerGraphColor(246, 246, 246);
        highlighted = new JPowerGraphColor(197, 197, 197);
    }
    
    /**
     * Paints the supplied edge.
     *
     * @param graphPane             the graph pane
     * @param g                     the graphics
     * @param edge                  the edge to paint
     */
    public void paintEdge(JGraphPane graphPane, JPowerGraphGraphics g, Edge edge, SubGraphHighlighter theSubGraphHighlighter) {
        JPowerGraphPoint from=graphPane.getScreenPointForNode(edge.getFrom());
        JPowerGraphPoint to=graphPane.getScreenPointForNode(edge.getTo());
        
        JPowerGraphRectangle nodeRectangle = new JPowerGraphRectangle(0, 0, 0, 0);
        graphPane.getNodeScreenBounds(edge.getTo(), nodeRectangle);
        
        JPowerGraphColor oldBGColor = g.getBackground();
        JPowerGraphColor oldFGColor = g.getForeground();
        g.setBackground(normal);
        g.setForeground(highlighted);
        paintArrow(g,from.x,from.y,to.x,to.y, nodeRectangle);
        g.setBackground(oldBGColor);
        g.setForeground(oldFGColor);
    }
    
    /**
     * Paints the arrow.
     *
     * @param g                     the graphics
     * @param x1                    the source x coordinate
     * @param y1                    the source y coordinate
     * @param x2                    the target x coordinate
     * @param y2                    the target y coordinate
     */
    public static void paintArrow(JPowerGraphGraphics g,int x1,int y1,int x2,int y2, JPowerGraphRectangle nodeRactangle) {
        double dx;
        double dy;
        double deltaX=x1-x2;
        double deltaY=y1-y2;
        if (Math.abs(deltaY)>Math.abs(deltaX)) {
            double slope=Math.abs(deltaX/deltaY);
            dx=(nodeRactangle.width/2 - 2)/Math.sqrt(1+slope*slope);
            dy=dx*slope;
        }
        else {
            double slope=Math.abs(deltaY/deltaX);
            dy=(nodeRactangle.width/2 - 2)/Math.sqrt(1+slope*slope);
            dx=dy*slope;
        }
        if (deltaY>0)
            dx*=-1;
        if (deltaX<0)
            dy*=-1;
        
        int polyX1 = x1;
        int polyY1 = y1;
        int polyX2 = (int)(x2-dx);
        int polyY2 = (int)(y2-dy);
        int polyX3 = (int)(x2+dx);
        int polyY3 = (int)(y2+dy);
        
        g.fillPolygon(new int[]{polyX1, polyY1, polyX2, polyY2, polyX3, polyY3});
        g.drawPolygon(new int[]{polyX1, polyY1, polyX2, polyY2, polyX3, polyY3});
    }
    
    /**
     * Returns the outer rectangle of the edge on screen.
     *
     * @param graphPane             the graph pane
     * @param edge                  the edge
     * @param edgeScreenRectangle   the rectangle receiving the edge's coordinates
     */
    public void getEdgeScreenBounds(JGraphPane graphPane, Edge edge, JPowerGraphRectangle edgeScreenRectangle) {
        JPowerGraphPoint from=graphPane.getScreenPointForNode(edge.getFrom());
        JPowerGraphPoint to=graphPane.getScreenPointForNode(edge.getTo());
        edgeScreenRectangle.x = Math.min(from.x,to.x);
        edgeScreenRectangle.y = Math.min(from.y,to.y);
        edgeScreenRectangle.width = Math.abs(to.x-from.x)+1;
        edgeScreenRectangle.height = Math.abs(to.y-from.y)+1;
    }
}
