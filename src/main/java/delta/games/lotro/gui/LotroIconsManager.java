package delta.games.lotro.gui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

import delta.common.ui.swing.icons.ApplicationIcons;
import delta.common.ui.swing.icons.IconsManager;
import delta.games.lotro.character.virtues.VirtueDescription;
import delta.games.lotro.common.CharacterClass;
import delta.games.lotro.common.CharacterSex;
import delta.games.lotro.common.Race;
import delta.games.lotro.common.stats.StatDescription;
import delta.games.lotro.lore.crafting.Profession;
import delta.games.lotro.lore.deeds.DeedType;

/**
 * Access to Lotro specific icons.
 * @author DAM
 */
public class LotroIconsManager
{
  /**
   * Compact size.
   */
  public static final String COMPACT_SIZE="compact";
  /**
   * Small size.
   */
  public static final String SMALL_SIZE="small";

  /**
   * Get an icon for a character class.
   * @param cClass Character class.
   * @param size Icon size. Use <code>MEDIUM</code> or <code>SMALL</code>.
   * @return An icon or <code>null</code> if not found.
   */
  public static ImageIcon getClassIcon(CharacterClass cClass, String size)
  {
    ImageIcon ret=null;
    if (cClass!=null)
    {
      String classIconPath=cClass.getIconPath();
      String iconPath="/resources/gui/classes/"+size+"/"+classIconPath+".png";
      ret=IconsManager.getIcon(iconPath);
    }
    return ret;
  }

  /**
   * Get an icon for a character race/sex.
   * @param race Character race.
   * @param sex Character sex.
   * @return An icon or <code>null</code> if not found.
   */
  public static ImageIcon getCharacterIcon(Race race, CharacterSex sex)
  {
    ImageIcon ret=null;
    if (race!=null)
    {
      if (sex==null)
      {
        sex=CharacterSex.MALE;
      }
      String iconPath=race.getIconPath()+"_"+sex.getKey().toLowerCase();
      ret=IconsManager.getIcon("/resources/gui/races/"+iconPath+".png");
    }
    return ret;
  }

  /**
   * Get the icon for an item.
   * @param icon Icon path.
   * @return An icon or <code>null</code> if not found.
   */
  public static ImageIcon getItemIcon(String icon)
  {
    return IconsManager.getIcon("/icons/"+icon+".png");
  }

  /**
   * Get the icon for a title.
   * @param titleId Title identifier.
   * @return An icon or <code>null</code> if not found.
   */
  public static ImageIcon getTitleIcon(int titleId)
  {
    String path="/titleIcons/"+titleId+".png";
    return IconsManager.getIcon(path);
  }

  /**
   * Get the icon for a emote.
   * @param iconFilename Filename of the emote icon.
   * @return An icon or <code>null</code> if not found.
   */
  public static ImageIcon getEmoteIcon(String iconFilename)
  {
    String path="/emoteIcons/"+iconFilename;
    return IconsManager.getIcon(path);
  }

  /**
   * Get the icon for a relic.
   * @param iconFilename Filename of the relic icon.
   * @return An icon or <code>null</code> if not found.
   */
  public static ImageIcon getRelicIcon(String iconFilename)
  {
    String path="/relicIcons/"+iconFilename;
    return IconsManager.getIcon(path);
  }

  /**
   * Get the icon for a buff.
   * @param iconFilename Filename of the buff icon.
   * @return An icon or <code>null</code> if not found.
   */
  public static ImageIcon getBuffIcon(String iconFilename)
  {
    if (iconFilename.startsWith("/traitIcons"))
    {
      // Trait icon
      return IconsManager.getIcon(iconFilename);
    }
    if (iconFilename.startsWith("/effectIcons"))
    {
      // Effect icon
      return IconsManager.getIcon(iconFilename);
    }
    if (iconFilename.startsWith("/icons"))
    {
      // Item icon
      return IconsManager.getIcon(iconFilename);
    }
    String path="/resources/gui/buffs/"+iconFilename+".png";
    return IconsManager.getIcon(path);
  }

  /**
   * Get the icon for a trait.
   * @param traitIconId Trait icon identifier.
   * @return An icon or <code>null</code> if not found.
   */
  public static ImageIcon getTraitIcon(int traitIconId)
  {
    String path="/traitIcons/"+traitIconId+".png";
    return IconsManager.getIcon(path);
  }

  /**
   * Get the icon for a stat tome.
   * @param stat Targeted stat.
   * @return An icon or <code>null</code> if not found.
   */
  public static ImageIcon getTomeIcon(StatDescription stat)
  {
    String path="/resources/gui/tomes/"+stat.getPersistenceKey().toLowerCase()+".png";
    return IconsManager.getIcon(path);
  }

  /**
   * Get icon for a virtue.
   * @param virtue Virtue.
   * @return An icon.
   */
  public static ImageIcon getVirtueIcon(VirtueDescription virtue)
  {
    if (virtue!=null)
    {
      int virtueIconId=virtue.getIconId();
      String path="/virtueIcons/"+virtueIconId+".png";
      return IconsManager.getIcon(path);
    }
    return null;
  }

  /**
   * Get the icon for a profession.
   * @param profession Profession.
   * @return An icon or <code>null</code> if not found.
   */
  public static ImageIcon getProfessionIcon(Profession profession)
  {
    String path="/resources/gui/crafting/professions/"+profession.getKey().toLowerCase()+".png";
    return IconsManager.getIcon(path);
  }

  /**
   * Get the icon for a crafting tier.
   * @param mastery Mastery or proficiency.
   * @return An icon or <code>null</code> if not found.
   */
  public static ImageIcon getCraftingTierIcon(boolean mastery)
  {
    String key=mastery?"mastery":"proficiency";
    String path="/resources/gui/crafting/"+key+".png";
    return IconsManager.getIcon(path);
  }

  /**
   * Get an icon for a deed type.
   * @param type Deed type.
   * @return An icon or <code>null</code> if not found.
   */
  public static ImageIcon getDeedTypeIcon(DeedType type)
  {
    String filename=type.name().toLowerCase();
    String path="/resources/gui/deeds/"+filename+".png";
    ImageIcon ret=IconsManager.getIcon(path);
    return ret;
  }

  /**
   * Get the icon for a given legacy tier and type.
   * @param tier Legacy tier.
   * @param major Legacy type (major or minor).
   * @return An icon or <code>null</code> if not found.
   */
  public static ImageIcon getLegacyIcon(int tier, boolean major)
  {
    String path="/resources/gui/legendary/Legacy_"+(major?"Major":"Minor")+"_Tier_"+tier+"-icon.png";
    ImageIcon ret=IconsManager.getIcon(path);
    return ret;
  }

  /**
   * Get the icon for a given legacy.
   * @param legacyIconId Icon identifier.
   * @return An icon or <code>null</code> if not found.
   */
  public static ImageIcon getLegacyIcon(int legacyIconId)
  {
    String path="/legaciesIcons/"+legacyIconId+".png";
    ImageIcon ret=IconsManager.getIcon(path);
    return ret;
  }

  /**
   * Initialize the application icons.
   */
  public static void initApplicationIcons()
  {
    List<String> iconPaths=new ArrayList<String>();
    int[] sizes={16,32,48,256};
    for(int size : sizes)
    {
      String iconPath="/resources/gui/ring/ring"+size+".png";
      iconPaths.add(iconPath);
    }
    ApplicationIcons.setApplicationIconPaths(iconPaths);
  }
}
