package delta.games.lotro.gui.character.status.currencies;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.combobox.ItemSelectionListener;
import delta.common.ui.swing.windows.DefaultDisplayDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.account.AccountOnServer;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.storage.currencies.Currencies;
import delta.games.lotro.character.storage.currencies.CurrenciesFacade;
import delta.games.lotro.character.storage.currencies.Currency;
import delta.games.lotro.gui.account.AccountPreferencesManager;
import delta.games.lotro.gui.character.CharacterPreferencesManager;

/**
 * Controller for a "currency history" window for a single character.
 * @author DAM
 */
public class SingleCharacterCurrencyHistoryWindowController extends DefaultDisplayDialogController<Void>
{
  // Controllers
  private SingleCharacterCurrencyHistoryPanelController _panelController;
  private CurrencyChoicePanelController _currencyChoice;
  // UI
  private JPanel _chartHostPanel;
  // Data
  private TypedProperties _preferences;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param toon Character to use.
   */
  public SingleCharacterCurrencyHistoryWindowController(WindowController parent, CharacterFile toon)
  {
    super(parent,null);
    CurrenciesFacade facade=new CurrenciesFacade(toon);
    _panelController=new SingleCharacterCurrencyHistoryPanelController(facade);
    List<Currency> currencies=Currencies.getAvailableCurrencies(true,true,true);
    _preferences=CharacterPreferencesManager.getUserProperties(toon,CurrenciesPreferences.CURRENCIES_PREFERENCES_ID);
    _currencyChoice=new CurrencyChoicePanelController(this,currencies,_preferences);
  }

  /**
   * Constructor.
   * @param parent Parent window.
   * @param accountOnServer Account/server to use.
   */
  public SingleCharacterCurrencyHistoryWindowController(WindowController parent, AccountOnServer accountOnServer)
  {
    super(parent,null);
    CurrenciesFacade facade=new CurrenciesFacade(accountOnServer);
    _panelController=new SingleCharacterCurrencyHistoryPanelController(facade);
    List<Currency> currencies=Currencies.getAvailableCurrencies(false,true,true);
    _preferences=AccountPreferencesManager.getPreferencesProperties(accountOnServer,CurrenciesPreferences.CURRENCIES_PREFERENCES_ID);
    _currencyChoice=new CurrencyChoicePanelController(this,currencies,_preferences);
  }


  @Override
  protected JPanel buildFormPanel()
  {
    ItemSelectionListener<Currency> listener=new ItemSelectionListener<Currency>()
    {
      @Override
      public void itemSelected(Currency currency)
      {
        handleCurrencyChange(currency);
      }
    };
    _currencyChoice.getCurrencySelector().addListener(listener);
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    ret.add(_currencyChoice.getPanel(),c);
    _chartHostPanel=GuiFactory.buildPanel(new BorderLayout());
    c=new GridBagConstraints(0,1,1,1,1.0,1.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
    ret.add(_chartHostPanel,c);
    Currency toShow=_currencyChoice.getCurrencySelector().getSelectedItem();
    handleCurrencyChange(toShow);
    return ret;
  }

  private void handleCurrencyChange(Currency currency)
  {
    _chartHostPanel.removeAll();
    JPanel toUse=null;
    if (currency!=null)
    {
      _panelController.setCurve(currency);
      toUse=_panelController.getPanelController().getPanel();
    }
    else
    {
      toUse=GuiFactory.buildPanel(new BorderLayout());
    }
    _chartHostPanel.add(toUse,BorderLayout.CENTER);
    _chartHostPanel.revalidate();
    _chartHostPanel.repaint();
  }

  /**
   * Get the window identifier for this window.
   * @return A window identifier.
   */
  public static String getIdentifier()
  {
    return "SINGLE_CURRENCY";
  }

  @Override
  protected JDialog build()
  {
    JDialog dialog=super.build();
    dialog.setMinimumSize(new Dimension(500,380));
    dialog.setPreferredSize(new Dimension(700,500));
    dialog.setTitle("Currencies"); // I18n
    dialog.pack();
    return dialog;
  }

  @Override
  public void configureWindow()
  {
    automaticLocationSetup();
  }

  @Override
  public String getWindowIdentifier()
  {
    return getIdentifier();
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    saveBoundsPreferences();
    super.dispose();
    if (_panelController!=null)
    {
      _panelController.dispose();
      _panelController=null;
    }
    if (_currencyChoice!=null)
    {
      _currencyChoice.dispose();
      _currencyChoice=null;
    }
  }
}
