package delta.games.lotro.gui.lore.quests;

import delta.common.ui.swing.navigator.NavigablePanelController;
import delta.common.ui.swing.navigator.NavigablePanelControllerFactory;
import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.common.ui.swing.navigator.PageIdentifier;
import delta.games.lotro.gui.common.navigation.ReferenceConstants;
import delta.games.lotro.gui.lore.quests.form.QuestDisplayPanelController;
import delta.games.lotro.lore.quests.AchievableProxiesResolver;
import delta.games.lotro.lore.quests.QuestDescription;
import delta.games.lotro.lore.quests.QuestsManager;

/**
 * Factory for quest panels.
 * @author DAM
 */
public class QuestPanelsFactory implements NavigablePanelControllerFactory
{
  private NavigatorWindowController _parent;

  /**
   * Constructor.
   * @param parent Parent window.
   */
  public QuestPanelsFactory(NavigatorWindowController parent)
  {
    _parent=parent;
  }

  @Override
  public NavigablePanelController build(PageIdentifier pageId)
  {
    NavigablePanelController ret=null;
    String address=pageId.getBaseAddress();
    if (address.equals(ReferenceConstants.QUEST_PAGE))
    {
      int id=pageId.getIntParameter(PageIdentifier.ID_PARAMETER).intValue();
      ret=buildQuestPanel(id);
    }
    return ret;
  }

  private QuestDisplayPanelController buildQuestPanel(int questId)
  {
    QuestsManager questsMgr=QuestsManager.getInstance();
    QuestDescription quest=questsMgr.getQuest(questId);
    if (quest!=null)
    {
      AchievableProxiesResolver.resolve(quest);
      QuestDisplayPanelController questPanel=new QuestDisplayPanelController(_parent,quest);
      return questPanel;
    }
    return null;
  }
}
