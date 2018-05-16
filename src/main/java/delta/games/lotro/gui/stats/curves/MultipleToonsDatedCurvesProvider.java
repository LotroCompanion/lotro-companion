package delta.games.lotro.gui.stats.curves;

import java.util.ArrayList;
import java.util.List;

import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.stats.MultipleToonsStats;
import delta.games.lotro.utils.charts.DatedCurve;

/**
 * Dated curves provider that uses stats for multiple toons.
 * @param <T> Typed of managed stats.
 * @author DAM
 */
public class MultipleToonsDatedCurvesProvider<T> implements DatedCurvesProvider
{
  private DatedCurveProvider<T> _curveProvider;
  private MultipleToonsStats<T> _stats;

  /**
   * Constructor.
   * @param stats Stats to show.
   * @param curveProvider Curves provider.
   */
  public MultipleToonsDatedCurvesProvider(MultipleToonsStats<T> stats, DatedCurveProvider<T> curveProvider)
  {
    _stats=stats;
    _curveProvider=curveProvider;
  }

  @Override
  public List<String> getCurveIds()
  {
    List<String> curveIds=new ArrayList<String>();
    for(CharacterFile toon : _stats.getToonsList())
    {
      String id=toon.getIdentifier();
      curveIds.add(id);
    }
    return curveIds;
  }

  @Override
  public DatedCurve<?> getCurve(String curveId)
  {
    T stat=_stats.getStatsForToon(curveId);
    DatedCurve<?> curve=_curveProvider.getCurve(stat);
    return curve;
  }
}
