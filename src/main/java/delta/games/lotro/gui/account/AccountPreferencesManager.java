package delta.games.lotro.gui.account;

import java.util.List;

import delta.common.utils.misc.Preferences;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.account.Account;
import delta.games.lotro.account.AccountOnServer;
import delta.games.lotro.gui.character.status.currencies.CurrenciesPreferences;

/**
 * Provides access to account level preferences.
 * @author DAM
 */
public class AccountPreferencesManager
{
  /**
   * Get the preferences for an account and server.
   * @param accountOnServer Account/server to use.
   * @param id Identifier of the preferences set.
   * @return Some properties, may be empty.
   */
  public static TypedProperties getPreferencesProperties(AccountOnServer accountOnServer, String id)
  {
    Preferences prefs=accountOnServer.getPreferences();
    TypedProperties props=prefs.getPreferences(id);
    if (CurrenciesPreferences.CURRENCIES_PREFERENCES_ID.equals(id))
    {
      List<String> currencyKeys=props.getStringList(CurrenciesPreferences.SELECTED_CURRENCIES_PROPERTY_NAME);
      if (currencyKeys==null)
      {
        currencyKeys=CurrenciesPreferences.getDefaultCurrenciesForAccountAndServer();
        props.setStringList(CurrenciesPreferences.SELECTED_CURRENCIES_PROPERTY_NAME,currencyKeys);
        prefs.savePreferences(props);
      }
    }
    return props;
  }

  /**
   * Get the preferences for an account.
   * @param account Account.
   * @param id Identifier of the preferences set.
   * @return Some properties, may be empty.
   */
  public static TypedProperties getPreferencesProperties(Account account, String id)
  {
    Preferences prefs=account.getPreferences();
    return prefs.getPreferences(id);
  }
}
