package delta.games.lotro.gui.deed;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import delta.games.lotro.lore.deeds.DeedDescription;
import delta.games.lotro.lore.deeds.DeedRewardsExplorer;
import delta.games.lotro.lore.deeds.DeedsManager;
import delta.games.lotro.lore.items.Item;

/**
 * Deed-related utility methods.
 * @author DAM
 */
public class DeedUtils
{
  /**
   * Load available categories from deeds manager.
   * @return A sorted list of deed categories.
   */
  public static List<String> getCategories()
  {
    Set<String> categories=new HashSet<String>(); 
    List<DeedDescription> deeds=DeedsManager.getInstance().getAll();
    for(DeedDescription deed : deeds)
    {
      categories.add(deed.getCategory());
    }
    List<String> ret=new ArrayList<String>(categories);
    ret.remove(null);
    Collections.sort(ret);
    return ret;
  }

  /**
   * Load available traits from deeds manager.
   * @return A sorted list of traits.
   */
  public static List<String> getTraits()
  {
    DeedRewardsExplorer rewardsExplorer=DeedsManager.getInstance().getRewardsExplorer();
    return rewardsExplorer.getTraits();
  }

  /**
   * Load available skills from deeds manager.
   * @return A sorted list of skills.
   */
  public static List<String> getSkills()
  {
    DeedRewardsExplorer rewardsExplorer=DeedsManager.getInstance().getRewardsExplorer();
    return rewardsExplorer.getSkills();
  }

  /**
   * Load available titles from deeds manager.
   * @return A sorted list of titles.
   */
  public static List<String> getTitles()
  {
    DeedRewardsExplorer rewardsExplorer=DeedsManager.getInstance().getRewardsExplorer();
    return rewardsExplorer.getTitles();
  }

  /**
   * Load available emotes from deeds manager.
   * @return A sorted list of emotes.
   */
  public static List<String> getEmotes()
  {
    DeedRewardsExplorer rewardsExplorer=DeedsManager.getInstance().getRewardsExplorer();
    return rewardsExplorer.getEmotes();
  }

  /**
   * Load available items from deeds manager.
   * @return A list of items, sorted by name.
   */
  public static List<Item> getItems()
  {
    DeedRewardsExplorer rewardsExplorer=DeedsManager.getInstance().getRewardsExplorer();
    return rewardsExplorer.getItems();
  }
}
