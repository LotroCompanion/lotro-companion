package delta.games.lotro.gui.lore.instances.explorer;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.windows.DefaultWindowController;
import delta.common.ui.swing.windows.WindowController;
import delta.common.ui.swing.windows.WindowsManager;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.gui.lore.instances.InstanceEntriesFilter;
import delta.games.lotro.gui.lore.instances.InstancesFilterController;
import delta.games.lotro.gui.lore.instances.InstancesTableController;
import delta.games.lotro.gui.main.GlobalPreferences;
import delta.games.lotro.gui.maps.instances.InstanceMapsWindowController;
import delta.games.lotro.lore.instances.InstanceTreeEntry;
import delta.games.lotro.lore.instances.SkirmishPrivateEncounter;

/**
 * Controller for the instances explorer window.
 * @author DAM
 */
public class InstancesExplorerWindowController extends DefaultWindowController
{
  /**
   * Identifier for this window.
   */
  public static final String IDENTIFIER="INSTANCES_EXPLORER";

  private InstancesFilterController _filterController;
  private InstancesExplorerPanelController _panelController;
  private InstancesTableController _tableController;
  private InstanceEntriesFilter _filter;
  private WindowsManager _formWindows;

  /**
   * Constructor.
   * @param parent Parent window.
   */
  public InstancesExplorerWindowController(WindowController parent)
  {
    super(parent);
    _filter=new InstanceEntriesFilter();
    _formWindows=new WindowsManager();
  }

  @Override
  protected JFrame build()
  {
    JFrame frame=super.build();
    frame.setTitle("Instances explorer"); // I18n
    frame.setMinimumSize(new Dimension(400,300));
    frame.setSize(950,700);
    return frame;
  }

  @Override
  public void configureWindow()
  {
    automaticLocationSetup();
  }

  @Override
  public String getWindowIdentifier()
  {
    return IDENTIFIER;
  }

  @Override
  protected JPanel buildContents()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    // Table
    initInstancesTable();
    _panelController=new InstancesExplorerPanelController(this,_tableController);
    JPanel tablePanel=_panelController.getPanel();
    // Filter
    _filterController=new InstancesFilterController(_filter,_panelController);
    JPanel filterPanel=_filterController.getPanel();
    TitledBorder filterBorder=GuiFactory.buildTitledBorder("Filter"); // I18n
    filterPanel.setBorder(filterBorder);
    // Whole panel
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(filterPanel,c);
    c=new GridBagConstraints(1,0,1,1,1,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(GuiFactory.buildPanel(null),c);
    c=new GridBagConstraints(0,1,2,1,1,1,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
    panel.add(tablePanel,c);
    return panel;
  }

  private void initInstancesTable()
  {
    TypedProperties prefs=GlobalPreferences.getGlobalProperties("InstancesExplorer");
    _tableController=new InstancesTableController(prefs,_filter);
    ActionListener al=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent event)
      {
        String action=event.getActionCommand();
        if (GenericTableController.DOUBLE_CLICK.equals(action))
        {
          InstanceTreeEntry entry=(InstanceTreeEntry)event.getSource();
          SkirmishPrivateEncounter instance=entry.getInstance();
          showInstance(instance);
        }
      }
    };
    _tableController.addActionListener(al);
  }

  private void showInstance(SkirmishPrivateEncounter instance)
  {
    String id=InstanceMapsWindowController.getId(instance.getIdentifier());
    WindowController window=_formWindows.getWindow(id);
    if (window==null)
    {
      window=new InstanceMapsWindowController(instance);
      Window w=window.getWindow();
      w.setLocationRelativeTo(getWindow());
      _formWindows.registerWindow(window);
    }
    window.show();
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    saveBoundsPreferences();
    super.dispose();
    if (_formWindows!=null)
    {
      _formWindows.disposeAll();
      _formWindows=null;
    }
    if (_tableController!=null)
    {
      _tableController.dispose();
      _tableController=null;
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
  }
}
