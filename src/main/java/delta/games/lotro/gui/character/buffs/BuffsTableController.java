package delta.games.lotro.gui.character.buffs;

import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JTable;

import delta.games.lotro.character.stats.buffs.Buff;
import delta.games.lotro.character.stats.buffs.BuffFilter;
import delta.games.lotro.common.CharacterClass;
import delta.games.lotro.common.Race;
import delta.games.lotro.gui.utils.IconsManager;
import delta.games.lotro.utils.gui.tables.CellDataProvider;
import delta.games.lotro.utils.gui.tables.DataProvider;
import delta.games.lotro.utils.gui.tables.GenericTableController;
import delta.games.lotro.utils.gui.tables.ListDataProvider;
import delta.games.lotro.utils.gui.tables.TableColumnController;

/**
 * Controller for a table that shows buffs.
 * @author DAM
 */
public class BuffsTableController
{
  // Data
  private List<Buff> _buffs;
  // GUI
  private GenericTableController<Buff> _tableController;

  /**
   * Constructor.
   * @param buffs Buffs to choose from.
   * @param filter Buffs filter.
   */
  public BuffsTableController(List<Buff> buffs, BuffFilter filter)
  {
    _buffs=buffs;
    _tableController=buildTable();
    _tableController.setFilter(filter);
    configureTable();
  }

  private GenericTableController<Buff> buildTable()
  {
    DataProvider<Buff> provider=new ListDataProvider<Buff>(_buffs);
    GenericTableController<Buff> table=new GenericTableController<Buff>(provider);

    // Icon column
    {
      CellDataProvider<Buff,ImageIcon> iconCell=new CellDataProvider<Buff,ImageIcon>()
      {
        public ImageIcon getData(Buff buff)
        {
          String filename=buff.getIcon();
          ImageIcon icon=IconsManager.getBuffIcon(filename);
          return icon;
        }
      };
      TableColumnController<Buff,ImageIcon> iconColumn=new TableColumnController<Buff,ImageIcon>("Icon",ImageIcon.class,iconCell);
      iconColumn.setWidthSpecs(50,50,50);
      iconColumn.setSortable(false);
      table.addColumnController(iconColumn);
    }
    // Name column
    {
      CellDataProvider<Buff,String> nameCell=new CellDataProvider<Buff,String>()
      {
        public String getData(Buff buff)
        {
          return buff.getLabel();
        }
      };
      TableColumnController<Buff,String> nameColumn=new TableColumnController<Buff,String>("Name",String.class,nameCell);
      nameColumn.setWidthSpecs(100,-1,210);
      table.addColumnController(nameColumn);
    }
    // Class column
    {
      CellDataProvider<Buff,CharacterClass> classCell=new CellDataProvider<Buff,CharacterClass>()
      {
        public CharacterClass getData(Buff buff)
        {
          return buff.getRequiredClass();
        }
      };
      TableColumnController<Buff,CharacterClass> classColumn=new TableColumnController<Buff,CharacterClass>("Class",CharacterClass.class,classCell);
      classColumn.setWidthSpecs(100,100,50);
      table.addColumnController(classColumn);
    }
    // Race column
    {
      CellDataProvider<Buff,Race> classCell=new CellDataProvider<Buff,Race>()
      {
        public Race getData(Buff buff)
        {
          return buff.getRequiredRace();
        }
      };
      TableColumnController<Buff,Race> classColumn=new TableColumnController<Buff,Race>("Race",Race.class,classCell);
      classColumn.setWidthSpecs(100,100,50);
      table.addColumnController(classColumn);
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
   * Get the total number of buffs.
   * @return A number of buffs.
   */
  public int getNbItems()
  {
    return _buffs.size();
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
   * Get the currently selected buff.
   * @return a buff or <code>null</code>.
   */
  public Buff getSelectedBuff()
  {
    return _tableController.getSelectedItem();
  }

  /**
   * Select a buff.
   * @param buff Buff to select (may be <code>null</code>).
   */
  public void selectBuff(Buff buff)
  {
    _tableController.selectItem(buff);
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
    _buffs=null;
  }
}
