package delta.games.lotro.gui.lore.quests.form;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.icons.IconsManager;
import delta.common.ui.swing.labels.HyperLinkController;
import delta.common.ui.swing.labels.LocalHyperlinkAction;
import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.common.ui.swing.navigator.PageIdentifier;
import delta.common.ui.swing.windows.WindowController;
import delta.common.utils.text.StringTools;
import delta.games.lotro.character.status.achievables.QuestRequirementStateComputer;
import delta.games.lotro.common.requirements.QuestRequirement;
import delta.games.lotro.common.requirements.QuestStatus;
import delta.games.lotro.common.utils.ComparisonOperator;
import delta.games.lotro.gui.common.navigation.ReferenceConstants;
import delta.games.lotro.lore.quests.Achievable;
import delta.games.lotro.utils.Proxy;

/**
 * Controller for a panel to show a simple achievable requirement.
 * @author DAM
 */
public class SimpleAchievableRequirementPanelController extends AbstractAchievableRequirementPanelController
{
  private static final String UNMANAGED_QUEST_STATUS="Unmanaged quest status: ";

  private static final Logger LOGGER=Logger.getLogger(SimpleAchievableRequirementPanelController.class);

  private static final String ACHIEVABLE_LINK_SEED="{LINK}";

  // Controllers
  private HyperLinkController _link;
  // Data
  private QuestRequirement _requirement;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param computer State computer.
   * @param requirement Requirement to show.
   */
  public SimpleAchievableRequirementPanelController(WindowController parent, QuestRequirementStateComputer computer, QuestRequirement requirement)
  {
    super(parent,computer);
    _requirement=requirement;
    _link=buildLinkController();
    JPanel panel=buildPanel();
    setPanel(panel);
  }

  private HyperLinkController buildLinkController()
  {
    HyperLinkController ret=null;
    Proxy<Achievable> proxy=_requirement.getRequiredAchievable();
    if (proxy!=null)
    {
      Achievable achievable=proxy.getObject();
      if (achievable!=null)
      {
        final PageIdentifier ref=ReferenceConstants.getAchievableReference(achievable);
        ActionListener listener=new ActionListener()
        {
          @Override
          public void actionPerformed(ActionEvent e)
          {
            NavigatorWindowController parent=(NavigatorWindowController)getParentController();
            parent.navigateTo(ref);
          }
        };
        String name=achievable.getName();
        LocalHyperlinkAction action=new LocalHyperlinkAction(name,listener);
        ret=new HyperLinkController(action);
      }
      else
      {
        LOGGER.warn("Achievable not managed: "+achievable);
      }
    }
    return ret;
  }

  private JPanel buildPanel()
  {
    String label=buildRequirementLabel();
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    if (_stateComputer!=null)
    {
      boolean ok=_stateComputer.assess(_requirement);
      Icon stateIcon=IconsManager.getIcon("/resources/gui/icons/state/"+(ok?"ok":"ko")+".png");
      JLabel icon=GuiFactory.buildIconLabel(stateIcon);
      ret.add(icon,c);
      c.gridx++;
    }
    String before=StringTools.findBefore(label,ACHIEVABLE_LINK_SEED);
    if ((before!=null) && (before.length()>0))
    {
      ret.add(GuiFactory.buildLabel(before),c);
      c.gridx++;
    }
    JLabel linkLabel=((_link!=null)?_link.getLabel():GuiFactory.buildLabel("???"));
    c.weightx=1.0;
    ret.add(linkLabel,c);
    c.gridx++;
    String after=StringTools.findAfter(label,ACHIEVABLE_LINK_SEED);
    if ((after!=null) && (after.length()>0))
    {
      ret.add(GuiFactory.buildLabel(after),c);
      c.gridx++;
    }
    return ret;
  }

  private String buildRequirementLabel()
  {
    ComparisonOperator operator=_requirement.getOperator();
    QuestStatus questStatus=_requirement.getQuestStatus();
    if (operator==ComparisonOperator.EQUAL) return buildEqualRequirementLabel(questStatus);
    if (operator==ComparisonOperator.NOT_EQUAL) return buildNotEqualRequirementLabel(questStatus);
    if (operator==ComparisonOperator.GREATER_OR_EQUAL) return buildGreaterOrEqualsRequirementLabel(questStatus);
    if (operator==ComparisonOperator.LESS) return buildLessRequirementLabel(questStatus);
    LOGGER.warn("Unmanaged quest requirement operator: "+operator);
    return "??? "+ACHIEVABLE_LINK_SEED+" ???";
  }

  private String buildEqualRequirementLabel(QuestStatus status)
  {
    if (status==QuestStatus.COMPLETED) return ACHIEVABLE_LINK_SEED+" is completed"; // I18n
    if (status==QuestStatus.UNDERWAY) return ACHIEVABLE_LINK_SEED+" is underway"; // I18n
    if (status==QuestStatus.FAILED) return ACHIEVABLE_LINK_SEED+" is failed"; // I18n
    int objectiveIndex=status.getObjectiveIndex();
    if (objectiveIndex>0)
    {
      return ACHIEVABLE_LINK_SEED+" is underway at objective "+objectiveIndex; // I18n
    }
    LOGGER.warn(UNMANAGED_QUEST_STATUS+status);
    return ACHIEVABLE_LINK_SEED+" is ???";
  }

  private String buildNotEqualRequirementLabel(QuestStatus status)
  {
    if (status==QuestStatus.COMPLETED) return ACHIEVABLE_LINK_SEED+" is not completed"; // I18n
    if (status==QuestStatus.UNDERWAY) return ACHIEVABLE_LINK_SEED+" is not underway"; // I18n
    if (status==QuestStatus.FAILED) return ACHIEVABLE_LINK_SEED+" is not failed"; // I18n
    int objectiveIndex=status.getObjectiveIndex();
    if (objectiveIndex>0)
    {
      return ACHIEVABLE_LINK_SEED+" is not underway at objective "+objectiveIndex; // I18n
    }
    LOGGER.warn(UNMANAGED_QUEST_STATUS+status);
    return ACHIEVABLE_LINK_SEED+" is not ???";
  }

  private String buildGreaterOrEqualsRequirementLabel(QuestStatus status)
  {
    if (status==QuestStatus.UNDERWAY) return ACHIEVABLE_LINK_SEED+" is underway/completed"; // I18n
    if (status==QuestStatus.COMPLETED) return ACHIEVABLE_LINK_SEED+" is completed"; // I18n
    if (status==null) return ACHIEVABLE_LINK_SEED+" is at least ???"; // I18n
    if (status==QuestStatus.FAILED)
    {
      LOGGER.warn("Unexpected requirement combinaison: greater or equal "+status+" for "+_requirement.getQuestId());
    }
    int objectiveIndex=status.getObjectiveIndex();
    if (objectiveIndex>0)
    {
      return ACHIEVABLE_LINK_SEED+" is completed or underway with at least objective "+objectiveIndex+" done"; // I18n
    }
    LOGGER.warn(UNMANAGED_QUEST_STATUS+status);
    return "??? "+ACHIEVABLE_LINK_SEED+" ???";
  }

  private String buildLessRequirementLabel(QuestStatus status)
  {
    if (status==QuestStatus.UNDERWAY) return ACHIEVABLE_LINK_SEED+" is not started"; // I18n
    if (status==null) return ACHIEVABLE_LINK_SEED+" has not reached ???"; // I18n
    if ((status==QuestStatus.COMPLETED) || (status==QuestStatus.FAILED))
    {
      LOGGER.warn("Unexpected requirement combinaison: greater or equal "+status+" for "+_requirement.getQuestId());
    }
    LOGGER.warn(UNMANAGED_QUEST_STATUS+status);
    return "??? "+ACHIEVABLE_LINK_SEED+" ???";
  }

  @Override
  public void dispose()
  {
    super.dispose();
    // Controllers
    if (_link!=null)
    {
      _link.dispose();
      _link=null;
    }
  }
}
