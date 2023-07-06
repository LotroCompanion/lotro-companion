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
import delta.games.lotro.character.status.skirmishes.SkirmishEntriesManager;
import delta.games.lotro.gui.character.status.skirmishes.cfg.SkirmishEntryConfigController;
import delta.games.lotro.gui.character.status.skirmishes.filter.SkirmishEntryFilterController;
import delta.games.lotro.gui.character.status.skirmishes.table.SkirmishEntriesTableController;
import delta.games.lotro.gui.lore.items.FilterUpdateListener;
import delta.games.lotro.gui.main.GlobalPreferences;
import delta.games.lotro.gui.utils.ConfigUpdateListener;

/**
 * Controller for a skirmish statistics display window.
 * @author DAM
 */
public class SkirmishStatisticsWindowController extends DefaultDisplayDialogController<SkirmishEntriesManager> implements FilterUpdateListener, ConfigUpdateListener
{
  /**
   * Window identifier.
   */
  public static final String IDENTIFIER="SKIRMISH_STATUS";

  private static final int MAX_HEIGHT=800;

  // Controllers
  private SkirmishEntryFilterController _filterController;
  private SkirmishEntryConfigController _configController;
  private SkirmishStatsDisplayPanelController _totalsController;
  private SkirmishEntriesPanelController _panelController;
  private SkirmishEntriesTableController _tableController;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param entriesMgr Entries to show.
   */
  public SkirmishStatisticsWindowController(WindowController parent, SkirmishEntriesManager entriesMgr)
  {
    super(parent,entriesMgr);
  }

  @Override
  protected JDialog build()
  {
    JDialog dialog=super.build();
    dialog.setMinimumSize(new Dimension(1000,300));
    dialog.setTitle("Skirmish statistics"); // I18n
    dialog.setSize(new Dimension(1200,600));
    //dialog.pack();
    Dimension size=dialog.getSize();
    if (size.height>MAX_HEIGHT)
    {
      dialog.setSize(size.width,MAX_HEIGHT);
    }
    return dialog;
  }

  @Override
  public String getWindowIdentifier()
  {
    return IDENTIFIER;
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
    _filterController=new SkirmishEntryFilterController(_data.getFilter(),this);
    _configController=new SkirmishEntryConfigController(_data.getConfig(),this);
    _totalsController=new SkirmishStatsDisplayPanelController();
    // Whole panel
    // - filter
    JPanel filterPanel=_filterController.getPanel();
    filterPanel.setBorder(GuiFactory.buildTitledBorder("Filter")); // I18n
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(filterPanel,c);
    // - config
    JPanel configPanel=_configController.getPanel();
    configPanel.setBorder(GuiFactory.buildTitledBorder("Configuration")); // I18n
    c=new GridBagConstraints(0,1,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(configPanel,c);
    // - totals
    JPanel totalsPanel=_totalsController.getPanel();
    totalsPanel.setBorder(GuiFactory.buildTitledBorder("Totals")); // I18n
    c=new GridBagConstraints(1,0,1,2,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(totalsPanel,c);
    // - table
    c=new GridBagConstraints(0,2,2,1,1,1,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
    panel.add(tablePanel,c);
    return panel;
  }

  private void initTable()
  {
    TypedProperties prefs=GlobalPreferences.getGlobalProperties("SkirmishStats");
    _tableController=new SkirmishEntriesTableController(_data,prefs);
  }

  @Override
  public void filterUpdated()
  {
    update();
  }

  @Override
  public void configurationUpdated()
  {
    update();
  }

  private void update()
  {
    _data.update();
    _totalsController.updateUI(_data.getTotals());
    _panelController.updateContents();
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
    // Controllers
    if (_filterController!=null)
    {
      _filterController.dispose();
      _filterController=null;
    }
    if (_configController!=null)
    {
      _configController.dispose();
      _configController=null;
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
