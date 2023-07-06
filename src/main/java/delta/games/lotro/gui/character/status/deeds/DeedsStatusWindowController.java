package delta.games.lotro.gui.character.status.deeds;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.windows.DefaultFormDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.common.ui.swing.windows.WindowsManager;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.classes.ClassDescription;
import delta.games.lotro.character.events.CharacterEvent;
import delta.games.lotro.character.events.CharacterEventType;
import delta.games.lotro.character.status.achievables.AchievableStatus;
import delta.games.lotro.character.status.achievables.AchievablesStatusManager;
import delta.games.lotro.character.status.achievables.SyncAchievablesStatusAndTraitPoints;
import delta.games.lotro.character.status.achievables.filter.DeedStatusFilter;
import delta.games.lotro.character.status.reputation.ReputationStatus;
import delta.games.lotro.character.status.traitPoints.TraitPoints;
import delta.games.lotro.character.status.traitPoints.TraitPointsStatus;
import delta.games.lotro.common.blacklist.Blacklist;
import delta.games.lotro.common.blacklist.io.BlackListIO;
import delta.games.lotro.gui.character.status.achievables.AchievableUIMode;
import delta.games.lotro.gui.character.status.achievables.aggregator.AggregatedGeoItemsMapWindowController;
import delta.games.lotro.gui.character.status.achievables.filter.AchievableStatusFilterController;
import delta.games.lotro.gui.character.status.achievables.statistics.AchievablesStatisticsWindowController;
import delta.games.lotro.gui.character.status.deeds.form.DeedStatusDialogController;
import delta.games.lotro.gui.character.status.deeds.table.DeedStatusTableController;
import delta.games.lotro.gui.character.status.quests.BlacklistController;
import delta.games.lotro.gui.lore.deeds.filter.DeedFilterController;
import delta.games.lotro.gui.lore.items.FilterUpdateListener;
import delta.games.lotro.gui.main.GlobalPreferences;
import delta.games.lotro.lore.deeds.DeedDescription;
import delta.games.lotro.lore.quests.AchievablesUtils;
import delta.games.lotro.stats.deeds.SyncDeedsStatusAndReputationStatus;
import delta.games.lotro.utils.events.EventsManager;

/**
 * Controller for a deeds status edition window.
 * @author DAM
 */
public class DeedsStatusWindowController extends DefaultFormDialogController<AchievablesStatusManager> implements FilterUpdateListener
{
  private static final int MAX_HEIGHT=900;

  // Data
  private CharacterFile _toon;
  private List<DeedDescription> _deeds;
  private DeedStatusFilter _filter;
  // Controllers
  private AchievableStatusFilterController _statusFilterController;
  private DeedFilterController _filterController;
  private DeedsStatusPanelController _panelController;
  private DeedStatusTableController _tableController;
  private BlacklistController<AchievableStatus> _blacklistController;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param status Status to edit.
   * @param toon Parent toon.
   */
  public DeedsStatusWindowController(WindowController parent, AchievablesStatusManager status, CharacterFile toon)
  {
    super(parent,status);
    _toon=toon;
    _deeds=AchievablesUtils.getDeeds(_toon.getSummary());
    _filter=new DeedStatusFilter();
  }

  @Override
  protected JDialog build()
  {
    JDialog dialog=super.build();
    dialog.setMinimumSize(new Dimension(400,300));
    dialog.setTitle("Deeds status edition"); // I18n
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
    Blacklist blacklist=BlackListIO.load(_toon,false);
    initTable(blacklist);
    _panelController=new DeedsStatusPanelController(this,_tableController);
    JPanel tablePanel=_panelController.getPanel();
    // Deed filter
    _filterController=new DeedFilterController(this,_filter.getDeedFilter(),this,false);
    JPanel deedFilterPanel=_filterController.getPanel();
    TitledBorder deedFilterBorder=GuiFactory.buildTitledBorder("Deed Filter"); // I18n
    deedFilterPanel.setBorder(deedFilterBorder);
    // Deed status filter
    _statusFilterController=new AchievableStatusFilterController(_filter,this);
    JPanel statusFilterPanel=_statusFilterController.getPanel();
    TitledBorder statusFilterBorder=GuiFactory.buildTitledBorder("Status Filter"); // I18n
    statusFilterPanel.setBorder(statusFilterBorder);
    // Blacklist
    _filter.setBlacklist(blacklist);
    _blacklistController=new BlacklistController<AchievableStatus>(blacklist,_tableController.getTableController(),this,_filter.getBlacklistFilter());
    JPanel blacklistPanel=_blacklistController.getPanel();
    filterUpdated();
    // Buttons panel
    JPanel buttonsPanel=buildButtonsPanel();
    // Whole panel
    GridBagConstraints c=new GridBagConstraints(0,0,3,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(deedFilterPanel,c);
    c=new GridBagConstraints(0,1,1,1,0.0,0.0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(statusFilterPanel,c);
    c=new GridBagConstraints(1,1,1,1,0.0,0.0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(blacklistPanel,c);
    c=new GridBagConstraints(2,1,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(buttonsPanel,c);
    c=new GridBagConstraints(3,1,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(Box.createGlue(),c);
    c=new GridBagConstraints(0,2,4,1,1.0,1.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
    panel.add(tablePanel,c);
    return panel;
  }

  private JPanel buildButtonsPanel()
  {
    JPanel ret=GuiFactory.buildPanel(new FlowLayout());
    // Stats button
    {
      JButton statsButton=GuiFactory.buildButton("Stats"); // I18n
      ActionListener alStats=new ActionListener()
      {
        @Override
        public void actionPerformed(ActionEvent e)
        {
          showStatistics();
        }
      };
      statsButton.addActionListener(alStats);
      ret.add(statsButton);
    }
    // Maps button
    {
      JButton mapsButton=GuiFactory.buildButton("Maps"); // I18n
      ActionListener alMaps=new ActionListener()
      {
        @Override
        public void actionPerformed(ActionEvent e)
        {
          showMaps();
        }
      };
      mapsButton.addActionListener(alMaps);
      ret.add(mapsButton);
    }
    return ret;
  }

  private void initTable(Blacklist blacklist)
  {
    TypedProperties prefs=GlobalPreferences.getGlobalProperties("DeedsStatus"); // I18n
    _tableController=new DeedStatusTableController(this,_data,prefs,_filter,_deeds,this,blacklist);
    GenericTableController<AchievableStatus> tableController=_tableController.getTableController();
    JTable table=tableController.getTable();
    table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    ActionListener al=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent event)
      {
        String action=event.getActionCommand();
        if (GenericTableController.DOUBLE_CLICK.equals(action))
        {
          AchievableStatus status=(AchievableStatus)event.getSource();
          editDeedStatus(status);
        }
      }
    };
    _tableController.getTableController().addActionListener(al);
  }

  @Override
  public void filterUpdated()
  {
    _panelController.filterUpdated();
    List<DeedDescription> selectedDeeds=getSelectedDeeds();
    AchievablesStatisticsWindowController<DeedDescription> statisticsController=getStatisticsWindow();
    if (statisticsController!=null)
    {
      statisticsController.updateStats(selectedDeeds);
    }
    AggregatedGeoItemsMapWindowController mapsController=getMapsWindow();
    if (mapsController!=null)
    {
      mapsController.setAchievables(selectedDeeds);
    }
  }

  private List<DeedDescription> getSelectedDeeds()
  {
    List<DeedDescription> ret=new ArrayList<DeedDescription>();
    for(DeedDescription deed : _deeds)
    {
      AchievableStatus status=_data.get(deed,false);
      if (_filter.accept(status))
      {
        ret.add(deed);
      }
    }
    return ret;
  }

  private void editDeedStatus(AchievableStatus status)
  {
    DeedStatusDialogController dialog=new DeedStatusDialogController(status,this);
    Window parentWindow=getWindow();
    dialog.getDialog().setLocationRelativeTo(parentWindow);
    AchievableStatus notNullIfOk=dialog.editModal();
    if (notNullIfOk!=null)
    {
      filterUpdated();
      _tableController.getTableController().refresh(status);
    }
  }

  @SuppressWarnings("unchecked")
  private AchievablesStatisticsWindowController<DeedDescription> getStatisticsWindow()
  {
    WindowsManager windowsMgr=getWindowsManager();
    AchievablesStatisticsWindowController<DeedDescription> ret=(AchievablesStatisticsWindowController<DeedDescription>)windowsMgr.getWindow(AchievablesStatisticsWindowController.IDENTIFIER);
    return ret;
  }

  private void showStatistics()
  {
    AchievablesStatisticsWindowController<DeedDescription> statisticsController=getStatisticsWindow();
    if (statisticsController==null)
    {
      statisticsController=new AchievablesStatisticsWindowController<DeedDescription>(this,_toon,_data,AchievableUIMode.DEED);
      statisticsController.updateStats(getSelectedDeeds());
      WindowsManager windowsMgr=getWindowsManager();
      windowsMgr.registerWindow(statisticsController);
      statisticsController.getWindow().setLocationRelativeTo(getWindow());
    }
    statisticsController.bringToFront();
  }

  private AggregatedGeoItemsMapWindowController getMapsWindow()
  {
    WindowsManager windowsMgr=getWindowsManager();
    AggregatedGeoItemsMapWindowController ret=(AggregatedGeoItemsMapWindowController)windowsMgr.getWindow(AggregatedGeoItemsMapWindowController.IDENTIFIER);
    return ret;
  }

  private void showMaps()
  {
    AggregatedGeoItemsMapWindowController mapsController=getMapsWindow();
    if (mapsController==null)
    {
      mapsController=new AggregatedGeoItemsMapWindowController(this,_data);
      mapsController.setAchievables(getSelectedDeeds());
      WindowsManager windowsMgr=getWindowsManager();
      windowsMgr.registerWindow(mapsController);
      mapsController.getWindow().setLocationRelativeTo(getWindow());
    }
    mapsController.bringToFront();
  }

  @Override
  protected void okImpl()
  {
    super.okImpl();
    // Sync reputation status
    {
      ReputationStatus reputationStatus=_toon.getReputation();
      SyncDeedsStatusAndReputationStatus.syncReputationStatus(_data,reputationStatus);
      _toon.saveReputation();
      CharacterEvent event=new CharacterEvent(CharacterEventType.CHARACTER_REPUTATION_UPDATED,_toon,null);
      EventsManager.invokeEvent(event);
    }
    // Sync trait points
    TraitPointsStatus pointsStatus=TraitPoints.get().load(_toon);
    ClassDescription characterClass=_toon.getSummary().getCharacterClass();
    SyncAchievablesStatusAndTraitPoints.syncTraitPointsFromDeeds(characterClass,pointsStatus,_data);
    TraitPoints.get().save(_toon,pointsStatus);
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    super.dispose();
    // Data
    _toon=null;
    _deeds=null;
    _filter=null;
    // Controllers
    if (_statusFilterController!=null)
    {
      _statusFilterController.dispose();
      _statusFilterController=null;
    }
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
    if (_blacklistController!=null)
    {
      _blacklistController.dispose();
      _blacklistController=null;
    }
  }
}
