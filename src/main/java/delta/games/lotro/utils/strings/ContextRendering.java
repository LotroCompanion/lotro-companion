package delta.games.lotro.utils.strings;

import delta.games.lotro.character.BaseCharacterSummary;
import delta.games.lotro.dat.data.strings.renderer.StringRenderer;

/**
 * String rendering using character context..
 * @author DAM
 */
public class ContextRendering
{
  /**
   * Render a given string using the given character summary.
   * @param summary Character summary.
   * @param rawFormat Input string.
   * @return the rendered string.
   */
  public static String render(BaseCharacterSummary summary, String rawFormat)
  {
    if (rawFormat==null)
    {
      return null;
    }
    if (rawFormat.indexOf("${")==-1)
    {
      return rawFormat;
    }
    ContextVariableValueProvider provider=new ContextVariableValueProvider(null);
    provider.setup(summary);
    StringRenderer renderer=new StringRenderer(provider);
    String ret=renderer.render(rawFormat);
    ret=ret.replace(" ,",",");
    ret=ret.replace("  "," ");
    ret=ret.trim();
    return ret;
  }
}
