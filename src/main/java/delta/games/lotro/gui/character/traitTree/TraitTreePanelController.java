package delta.games.lotro.gui.character.traitTree;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.combobox.ComboBoxController;
import delta.common.ui.swing.combobox.ItemSelectionListener;
import delta.games.lotro.character.classes.TraitTree;
import delta.games.lotro.character.classes.TraitTreeBranch;
import delta.games.lotro.character.classes.TraitTreeStatus;
import delta.games.lotro.character.traits.TraitDescription;

/**
 * Controller for trait tree panel.
 * @author DAM
 */
public class TraitTreePanelController
{
  private JPanel _panel;
  private TraitTreeSidePanelController _side;
  private List<TraitTreeBranchPanelController> _branches;
  private ComboBoxController<TraitTreeBranch> _branchCombo;
  // Data
  private TraitTree _tree;
  private TraitTreeStatus _status;

  /**
   * Constructor.
   * @param tree Tree to show.
   * @param status Trait tree status to show.
   */
  public TraitTreePanelController(TraitTree tree, TraitTreeStatus status)
  {
    _tree=tree;
    _status=status;
    _side=new TraitTreeSidePanelController(tree,status);
    _branches=new ArrayList<TraitTreeBranchPanelController>();
    MouseListener listener=buildMouseListener();
    for(TraitTreeBranch branch : _tree.getBranches())
    {
      TraitTreeBranchPanelController branchCtrl=new TraitTreeBranchPanelController(branch,status);
      branchCtrl.setMouseListener(listener);
      _branches.add(branchCtrl);
    }
    _branchCombo=buildBranchCombo();
  }

  private ComboBoxController<TraitTreeBranch> buildBranchCombo()
  {
    ComboBoxController<TraitTreeBranch> ret=new ComboBoxController<TraitTreeBranch>();
    List<TraitTreeBranch> branches=_tree.getBranches();
    for(TraitTreeBranch branch : branches)
    {
      ret.addItem(branch,branch.getName());
    }
    ItemSelectionListener<TraitTreeBranch> listener=new ItemSelectionListener<TraitTreeBranch>()
    {
      public void itemSelected(TraitTreeBranch branch)
      {
        selectBranch(branch);
      }
    };
    ret.addListener(listener);
    return ret;
  }

  private MouseListener buildMouseListener()
  {
    MouseListener listener=new MouseAdapter()
    {
      @Override
      public void mousePressed(MouseEvent e)
      {
        boolean rightClick=SwingUtilities.isRightMouseButton(e);
        boolean leftClick=SwingUtilities.isLeftMouseButton(e);
        if (leftClick || rightClick)
        {
          JButton source=(JButton)e.getSource();
          String cellId=source.getActionCommand();
          handleClick(cellId,leftClick);
        }
      }
    };
    return listener;
  }

  private void handleClick(String cellId, boolean leftClick)
  {
    TraitTreeCellController ctrl=getController(cellId);
    boolean enabled=ctrl.isEnabled();
    if (!enabled)
    {
      return;
    }
    TraitDescription trait=ctrl.getTrait();
    int rank=ctrl.getRank();
    int newRank=rank;
    if (leftClick)
    {
      int maxRank=trait.getTiersCount();
      if (rank<maxRank)
      {
        newRank=rank+1;
      }
    }
    else
    {
      if (rank>0)
      {
        newRank=rank-1;
      }
    }
    if (newRank!=rank)
    {
      ctrl.setRank(newRank);
      _status.setRankForCell(cellId,newRank);
      updateUi();
    }
  }

  private void selectBranch(TraitTreeBranch branch)
  {
    _status.setSelectedBranch(branch);
    _side.setSelectedBranch(branch);
    _side.updateUi();
  }

  private void updateUi()
  {
    _side.updateUi();
    for(TraitTreeBranchPanelController ctrl : _branches)
    {
      ctrl.updateUi();
    }
  }

  /**
   * Get the controller for the specified cell.
   * @param cellId Cell identifier.
   * @return A controller or <code>null</code> if not found.
   */
  private TraitTreeCellController getController(String cellId)
  {
    for(TraitTreeBranchPanelController ctrl : _branches)
    {
      TraitTreeCellController cellCtr=ctrl.getController(cellId);
      if (cellCtr!=null)
      {
        return cellCtr;
      }
    }
    return null;
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
    JPanel panel=GuiFactory.buildBackgroundPanel(new BorderLayout());
    JPanel topPanel=buildTopPanel();
    panel.add(topPanel,BorderLayout.NORTH);
    JPanel centerPanel=buildCenterPanel();
    panel.add(centerPanel,BorderLayout.CENTER);
    return panel;
  }

  private JPanel buildTopPanel()
  {
    JPanel ret=GuiFactory.buildPanel(new FlowLayout());
    JLabel label=GuiFactory.buildLabel("Main branch:");
    ret.add(label);
    ret.add(_branchCombo.getComboBox());
    return ret;
  }

  private JPanel buildCenterPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    int x=0;
    // Side
    GridBagConstraints c=new GridBagConstraints(x,0,1,1,0.0,0.0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    panel.add(_side.getPanel(),c);
    x++;
    // Branches
    for(TraitTreeBranchPanelController branchCtrl : _branches)
    {
      JPanel branchPanel=branchCtrl.getPanel();
      c=new GridBagConstraints(x,0,1,1,0.0,0.0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
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
    if (_side!=null)
    {
      _side.dispose();
      _side=null;
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
    if (_branchCombo!=null)
    {
      _branchCombo.dispose();
      _branchCombo=null;
    }
  }
}
