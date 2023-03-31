package delta.games.lotro.gui.utils.l10n;

import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

import delta.games.lotro.common.stats.StatDescription;
import delta.games.lotro.common.stats.StatUtils;

/**
 * Renderer for stat columns.
 * @author DAM
 */
public class StatRenderer extends DefaultTableCellRenderer
{
  private StatDescription _stat;

  /**
   * Constructor.
   * @param stat Stat to use.
   */
  public StatRenderer(StatDescription stat)
  {
    _stat=stat;
    setHorizontalAlignment(SwingConstants.RIGHT);
  }

  /**
   * Set the stat to use.
   * @param stat Stat to use.
   */
  public void setStat(StatDescription stat)
  {
    _stat=stat;
  }

  @Override
  public void setValue(Object value)
  {
    String label="";
    if (value instanceof Number)
    {
      Number statValue=(Number)value;
      label=StatUtils.getStatDisplay(statValue,_stat);
    }
    setText(label);
  }
}
