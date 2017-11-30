package delta.games.lotro.gui.character.stats.curves;

import java.util.ArrayList;
import java.util.List;

import delta.games.lotro.character.stats.STAT;
import delta.games.lotro.character.stats.ratings.RatingCurve;

/**
 * Configuration of a single stat curve.
 * @author DAM
 */
public class SingleStatCurveConfiguration
{
  private String _title;
  private RatingCurve _curve;
  private List<STAT> _stats;

  /**
   * Constructor.
   * @param title Curve title.
   * @param curve Curve to display.
   */
  public SingleStatCurveConfiguration(String title, RatingCurve curve)
  {
    _title=title;
    _curve=curve;
    _stats=new ArrayList<STAT>();
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
   * Add an associated stat.
   * @param stat Stat to add.
   */
  public void addStat(STAT stat)
  {
    _stats.add(stat);
  }

  /**
   * Get the associated stats.
   * @return A list of stats.
   */
  public List<STAT> getStats()
  {
    return _stats;
  }
}
