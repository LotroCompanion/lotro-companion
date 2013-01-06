package delta.games.lotro.gui.stats.warbands;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;

import delta.games.lotro.gui.utils.GuiFactory;
import delta.games.lotro.lore.warbands.WarbandFilter;
import delta.games.lotro.stats.warbands.MultipleToonsWarbandsStats;

/**
 * Controller for a warbands statistics panel.
 * @author DAM
 */
public class WarbandsPanelController
{
  // Controllers
  private WarbandsFilterController _filterController;
  private WarbandsTableController _tableController;
  // Data
  private WarbandFilter _filter;
  // GUI
  private JPanel _panel;
  
  /**
   * Constructor.
   * @param stats Underlying warbands statistics.
   */
  public WarbandsPanelController(MultipleToonsWarbandsStats stats)
  {
    _filter=new WarbandFilter();
    _filterController=new WarbandsFilterController(_filter,this);
    _tableController=new WarbandsTableController(stats,_filter);
  }

  /**
   * Get the managed panel.
   * @return a panel.
   */
  public JPanel getPanel()
  {
    if (_panel==null)
    {
      _panel=buildPanel();
    }
    return _panel;
  }
  
  private JPanel buildPanel()
  {
    JPanel panel=GuiFactory.buildBackgroundPanel(new BorderLayout());
    JPanel statsPanels=buildStatsPanel();
    panel.add(statsPanels,BorderLayout.CENTER);
    JPanel commandsPanel=buildCommandsPanel();
    panel.add(commandsPanel,BorderLayout.NORTH);
    return panel;
  }

  private JPanel buildCommandsPanel()
  {
    JPanel panel=_filterController.getPanel();
    TitledBorder filterBorder=GuiFactory.buildTitledBorder("Filter");
    panel.setBorder(filterBorder);
    return panel;
  }

  private JPanel buildStatsPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new BorderLayout());
    TitledBorder logFrameBorder=GuiFactory.buildTitledBorder("Warbands");
    panel.setBorder(logFrameBorder);
    
    // Table
    JTable table=_tableController.getTable();
    JScrollPane scroll=GuiFactory.buildScrollPane(table);
    //scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    panel.add(scroll,BorderLayout.CENTER);
    //System.out.println("Scroll: "+scroll.getPreferredSize());
    return panel;
  }

  /**
   * Update filter.
   */
  public void updateFilter()
  {
    _tableController.updateFilter();
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // GUI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    // Controllers
    if (_filterController!=null)
    {
      _filterController.dispose();
      _filterController=null;
    }
    if (_tableController!=null)
    {
      _tableController.dispose();
      _tableController=null;
    }
    // Data
    _filter=null;
  }
}
