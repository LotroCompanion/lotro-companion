package delta.games.lotro.gui.character.status.skirmishes;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JDialog;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.DefaultDisplayDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.character.status.skirmishes.SkirmishStatsManager;
import delta.games.lotro.character.status.skirmishes.filter.SkirmishEntryFilter;
import delta.games.lotro.gui.character.status.skirmishes.filter.SkirmishEntryFilterController;
import delta.games.lotro.gui.character.status.skirmishes.table.SkirmishEntriesTableController;
import delta.games.lotro.gui.lore.items.FilterUpdateListener;
import delta.games.lotro.gui.main.GlobalPreferences;

/**
 * Controller for a skirmish statistics display window.
 * @author DAM
 */
public class SkirmishStatisticsWindowController extends DefaultDisplayDialogController<SkirmishStatsManager> implements FilterUpdateListener
{
  private static final int MAX_HEIGHT=800;

  // Data
  private SkirmishEntryFilter _filter;
  // Controllers
  private SkirmishEntryFilterController _filterController;
  private SkirmishEntriesPanelController _panelController;
  private SkirmishEntriesTableController _tableController;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param status Status to show.
   */
  public SkirmishStatisticsWindowController(WindowController parent, SkirmishStatsManager status)
  {
    super(parent,status);
    _filter=new SkirmishEntryFilter();
  }

  @Override
  protected JDialog build()
  {
    JDialog dialog=super.build();
    dialog.setMinimumSize(new Dimension(1000,300));
    dialog.setTitle("Skirmish statistics");
    dialog.pack();
    Dimension size=dialog.getSize();
    if (size.height>MAX_HEIGHT)
    {
      dialog.setSize(size.width,MAX_HEIGHT);
    }
    return dialog;
  }

  @Override
  public void configureWindow()
  {
    automaticLocationSetup();
  }

  @Override
  protected JPanel buildFormPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    // Table
    initTable();
    _panelController=new SkirmishEntriesPanelController(this,_tableController);
    JPanel tablePanel=_panelController.getPanel();
    // Build child controllers
    _filterController=new SkirmishEntryFilterController(_filter,this);
    // Whole panel
    // - filter
    JPanel filterPanel=_filterController.getPanel();
    filterPanel.setBorder(GuiFactory.buildTitledBorder("Filter"));
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(filterPanel,c);
    // - table
    c=new GridBagConstraints(0,1,1,1,1,1,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
    panel.add(tablePanel,c);
    return panel;
  }

  private void initTable()
  {
    TypedProperties prefs=GlobalPreferences.getGlobalProperties("SkirmishStats");
    _tableController=new SkirmishEntriesTableController(prefs,_filter);
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
    // Data
    _data=null;
    _filter=null;
    // Controllers
    if (_filterController!=null)
    {
      _filterController.dispose();
      _filterController=null;
    }
    if (_panelController!=null)
    {
      _panelController.dispose();
      _panelController=null;
    }
    if (_tableController!=null)
    {
      _tableController.dispose();
      _tableController=null;
    }
  }
}
