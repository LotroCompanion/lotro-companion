package delta.games.lotro.gui.character.traitTree;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.combobox.ComboBoxController;
import delta.common.ui.swing.combobox.ItemSelectionListener;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.classes.traitTree.TraitTree;
import delta.games.lotro.character.classes.traitTree.TraitTreeBranch;
import delta.games.lotro.character.classes.traitTree.setup.TraitTreeSetup;
import delta.games.lotro.character.classes.traitTree.setup.TraitTreeSetupsManager;
import delta.games.lotro.character.status.traitPoints.TraitPoints;
import delta.games.lotro.character.status.traitTree.TraitTreeStatus;
import delta.games.lotro.character.traits.TraitDescription;
import delta.games.lotro.gui.character.traitTree.setup.TraitTreeSetupAttrsDialogController;
import delta.games.lotro.gui.character.traitTree.setup.TraitTreeSetupChooser;
import delta.games.lotro.utils.gui.HtmlUiUtils;

/**
 * Controller for trait tree panel.
 * @author DAM
 */
public class TraitTreePanelController
{
  // UI
  private JPanel _panel;
  private JLabel _points;
  // Controllers
  private WindowController _parent;
  private TraitTreeSidePanelController _side;
  private JEditorPane _branchDescription;
  private List<TraitTreeBranchPanelController> _branches;
  private ComboBoxController<TraitTreeBranch> _branchCombo;
  // Data
  private int _level;
  private TraitTree _tree;
  private TraitTreeStatus _status;
  private boolean _edition;

  /**
   * Constructor.
   * @param parent Parent window controller.
   * @param level Character level.
   * @param status Trait tree status to show.
   * @param edition Allow edition or not.
   */
  public TraitTreePanelController(WindowController parent,int level,TraitTreeStatus status,boolean edition)
  {
    _parent=parent;
    _level=level;
    _tree=status.getTraitTree();
    _status=status;
    _edition=edition;
    _side=new TraitTreeSidePanelController(level,_tree,status);
    _branches=new ArrayList<TraitTreeBranchPanelController>();
    for(TraitTreeBranch branch : _tree.getBranches())
    {
      TraitTreeBranchPanelController branchCtrl=new TraitTreeBranchPanelController(level,branch,status);
      _branches.add(branchCtrl);
    }
    if (_edition)
    {
      MouseListener listener=buildMouseListener();
      for(TraitTreeBranchPanelController branchCtrl : _branches)
      {
        branchCtrl.setMouseListener(listener);
      }
    }
    _branchCombo=buildBranchCombo();
    _branchCombo.getComboBox().setEnabled(edition);
  }

  private ComboBoxController<TraitTreeBranch> buildBranchCombo()
  {
    ComboBoxController<TraitTreeBranch> ret=new ComboBoxController<TraitTreeBranch>();
    List<TraitTreeBranch> branches=_tree.getBranches();
    ret.addEmptyItem("(none)"); // I18n
    for(TraitTreeBranch branch : branches)
    {
      if (branch.isEnabled())
      {
        ret.addItem(branch,branch.getName());
      }
    }
    ItemSelectionListener<TraitTreeBranch> listener=new ItemSelectionListener<TraitTreeBranch>()
    {
      @Override
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
    if (ctrl==null)
    {
      return;
    }
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
    updateBranchDescription();
    _side.setSelectedBranch(branch);
    _side.updateUi();
    updatePoints();
  }

  private void updateBranchDescription()
  {
    TraitTreeBranch branch=_status.getSelectedBranch();
    String description="";
    if (branch!=null)
    {
      description=branch.getDescription();
    }
    int ranks=_status.getRanksForBranch(branch);
    description=description.replace("${NUM}",String.valueOf(ranks));
    HtmlUiUtils.setText(_branchDescription,description);
  }

  private void updateUi()
  {
    _side.updateUi();
    for(TraitTreeBranchPanelController ctrl : _branches)
    {
      ctrl.updateUi();
    }
    updatePoints();
    updateBranchDescription();
  }

  private void updatePoints()
  {
    int cost=_status.computeCost();
    _status.setCost(cost);
    int max=TraitPoints.getTraitPointsFromLevel(_level);
    _points.setText(cost+" / "+max);
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
    _branchCombo.selectItem(_status.getSelectedBranch());
    updatePoints();
    return panel;
  }

  private JPanel buildTopPanel()
  {
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    // Summary
    JPanel summaryPanel=buildSummaryPanel();
    summaryPanel.setBorder(GuiFactory.buildTitledBorder("Summary"));
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,5),0,0);
    ret.add(summaryPanel,c);
    // Glue
    c=new GridBagConstraints(1,0,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(5,0,5,5),0,0);
    ret.add(Box.createGlue(),c);
    // Templates
    if (_edition)
    {
      JPanel templatePanel=buildTemplatePanel();
      templatePanel.setBorder(GuiFactory.buildTitledBorder("Templates"));
      c=new GridBagConstraints(2,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
      ret.add(templatePanel,c);
    }
    // Description
    _branchDescription=GuiFactory.buildHtmlPanel();
    _branchDescription.setBorder(GuiFactory.buildTitledBorder("Description"));
    c=new GridBagConstraints(0,1,3,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    ret.add(_branchDescription,c);
    return ret;
  }

  private JPanel buildSummaryPanel()
  {
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    // Branch chooser
    JLabel label=GuiFactory.buildLabel("Main branch:"); // I18n
    ret.add(label,c);
    c.gridx++;
    ret.add(_branchCombo.getComboBox(),c);
    // Points cost
    JLabel pointsLabel=GuiFactory.buildLabel("Cost: "); // I18n
    c.gridx++;
    ret.add(pointsLabel,c);
    _points=GuiFactory.buildLabel("");
    c.gridx++;
    ret.add(_points,c);
    c.gridx++;
    return ret;
  }

  private JPanel buildTemplatePanel()
  {
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    // Buttons
    // - load
    JButton load=GuiFactory.buildButton("Load from template..."); // I18n
    ActionListener alLoad=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        loadFromTemplate();
      }
    };
    load.addActionListener(alLoad);
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    ret.add(load,c);
    c.gridx++;
    // - save
    JButton save=GuiFactory.buildButton("Save as template..."); // I18n
    ActionListener alSave=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        saveAsTemplate();
      }
    };
    save.addActionListener(alSave);
    ret.add(save,c);
    c.gridx++;
    return ret;
  }

  private void loadFromTemplate()
  {
    // Choose template
    TraitTreeSetup setup=chooseSetup();
    if (setup==null)
    {
      return;
    }
    TraitTreeStatus status=setup.getStatus();
    _status.copyFrom(status);
    _branchCombo.selectItem(_status.getSelectedBranch());
    updateUi();
  }

  private TraitTreeSetup chooseSetup()
  {
    TraitTreeSetup setup=TraitTreeSetupChooser.chooseTraitTreeSetup(_parent,_tree.getType());
    return setup;
  }

  private void saveAsTemplate()
  {
    TraitTreeSetup setup=buildSetupFromCurrentData();
    TraitTreeSetupAttrsDialogController editDialog=new TraitTreeSetupAttrsDialogController(_parent,setup);
    setup=editDialog.editModal();
    if (setup!=null)
    {
      TraitTreeSetupsManager setupsMgr=TraitTreeSetupsManager.getInstance();
      setupsMgr.writeNewDataFile(setup);
    }
  }

  private TraitTreeSetup buildSetupFromCurrentData()
  {
    TraitTreeSetup setup=new TraitTreeSetup(_tree);
    // Status
    TraitTreeStatus status=setup.getStatus();
    status.copyFrom(_status);
    // Cost
    _status.setCost(_status.computeCost());
    return setup;
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
    panel.setBorder(GuiFactory.buildTitledBorder("Branches"));
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
    _parent=null;
    if (_side!=null)
    {
      _side.dispose();
      _side=null;
    }
    _branchDescription=null;
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
