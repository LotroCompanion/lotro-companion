package delta.games.lotro.gui.lore.items.legendary.passives;

import java.awt.Dimension;
import java.util.List;

import javax.swing.JDialog;

import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.lore.items.legendary.PassivesManager;
import delta.games.lotro.lore.items.legendary.passives.Passive;
import delta.games.lotro.utils.gui.chooser.ObjectChoiceWindowController;

/**
 * Facade for passive chooser.
 * @author DAM
 */
public class PassiveChooser
{
  private static ObjectChoiceWindowController<Passive> buildPassiveChooser(WindowController parent, int itemLevel, List<Passive> passives, Passive selectedPassive)
  {
    // Table
    GenericTableController<Passive> table=PassivesTableBuilder.buildTable(passives,itemLevel);
    // Filter
    // ... none ...

    // Build and configure chooser
    ObjectChoiceWindowController<Passive> chooser=new ObjectChoiceWindowController<Passive>(parent,null,table);
    table.getTable();
    // - selection
    table.selectItem(selectedPassive);
    // - filter
    // ... none ...
    JDialog dialog=chooser.getDialog();
    // - title
    dialog.setTitle("Choose a passive: ");
    // - dimension
    dialog.setMinimumSize(new Dimension(600,200));
    dialog.setSize(500,dialog.getHeight());
    return chooser;
  }

  /**
   * Show the passive selection dialog.
   * @param parent Parent controller.
   * @param itemId Parent item.
   * @param itemLevel Item level to use for stats computations.
   * @param selectedPassive Selected passive.
   * @return The selected passive or <code>null</code> if the window was closed or canceled.
   */
  public static Passive selectPassive(WindowController parent, int itemId, int itemLevel, Passive selectedPassive)
  {
    PassivesManager passivesMgr=PassivesManager.getInstance();
    List<Passive> passives=passivesMgr.getPassivesForItem(itemId);
    // Build chooser
    ObjectChoiceWindowController<Passive> chooser=buildPassiveChooser(parent,itemLevel,passives,selectedPassive);
    // Show modal
    Passive chosenPassive=chooser.editModal();
    return chosenPassive;
  }
}
