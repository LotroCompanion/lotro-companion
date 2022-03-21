package delta.games.lotro.gui.character.status.achievables.aggregator;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;
import java.util.Objects;

import javax.swing.JFrame;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.combobox.ComboBoxController;
import delta.common.ui.swing.combobox.ItemSelectionListener;
import delta.common.ui.swing.windows.DefaultWindowController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.status.achievables.AchievableStatus;
import delta.games.lotro.character.status.achievables.AchievablesStatusManager;
import delta.games.lotro.gui.character.status.achievables.map.AchievableGeoPointsMapPanelController;
import delta.games.lotro.lore.maps.MapDescription;
import delta.games.lotro.lore.quests.Achievable;

/**
 * Controller for a window to show aggregated maps of achievables geo items.
 * @author DAM
 */
public class AggregatedGeoItemsMapWindowController extends DefaultWindowController
{
  /**
   * Identifier for this window.
   */
  public static final String IDENTIFIER="AGGREGATED_GEO_ITEMS_MAP";

  // Controllers
  private ComboBoxController<AggregatedGeoItemsMap> _mapChooser;
  private AchievableGeoPointsMapPanelController _mapPanelCtrl;
  // UI
  private JPanel _mainPanel;
  private JPanel _mapPanel;
  // Data
  private AchievablesStatusManager _statusMgr;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param statusMgr Achievables status manager to use.
   */
  public AggregatedGeoItemsMapWindowController(WindowController parent, AchievablesStatusManager statusMgr)
  {
    _statusMgr=statusMgr;
    _mapChooser=buildMapChooser();
    _mapPanel=GuiFactory.buildPanel(new BorderLayout());
    _mainPanel=buildMainPanel();
  }

  @Override
  protected JFrame build()
  {
    JFrame frame=super.build();
    frame.setTitle("Aggregated geographic items map");
    frame.setMinimumSize(new Dimension(400,300));
    frame.pack();
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
    return _mainPanel;
  }

  private JPanel buildMainPanel()
  {
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    // Map chooser
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    JPanel mapChooserPanel=buildChooserPanel();
    ret.add(mapChooserPanel,c);
    // Map panel
    c=new GridBagConstraints(0,1,1,1,1.0,1.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
    ret.add(_mapPanel,c);
    return ret;
  }

  private JPanel buildChooserPanel()
  {
    JPanel ret=GuiFactory.buildPanel(new FlowLayout());
    ret.add(GuiFactory.buildLabel("Map: "));
    ret.add(_mapChooser.getComboBox());
    return ret;
  }

  private ComboBoxController<AggregatedGeoItemsMap> buildMapChooser()
  {
    ComboBoxController<AggregatedGeoItemsMap> ret=new ComboBoxController<AggregatedGeoItemsMap>();
    ItemSelectionListener<AggregatedGeoItemsMap> listener=new ItemSelectionListener<AggregatedGeoItemsMap>()
    {
      @Override
      public void itemSelected(AggregatedGeoItemsMap map)
      {
        selectMap(map);
      }
    };
    ret.addListener(listener);
    return ret;
  }

  /**
   * Set the achievables to show.
   * @param achievables Achievables to show.
   */
  public void setAchievables(List<Achievable> achievables)
  {
    AggregatedGeoItemsManager mgr=new AggregatedGeoItemsManager();
    for(Achievable achievable : achievables)
    {
      AchievableStatus status=_statusMgr.get(achievable,true);
      mgr.addAchievableStatus(status);
    }
    updateMapChooser(mgr);
  }

  private void updateMapChooser(AggregatedGeoItemsManager mgr)
  {
    AggregatedGeoItemsMap currentMap=_mapChooser.getSelectedItem();
    _mapChooser.removeAllItems();
    List<AggregatedGeoItemsMap> maps=mgr.getMaps();
    for(AggregatedGeoItemsMap map : maps)
    {
      _mapChooser.addItem(map,map.getName());
    }
    // Attempt to select the previous map
    AggregatedGeoItemsMap toSelect=chooseMapToShow(currentMap,maps);
    selectMap(toSelect);
  }

  private AggregatedGeoItemsMap chooseMapToShow(AggregatedGeoItemsMap currentMap, List<AggregatedGeoItemsMap> maps)
  {
    AggregatedGeoItemsMap toSelect=null;
    if (currentMap!=null)
    {
      MapDescription oldMapDescription=currentMap.getMap();
      Integer oldMapId=oldMapDescription.getMapId();
      if (oldMapId!=null)
      {
        for(AggregatedGeoItemsMap newMap : maps)
        {
          Integer newMapId=newMap.getMap().getMapId();
          if (Objects.equals(oldMapId,newMapId))
          {
            toSelect=newMap;
            break;
          }
        }
      }
    }
    if (toSelect==null)
    {
      if (maps.size()>0)
      {
        toSelect=maps.get(0);
      }
    }
    return toSelect;
  }

  private void selectMap(AggregatedGeoItemsMap aggregatedMap)
  {
    _mapPanel.removeAll();
    if (_mapPanelCtrl!=null)
    {
      _mapPanelCtrl.dispose();
    }
    if (aggregatedMap!=null)
    {
      MapDescription map=aggregatedMap.getMap();
      _mapPanelCtrl=new AchievableGeoPointsMapPanelController(map,aggregatedMap.getPoints(),false,null);
      _mapPanel.add(_mapPanelCtrl.getMapComponent(),BorderLayout.CENTER);
    }
    _mainPanel.revalidate();
    _mainPanel.repaint();
  }

}
