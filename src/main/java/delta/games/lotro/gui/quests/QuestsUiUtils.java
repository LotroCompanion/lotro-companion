package delta.games.lotro.gui.quests;

import java.util.List;

import delta.common.ui.swing.combobox.ComboBoxController;

/**
 * Utility methods for quest-related UIs.
 * @author DAM
 */
public class QuestsUiUtils
{
  /**
   * Build a combo-box controller to choose a quest category.
   * @return A new combo-box controller.
   */
  public static ComboBoxController<String> buildCategoryCombo()
  {
    ComboBoxController<String> ctrl=new ComboBoxController<String>();
    ctrl.addEmptyItem("");
    List<String> categories=QuestsUtils.getCategories();
    for(String category : categories)
    {
      ctrl.addItem(category,category);
    }
    ctrl.selectItem(null);
    return ctrl;
  }
}
