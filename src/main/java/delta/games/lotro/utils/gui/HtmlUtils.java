package delta.games.lotro.utils.gui;

import org.apache.log4j.Logger;

import delta.games.lotro.utils.gui.themes.ColorMapping;

/**
 * Utility methods related to HTML generation.
 * @author DAM
 */
public class HtmlUtils
{
  private static final Logger LOGGER=Logger.getLogger(HtmlUtils.class);
  private static ColorMapping _colorMapping=new ColorMapping();

  /**
   * RGB start.
   */
  public static final String RGB_START="<rgb=";
  /**
   * RGB end.
   */
  public static final String RGB_END="</rgb>";

  /**
   * Add an hyperlink to the given StringBuilder.
   * @param sb String builder to use.
   * @param to Address of the link.
   * @param text Link text.
   */
  public static void printLink(StringBuilder sb, String to, String text)
  {
    sb.append("<a href=\"");
    sb.append(to);
    sb.append("\">");
    sb.append(text);
    sb.append("</a>");
  }

  /**
   * Convert the given text to HTML.
   * @param text Input text.
   * @return Result HTML text.
   */
  public static String toHtml(String text)
  {
    text=text.trim();
    text=text.replace("\n\n","<br>");
    text=text.replace("\n","<br>");
    text=text.replace("\\q","&quot;");
    text=encodeColoredText(text);
    return text;
  }

  /**
   * Transform all patterns {@code <rgb=#RRGGBB>...</rgb>"} into equivalent HTML {@code <font color="#RRGGBB">...</font>}.
   * @param text Input text.
   * @return Transformed text.
   */
  private static String encodeColoredText(String text)
  {
    while(true)
    {
      int index=text.indexOf(RGB_START);
      if (index==-1)
      {
        break;
      }
      int tagClosureIndex=text.indexOf(">",index+1);
      if (tagClosureIndex==-1)
      {
        LOGGER.warn("Expected a > to close a RGB tag. Not found!");
        break;
      }
      String color=text.substring(index+RGB_START.length(),tagClosureIndex);
      color=_colorMapping.mapColor(color);
      int indexRgbEnd=text.indexOf(RGB_END,tagClosureIndex+1);
      if (indexRgbEnd!=-1)
      {
        String between=text.substring(tagClosureIndex+1,indexRgbEnd);
        text=text.substring(0,index)+"<font color=\""+color+"\">"+between+"</font>"+text.substring(indexRgbEnd);
      }
      else
      {
        LOGGER.warn("Expected a RGB Tag end. Not found!");
        String end=text.substring(tagClosureIndex+1);
        text=text.substring(0,index)+"<font color=\""+color+"\">"+end+"</font>";
      }
    }
    return text;
  }
}
