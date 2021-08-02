package delta.games.lotro.gui.character.status.currencies;

import delta.games.lotro.character.storage.currencies.Currency;
import delta.games.lotro.character.storage.currencies.CurrencyHistory;
import delta.games.lotro.character.storage.currencies.CurrencyStorage;
import delta.games.lotro.gui.character.status.curves.DatedCurveProvider;
import delta.games.lotro.utils.charts.DatedCurve;

/**
 * Provider for currency history curves.
 * @author DAM
 */
public class CurrencyHistoryCurveProvider implements DatedCurveProvider<CurrencyHistory>
{
  private static int count=0;
  @Override
  public DatedCurve<?> getCurve(CurrencyHistory source)
  {
    Currency currency=source.getCurrency();
    CurrencyStorage storage=source.getStorage();
    String name=currency.getName()+" #"+count;
    count++;
    DatedCurve<Float> curve=new DatedCurve<Float>(name);
    int nbPoints=storage.getPoints();
    for(int i=0;i<nbPoints;i++)
    {
      Long time=storage.getTimeAtIndex(i);
      Integer intValue=storage.getValueAtIndex(i);
      if (intValue!=null)
      {
        Float value=Float.valueOf(intValue.floatValue()/100/1000);
        curve.addValue(time.longValue(),value);
      }
    }
    return curve;
  }
}
