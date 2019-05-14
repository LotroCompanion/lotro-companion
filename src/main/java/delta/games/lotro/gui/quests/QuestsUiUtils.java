package delta.games.lotro.gui.quests;

import java.util.List;

import delta.common.ui.swing.combobox.ComboBoxController;

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
}
