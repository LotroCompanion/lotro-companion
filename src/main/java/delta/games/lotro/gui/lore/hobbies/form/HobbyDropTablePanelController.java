package delta.games.lotro.gui.lore.hobbies.form;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.lore.hobbies.rewards.HobbyDropTableEntry;

/**
 * Controller for a panel to display a hobby drop table.
 * @author DAM
 */
public class HobbyDropTablePanelController
{
  // Data
  private List<HobbyDropTableEntry> _entries;
  // GUI
  private JPanel _panel;
  // Controllers
  private WindowController _parent;
  private GenericTableController<HobbyDropTableEntry> _table;

  /**
   * Constructor.
   * @param parent Parent window controller.
   */
  public HobbyDropTablePanelController(WindowController parent)
  {
    _parent=parent;
    _entries=new ArrayList<HobbyDropTableEntry>();
    _panel=build();
  }

  /**
   * Set the entries to show.
   * @param entries
   */
  public void setEntries(List<HobbyDropTableEntry> entries)
  {
    _entries.clear();
    _entries.addAll(entries);
    _table.refresh();
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
    // Table
    _table=HobbyDropTableBuilder.buildTable(_entries);
    JTable table=_table.getTable();
    // Add details column
    HobbyDropTableBuilder.addDetailsColumn(_parent,_table);
    JScrollPane scroll=GuiFactory.buildScrollPane(table);
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,1,1,1,1.0,1.0,GridBagConstraints.NORTHWEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
    panel.add(scroll,c);
    return panel;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Data
    _entries=null;
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
