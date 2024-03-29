package delta.games.lotro.gui.lore.items.legendary.relics;

import java.awt.Dimension;
import java.util.List;

import javax.swing.JDialog;

import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.lore.items.EquipmentLocation;
import delta.games.lotro.lore.items.legendary.relics.Relic;
import delta.games.lotro.lore.items.legendary.relics.RelicFilter;
import delta.games.lotro.lore.items.legendary.relics.RelicType;
import delta.games.lotro.lore.items.legendary.relics.RelicsManager;
import delta.games.lotro.utils.gui.chooser.ObjectChoiceWindowController;
import delta.games.lotro.utils.gui.filter.ObjectFilterPanelController;

/**
 * Controller for a "relic choice" dialog.
 * @author DAM
 */
public class RelicChooser
{
  private static ObjectChoiceWindowController<Relic> buildChooser(WindowController parent, RelicType type, EquipmentLocation slot, Relic selectedRelic)
  {
    // Data
    RelicsManager relicsMgr=RelicsManager.getInstance();
    List<Relic> relics=relicsMgr.getAllRelics(type,slot);
    // Table
    GenericTableController<Relic> relicsTable=RelicsTableBuilder.buildTable(relics);
    // Filter
    RelicFilter filter=new RelicFilter();
    filter.setRelicType(type);
    ObjectFilterPanelController filterUiController=new RelicsFilterController(filter,relics);

    // Build and configure chooser
    ObjectChoiceWindowController<Relic> chooser=new ObjectChoiceWindowController<Relic>(parent,null,relicsTable);
    // - selection
    relicsTable.selectItem(selectedRelic);
    // - filter
    chooser.setFilter(filter,filterUiController);
    JDialog dialog=chooser.getDialog();
    // - title
    dialog.setTitle("Choose relic: ");
    // - dimension
    dialog.setMinimumSize(new Dimension(900,300));
    // Add details column
    RelicsTableBuilder.addDetailsColumn(chooser,relicsTable);
    return chooser;
  }

  /**
   * Show the relic selection dialog.
   * @param parent Parent controller.
   * @param type Relic type to use (locks filter) or <code>null</code>.
   * @param slot Slot to use.
   * @param selectedRelic Selected relic.
   * @return The selected relic or <code>null</code> if the window was closed or canceled.
   */
  public static Relic selectRelic(WindowController parent, RelicType type, EquipmentLocation slot, Relic selectedRelic)
  {
    // Build chooser
    ObjectChoiceWindowController<Relic> chooser=buildChooser(parent,type,slot,selectedRelic);
    // Show modal
    Relic chosenRelic=chooser.editModal();
    return chosenRelic;
  }
}
