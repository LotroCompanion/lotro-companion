package delta.games.lotro.gui.items;

import javax.swing.JComboBox;

import delta.games.lotro.gui.utils.GuiFactory;
import delta.games.lotro.lore.items.ItemQuality;
import delta.games.lotro.utils.gui.combobox.ComboBoxController;

/**
 * Tools related to items UI.
 * @author DAM
 */
public class ItemUiTools
{
  /**
   * Build a controller for a combo box to choose an item quality.
   * @return A new controller.
   */
  public static ComboBoxController<ItemQuality> buildQualityCombo()
  {
    JComboBox combo=GuiFactory.buildComboBox();
    ComboBoxController<ItemQuality> ctrl=new ComboBoxController<ItemQuality>(combo);
    ctrl.addEmptyItem("");
    for(ItemQuality quality : ItemQuality.getAll())
    {
      ctrl.addItem(quality,quality.getMeaning());
    }
    ctrl.selectItem(null);
    return ctrl;
  }
}
