package delta.games.lotro.gui.character.status.housing;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.panels.AbstractPanelController;
import delta.common.ui.swing.tables.panel.GenericTablePanelController;
import delta.common.ui.swing.windows.WindowController;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.character.status.housing.HouseContents;
import delta.games.lotro.character.status.housing.HousingItem;
import delta.games.lotro.character.status.housing.filter.HousingItemFilter;
import delta.games.lotro.gui.character.status.housing.filter.HousingItemFilterController;
import delta.games.lotro.gui.character.status.housing.map.HouseMapWindowController;
import delta.games.lotro.gui.common.status.StatusMetadataPanelController;
import delta.games.lotro.gui.main.GlobalPreferences;
import delta.games.lotro.gui.utils.l10n.Labels;

/**
 * Controller for a panel to show the contents of a house (interior or exterior).
 * @author DAM
 */
public class HouseContentsDisplayPanelController extends AbstractPanelController
{
  // Data
  private HouseContents _houseContents;
  private HousingItemFilter _filter;
  // Controllers
  // - status
  private StatusMetadataPanelController _status;
  // - filter
  private HousingItemFilterController _filterController;
  // - table
  private GenericTablePanelController<HousingItem> _panelController;
  private HouseItemsTableController _tableController;
  // - map
  private HouseMapWindowController _mapController;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param houseContents Contents to show.
   */
  public HouseContentsDisplayPanelController(WindowController parent, HouseContents houseContents)
  {
    super(parent);
    _houseContents=houseContents;
    _filter=new HousingItemFilter();
    setPanel(buildPanel());
  }

  private JPanel buildPanel()
  {
    JPanel panel=GuiFactory.buildBackgroundPanel(new GridBagLayout());
    if (_houseContents==null)
    {
      return panel;
    }
    // Top panel
    JPanel topPanel=buildTopPanel();
    // Table
    List<HousingItem> items=_houseContents.getItems();
    WindowController parent=getWindowController();
    TypedProperties prefs=GlobalPreferences.getGlobalProperties("HouseContents");
    _tableController=new HouseItemsTableController(parent,prefs,items,_filter);
    _panelController=new GenericTablePanelController<HousingItem>(parent,_tableController.getTableController());
    _panelController.getConfiguration().setBorderTitle("Items"); // I18n
    _panelController.getCountsDisplay().setText("Item(s)"); // I18n
    JPanel tablePanel=_panelController.getPanel();
    // Filter
    _filterController=new HousingItemFilterController(_filter,items,_panelController);
    JPanel filterPanel=_filterController.getPanel();
    TitledBorder filterBorder=GuiFactory.buildTitledBorder(Labels.getLabel("shared.title.filter"));
    filterPanel.setBorder(filterBorder);
    // Whole panel
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,1,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(topPanel,c);
    c=new GridBagConstraints(0,1,1,1,1,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(filterPanel,c);
    c=new GridBagConstraints(0,2,1,1,1,1,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
    panel.add(tablePanel,c);
    return panel;
  }

  private JPanel buildTopPanel()
  {
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    // Status date
    _status=new StatusMetadataPanelController();
    _status.setData(_houseContents.getStatusMetadata());
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    ret.add(_status.getPanel(),c);
    // Glue
    c=new GridBagConstraints(0,0,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    ret.add(Box.createHorizontalGlue(),c);
    // Map button
    JButton mapButton=GuiFactory.buildButton("Map...");
    ActionListener al=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        doMap();
      }
    };
    mapButton.addActionListener(al);
    c=new GridBagConstraints(1,0,1,1,0.0,0.0,GridBagConstraints.EAST,GridBagConstraints.NONE,new Insets(0,5,0,5),0,0);
    ret.add(mapButton,c);
    return ret;
  }

  private void doMap()
  {
    if (_mapController==null)
    {
      WindowController parent=getWindowController();
      _mapController=new HouseMapWindowController(parent,_houseContents);
      Window window=_mapController.getWindow();
      WindowAdapter l=new WindowAdapter()
      {
        @Override
        public void windowClosed(WindowEvent e)
        {
          _mapController=null;
        }
      };
      window.addWindowListener(l);
    }
    _mapController.bringToFront();
  }

  @Override
  public void dispose()
  {
    super.dispose();
    // Data
    _houseContents=null;
    _filter=null;
    // Controllers
    // - status
    if (_status!=null)
    {
      _status.dispose();
      _status=null;
    }
    // - filter
    if (_filterController!=null)
    {
      _filterController.dispose();
      _filterController=null;
    }
    // - table
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
    // - map
    if (_mapController!=null)
    {
      _mapController.dispose();
      _mapController=null;
    }
  }
}
