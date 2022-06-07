package delta.games.lotro.gui.lore.deeds.form;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.navigator.NavigablePanelController;
import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.common.ui.swing.navigator.PageIdentifier;
import delta.games.lotro.common.ChallengeLevel;
import delta.games.lotro.common.requirements.AbstractAchievableRequirement;
import delta.games.lotro.gui.LotroIconsManager;
import delta.games.lotro.gui.common.requirements.RequirementsUtils;
import delta.games.lotro.gui.common.rewards.form.RewardsPanelController;
import delta.games.lotro.gui.lore.quests.ObjectivesDisplayBuilder;
import delta.games.lotro.gui.lore.quests.form.AbstractAchievableRequirementPanelController;
import delta.games.lotro.gui.lore.quests.form.AchievableRequirementsPanelFactory;
import delta.games.lotro.lore.deeds.DeedDescription;
import delta.games.lotro.lore.deeds.DeedType;
import delta.games.lotro.lore.webStore.WebStoreItem;
import delta.games.lotro.utils.gui.HtmlUtils;

/**
 * Controller for an deed display panel.
 * @author DAM
 */
public class DeedDisplayPanelController implements NavigablePanelController
{
  // Data
  private DeedDescription _deed;
  // GUI
  private JPanel _panel;

  private JLabel _icon;
  private JLabel _type;
  private JLabel _category;
  private JLabel _name;
  private JLabel _attributes;
  private JLabel _challengeLevel;
  private JLabel _requirements;
  private JLabel _questPack;
  private JEditorPane _details;

  // Controllers
  private NavigatorWindowController _parent;
  private RewardsPanelController _rewards;
  private AbstractAchievableRequirementPanelController _achievablesRequirements;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param deed Deed to edit.
   */
  public DeedDisplayPanelController(NavigatorWindowController parent, DeedDescription deed)
  {
    _parent=parent;
    _deed=deed;
  }

  @Override
  public String getTitle()
  {
    return "Deed: "+_deed.getName();
  }

  @Override
  public JPanel getPanel()
  {
    if (_panel==null)
    {
      _panel=build();
    }
    return _panel;
  }

  private JPanel build()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    GridBagConstraints c=new GridBagConstraints(0,0,2,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);

    // Top panel
    JPanel topPanel=buildTopPanel();
    panel.add(topPanel,c);

    // Rewards
    _rewards=new RewardsPanelController(_parent,_deed.getRewards());
    JPanel rewardsPanel=_rewards.getPanel();
    TitledBorder rewardsBorder=GuiFactory.buildTitledBorder("Rewards");
    rewardsPanel.setBorder(rewardsBorder);
    c=new GridBagConstraints(1,1,1,1,0.0,0.0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(rewardsPanel,c);

    // Details
    _details=buildDetailsPane();
    JScrollPane detailsPane=GuiFactory.buildScrollPane(_details);
    detailsPane.setBorder(GuiFactory.buildTitledBorder("Details"));
    c=new GridBagConstraints(0,1,1,1,1.0,1.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
    panel.add(detailsPane,c);

    _panel=panel;
    setData();
    return _panel;
  }

  private JPanel buildTopPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    // Main data line
    {
      JPanel panelLine=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
      // Icon
      _icon=GuiFactory.buildIconLabel(null);
      panelLine.add(_icon);
      panel.add(panelLine,c);
      c.gridy++;
      // Name
      _name=GuiFactory.buildLabel("");
      _name.setFont(_name.getFont().deriveFont(16f).deriveFont(Font.BOLD));
      panelLine.add(_name);
    }

    // Line 2
    {
      JPanel panelLine=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
      panel.add(panelLine,c);
      c.gridy++;
      // Category
      panelLine.add(GuiFactory.buildLabel("Category: "));
      _category=GuiFactory.buildLabel("");
      panelLine.add(_category);
      // Type
      panelLine.add(GuiFactory.buildLabel("Type: "));
      _type=GuiFactory.buildLabel("");
      panelLine.add(_type);
    }
    // Line 3 (challenge level)
    {
      JPanel panelLine=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
      panel.add(panelLine,c);
      c.gridy++;
      // Challenge level
      panelLine.add(GuiFactory.buildLabel("Level: "));
      _challengeLevel=GuiFactory.buildLabel("");
      panelLine.add(_challengeLevel);
    }
    // Line 4
    {
      JPanel panelLine=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
      panel.add(panelLine,c);
      c.gridy++;
      // Requirements
      panelLine.add(GuiFactory.buildLabel("Requirements: "));
      _requirements=GuiFactory.buildLabel("");
      panelLine.add(_requirements);
    }
    // Line 5 (attributes)
    {
      JPanel panelLine=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
      panel.add(panelLine,c);
      c.gridy++;
      // Attributes
      _attributes=GuiFactory.buildLabel("");
      panelLine.add(_attributes);
    }
    // Line 6 (quest pack)
    {
      JPanel panelLine=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
      panel.add(panelLine,c);
      c.gridy++;
      // Quest pack
      panelLine.add(GuiFactory.buildLabel("Contents pack: "));
      _questPack=GuiFactory.buildLabel("");
      panelLine.add(_questPack);
    }

    // Achievables requirements
    AbstractAchievableRequirement requirement=_deed.getQuestRequirements();
    if (requirement!=null)
    {
      _achievablesRequirements=AchievableRequirementsPanelFactory.buildAchievableRequirementPanelController(_parent,requirement);
      JPanel achievablesRequirementsPanel=_achievablesRequirements.getPanel();
      c=new GridBagConstraints(0,c.gridy,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
      panel.add(achievablesRequirementsPanel,c);
      achievablesRequirementsPanel.setBorder(GuiFactory.buildTitledBorder("Quests/deeds Requirements"));
      c.gridy++;
    }
    // Padding to push everything on left
    c=new GridBagConstraints(0,c.gridy,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(Box.createHorizontalGlue(),c);
    return panel;
  }

  private JEditorPane buildDetailsPane()
  {
    JEditorPane editor=new JEditorPane("text/html","");
    editor.setEditable(false);
    editor.setPreferredSize(new Dimension(500,300));
    editor.setOpaque(false);
    HyperlinkListener l=new HyperlinkListener()
    {
      @Override
      public void hyperlinkUpdate(HyperlinkEvent e)
      {
        if(e.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
        {
          String referenceStr=e.getDescription();
          PageIdentifier pageId=PageIdentifier.fromString(referenceStr);
          _parent.navigateTo(pageId);
        }
      }
    };
    editor.addHyperlinkListener(l);
    return editor;
  }

  private String buildHtml()
  {
    StringBuilder sb=new StringBuilder();
    sb.append("<html><body>");
    sb.append("<b>Description</b><p>");
    sb.append(HtmlUtils.toHtml(_deed.getDescription()));
    // Objectives
    new ObjectivesDisplayBuilder(true).build(sb,_deed);
    sb.append("</body></html>");
    return sb.toString();
  }

  /**
   * Set the deed to display.
   */
  private void setData()
  {
    String name=_deed.getName();
    // Name
    _name.setText(name);
    // Type
    DeedType type=_deed.getType();
    _type.setText((type!=null)?type.toString():"-");
    // Icon
    ImageIcon icon=LotroIconsManager.getDeedTypeIcon(type);
    _icon.setIcon(icon);
    // Category
    String category=_deed.getCategory();
    _category.setText((category!=null)?category:"");
    // Challenge level
    ChallengeLevel challengeLevel=_deed.getChallengeLevel();
    _challengeLevel.setText(challengeLevel.getLabel());
    // Requirements
    String requirements=RequirementsUtils.buildRequirementString(_deed.getUsageRequirement());
    if (requirements.length()==0) requirements="-";
    _requirements.setText(requirements);
    // Attributes
    String attributes=buildAttributesString();
    if (attributes.length()>0)
    {
      _attributes.setText(attributes);
    }
    else
    {
      _attributes.getParent().setVisible(false);
    }
    // Quest pack
    WebStoreItem webStoreItem=_deed.getWebStoreItem();
    if (webStoreItem!=null)
    {
      _questPack.setText(webStoreItem.getName());
    }
    else
    {
      _questPack.getParent().setVisible(false);
    }
    // Details
    _details.setText(buildHtml());
    _details.setCaretPosition(0);
  }

  /**
   * Build an attributes string.
   * @return A string, empty if no requirement.
   */
  private String buildAttributesString()
  {
    StringBuilder sb=new StringBuilder();
    // Monster play
    boolean isMonsterPlay=_deed.isMonsterPlay();
    if (isMonsterPlay)
    {
      if (sb.length()>0) sb.append(", ");
      sb.append("Monster Play");
    }
    // Hidden
    boolean hidden=_deed.isHidden();
    if (hidden)
    {
      if (sb.length()>0) sb.append(", ");
      sb.append("Hidden");
    }
    String ret=sb.toString();
    return ret;
  }

  @Override
  public void dispose()
  {
    // Data
    _deed=null;
    // Controllers
    if (_rewards!=null)
    {
      _rewards.dispose();
      _rewards=null;
    }
    if (_achievablesRequirements!=null)
    {
      _achievablesRequirements.dispose();
      _achievablesRequirements=null;
    }
    _parent=null;
    // UI
    _type=null;
    _category=null;
    _challengeLevel=null;
    _requirements=null;
    _attributes=null;
    _questPack=null;
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    _icon=null;
    _name=null;
    _details=null;
  }
}
