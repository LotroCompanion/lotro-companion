package delta.games.lotro.gui.utils;

import javax.swing.Icon;

import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.skills.SkillDescription;
import delta.games.lotro.character.traits.TraitDescription;
import delta.games.lotro.gui.LotroIconsManager;
import delta.games.lotro.gui.common.navigation.ReferenceConstants;
import delta.games.lotro.gui.lore.items.ItemUiTools;
import delta.games.lotro.gui.lore.items.legendary.relics.RelicUiTools;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.legendary.relics.Relic;

/**
 * Factory of icon controllers.
 * @author DAM
 */
public class IconControllerFactory
{
  /**
   * Build an item icon.
   * @param parent Parent window.
   * @param item Item to use.
   * @param count Count.
   * @return A new controller.
   */
  public static IconController buildItemIcon(WindowController parent, Item item, int count)
  {
    IconController ret=new IconController(parent);
    if (item!=null)
    {
      updateItemIcon(ret,item,count);
    }
    else
    {
      ret.clear(LotroIconsManager.getDefaultItemIcon());
    }
    return ret;
  }

  /**
   * Update an item icon.
   * @param iconController Targeted controller.
   * @param item Item to show.
   * @param count Count.
   */
  public static void updateItemIcon(IconController iconController, Item item, int count)
  {
    Icon icon=ItemUiTools.buildItemIcon(item,count);
    iconController.setIcon(icon);
    iconController.setPageId(ReferenceConstants.getItemReference(item.getIdentifier()));
    if (item!=null)
    {
      iconController.setTooltipText(item.getName());
    }
  }

  /**
   * Build a relic icon.
   * @param parent Parent window.
   * @param relic Relic to use.
   * @param count Count.
   * @return A new controller.
   */
  public static IconController buildRelicIcon(WindowController parent, Relic relic, int count)
  {
    IconController ret=new IconController(parent);
    if (relic!=null)
    {
      Icon icon=RelicUiTools.buildRelicIcon(relic,count);
      ret.setIcon(icon);
      ret.setPageId(ReferenceConstants.getRelicReference(relic.getIdentifier()));
      ret.setTooltipText(relic.getName());
    }
    return ret;
  }

  /**
   * Build a skill icon.
   * @param parent Parent window.
   * @param skill Skill to use.
   * @return A new controller.
   */
  public static IconController buildSkillIcon(WindowController parent, SkillDescription skill)
  {
    IconController ret=new IconController(parent);
    if (skill!=null)
    {
      Icon icon=LotroIconsManager.getSkillIcon(skill.getIconId());
      ret.setIcon(icon);
      ret.setPageId(ReferenceConstants.getSkillReference(skill.getIdentifier()));
      ret.setTooltipText(skill.getName());
    }
    return ret;
  }

  /**
   * Build a trait icon.
   * @param parent Parent window.
   * @param trait Trait to use.
   * @return A new controller.
   */
  public static IconController buildTraitIcon(WindowController parent, TraitDescription trait)
  {
    IconController ret=new IconController(parent);
    if (trait!=null)
    {
      Icon icon=LotroIconsManager.getTraitIcon(trait.getIconId());
      ret.setIcon(icon);
      ret.setPageId(ReferenceConstants.getTraitReference(trait.getIdentifier()));
      ret.setTooltipText(trait.getName());
    }
    return ret;
  }
}
