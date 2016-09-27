package delta.games.lotro.utils.gui.tables;

/**
 * Interface of an object that can access a value using a POJO.
 * @param <POJO> Type of source data.
 * @param <VALUE> Type of provided data.
 * @author DAM
 */
public interface CellDataProvider<POJO,VALUE>
{
  /**
   * Get a value from a source object.
   * @param p Source object.
   * @return A value (may be <code>null</code>).
   */
  VALUE getData(POJO p);
}
