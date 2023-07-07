package delta.games.lotro.gui.lore.quests.form;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.labels.HyperLinkController;
import delta.common.ui.swing.labels.LocalHyperlinkAction;
import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.common.ui.swing.navigator.PageIdentifier;
import delta.common.utils.text.StringTools;
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
  private static final Logger LOGGER=Logger.getLogger(SimpleAchievableRequirementPanelController.class);

  private static final String ACHIEVABLE_LINK_SEED="{LINK}";

  // Controllers
  private NavigatorWindowController _parent;
  private HyperLinkController _link;
  // UI
  private JPanel _panel;
  // Data
  private QuestRequirement _requirement;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param requirement Requirement to show.
   */
  public SimpleAchievableRequirementPanelController(NavigatorWindowController parent, QuestRequirement requirement)
  {
    _parent=parent;
    _requirement=requirement;
    _link=buildLinkController();
    _panel=buildPanel();
  }

  /**
   * Get the managed panel.
   * @return A panel.
   */
  public JPanel getPanel()
  {
    return _panel;
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
            _parent.navigateTo(ref);
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
    JPanel ret=GuiFactory.buildPanel(new FlowLayout());
    String before=StringTools.findBefore(label,ACHIEVABLE_LINK_SEED);
    if ((before!=null) && (before.length()>0))
    {
      ret.add(GuiFactory.buildLabel(before));
    }
    JLabel linkLabel=((_link!=null)?_link.getLabel():GuiFactory.buildLabel("???"));
    ret.add(linkLabel);
    String after=StringTools.findAfter(label,ACHIEVABLE_LINK_SEED);
    if ((after!=null) && (after.length()>0))
    {
      ret.add(GuiFactory.buildLabel(after));
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
    LOGGER.warn("Unmanaged quest status: "+status);
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
    LOGGER.warn("Unmanaged quest status: "+status);
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
    LOGGER.warn("Unmanaged quest status: "+status);
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
    LOGGER.warn("Unmanaged quest status: "+status);
    return "??? "+ACHIEVABLE_LINK_SEED+" ???";
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Controllers
    _parent=null;
    if (_link!=null)
    {
      _link.dispose();
      _link=null;
    }
    // UI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
  }
}
