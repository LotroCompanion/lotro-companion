package delta.games.lotro.gui.common.navigator;

import delta.games.lotro.lore.deeds.DeedDescription;
import delta.games.lotro.lore.quests.Achievable;
import delta.games.lotro.lore.quests.QuestDescription;
import delta.games.lotro.utils.Proxy;

/**
 * Facilities related to object references.
 * @author DAM
 */
public class ReferenceConstants
{
  /**
   * Seed for quest display panel.
   */
  public static final String QUEST_SEED="QUEST:";
  /**
   * Seed for deed display panel.
   */
  public static final String DEED_SEED="DEED:";

  /**
   * Get a reference string for the given achievable proxy.
   * @param proxy Proxy to use.
   * @return A reference string.
   */
  public static final String getAchievableReference(Proxy<? extends Achievable> proxy)
  {
    if (proxy!=null)
    {
      Achievable achievable=proxy.getObject();
      return getAchievableReference(achievable);
    }
    return null;
  }

  /**
   * Get a reference string for the given achievable.
   * @param achievable Achievable to use.
   * @return A reference string.
   */
  public static final String getAchievableReference(Achievable achievable)
  {
    int id=achievable.getIdentifier();
    if (achievable instanceof DeedDescription)
    {
      return DEED_SEED+id;
    }
    if (achievable instanceof QuestDescription)
    {
      return QUEST_SEED+id;
    }
    return null;
  }
}
