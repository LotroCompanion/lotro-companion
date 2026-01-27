package delta.games.lotro.gui.character.storage.cosmetics;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.tables.panel.GenericTablePanelController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.storage.StoredItem;
import delta.games.lotro.character.storage.cosmetics.CosmeticItemsGroup;

/**
 * Controller for a panel to show a collection of 'same cosmetics' groups.
 * @author DAM
 */
public class SameCosmeticsPanelController2
{
  // UI
  private JPanel _panel;
  // Controllers
  private List<SameCosmeticsTableRow> _data;
  private GenericTableController<SameCosmeticsTableRow> _table;
  private GenericTablePanelController<SameCosmeticsTableRow> _panelController;

  /**
   * Constructor.
   * @param parent Parent window.
   */
  public SameCosmeticsPanelController2(WindowController parent)
  {
    _panel=GuiFactory.buildPanel(new BorderLayout());
    _data=new ArrayList<SameCosmeticsTableRow>();
    _table=SameCosmeticsTableBuilder.buildTable(parent,_data);
    _panelController=new GenericTablePanelController<SameCosmeticsTableRow>(parent,_table);
    //_panelController.getConfiguration().setBorderTitle("Mounts"); // I18n
    //_panelController.getCountsDisplay().setText("Mount(s)"); // I18n
    JPanel tablePanel=_panelController.getPanel();
    _panel.add(tablePanel,BorderLayout.CENTER);
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
   * Get the managed panel.
   * @return a panel.
   */
  public JPanel getPanel()
  {
    return _panel;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    if (_table!=null)
    {
      _table.dispose();
      _table=null;
    }
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
  }
}
