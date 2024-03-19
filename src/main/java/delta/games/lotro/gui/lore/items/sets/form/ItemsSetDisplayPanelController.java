package delta.games.lotro.gui.lore.items.sets.form;

import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.combobox.ComboBoxController;
import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.sets.ItemsSet;
import delta.games.lotro.lore.items.sets.ItemsSetsUtils;

/**
 * Controller for a panel to display an items set.
 * @author DAM
 */
public class ItemsSetDisplayPanelController extends AbstractSetDisplayPanelController
{
  // UI
  private ItemsSetDisplayConfigurationGadgets _cfgGadgets;
  private JPanel _itemLevelCfgPanel;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param set Items set to show.
   */
  public ItemsSetDisplayPanelController(NavigatorWindowController parent, ItemsSet set)
  {
    super(parent,set);
    boolean useItemLevelCombo=ItemsSetsUtils.hasMultipleItemLevels(set);
    if (useItemLevelCombo)
    {
      _cfgGadgets=new ItemsSetDisplayConfigurationGadgets(set,this);
    }
  }

  @Override
  protected String getTitlePrefix()
  {
    return "Items set";
  }

  @Override
  protected JPanel buildBonusConfigurationPanel()
  {
    if (_cfgGadgets!=null)
    {
      JPanel ret=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING,5,0));
      ret.add(GuiFactory.buildLabel("Average item level:"));
      ComboBoxController<Integer> itemLevelCombo=_cfgGadgets.getItemLevelCombo();
      ret.add(itemLevelCombo.getComboBox());
      _itemLevelCfgPanel=ret;
      return ret;
    }
    return null;
  }

  @Override
  protected List<Item> getMembers()
  {
    List<Item> members=new ArrayList<Item>(_set.getMembers());
    return members;
  }

  @Override
  protected Integer getSetLevel()
  {
    if (_cfgGadgets!=null)
    {
      ComboBoxController<Integer> itemLevelCombo=_cfgGadgets.getItemLevelCombo();
      return itemLevelCombo.getSelectedItem();
    }
    return super.getSetLevel();
  }

  @Override
  public void dispose()
  {
    super.dispose();
    // Controllers
    if (_cfgGadgets!=null)
    {
      _cfgGadgets.dispose();
      _cfgGadgets=null;
    }
    // UI
    if (_itemLevelCfgPanel!=null)
    {
      _itemLevelCfgPanel.removeAll();
      _itemLevelCfgPanel=null;
    }
  }
}
