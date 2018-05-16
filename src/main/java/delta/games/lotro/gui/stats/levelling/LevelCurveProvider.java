package delta.games.lotro.gui.stats.levelling;

import delta.games.lotro.character.level.LevelHistory;
import delta.games.lotro.gui.stats.curves.DatedCurveProvider;
import delta.games.lotro.utils.charts.DatedCurve;

/**
 * Provide for level curves.
 * @author DAM
 */
public class LevelCurveProvider implements DatedCurveProvider<LevelHistory>
{
  @Override
  public DatedCurve<?> getCurve(LevelHistory source)
  {
    String name=source.getName();
    DatedCurve<Integer> curve=new DatedCurve<Integer>(name);
    int[] levels=source.getLevels();
    long[] dates=source.getDatesSortedByLevel();
    int nbItems=Math.min(levels.length,dates.length);
    for(int i=0;i<nbItems;i++)
    {
      curve.addValue(dates[i],Integer.valueOf(levels[i]));
    }
    return curve;
  }
}
