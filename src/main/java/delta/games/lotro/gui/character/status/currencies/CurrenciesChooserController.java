package delta.games.lotro.gui.character.status.currencies;

import java.util.Comparator;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JPanel;

import delta.common.ui.swing.lists.LabelProvider;
import delta.common.ui.swing.lists.OrderedItemsSelectionController;
import delta.common.ui.swing.windows.DefaultFormDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.storage.currencies.Currency;
import delta.games.lotro.character.storage.currencies.CurrencyNameComparator;

/**
 * Controller for a currencies chooser.
 * @author DAM
 */
public final class CurrenciesChooserController extends DefaultFormDialogController<List<Currency>>
{
  private OrderedItemsSelectionController<Currency> _selectionController;

  /**
   * Show the currencies selection dialog.
   * @param parent Parent controller.
   * @param currencies Currencies to show.
   * @param selectedCurrencies Pre-selected currencies.
   * @return A list of selected currencies or <code>null</code> if the window was closed or canceled.
   */
  public static List<Currency> selectCurrencies(WindowController parent, List<Currency> currencies, List<Currency> selectedCurrencies)
  {
    CurrenciesChooserController controller=new CurrenciesChooserController(parent,currencies,selectedCurrencies);
    List<Currency> newSelectedCurrencies=controller.editModal();
    controller.dispose();
    return newSelectedCurrencies;
  }

  /**
   * Constructor.
   * @param parent Parent controller.
   * @param currencies Managed currencies.
   * @param selectedCurrencies Selected currencies.
   */
  private CurrenciesChooserController(WindowController parent, List<Currency> currencies, List<Currency> selectedCurrencies)
  {
    super(parent,null);
    Comparator<Currency> comparator=new CurrencyNameComparator();
    LabelProvider<Currency> labelProvider=new LabelProvider<Currency>()
    {
      @Override
      public String getLabel(Currency item)
      {
        return item.getName();
      }
    };
    _selectionController=new OrderedItemsSelectionController<Currency>(comparator,labelProvider);
    _selectionController.setItems(currencies);
    _selectionController.selectItems(selectedCurrencies);
  }

  @Override
  protected JDialog build()
  {
    JDialog dialog=super.build();
    dialog.setTitle("Choose currencies..."); // I18n
    return dialog;
  }

  @Override
  protected JPanel buildFormPanel()
  {
    return _selectionController.getPanel();
  }

  @Override
  protected void okImpl()
  {
    _data=_selectionController.getSelectedItems();
  }
}
