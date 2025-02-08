package delta.games.lotro.gui.character.status.housing.map;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import delta.common.ui.swing.draw.Arrows;
import delta.common.utils.math.geometry.Vector3D;
import delta.games.lotro.character.status.housing.HousingItem;
import delta.games.lotro.common.geo.Position;
import delta.games.lotro.dat.data.DatPosition;
import delta.games.lotro.dat.loaders.PositionDecoder;
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
public class HouseItemOffsetLayer extends BaseVectorLayer
{
  private List<HousingItem> _items;

  /**
   * Constructor.
   */
  public HouseItemOffsetLayer()
  {
    setName("Offsets");
    _items=new ArrayList<HousingItem>();
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
   * Set the items to use.
   * @param items Items to use.
   */
  public void setItems(List<HousingItem> items)
  {
    _items.clear();
    _items.addAll(items);
  }

  /**
   * Paint the links.
   * @param view Parent view.
   * @param g Graphics.
   */
  @Override
  public void paintLayer(MapView view, Graphics g)
  {
    if (!_items.isEmpty())
    {
      for(HousingItem item : _items)
      {
        paintOffset(view,item,g);
      }
    }
  }

  private void paintOffset(MapView view, HousingItem item, Graphics g)
  {
    Vector3D offset=item.getPositionOffset();
    float length=Math.abs(offset.getX())+Math.abs(offset.getY())+Math.abs(offset.getZ());
    if (length<0.0001)
    {
      return;
    }
    Position position=item.getPosition();
    GeoReference viewReference=view.getViewReference();
    // Item position
    Dimension itemPosition=viewReference.geo2pixel(new GeoPoint(position.getLongitude(),position.getLatitude()));

    // Compute hook position
    DatPosition datPosition=PositionDecoder.fromLatLon(position.getLongitude(),position.getLatitude());
    Vector3D datVector=datPosition.getPosition();
    double cos=Math.cos(item.getHookRotation()*Math.PI/180);
    double sin=Math.sin(item.getHookRotation()*Math.PI/180);
    double hookX=datVector.getX()-offset.getX()*cos-offset.getY()*sin;
    double hookY=datVector.getY()+offset.getX()*sin-offset.getY()*cos;
    double hookZ=datVector.getZ()-offset.getZ();
    datPosition.setPosition((float)hookX,(float)hookY,(float)hookZ);
    float[] hookLonLat=datPosition.getLonLat();
    Dimension hookPosition=viewReference.geo2pixel(new GeoPoint(hookLonLat[0],hookLonLat[1]));

    int x=hookPosition.width;
    int y=hookPosition.height;
    g.fillRect(x-3,y-3,6,6);
    g.setColor(Color.GREEN);
    int toX=itemPosition.width;
    int toY=itemPosition.height;
    g.drawLine(x,y,toX,toY);
    Arrows.drawArrow((Graphics2D)g,x,y,x+(toX-x)/2,y+(toY-y)/2);
  }
}
