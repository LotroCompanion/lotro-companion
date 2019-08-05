package delta.games.lotro.gui.items.legendary.passives;

import java.awt.Dimension;
import java.util.List;

import javax.swing.JDialog;

import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.common.effects.Effect;
import delta.games.lotro.lore.items.legendary.PassivesManager;
import delta.games.lotro.utils.gui.chooser.ObjectChoiceWindowController;

/**
 * Facade for passive chooser.
 * @author DAM
 */
public class PassiveChooser
{
  private static ObjectChoiceWindowController<Effect> buildPassiveChooser(WindowController parent, List<Effect> passives, Effect selectedPassive)
  {
    // Table
    GenericTableController<Effect> table=PassivesTableBuilder.buildTable(passives);
    // Filter
    // ... none ...

    // Build and configure chooser
    ObjectChoiceWindowController<Effect> chooser=new ObjectChoiceWindowController<Effect>(parent,null,table);
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
   * @param selectedPassive Selected passive.
   * @return The selected passive or <code>null</code> if the window was closed or canceled.
   */
  public static Effect selectPassive(WindowController parent, int itemId, Effect selectedPassive)
  {
    PassivesManager passivesMgr=PassivesManager.getInstance();
    List<Effect> passives=passivesMgr.getPassivesForItem(itemId);
    // Build chooser
    ObjectChoiceWindowController<Effect> chooser=buildPassiveChooser(parent,passives,selectedPassive);
    // Show modal
    Effect chosenPassive=chooser.editModal();
    // Dispose...
    chooser.dispose();
    return chosenPassive;
  }
}
