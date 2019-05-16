package delta.games.lotro.gui.quests;

import java.util.List;

import delta.common.ui.swing.combobox.ComboBoxController;
import delta.games.lotro.common.Repeatability;
import delta.games.lotro.common.Size;
import delta.games.lotro.lore.quests.QuestDescription.FACTION;

/**
 * Utility methods for quest-related UIs.
 * @author DAM
 */
public class QuestsUiUtils
{
  /**
   * Build a combo-box controller to choose a quest category.
   * @return A new combo-box controller.
   */
  public static ComboBoxController<String> buildCategoryCombo()
  {
    ComboBoxController<String> ctrl=new ComboBoxController<String>();
    ctrl.addEmptyItem("");
    List<String> categories=QuestsUtils.getCategories();
    for(String category : categories)
    {
      ctrl.addItem(category,category);
    }
    ctrl.selectItem(null);
    return ctrl;
  }

  /**
   * Build a combo-box controller to choose a quest arc.
   * @return A new combo-box controller.
   */
  public static ComboBoxController<String> buildQuestArcCombo()
  {
    ComboBoxController<String> ctrl=new ComboBoxController<String>();
    ctrl.addEmptyItem("");
    List<String> questArcs=QuestsUtils.getQuestArcs();
    for(String questArc : questArcs)
    {
      String label=(questArc.length()>0)?questArc:"(none)";
      ctrl.addItem(questArc,label);
    }
    ctrl.selectItem(null);
    return ctrl;
  }

  /**
   * Build a combo-box controller to choose a quest size.
   * @return A new combo-box controller.
   */
  public static ComboBoxController<Size> buildQuestSizeCombo()
  {
    ComboBoxController<Size> ctrl=new ComboBoxController<Size>();
    ctrl.addEmptyItem("");
    for(Size size : Size.values())
    {
      ctrl.addItem(size,size.toString());
    }
    ctrl.selectItem(null);
    return ctrl;
  }

  /**
   * Build a combo-box controller to choose a quest faction.
   * @return A new combo-box controller.
   */
  public static ComboBoxController<FACTION> buildQuestFactionCombo()
  {
    ComboBoxController<FACTION> ctrl=new ComboBoxController<FACTION>();
    ctrl.addEmptyItem("");
    for(FACTION faction : FACTION.values())
    {
      ctrl.addItem(faction,faction.toString());
    }
    ctrl.selectItem(null);
    return ctrl;
  }

  /**
   * Build a combo-box controller to choose from null, true or false.
   * @return A new combo-box controller.
   */
  public static ComboBoxController<Boolean> build3StatesBooleanCombobox()
  {
    ComboBoxController<Boolean> ctrl=new ComboBoxController<Boolean>();
    ctrl.addEmptyItem("");
    ctrl.addItem(Boolean.TRUE,"Yes");
    ctrl.addItem(Boolean.FALSE,"No");
    ctrl.selectItem(null);
    return ctrl;
  }

  /**
   * Build a combo-box controller to choose a repeatability.
   * @return A new combo-box controller.
   */
  public static ComboBoxController<Repeatability> buildRepeatabilityCombo()
  {
    ComboBoxController<Repeatability> ctrl=new ComboBoxController<Repeatability>();
    ctrl.addEmptyItem("");
    List<Repeatability> repeatabilities=QuestsUtils.getRepeatabilities();
    for(Repeatability repeatability : repeatabilities)
    {
      String label=repeatability.toString();
      ctrl.addItem(repeatability,label);
    }
    ctrl.selectItem(null);
    return ctrl;
  }
}
