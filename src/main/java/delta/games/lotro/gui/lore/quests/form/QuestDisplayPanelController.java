package delta.games.lotro.gui.lore.quests.form;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.Box;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.navigator.AbstractNavigablePanelController;
import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.common.ui.swing.navigator.PageIdentifier;
import delta.common.utils.expressions.logical.LogicalTreeNode;
import delta.games.lotro.common.ChallengeLevel;
import delta.games.lotro.common.LockType;
import delta.games.lotro.common.Repeatability;
import delta.games.lotro.common.Size;
import delta.games.lotro.common.enums.QuestCategory;
import delta.games.lotro.common.requirements.AbstractAchievableRequirement;
import delta.games.lotro.gui.common.requirements.RequirementsUtils;
import delta.games.lotro.gui.common.rewards.form.RewardsPanelController;
import delta.games.lotro.gui.lore.quests.ObjectivesDisplayBuilder;
import delta.games.lotro.gui.lore.quests.QuestsHtmlUtils;
import delta.games.lotro.gui.lore.worldEvents.form.LogicalExpressionsPanelFactory;
import delta.games.lotro.gui.lore.worldEvents.form.PanelProvider;
import delta.games.lotro.lore.quests.QuestDescription;
import delta.games.lotro.lore.quests.dialogs.DialogElement;
import delta.games.lotro.lore.quests.dialogs.QuestCompletionComment;
import delta.games.lotro.lore.webStore.WebStoreItem;
import delta.games.lotro.lore.worldEvents.AbstractWorldEventCondition;
import delta.games.lotro.lore.worldEvents.WorldEventConditionsUtils;
import delta.games.lotro.utils.gui.HtmlUtils;
import delta.games.lotro.utils.strings.ContextRendering;

/**
 * Controller for a quest display panel.
 * @author DAM
 */
public class QuestDisplayPanelController extends AbstractNavigablePanelController
{
  // Data
  private QuestDescription _quest;
  // GUI
  private JLabel _category;
  private JLabel _name;
  private JLabel _questArc;
  private JLabel _attributes;
  private JLabel _challengeLevel;
  private JLabel _requirements;
  private JLabel _questPack;
  private JEditorPane _details;

  // Controllers
  private RewardsPanelController _rewards;
  private QuestLinksDisplayPanelController _links;
  private AbstractAchievableRequirementPanelController _achievablesRequirements;
  private PanelProvider _worldEventConditions;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param quest Quest to edit.
   */
  public QuestDisplayPanelController(NavigatorWindowController parent, QuestDescription quest)
  {
    super(parent);
    _quest=quest;
    setPanel(build());
  }

  @Override
  public String getTitle()
  {
    return "Quest: "+_quest.getName(); // I18n
  }

  private JPanel build()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    GridBagConstraints c=new GridBagConstraints(0,0,2,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);

    // Top panel
    JPanel topPanel=buildTopPanel();
    panel.add(topPanel,c);

    // Rewards
    _rewards=new RewardsPanelController(getParent(),_quest.getRewards());
    JPanel rewardsPanel=_rewards.getPanel();
    TitledBorder rewardsBorder=GuiFactory.buildTitledBorder("Rewards"); // I18n
    rewardsPanel.setBorder(rewardsBorder);
    c=new GridBagConstraints(1,1,1,1,0.0,0.0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(rewardsPanel,c);

    // Details
    _details=buildDetailsPane();
    JScrollPane detailsPane=GuiFactory.buildScrollPane(_details);
    detailsPane.setBorder(GuiFactory.buildTitledBorder("Details")); // I18n
    c=new GridBagConstraints(0,1,1,1,1.0,1.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
    panel.add(detailsPane,c);
    setData();
    return panel;
  }

  private JPanel buildTopPanel()
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
      panelLine.add(GuiFactory.buildLabel("Category: ")); // I18n
      _category=GuiFactory.buildLabel("");
      panelLine.add(_category);
    }
    // Line 3
    {
      JPanel panelLine=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
      panel.add(panelLine,c);
      c.gridy++;
      // Quest arc
      panelLine.add(GuiFactory.buildLabel("Quest arc: ")); // I18n
      _questArc=GuiFactory.buildLabel("");
      panelLine.add(_questArc);
    }
    // Line 4 (challenge level)
    {
      JPanel panelLine=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
      panel.add(panelLine,c);
      c.gridy++;
      // Challenge level
      panelLine.add(GuiFactory.buildLabel("Level: ")); // I18n
      _challengeLevel=GuiFactory.buildLabel("");
      panelLine.add(_challengeLevel);
    }
    // Line 5 (requirements)
    {
      JPanel panelLine=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
      panel.add(panelLine,c);
      c.gridy++;
      // Requirements
      panelLine.add(GuiFactory.buildLabel("Requirements: ")); // I18n
      _requirements=GuiFactory.buildLabel("");
      panelLine.add(_requirements);
    }
    // Line 6 (attributes)
    {
      JPanel panelLine=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
      panel.add(panelLine,c);
      c.gridy++;
      // Attributes
      _attributes=GuiFactory.buildLabel("");
      panelLine.add(_attributes);
    }
    // Line 7 (quest pack)
    {
      JPanel panelLine=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
      panel.add(panelLine,c);
      c.gridy++;
      // Quest pack
      panelLine.add(GuiFactory.buildLabel("Contents pack: ")); // I18n
      _questPack=GuiFactory.buildLabel("");
      panelLine.add(_questPack);
    }

    // Links
    _links=new QuestLinksDisplayPanelController(getParent(),_quest);
    JPanel linksPanel=_links.getPanel();
    c=new GridBagConstraints(0,c.gridy,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(linksPanel,c);
    c.gridy++;

    // Achievables requirements
    AbstractAchievableRequirement requirement=_quest.getQuestRequirements();
    if (requirement!=null)
    {
      _achievablesRequirements=AchievableRequirementsPanelFactory.buildAchievableRequirementPanelController(getParent(),requirement);
      JPanel achievablesRequirementsPanel=_achievablesRequirements.getPanel();
      c=new GridBagConstraints(0,c.gridy,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
      panel.add(achievablesRequirementsPanel,c);
      achievablesRequirementsPanel.setBorder(GuiFactory.buildTitledBorder("Quests/deeds Requirements")); // I18n
      c.gridy++;
    }
    // World events conditions
    AbstractWorldEventCondition worldEventsConditions=_quest.getWorldEventsRequirement();
    if (worldEventsConditions!=null)
    {
      LogicalTreeNode<String> expression=WorldEventConditionsUtils.renderWorldEventCondition(worldEventsConditions);
      if (expression!=null)
      {
        _worldEventConditions=LogicalExpressionsPanelFactory.buildPanelController(expression);
        JPanel worldEventConditionsPanel=_worldEventConditions.getPanel();
        c=new GridBagConstraints(0,c.gridy,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
        panel.add(worldEventConditionsPanel,c);
        worldEventConditionsPanel.setBorder(GuiFactory.buildTitledBorder("Context")); // I18n
        c.gridy++;
      }
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
          getParent().navigateTo(pageId);
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
    sb.append("<b>Description</b><br>"); // I18n
    String description=_quest.getDescription();
    description=ContextRendering.render(this,description);
    sb.append(HtmlUtils.toHtml(description));
    // Bestowers
    List<DialogElement> bestowers=_quest.getBestowers();
    if (bestowers.size()>0)
    {
      sb.append("<p><b>Bestowal dialogue</b>"); // I18n
      int index=0;
      for(DialogElement bestower : bestowers)
      {
        if (index>0)
        {
          sb.append("<br>OR"); // I18n
        }
        QuestsHtmlUtils.buildHtmlForDialog(this,sb,bestower);
        index++;
      }
      sb.append("</p>");
    }
    // Objectives
    new ObjectivesDisplayBuilder(this,true).build(sb,_quest);
    // End dialogs
    List<DialogElement> endDialogs=_quest.getEndDialogs();
    if (endDialogs.size()>0)
    {
      sb.append("<p><b>End</b>"); // I18n
      for(DialogElement endDialog : endDialogs)
      {
        QuestsHtmlUtils.buildHtmlForDialog(this,sb,endDialog);
      }
      sb.append("</p>");
    }
    // Completion comments
    List<QuestCompletionComment> comments=_quest.getCompletionComments();
    if (comments.size()>0)
    {
      sb.append("<p><b>Completion comments</b>"); // I18n
      for(QuestCompletionComment comment : comments)
      {
        QuestsHtmlUtils.buildHtmlForCompletionComment(this,sb,comment);
      }
      sb.append("</p>");
    }
    // HTML end
    sb.append("</body></html>");
    return sb.toString();
  }

  /**
   * Set the quest to display.
   */
  private void setData()
  {
    String name=_quest.getName();
    // Name
    _name.setText(name);
    // Category
    QuestCategory category=_quest.getCategory();
    _category.setText((category!=null)?category.getLabel():"");
    // Quest arc
    String questArc=_quest.getQuestArc();
    _questArc.setText((questArc!=null)?questArc:"");
    // Challenge level
    ChallengeLevel challengeLevel=_quest.getChallengeLevel();
    _challengeLevel.setText(challengeLevel.getLabel());
    // Requirements
    String requirements=RequirementsUtils.buildRequirementString(this,_quest.getUsageRequirement());
    if (requirements.length()==0) requirements="-";
    _requirements.setText(requirements);
    // Attributes
    String attributes=buildAttributesString();
    _attributes.setText(attributes);
    // Quest pack
    WebStoreItem webStoreItem=_quest.getWebStoreItem();
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
    // Size
    Size size=_quest.getSize();
    if ((size!=null) && (size!=Size.SOLO))
    {
      sb.append(size.toString());
    }
    // Monster play
    boolean isMonsterPlay=_quest.isMonsterPlay();
    if (isMonsterPlay)
    {
      if (sb.length()>0) sb.append(", ");
      sb.append("Monster Play"); // I18n
    }
    // Repeatability
    Repeatability repeatability=_quest.getRepeatability();
    if ((repeatability!=null) && (repeatability!=Repeatability.NOT_REPEATABLE))
    {
      if (sb.length()>0) sb.append(", ");
      sb.append(repeatability.toString());
    }
    // Lock type
    LockType lockType=_quest.getLockType();
    if (lockType!=null)
    {
      if (sb.length()>0) sb.append(", ");
      sb.append(lockType.toString());
    }
    // Instanced
    boolean instanced=_quest.isInstanced();
    if (instanced)
    {
      if (sb.length()>0) sb.append(", ");
      sb.append("Instanced"); // I18n
    }
    // Shareable
    boolean shareable=_quest.isShareable();
    if (!shareable)
    {
      if (sb.length()>0) sb.append(", ");
      sb.append("Not shareable"); // I18n
    }
    // Session play
    boolean sessionPlay=_quest.isSessionPlay();
    if (sessionPlay)
    {
      if (sb.length()>0) sb.append(", ");
      sb.append("Session play"); // I18n
    }
    // Auto-bestowed
    boolean autoBestowed=_quest.isAutoBestowed();
    if (autoBestowed)
    {
      if (sb.length()>0) sb.append(", ");
      sb.append("Auto-bestowed"); // I18n
    }
    // Hidden
    boolean hidden=_quest.isHidden();
    if (hidden)
    {
      if (sb.length()>0) sb.append(", ");
      sb.append("Hidden"); // I18n
    }
    String ret=sb.toString();
    return ret;
  }

  @Override
  public void dispose()
  {
    super.dispose();
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
    if (_achievablesRequirements!=null)
    {
      _achievablesRequirements.dispose();
      _achievablesRequirements=null;
    }
    if (_worldEventConditions!=null)
    {
      _worldEventConditions.dispose();
      _worldEventConditions=null;
    }
    // UI
    _category=null;
    _questArc=null;
    _challengeLevel=null;
    _requirements=null;
    _attributes=null;
    _questPack=null;
    _name=null;
    _details=null;
  }
}
