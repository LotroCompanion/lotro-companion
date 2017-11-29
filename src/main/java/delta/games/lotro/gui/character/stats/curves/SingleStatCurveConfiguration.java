package delta.games.lotro.gui.character.stats.curves;

import delta.games.lotro.character.stats.ratings.RatingCurve;

/**
 * Configuration of a single stat curve.
 * @author DAM
 */
public class SingleStatCurveConfiguration
{
  private String _title;
  private RatingCurve _curve;

  /**
   * Constructor.
   * @param title Curve title.
   * @param curve Curve to display.
   */
  public SingleStatCurveConfiguration(String title, RatingCurve curve)
  {
    _title=title;
    _curve=curve;
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
}
