package delta.games.lotro.gui.utils;

import java.util.List;

import delta.common.ui.swing.area.AreaController;
import delta.common.ui.swing.combobox.ComboBoxController;
import delta.games.lotro.character.virtues.VirtueDescription;
import delta.games.lotro.character.virtues.VirtuesManager;
import delta.games.lotro.common.stats.StatDescription;
import delta.games.lotro.common.stats.StatsRegistry;
import delta.games.lotro.gui.utils.l10n.Labels;
import delta.games.lotro.lore.reputation.Faction;
import delta.games.lotro.lore.reputation.FactionsRegistry;
import delta.games.lotro.utils.strings.ContextRendering;

/**
 * Shared UI utilities.
 * @author DAM
 */
public class SharedUiUtils
{
  /**
   * Build a combo-box controller to choose a faction.
   * @param parent Parent controller.
   * @param factions Faction to use.
   * @return A new combo-box controller.
   */
  public static ComboBoxController<Faction> buildFactionCombo(AreaController parent, List<Faction> factions)
  {
    ComboBoxController<Faction> ctrl=new ComboBoxController<Faction>();
    ctrl.addEmptyItem("");
    for(Faction faction : factions)
    {
      String rawFactionName=faction.getName();
      String name=ContextRendering.render(parent,rawFactionName);
      ctrl.addItem(faction,name);
    }
    ctrl.selectItem(null);
    return ctrl;
  }

  /**
   * Build a combo-box controller to choose a faction.
   * @param parent Parent controller.
   * @return A new combo-box controller.
   */
  public static ComboBoxController<Faction> buildFactionCombo(AreaController parent)
  {
    List<Faction> factions=FactionsRegistry.getInstance().getAll();
    return buildFactionCombo(parent,factions);
  }

  /**
   * Build a combobox with integer values.
   * @param values Values to show.
   * @param includeEmpty Include an empty choice or not.
   * @return a combobox.
   */
  public static ComboBoxController<Integer> buildIntegerCombo(List<Integer> values, boolean includeEmpty)
  {
    ComboBoxController<Integer> ctrl=new ComboBoxController<Integer>();
    if (includeEmpty)
    {
      ctrl.addEmptyItem("");
    }
    for(Integer value : values)
    {
      ctrl.addItem(value,value.toString());
    }
    return ctrl;
  }

  /**
   * Build a combo-box controller to choose a virtue.
   * @return A new combo-box controller.
   */
  public static ComboBoxController<VirtueDescription> buildVirtueCombo()
  {
    ComboBoxController<VirtueDescription> ctrl=new ComboBoxController<VirtueDescription>();
    ctrl.addEmptyItem("");
    List<VirtueDescription> virtues=VirtuesManager.getInstance().getAll();
    for(VirtueDescription virtue : virtues)
    {
      ctrl.addItem(virtue,virtue.getName());
    }
    ctrl.selectItem(null);
    return ctrl;
  }

  /**
   * Get the path for a toolbar icon.
   * @param iconName Icon name.
   * @return A path.
   */
  public static String getToolbarIconPath(String iconName)
  {
    String imgLocation="/resources/gui/icons/"+iconName+"-icon.png";
    return imgLocation;
  }

  /**
   * Build a combo-box controller to choose from null, true or false (use default labels).
   * @return A new combo-box controller.
   */
  public static ComboBoxController<Boolean> build3StatesBooleanCombobox()
  {
    return build3StatesBooleanCombobox("");
  }

  /**
   * Build a combo-box controller to choose from null, true or false (use default labels).
   * @param nullLabel Label for null case.
   * @return A new combo-box controller.
   */
  public static ComboBoxController<Boolean> build3StatesBooleanCombobox(String nullLabel)
  {
    String yes=Labels.getLabel("shared.yes");
    String no=Labels.getLabel("shared.no");
    return build3StatesBooleanCombobox(nullLabel,yes,no);
  }

  /**
   * Build a combo-box controller to choose from null, true or false.
   * @param nullLabel Label for null case.
   * @param trueLabel Label for true case.
   * @param falseLabel Label for false case.
   * @return A new combo-box controller.
   */
  public static ComboBoxController<Boolean> build3StatesBooleanCombobox(String nullLabel, String trueLabel, String falseLabel)
  {
    ComboBoxController<Boolean> ctrl=new ComboBoxController<Boolean>();
    ctrl.addEmptyItem(nullLabel);
    ctrl.addItem(Boolean.TRUE,trueLabel);
    ctrl.addItem(Boolean.FALSE,falseLabel);
    ctrl.selectItem(null);
    return ctrl;
  }

  /**
   * Build a controller for combo box to choose a stat.
   * @return A new controller.
   */
  public static ComboBoxController<StatDescription> buildStatChooser()
  {
    List<StatDescription> indexedStats=StatsRegistry.getInstance().getIndexedStats();
    return buildStatChooser(indexedStats);
  }

  /**
   * Build a controller for combo box to choose a stat.
   * @param stats Stats to choose from.
   * @return A new controller.
   */
  public static ComboBoxController<StatDescription> buildStatChooser(List<StatDescription> stats)
  {
    ComboBoxController<StatDescription> controller=new ComboBoxController<StatDescription>();
    controller.addEmptyItem("");
    for(StatDescription stat : stats)
    {
      String label=stat.getName();
      controller.addItem(stat,label);
    }
    controller.selectItem(null);
    return controller;
  }
}
