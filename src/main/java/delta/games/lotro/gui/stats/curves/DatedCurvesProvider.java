package delta.games.lotro.gui.stats.curves;

import java.util.List;

import delta.games.lotro.utils.charts.DatedCurve;

/**
 * Provider for an ordered collection of dated curves.
 * @author DAM
 */
public interface DatedCurvesProvider
{
  /**
   * Get the identifiers for the curves to show.
   * @return a list of curve identifiers.
   */
  List<String> getCurveIds();

  /**
   * Get a curve using its identifier.
   * @param curveId Curve identifier.
   * @return A curve or <code>null</code> if not found.
   */
  public DatedCurve<?> getCurve(String curveId);
}
