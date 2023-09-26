package delta.games.lotro.gui;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import delta.common.ui.swing.icons.ApplicationIcons;
import delta.common.ui.swing.icons.IconWithText;
import delta.common.ui.swing.icons.IconsManager;
import delta.games.lotro.character.races.RaceDescription;
import delta.games.lotro.character.races.RaceGender;
import delta.games.lotro.character.virtues.VirtueDescription;
import delta.games.lotro.common.CharacterSex;
import delta.games.lotro.common.Genders;
import delta.games.lotro.common.stats.StatDescription;
import delta.games.lotro.config.LotroCoreConfig;
import delta.games.lotro.gui.utils.icons.ItemIconBuilder;
import delta.games.lotro.gui.utils.icons.SocketIconBuilder;
import delta.games.lotro.lore.crafting.Profession;
import delta.games.lotro.lore.deeds.DeedType;
import delta.games.lotro.utils.IconsUtils;

/**
 * Access to Lotro specific icons.
 * @author DAM
 */
public class LotroIconsManager
{
  private static final String ITEM_WITH_NO_ICON="/resources/gui/items/itemNoIcon.png";

  private static ItemIconBuilder _itemIconBuilder=new ItemIconBuilder();

  /**
   * Get an icon for a character class.
   * @param iconID Icon identifier.
   * @return An icon or <code>null</code> if not found.
   */
  public static ImageIcon getClassIcon(int iconID)
  {
    String iconPath="/classes/"+iconID+".png";
    ImageIcon ret=IconsManager.getIcon(iconPath);
    return ret;
  }

  /**
   * Get an icon for a character race/sex.
   * @param race Character race.
   * @param sex Character sex.
   * @return An icon or <code>null</code> if not found.
   */
  public static ImageIcon getCharacterIcon(RaceDescription race, CharacterSex sex)
  {
    ImageIcon ret=null;
    if (race!=null)
    {
      if (sex==null)
      {
        sex=Genders.MALE;
      }
      RaceGender gender=(sex==Genders.MALE)?race.getMaleGender():race.getFemaleGender();
      if (gender!=null)
      {
        int iconID=gender.getLargeIconId();
        ret=IconsManager.getIcon("/races/"+iconID+".png");
      }
    }
    return ret;
  }

  /**
   * Get the icon for an item.
   * @param iconPath Icon path.
   * @return An icon or <code>null</code> if not found.
   */
  public static ImageIcon getItemIcon(String iconPath)
  {
    ImageIcon icon=null;
    if (iconPath!=null)
    {
      BufferedImage itemImage=_itemIconBuilder.getItemIcon(iconPath);
      if (itemImage!=null)
      {
        icon=new ImageIcon(itemImage);
      }
    }
    if (icon==null)
    {
      icon=getDefaultItemIcon();
    }
    return icon;
  }

  /**
   * Get the large icon for an item.
   * @param largeIconID Icon identifier.
   * @return An icon or <code>null</code> if not found.
   */
  public static ImageIcon getLargeItemIcon(int largeIconID)
  {
    String path="/largeItems/"+largeIconID+".png";
    return IconsManager.getIcon(path);
  }

  /**
   * Get the default icon for items.
   * @return An icon.
   */
  public static ImageIcon getDefaultItemIcon()
  {
    return IconsManager.getIcon(ITEM_WITH_NO_ICON);
  }

  /**
   * Get the default icon for essences.
   * @param socketType Socket type code.
   * @return An icon.
   */
  public static ImageIcon getDefaultEssenceIcon(int socketType)
  {
    return getSocketBackgroundIcon(socketType);
  }

  /**
   * Get the background icon for a given socket type.
   * @param socketType Socket type code.
   * @return An icon.
   */
  private static ImageIcon getSocketBackgroundIcon(int socketType)
  {
    String path="/sockets/background-"+socketType+".png";
    return IconsManager.getIcon(path);
  }

  /**
   * Get the icon for an empty socket icon.
   * @param socketType Socket type.
   * @return An icon or <code>null</code> if not found.
   */
  public static ImageIcon getEmptySocketIcon(int socketType)
  {
    ImageIcon icon=null;
    BufferedImage image=SocketIconBuilder.getEmptySocketIcon(socketType);
    if (image!=null)
    {
      icon=new ImageIcon(image);
    }
    return icon;
  }

  /**
   * Get the icon for a title.
   * @param iconID Title icon identifier.
   * @return An icon or <code>null</code> if not found.
   */
  public static ImageIcon getTitleIcon(int iconID)
  {
    String path="/titleIcons/"+iconID+".png";
    return IconsManager.getIcon(path);
  }

  /**
   * Get the icon for a emote.
   * @param iconID Icon identifier.
   * @return An icon or <code>null</code> if not found.
   */
  public static ImageIcon getEmoteIcon(int iconID)
  {
    String path="/emotes/"+iconID+".png";
    return IconsManager.getIcon(path);
  }

  /**
   * Get the icon for a relic.
   * @param iconFilename Filename of the relic icon.
   * @return An icon or <code>null</code> if not found.
   */
  public static ImageIcon getRelicIcon(String iconFilename)
  {
    String path="/relics/"+iconFilename;
    return IconsManager.getIcon(path);
  }

  /**
   * Get the icon for a buff.
   * @param iconFilename Filename of the buff icon.
   * @return An icon or <code>null</code> if not found.
   */
  public static ImageIcon getBuffIcon(String iconFilename)
  {
    if (iconFilename.startsWith("/traits"))
    {
      // Trait icon
      return IconsManager.getIcon(iconFilename);
    }
    if (iconFilename.startsWith("/effectIcons"))
    {
      // Effect icon
      return IconsManager.getIcon(iconFilename);
    }
    if (iconFilename.startsWith(IconsUtils.ITEM_ICON_PREFIX))
    {
      return getItemIcon(iconFilename.substring(IconsUtils.ITEM_ICON_PREFIX.length()));
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
    String path="/traits/"+traitIconId+".png";
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
  public static Icon getVirtueIcon(VirtueDescription virtue)
  {
    if (virtue==null)
    {
      return null;
    }
    boolean isLive=LotroCoreConfig.isLive();
    if (isLive)
    {
      int virtueIconId=virtue.getIconId();
      String path="/traits/"+virtueIconId+".png";
      return IconsManager.getIcon(path);
    }
    return getVirtueIcon(virtue,1);
  }

  /**
   * Get icon for a virtue.
   * @param virtue Virtue.
   * @param count Count.
   * @return An icon.
   */
  public static Icon getVirtueIcon(VirtueDescription virtue, int count)
  {
    if (virtue==null)
    {
      return null;
    }
    Icon virtueIcon=null;
    boolean isLive=LotroCoreConfig.isLive();
    if (isLive)
    {
      virtueIcon=LotroIconsManager.getVirtueIcon(virtue);
      if (count>0)
      {
        virtueIcon=new IconWithText(virtueIcon,String.valueOf(count),Color.WHITE);
      }
    }
    else
    {
      String virtueKey=virtue.getKey();
      String path="/traits/"+virtueKey+"-"+count+".png";
      virtueIcon=IconsManager.getIcon(path);
    }
    return virtueIcon;
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
    String filename=type.getKey().toLowerCase();
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
   * Get the icon for a mount.
   * @param mountId Mount identifier.
   * @return An icon or <code>null</code> if not found.
   */
  public static ImageIcon getMountIcon(int mountId)
  {
    return getSkillIcon(mountId);
  }

  /**
   * Get the icon for a pet.
   * @param petId Pet identifier.
   * @return An icon or <code>null</code> if not found.
   */
  public static ImageIcon getPetIcon(int petId)
  {
    return getSkillIcon(petId);
  }

  /**
   * Get the icon for a given allegiance.
   * @param iconId Icon identifier.
   * @return An icon or <code>null</code> if not found.
   */
  public static BufferedImage getAllegianceImage(int iconId)
  {
    String path="/allegianceIcons/"+iconId+".png";
    BufferedImage image=IconsManager.getImage(path);
    return image;
  }

  /**
   * Get the icon for a skill.
   * @param skillIconId Skill icon identifier.
   * @return An icon or <code>null</code> if not found.
   */
  public static ImageIcon getSkillIcon(int skillIconId)
  {
    String path="/skills/"+skillIconId+".png";
    return IconsManager.getIcon(path);
  }

  /**
   * Get the icon for an area.
   * @param areaId Area identifier.
   * @return An icon or <code>null</code> if not found.
   */
  public static ImageIcon getAreaIcon(int areaId)
  {
    String path="/areaIcons/"+areaId+".png";
    return IconsManager.getIcon(path);
  }

  /**
   * Get the icon for a hobby.
   * @param hobbyIconId Hobby icon identifier.
   * @return An icon or <code>null</code> if not found.
   */
  public static ImageIcon getHobbyIcon(int hobbyIconId)
  {
    String path="/hobbyIcons/"+hobbyIconId+".png";
    return IconsManager.getIcon(path);
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
