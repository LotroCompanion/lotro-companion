package delta.games.lotro.gui.character.buffs;

import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JTable;

import delta.common.ui.swing.tables.CellDataProvider;
import delta.common.ui.swing.tables.DataProvider;
import delta.common.ui.swing.tables.DefaultTableColumnController;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.tables.ListDataProvider;
import delta.games.lotro.character.classes.ClassDescription;
import delta.games.lotro.character.races.RaceDescription;
import delta.games.lotro.character.stats.buffs.Buff;
import delta.games.lotro.gui.LotroIconsManager;

/**
 * Builder for a table that shows buffs.
 * @author DAM
 */
public class BuffsTableBuilder
{
  /**
   * Build a table to show buffs.
   * @param buffs Buffs to show.
   * @return A new table controller.
   */
  public static GenericTableController<Buff> buildTable(List<Buff> buffs)
  {
    DataProvider<Buff> provider=new ListDataProvider<Buff>(buffs);
    GenericTableController<Buff> table=new GenericTableController<Buff>(provider);

    // Icon column
    {
      CellDataProvider<Buff,ImageIcon> iconCell=new CellDataProvider<Buff,ImageIcon>()
      {
        @Override
        public ImageIcon getData(Buff buff)
        {
          String filename=buff.getIcon();
          ImageIcon icon=LotroIconsManager.getBuffIcon(filename);
          return icon;
        }
      };
      DefaultTableColumnController<Buff,ImageIcon> iconColumn=new DefaultTableColumnController<Buff,ImageIcon>("Icon",ImageIcon.class,iconCell); // I18n
      iconColumn.setWidthSpecs(50,50,50);
      iconColumn.setSortable(false);
      table.addColumnController(iconColumn);
    }
    // Name column
    {
      CellDataProvider<Buff,String> nameCell=new CellDataProvider<Buff,String>()
      {
        @Override
        public String getData(Buff buff)
        {
          return buff.getLabel();
        }
      };
      DefaultTableColumnController<Buff,String> nameColumn=new DefaultTableColumnController<Buff,String>("Name",String.class,nameCell); // I18n
      nameColumn.setWidthSpecs(100,-1,210);
      table.addColumnController(nameColumn);
    }
    // Class column
    {
      CellDataProvider<Buff,ClassDescription> classCell=new CellDataProvider<Buff,ClassDescription>()
      {
        @Override
        public ClassDescription getData(Buff buff)
        {
          return buff.getRequiredClass();
        }
      };
      DefaultTableColumnController<Buff,ClassDescription> classColumn=new DefaultTableColumnController<Buff,ClassDescription>("Class",ClassDescription.class,classCell); // I18n
      classColumn.setWidthSpecs(100,100,50);
      table.addColumnController(classColumn);
    }
    // Race column
    {
      CellDataProvider<Buff,RaceDescription> classCell=new CellDataProvider<Buff,RaceDescription>()
      {
        @Override
        public RaceDescription getData(Buff buff)
        {
          return buff.getRequiredRace();
        }
      };
      DefaultTableColumnController<Buff,RaceDescription> classColumn=new DefaultTableColumnController<Buff,RaceDescription>("Race",RaceDescription.class,classCell); // I18n
      classColumn.setWidthSpecs(100,100,50);
      table.addColumnController(classColumn);
    }
    // Adjust table row height for icons (32 pixels)
    JTable swingTable=table.getTable();
    swingTable.setRowHeight(32);
    return table;
  }
}
