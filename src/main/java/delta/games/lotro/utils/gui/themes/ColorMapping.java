package delta.games.lotro.utils.gui.themes;

import java.util.HashMap;
import java.util.Map;

/**
 * Simple color mapping.
 * <p>
 * This was initially done to update some colors designed for a black background (in the lotro client)
 * into colors that display well in LC (white background).
 * @author DAM
 */
public class ColorMapping
{
  private Map<String,String> _colorsToChange;

  /**
   * Constructor.
   */
  public ColorMapping()
  {
    _colorsToChange=new HashMap<String,String>();
    // Yellow->Orange
    setupColorMapping("#FFFF00","#FF8500");
    // Plain green->Darker green
    setupColorMapping("#00FF00","#3B833B");
  }

  private void setupColorMapping(String inputColor, String newColor)
  {
    _colorsToChange.put(inputColor,newColor);
  }

  /**
   * Map a color.
   * @param inputColor Input color.
   * @return the color to use.
   */
  public String mapColor(String inputColor)
  {
    String newColor=_colorsToChange.get(inputColor);
    if (newColor==null)
    {
      newColor=inputColor;
    }
    return newColor;
  }
}
