package delta.games.lotro.gui.mounts;

import java.util.List;

import delta.common.ui.swing.combobox.ComboBoxController;
import delta.games.lotro.lore.collections.mounts.MountsManager;

/**
 * Utility methods for mount-related UIs.
 * @author DAM
 */
public class MountUiUtils
{
  /**
   * Build a combo-box controller to choose a mount category.
   * @return A new combo-box controller.
   */
  public static ComboBoxController<String> buildCategoryCombo()
  {
    ComboBoxController<String> ctrl=new ComboBoxController<String>();
    ctrl.addEmptyItem("");
    List<String> categories=MountsManager.getInstance().getCategories();
    for(String category : categories)
    {
      ctrl.addItem(category,category);
    }
    ctrl.selectItem(null);
    return ctrl;
  }
}
