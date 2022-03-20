package delta.games.lotro.gui.character.status.currencies;

import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.storage.currencies.CurrenciesManager;
import delta.games.lotro.character.storage.currencies.Currency;
import delta.games.lotro.character.storage.currencies.CurrencyHistory;
import delta.games.lotro.character.utils.MultipleToonsStats;

/**
 * Currency history for several toons.
 * @author DAM
 */
public class MultipleToonsCurrencyHistory extends MultipleToonsStats<CurrencyHistoryCurve>
{
  private Currency _currency;

  /**
   * Set the currency to use.
   * @param currency A currency.
   */
  public void setCurrency(Currency currency)
  {
    _currency=currency;
  }

  @Override
  protected CurrencyHistoryCurve loadToonStats(CharacterFile toon)
  {
    CurrenciesManager mgr=new CurrenciesManager(toon);
    CurrencyHistory history=mgr.getHistory(_currency);
    String name=toon.getName();
    CurrencyHistoryCurve curve=new CurrencyHistoryCurve(name,history);
    return curve;
  }
}
