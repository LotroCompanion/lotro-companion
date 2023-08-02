package delta.games.lotro.gui.utils;

import javax.swing.Icon;

import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.races.RaceDescription;
import delta.games.lotro.character.skills.SkillDescription;
import delta.games.lotro.character.traits.TraitDescription;
import delta.games.lotro.character.virtues.VirtueDescription;
import delta.games.lotro.common.Genders;
import delta.games.lotro.gui.LotroIconsManager;
import delta.games.lotro.gui.common.navigation.ReferenceConstants;
import delta.games.lotro.gui.lore.items.legendary.relics.RelicUiTools;
import delta.games.lotro.gui.utils.items.ItemIconController;
import delta.games.lotro.lore.emotes.EmoteDescription;
import delta.games.lotro.lore.hobbies.HobbyDescription;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.legendary.relics.Relic;
import delta.games.lotro.lore.titles.TitleDescription;
import delta.games.lotro.utils.strings.ContextRendering;

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
  public static ItemIconController buildItemIcon(WindowController parent, Item item, int count)
  {
    ItemIconController ret=new ItemIconController(parent);
    ret.setItem(item,count);
    return ret;
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

  /**
   * Build a virtue icon.
   * @param parent Parent window.
   * @param virtue Virtue to use.
   * @param count Count.
   * @return A new controller.
   */
  public static IconController buildVirtueIcon(WindowController parent, VirtueDescription virtue, int count)
  {
    IconController ret=new IconController(parent);
    if (virtue!=null)
    {
      Icon virtueIcon=LotroIconsManager.getVirtueIcon(virtue,count);
      ret.setIcon(virtueIcon);
      ret.setPageId(ReferenceConstants.getVirtueReference(virtue.getIdentifier()));
      ret.setTooltipText(virtue.getName());
    }
    return ret;
  }

  /**
   * Build an emote icon.
   * @param parent Parent window.
   * @param emote Emote to use.
   * @return A new controller.
   */
  public static IconController buildEmoteIcon(WindowController parent, EmoteDescription emote)
  {
    IconController ret=new IconController(parent);
    if (emote!=null)
    {
      Icon icon=LotroIconsManager.getEmoteIcon(emote.getIconId());
      ret.setIcon(icon);
      ret.setPageId(ReferenceConstants.getEmoteReference(emote.getIdentifier()));
      ret.setTooltipText(emote.getName());
    }
    return ret;
  }

  /**
   * Build a hobby icon.
   * @param parent Parent window.
   * @param hobby Hobby to use.
   * @return A new controller.
   */
  public static IconController buildHobbyIcon(WindowController parent, HobbyDescription hobby)
  {
    IconController ret=new IconController(parent);
    if (hobby!=null)
    {
      Icon icon=LotroIconsManager.getHobbyIcon(hobby.getIconId());
      ret.setIcon(icon);
      ret.setPageId(ReferenceConstants.getHobbyReference(hobby.getIdentifier()));
      ret.setTooltipText(hobby.getName());
    }
    return ret;
  }

  /**
   * Build a race icon.
   * @param parent Parent window.
   * @param race Race to use.
   * @return A new controller.
   */
  public static IconController buildRaceIcon(WindowController parent, RaceDescription race)
  {
    IconController ret=new IconController(parent);
    if (race!=null)
    {
      Icon icon=LotroIconsManager.getCharacterIcon(race,Genders.MALE);
      ret.setIcon(icon);
      ret.setPageId(ReferenceConstants.getRaceReference(race));
      ret.setTooltipText(race.getName());
    }
    return ret;
  }

  /**
   * Build a title icon.
   * @param parent Parent window.
   * @param title Title to use.
   * @return A new controller.
   */
  public static IconController buildTitleIcon(WindowController parent, TitleDescription title)
  {
    IconController ret=new IconController(parent);
    if (title!=null)
    {
      Icon icon=LotroIconsManager.getTitleIcon(title.getIconId());
      ret.setIcon(icon);
      ret.setPageId(ReferenceConstants.getTitleReference(title.getIdentifier()));
      String rawTitleName=title.getName();
      String titleName=ContextRendering.render(parent,rawTitleName);
      ret.setTooltipText(titleName);
    }
    return ret;
  }
}
