package delta.games.lotro.lore.sql;

import java.util.HashMap;

import org.apache.log4j.Logger;

import delta.common.framework.objects.sql.DatabaseConfiguration;
import delta.common.framework.objects.sql.SqlObjectsSource;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.utils.LotroLoggers;

/**
 * Data source for the genea objects of a single database.
 * @author DAM
 */
public class LotroDataSource extends SqlObjectsSource
{
  private static final Logger _logger=LotroLoggers.getLotroLogger();

  private static HashMap<String,LotroDataSource> _sources=new HashMap<String,LotroDataSource>();

  /**
   * Get the data source that manages the genea objects
   * for the given database.
   * @param dbName Name of the targeted database.
   * @return A genea objects data source.
   */
  public static LotroDataSource getInstance(String dbName)
  {
    synchronized (_sources)
    {
      LotroDataSource instance=_sources.get(dbName);
      if (instance==null)
      {
        instance=new LotroDataSource(dbName);
        _sources.put(dbName,instance);
      }
      return instance;
    }
  }

  /**
   * Private constructor.
   * @param dbName Name of the database to manage.
   */
  private LotroDataSource(String dbName)
  {
    super(dbName);
    buildDrivers();
    try
    {
      start();
    }
    catch(Exception e)
    {
      _logger.error("Cannot start lotro data source!",e);
    }
  }

  @Override
  protected DatabaseConfiguration buildDatabaseConfiguration(String dbName)
  {
    return new DatabaseConfiguration("delta/games/lotro/lore/sql/database.properties");
  }

  /**
   * Build the drivers for all the object classes.
   */
  private void buildDrivers()
  {
    try
    {
      addClass(Item.class,new ItemsSqlDriver());
    }
    catch(Exception e)
    {
      _logger.error("",e);
    }
  }

  /**
   * Close all data sources.
   */
  public static void closeAll()
  {
    for(LotroDataSource source : _sources.values())
    {
      source.close();
    }
    _sources.clear();
  }
}
