package delta.games.lotro.gui.maps.resources;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import delta.common.ui.swing.GuiFactory;
import delta.common.utils.NumericTools;
import delta.games.lotro.dat.data.DataFacade;
import delta.games.lotro.lore.crafting.CraftingLevel;
import delta.games.lotro.lore.maps.resources.ResourcesMapDescriptor;
import delta.games.lotro.lore.maps.resources.ResourcesMapsManager;

/**
 * Controller for the resources maps explorer.
 * @author DAM
 */
public class ResourcesMapsExplorerPanelController implements ActionListener
{
  private DataFacade _facade;
  private CraftingLevelSelectionPanelController _craftingLevelSelection;
  private MapSelectionPanelController _mapSelection;
  private JPanel _mapPanel;
  private ResourcesMapPanelController _mapPanelCtrl;
  private JPanel _panel;

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
    changeMap(descriptor,mapId);
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  public JPanel getPanel()
  {
    return _panel;
  }

  private void changeMap(ResourcesMapDescriptor map, int mapId)
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
    GridBagConstraints c=new GridBagConstraints(1,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    JPanel craftingLevelSelectionPanel=_craftingLevelSelection.getPanel();
    craftingLevelSelectionPanel.setBorder(GuiFactory.buildTitledBorder("Choose crafting level:"));
    panel.add(craftingLevelSelectionPanel,c);
    c=new GridBagConstraints(0,0,1,2,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.VERTICAL,new Insets(5,5,5,5),0,0);
    JPanel mapSelectionPanel=_mapSelection.getPanel();
    mapSelectionPanel.setBorder(GuiFactory.buildTitledBorder("Choose map:"));
    panel.add(mapSelectionPanel,c);
    c=new GridBagConstraints(1,1,1,1,1.0,1.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
    _mapPanel.setBorder(GuiFactory.buildTitledBorder("Map"));
    panel.add(_mapPanel,c);
    return panel;
  }
}
