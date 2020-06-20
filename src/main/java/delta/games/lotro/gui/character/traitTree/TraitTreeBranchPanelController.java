package delta.games.lotro.gui.character.traitTree;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.utils.NumericTools;
import delta.games.lotro.character.classes.TraitTreeBranch;
import delta.games.lotro.character.classes.TraitTreeCell;
import delta.games.lotro.character.classes.TraitTreeStatus;
import delta.games.lotro.character.traits.TraitDescription;

/**
 * Controller for trait tree branch panel.
 * @author DAM
 */
public class TraitTreeBranchPanelController
{
  private static final Integer ICONS_DEPTH=Integer.valueOf(1);

  private static final int ICON_SIZE=32;
  private static final int ICON_FRAME_SIZE=0;
  private static final int FRAMED_ICON_SIZE=2*ICON_FRAME_SIZE+ICON_SIZE;
  private static final int DELTA_X=24;
  private static final int DELTA_Y=24;
  private static final int X_MARGIN=15;
  private static final int Y_MARGIN=10;
  private static final int NB_ROWS=7;
  private static final int NB_COLUMNS=4;
  private static final int WIDTH=X_MARGIN+NB_COLUMNS*FRAMED_ICON_SIZE+(NB_COLUMNS-1)*DELTA_X+X_MARGIN;
  private static final int HEIGHT=Y_MARGIN+NB_ROWS*FRAMED_ICON_SIZE+(NB_ROWS-1)*DELTA_Y+Y_MARGIN;

  private JPanel _panel;
  private JLayeredPane _layeredPane;
  private HashMap<String,TraitTreeCellController> _cells;
  // Data
  private TraitTreeBranch _branch;
  private TraitTreeStatus _status;

  /**
   * Constructor.
   * @param branch Branch to show.
   * @param status Trait tree status to show.
   */
  public TraitTreeBranchPanelController(TraitTreeBranch branch, TraitTreeStatus status)
  {
    _branch=branch;
    _status=status;
    _cells=new HashMap<String,TraitTreeCellController>();
    init();
    updateUi();
  }

  /**
   * Get the position for a cell of a trait tree branch.
   * @param column Column, starting at 0.
   * @param row Row, starting at 0.
   * @return A position.
   */
  private Dimension getPosition(int column, int row)
  {
    int x=X_MARGIN+column*(FRAMED_ICON_SIZE+DELTA_X);
    int y=Y_MARGIN+row*(FRAMED_ICON_SIZE+DELTA_Y);
    return new Dimension(x,y);
  }

  private Dimension getPosition(String cellId)
  {
    int index=cellId.indexOf('_');
    int y=NumericTools.parseInt(cellId.substring(0,index),0);
    int x=NumericTools.parseInt(cellId.substring(index+1),0);
    int column=(x-1)%4;
    int row=y-1;
    return getPosition(column,row);
  }

  private void init()
  {
    List<String> cellIds=_branch.getCells();
    for(String cellId : cellIds)
    {
      TraitTreeCell cell=_branch.getCell(cellId);
      TraitDescription trait=cell.getTrait();
      TraitTreeCellController cellController=new TraitTreeCellController(cellId,trait);
      _cells.put(cellId,cellController);
    }
  }

  /**
   * Update UI to reflect the current trait tree status.
   */
  public void updateUi()
  {
    for(TraitTreeCellController ctrl : _cells.values())
    {
      String cellId=ctrl.getCellId();
      int rank=_status.getRankForCell(cellId);
      ctrl.setRank(rank);
      boolean enabled=_status.isEnabled(cellId);
      ctrl.setEnabled(enabled);
    }
  }

  /**
   * Get the managed panel.
   * @return a panel.
   */
  public JPanel getPanel()
  {
    if (_panel==null)
    {
      _panel=buildPanel();
    }
    return _panel;
  }

  private JPanel buildPanel()
  {
    JPanel panel=GuiFactory.buildPanel(null);
    _layeredPane=new JLayeredPane();
    panel.add(_layeredPane,BorderLayout.CENTER);
    Dimension d=new Dimension(WIDTH,HEIGHT);
    panel.setPreferredSize(d);
    panel.setMinimumSize(d);
    _layeredPane.setSize(d);

    for(String cellId : _cells.keySet())
    {
      TraitTreeCellController cellController=_cells.get(cellId);
      JButton button=cellController.getButton();
      Dimension position=getPosition(cellId);
      button.setBounds(position.width,position.height,ICON_SIZE,ICON_SIZE);
      _layeredPane.add(button,ICONS_DEPTH);
    }
    return panel;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    if (_layeredPane!=null)
    {
      _layeredPane.removeAll();
      _layeredPane=null;
    }
    if (_cells!=null)
    {
      for(TraitTreeCellController cellController : _cells.values())
      {
        cellController.dispose();
      }
      _cells.clear();
      _cells=null;
    }
  }
}
