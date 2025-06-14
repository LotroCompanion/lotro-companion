package delta.games.lotro.gui.travels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import delta.games.lotro.lore.travels.map.TravelsMap;
import delta.games.lotro.lore.travels.map.TravelsMapLabel;
import delta.games.lotro.maps.data.GeoPoint;
import delta.games.lotro.maps.data.GeoReference;
import delta.games.lotro.maps.data.MapPoint;
import delta.games.lotro.maps.data.Marker;
import delta.games.lotro.maps.ui.MapView;
import delta.games.lotro.maps.ui.layers.BaseVectorLayer;

/**
 * Layer for housing item offsets.
 * @author DAM
 */
public class TravelsMapLabelsLayer extends BaseVectorLayer
{
  private List<TravelsMapLabel> _labels;

  /**
   * Constructor.
   * @param map Map data.
   */
  public TravelsMapLabelsLayer(TravelsMap map)
  {
    setName("Labels");
    _labels=map.getLabels();
  }

  @Override
  public int getPriority()
  {
    return 10;
  }

  @Override
  public List<? extends MapPoint> getVisiblePoints()
  {
    return new ArrayList<Marker>();
  }

  /**
   * Paint the layer.
   * @param view Parent view.
   * @param g Graphics.
   */
  @Override
  public void paintLayer(MapView view, Graphics g)
  {
    for(TravelsMapLabel label : _labels)
    {
      paintLabel(view,label,g);
    }
  }

  private void paintLabel(MapView view, TravelsMapLabel label, Graphics g)
  {
    Dimension position=label.getUIPosition();
    GeoReference viewReference=view.getViewReference();
    Dimension uiPosition=viewReference.geo2pixel(new GeoPoint(position.width,-position.height));
    char[] chars=label.getText().toCharArray();
    g.setColor(Color.GREEN);
    g.drawChars(chars,0,chars.length,uiPosition.width,uiPosition.height);
  }
}
