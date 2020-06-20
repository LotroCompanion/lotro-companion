package delta.games.lotro.gui.character.traitTree;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.games.lotro.character.classes.TraitTree;
import delta.games.lotro.character.classes.TraitTreeBranch;
import delta.games.lotro.character.classes.TraitTreeStatus;

/**
 * Controller for trait tree panel.
 * @author DAM
 */
public class TraitTreePanelController
{
  private JPanel _panel;
  List<TraitTreeBranchPanelController> _branches;
  // Data
  private TraitTree _tree;

  /**
   * Constructor.
   * @param tree Tree to show.
   * @param status Trait tree status to show.
   */
  public TraitTreePanelController(TraitTree tree, TraitTreeStatus status)
  {
    _tree=tree;
    _branches=new ArrayList<TraitTreeBranchPanelController>();
    for(TraitTreeBranch branch : _tree.getBranches())
    {
      TraitTreeBranchPanelController branchCtrl=new TraitTreeBranchPanelController(branch,status);
      _branches.add(branchCtrl);
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
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    int x=0;
    for(TraitTreeBranchPanelController branchCtrl : _branches)
    {
      JPanel branchPanel=branchCtrl.getPanel();
      GridBagConstraints c=new GridBagConstraints(x,0,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
      panel.add(branchPanel,c);
      x++;
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
    if (_branches!=null)
    {
      for(TraitTreeBranchPanelController branchController : _branches)
      {
        branchController.dispose();
      }
      _branches.clear();
      _branches=null;
    }
  }
}
