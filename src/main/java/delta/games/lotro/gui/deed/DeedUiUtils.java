package delta.games.lotro.gui.deed;

import java.util.List;

import delta.common.ui.swing.combobox.ComboBoxController;
import delta.games.lotro.common.VirtueId;
import delta.games.lotro.lore.deeds.DeedType;
import delta.games.lotro.lore.reputation.Faction;
import delta.games.lotro.lore.reputation.FactionsRegistry;

/**
 * Utility methods for deed-related UIs.
 * @author DAM
 */
public class DeedUiUtils
{
  /**
   * Build a combo-box controller to choose a deed category.
   * @return A new combo-box controller.
   */
  public static ComboBoxController<String> buildCategoryCombo()
  {
    ComboBoxController<String> ctrl=new ComboBoxController<String>();
    ctrl.addEmptyItem("");
    List<String> categories=DeedUtils.getCategories();
    for(String category : categories)
    {
      ctrl.addItem(category,category);
    }
    ctrl.selectItem(null);
    return ctrl;
  }

  /**
   * Build a combo-box controller to choose a faction.
   * @return A new combo-box controller.
   */
  public static ComboBoxController<Faction> buildFactionCombo()
  {
    ComboBoxController<Faction> ctrl=new ComboBoxController<Faction>();
    ctrl.addEmptyItem("");
    List<Faction> factions=FactionsRegistry.getInstance().getAll();
    for(Faction faction : factions)
    {
      ctrl.addItem(faction,faction.getName());
    }
    ctrl.selectItem(null);
    return ctrl;
  }

  /**
   * Build a combo-box controller to choose a trait.
   * @return A new combo-box controller.
   */
  public static ComboBoxController<String> buildTraitsCombo()
  {
    ComboBoxController<String> ctrl=new ComboBoxController<String>();
    ctrl.addEmptyItem("");
    List<String> traits=DeedUtils.getTraits();
    for(String trait : traits)
    {
      ctrl.addItem(trait,trait);
    }
    ctrl.selectItem(null);
    return ctrl;
  }

  /**
   * Build a combo-box controller to choose a skill.
   * @return A new combo-box controller.
   */
  public static ComboBoxController<String> buildSkillsCombo()
  {
    ComboBoxController<String> ctrl=new ComboBoxController<String>();
    ctrl.addEmptyItem("");
    List<String> skills=DeedUtils.getSkills();
    for(String skill : skills)
    {
      ctrl.addItem(skill,skill);
    }
    ctrl.selectItem(null);
    return ctrl;
  }

  /**
   * Build a combo-box controller to choose a title.
   * @return A new combo-box controller.
   */
  public static ComboBoxController<String> buildTitlesCombo()
  {
    ComboBoxController<String> ctrl=new ComboBoxController<String>();
    ctrl.addEmptyItem("");
    List<String> titles=DeedUtils.getTitles();
    for(String title : titles)
    {
      ctrl.addItem(title,title);
    }
    ctrl.selectItem(null);
    return ctrl;
  }

  /**
   * Build a combo-box controller to choose a virtue.
   * @return A new combo-box controller.
   */
  public static ComboBoxController<VirtueId> buildVirtueCombo()
  {
    ComboBoxController<VirtueId> ctrl=new ComboBoxController<VirtueId>();
    ctrl.addEmptyItem("");
    for(VirtueId virtue : VirtueId.values())
    {
      ctrl.addItem(virtue,virtue.getLabel());
    }
    ctrl.selectItem(null);
    return ctrl;
  }

  /**
   * Build a combo-box controller to choose an emote.
   * @return A new combo-box controller.
   */
  public static ComboBoxController<String> buildEmotesCombo()
  {
    ComboBoxController<String> ctrl=new ComboBoxController<String>();
    ctrl.addEmptyItem("");
    List<String> emotes=DeedUtils.getEmotes();
    for(String emote : emotes)
    {
      ctrl.addItem(emote,emote);
    }
    ctrl.selectItem(null);
    return ctrl;
  }

  /**
   * Build a combo-box controller to choose an item name.
   * @return A new combo-box controller.
   */
  public static ComboBoxController<String> buildItemsCombo()
  {
    ComboBoxController<String> ctrl=new ComboBoxController<String>();
    ctrl.addEmptyItem("");
    List<String> itemNames=DeedUtils.getItemNames();
    for(String itemName : itemNames)
    {
      ctrl.addItem(itemName,itemName);
    }
    ctrl.selectItem(null);
    return ctrl;
  }

  /**
   * Build a combo-box controller to choose a deed type.
   * @return A new combo-box controller.
   */
  public static  ComboBoxController<DeedType> buildDeedTypeCombo()
  {
    ComboBoxController<DeedType> ctrl=new ComboBoxController<DeedType>();
    ctrl.addEmptyItem("");
    for(DeedType deedType : DeedType.values())
    {
      ctrl.addItem(deedType,deedType.name());// TODO Label
    }
    ctrl.selectItem(null);
    return ctrl;
  }
}
