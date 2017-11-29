package delta.games.lotro.gui.character.stats.curves;

import java.util.ArrayList;
import java.util.List;

/**
 * Configuration of a stat curve.
 * @author DAM
 */
public class StatCurvesChartConfiguration
{
  private String _title;
  private int _level;
  private double _minRating;
  private double _maxRating;
  private List<SingleStatCurveConfiguration> _curves;

  /**
   * Constructor.
   * @param title Chart title.
   * @param level Level to use.
   * @param minRating Minimum rating.
   * @param maxRating Maximum rating.
   */
  public StatCurvesChartConfiguration(String title, int level, double minRating, double maxRating)
  {
    _title=title;
    _level=level;
    _minRating=minRating;
    _maxRating=maxRating;
    _curves=new ArrayList<SingleStatCurveConfiguration>();
  }

  /**
   * Get the title for the chart.
   * @return a title.
   */
  public String getTitle()
  {
    return _title;
  }

  /**
   * Get the level to use.
   * @return the level to use.
   */
  public int getLevel()
  {
    return _level;
  }

  /**
   * Get the minimum rating to use.
   * @return the minimum rating to use.
   */
  public double getMinRating()
  {
    return _minRating;
  }

  /**
   * Get the maximum rating to use.
   * @return the maximum rating to use.
   */
  public double getMaxRating()
  {
    return _maxRating;
  }

  public void addCurve(SingleStatCurveConfiguration curveConfiguration)
  {
    _curves.add(curveConfiguration);
  }

  public List<SingleStatCurveConfiguration> getCurveConfigurations()
  {
    return _curves;
  }
}
