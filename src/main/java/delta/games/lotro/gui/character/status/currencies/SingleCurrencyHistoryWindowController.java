package delta.games.lotro.gui.character.status.currencies;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.combobox.ItemSelectionListener;
import delta.common.ui.swing.windows.DefaultDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.account.Account;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.storage.currencies.Currencies;
import delta.games.lotro.character.storage.currencies.CurrenciesFacade;
import delta.games.lotro.character.storage.currencies.Currency;
import delta.games.lotro.character.storage.currencies.CurrencyKeys;
import delta.games.lotro.common.Scope;
import delta.games.lotro.common.comparators.NamedComparator;

/**
 * Controller for a "currency history" window.
 * @author DAM
 */
public class SingleCurrencyHistoryWindowController extends DefaultDialogController
{
  // Controllers
  private SingleCurrencyHistoryPanelController _panelController;
  private CurrencyChoicePanelController _currencyChoice;
  // UI
  private JPanel _chartHostPanel;

  /**
   * Build a window controller to show account/server currencies.
   * @param parent Parent window.
   * @param account Account.
   * @param serverName Server name.
   * @return A window controller.
   */
  public static SingleCurrencyHistoryWindowController buildAccountServerWindow(WindowController parent, Account account, String serverName)
  {
    CurrenciesFacade facade=new CurrenciesFacade(account,serverName);
    SingleCurrencyHistoryPanelController panel=new SingleCurrencyHistoryPanelController(facade);
    Set<Scope> scopes=new HashSet<Scope>();
    scopes.add(Scope.SERVER);
    scopes.add(Scope.ACCOUNT);
    List<Currency> currencies=getCurrencies(scopes);
    List<Currency> selectedCurrencies=getSelectedCurrencies(scopes);
    SingleCurrencyHistoryWindowController ret=new SingleCurrencyHistoryWindowController(parent,panel,currencies,selectedCurrencies);
    return ret;
  }

  /**
   * Build a window controller to show character currencies.
   * @param parent Parent window.
   * @param toon Character to use.
   * @return A window controller.
   */
  public static SingleCurrencyHistoryWindowController buildCharacterWindow(WindowController parent, CharacterFile toon)
  {
    CurrenciesFacade facade=new CurrenciesFacade(toon);
    SingleCurrencyHistoryPanelController panel=new SingleCurrencyHistoryPanelController(facade);
    Set<Scope> scopes=new HashSet<Scope>();
    scopes.add(Scope.CHARACTER);
    scopes.add(Scope.SERVER);
    scopes.add(Scope.ACCOUNT);
    List<Currency> currencies=getCurrencies(scopes);
    List<Currency> selectedCurrencies=getSelectedCurrencies(scopes);
    SingleCurrencyHistoryWindowController ret=new SingleCurrencyHistoryWindowController(parent,panel,currencies,selectedCurrencies);
    return ret;
  }

  private static List<Currency> getCurrencies(Set<Scope> scopes)
  {
    List<Currency> ret=new ArrayList<Currency>();
    for(Currency currency : Currencies.get().getCurrencies())
    {
      if (scopes.contains(currency.getScope()))
      {
        ret.add(currency);
      }
    }
    Collections.sort(ret,new NamedComparator());
    return ret;
  }

  private static List<Currency> getSelectedCurrencies(Set<Scope> scopes)
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
    List<Currency> ret=new ArrayList<Currency>();
    for(String key : keys)
    {
      Currency currency=Currencies.get().getByKey(key);
      if ((currency!=null) && (scopes.contains(currency.getScope())))
      {
        ret.add(currency);
      }
    }
    //Collections.sort(ret,new NamedComparator());
    return ret;
  }

  /**
   * Constructor.
   * @param parent Parent window.
   * @param panelController Currency history display panel.
   * @param currencies Currencies to use.
   * @param selectedCurrencies Selected currencies.
   */
  public SingleCurrencyHistoryWindowController(WindowController parent, SingleCurrencyHistoryPanelController panelController, List<Currency> currencies, List<Currency> selectedCurrencies)
  {
    super(parent);
    _panelController=panelController;
    _currencyChoice=new CurrencyChoicePanelController(this,currencies,selectedCurrencies);
  }

  @Override
  protected JComponent buildContents()
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
    dialog.pack();
    return dialog;
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
