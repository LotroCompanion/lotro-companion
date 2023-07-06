package delta.games.lotro.gui.character.stats.curves;

import java.util.ArrayList;
import java.util.List;

import delta.games.lotro.character.stats.ratings.RatingCurve;
import delta.games.lotro.common.stats.StatDescription;

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
  private StatDescription _baseStat;
  private List<SingleStatCurveConfiguration> _curves;

  /**
   * Constructor.
   * @param title Chart title.
   * @param baseStat Base stat.
   */
  public StatCurvesChartConfiguration(String title, StatDescription baseStat)
  {
    _title=title;
    _level=1;
    _minRating=0;
    _maxRating=0;
    _baseStat=baseStat;
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
   * Set level to use.
   * @param level Level to use.
   */
  public void setLevel(int level)
  {
    _level=level;
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

  /**
   * Set the max rating to use.
   * @param maxRating the maximum rating to use.
   */
  public void setMaxRating(double maxRating)
  {
    _maxRating=maxRating;
  }

  /**
   * Get the stat used to build this chart.
   * @return a stat.
   */
  public StatDescription getBaseStat()
  {
    return _baseStat;
  }

  /**
   * Add a curve.
   * @param curveConfiguration Curve configuration.
   */
  public void addCurve(SingleStatCurveConfiguration curveConfiguration)
  {
    _curves.add(curveConfiguration);
    _maxRating=getAutoMaxRating();
  }

  /**
   * Get the curves for this chart.
   * @return A list of curve configurations.
   */
  public List<SingleStatCurveConfiguration> getCurveConfigurations()
  {
    return _curves;
  }

  /**
   * Automagically compute value of the maximum rating for this curve.
   * @return A rating value.
   */
  public double getAutoMaxRating()
  {
    double max=0;
    for(SingleStatCurveConfiguration config : _curves)
    {
      RatingCurve curve=config.getCurve();
      Double rating=curve.getRatingForCap(_level);
      //System.out.println("Found rating "+rating+" for cap of curve "+config.getTitle()+" at level "+_level);
      if (rating!=null)
      {
        if (rating.doubleValue()>max)
        {
          max=rating.doubleValue();
        }
      }
    }
    return max;
  }
}
