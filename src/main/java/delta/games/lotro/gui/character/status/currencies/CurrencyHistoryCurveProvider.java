package delta.games.lotro.gui.character.status.currencies;

import delta.games.lotro.character.storage.currencies.CurrencyHistory;
import delta.games.lotro.character.storage.currencies.CurrencyStorage;
import delta.games.lotro.gui.character.status.curves.DatedCurveProvider;
import delta.games.lotro.utils.charts.DatedCurve;

/**
 * Provider for currency history curves.
 * @author DAM
 */
public class CurrencyHistoryCurveProvider implements DatedCurveProvider<CurrencyHistoryCurve>
{
  @Override
  public DatedCurve<?> getCurve(CurrencyHistoryCurve source)
  {
    CurrencyHistory history=source.getHistory();
    CurrencyStorage storage=history.getStorage();
    String name=source.getName();
    DatedCurve<Float> curve=new DatedCurve<Float>(name);
    int nbPoints=storage.getPoints();
    for(int i=0;i<nbPoints;i++)
    {
      Long time=storage.getTimeAtIndex(i);
      Integer intValue=storage.getValueAtIndex(i);
      if (intValue!=null)
      {
        Float value=Float.valueOf(intValue.floatValue());
        curve.addValue(time.longValue(),value);
      }
    }
    return curve;
  }
}
