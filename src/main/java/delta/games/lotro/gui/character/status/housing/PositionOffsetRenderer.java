package delta.games.lotro.gui.character.status.housing;

import javax.swing.table.DefaultTableCellRenderer;

import delta.common.utils.math.geometry.Vector3D;

/**
 * Position offset renderer.
 * @author DAM
 */
public class PositionOffsetRenderer extends DefaultTableCellRenderer
{
  @Override
  public void setValue(Object value)
  {
    Vector3D offset=(Vector3D)value;
    if (offset!=null)
    {
      String text=offset.toString();
      setText(text);
    }
    else
    {
      setText("");
    }
  }
}
