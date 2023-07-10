package delta.games.lotro.gui.maps.global;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import org.apache.log4j.Logger;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.misc.Disposable;
import delta.common.utils.NumericTools;
import delta.games.lotro.common.comparators.NamedComparator;
import delta.games.lotro.lore.maps.ParchmentMap;
import delta.games.lotro.lore.maps.ParchmentMapsManager;
import delta.games.lotro.maps.ui.navigation.NavigationSupport;

/**
 * Controller for the maps navigation menu bar.
 * @author DAM
 */
public class NavigationMenuController implements Disposable
{
  private static final Logger LOGGER=Logger.getLogger(NavigationMenuController.class);

  private JMenuBar _menuBar;
  private ActionListener _actionListener;
  private NavigationSupport _navigation;

  /**
   * Constructor.
   * @param navigation Navigation manager.
   */
  public NavigationMenuController(NavigationSupport navigation)
  {
    _navigation=navigation;
    init();
  }

  /**
   * Get the managed menu bar.
   * @return the managed menu bar.
   */
  public JMenuBar getMenuBar()
  {
    return _menuBar;
  }

  private void init()
  {
    _actionListener=buildActionListener();
    _menuBar=GuiFactory.buildMenuBar();
    ParchmentMapsManager mapsManager=ParchmentMapsManager.getInstance();
    ParchmentMap rootMap=mapsManager.getRootMap();
    if (rootMap==null)
    {
      LOGGER.warn("Root map not found!");
      return;
    }

    JMenu middleEarthMenu=buildMiddleEarthMenu();
    _menuBar.add(middleEarthMenu);
    List<ParchmentMap> regionMaps=mapsManager.getChildMaps(rootMap.getIdentifier());
    for(ParchmentMap regionMap : regionMaps)
    {
      JMenu regionMenu=buildMenu(regionMap,false);
      _menuBar.add(regionMenu);
    }
  }

  private JMenu buildMiddleEarthMenu()
  {
    ParchmentMapsManager mapsManager=ParchmentMapsManager.getInstance();
    ParchmentMap rootMap=mapsManager.getRootMap();
    List<ParchmentMap> maps=mapsManager.getChildMaps(rootMap.getIdentifier());
    JMenu menu=GuiFactory.buildMenu(rootMap.getName());
    // Root map
    JMenuItem rootMapMenu=buildMenuItem(rootMap);
    menu.add(rootMapMenu);
    // Region maps
    for(ParchmentMap childMap : maps)
    {
      JMenuItem mapMenu=buildMenuItem(childMap);
      menu.add(mapMenu);
    }
    return menu;
  }

  private JMenu buildMenu(ParchmentMap parentMap, boolean includeParent)
  {
    List<ParchmentMap> maps=getChildMaps(parentMap);
    JMenu menu=GuiFactory.buildMenu(parentMap.getName());
    if (includeParent)
    {
      JMenuItem parentMapMenu=buildMenuItem(parentMap);
      menu.add(parentMapMenu);
    }
    for(ParchmentMap childMap : maps)
    {
      if (!useMap(childMap))
      {
        continue;
      }
      boolean isSubMenu=isSubMenu(childMap);
      if (isSubMenu)
      {
        JMenu subMenu=buildMenu(childMap,true);
        menu.add(subMenu);
      }
      else
      {
        JMenuItem mapMenu=buildMenuItem(childMap);
        menu.add(mapMenu);
      }
    }
    return menu;
  }

  private JMenuItem buildMenuItem(ParchmentMap map)
  {
    JMenuItem mapMenu=GuiFactory.buildMenuItem(map.getName());
    mapMenu.setActionCommand(String.valueOf(map.getIdentifier()));
    mapMenu.addActionListener(_actionListener);
    return mapMenu;
  }

  private boolean isSubMenu(ParchmentMap map)
  {
    int mapId=map.getIdentifier();
    if (mapId==268449764) return true; // Western Rohan
    if (mapId==268448107) return true; // East Rohan
    if (mapId==268442355) return true; // Moria
    if (mapId==268437653) return true; // Bree-land
    return false;
  }

  private boolean useMap(ParchmentMap map)
  {
    if ("".equals(map.getName()))
    {
      return false;
    }
    return true;
  }

  private List<ParchmentMap> getChildMaps(ParchmentMap map)
  {
    List<ParchmentMap> ret=new ArrayList<ParchmentMap>();
    getAllChildMaps(map,ret);
    Collections.sort(ret,new NamedComparator());
    return ret;
  }

  private void getAllChildMaps(ParchmentMap map, List<ParchmentMap> storage)
  {
    ParchmentMapsManager mapsManager=ParchmentMapsManager.getInstance();
    List<ParchmentMap> childMaps=mapsManager.getChildMaps(map.getIdentifier());
    storage.addAll(childMaps);
    for(ParchmentMap childMap : childMaps)
    {
      if (!isSubMenu(childMap))
      {
        getAllChildMaps(childMap,storage);
      }
    }
  }

  private ActionListener buildActionListener()
  {
    ActionListener actionListener=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        String command=e.getActionCommand();
        Integer mapId=NumericTools.parseInteger(command);
        if (mapId!=null)
        {
          _navigation.requestMap(mapId.intValue());
        }
      }
    };
    return actionListener;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    if (_menuBar!=null)
    {
      _menuBar.removeAll();
      _menuBar=null;
    }
  }
}
