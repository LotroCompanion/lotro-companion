package delta.games.lotro.gui.utils.l10n;

import delta.common.ui.swing.tables.ColumnsUtils;
import delta.common.ui.swing.tables.DefaultTableColumnController;

/**
 * Columns configuration utilities.
 * @author DAM
 */
public class StatColumnsUtils extends ColumnsUtils
{
  /**
   * Configure a stat column.
   * @param statColumn Column to use.
   * @param renderer Renderer to set.
   * @param width Width to use.
   */
  public static final void configureStatValueColumn(DefaultTableColumnController<?,?> statColumn, StatRenderer renderer, int width)
  {
    statColumn.setCellRenderer(renderer);
    statColumn.setWidthSpecs(width,width,width);
  }
}
