package delta.games.lotro.gui.lore.instances;

import java.util.List;

import delta.common.ui.swing.combobox.ComboBoxController;
import delta.games.lotro.common.enums.WJEncounterCategory;
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
  public static ComboBoxController<WJEncounterCategory> buildCategoryCombo()
  {
    ComboBoxController<WJEncounterCategory> ctrl=new ComboBoxController<WJEncounterCategory>();
    ctrl.addEmptyItem("");
    List<WJEncounterCategory> categories=InstancesTree.getInstance().getInstanceCategories();
    for(WJEncounterCategory category : categories)
    {
      if (category!=null)
      {
        ctrl.addItem(category,category.getLabel());
      }
    }
    ctrl.selectItem(null);
    return ctrl;
  }
}
