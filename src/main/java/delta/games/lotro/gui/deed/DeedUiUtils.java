package delta.games.lotro.gui.deed;

import java.util.List;

import delta.common.ui.swing.combobox.ComboBoxController;
import delta.games.lotro.lore.deeds.DeedType;

/**
 * Utility methods for deed-related UIs.
 * @author DAM
 */
public class DeedUiUtils
{
  /**
   * Build a combo-box controller to choose a deed category.
   * @return A new combo-box controller.
   */
  public static ComboBoxController<String> buildCategoryCombo()
  {
    ComboBoxController<String> ctrl=new ComboBoxController<String>();
    ctrl.addEmptyItem("");
    List<String> categories=DeedUtils.getCategories();
    for(String category : categories)
    {
      ctrl.addItem(category,category);
    }
    ctrl.selectItem(null);
    return ctrl;
  }

  /**
   * Build a combo-box controller to choose a deed type.
   * @return A new combo-box controller.
   */
  public static  ComboBoxController<DeedType> buildDeedTypeCombo()
  {
    ComboBoxController<DeedType> ctrl=new ComboBoxController<DeedType>();
    ctrl.addEmptyItem("");
    for(DeedType deedType : DeedType.values())
    {
      ctrl.addItem(deedType,deedType.toString());
    }
    ctrl.selectItem(null);
    return ctrl;
  }
}
