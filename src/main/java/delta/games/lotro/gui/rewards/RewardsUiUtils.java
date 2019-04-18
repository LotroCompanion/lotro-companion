package delta.games.lotro.gui.rewards;

import java.util.List;

import delta.common.ui.swing.combobox.ComboBoxController;
import delta.games.lotro.common.rewards.RewardsExplorer;
import delta.games.lotro.lore.items.Item;

/**
 * Utility methods for reward-related UIs.
 * @author DAM
 */
public class RewardsUiUtils
{
  private RewardsExplorer _rewardsExplorer;

  /**
   * Constructor.
   * @param rewardsExplorer
   */
  public RewardsUiUtils(RewardsExplorer rewardsExplorer)
  {
    _rewardsExplorer=rewardsExplorer;
  }

  /**
   * Build a combo-box controller to choose a trait.
   * @return A new combo-box controller.
   */
  public ComboBoxController<String> buildTraitsCombo()
  {
    ComboBoxController<String> ctrl=new ComboBoxController<String>();
    ctrl.addEmptyItem("");
    List<String> traits=_rewardsExplorer.getTraits();
    for(String trait : traits)
    {
      ctrl.addItem(trait,trait);
    }
    ctrl.selectItem(null);
    return ctrl;
  }

  /**
   * Build a combo-box controller to choose a skill.
   * @return A new combo-box controller.
   */
  public ComboBoxController<String> buildSkillsCombo()
  {
    ComboBoxController<String> ctrl=new ComboBoxController<String>();
    ctrl.addEmptyItem("");
    List<String> skills=_rewardsExplorer.getSkills();
    for(String skill : skills)
    {
      ctrl.addItem(skill,skill);
    }
    ctrl.selectItem(null);
    return ctrl;
  }

  /**
   * Build a combo-box controller to choose a title.
   * @return A new combo-box controller.
   */
  public ComboBoxController<String> buildTitlesCombo()
  {
    ComboBoxController<String> ctrl=new ComboBoxController<String>();
    ctrl.addEmptyItem("");
    List<String> titles=_rewardsExplorer.getTitles();
    for(String title : titles)
    {
      ctrl.addItem(title,title);
    }
    ctrl.selectItem(null);
    return ctrl;
  }

  /**
   * Build a combo-box controller to choose an emote.
   * @return A new combo-box controller.
   */
  public ComboBoxController<String> buildEmotesCombo()
  {
    ComboBoxController<String> ctrl=new ComboBoxController<String>();
    ctrl.addEmptyItem("");
    List<String> emotes=_rewardsExplorer.getEmotes();
    for(String emote : emotes)
    {
      ctrl.addItem(emote,emote);
    }
    ctrl.selectItem(null);
    return ctrl;
  }

  /**
   * Build a combo-box controller to choose an item name.
   * @return A new combo-box controller.
   */
  public ComboBoxController<Integer> buildItemsCombo()
  {
    ComboBoxController<Integer> ctrl=new ComboBoxController<Integer>();
    ctrl.addEmptyItem("");
    List<Item> items=_rewardsExplorer.getItems();
    for(Item item : items)
    {
      ctrl.addItem(Integer.valueOf(item.getIdentifier()),item.getName());
    }
    ctrl.selectItem(null);
    return ctrl;
  }
}
