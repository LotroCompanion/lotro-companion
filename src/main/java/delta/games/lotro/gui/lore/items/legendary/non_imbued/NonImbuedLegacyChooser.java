package delta.games.lotro.gui.lore.items.legendary.non_imbued;

import java.awt.Dimension;
import java.util.List;

import javax.swing.JDialog;

import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.classes.ClassDescription;
import delta.games.lotro.lore.items.EquipmentLocation;
import delta.games.lotro.lore.items.legendary.non_imbued.NonImbuedLegaciesManager;
import delta.games.lotro.lore.items.legendary.non_imbued.TieredNonImbuedLegacy;
import delta.games.lotro.utils.gui.chooser.ObjectChoiceWindowController;

/**
 * Facade for non-imbued legacies choosers.
 * @author DAM
 */
public class NonImbuedLegacyChooser
{
  private static ObjectChoiceWindowController<TieredNonImbuedLegacy> buildTieredLegacyChooser(WindowController parent, List<TieredNonImbuedLegacy> legacies, TieredNonImbuedLegacy selectedLegacy)
  {
    // Table
    GenericTableController<TieredNonImbuedLegacy> table=TieredNonImbuedLegaciesTableBuilder.buildTable(legacies);
    // Filter
    // ... none ...

    // Build and configure chooser
    ObjectChoiceWindowController<TieredNonImbuedLegacy> chooser=new ObjectChoiceWindowController<TieredNonImbuedLegacy>(parent,null,table);
    table.getTable();
    // - selection
    table.selectItem(selectedLegacy);
    // - filter
    // ... none ...
    JDialog dialog=chooser.getDialog();
    // - title
    dialog.setTitle("Choose non-imbued legacy: ");
    // - dimension
    dialog.setMinimumSize(new Dimension(600,200));
    dialog.setSize(500,dialog.getHeight());
    return chooser;
  }

  /**
   * Show the tiered non-imbued legacy selection dialog.
   * @param parent Parent controller.
   * @param characterClass Character class to use.
   * @param slot Slot to use.
   * @param selectedLegacy Selected legacy.
   * @return The selected tiered non-imbued legacy or <code>null</code> if the window was closed or canceled.
   */
  public static TieredNonImbuedLegacy selectTieredNonImbuedLegacy(WindowController parent, ClassDescription characterClass, EquipmentLocation slot, TieredNonImbuedLegacy selectedLegacy)
  {
    NonImbuedLegaciesManager legaciesMgr=NonImbuedLegaciesManager.getInstance();
    List<TieredNonImbuedLegacy> legacies=legaciesMgr.getTieredLegacies(characterClass,slot);
    // Build chooser
    ObjectChoiceWindowController<TieredNonImbuedLegacy> chooser=buildTieredLegacyChooser(parent,legacies,selectedLegacy);
    // Show modal
    TieredNonImbuedLegacy chosenLegacy=chooser.editModal();
    return chosenLegacy;
  }
}
