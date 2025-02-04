package delta.games.lotro.gui.utils.tables.renderers;

import javax.swing.table.DefaultTableCellRenderer;

import delta.games.lotro.common.owner.Owner;
import delta.games.lotro.gui.character.storage.StorageFilterController;

/**
 * Owner renderer.
 * @author DAM
 */
public class OwnerRenderer extends DefaultTableCellRenderer
{
  @Override
  public void setValue(Object value)
  {
    String text=StorageFilterController.getLabelForOwner((Owner)value);
    setText(text);
  }
}
