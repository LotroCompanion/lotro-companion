package delta.games.lotro.gui.stats.quests;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.windows.DefaultFormDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.achievables.AchievableStatus;
import delta.games.lotro.character.achievables.AchievablesStatusManager;
import delta.games.lotro.character.achievables.filter.QuestStatusFilter;
import delta.games.lotro.gui.main.GlobalPreferences;
import delta.games.lotro.gui.quests.filter.QuestFilterController;
import delta.games.lotro.gui.stats.achievables.filter.AchievableStatusFilterController;
import delta.games.lotro.gui.stats.quests.form.QuestStatusDisplayDialogController;
import delta.games.lotro.gui.stats.quests.table.QuestStatusTableController;

/**
 * Controller for a quests status edition window.
 * @author DAM
 */
public class QuestsStatusEditionWindowController extends DefaultFormDialogController<AchievablesStatusManager>
{
  // Data
  @SuppressWarnings("unused")
  private CharacterFile _toon;
  // Controllers
  private AchievableStatusFilterController _statusFilterController;
  private QuestFilterController _filterController;
  private QuestsStatusEditionPanelController _panelController;
  private QuestStatusTableController _tableController;
  private QuestStatusFilter _filter;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param status Status to edit.
   * @param toon Parent toon.
   */
  public QuestsStatusEditionWindowController(WindowController parent, AchievablesStatusManager status, CharacterFile toon)
  {
    super(parent,status);
    _toon=toon;
    _filter=new QuestStatusFilter();
  }

  @Override
  protected JDialog build()
  {
    JDialog dialog=super.build();
    dialog.setMinimumSize(new Dimension(400,300));
    dialog.setSize(900,dialog.getHeight());
    dialog.setTitle("Quests status edition");
    return dialog;
  }

  @Override
  protected JPanel buildFormPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    // Table
    initTable();
    _panelController=new QuestsStatusEditionPanelController(this,_tableController);
    JPanel tablePanel=_panelController.getPanel();
    // Quest filter
    _filterController=new QuestFilterController(_filter.getQuestFilter(),_panelController);
    JPanel questFilterPanel=_filterController.getPanel();
    TitledBorder questFilterBorder=GuiFactory.buildTitledBorder("Quest Filter");
    questFilterPanel.setBorder(questFilterBorder);
    // Status filter
    _statusFilterController=new AchievableStatusFilterController(_filter,_panelController);
    JPanel statusFilterPanel=_statusFilterController.getPanel();
    TitledBorder statusFilterBorder=GuiFactory.buildTitledBorder("Status Filter");
    statusFilterPanel.setBorder(statusFilterBorder);
    // Whole panel
    GridBagConstraints c=new GridBagConstraints(0,0,2,1,1,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(questFilterPanel,c);
    c=new GridBagConstraints(0,1,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(statusFilterPanel,c);
    c=new GridBagConstraints(1,1,1,1,1,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(Box.createGlue(),c);
    c=new GridBagConstraints(0,2,2,1,1,1,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);

    panel.add(tablePanel,c);
    return panel;
  }

  private void initTable()
  {
    TypedProperties prefs=GlobalPreferences.getGlobalProperties("QuestsStatus");
    _tableController=new QuestStatusTableController(_data,prefs,_filter);
    ActionListener al=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent event)
      {
        String action=event.getActionCommand();
        if (GenericTableController.DOUBLE_CLICK.equals(action))
        {
          AchievableStatus status=(AchievableStatus)event.getSource();
          editQuestStatus(status);
        }
      }
    };
    _tableController.getTableController().addActionListener(al);
  }

  private void editQuestStatus(AchievableStatus status)
  {
    QuestStatusDisplayDialogController dialog=new QuestStatusDisplayDialogController(status,this);
    Window parentWindow=getWindow();
    dialog.getDialog().setLocationRelativeTo(parentWindow);
    dialog.show(false);
  }

  @Override
  protected void okImpl()
  {
    super.okImpl();
    // TODO Sync trait points
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    super.dispose();
    _toon=null;
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
    _filter=null;
  }
}
