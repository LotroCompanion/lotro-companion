package delta.games.lotro.gui.utils.tables.renderers;

import javax.swing.table.DefaultTableCellRenderer;

import delta.games.lotro.character.BaseCharacterSummary;
import delta.games.lotro.common.binding.BindingsManager;
import delta.games.lotro.common.id.InternalGameId;

/**
 * Internal game ID renderer.
 * @author DAM
 */
public class InternalGameIdRenderer extends DefaultTableCellRenderer
{
  private BaseCharacterSummary _toon;

  /**
   * Constructor.
   * @param toon
   */
  public InternalGameIdRenderer(BaseCharacterSummary toon)
  {
    _toon=toon;
  }

  @Override
  public void setValue(Object value)
  {
    if (value==null)
    {
      return;
    }
    InternalGameId id=(InternalGameId)value;
    String text=BindingsManager.getInstance().getSimpleBindingInfo(id,_toon);
    setText(text);
  }
}
