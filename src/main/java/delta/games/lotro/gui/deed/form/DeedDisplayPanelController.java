package delta.games.lotro.gui.deed.form;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import delta.common.ui.swing.GuiFactory;
import delta.games.lotro.common.ChallengeLevel;
import delta.games.lotro.gui.LotroIconsManager;
import delta.games.lotro.gui.common.navigator.NavigablePanelController;
import delta.games.lotro.gui.common.navigator.NavigatorWindowController;
import delta.games.lotro.gui.common.requirements.RequirementsUtils;
import delta.games.lotro.gui.common.rewards.form.RewardsPanelController;
import delta.games.lotro.gui.quests.ObjectivesHtmlBuilder;
import delta.games.lotro.lore.deeds.DeedDescription;
import delta.games.lotro.lore.deeds.DeedType;
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
  private JLabel _challengeLevel;
  private JLabel _requirements;
  private JEditorPane _details;

  // Controllers
  private NavigatorWindowController _parent;
  private RewardsPanelController _rewards;

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

    // Top panel
    JPanel topPanel=buildTopPanel();
    GridBagConstraints c=new GridBagConstraints(0,0,2,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(topPanel,c);

    // Rewards
    _rewards=new RewardsPanelController(_deed.getRewards());
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
      // Name
      _name=GuiFactory.buildLabel("");
      _name.setFont(_name.getFont().deriveFont(16f).deriveFont(Font.BOLD));
      panelLine.add(_name);
      panel.add(panelLine,c);
      c.gridy++;
    }
    // Line 2
    {
      JPanel panelLine=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
      // Category
      panelLine.add(GuiFactory.buildLabel("Category: "));
      _category=GuiFactory.buildLabel("");
      panelLine.add(_category);
      // Type
      panelLine.add(GuiFactory.buildLabel("Type: "));
      _type=GuiFactory.buildLabel("");
      panelLine.add(_type);
      panel.add(panelLine,c);
      c.gridy++;
    }
    // Line 3 (challenge level)
    {
      JPanel panelLine=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
      // Challenge level
      panelLine.add(GuiFactory.buildLabel("Level: "));
      _challengeLevel=GuiFactory.buildLabel("");
      panelLine.add(_challengeLevel);
      panel.add(panelLine,c);
      c.gridy++;
    }
    // Line 4
    {
      JPanel panelLine=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
      // Requirements
      panelLine.add(GuiFactory.buildLabel("Requirements: "));
      _requirements=GuiFactory.buildLabel("");
      panelLine.add(_requirements);
      panel.add(panelLine,c);
      c.gridy++;
    }
    // Padding to push everything on left
    JPanel paddingPanel=GuiFactory.buildPanel(new BorderLayout());
    c.fill=GridBagConstraints.HORIZONTAL;
    c.weightx=1.0;
    panel.add(paddingPanel,c);

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
          String reference=e.getDescription();
          _parent.navigateTo(reference);
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
    ObjectivesHtmlBuilder.buildHtml(sb,_deed);
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
    _requirements.setText(requirements);
    // Details
    _details.setText(buildHtml());
    _details.setCaretPosition(0);
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
    _parent=null;
    // UI
    _type=null;
    _category=null;
    _challengeLevel=null;
    _requirements=null;
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
