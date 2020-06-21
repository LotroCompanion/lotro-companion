package delta.games.lotro.gui.character.traitTree;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.Path2D;
import java.util.List;

import javax.swing.JPanel;

import delta.games.lotro.character.classes.TraitTreeBranch;
import delta.games.lotro.character.classes.TraitTreeCell;
import delta.games.lotro.character.classes.TraitTreeCellDependency;

/**
 * Panel to show the dependencies between cells.
 * @author DAM
 */
public class TraitTreeDependenciesPanelController extends JPanel
{
  private TraitTreeBranch _branch;

  /**
   * Constructor.
   * @param branch Branch to show.
   */
  public TraitTreeDependenciesPanelController(TraitTreeBranch branch)
  {
    _branch=branch;
    Dimension d=TraitTreeBranchGeometryUtils.getPanelSize();
    setPreferredSize(d);
    setMinimumSize(d);
    setSize(d);
    setOpaque(false);
  }

  @Override
  protected void paintComponent(Graphics g)
  {
    super.paintComponent(g);
    paintDependencies(g);
  }

  private void paintDependencies(Graphics g)
  {
    for(String cellId : _branch.getCells())
    {
      TraitTreeCell cell=_branch.getCell(cellId);
      List<TraitTreeCellDependency> dependencies=cell.getDependencies();
      for(TraitTreeCellDependency dependency : dependencies)
      {
        paintDependency(g,cell,dependency);
      }
    }
  }

  private void paintDependency(Graphics g, TraitTreeCell cell, TraitTreeCellDependency dependency)
  {
    String from=cell.getCellId();
    Dimension fromPosition=TraitTreeBranchGeometryUtils.getCenterPosition(from);
    String to=dependency.getCellId();
    Dimension toPosition=TraitTreeBranchGeometryUtils.getCenterPosition(to);
    g.drawLine(fromPosition.width,fromPosition.height,toPosition.width,toPosition.height);
    //System.out.println("From: "+fromPosition+", to: "+toPosition);
    double angle=0;
    if (toPosition.width==fromPosition.width)
    {
      angle=Math.PI/2;
    }
    else if (toPosition.height==fromPosition.height)
    {
      angle=0;
    }
    int x=(toPosition.width+fromPosition.width)/2;
    int y=(toPosition.height+fromPosition.height)/2;
    drawArrow((Graphics2D)g,x,y,angle);
  }

  private void drawArrow(Graphics2D g, int x, int y, double angle)
  {
    g=(Graphics2D)g.create();
    g.translate(x,y);
    g.rotate(angle);
    float arrowRatio=0.5f;
    float arrowLength=10.0f;

    BasicStroke stroke=(BasicStroke)g.getStroke();

    float endX=arrowLength/2;
    float veeX=endX-stroke.getLineWidth()*0.5f/arrowRatio;

    // Path
    Path2D.Float path=new Path2D.Float();
    float waisting=0.5f;
    float waistX=endX-arrowLength*0.5f;
    float waistY=arrowRatio*arrowLength*0.5f*waisting;
    float arrowWidth=arrowRatio*arrowLength;

    path.moveTo(veeX-arrowLength,-arrowWidth);
    path.quadTo(waistX,-waistY,endX,0.0f);
    path.quadTo(waistX,waistY,veeX-arrowLength,arrowWidth);

    // End of arrow is pinched in
    path.lineTo(veeX-arrowLength*0.75f,0.0f);
    path.lineTo(veeX-arrowLength,-arrowWidth);
    g.fill(path);
  }

  @Override
  public String getToolTipText(MouseEvent event)
  {
    // Nothing
    return null;
  }
}
