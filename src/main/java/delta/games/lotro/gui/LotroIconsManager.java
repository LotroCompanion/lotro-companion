package delta.games.lotro.gui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

import delta.common.ui.swing.icons.ApplicationIcons;
import delta.common.ui.swing.icons.IconsManager;
import delta.games.lotro.character.stats.STAT;
import delta.games.lotro.common.CharacterClass;
import delta.games.lotro.common.Race;

/**
 * Access to Lotro specific icons.
 * @author DAM
 */
public class LotroIconsManager
{
  /**
   * Medium size.
   */
  public static final String MEDIUM_SIZE="medium";
  /**
   * Small size.
   */
  public static final String SMALL_SIZE="small";

  /**
   * Get an icon for a character class.
   * @param cClass Character class.
   * @param size Icon size. Use <code>MEDIUM</code> or <code>SMALL</code>.
   * @return An icon or <code>null/code> if not found.
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
   * Get an icon for a character race.
   * @param race Character race.
   * @return An icon or <code>null/code> if not found.
   */
  public static ImageIcon getRaceIcon(Race race)
  {
    ImageIcon ret=null;
    if (race!=null)
    {
      String classIconPath=race.getIconPath();
      String iconPath="/resources/gui/races/"+classIconPath+".png";
      ret=IconsManager.getIcon(iconPath);
    }
    return ret;
  }

  /**
   * Get the icon for an item.
   * @param iconId Foreground icon ID.
   * @param backgroundId Background icon ID.
   * @return An icon or <code>null</code> if not found.
   */
  public static ImageIcon getItemIcon(String iconId, String backgroundId)
  {
    String path="/icons/"+iconId+"-"+backgroundId+".png";
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
   * @param iconFilename Filename of the relic icon.
   * @return An icon or <code>null</code> if not found.
   */
  public static ImageIcon getBuffIcon(String iconFilename)
  {
    String path="/resources/gui/buffs/"+iconFilename+".png";
    return IconsManager.getIcon(path);
  }

  /**
   * Get the icon for a stat tome.
   * @param stat Targeted stat.
   * @return An icon or <code>null</code> if not found.
   */
  public static ImageIcon getTomeIcon(STAT stat)
  {
    String path="/resources/gui/tomes/"+stat.getKey().toLowerCase()+".png";
    return IconsManager.getIcon(path);
  }

  /**
   * Get icon for a virtue.
   * @param virtueId Virtue identifier.
   * @return An icon.
   */
  public static ImageIcon getVirtueIcon(String virtueId)
  {
    String path="/resources/gui/virtues/"+virtueId.toLowerCase()+".png";
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