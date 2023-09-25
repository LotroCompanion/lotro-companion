package delta.games.lotro.gui.lore.items.chooser;

import java.awt.Dimension;
import java.util.List;

import javax.swing.JDialog;

import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.windows.WindowController;
import delta.common.utils.collections.filters.Filter;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.utils.gui.chooser.ObjectChoiceWindowController;

/**
 * Controller for an item choice window.
 * @author DAM
 */
public class ItemChooser
{
  /**
   * Preference file for the columns of the item chooser.
   */
  public static final String ITEM_CHOOSER_PROPERTIES_ID="ItemChooserColumn";
  /**
   * Preference file for the columns of the item instance chooser.
   */
  public static final String ITEM_INSTANCE_CHOOSER_PROPERTIES_ID="ItemInstanceChooserColumn";
  /**
   * Name of the property for column IDs.
   */
  public static final String COLUMNS_PROPERTY="columns";

  /**
   * Build an item chooser window.
   * @param parent Parent controller.
   * @param prefs Preferences for this window.
   * @param items Item to use.
   * @param filter Filter to use.
   * @param filterController Filter UI to use.
   * @return the newly built chooser.
   */
  public static ObjectChoiceWindowController<Item> buildChooser(WindowController parent, TypedProperties prefs, List<? extends Item> items, Filter<Item> filter, ItemFilterController filterController)
  {
    // Table
    GenericTableController<Item> itemsTable=ItemsTableBuilder.buildTable(items);

    // Build and configure chooser
    final ObjectChoiceWindowController<Item> chooser=new ObjectChoiceWindowController<Item>(parent,prefs,itemsTable);
    // Filter
    chooser.setFilter(filter,filterController);
    JDialog dialog=chooser.getDialog();
    // Title
    dialog.setTitle("Choose item:");
    // Dimension
    dialog.setMinimumSize(new Dimension(400,300));
    dialog.setSize(1000,dialog.getHeight());
    // Add details column
    ItemsTableBuilder.addDetailsColumn(chooser,itemsTable);
    return chooser;
  }
}
