package delta.games.lotro.utils.gui;

/**
 * Text sanitizer.
 * @author DAM
 */
public class TextSanitizer
{
  /**
   * Remove all patterns {@code <rgb=#RRGGBB>...</rgb>"}.
   * @param text Input text.
   * @return Transformed text.
   */
  public static String removeColorHints(String text)
  {
    while(true)
    {
      boolean exit=true;
      int index=text.indexOf(HtmlUtils.RGB_START);
      if (index!=-1)
      {
        int tagClosureIndex=text.indexOf(">",index+1);
        if (tagClosureIndex!=-1)
        {
          int indexRgbEnd=text.indexOf(HtmlUtils.RGB_END,tagClosureIndex+1);
          if (indexRgbEnd!=-1)
          {
            String between=text.substring(tagClosureIndex+1,indexRgbEnd);
            text=text.substring(0,index)+between+text.substring(indexRgbEnd+HtmlUtils.RGB_END.length());
          }
          exit=false;
        }
      }
      if (exit) break;
    }
    return text;
  }

}
