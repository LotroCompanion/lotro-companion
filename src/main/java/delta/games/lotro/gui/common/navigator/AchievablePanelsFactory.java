package delta.games.lotro.gui.common.navigator;

import delta.common.ui.swing.navigator.NavigablePanelController;
import delta.common.ui.swing.navigator.NavigablePanelControllerFactory;
import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.common.ui.swing.navigator.PageIdentifier;
import delta.games.lotro.gui.deed.form.DeedDisplayPanelController;
import delta.games.lotro.gui.quests.form.QuestDisplayPanelController;
import delta.games.lotro.lore.deeds.DeedDescription;
import delta.games.lotro.lore.deeds.DeedsManager;
import delta.games.lotro.lore.quests.AchievableProxiesResolver;
import delta.games.lotro.lore.quests.QuestDescription;
import delta.games.lotro.lore.quests.QuestsManager;

/**
 * Factory for achievable panels.
 * @author DAM
 */
public class AchievablePanelsFactory implements NavigablePanelControllerFactory
{
  private NavigatorWindowController _parent;

  /**
   * Constructor.
   * @param parent Parent window.
   */
  public AchievablePanelsFactory(NavigatorWindowController parent)
  {
    _parent=parent;
  }

  @Override
  public NavigablePanelController build(PageIdentifier pageId)
  {
    NavigablePanelController ret=null;
    String address=pageId.getBaseAddress();
    if (address.equals(ReferenceConstants.DEED_PAGE))
    {
      int id=pageId.getIntParameter(PageIdentifier.ID_PARAMETER).intValue();
      ret=buildDeedPanel(id);
    }
    else if (address.equals(ReferenceConstants.QUEST_PAGE))
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

  private DeedDisplayPanelController buildDeedPanel(int deedId)
  {
    DeedsManager deedsMgr=DeedsManager.getInstance();
    DeedDescription deed=deedsMgr.getDeed(deedId);
    if (deed!=null)
    {
      AchievableProxiesResolver.resolve(deed);
      DeedDisplayPanelController deedPanel=new DeedDisplayPanelController(_parent,deed);
      return deedPanel;
    }
    return null;
  }
}
