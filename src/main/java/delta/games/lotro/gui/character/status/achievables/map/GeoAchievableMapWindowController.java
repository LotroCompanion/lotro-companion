package delta.games.lotro.gui.character.status.achievables.map;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.DefaultDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.status.achievables.edition.AchievableGeoStatusManager;
import delta.games.lotro.character.status.achievables.edition.AchievableStatusGeoItem;
import delta.games.lotro.lore.maps.MapDescription;
import delta.games.lotro.lore.quests.Achievable;

/**
 * Controller for a window to show the status of a geographic achievable.
 * @author DAM
 */
public class GeoAchievableMapWindowController extends DefaultDialogController
{
  private List<AchievableGeoPointsMapPanelController> _maps;
  private JTabbedPane _ui;
  private boolean _editable;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param mgr Geo status manager.
   * @param editable Editable or not.
   */
  public GeoAchievableMapWindowController(WindowController parent, AchievableGeoStatusManager mgr, boolean editable)
  {
    super(parent);
    _editable=editable;
    init(mgr);
  }

  private void init(AchievableGeoStatusManager mgr)
  {
    _maps=new ArrayList<AchievableGeoPointsMapPanelController>();
    Achievable achievable=mgr.getAchievable();
    List<AchievableStatusGeoItem> points=mgr.getPoints();
    List<MapDescription> maps=achievable.getMaps();
    int mapIndex=0;
    for(MapDescription map : maps)
    {
      List<AchievableStatusGeoItem> mapPoints=new ArrayList<AchievableStatusGeoItem>();
      for(AchievableStatusGeoItem point : points)
      {
        if (point.getPoint().getMapIndex()==mapIndex)
        {
          mapPoints.add(point);
        }
      }
      AchievableGeoPointsMapPanelController panelCtrl=new AchievableGeoPointsMapPanelController(this,map,mapPoints,_editable,mgr);
      _maps.add(panelCtrl);
      mapIndex++;
    }
    _ui=buildUi();
  }

  private JTabbedPane buildUi()
  {
    JTabbedPane tabbedPane=GuiFactory.buildTabbedPane();
    for(AchievableGeoPointsMapPanelController mapPanelCtrl : _maps)
    {
      JPanel panel=GuiFactory.buildBackgroundPanel(new GridBagLayout());
      GridBagConstraints c=new GridBagConstraints(1,1,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
      panel.add(mapPanelCtrl.getMapComponent(),c);
      String title=mapPanelCtrl.getMapTitle();
      tabbedPane.add(title,panel);
    }
    return tabbedPane;
  }

  /**
   * Update UI.
   */
  public void updateUi()
  {
    for(AchievableGeoPointsMapPanelController panelCtrl : _maps)
    {
      panelCtrl.updateUi();
    }
  }

  @Override
  protected JComponent buildContents()
  {
    JPanel panel=GuiFactory.buildPanel(new BorderLayout());
    // Center
    panel.add(_ui,BorderLayout.CENTER);
    return panel;
  }

  @Override
  protected JDialog build()
  {
    JDialog dialog=super.build();
    // Title
    dialog.setTitle("Map"); // I18n
    dialog.pack();
    dialog.setResizable(false);
    return dialog;
  }

  @Override
  public void configureWindow()
  {
    automaticLocationSetup();
  }

  @Override
  public String getWindowIdentifier()
  {
    return "GEODEED MAP";
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    super.dispose();
    if (_maps!=null)
    {
      for(AchievableGeoPointsMapPanelController mapPanelCtrl : _maps)
      {
        mapPanelCtrl.dispose();
      }
      _maps.clear();
      _maps=null;
    }
    _ui=null;
  }
}
