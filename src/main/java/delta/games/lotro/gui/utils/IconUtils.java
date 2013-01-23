package delta.games.lotro.gui.utils;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

import delta.games.lotro.common.CharacterClass;
import delta.games.lotro.common.Race;

/**
 * Utility methods for icons management.
 * @author DAM
 */
public class IconUtils
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
   * Get a list of window icons.
   * @return a list of icons.
   */
  public static List<Image> getIcons()
  {
    int[] sizes={16,32,48,256};
    List<Image> icons=new ArrayList<Image>();
    for(int size : sizes)
    {
      String iconPath="/resources/gui/ring/ring"+size+".png";
      Image image=IconsManager.getImage(iconPath);
      if (image!=null)
      {
        icons.add(image);
      }
    }
    return icons;
  }

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
}
