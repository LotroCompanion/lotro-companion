package delta.games.lotro.utils.gui.tables;

/**
 * Interface of an object that can set a value using a POJO.
 * @param <POJO> Type of source data.
 * @author DAM
 */
public interface CellDataUpdater<POJO>
{
  /**
   * Set a value in a source object.
   * @param p Source object.
   * @param data Value to set.
   */
  void setData(POJO p, Object data);
}
