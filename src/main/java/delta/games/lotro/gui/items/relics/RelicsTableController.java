package delta.games.lotro.gui.items.relics;

import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JTable;

import delta.common.ui.swing.tables.CellDataProvider;
import delta.common.ui.swing.tables.DataProvider;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.tables.ListDataProvider;
import delta.common.ui.swing.tables.TableColumnController;
import delta.common.utils.collections.filters.Filter;
import delta.games.lotro.gui.LotroIconsManager;
import delta.games.lotro.lore.items.legendary.relics.Relic;

/**
 * Controller for a table that shows relics.
 * @author DAM
 */
public class RelicsTableController
{
  // Data
  private List<Relic> _relics;
  // GUI
  private GenericTableController<Relic> _tableController;

  /**
   * Constructor.
   * @param relics Relics to choose from.
   * @param filter Relics filter.
   */
  public RelicsTableController(List<Relic> relics, Filter<Relic> filter)
  {
    _relics=relics;
    _tableController=buildTable();
    _tableController.setFilter(filter);
    configureTable();
  }

  private GenericTableController<Relic> buildTable()
  {
    DataProvider<Relic> provider=new ListDataProvider<Relic>(_relics);
    GenericTableController<Relic> table=new GenericTableController<Relic>(provider);

    // Icon column
    {
      CellDataProvider<Relic,ImageIcon> iconCell=new CellDataProvider<Relic,ImageIcon>()
      {
        public ImageIcon getData(Relic item)
        {
          String filename=item.getIconFilename();
          ImageIcon icon=LotroIconsManager.getRelicIcon(filename);
          return icon;
        }
      };
      TableColumnController<Relic,ImageIcon> iconColumn=new TableColumnController<Relic,ImageIcon>("Icon",ImageIcon.class,iconCell);
      iconColumn.setWidthSpecs(50,50,50);
      iconColumn.setSortable(false);
      table.addColumnController(iconColumn);
    }
    // Category column
    /*
    {
      CellDataProvider<Relic,String> categoryCell=new CellDataProvider<Relic,String>()
      {
        public String getData(Relic item)
        {
          return item.getCategory();
        }
      };
      TableColumnController<Relic,String> categoryColumn=new TableColumnController<Relic,String>("Category",String.class,categoryCell);
      categoryColumn.setWidthSpecs(150,-1,150);
      table.addColumnController(categoryColumn);
    }
    */
    // Name column
    {
      CellDataProvider<Relic,String> nameCell=new CellDataProvider<Relic,String>()
      {
        public String getData(Relic item)
        {
          return item.getName();
        }
      };
      TableColumnController<Relic,String> nameColumn=new TableColumnController<Relic,String>("Name",String.class,nameCell);
      nameColumn.setWidthSpecs(100,230,210);
      table.addColumnController(nameColumn);
    }
    // Level column
    {
      CellDataProvider<Relic,Integer> levelCell=new CellDataProvider<Relic,Integer>()
      {
        public Integer getData(Relic item)
        {
          return item.getRequiredLevel();
        }
      };
      TableColumnController<Relic,Integer> levelColumn=new TableColumnController<Relic,Integer>("Level",Integer.class,levelCell);
      levelColumn.setWidthSpecs(70,70,50);
      table.addColumnController(levelColumn);
    }
    // Stats column
    {
      CellDataProvider<Relic,String> statsCell=new CellDataProvider<Relic,String>()
      {
        public String getData(Relic item)
        {
          String statsStr=item.getStats().toString();
          return statsStr;
        }
      };
      TableColumnController<Relic,String> statsColumn=new TableColumnController<Relic,String>("Stats",String.class,statsCell);
      statsColumn.setWidthSpecs(250,-1,250);
      table.addColumnController(statsColumn);
    }
    return table;
  }

  private void configureTable()
  {
    JTable table=getTable();
    // Adjust table row height for icons (32 pixels)
    table.setRowHeight(32);
  }

  /**
   * Get the managed table.
   * @return the managed table.
   */
  public JTable getTable()
  {
    return _tableController.getTable();
  }

  /**
   * Update managed filter.
   */
  public void updateFilter()
  {
    _tableController.filterUpdated();
  }

  /**
   * Get the total number of relics.
   * @return A number of relics.
   */
  public int getNbItems()
  {
    return _relics.size();
  }

  /**
   * Get the number of filtered items in the managed log.
   * @return A number of items.
   */
  public int getNbFilteredItems()
  {
    int ret=_tableController.getNbFilteredItems();
    return ret;
  }

  /**
   * Get the currently selected relic.
   * @return a relic or <code>null</code>.
   */
  public Relic getSelectedRelic()
  {
    return _tableController.getSelectedItem();
  }

  /**
   * Select a relic.
   * @param relic Relic to select (may be <code>null</code>).
   */
  public void selectRelic(Relic relic)
  {
    _tableController.selectItem(relic);
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // GUI
    if (_tableController!=null)
    {
      _tableController.dispose();
      _tableController=null;
    }
    // Data
    _relics=null;
  }
}
