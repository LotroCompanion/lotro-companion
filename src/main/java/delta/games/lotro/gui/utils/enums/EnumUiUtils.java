package delta.games.lotro.gui.utils.enums;

import java.util.List;

import delta.common.ui.swing.combobox.ComboBoxController;
import delta.games.lotro.common.enums.LotroEnumEntry;

/**
 * UI utilities related to enums.
 * @author DAM
 */
public class EnumUiUtils
{
  /**
   * Build a combo-box controller to choose an enum value.
   * @param entries Type of enum entries.
   * @param includeEmpty Include an empty choice or not.
   * @return A new combo-box controller.
   */
  public static <T extends LotroEnumEntry> ComboBoxController<T> buildEnumCombo(List<T> entries, boolean includeEmpty)
  {
    ComboBoxController<T> ctrl=new ComboBoxController<T>();
    if (includeEmpty)
    {
      ctrl.addEmptyItem("");
    }
    for(T entry : entries)
    {
      if (entry!=null)
      {
        ctrl.addItem(entry,entry.getLabel());
      }
    }
    return ctrl;
  }
}
