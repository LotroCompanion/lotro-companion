package delta.games.lotro.gui.travels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
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
    Rectangle position=label.getUIPosition();
    GeoReference viewReference=view.getViewReference();
    String text=label.getText().toUpperCase();
    int x=position.x+(position.width/2);
    int y=-position.y-(position.height/2);
    Dimension uiPosition=viewReference.geo2pixel(new GeoPoint(x,y));
    char[] chars=text.toCharArray();
    g.setColor(Color.GREEN);
    Rectangle2D bounds=getTextBounds(g,text);
    int ascent=g.getFontMetrics().getAscent();
    int xDraw=uiPosition.width-(int)(bounds.getWidth()/2);
    int yDraw=uiPosition.height-(int)(bounds.getHeight()/2)+ascent;
    g.drawChars(chars,0,chars.length,xDraw,yDraw);
  }

  private Rectangle2D getTextBounds(Graphics g, String text)
  {
    return g.getFontMetrics().getStringBounds(text,g);
  }
}
