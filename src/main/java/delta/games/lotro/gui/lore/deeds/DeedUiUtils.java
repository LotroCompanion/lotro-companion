package delta.games.lotro.gui.lore.deeds;

import java.util.List;

import delta.common.ui.swing.combobox.ComboBoxController;
import delta.games.lotro.common.enums.DeedCategory;
import delta.games.lotro.common.enums.LotroEnumsRegistry;
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
  public static ComboBoxController<DeedCategory> buildCategoryCombo()
  {
    ComboBoxController<DeedCategory> ctrl=new ComboBoxController<DeedCategory>();
    ctrl.addEmptyItem("");
    List<DeedCategory> categories=DeedUtils.getCategories();
    for(DeedCategory category : categories)
    {
      ctrl.addItem(category,category.getLabel());
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
    for(DeedType deedType : LotroEnumsRegistry.getInstance().get(DeedType.class).getAll())
    {
      ctrl.addItem(deedType,deedType.toString());
    }
    ctrl.selectItem(null);
    return ctrl;
  }
}
