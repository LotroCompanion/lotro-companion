package delta.games.lotro.gui.character.buffs;

import java.awt.Dimension;
import java.util.List;

import javax.swing.JDialog;

import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.stats.buffs.Buff;
import delta.games.lotro.character.stats.buffs.BuffFilter;
import delta.games.lotro.utils.gui.chooser.ObjectChoiceWindowController;
import delta.games.lotro.utils.gui.filter.ObjectFilterPanelController;

/**
 * Controller for a "buff choice" dialog.
 * @author DAM
 */
public class BuffChoiceWindowController
{
  private static ObjectChoiceWindowController<Buff> buildChooser(WindowController parent, List<Buff> possibleBuffs, Buff selectedBuff)
  {
    // Table
    GenericTableController<Buff> buffsTable=BuffsTableBuilder.buildTable(possibleBuffs);
    // Filter
    BuffFilter filter=new BuffFilter();
    ObjectFilterPanelController filterUiController=new BuffsFilterController(possibleBuffs,filter);

    // Build and configure chooser
    ObjectChoiceWindowController<Buff> chooser=new ObjectChoiceWindowController<Buff>(parent,null,buffsTable);
    // - selection
    buffsTable.selectItem(selectedBuff);
    // - filter
    chooser.setFilter(filter,filterUiController);
    JDialog dialog=chooser.getDialog();
    // - title
    dialog.setTitle("Choose buff: "); // I18n
    // - dimension
    dialog.setSize(500,500);
    dialog.setMinimumSize(new Dimension(500,400));
    return chooser;
  }

  /**
   * Show the buff selection dialog.
   * @param parent Parent controller.
   * @param possibleBuffs Possible buffs.
   * @param selectedBuff Selected buff.
   * @return The selected buff or <code>null</code> if the window was closed or canceled.
   */
  public static Buff selectBuff(WindowController parent, List<Buff> possibleBuffs, Buff selectedBuff)
  {
    // Build chooser
    ObjectChoiceWindowController<Buff> chooser=buildChooser(parent,possibleBuffs,selectedBuff);
    if (parent!=null)
    {
      chooser.getWindow().setLocationRelativeTo(parent.getWindow());
    }
    Buff chosenBuff=chooser.editModal();
    return chosenBuff;
  }
}
