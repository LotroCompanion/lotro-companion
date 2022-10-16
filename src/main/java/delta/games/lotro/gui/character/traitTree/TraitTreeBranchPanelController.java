package delta.games.lotro.gui.character.traitTree;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.games.lotro.character.CharacterData;
import delta.games.lotro.character.classes.traitTree.TraitTreeBranch;
import delta.games.lotro.character.classes.traitTree.TraitTreeCell;
import delta.games.lotro.character.status.traitTree.TraitTreeStatus;
import delta.games.lotro.character.traits.TraitDescription;

/**
 * Controller for trait tree branch panel.
 * @author DAM
 */
public class TraitTreeBranchPanelController
{
  private static final Integer DEPENDENCIES_DEPTH=Integer.valueOf(0);
  private static final Integer ICONS_DEPTH=Integer.valueOf(1);

  private JPanel _panel;
  private JLayeredPane _layeredPane;
  private HashMap<String,TraitTreeCellController> _cells;
  private TraitTreeDependenciesPanelController _dependenciesPanel;
  // Data
  private TraitTreeBranch _branch;
  private TraitTreeStatus _status;

  /**
   * Constructor.
   * @param toon Character data.
   * @param branch Branch to show.
   * @param status Trait tree status to show.
   */
  public TraitTreeBranchPanelController(CharacterData toon, TraitTreeBranch branch, TraitTreeStatus status)
  {
    _branch=branch;
    _status=status;
    _dependenciesPanel=new TraitTreeDependenciesPanelController(branch);
    _cells=new HashMap<String,TraitTreeCellController>();
    init(toon);
    updateUi();
  }

  /**
   * Set the mouse listener for buttons.
   * @param listener Listener to set.
   */
  public void setMouseListener(MouseListener listener)
  {
    for(TraitTreeCellController ctrl : _cells.values())
    {
      JButton button=ctrl.getButton();
      button.addMouseListener(listener);
    }
  }

  /**
   * Get the controller for the specified cell.
   * @param cellId Cell identifier.
   * @return A controller or <code>null</code> if not found.
   */
  public TraitTreeCellController getController(String cellId)
  {
    return _cells.get(cellId);
  }

  private void init(CharacterData toon)
  {
    List<String> cellIds=_branch.getCells();
    for(String cellId : cellIds)
    {
      TraitTreeCell cell=_branch.getCell(cellId);
      TraitDescription trait=cell.getTrait();
      TraitTreeCellController cellController=new TraitTreeCellController(toon,cellId,trait);
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
      boolean enabled=_status.isEnabled(cellId);
      ctrl.setEnabled(enabled);
      if (enabled)
      {
        Integer rank=_status.getRankForCell(cellId);
        ctrl.setRank(rank.intValue());
      }
      else
      {
        _status.setRankForCell(cellId,0);
        ctrl.setRank(0);
      }
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
    Dimension d=TraitTreeBranchGeometryUtils.getPanelSize();
    panel.setPreferredSize(d);
    panel.setMinimumSize(d);
    _layeredPane.setSize(d);

    int iconSize=TraitTreeBranchGeometryUtils.ICON_SIZE;
    for(String cellId : _cells.keySet())
    {
      TraitTreeCellController cellController=_cells.get(cellId);
      JButton button=cellController.getButton();
      Dimension position=TraitTreeBranchGeometryUtils.getPosition(cellId);
      button.setBounds(position.width,position.height,iconSize,iconSize);
      _layeredPane.add(button,ICONS_DEPTH);
    }
    _dependenciesPanel.setLocation(0,0);
    _layeredPane.add(_dependenciesPanel,DEPENDENCIES_DEPTH);
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
