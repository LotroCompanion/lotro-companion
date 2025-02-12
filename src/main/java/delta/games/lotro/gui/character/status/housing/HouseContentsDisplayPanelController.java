package delta.games.lotro.gui.character.status.housing;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

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
import delta.games.lotro.common.status.StatusMetadata;
import delta.games.lotro.gui.character.status.housing.filter.HousingItemFilterController;
import delta.games.lotro.gui.main.GlobalPreferences;

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
  private HousingItemFilterController _filterController;
  private GenericTablePanelController<HousingItem> _panelController;
  private HouseItemsTableController _tableController;

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
    _filterController=new HousingItemFilterController(_filter,_panelController);
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

  private JPanel buildTopPanel()
  {
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    // TODO Last update time
    StatusMetadata status=_houseContents.getStatusMetadata();
    long lastUpdateTime=status.getTimeStamp();
    // TODO Map button
    return ret;
  }

  @Override
  public void dispose()
  {
    super.dispose();
    // Data
    _houseContents=null;
    _filter=null;
    // Controllers
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
