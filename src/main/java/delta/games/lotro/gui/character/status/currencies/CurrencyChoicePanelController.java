package delta.games.lotro.gui.character.status.currencies;

import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.combobox.ComboBoxController;
import delta.games.lotro.character.storage.currencies.Currencies;
import delta.games.lotro.character.storage.currencies.Currency;
import delta.games.lotro.common.Scope;
import delta.games.lotro.common.comparators.NamedComparator;

/**
 * Controller for a currency choice panel.
 * @author DAM
 */
public class CurrencyChoicePanelController
{
  // Controllers
  private ComboBoxController<Currency> _currencySelector;
  // UI
  private JPanel _panel;

  /**
   * Constructor.
   * @param scopes Scopes to use.
   */
  public CurrencyChoicePanelController(Set<Scope> scopes)
  {
    List<Currency> currencies=getCurrencies(scopes);
    _currencySelector=buildCurrencyCombo(currencies);
    _panel=buildPanel();
  }

  private List<Currency> getCurrencies(Set<Scope> scopes)
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

  private ComboBoxController<Currency> buildCurrencyCombo(List<Currency> currencies)
  {
    ComboBoxController<Currency> ctrl=new ComboBoxController<Currency>();
    for(Currency currency : currencies)
    {
      ctrl.addItem(currency,currency.getName());
    }
    ctrl.selectItem(currencies.get(0));
    return ctrl;
  }

  private JPanel buildPanel()
  {
    JPanel ret=GuiFactory.buildPanel(new FlowLayout());
    ret.add(GuiFactory.buildLabel("Currency: "));
    ret.add(_currencySelector.getComboBox());
    return ret;
  }

  /**
   * Get the currency selector.
   * @return the currency selector.
   */
  public ComboBoxController<Currency> getCurrencySelector()
  {
    return _currencySelector;
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  public JPanel getPanel()
  {
    return _panel;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    if (_currencySelector!=null)
    {
      _currencySelector.dispose();
      _currencySelector=null;
    }
  }
}
