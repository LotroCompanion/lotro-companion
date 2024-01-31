package delta.games.lotro.gui.character.storage.wardrobe;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import delta.common.ui.swing.tables.panel.GenericTablePanelController;
import delta.common.ui.swing.windows.WindowController;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.character.storage.wardrobe.WardrobeItem;
import delta.games.lotro.gui.main.GlobalPreferences;

/**
 * Controller for the wardrobe display panel.
 * @author DAM
 */
public class WardrobeDisplayPanelController// implements FilterUpdateListener
{
  // Data
  private List<WardrobeItem> _items;
  private WardrobeFilter _filter;
  // GUI
  private JPanel _panel;
  // Controllers
  private WardrobeItemsTableController _tableController;
  private GenericTablePanelController<WardrobeItem> _tablePanel;
  private WindowController _parent;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param filter FIlter of stored items.
   */
  public WardrobeDisplayPanelController(WindowController parent, WardrobeFilter filter)
  {
    _parent=parent;
    _filter=filter;
    _items=new ArrayList<WardrobeItem>();
    TypedProperties prefs=GlobalPreferences.getGlobalProperties("Wardrobe");
    _tableController=new WardrobeItemsTableController(parent,prefs,_items,filter);
    _tablePanel=new GenericTablePanelController<WardrobeItem>(_parent,_tableController.getTableController());
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
  public void update(List<WardrobeItem> items)
  {
    updateContents(items);
    _tableController.update();
    _tablePanel.filterUpdated();
  }

  /**
   * Update filter.
   */
  public void filterUpdated()
  {
    _tableController.updateFilter();
  }

  private void updateContents(List<WardrobeItem> items)
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
    // GUI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    // Controllers
    _tableController=null;
    if (_tablePanel!=null)
    {
      _tablePanel.dispose();
      _tablePanel=null;
    }
    _parent=null;
  }
}
