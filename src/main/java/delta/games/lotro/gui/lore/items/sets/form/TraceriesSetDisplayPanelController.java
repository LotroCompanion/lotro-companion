package delta.games.lotro.gui.lore.items.sets.form;

import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.combobox.ComboBoxController;
import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.legendary2.TraceriesSetsUtils;
import delta.games.lotro.lore.items.legendary2.comparators.TraceryComparators;
import delta.games.lotro.lore.items.sets.ItemsSet;

/**
 * Controller for a panel to display a traceries set.
 * @author DAM
 */
public class TraceriesSetDisplayPanelController extends AbstractSetDisplayPanelController
{
  // UI
  private TraceriesSetDisplayConfigurationGadgets _cfgGadgets;
  private JPanel _characterLevelCfgPanel;
  private JPanel _itemLevelCfgPanel;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param set Items set to show.
   */
  public TraceriesSetDisplayPanelController(NavigatorWindowController parent, ItemsSet set)
  {
    super(parent,set);
    _cfgGadgets=new TraceriesSetDisplayConfigurationGadgets(set,this);
  }

  @Override
  protected String getTitlePrefix()
  {
    return "Traceries set";
  }

  @Override
  protected JPanel buildMembersConfigurationPanel()
  {
    JPanel ret=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING,5,0));
    ret.add(GuiFactory.buildLabel("Character level:"));
    ComboBoxController<Integer> characterLevelCombo=_cfgGadgets.getCharacterLevelCombo();
    ret.add(characterLevelCombo.getComboBox());
    _characterLevelCfgPanel=ret;
    _cfgGadgets.update();
    return ret;
  }

  @Override
  protected JPanel buildBonusConfigurationPanel()
  {
    JPanel ret=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING,5,0));
    ret.add(GuiFactory.buildLabel("Average item level:"));
    ComboBoxController<Integer> itemLevelCombo=_cfgGadgets.getItemLevelCombo();
    ret.add(itemLevelCombo.getComboBox());
    _itemLevelCfgPanel=ret;
    return ret;
  }

  @Override
  protected List<Item> getMembers()
  {
    ComboBoxController<Integer> characterLevels=_cfgGadgets.getCharacterLevelCombo();
    Integer characterLevel=characterLevels.getSelectedItem();
    if (characterLevel!=null)
    {
      List<Item> members=TraceriesSetsUtils.findTraceryItemsForCharacterLevel(_set.getMembers(),characterLevel.intValue());
      Collections.sort(members,TraceryComparators.buildTraceryItemsComparator());
      return members;
    }
    return new ArrayList<Item>();
  }

  @Override
  protected Integer getSetLevel()
  {
    ComboBoxController<Integer> itemLevelCombo=_cfgGadgets.getItemLevelCombo();
    return itemLevelCombo.getSelectedItem();
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
    if (_characterLevelCfgPanel!=null)
    {
      _characterLevelCfgPanel.removeAll();
      _characterLevelCfgPanel=null;
    }
    if (_itemLevelCfgPanel!=null)
    {
      _itemLevelCfgPanel.removeAll();
      _itemLevelCfgPanel=null;
    }
  }
}
