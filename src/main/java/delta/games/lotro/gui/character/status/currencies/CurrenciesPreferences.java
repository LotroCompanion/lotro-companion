package delta.games.lotro.gui.character.status.currencies;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.character.storage.currencies.Currencies;
import delta.games.lotro.character.storage.currencies.Currency;
import delta.games.lotro.character.storage.currencies.CurrencyKeys;
import delta.games.lotro.common.Scope;

/**
 * Preferences related to currencies.
 * @author DAM
 */
public class CurrenciesPreferences
{
  /**
   * Preferences ID for the currencies.
   */
  public static final String CURRENCIES_PREFERENCES_ID="currencies";
  /**
   * Property name for the selected currencies.
   */
  public static final String SELECTED_CURRENCIES_PROPERTY_NAME="selectedCurrencies";

  /**
   * Get the default currencies for account/server.
   * @return A list of currency keys.
   */
  public static List<String> getDefaultCurrenciesForAccountAndServer()
  {
    Set<Scope> scopes=new HashSet<Scope>();
    scopes.add(Scope.ACCOUNT);
    scopes.add(Scope.SERVER);
    return CurrenciesPreferences.getDefaultCurrencies(scopes);
  }

  /**
   * Get the default currencies for account/server.
   * @return A list of currency keys.
   */
  public static List<String> getDefaultCurrenciesForCharacter()
  {
    Set<Scope> scopes=new HashSet<Scope>();
    scopes.add(Scope.CHARACTER);
    scopes.add(Scope.ACCOUNT);
    scopes.add(Scope.SERVER);
    return CurrenciesPreferences.getDefaultCurrencies(scopes);
  }

  /**
   * Get the default currencies to use.
   * @param scopes Scopes to use.
   * @return A list of currency keys.
   */
  private static List<String> getDefaultCurrencies(Set<Scope> scopes)
  {
    List<String> keys=new ArrayList<String>();
    if (scopes.contains(Scope.CHARACTER))
    {
      keys.add(CurrencyKeys.GOLD);
    }
    if (scopes.contains(Scope.SERVER))
    {
      keys.add(CurrencyKeys.MEDALLIONS);
      keys.add(CurrencyKeys.MARKS);
      keys.add(CurrencyKeys.SEALS);
      keys.add("1879352247"); // Motes of Enchantment
      keys.add("1879377205"); // Embers of Enchantment
    }
    return keys;
  }

  /**
   * Read the selected currencies from some preference properties.
   * @param props Properties to read from.
   * @return A list of currencies.
   */
  public static List<Currency> getSelectedCurrencies(TypedProperties props)
  {
    List<String> keys=props.getStringList(SELECTED_CURRENCIES_PROPERTY_NAME);
    if (keys==null)
    {
      keys=new ArrayList<String>();
    }
    return getCurrencies(keys);
  }

  /**
   * Build a list of currencies from a list of currency keys.
   * @param keys Keys to use.
   * @return A list of currencies.
   */
  private static List<Currency> getCurrencies(List<String> keys)
  {
    List<Currency> currencies=new ArrayList<Currency>();
    for(String key : keys)
    {
      Currency currency=Currencies.get().getByKey(key);
      if (currency!=null)
      {
        currencies.add(currency);
      }
    }
    return currencies;
  }

  /**
   * Save the selected currencies to the given properties.
   * @param currencies Currencies to write.
   * @param props Properties to write to.
   */
  public static void saveSelectedCurrencies(List<Currency> currencies, TypedProperties props)
  {
    List<String> keys=new ArrayList<String>();
    for(Currency currency : currencies)
    {
      keys.add(currency.getPersistenceKey());
    }
    props.setStringList(SELECTED_CURRENCIES_PROPERTY_NAME,keys);
  }
}
