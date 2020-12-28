package delta.games.lotro.gui.stats.deeds;

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
import delta.games.lotro.character.achievables.DeedsStatusManager;
import delta.games.lotro.character.achievables.filter.DeedStatusFilter;
import delta.games.lotro.character.events.CharacterEvent;
import delta.games.lotro.character.events.CharacterEventType;
import delta.games.lotro.character.reputation.ReputationStatus;
import delta.games.lotro.gui.deed.filter.DeedFilterController;
import delta.games.lotro.gui.main.GlobalPreferences;
import delta.games.lotro.gui.stats.deeds.filter.DeedStatusFilterController;
import delta.games.lotro.gui.stats.deeds.form.DeedStatusEditionDialogControllerNew;
import delta.games.lotro.gui.stats.deeds.table.DeedStatusTableController;
import delta.games.lotro.stats.deeds.SyncDeedsStatusAndReputationStatus;
import delta.games.lotro.utils.events.EventsManager;

/**
 * Controller for a traits points edition window.
 * @author DAM
 */
public class DeedsStatusEditionWindowController extends DefaultFormDialogController<DeedsStatusManager>
{
  // Data
  private CharacterFile _toon;
  // Controllers
  private DeedStatusFilterController _statusFilterController;
  private DeedFilterController _filterController;
  private DeedsStatusEditionPanelController _panelController;
  private DeedStatusTableController _tableController;
  private DeedStatusFilter _filter;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param status Status to edit.
   * @param toon Parent toon.
   */
  public DeedsStatusEditionWindowController(WindowController parent, DeedsStatusManager status, CharacterFile toon)
  {
    super(parent,status);
    _toon=toon;
    _filter=new DeedStatusFilter();
  }

  @Override
  protected JDialog build()
  {
    JDialog dialog=super.build();
    dialog.setMinimumSize(new Dimension(400,300));
    dialog.setSize(900,dialog.getHeight());
    dialog.setTitle("Deeds status edition");
    return dialog;
  }

  @Override
  protected JPanel buildFormPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    // Table
    initTable();
    _panelController=new DeedsStatusEditionPanelController(this,_tableController);
    JPanel tablePanel=_panelController.getPanel();
    // Deed filter
    _filterController=new DeedFilterController(_filter.getDeedFilter(),_panelController);
    JPanel deedFilterPanel=_filterController.getPanel();
    TitledBorder deedFilterBorder=GuiFactory.buildTitledBorder("Deed Filter");
    deedFilterPanel.setBorder(deedFilterBorder);
    // Deed status filter
    _statusFilterController=new DeedStatusFilterController(_filter,_panelController);
    JPanel statusFilterPanel=_statusFilterController.getPanel();
    TitledBorder statusFilterBorder=GuiFactory.buildTitledBorder("Status Filter");
    statusFilterPanel.setBorder(statusFilterBorder);
    // Whole panel
    GridBagConstraints c=new GridBagConstraints(0,0,2,1,1,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(deedFilterPanel,c);
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
    TypedProperties prefs=GlobalPreferences.getGlobalProperties("DeedsStatus");
    _tableController=new DeedStatusTableController(_data,prefs,_filter);
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

  private void editDeedStatus(AchievableStatus status)
  {
    DeedStatusEditionDialogControllerNew dialog=new DeedStatusEditionDialogControllerNew(status,this);
    Window parentWindow=getWindow();
    dialog.getDialog().setLocationRelativeTo(parentWindow);
    status=dialog.editModal();
    if (status!=null)
    {
      _tableController.getTableController().refresh(status);
    }
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
