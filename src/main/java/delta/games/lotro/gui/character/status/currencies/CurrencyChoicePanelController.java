package delta.games.lotro.gui.character.status.currencies;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.combobox.ComboBoxController;
import delta.common.ui.swing.combobox.ComboBoxItem;
import delta.common.ui.swing.windows.WindowController;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.character.storage.currencies.Currency;

/**
 * Controller for a currency choice panel.
 * @author DAM
 */
public class CurrencyChoicePanelController
{
  // Data
  private List<Currency> _currencies;
  private List<Currency> _selectedCurrencies;
  private TypedProperties _preferences;
  // Controllers
  private WindowController _parent;
  private ComboBoxController<Currency> _currencySelector;
  // UI
  private JPanel _panel;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param currencies Currencies to use.
   * @param preferences Preferences to use.
   */
  public CurrencyChoicePanelController(WindowController parent, List<Currency> currencies, TypedProperties preferences)
  {
    _parent=parent;
    _currencies=new ArrayList<Currency>(currencies);
    _preferences=preferences;
    _selectedCurrencies=CurrenciesPreferences.getSelectedCurrencies(_preferences);
    _currencySelector=buildCurrencyCombo(_selectedCurrencies);
    _panel=buildPanel();
  }

  private ComboBoxController<Currency> buildCurrencyCombo(List<Currency> currencies)
  {
    ComboBoxController<Currency> ctrl=new ComboBoxController<Currency>();
    updateCurrencyCombo(ctrl,currencies);
    if (currencies.size()>0)
    {
      ctrl.selectItem(currencies.get(0));
    }
    return ctrl;
  }

  private void updateCurrencyCombo(ComboBoxController<Currency> ctrl, List<Currency> currencies)
  {
    List<ComboBoxItem<Currency>> items=new ArrayList<ComboBoxItem<Currency>>();
    for(Currency currency : currencies)
    {
      ComboBoxItem<Currency> item=new ComboBoxItem<Currency>(currency,currency.getName());
      items.add(item);
    }
    ctrl.updateItems(items);
  }

  private JButton buildCurrenciesChooserButton()
  {
    JButton ret=GuiFactory.buildButton("Choose currencies..."); // I18n
    ActionListener al=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        List<Currency> selectedCurrencies=CurrenciesChooserController.selectCurrencies(_parent,_currencies,_selectedCurrencies);
        if (selectedCurrencies!=null)
        {
          _selectedCurrencies.clear();
          _selectedCurrencies.addAll(selectedCurrencies);
          updateCurrencyCombo(_currencySelector,_selectedCurrencies);
        }
      }
    };
    ret.addActionListener(al);
    return ret;
  }

  private JPanel buildPanel()
  {
    JPanel ret=GuiFactory.buildPanel(new FlowLayout());
    ret.add(GuiFactory.buildLabel("Currency: ")); // I18n
    ret.add(_currencySelector.getComboBox());
    JButton chooser=buildCurrenciesChooserButton();
    ret.add(chooser);
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
    // Data
    CurrenciesPreferences.saveSelectedCurrencies(_selectedCurrencies,_preferences);
    _currencies=null;
    _selectedCurrencies=null;
    // Controllers
    _parent=null;
    if (_currencySelector!=null)
    {
      _currencySelector.dispose();
      _currencySelector=null;
    }
    // UI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
  }
}
