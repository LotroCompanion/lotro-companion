package delta.games.lotro.gui.lore.instances;

import java.util.List;

import delta.common.ui.swing.combobox.ComboBoxController;
import delta.games.lotro.lore.instances.InstancesTree;

/**
 * Utility methods for instances-related UIs.
 * @author DAM
 */
public class InstancesUiUtils
{
  /**
   * Build a combo-box controller to choose a mount category.
   * @return A new combo-box controller.
   */
  public static ComboBoxController<String> buildCategoryCombo()
  {
    ComboBoxController<String> ctrl=new ComboBoxController<String>();
    ctrl.addEmptyItem("");
    List<String> categories=InstancesTree.getInstance().getCategories();
    for(String category : categories)
    {
      ctrl.addItem(category,category);
    }
    ctrl.selectItem(null);
    return ctrl;
  }
}
