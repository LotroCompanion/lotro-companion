package delta.games.lotro.gui.character.status.collections;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.icons.IconsManager;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.skills.SkillDescription;
import delta.games.lotro.character.skills.SkillsManager;
import delta.games.lotro.character.status.collections.CollectionStatus;
import delta.games.lotro.common.rewards.Rewards;
import delta.games.lotro.gui.common.rewards.form.RewardsPanelController;
import delta.games.lotro.gui.utils.GadgetsControllersFactory;
import delta.games.lotro.gui.utils.IconLinkLabelGadgetsController;
import delta.games.lotro.lore.collections.Collectable;
import delta.games.lotro.lore.collections.CollectionDescription;

/**
 * A panel to display the status of a collection.
 * @author DAM
 */
public class CollectionStatusDisplayPanelController
{
  // Data
  private CollectionStatus _status;
  // Controllers
  private WindowController _parent;
  private List<IconLinkLabelGadgetsController> _gadgets;
  private RewardsPanelController _rewardsCtrl;
  // UI
  private JPanel _panel;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param status Status to show.
   */
  public CollectionStatusDisplayPanelController(WindowController parent, CollectionStatus status)
  {
    _parent=parent;
    _status=status;
    _gadgets=new ArrayList<IconLinkLabelGadgetsController>();
    _panel=buildPanel();
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  public JPanel getPanel()
  {
    return _panel;
  }

  private JPanel buildPanel()
  {
    // Summary
    JPanel summaryPanel=buildSummaryPanel();
    // Stats
    JPanel statsPanel=buildStatsPanel();
    // Items
    JPanel itemsPanel=buildCollectionItemsPanel();
    // Rewards
    _rewardsCtrl=buildRewardsPanelController();
    // Assembly
    int y=0;
    JPanel ret=GuiFactory.buildBackgroundPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,y,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    ret.add(summaryPanel,c);
    y++;
    c=new GridBagConstraints(0,y,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    ret.add(statsPanel,c);
    y++;
    c=new GridBagConstraints(0,y,1,1,1.0,1.0,GridBagConstraints.NORTHWEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
    itemsPanel.setBorder(GuiFactory.buildTitledBorder("Elements")); // I18n
    ret.add(itemsPanel,c);
    y++;
    c=new GridBagConstraints(0,y,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    JPanel rewardsPanel=_rewardsCtrl.getPanel();
    rewardsPanel.setBorder(GuiFactory.buildTitledBorder("Rewards")); // I18n
    ret.add(rewardsPanel,c);
    return ret;
  }

  private JPanel buildSummaryPanel()
  {
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    CollectionDescription collection=_status.getCollection();
    String name=collection.getName();
    JLabel stateLabel=GuiFactory.buildLabel("Collection: "+name); // I18n
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(5,5,2,5),0,0);
    ret.add(stateLabel,c);
    return ret;
  }

  private JPanel buildStatsPanel()
  {
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    String label="";
    if (_status.isComplete())
    {
      label="Completed"; // I18n
    }
    else
    {
      int completedCount=_status.getCompletedCount();
      if (completedCount==0)
      {
        label="Not started"; // I18n
      }
      else
      {
        int total=_status.getTotalCount();
        label="Done "+completedCount+" / "+total; // I18n
      }
    }
    JLabel stateLabel=GuiFactory.buildLabel("State: "+label);
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,5,0,5),0,0);
    ret.add(stateLabel,c);
    return ret;
  }

  private JPanel buildCollectionItemsPanel()
  {
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    int y=0;
    CollectionDescription collection=_status.getCollection();
    List<Collectable> collectables=collection.getElements();
    for(Collectable collectable : collectables)
    {
      IconLinkLabelGadgetsController ctrl=buildGadgets(collectable);
      _gadgets.add(ctrl);
      boolean completed=_status.isCompleted(collectable);
      String icon="/resources/gui/icons/"+(completed?"check32":"delete32")+".png";
      ImageIcon imageIcon=IconsManager.getIcon(icon);
      JLabel iconLabel=GuiFactory.buildIconLabel(imageIcon);
      // Assembly
      GridBagConstraints c=new GridBagConstraints(0,y,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,5,2,5),0,0);
      ret.add(ctrl.getIcon().getIcon(),c);
      c=new GridBagConstraints(1,y,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(2,0,2,0),0,0);
      ret.add(ctrl.getLink().getLabel(),c);
      c=new GridBagConstraints(2,y,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,5,2,5),0,0);
      ret.add(iconLabel,c);
      y++;
    }
    return ret;
  }

  private RewardsPanelController buildRewardsPanelController()
  {
    CollectionDescription collection=_status.getCollection();
    Rewards rewards=collection.getRewards();
    RewardsPanelController ctrl=new RewardsPanelController(_parent,rewards);
    return ctrl;
  }

  private IconLinkLabelGadgetsController buildGadgets(Collectable collectable)
  {
    SkillDescription skill=SkillsManager.getInstance().getSkill(collectable.getIdentifier());
    IconLinkLabelGadgetsController ctrl=GadgetsControllersFactory.build(_parent,skill);
    return ctrl;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Data
    _status=null;
    // Controllers
    _parent=null;
    if (_gadgets!=null)
    {
      for(IconLinkLabelGadgetsController gadgets : _gadgets)
      {
        gadgets.dispose();
      }
      _gadgets=null;
    }
    if (_rewardsCtrl!=null)
    { 
      _rewardsCtrl.dispose();
      _rewardsCtrl=null;
    }
    // UI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
  }
}
