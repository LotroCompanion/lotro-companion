package delta.games.lotro.gui.maps.resources;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.WindowController;
import delta.common.utils.NumericTools;
import delta.games.lotro.dat.data.DataFacade;
import delta.games.lotro.gui.maps.resources.filter.ResourceNodeFilterWindowController;
import delta.games.lotro.lore.crafting.CraftingLevel;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.maps.resources.ResourcesMapDescriptor;
import delta.games.lotro.lore.maps.resources.ResourcesMapsManager;

/**
 * Controller for the resources maps explorer.
 * @author DAM
 */
public class ResourcesMapsExplorerPanelController implements ActionListener
{
  // Controllers
  private CraftingLevelSelectionPanelController _craftingLevelSelection;
  private ItemsController _items;
  private MapSelectionPanelController _mapSelection;
  private ResourcesMapPanelController _mapPanelCtrl;
  private JButton _filterButton;
  private ResourceNodeFilterWindowController _filterWindow;
  // UI
  private JPanel _mapPanel;
  private JPanel _panel;
  // Data
  private DataFacade _facade;

  /**
   * Constructor.
   * @param parent Parent window controller.
   * @param facade Data facade.
   */
  public ResourcesMapsExplorerPanelController(WindowController parent, DataFacade facade)
  {
    _facade=facade;
    _mapPanel=GuiFactory.buildPanel(new BorderLayout());
    _mapSelection=new MapSelectionPanelController(this);
    _craftingLevelSelection=new CraftingLevelSelectionPanelController(_mapSelection);
    _items=new ItemsController(parent);
    initFilter(parent);
    _panel=buildPanel();
  }

  private void initFilter(WindowController parent)
  {
    _filterButton=GuiFactory.buildButton("Filter..."); // I18n
    ActionListener al=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        if (_filterWindow.getWindow().isVisible())
        {
          _filterWindow.hide();
        }
        else
        {
          _filterWindow.pack();
          _filterWindow.getWindow().setLocationRelativeTo(_filterButton);
          _filterWindow.show(false);
        }
      }
    };
    _filterButton.addActionListener(al);
    _filterWindow=new ResourceNodeFilterWindowController(parent);
  }

  @Override
  public void actionPerformed(ActionEvent e)
  {
    int mapId=NumericTools.parseInt(e.getActionCommand(),0);
    //System.out.println("Select map: "+mapId);
    CraftingLevel craftingLevel=_craftingLevelSelection.getCraftingLevel();
    ResourcesMapDescriptor descriptor=ResourcesMapsManager.getInstance().getMapForLevel(craftingLevel);
    changeMap(craftingLevel,descriptor,mapId);
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  public JPanel getPanel()
  {
    return _panel;
  }

  private void changeMap(CraftingLevel craftingLevel, ResourcesMapDescriptor map, int mapId)
  {
    // Cleanup
    _mapPanel.removeAll();
    if (_mapPanelCtrl!=null)
    {
      _mapPanelCtrl.dispose();
      _mapPanelCtrl=null;
    }
    // Build new map panel
    _mapPanelCtrl=new ResourcesMapPanelController(_facade,map,mapId);
    _mapPanelCtrl.setFilter(_filterWindow.getFilter());
    _mapPanel.add(_mapPanelCtrl.getMapPanelController().getLayers(),BorderLayout.CENTER);

    // Set items
    ResourceNodesLootManager mgr=new ResourceNodesLootManager(craftingLevel);
    List<Integer> itemIds=_mapPanelCtrl.getItems();
    List<Item> sourceItems=mgr.buildItemsList(itemIds);
    sourceItems=mgr.sortItems(sourceItems);
    _items.setItems(mgr.getGlobalLoots(sourceItems));
    // Update filter
    _filterWindow.setItems(mgr,sourceItems);
    // Pack
    Window window=(Window)SwingUtilities.getRoot(_panel);
    if (window!=null)
    {
      window.pack();
    }
  }

  private JPanel buildPanel()
  {
    JPanel panel=GuiFactory.buildBackgroundPanel(new GridBagLayout());
    // Crafting level selection
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    JPanel craftingLevelSelectionPanel=_craftingLevelSelection.getPanel();
    craftingLevelSelectionPanel.setBorder(GuiFactory.buildTitledBorder("Choose crafting level:")); // I18n
    panel.add(craftingLevelSelectionPanel,c);
    // Map selection
    c=new GridBagConstraints(1,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    JPanel mapSelectionPanel=GuiFactory.buildPanel(new BorderLayout());
    JComponent mapSelection=_mapSelection.getComboBox().getComboBox();
    mapSelectionPanel.add(mapSelection,BorderLayout.CENTER);
    mapSelectionPanel.setBorder(GuiFactory.buildTitledBorder("Choose map:")); // I18n
    panel.add(mapSelectionPanel,c);
    // Items
    c=new GridBagConstraints(2,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    JPanel itemsPanel=_items.getPanel();
    itemsPanel.setBorder(GuiFactory.buildTitledBorder("Items:")); // I18n
    panel.add(itemsPanel,c);
    // Filter...
    c=new GridBagConstraints(3,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    panel.add(_filterButton,c);
    // Map
    c=new GridBagConstraints(0,1,4,1,1.0,1.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
    _mapPanel.setBorder(GuiFactory.buildTitledBorder("Map")); // I18n
    panel.add(_mapPanel,c);
    return panel;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    _facade=null;
    if (_craftingLevelSelection!=null)
    {
      _craftingLevelSelection.dispose();
      _craftingLevelSelection=null;
    }
    if (_items!=null)
    {
      _items.dispose();
      _items=null;
    }
    _filterButton=null;
    if (_filterWindow!=null)
    {
      _filterWindow.dispose();
      _filterWindow=null;
    }
    if (_mapSelection!=null)
    {
      _mapSelection.dispose();
      _mapSelection=null;
    }
    if (_mapPanelCtrl!=null)
    {
      _mapPanelCtrl.dispose();
      _mapPanelCtrl=null;
    }
    if (_mapPanel!=null)
    {
      _mapPanel.removeAll();
      _mapPanel=null;
    }
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
  }
}
