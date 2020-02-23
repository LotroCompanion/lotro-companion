package delta.games.lotro.gui.items.legendary.relics;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JTable;

import delta.common.ui.swing.tables.CellDataProvider;
import delta.common.ui.swing.tables.DataProvider;
import delta.common.ui.swing.tables.DefaultTableColumnController;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.tables.ListDataProvider;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.common.CharacterClass;
import delta.games.lotro.gui.LotroIconsManager;
import delta.games.lotro.gui.items.ItemColumnIds;
import delta.games.lotro.lore.items.legendary.relics.Relic;

/**
 * Builder for a table that shows relics.
 * @author DAM
 */
public class RelicsTableBuilder
{
  /**
   * Build a table to show relics.
   * @param relics Relics to show.
   * @return A new table controller.
   */
  public static GenericTableController<Relic> buildTable(List<Relic> relics)
  {
    DataProvider<Relic> provider=new ListDataProvider<Relic>(relics);
    GenericTableController<Relic> table=new GenericTableController<Relic>(provider);

    // Icon column
    {
      CellDataProvider<Relic,ImageIcon> iconCell=new CellDataProvider<Relic,ImageIcon>()
      {
        @Override
        public ImageIcon getData(Relic item)
        {
          String filename=item.getIconFilename();
          ImageIcon icon=LotroIconsManager.getRelicIcon(filename);
          return icon;
        }
      };
      DefaultTableColumnController<Relic,ImageIcon> iconColumn=new DefaultTableColumnController<Relic,ImageIcon>("Icon",ImageIcon.class,iconCell);
      iconColumn.setWidthSpecs(50,50,50);
      iconColumn.setSortable(false);
      table.addColumnController(iconColumn);
    }
    // Name column
    {
      CellDataProvider<Relic,String> nameCell=new CellDataProvider<Relic,String>()
      {
        @Override
        public String getData(Relic item)
        {
          return item.getName();
        }
      };
      DefaultTableColumnController<Relic,String> nameColumn=new DefaultTableColumnController<Relic,String>("Name",String.class,nameCell);
      nameColumn.setWidthSpecs(100,230,210);
      table.addColumnController(nameColumn);
    }
    // Category column
    {
      CellDataProvider<Relic,String> categoryCell=new CellDataProvider<Relic,String>()
      {
        public String getData(Relic item)
        {
          return item.getCategory().getName();
        }
      };
      DefaultTableColumnController<Relic,String> categoryColumn=new DefaultTableColumnController<Relic,String>("Category",String.class,categoryCell);
      categoryColumn.setWidthSpecs(80,100,100);
      table.addColumnController(categoryColumn);
    }
    // Level column
    {
      CellDataProvider<Relic,Integer> levelCell=new CellDataProvider<Relic,Integer>()
      {
        @Override
        public Integer getData(Relic item)
        {
          return item.getRequiredLevel();
        }
      };
      DefaultTableColumnController<Relic,Integer> levelColumn=new DefaultTableColumnController<Relic,Integer>("Level",Integer.class,levelCell);
      levelColumn.setWidthSpecs(70,70,50);
      table.addColumnController(levelColumn);
    }
    // Class column
    {
      CellDataProvider<Relic,CharacterClass> classCell=new CellDataProvider<Relic,CharacterClass>()
      {
        @Override
        public CharacterClass getData(Relic item)
        {
          return item.getUsageRequirement().getRequiredClass();
        }
      };
      DefaultTableColumnController<Relic,CharacterClass> classColumn=new DefaultTableColumnController<Relic,CharacterClass>("Class",CharacterClass.class,classCell);
      classColumn.setWidthSpecs(100,100,100);
      table.addColumnController(classColumn);
    }
    // Stats column
    {
      CellDataProvider<Relic,String> statsCell=new CellDataProvider<Relic,String>()
      {
        @Override
        public String getData(Relic item)
        {
          String statsStr=item.getStats().toString();
          return statsStr;
        }
      };
      DefaultTableColumnController<Relic,String> statsColumn=new DefaultTableColumnController<Relic,String>("Stats",String.class,statsCell);
      statsColumn.setWidthSpecs(250,-1,250);
      table.addColumnController(statsColumn);
    }
    // Adjust table row height for icons (32 pixels)
    JTable swingTable=table.getTable();
    swingTable.setRowHeight(32);
    return table;
  }

  /**
   * Add a details column on the given table.
   * @param parent Parent window.
   * @param table Table to use.
   * @return A column controller.
   */
  public static DefaultTableColumnController<Relic,String> addDetailsColumn(final WindowController parent, GenericTableController<Relic> table)
  {
    DefaultTableColumnController<Relic,String> column=table.buildButtonColumn(ItemColumnIds.DETAILS.name(),"Details...",90);
    ActionListener al=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        Relic source=(Relic)e.getSource();
        RelicUiTools.showRelicForm(parent,source);
      }
    };
    column.setActionListener(al);
    table.addColumnController(column);
    table.updateColumns();
    return column;
  }
}
