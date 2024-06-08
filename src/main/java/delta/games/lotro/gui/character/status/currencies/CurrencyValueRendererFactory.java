package delta.games.lotro.gui.character.status.currencies;

import delta.games.lotro.character.storage.currencies.Currency;
import delta.games.lotro.character.storage.currencies.CurrencySemantics;
import delta.games.lotro.common.Duration;
import delta.games.lotro.common.money.Money;
import delta.games.lotro.gui.character.status.curves.ValueRenderer;

/**
 * Factory for currency value renderers.
 * @author DAM
 */
public class CurrencyValueRendererFactory
{
  /**
   * Build a value renderer for the given currency.
   * @param currency Currency to use.
   * @return A renderer or <code>null</code> to use default rendering.
   */
  public static ValueRenderer buildValueRenderer(Currency currency)
  {
    CurrencySemantics semantics=currency.getSemantics();
    if (semantics==CurrencySemantics.DURATION)
    {
      return buildDurationRenderer();
    }
    if (semantics==CurrencySemantics.MONEY)
    {
      return buildMoneyRenderer();
    }
    return null;
  }

  private static ValueRenderer buildDurationRenderer()
  {
    ValueRenderer ret=new ValueRenderer()
    {
      @Override
      public String render(double value)
      {
        return Duration.getDurationStringNoSeconds((int)value);
      }
    };
    return ret;
  }

  private static ValueRenderer buildMoneyRenderer()
  {
    ValueRenderer ret=new ValueRenderer()
    {
      private Money _money=new Money();
      @Override
      public String render(double value)
      {
        _money.setRawValue((int)value);
        return _money.getShortLabel();
      }
    };
    return ret;
  }
}
