package delta.games.lotro.gui.common.navigation;

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
   * Item page identifier.
   */
  public static final String ITEM_PAGE="item";
  /**
   * Recipe page identifier.
   */
  public static final String RECIPE_PAGE="recipe";
  /**
   * Barterer page identifier.
   */
  public static final String BARTERER_PAGE="barterer";
  /**
   * Vendor page identifier.
   */
  public static final String VENDOR_PAGE="vendor";

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

  /**
   * Get a page identifier for the given item.
   * @param itemId Identifier of the item to use.
   * @return A page identifier.
   */
  public static final PageIdentifier getItemReference(int itemId)
  {
    return new PageIdentifier(ITEM_PAGE,itemId);
  }

  /**
   * Get a page identifier for the given recipe.
   * @param recipeId Identifier of the recipe to use.
   * @return A page identifier.
   */
  public static final PageIdentifier getRecipeReference(int recipeId)
  {
    return new PageIdentifier(RECIPE_PAGE,recipeId);
  }

  /**
   * Get a page identifier for the given barterer.
   * @param bartererId Identifier of the barterer to use.
   * @return A page identifier.
   */
  public static final PageIdentifier getBartererReference(int bartererId)
  {
    return new PageIdentifier(BARTERER_PAGE,bartererId);
  }

  /**
   * Get a page identifier for the given vendor.
   * @param vendorId Identifier of the vendor to use.
   * @return A page identifier.
   */
  public static final PageIdentifier getVendorReference(int vendorId)
  {
    return new PageIdentifier(VENDOR_PAGE,vendorId);
  }
}
