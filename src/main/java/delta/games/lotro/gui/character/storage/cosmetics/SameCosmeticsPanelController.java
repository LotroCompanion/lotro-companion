package delta.games.lotro.gui.character.storage.cosmetics;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.panels.AbstractPanelController;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.tables.panel.FilterUpdateListener;
import delta.common.ui.swing.tables.panel.GenericTablePanelController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.storage.StoredItem;
import delta.games.lotro.character.storage.cosmetics.CosmeticItemsGroup;

/**
 * Controller for a panel to show a collection of 'same cosmetics' groups.
 * @author DAM
 */
public class SameCosmeticsPanelController extends AbstractPanelController implements FilterUpdateListener
{
  // Controllers
  private List<SameCosmeticsTableRow> _data;
  private GenericTableController<SameCosmeticsTableRow> _table;
  private GenericTablePanelController<SameCosmeticsTableRow> _panelController;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param filter Filter.
   */
  public SameCosmeticsPanelController(WindowController parent, SameCosmeticsTableRowFilter filter)
  {
    _data=new ArrayList<SameCosmeticsTableRow>();
    _table=SameCosmeticsTableBuilder.buildTable(parent,_data);
    _panelController=new GenericTablePanelController<SameCosmeticsTableRow>(parent,_table);
    _table.setFilter(filter);
    JPanel tablePanel=_panelController.getPanel();
    JPanel panel=GuiFactory.buildPanel(new BorderLayout());
    panel.add(tablePanel,BorderLayout.CENTER);
    setPanel(panel);
  }

  private List<SameCosmeticsTableRow> buildRows(List<CosmeticItemsGroup> groups)
  {
    List<SameCosmeticsTableRow> ret=new ArrayList<SameCosmeticsTableRow>();
    for(CosmeticItemsGroup group : groups)
    {
      for(StoredItem item : group.getItems())
      {
        SameCosmeticsTableRow row=new SameCosmeticsTableRow(group,item);
        ret.add(row);
      }
    }
    Collections.sort(ret,new SameCosmeticsTableRowComparator());
    return ret;
  }
  /**
   * Update the display.
   * @param groups Groups to display.
   */
  public void updateDisplay(List<CosmeticItemsGroup> groups)
  {
    _data.clear();
    _data.addAll(buildRows(groups));
    _table.refresh();
  }

  /**
   * Get the managed rows.
   * @return the managed rows.
   */
  public List<SameCosmeticsTableRow> getRows()
  {
    return _data;
  }

  /**
   * Get the managed stored items.
   * @return the managed stored items.
   */
  public List<StoredItem> getStoredItems()
  {
    List<StoredItem> ret=new ArrayList<StoredItem>();
    for(SameCosmeticsTableRow row : _data)
    {
      ret.add(row.getStoredItem());
    }
    return ret;
  }

  @Override
  public void filterUpdated()
  {
    _panelController.filterUpdated();
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    super.dispose();
    if (_table!=null)
    {
      _table.dispose();
      _table=null;
    }
  }
}
