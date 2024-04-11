package delta.games.lotro.gui.character.traitTree;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JPanel;

import org.apache.log4j.Logger;

import delta.common.ui.swing.draw.Arrows;
import delta.games.lotro.character.classes.traitTree.TraitTreeBranch;
import delta.games.lotro.character.classes.traitTree.TraitTreeCell;
import delta.games.lotro.character.classes.traitTree.TraitTreeCellDependency;

/**
 * Panel to show the dependencies between cells.
 * @author DAM
 */
public class TraitTreeDependenciesPanelController extends JPanel
{
  private static final Logger LOGGER=Logger.getLogger(TraitTreeDependenciesPanelController.class);
  private transient TraitTreeBranch _branch;

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
    if (LOGGER.isDebugEnabled())
    {
      LOGGER.debug("From: "+fromPosition+", to: "+toPosition);
    }
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
    Arrows.drawArrow((Graphics2D)g,x,y,angle);
  }

  @Override
  public String getToolTipText(MouseEvent event)
  {
    // Nothing
    return null;
  }
}
