package delta.games.lotro.gui.quests.form;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import delta.common.ui.swing.GuiFactory;
import delta.games.lotro.common.CharacterClass;
import delta.games.lotro.common.Race;
import delta.games.lotro.common.Repeatability;
import delta.games.lotro.common.Size;
import delta.games.lotro.common.requirements.UsageRequirement;
import delta.games.lotro.gui.common.navigator.NavigablePanelController;
import delta.games.lotro.gui.common.navigator.NavigatorWindowController;
import delta.games.lotro.gui.common.rewards.form.RewardsPanelController;
import delta.games.lotro.gui.quests.ObjectivesHtmlBuilder;
import delta.games.lotro.lore.quests.QuestDescription;
import delta.games.lotro.lore.quests.QuestDescription.FACTION;

/**
 * Controller for a quest display panel.
 * @author DAM
 */
public class QuestDisplayPanelController implements NavigablePanelController
{
  // Data
  private QuestDescription _quest;
  // GUI
  private JPanel _panel;

  private JLabel _category;
  private JLabel _name;
  private JLabel _questArc;
  private JLabel _attributes;
  private JLabel _requirements;
  private JEditorPane _details;

  // Controllers
  private NavigatorWindowController _parent;
  private RewardsPanelController _rewards;
  private QuestLinksDisplayPanelController _links;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param quest Quest to edit.
   */
  public QuestDisplayPanelController(NavigatorWindowController parent, QuestDescription quest)
  {
    _parent=parent;
    _quest=quest;
  }

  @Override
  public String getTitle()
  {
    return "Quest: "+_quest.getName();
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

    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    // Main data line
    {
      JPanel panelLine=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
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
    }
    // Line 3
    {
      JPanel panelLine=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
      panel.add(panelLine,c);
      c.gridy++;
      // Quest arc
      panelLine.add(GuiFactory.buildLabel("Quest arc: "));
      _questArc=GuiFactory.buildLabel("");
      panelLine.add(_questArc);
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

    // Rewards
    _rewards=new RewardsPanelController(_quest.getRewards());
    JPanel rewardsPanel=_rewards.getPanel();
    TitledBorder rewardsBorder=GuiFactory.buildTitledBorder("Rewards");
    rewardsPanel.setBorder(rewardsBorder);
    c=new GridBagConstraints(0,c.gridy,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(rewardsPanel,c);
    c.gridy++;

    // Links
    _links=new QuestLinksDisplayPanelController(_parent,_quest);
    JPanel linksPanel=_links.getPanel();
    c=new GridBagConstraints(0,c.gridy,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(linksPanel,c);
    c.gridy++;

    // Details
    _details=buildDetailsPane();
    JScrollPane detailsPane=GuiFactory.buildScrollPane(_details);
    detailsPane.setBorder(GuiFactory.buildTitledBorder("Details"));
    c.fill=GridBagConstraints.BOTH;
    c.weightx=1.0;
    c.weighty=1.0;
    panel.add(detailsPane,c);
    c.gridy++;

    _panel=panel;
    setItem();
    return _panel;
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
    sb.append(toHtml(_quest.getDescription()));
    // Objectives
    ObjectivesHtmlBuilder.buildHtml(sb,_quest);
    sb.append("</body></html>");
    return sb.toString();
  }

  private String toHtml(String text)
  {
    text=text.trim();
    text=text.replace("\n\n","<br>");
    text=text.replace("\n","<br>");
    return text;
  }

  /**
   * Set the quest to display.
   */
  private void setItem()
  {
    String name=_quest.getName();
    // Name
    _name.setText(name);
    // Category
    String category=_quest.getCategory();
    _category.setText((category!=null)?category:"");
    // Quest arc
    String questArc=_quest.getQuestArc();
    _questArc.setText((questArc!=null)?questArc:"");
    // Requirements
    String requirements=buildRequirementString();
    _requirements.setText(requirements);
    // Attributes
    String attributes=buildAttributesString();
    _attributes.setText(attributes);
    // Details
    _details.setText(buildHtml());
    _details.setCaretPosition(0);
  }

  /**
   * Build a requirement string.
   * @return A string, empty if no requirement.
   */
  private String buildRequirementString()
  {
    UsageRequirement requirements=_quest.getUsageRequirement();
    CharacterClass requiredClass=requirements.getRequiredClass();
    Race requiredRace=requirements.getRequiredRace();
    Integer minLevel=requirements.getMinLevel();
    StringBuilder sb=new StringBuilder();
    if (requiredClass!=null)
    {
      if (sb.length()>0) sb.append(", ");
      sb.append(requiredClass.getLabel());
    }
    if (requiredRace!=null)
    {
      if (sb.length()>0) sb.append(", ");
      sb.append(requiredRace.getLabel());
    }
    if (minLevel!=null)
    {
      if (sb.length()>0) sb.append(", ");
      if (minLevel.intValue()==1000)
      {
        sb.append("level cap");
      }
      else
      {
        sb.append("level>=").append(minLevel);
      }
    }
    String ret=sb.toString();
    if (ret.isEmpty())
    {
      ret="-";
    }
    return ret;
  }

  /**
   * Build an attributes string.
   * @return A string, empty if no requirement.
   */
  private String buildAttributesString()
  {
    StringBuilder sb=new StringBuilder();
    // Size
    Size size=_quest.getSize();
    if ((size!=null) && (size!=Size.SOLO))
    {
      sb.append(size.toString());
    }
    // Faction
    FACTION faction=_quest.getFaction();
    if ((faction!=null) && (faction!=FACTION.FREE_PEOPLES))
    {
      if (sb.length()>0) sb.append(", ");
      sb.append(faction.toString());
    }
    // Repeatability
    Repeatability repeatability=_quest.getRepeatability();
    if ((repeatability!=null) && (repeatability!=Repeatability.NOT_REPEATABLE))
    {
      if (sb.length()>0) sb.append(", ");
      sb.append(repeatability.toString());
    }
    // Instanced
    boolean instanced=_quest.isInstanced();
    if (instanced)
    {
      if (sb.length()>0) sb.append(", ");
      sb.append("Instanced");
    }
    // Shareable
    boolean shareable=_quest.isShareable();
    if (!shareable)
    {
      if (sb.length()>0) sb.append(", ");
      sb.append("Not shareable");
    }
    // Session play
    boolean sessionPlay=_quest.isSessionPlay();
    if (sessionPlay)
    {
      if (sb.length()>0) sb.append(", ");
      sb.append("Session play");
    }
    // Auto-bestowed
    boolean autoBestowed=_quest.isAutoBestowed();
    if (autoBestowed)
    {
      if (sb.length()>0) sb.append(", ");
      sb.append("Auto-bestowed");
    }
    String ret=sb.toString();
    return ret;
  }

  @Override
  public void dispose()
  {
    // Data
    _quest=null;
    // Controllers
    if (_rewards!=null)
    {
      _rewards.dispose();
      _rewards=null;
    }
    if (_links!=null)
    {
      _links.dispose();
      _links=null;
    }
    _parent=null;
    // UI
    _category=null;
    _questArc=null;
    _requirements=null;
    _attributes=null;
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    _name=null;
    _details=null;
  }
}
