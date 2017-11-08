package delta.games.lotro.gui.character.stats.curves;

import delta.games.lotro.character.stats.ratings.RatingCurve;

/**
 * Configuration of a stat curve.
 * @author DAM
 */
public class StatCurveConfiguration
{
  private String _title;
  private RatingCurve _curve;
  private int _level;
  private double _minRating;
  private double _maxRating;

  /**
   * Constructor.
   * @param title Curve title.
   * @param curve Curve to display.
   * @param level Level to use.
   * @param minRating Minimum rating.
   * @param maxRating Maximum rating.
   */
  public StatCurveConfiguration(String title, RatingCurve curve, int level, double minRating, double maxRating)
  {
    _title=title;
    _curve=curve;
    _level=level;
    _minRating=minRating;
    _maxRating=maxRating;
  }

  /**
   * Get the title for the curve.
   * @return a title.
   */
  public String getTitle()
  {
    return _title;
  }

  /**
   * Get the curve to display.
   * @return the curve to display.
   */
  public RatingCurve getCurve()
  {
    return _curve;
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
}
