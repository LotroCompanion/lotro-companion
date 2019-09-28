package delta.games.lotro.gui.stats.deeds;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.windows.DefaultFormDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.events.CharacterEvent;
import delta.games.lotro.character.events.CharacterEventType;
import delta.games.lotro.character.reputation.ReputationStatus;
import delta.games.lotro.gui.deed.filter.DeedFilter;
import delta.games.lotro.gui.deed.filter.DeedFilterController;
import delta.games.lotro.gui.main.GlobalPreferences;
import delta.games.lotro.lore.deeds.DeedDescription;
import delta.games.lotro.stats.deeds.DeedStatus;
import delta.games.lotro.stats.deeds.DeedsStatusManager;
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
  private DeedFilterController _filterController;
  private DeedsStatusEditionPanelController _panelController;
  private DeedStatusTableController _tableController;
  private DeedFilter _filter;

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
    _filter=new DeedFilter();
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
    initDeedsTable();
    _panelController=new DeedsStatusEditionPanelController(this,_tableController);
    JPanel tablePanel=_panelController.getPanel();
    // Filter
    _filterController=new DeedFilterController(_filter,_panelController);
    JPanel filterPanel=_filterController.getPanel();
    TitledBorder filterBorder=GuiFactory.buildTitledBorder("Filter");
    filterPanel.setBorder(filterBorder);
    // Whole panel
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,1,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(filterPanel,c);
    c.gridy=1;c.weighty=1;c.fill=GridBagConstraints.BOTH;
    panel.add(tablePanel,c);
    return panel;
  }

  private void initDeedsTable()
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
          DeedDescription deed=(DeedDescription)event.getSource();
          editDeedStatus(deed);
        }
      }
    };
    _tableController.getTableController().addActionListener(al);
  }

  private void editDeedStatus(DeedDescription deed)
  {
    String deedKey=deed.getIdentifyingKey();
    DeedStatus status=_data.get(deedKey,true);
    DeedStatusEditionDialogController dialog=new DeedStatusEditionDialogController(deed,status,this);
    status=dialog.editModal();
    if (status!=null)
    {
      _tableController.getTableController().refresh(deed);
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
