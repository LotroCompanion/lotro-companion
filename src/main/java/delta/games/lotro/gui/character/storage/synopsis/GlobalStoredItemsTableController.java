package delta.games.lotro.gui.character.storage.synopsis;

import java.util.ArrayList;
import java.util.List;

import delta.common.ui.swing.windows.WindowController;
import delta.common.utils.collections.filters.Filter;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.character.storage.StoredItem;
import delta.games.lotro.gui.character.storage.StoredItemsTableController;
import delta.games.lotro.gui.lore.items.CountedItemsTableController;
import delta.games.lotro.gui.lore.items.table.ItemColumnIds;

/**
 * Stored items table for the global storage synopsis (includes account column).
 * @author DAM
 */
public class GlobalStoredItemsTableController extends StoredItemsTableController
{
  /**
   * Constructor.
   * @param parent Parent controller.
   * @param prefs User preferences.
   * @param items Items to show.
   * @param filter Managed filter.
   */
  public GlobalStoredItemsTableController(WindowController parent, TypedProperties prefs, List<StoredItem> items, Filter<StoredItem> filter)
  {
    super(parent,prefs,items,filter);
  }

  @Override
  protected List<String> getDefaultColumnIds()
  {
    List<String> columnsIds=new ArrayList<String>();
    columnsIds.add(ItemColumnIds.ICON.name());
    columnsIds.add(ItemColumnIds.NAME.name());
    columnsIds.add(CountedItemsTableController.COUNT_COLUMN);
    columnsIds.add(ACCOUNT_COLUMN);
    columnsIds.add(OWNER_COLUMN);
    columnsIds.add(LOCATION_COLUMN);
    return columnsIds;
  }
}
