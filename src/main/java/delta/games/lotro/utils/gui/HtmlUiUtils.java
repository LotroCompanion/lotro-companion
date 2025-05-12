package delta.games.lotro.utils.gui;

import javax.swing.JEditorPane;

import delta.common.ui.swing.GuiFactory;
import delta.games.lotro.utils.html.HtmlUtils;

/**
 * Utility methods related to HTML.
 * @author DAM
 */
public class HtmlUiUtils
{
  /**
   * Build an editor pane to display the given text.
   * @param input Input text.
   * @return <code>true</code> if input is <code>null</code> or empty.
   */
  public static JEditorPane buildEditorPane(String input)
  {
    JEditorPane editor=null;
    if ((input!=null) && (!input.isEmpty()))
    {
      editor=GuiFactory.buildHtmlPanel();
      setText(editor,input);
    }
    return editor;
  }

  /**
   * Set the text of an HTML display.
   * @param display Targeted component.
   * @param text Text to set.
   */
  public static void setText(JEditorPane display, String text)
  {
    StringBuilder sb=new StringBuilder();
    sb.append("<html><body>");
    sb.append(HtmlUtils.toHtml(text));
    sb.append("</body></html>");
    display.setText(sb.toString());
  }
}
