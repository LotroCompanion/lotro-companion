package delta.games.lotro.gui.character.status.quests;

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
import delta.common.ui.swing.windows.DefaultDisplayDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.common.ui.swing.windows.WindowsManager;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.status.achievables.AchievableStatus;
import delta.games.lotro.character.status.achievables.AchievablesStatusManager;
import delta.games.lotro.character.status.achievables.filter.QuestStatusFilter;
import delta.games.lotro.common.blacklist.Blacklist;
import delta.games.lotro.common.blacklist.io.BlackListIO;
import delta.games.lotro.gui.character.status.achievables.AchievableUIMode;
import delta.games.lotro.gui.character.status.achievables.filter.AchievableStatusFilterController;
import delta.games.lotro.gui.character.status.achievables.statistics.AchievablesStatisticsWindowController;
import delta.games.lotro.gui.character.status.quests.form.QuestStatusDialogController;
import delta.games.lotro.gui.character.status.quests.table.QuestStatusTableController;
import delta.games.lotro.gui.lore.items.FilterUpdateListener;
import delta.games.lotro.gui.lore.quests.filter.QuestFilterController;
import delta.games.lotro.gui.main.GlobalPreferences;
import delta.games.lotro.lore.quests.AchievablesUtils;
import delta.games.lotro.lore.quests.QuestDescription;

/**
 * Controller for a quests status display window.
 * @author DAM
 */
public class QuestsStatusWindowController extends DefaultDisplayDialogController<AchievablesStatusManager> implements FilterUpdateListener
{
  /**
   * Window identifier.
   */
  public static final String IDENTIFIER="QUESTS_STATUS";

  private static final int MAX_HEIGHT=800;

  // Data
  private CharacterFile _toon;
  private List<QuestDescription> _quests;
  private QuestStatusFilter _filter;
  // Controllers
  private AchievableStatusFilterController _statusFilterController;
  private QuestFilterController _filterController;
  private QuestsStatusPanelController _panelController;
  private QuestStatusTableController _tableController;
  private BlacklistController<AchievableStatus> _blacklistController;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param status Status to show.
   * @param toon Parent toon.
   */
  public QuestsStatusWindowController(WindowController parent, AchievablesStatusManager status, CharacterFile toon)
  {
    super(parent,status);
    _toon=toon;
    _quests=AchievablesUtils.getQuests(_toon.getSummary());
    _filter=new QuestStatusFilter();
  }

  @Override
  protected JDialog build()
  {
    JDialog dialog=super.build();
    dialog.setMinimumSize(new Dimension(400,300));
    dialog.setTitle("Quests status"); // I18n
    dialog.pack();
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
    Blacklist blacklist=BlackListIO.load(_toon,true);
    initTable(blacklist);
    _panelController=new QuestsStatusPanelController(this,_tableController);
    JPanel tablePanel=_panelController.getPanel();
    // Quest filter
    _filterController=new QuestFilterController(this,_filter.getQuestFilter(),this,false);
    JPanel questFilterPanel=_filterController.getPanel();
    TitledBorder questFilterBorder=GuiFactory.buildTitledBorder("Quest Filter"); // I18n
    questFilterPanel.setBorder(questFilterBorder);
    // Status filter
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
    panel.add(questFilterPanel,c);
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
    return ret;
  }

  private void initTable(Blacklist blacklist)
  {
    TypedProperties prefs=GlobalPreferences.getGlobalProperties("QuestsStatus");
    _tableController=new QuestStatusTableController(this,_data,prefs,_filter,_quests,blacklist);
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
          showQuestStatus(status);
        }
      }
    };
    tableController.addActionListener(al);
  }

  @Override
  public void filterUpdated()
  {
    _panelController.filterUpdated();
    List<QuestDescription> selectedQuests=getSelectedQuests();
    AchievablesStatisticsWindowController<QuestDescription> statisticsController=getStatisticsWindow();
    if (statisticsController!=null)
    {
      statisticsController.updateStats(selectedQuests);
    }
  }

  private List<QuestDescription> getSelectedQuests()
  {
    List<QuestDescription> ret=new ArrayList<QuestDescription>();
    for(QuestDescription quest : _quests)
    {
      AchievableStatus status=_data.get(quest,false);
      if (_filter.accept(status))
      {
        ret.add(quest);
      }
    }
    return ret;
  }

  private void showQuestStatus(AchievableStatus status)
  {
    QuestStatusDialogController dialog=new QuestStatusDialogController(status,this);
    Window parentWindow=getWindow();
    dialog.getDialog().setLocationRelativeTo(parentWindow);
    dialog.show(false);
  }

  @SuppressWarnings("unchecked")
  private AchievablesStatisticsWindowController<QuestDescription> getStatisticsWindow()
  {
    WindowsManager windowsMgr=getWindowsManager();
    AchievablesStatisticsWindowController<QuestDescription> ret=(AchievablesStatisticsWindowController<QuestDescription>)windowsMgr.getWindow(AchievablesStatisticsWindowController.IDENTIFIER);
    return ret;
  }

  private void showStatistics()
  {
    AchievablesStatisticsWindowController<QuestDescription> statisticsController=getStatisticsWindow();
    if (statisticsController==null)
    {
      statisticsController=new AchievablesStatisticsWindowController<QuestDescription>(this,_toon,_data,AchievableUIMode.QUEST);
      statisticsController.updateStats(getSelectedQuests());
      WindowsManager windowsMgr=getWindowsManager();
      windowsMgr.registerWindow(statisticsController);
      statisticsController.getWindow().setLocationRelativeTo(getWindow());
    }
    statisticsController.bringToFront();
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
    _toon=null;
    _quests=null;
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
