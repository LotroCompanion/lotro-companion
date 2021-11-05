package delta.games.lotro.gui.lore.items.sets.form;

import delta.common.ui.swing.combobox.ComboBoxController;
import delta.common.ui.swing.combobox.ItemSelectionListener;
import delta.common.utils.math.Range;
import delta.games.lotro.lore.items.sets.ItemsSet;
import delta.games.lotro.lore.items.sets.ItemsSetsUtils;

/**
 * Gadgets for the configuration of the display of traceries sets.
 * @author DAM
 */
public class ItemsSetDisplayConfigurationGadgets
{
  // Data
  private ItemsSet _set;
  // Controllers
  private ComboBoxController<Integer> _itemLevels;
  private AbstractSetDisplayPanelController _parent;

  /**
   * Constructor.
   * @param set Set to use.
   * @param parent Parent panel.
   */
  public ItemsSetDisplayConfigurationGadgets(ItemsSet set, AbstractSetDisplayPanelController parent)
  {
    _set=set;
    _parent=parent;
    _itemLevels=buildItemLevelCombo();
  }

  /**
   * Get the managed item level combo-box controller.
   * @return a controller.
   */
  public ComboBoxController<Integer> getItemLevelCombo()
  {
    return _itemLevels;
  }

  private ComboBoxController<Integer> buildItemLevelCombo()
  {
    ComboBoxController<Integer> ret=new ComboBoxController<Integer>();
    Range itemLevelRange=ItemsSetsUtils.findItemLevelRange(_set);
    Integer minLevel=itemLevelRange.getMin();
    int minItemLevel=(minLevel!=null)?minLevel.intValue():1;
    Integer maxLevel=itemLevelRange.getMax();
    int maxItemLevel=(maxLevel!=null)?maxLevel.intValue():1000;
    // Push values from min to max
    for(int itemLevel=minItemLevel;itemLevel<=maxItemLevel;itemLevel++)
    {
      ret.addItem(Integer.valueOf(itemLevel),String.valueOf(itemLevel));
    }
    ItemSelectionListener<Integer> listener=new ItemSelectionListener<Integer>()
    {
      @Override
      public void itemSelected(Integer item)
      {
        chooseItemLevel(item);
      }
    };
    ret.addListener(listener);
    return ret;
  }

  private void chooseItemLevel(Integer itemLevel)
  {
    if (itemLevel!=null)
    {
      _parent.updateBonus();
    }
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Data
    _set=null;
    // Controllers
    _parent=null;
    if (_itemLevels!=null)
    {
      _itemLevels.dispose();
      _itemLevels=null;
    }
  }
}
