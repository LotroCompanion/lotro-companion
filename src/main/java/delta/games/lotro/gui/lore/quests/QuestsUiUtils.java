package delta.games.lotro.gui.lore.quests;

import java.util.List;

import delta.common.ui.swing.combobox.ComboBoxController;
import delta.games.lotro.common.LockType;
import delta.games.lotro.common.Repeatability;
import delta.games.lotro.common.Size;
import delta.games.lotro.common.enums.QuestCategory;
import delta.games.lotro.gui.utils.SharedUiUtils;

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
  public static ComboBoxController<QuestCategory> buildCategoryCombo()
  {
    ComboBoxController<QuestCategory> ctrl=new ComboBoxController<QuestCategory>();
    ctrl.addEmptyItem("");
    List<QuestCategory> categories=QuestsUtils.getCategories();
    for(QuestCategory category : categories)
    {
      ctrl.addItem(category,category.getLabel());
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
      String label=(questArc.length()>0)?questArc:"(none)"; // I18n
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
   * Build a combo-box controller to choose from null, true or false.
   * @return A new combo-box controller.
   */
  public static ComboBoxController<Boolean> build3StatesBooleanCombobox()
  {
    return SharedUiUtils.build3StatesBooleanCombobox();
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

  /**
   * Build a combo-box controller to choose a lock type.
   * @return A new combo-box controller.
   */
  public static ComboBoxController<LockType> buildLockTypeCombo()
  {
    ComboBoxController<LockType> ctrl=new ComboBoxController<LockType>();
    ctrl.addEmptyItem("");
    for(LockType lockType : LockType.values())
    {
      String label=lockType.toString();
      ctrl.addItem(lockType,label);
    }
    ctrl.selectItem(null);
    return ctrl;
  }
}
