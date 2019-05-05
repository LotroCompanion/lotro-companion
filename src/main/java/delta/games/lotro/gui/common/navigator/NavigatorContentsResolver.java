package delta.games.lotro.gui.common.navigator;

import delta.common.utils.NumericTools;
import delta.games.lotro.gui.deed.form.DeedDisplayPanelController;
import delta.games.lotro.gui.quests.form.QuestDisplayPanelController;
import delta.games.lotro.lore.deeds.DeedDescription;
import delta.games.lotro.lore.deeds.DeedsManager;
import delta.games.lotro.lore.quests.AchievableProxiesResolver;
import delta.games.lotro.lore.quests.QuestDescription;
import delta.games.lotro.lore.quests.QuestsManager;

/**
 * Resolver for navigable contents.
 * @author DAM
 */
public class NavigatorContentsResolver
{
  private NavigatorWindowController _parent;

  /**
   * Constructor.
   * @param parent Parent window.
   */
  public NavigatorContentsResolver(NavigatorWindowController parent)
  {
    _parent=parent;
  }

  /**
   * Build a panel controller for the given object reference.
   * @param reference Object reference.
   * @return A controller or <code>null</code> if not supported.
   */
  public NavigablePanelController resolveReference(String reference)
  {
    NavigablePanelController ret=null;
    if (reference.startsWith(ReferenceConstants.DEED_SEED))
    {
      String idStr=reference.substring(ReferenceConstants.DEED_SEED.length());
      int id=NumericTools.parseInt(idStr,0);
      ret=buildDeedPanel(id);
    }
    else if (reference.startsWith(ReferenceConstants.QUEST_SEED))
    {
      String idStr=reference.substring(ReferenceConstants.QUEST_SEED.length());
      int id=NumericTools.parseInt(idStr,0);
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
