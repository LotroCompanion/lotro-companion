package delta.games.lotro.gui.items.legendary.titles;

import java.awt.Dimension;
import java.util.List;

import javax.swing.JDialog;

import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.lore.items.legendary.titles.LegendaryTitle;
import delta.games.lotro.lore.items.legendary.titles.LegendaryTitleFilter;
import delta.games.lotro.lore.items.legendary.titles.LegendaryTitlesManager;
import delta.games.lotro.utils.gui.chooser.ObjectChoiceWindowController;
import delta.games.lotro.utils.gui.filter.ObjectFilterPanelController;

/**
 * Controller for a "legendary title choice" dialog.
 * @author DAM
 */
public class LegendaryTitleChooser
{
  private static ObjectChoiceWindowController<LegendaryTitle> buildChooser(WindowController parent, LegendaryTitle selectedTitle)
  {
    // Data
    LegendaryTitlesManager titlesMgr=LegendaryTitlesManager.getInstance();
    List<LegendaryTitle> titles=titlesMgr.getAll();
    // Table
    GenericTableController<LegendaryTitle> table=LegendaryTitlesTableBuilder.buildTable(titles);
    // Filter
    LegendaryTitleFilter filter=new LegendaryTitleFilter();
    ObjectFilterPanelController filterUiController=new LegendaryTitlesFilterController(filter);

    // Build and configure chooser
    ObjectChoiceWindowController<LegendaryTitle> chooser=new ObjectChoiceWindowController<LegendaryTitle>(parent,null,table);
    table.getTable();
    // - selection
    table.selectItem(selectedTitle);
    // - filter
    chooser.setFilter(filter,filterUiController);
    JDialog dialog=chooser.getDialog();
    // - title
    dialog.setTitle("Choose legendary title: ");
    // - dimension
    dialog.setMinimumSize(new Dimension(900,300));
    return chooser;
  }

  /**
   * Show the legendary title selection dialog.
   * @param parent Parent controller.
   * @param selectedTitle Selected legendary title.
   * @return The selected legendary title or <code>null</code> if the window was closed or canceled.
   */
  public static LegendaryTitle selectLegendaryTitle(WindowController parent, LegendaryTitle selectedTitle)
  {
    // Build chooser
    ObjectChoiceWindowController<LegendaryTitle> chooser=buildChooser(parent,selectedTitle);
    // Show modal
    LegendaryTitle chosenLegendaryTitle=chooser.editModal();
    return chosenLegendaryTitle;
  }
}
