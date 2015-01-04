package delta.games.lotro.lore.items.io.tulkas;

import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.sql.LotroDataSource;

/**
 * Base class for Tulkas items/sets loaders.
 * @author DAM
 */
public class TulkasItemsLoader
{
  protected void writeItemToDB(Item item)
  {
    LotroDataSource ds=LotroDataSource.getInstance("lotro");
    ds.create(Item.class,item);
  }
}
