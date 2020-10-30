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
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import delta.common.ui.swing.GuiFactory;
import delta.common.utils.NumericTools;
import delta.games.lotro.dat.data.DataFacade;
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
  // UI
  private JPanel _mapPanel;
  private JPanel _panel;
  // Data
  private DataFacade _facade;

  /**
   * Constructor.
   * @param facade Data facade.
   */
  public ResourcesMapsExplorerPanelController(DataFacade facade)
  {
    _facade=facade;
    _mapPanel=GuiFactory.buildPanel(new BorderLayout());
    _mapSelection=new MapSelectionPanelController(this);
    _craftingLevelSelection=new CraftingLevelSelectionPanelController(_mapSelection);
    _items=new ItemsController(null); // TODO Add parent
    _panel=buildPanel();
  }

  @Override
  public void actionPerformed(ActionEvent e)
  {
    JButton source=(JButton)e.getSource();
    _mapSelection.selectMap(source);

    String actionCommand=source.getActionCommand();
    int mapId=NumericTools.parseInt(actionCommand,0);
    if (mapId==0)
    {
      return;
    }
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
    // Set items
    ResourceNodesLootManager mgr=new ResourceNodesLootManager();
    List<Item> items=mgr.getLoots(craftingLevel);
    _items.setItems(items);
    // Build new map panel
    _mapPanelCtrl=new ResourcesMapPanelController(_facade,map,mapId);
    _mapPanel.add(_mapPanelCtrl.getMapPanelController().getLayers(),BorderLayout.CENTER);
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
    GridBagConstraints c=new GridBagConstraints(1,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    JPanel craftingLevelSelectionPanel=_craftingLevelSelection.getPanel();
    craftingLevelSelectionPanel.setBorder(GuiFactory.buildTitledBorder("Choose crafting level:"));
    panel.add(craftingLevelSelectionPanel,c);
    // Items
    c=new GridBagConstraints(2,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    JPanel itemsPanel=_items.getPanel();
    itemsPanel.setBorder(GuiFactory.buildTitledBorder("Items:"));
    panel.add(itemsPanel,c);
    // Map selection
    c=new GridBagConstraints(0,0,1,2,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.VERTICAL,new Insets(5,5,5,5),0,0);
    JPanel mapSelectionPanel=_mapSelection.getPanel();
    mapSelectionPanel.setBorder(GuiFactory.buildTitledBorder("Choose map:"));
    panel.add(mapSelectionPanel,c);
    // Map
    c=new GridBagConstraints(1,1,2,1,1.0,1.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
    _mapPanel.setBorder(GuiFactory.buildTitledBorder("Map"));
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
