package delta.games.lotro.gui.utils;

import javax.swing.table.DefaultTableCellRenderer;

import delta.games.lotro.common.id.InternalGameId;

/**
 * Internal game ID renderer.
 * @author DAM
 */
public class InternalGameIdRenderer extends DefaultTableCellRenderer
{
  @Override
  public void setValue(Object value)
  {
    InternalGameId id=(InternalGameId)value;
    String text=(id!=null)?id.asDisplayableString():"";
    setText(text);
  }
}
