package delta.games.lotro.gui.stats.curves;

import delta.games.lotro.utils.charts.DatedCurve;

/**
 * Get dated curve from some custom data.
 * @param <T> Type of custom data.
 * @author DAM
 */
public interface DatedCurveProvider<T>
{
  /**
   * Get the dated curve to represent the given data.
   * @param source Source data.
   * @return A curve.
   */
  DatedCurve<?> getCurve(T source);
}
