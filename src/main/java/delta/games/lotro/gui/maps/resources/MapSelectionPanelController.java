package delta.games.lotro.gui.maps.resources;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.games.lotro.lore.crafting.CraftingLevel;
import delta.games.lotro.lore.maps.resources.ResourcesMapDescriptor;
import delta.games.lotro.lore.maps.resources.ResourcesMapsManager;
import delta.games.lotro.maps.data.MapsManager;
import delta.games.lotro.maps.data.basemaps.GeoreferencedBasemap;
import delta.games.lotro.maps.data.basemaps.GeoreferencedBasemapsManager;
import delta.games.lotro.utils.maps.Maps;

/**
 * Controller for a map selection panel (1 button per map)
 * @author DAM
 */
public class MapSelectionPanelController
{
  // UI
  private List<JButton> _mapButtons;
  private ActionListener _actionListener;
  private JPanel _panel;

  /**
   * Constructor.
   * @param actionListener Action listener for buttons.
   */
  public MapSelectionPanelController(ActionListener actionListener)
  {
    _actionListener=actionListener;
    _mapButtons=new ArrayList<JButton>();
    _panel=GuiFactory.buildPanel(new GridBagLayout());
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  public JPanel getPanel()
  {
    return _panel;
  }

  /**
   * Init map buttons.
   * @param level Crafting level to use.
   */
  public void initMapButtons(CraftingLevel level)
  {
    disposeButtons();
    ResourcesMapsManager mapsMgr=ResourcesMapsManager.getInstance();
    ResourcesMapDescriptor descriptor=mapsMgr.getMapForLevel(level);
    List<Integer> mapIds=descriptor.getMapIds();
    for(Integer mapId : mapIds)
    {
      GeoreferencedBasemap basemap=getBasemap(mapId.intValue());
      String mapName=basemap.getName();
      /*
      if (mapName.length()>25)
      {
        mapName=mapName.substring(0,25)+"...";
      }
      */
      JButton button=GuiFactory.buildButton(mapName);
      button.setActionCommand(mapId.toString());
      button.addActionListener(_actionListener);
      _mapButtons.add(button);
    }
    fillPanel();
    _panel.revalidate();
    _panel.repaint();
    if (_mapButtons.size()>0)
    {
      _mapButtons.get(0).doClick();
    }
  }

  private void fillPanel()
  {
    _panel.removeAll();
    int y=0;
    for(JButton button : _mapButtons)
    {
      GridBagConstraints c=new GridBagConstraints(0,y,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
      _panel.add(button,c);
      y++;
    }
    Component glue=Box.createGlue();
    GridBagConstraints c=new GridBagConstraints(0,y,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
    _panel.add(glue,c);
  }

  /**
   * Update UI to reflect the selected map.
   * @param source Button of the selected map.
   */
  public void selectMap(JButton source)
  {
    for(JButton button : _mapButtons)
    {
      button.setSelected(false);
      button.setEnabled(true);
    }
    source.setSelected(true);
    source.setEnabled(false);
  }

  private GeoreferencedBasemap getBasemap(int mapId)
  {
    MapsManager mapsManager=Maps.getMaps().getMapsManager();
    GeoreferencedBasemapsManager basemapsManager=mapsManager.getBasemapsManager();
    GeoreferencedBasemap basemap=basemapsManager.getMapById(mapId);
    return basemap;
  }

  private void disposeButtons()
  {
    _panel.removeAll();
    for(JButton button : _mapButtons)
    {
      button.removeActionListener(_actionListener);
    }
    _mapButtons.clear();
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    if (_panel!=null)
    {
      disposeButtons();
      _panel=null;
      _actionListener=null;
    }
  }
}
