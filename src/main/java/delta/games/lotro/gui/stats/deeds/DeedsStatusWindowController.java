package delta.games.lotro.gui.stats.deeds;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.windows.DefaultFormDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.common.ui.swing.windows.WindowsManager;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.achievables.AchievableStatus;
import delta.games.lotro.character.achievables.AchievablesStatusManager;
import delta.games.lotro.character.achievables.SyncAchievablesStatusAndTraitPoints;
import delta.games.lotro.character.achievables.filter.DeedStatusFilter;
import delta.games.lotro.character.events.CharacterEvent;
import delta.games.lotro.character.events.CharacterEventType;
import delta.games.lotro.character.reputation.ReputationStatus;
import delta.games.lotro.character.traitPoints.TraitPoints;
import delta.games.lotro.character.traitPoints.TraitPointsStatus;
import delta.games.lotro.common.CharacterClass;
import delta.games.lotro.gui.deed.filter.DeedFilterController;
import delta.games.lotro.gui.items.FilterUpdateListener;
import delta.games.lotro.gui.main.GlobalPreferences;
import delta.games.lotro.gui.stats.achievables.AchievableUIMode;
import delta.games.lotro.gui.stats.achievables.filter.AchievableStatusFilterController;
import delta.games.lotro.gui.stats.achievables.statistics.AchievablesStatisticsWindowController;
import delta.games.lotro.gui.stats.deeds.form.DeedStatusDialogController;
import delta.games.lotro.gui.stats.deeds.table.DeedStatusTableController;
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
    dialog.setTitle("Deeds status edition");
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
    _panelController=new DeedsStatusPanelController(this,_tableController);
    JPanel tablePanel=_panelController.getPanel();
    // Deed filter
    _filterController=new DeedFilterController(_filter.getDeedFilter(),this,false);
    JPanel deedFilterPanel=_filterController.getPanel();
    TitledBorder deedFilterBorder=GuiFactory.buildTitledBorder("Deed Filter");
    deedFilterPanel.setBorder(deedFilterBorder);
    // Deed status filter
    _statusFilterController=new AchievableStatusFilterController(_filter,this);
    JPanel statusFilterPanel=_statusFilterController.getPanel();
    TitledBorder statusFilterBorder=GuiFactory.buildTitledBorder("Status Filter");
    statusFilterPanel.setBorder(statusFilterBorder);
    // Stats button
    JButton b=GuiFactory.buildButton("Stats");
    ActionListener al=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        showStatistics();
      }
    };
    b.addActionListener(al);
    // Whole panel
    GridBagConstraints c=new GridBagConstraints(0,0,3,1,1,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(deedFilterPanel,c);
    c=new GridBagConstraints(0,1,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(statusFilterPanel,c);
    c=new GridBagConstraints(1,1,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(b,c);
    c=new GridBagConstraints(2,1,1,1,1,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(Box.createGlue(),c);
    c=new GridBagConstraints(0,2,3,1,1,1,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
    panel.add(tablePanel,c);
    return panel;
  }

  private void initTable()
  {
    TypedProperties prefs=GlobalPreferences.getGlobalProperties("DeedsStatus");
    _tableController=new DeedStatusTableController(_data,prefs,_filter,_deeds,this);
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
    AchievablesStatisticsWindowController<DeedDescription> statisticsController=getStatisticsWindow();
    if (statisticsController!=null)
    {
      statisticsController.updateStats();
    }
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
      statisticsController=new AchievablesStatisticsWindowController<DeedDescription>(this,_toon,_data,_deeds,_filter.getDeedFilter(),AchievableUIMode.DEED);
      WindowsManager windowsMgr=getWindowsManager();
      windowsMgr.registerWindow(statisticsController);
      statisticsController.getWindow().setLocationRelativeTo(getWindow());
    }
    statisticsController.bringToFront();
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
    CharacterClass characterClass=_toon.getSummary().getCharacterClass();
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
  }
}
