package delta.games.lotro.gui.character.status.currencies;

import delta.games.lotro.character.storage.currencies.CurrencyHistory;

/**
 * Currency history curve.
 * @author DAM
 */
public class CurrencyHistoryCurve
{
  private String _name;
  private CurrencyHistory _history;

  /**
   * Constructor.
   * @param name Curve name.
   * @param history History data.
   */
  public CurrencyHistoryCurve(String name, CurrencyHistory history)
  {
    _name=name;
    _history=history;
  }

  /**
   * Get the curve name.
   * @return a name.
   */
  public String getName()
  {
    return _name;
  }

  /**
   * Get the values history.
   * @return the values history.
   */
  public CurrencyHistory getHistory()
  {
    return _history;
  }
}
