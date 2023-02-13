package delta.games.lotro.gui.common.navigation;

import delta.common.ui.swing.navigator.PageIdentifier;
import delta.games.lotro.character.classes.ClassDescription;
import delta.games.lotro.character.races.NationalityDescription;
import delta.games.lotro.character.races.RaceDescription;
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
   * Items set page identifier.
   */
  public static final String ITEMS_SET_PAGE="itemsSet";
  /**
   * Relic page identifier.
   */
  public static final String RELIC_PAGE="relic";
  /**
   * Recipe page identifier.
   */
  public static final String RECIPE_PAGE="recipe";
  /**
   * Barterer page identifier.
   */
  public static final String BARTERER_PAGE="barterer";
  /**
   * Barter entry page identifier.
   */
  public static final String BARTER_ENTRY_PAGE="barterEntry";
  /**
   * Vendor page identifier.
   */
  public static final String VENDOR_PAGE="vendor";
  /**
   * Vendor entry page identifier.
   */
  public static final String VENDOR_ENTRY_PAGE="vendorEntry";
  /**
   * Relic melding recipe page identifier.
   */
  public static final String RELIC_MELDING_RECIPE_PAGE="relicMeldingRecipe";
  /**
   * Skill page identifier.
   */
  public static final String SKILL_PAGE="skill";
  /**
   * Emote page identifier.
   */
  public static final String EMOTE_PAGE="emote";
  /**
   * Class page identifier.
   */
  public static final String CLASS_PAGE="class";
  /**
   * Trait page identifier.
   */
  public static final String TRAIT_PAGE="trait";
  /**
   * Race page identifier.
   */
  public static final String RACE_PAGE="race";
  /**
   * Nationality page identifier.
   */
  public static final String NATIONALITY_PAGE="nationality";
  /**
   * Virtues page identifier.
   */
  public static final String VIRTUE_PAGE="virtue";
  /**
   * Title page identifier.
   */
  public static final String TITLE_PAGE="title";
  /**
   * Billing group page identifier.
   */
  public static final String BILLING_GROUP_PAGE="billingGroup";
  /**
   * Hobby page identifier.
   */
  public static final String HOBBY_PAGE="hobby";

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
   * Get a page identifier for the given items set.
   * @param itemsSetId Identifier of the items set to use.
   * @return A page identifier.
   */
  public static final PageIdentifier getItemsSetReference(int itemsSetId)
  {
    return new PageIdentifier(ITEMS_SET_PAGE,itemsSetId);
  }

  /**
   * Get a page identifier for the given relic.
   * @param relicId Identifier of the item to use.
   * @return A page identifier.
   */
  public static final PageIdentifier getRelicReference(int relicId)
  {
    return new PageIdentifier(RELIC_PAGE,relicId);
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
   * Get a page identifier for a barter entry.
   * @param bartererId Identifier of the barterer to use.
   * @param index Index of barter entry in this barterer.
   * @return A page identifier.
   */
  public static final PageIdentifier getBarterEntryReference(int bartererId, int index)
  {
    PageIdentifier pageId=new PageIdentifier(BARTER_ENTRY_PAGE,bartererId);
    pageId.setParameter("index",String.valueOf(index));
    return pageId;
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

  /**
   * Get a page identifier for a vendor entry.
   * @param vendorId Identifier of the vendor to use.
   * @param index Index of entry in this vendor.
   * @return A page identifier.
   */
  public static final PageIdentifier getVendorEntryReference(int vendorId, int index)
  {
    PageIdentifier pageId=new PageIdentifier(VENDOR_ENTRY_PAGE,vendorId);
    pageId.setParameter("index",String.valueOf(index));
    return pageId;
  }

  /**
   * Get a page identifier for a relic melding recipe.
   * @param recipeId Identifier of the recipe to use.
   * @return A page identifier.
   */
  public static final PageIdentifier getMeldingRecipeReference(int recipeId)
  {
    PageIdentifier pageId=new PageIdentifier(RELIC_MELDING_RECIPE_PAGE,recipeId);
    return pageId;
  }

  /**
   * Get a page identifier for the given skill.
   * @param skillID Identifier of the skill to use.
   * @return A page identifier.
   */
  public static final PageIdentifier getSkillReference(int skillID)
  {
    return new PageIdentifier(SKILL_PAGE,skillID);
  }

  /**
   * Get a page identifier for the given emote.
   * @param emoteID Identifier of the emote to use.
   * @return A page identifier.
   */
  public static final PageIdentifier getEmoteReference(int emoteID)
  {
    return new PageIdentifier(EMOTE_PAGE,emoteID);
  }

  /**
   * Get a page identifier for the given class.
   * @param characterClass Identifier of the class to use.
   * @return A page identifier.
   */
  public static final PageIdentifier getClassReference(ClassDescription characterClass)
  {
    PageIdentifier id=new PageIdentifier();
    id.setBaseAddress(CLASS_PAGE);
    id.setParameter(PageIdentifier.ID_PARAMETER,characterClass.getKey());
    return id;
  }

  /**
   * Get a page identifier for the given trait.
   * @param traitID Identifier of the trait to use.
   * @return A page identifier.
   */
  public static final PageIdentifier getTraitReference(int traitID)
  {
    return new PageIdentifier(TRAIT_PAGE,traitID);
  }

  /**
   * Get a page identifier for the given race.
   * @param race Identifier of the race to use.
   * @return A page identifier.
   */
  public static final PageIdentifier getRaceReference(RaceDescription race)
  {
    PageIdentifier id=new PageIdentifier();
    id.setBaseAddress(RACE_PAGE);
    id.setParameter(PageIdentifier.ID_PARAMETER,race.getKey());
    return id;
  }

  /**
   * Get a page identifier for the given nationality.
   * @param nationality Identifier of the nationality to use.
   * @return A page identifier.
   */
  public static final PageIdentifier getNationalityReference(NationalityDescription nationality)
  {
    return new PageIdentifier(NATIONALITY_PAGE,nationality.getIdentifier());
  }

  /**
   * Get a page identifier for the given virtue.
   * @param virtueID Identifier of the virtue to use.
   * @return A page identifier.
   */
  public static final PageIdentifier getVirtueReference(int virtueID)
  {
    return new PageIdentifier(VIRTUE_PAGE,virtueID);
  }

  /**
   * Get a page identifier for the given title.
   * @param titleID Identifier of the title to use.
   * @return A page identifier.
   */
  public static final PageIdentifier getTitleReference(int titleID)
  {
    return new PageIdentifier(TITLE_PAGE,titleID);
  }

  /**
   * Get a page identifier for the given billing group.
   * @param billingGroupID Identifier of the billing group to use.
   * @return A page identifier.
   */
  public static final PageIdentifier getBillingGroupReference(int billingGroupID)
  {
    return new PageIdentifier(BILLING_GROUP_PAGE,billingGroupID);
  }

  /**
   * Get a page identifier for the given hobby.
   * @param hobbyID Identifier of the hobby to use.
   * @return A page identifier.
   */
  public static final PageIdentifier getHobbyReference(int hobbyID)
  {
    return new PageIdentifier(HOBBY_PAGE,hobbyID);
  }
}
