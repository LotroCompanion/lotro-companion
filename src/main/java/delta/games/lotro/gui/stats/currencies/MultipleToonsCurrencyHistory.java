package delta.games.lotro.gui.stats.currencies;

import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.storage.currencies.Currency;
import delta.games.lotro.character.storage.currencies.CurrencyHistory;
import delta.games.lotro.character.storage.currencies.io.CurrenciesIo;
import delta.games.lotro.stats.MultipleToonsStats;

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
    return CurrenciesIo.load(toon,_currency);
  }
}
