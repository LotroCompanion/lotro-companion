package delta.games.lotro.gui.stats.quests;

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
import delta.common.ui.swing.windows.DefaultDisplayDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.common.ui.swing.windows.WindowsManager;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.achievables.AchievableStatus;
import delta.games.lotro.character.achievables.AchievablesStatusManager;
import delta.games.lotro.character.achievables.filter.QuestStatusFilter;
import delta.games.lotro.gui.items.FilterUpdateListener;
import delta.games.lotro.gui.main.GlobalPreferences;
import delta.games.lotro.gui.quests.filter.QuestFilterController;
import delta.games.lotro.gui.stats.achievables.AchievableUIMode;
import delta.games.lotro.gui.stats.achievables.filter.AchievableStatusFilterController;
import delta.games.lotro.gui.stats.achievables.statistics.AchievablesStatisticsWindowController;
import delta.games.lotro.gui.stats.quests.form.QuestStatusDialogController;
import delta.games.lotro.gui.stats.quests.table.QuestStatusTableController;
import delta.games.lotro.lore.quests.AchievablesUtils;
import delta.games.lotro.lore.quests.QuestDescription;

/**
 * Controller for a quests status display window.
 * @author DAM
 */
public class QuestsStatusWindowController extends DefaultDisplayDialogController<AchievablesStatusManager> implements FilterUpdateListener
{
  // Data
  private CharacterFile _toon;
  private List<QuestDescription> _quests;
  private QuestStatusFilter _filter;
  // Controllers
  private AchievableStatusFilterController _statusFilterController;
  private QuestFilterController _filterController;
  private QuestsStatusPanelController _panelController;
  private QuestStatusTableController _tableController;

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
    dialog.setSize(900,dialog.getHeight());
    dialog.setTitle("Quests status");
    return dialog;
  }

  @Override
  protected JPanel buildFormPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    // Table
    initTable();
    _panelController=new QuestsStatusPanelController(this,_tableController);
    JPanel tablePanel=_panelController.getPanel();
    // Quest filter
    _filterController=new QuestFilterController(_filter.getQuestFilter(),this,false);
    JPanel questFilterPanel=_filterController.getPanel();
    TitledBorder questFilterBorder=GuiFactory.buildTitledBorder("Quest Filter");
    questFilterPanel.setBorder(questFilterBorder);
    // Status filter
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
    panel.add(questFilterPanel,c);
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
    TypedProperties prefs=GlobalPreferences.getGlobalProperties("QuestsStatus");
    _tableController=new QuestStatusTableController(_data,prefs,_filter,_quests);
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
    _tableController.getTableController().addActionListener(al);
  }

  @Override
  public void filterUpdated()
  {
    _panelController.filterUpdated();
    AchievablesStatisticsWindowController<QuestDescription> statisticsController=getStatisticsWindow();
    if (statisticsController!=null)
    {
      statisticsController.updateStats();
    }
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
    WindowsManager windowsMgr=getWindowsManager();
    AchievablesStatisticsWindowController<QuestDescription> statisticsController=getStatisticsWindow();
    if (statisticsController==null)
    {
      statisticsController=new AchievablesStatisticsWindowController<QuestDescription>(this,_toon,_data,_quests,_filter.getQuestFilter(),AchievableUIMode.QUEST);
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
  }
}
