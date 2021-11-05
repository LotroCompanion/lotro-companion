package delta.games.lotro.gui.lore.items.sets.form;

import java.util.List;

import delta.common.ui.swing.combobox.ComboBoxController;
import delta.common.ui.swing.combobox.ItemSelectionListener;
import delta.games.lotro.lore.items.legendary2.TraceriesSetsUtils;
import delta.games.lotro.lore.items.legendary2.Tracery;
import delta.games.lotro.lore.items.sets.ItemsSet;

/**
 * Gadgets for the configuration of the display of a traceries set.
 * @author DAM
 */
public class TraceriesSetDisplayConfigurationGadgets
{
  // Data
  private ItemsSet _set;
  private List<Tracery> _traceries;
  // Controllers
  private ComboBoxController<Integer> _characterLevels;
  private ComboBoxController<Integer> _itemLevels;
  private AbstractSetDisplayPanelController _parent;

  /**
   * Constructor.
   * @param set Set to use.
   * @param parent Parent panel.
   */
  public TraceriesSetDisplayConfigurationGadgets(ItemsSet set, AbstractSetDisplayPanelController parent)
  {
    _set=set;
    _parent=parent;
    _traceries=TraceriesSetsUtils.getMemberTraceries(_set);
    _itemLevels=buildItemLevelCombo();
    _characterLevels=buildCharacterLevelCombo();
  }

  /**
   * Get the managed character level combo-box controller.
   * @return a controller.
   */
  public ComboBoxController<Integer> getCharacterLevelCombo()
  {
    return _characterLevels;
  }

  /**
   * Get the managed item level combo-box controller.
   * @return a controller.
   */
  public ComboBoxController<Integer> getItemLevelCombo()
  {
    return _itemLevels;
  }

  private ComboBoxController<Integer> buildCharacterLevelCombo()
  {
    int[] minMaxCharacterLevel=TraceriesSetsUtils.findCharacterLevelRange(_traceries);
    ComboBoxController<Integer> ret=new ComboBoxController<Integer>();
    int min=minMaxCharacterLevel[0];
    int max=minMaxCharacterLevel[1];
    for(int i=min;i<=max;i++)
    {
      ret.addItem(Integer.valueOf(i),String.valueOf(i));
    }
    ItemSelectionListener<Integer> listener=new ItemSelectionListener<Integer>()
    {
      @Override
      public void itemSelected(Integer item)
      {
        chooseCharacterLevel(item.intValue());
      }
    };
    ret.addListener(listener);
    return ret;
  }

  private ComboBoxController<Integer> buildItemLevelCombo()
  {
    ComboBoxController<Integer> ret=new ComboBoxController<Integer>();
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

  /**
   * Update display.
   */
  public void update()
  {
    Integer characterLevel=_characterLevels.getSelectedItem();
    if (characterLevel!=null)
    {
      chooseCharacterLevel(characterLevel.intValue());
    }
  }

  private void chooseCharacterLevel(int characterLevel)
  {
    // Handle members
    _parent.updateMembersPanel();
    // Handle item level
    List<Tracery> traceries=TraceriesSetsUtils.findTraceriesForCharacterLevel(_traceries,characterLevel);
    int [] minMaxItemLevel=TraceriesSetsUtils.findItemLevelRange(traceries);
    int min=minMaxItemLevel[0];
    int max=minMaxItemLevel[1];
    setItemLevelRange(min,max);
  }

  private void setItemLevelRange(int min, int max)
  {
    Integer previousValue=_itemLevels.getSelectedItem();
    _itemLevels.removeAllItems();
    // Push values from min to max
    for(int itemLevel=min;itemLevel<=max;itemLevel++)
    {
      _itemLevels.addItem(Integer.valueOf(itemLevel),String.valueOf(itemLevel));
    }
    if (previousValue!=null)
    {
      int selectedValue=previousValue.intValue();
      if (selectedValue<min)
      {
        selectedValue=min;
      }
      if (selectedValue>max)
      {
        selectedValue=max;
      }
      _itemLevels.selectItem(Integer.valueOf(selectedValue));
    }
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
    _traceries=null;
    // Controllers
    _parent=null;
    if (_characterLevels!=null)
    {
      _characterLevels.dispose();
      _characterLevels=null;
    }
    if (_itemLevels!=null)
    {
      _itemLevels.dispose();
      _itemLevels=null;
    }
  }
}
