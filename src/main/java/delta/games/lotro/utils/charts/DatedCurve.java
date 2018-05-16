package delta.games.lotro.utils.charts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A curve of dated points.
 * @param <VALUE> Type of values.
 * @author DAM
 */
public class DatedCurve<VALUE extends Number>
{
  private String _name;
  private List<DatedCurveItem<VALUE>> _values;

  /**
   * Constructor.
   * @param name Name of curve.
   */
  public DatedCurve(String name)
  {
    _name=name;
    _values=new ArrayList<DatedCurveItem<VALUE>>();
  }

  /**
   * Get the name of this curve.
   * @return A displayable name.
   */
  public String getName()
  {
    return _name;
  }

  /**
   * Add a point in this curve.
   * @param date Date of point.
   * @param value Value of point.
   */
  public void addValue(long date, VALUE value)
  {
    DatedCurveItem<VALUE> item=new DatedCurveItem<VALUE>(date,value);
    _values.add(item);
  }

  /**
   * Get the managed points.
   * @return a list of curve points.
   */
  public List<DatedCurveItem<VALUE>> getPoints()
  {
    return _values;
  }

  /**
   * Finish edition of this curve.
   */
  public void finish()
  {
    // Sort
    Collections.sort(_values,new DatedCurveItemDateComparator());
  }
}
