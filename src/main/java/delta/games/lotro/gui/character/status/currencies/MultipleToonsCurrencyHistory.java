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
public class MultipleToonsCurrencyHistory extends MultipleToonsStats<CurrencyHistory>
{
  private Currency _currency;

  /**
   * Constructor.
   * @param currency Targeted currency.
   */
  public MultipleToonsCurrencyHistory(Currency currency)
  {
    _currency=currency;
  }

  @Override
  protected CurrencyHistory loadToonStats(CharacterFile toon)
  {
    CurrenciesManager mgr=new CurrenciesManager(toon);
    return mgr.getHistory(_currency);
  }
}
