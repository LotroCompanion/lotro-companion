package delta.games.lotro.gui.maps.resources;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import delta.common.ui.swing.combobox.ComboBoxController;
import delta.common.ui.swing.combobox.ComboBoxItem;
import delta.common.ui.swing.combobox.ComboBoxItemComparator;
import delta.common.ui.swing.combobox.ItemSelectionListener;
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
  // Controllers
  private ComboBoxController<Integer> _maps;
  // UI
  private ActionListener _actionListener;

  /**
   * Constructor.
   * @param actionListener Action listener for buttons.
   */
  public MapSelectionPanelController(ActionListener actionListener)
  {
    _actionListener=actionListener;
    _maps=new ComboBoxController<Integer>();
    ItemSelectionListener<Integer> listener=new ItemSelectionListener<Integer>()
    {
      @Override
      public void itemSelected(Integer mapId)
      {
        selectMap(mapId.intValue());
      }
    };
    _maps.addListener(listener);
  }

  /**
   * Init map buttons.
   * @param level Crafting level to use.
   */
  public void initMaps(CraftingLevel level)
  {
    List<ComboBoxItem<Integer>> items=new ArrayList<ComboBoxItem<Integer>>();
    ResourcesMapsManager mapsMgr=ResourcesMapsManager.getInstance();
    ResourcesMapDescriptor descriptor=mapsMgr.getMapForLevel(level);
    List<Integer> mapIds=descriptor.getMapIds();
    for(Integer mapId : mapIds)
    {
      GeoreferencedBasemap basemap=getBasemap(mapId.intValue());
      String mapName=basemap.getName();
      ComboBoxItem<Integer> item=new ComboBoxItem<Integer>(mapId,mapName);
      items.add(item);
    }
    Collections.sort(items,new ComboBoxItemComparator<Integer>());
    _maps.updateItems(items);
    Integer selectedMap=_maps.getSelectedItem();
    if (selectedMap!=null)
    {
      selectMap(selectedMap.intValue());
    }
  }

  private void selectMap(int mapId)
  {
    ActionEvent e=new ActionEvent(this,ActionEvent.ACTION_FIRST,String.valueOf(mapId));
    _actionListener.actionPerformed(e);
  }

  /**
   * Get the managed combo-box controller.
   * @return a combo-box controller.
   */
  public ComboBoxController<Integer> getComboBox()
  {
    return _maps;
  }

  private GeoreferencedBasemap getBasemap(int mapId)
  {
    MapsManager mapsManager=Maps.getMaps().getMapsManager();
    GeoreferencedBasemapsManager basemapsManager=mapsManager.getBasemapsManager();
    GeoreferencedBasemap basemap=basemapsManager.getMapById(mapId);
    return basemap;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    if (_maps!=null)
    {
      _maps.dispose();
      _maps=null;
    }
    _actionListener=null;
  }
}
