package delta.games.lotro.gui.character.storage.wallet;

import java.util.List;

import delta.common.ui.swing.combobox.ComboBoxController;
import delta.games.lotro.common.enums.PaperItemCategory;

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
  public static ComboBoxController<PaperItemCategory> buildCategoryCombo()
  {
    ComboBoxController<PaperItemCategory> ctrl=new ComboBoxController<PaperItemCategory>();
    ctrl.addEmptyItem("");
    List<PaperItemCategory> categories=WalletUtils.getCategories();
    for(PaperItemCategory category : categories)
    {
      ctrl.addItem(category,category.getLabel());
    }
    ctrl.selectItem(null);
    return ctrl;
  }
}
