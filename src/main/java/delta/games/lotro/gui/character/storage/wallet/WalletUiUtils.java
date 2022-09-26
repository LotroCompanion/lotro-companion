package delta.games.lotro.gui.character.storage.wallet;

import java.util.List;

import delta.common.ui.swing.combobox.ComboBoxController;

/**
 * Utility methods for wallet-related UIs.
 * @author DAM
 */
public class WalletUiUtils
{
  /**
   * Build a combo-box controller to choose a paper item category.
   * @return A new combo-box controller.
   */
  public static ComboBoxController<String> buildCategoryCombo()
  {
    ComboBoxController<String> ctrl=new ComboBoxController<String>();
    ctrl.addEmptyItem("");
    List<String> categories=WalletUtils.getCategories();
    for(String category : categories)
    {
      ctrl.addItem(category,category);
    }
    ctrl.selectItem(null);
    return ctrl;
  }
}
