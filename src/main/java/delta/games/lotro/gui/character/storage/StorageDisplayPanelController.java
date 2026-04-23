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
import delta.games.lotro.gui.utils.l10n.Labels;

/**
 * Controller for the storage display panel.
 * @author DAM
 */
public class StorageDisplayPanelController implements FilterUpdateListener
{
  // Data
  private List<StoredItem> _items;
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
    _items=new ArrayList<StoredItem>();
    TypedProperties prefs=GlobalPreferences.getGlobalProperties("StorageDisplay");
    _tableController=new StoredItemsTableController(parent,prefs,_items,filter);
    _tablePanel=new GenericTablePanelController<>(parent,_tableController.getTableController());
    String borderLabel=Labels.getLabel("shared.table.items.border");
    _tablePanel.getConfiguration().setBorderTitle(borderLabel);
    String itemsLabel=Labels.getLabel("shared.table.field.items");
    _tablePanel.getCountsDisplay().setText(itemsLabel);
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
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Data
    _items=null;
    // Controllers
    _tableController=null;
  }
}
