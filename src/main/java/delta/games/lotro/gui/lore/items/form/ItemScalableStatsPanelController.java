package delta.games.lotro.gui.lore.items.form;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.tables.GenericTableController;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.scaling.ItemScaling;
import delta.games.lotro.lore.items.scaling.ItemScalingBuilder;
import delta.games.lotro.lore.items.scaling.ItemScalingEntry;

/**
 * Controller for a panel to display a table of the scalable stats of an item.
 * @author DAM
 */
public class ItemScalableStatsPanelController
{
  // Data
  private Item _item;
  // GUI
  private JPanel _panel;
  // Controllers
  private GenericTableController<ItemScalingEntry> _table;

  /**
   * Constructor.
   * @param item Item to show.
   */
  public ItemScalableStatsPanelController(Item item)
  {
    _item=item;
    _panel=build();
  }

  /**
   * Get the managed panel.
   * @return the managed panel or <code>null</code> if no scaling.
   */
  public JPanel getPanel()
  {
    return _panel;
  }

  private JPanel build()
  {
    ItemScaling scaling=ItemScalingBuilder.build(_item);
    if (scaling.getEntries().size()==0)
    {
      return null;
    }

    _table=ItemScalingTableBuilder.buildTable(scaling);
    JTable table=_table.getTable();
    JScrollPane scroll=GuiFactory.buildScrollPane(table);
    JPanel panel=GuiFactory.buildPanel(new BorderLayout());
    panel.add(scroll,BorderLayout.CENTER);
    return panel;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Data
    _item=null;
    // UI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    // Controllers
    if (_table!=null)
    {
      _table.dispose();
      _table=null;
    }
  }
}
