package delta.games.lotro.gui.character.status.housing;

import javax.swing.table.DefaultTableCellRenderer;

import delta.games.lotro.common.geo.Position;
import delta.games.lotro.common.geo.PositionUtils;

/**
 * Position renderer.
 * @author DAM
 */
public class PositionRenderer extends DefaultTableCellRenderer
{
  @Override
  public void setValue(Object value)
  {
    Position position=(Position)value;
    if (position!=null)
    {
      String text=PositionUtils.getLabel(position);
      setText(text);
    }
    else
    {
      setText("");
    }
  }
}
