package delta.games.lotro.gui.items.chooser;

import java.awt.Dimension;
import java.util.List;

import javax.swing.JDialog;

import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.windows.WindowController;
import delta.common.utils.collections.filters.Filter;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemInstance;
import delta.games.lotro.lore.items.filters.ItemInstanceFilter;
import delta.games.lotro.utils.gui.chooser.ObjectChoiceWindowController;

/**
 * Controller for an item instance choice window.
 * @author DAM
 */
public class ItemInstanceChooser
{
  /**
   * Build an item chooser window.
   * @param parent Parent controller.
   * @param prefs Preferences for this window.
   * @param items Item to use.
   * @param filter Filter to use.
   * @param filterController Filter UI to use.
   * @return the newly built chooser.
   */
  public static ObjectChoiceWindowController<ItemInstance<? extends Item>> buildChooser(WindowController parent, TypedProperties prefs, List<ItemInstance<? extends Item>> items, Filter<Item> filter, ItemFilterController filterController)
  {
    // Table
    GenericTableController<ItemInstance<? extends Item>> itemsTable=ItemInstancesTableBuilder.buildTable(items);

    // Build and configure chooser
    ObjectChoiceWindowController<ItemInstance<? extends Item>> chooser=new ObjectChoiceWindowController<ItemInstance<? extends Item>>(parent,prefs,itemsTable);
    // Filter
    ItemInstanceFilter instanceFilter=new ItemInstanceFilter(filter);
    chooser.setFilter(instanceFilter,filterController);
    JDialog dialog=chooser.getDialog();
    // - title
    dialog.setTitle("Choose item:");
    // - dimension
    dialog.setMinimumSize(new Dimension(400,300));
    dialog.setSize(1000,dialog.getHeight());
    return chooser;
  }
}
