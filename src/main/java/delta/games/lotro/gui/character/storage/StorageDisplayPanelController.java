package delta.games.lotro.gui.character.storage;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import delta.common.ui.swing.tables.panel.FilterUpdateListener;
import delta.common.ui.swing.tables.panel.GenericTablePanelController;
import delta.common.ui.swing.windows.WindowController;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.character.storage.StoredItem;
import delta.games.lotro.gui.main.GlobalPreferences;

/**
 * Controller for the storage display panel.
 * @author DAM
 */
public class StorageDisplayPanelController implements FilterUpdateListener
{
  // Data
  private List<StoredItem> _items;
  private StorageFilter _filter;
  // Controllers
  private GenericTablePanelController<StoredItem> _tablePanel;
  private StoredItemsTableController _tableController;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param filter Filter of stored items.
   */
  public StorageDisplayPanelController(WindowController parent, StorageFilter filter)
  {
    _filter=filter;
    _items=new ArrayList<StoredItem>();
    TypedProperties prefs=GlobalPreferences.getGlobalProperties("StorageDisplay");
    _tableController=new StoredItemsTableController(parent,prefs,_items,filter);
    _tablePanel=new GenericTablePanelController<>(parent,_tableController.getTableController());
    _tablePanel.getConfiguration().setBorderTitle("Items"); // I18n
    _tablePanel.getCountsDisplay().setText("Item(s)"); // I18n
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  public JPanel getPanel()
  {
    return _tablePanel.getPanel();
  }

  /**
   * Update display.
   * @param items Items to show.
   */
  public void update(List<StoredItem> items)
  {
    updateStorage(items);
    _tableController.update();
    _tablePanel.filterUpdated();
  }

  /**
   * Update filter.
   */
  @Override
  public void filterUpdated()
  {
    _tablePanel.filterUpdated();
  }

  private void updateStorage(List<StoredItem> items)
  {
    _items.clear();
    _items.addAll(items);
    _filter.getConfiguration().setItems(_items);
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Data
    _items=null;
    _filter=null;
    // Controllers
    _tableController=null;
  }
}
