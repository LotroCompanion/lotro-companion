package delta.games.lotro.gui.character.status.relics;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.navigator.PageIdentifier;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.windows.DefaultDisplayDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.character.status.relics.RelicsInventoryEntry;
import delta.games.lotro.character.status.relics.RelicsInventoryManager;
import delta.games.lotro.character.status.relics.filter.RelicInventoryEntryFilter;
import delta.games.lotro.gui.character.status.relics.table.RelicsInventoryTableController;
import delta.games.lotro.gui.common.navigation.ReferenceConstants;
import delta.games.lotro.gui.lore.items.FilterUpdateListener;
import delta.games.lotro.gui.lore.items.legendary.relics.RelicsFilterController;
import delta.games.lotro.gui.main.GlobalPreferences;
import delta.games.lotro.gui.utils.NavigationUtils;
import delta.games.lotro.lore.items.legendary.relics.Relic;
import delta.games.lotro.lore.items.legendary.relics.RelicsManager;

/**
 * Controller for a relics status display window.
 * @author DAM
 */
public class RelicsInventoryWindowController extends DefaultDisplayDialogController<RelicsInventoryManager> implements FilterUpdateListener
{
  /**
   * Window identifier.
   */
  public static final String IDENTIFIER="RELICS_STATUS";

  private static final int MAX_HEIGHT=800;

  // Data
  private List<Relic> _relics;
  private RelicInventoryEntryFilter _filter;
  // Controllers
  private RelicsFilterController _filterController;
  private RelicsInventoryPanelController _panelController;
  private RelicsInventoryTableController _tableController;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param status Status to show.
   */
  public RelicsInventoryWindowController(WindowController parent, RelicsInventoryManager status)
  {
    super(parent,status);
    _relics=new ArrayList<Relic>(RelicsManager.getInstance().getAll());
    _filter=new RelicInventoryEntryFilter();
  }

  @Override
  protected JDialog build()
  {
    JDialog dialog=super.build();
    dialog.setMinimumSize(new Dimension(600,300));
    dialog.setTitle("Relics inventory"); // I18n
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
    initTable();
    _panelController=new RelicsInventoryPanelController(this,_tableController);
    JPanel tablePanel=_panelController.getPanel();
    // Build child controllers
    _filterController=new RelicsFilterController(_filter.getRelicFilter(),_relics);
    _filterController.setFilterUpdateListener(this);
    // Whole panel
    // - filter
    JPanel filterPanel=_filterController.getPanel();
    filterPanel.setBorder(GuiFactory.buildTitledBorder("Relic Filter")); // I18n
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(filterPanel,c);
    // - table
    c=new GridBagConstraints(0,1,1,1,1,1,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
    panel.add(tablePanel,c);
    return panel;
  }

  private void initTable()
  {
    TypedProperties prefs=GlobalPreferences.getGlobalProperties("RelicsInventory");
    _tableController=new RelicsInventoryTableController(_data,prefs,_filter,_relics);
    ActionListener al=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent event)
      {
        String action=event.getActionCommand();
        if (GenericTableController.DOUBLE_CLICK.equals(action))
        {
          RelicsInventoryEntry entry=(RelicsInventoryEntry)event.getSource();
          showRelic(entry.getRelic());
        }
      }
    };
    _tableController.getTableController().addActionListener(al);
  }

  @Override
  public void filterUpdated()
  {
    _panelController.filterUpdated();
  }

  private void showRelic(Relic relic)
  {
    PageIdentifier ref=ReferenceConstants.getRelicReference(relic.getIdentifier());
    NavigationUtils.navigateTo(ref,this);
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
