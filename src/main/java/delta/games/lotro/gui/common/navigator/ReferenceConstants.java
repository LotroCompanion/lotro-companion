package delta.games.lotro.gui.common.navigator;

import delta.common.ui.swing.navigator.PageIdentifier;
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
   * Quest page identifier.
   */
  public static final String QUEST_PAGE="quest";
  /**
   * Deed page identifier.
   */
  public static final String DEED_PAGE="deed";

  /**
   * Get a page identifier for the given achievable proxy.
   * @param proxy Proxy to use.
   * @return A page identifier.
   */
  public static final PageIdentifier getAchievableReference(Proxy<? extends Achievable> proxy)
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
  public static final PageIdentifier getAchievableReference(Achievable achievable)
  {
    int id=achievable.getIdentifier();
    if (achievable instanceof DeedDescription)
    {
      return new PageIdentifier(DEED_PAGE,id);
    }
    if (achievable instanceof QuestDescription)
    {
      return new PageIdentifier(QUEST_PAGE,id);
    }
    return null;
  }
}
