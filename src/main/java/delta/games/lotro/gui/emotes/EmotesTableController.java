package delta.games.lotro.gui.emotes;

import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JTable;

import delta.common.ui.swing.tables.CellDataProvider;
import delta.common.ui.swing.tables.DefaultTableColumnController;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.tables.ListDataProvider;
import delta.common.ui.swing.tables.TableColumnsManager;
import delta.common.utils.collections.filters.Filter;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.gui.LotroIconsManager;
import delta.games.lotro.gui.items.chooser.ItemChoiceWindowController;
import delta.games.lotro.lore.emotes.EmoteDescription;
import delta.games.lotro.lore.emotes.io.xml.EmoteXMLParser;

/**
 * Controller for a table that shows emotes.
 * @author DAM
 */
public class EmotesTableController
{
  // Data
  private TypedProperties _prefs;
  private List<EmoteDescription> _emotes;
  // GUI
  private JTable _table;
  private GenericTableController<EmoteDescription> _tableController;

  /**
   * Constructor.
   * @param prefs Preferences.
   * @param filter Managed filter.
   */
  public EmotesTableController(TypedProperties prefs, Filter<EmoteDescription> filter)
  {
    _prefs=prefs;
    _emotes=new ArrayList<EmoteDescription>();
    init();
    _tableController=buildTable();
    _tableController.setFilter(filter);
    configureTable();
  }

  private GenericTableController<EmoteDescription> buildTable()
  {
    ListDataProvider<EmoteDescription> provider=new ListDataProvider<EmoteDescription>(_emotes);
    GenericTableController<EmoteDescription> table=new GenericTableController<EmoteDescription>(provider);
    List<DefaultTableColumnController<EmoteDescription,?>> columns=buildColumns();
    for(DefaultTableColumnController<EmoteDescription,?> column : columns)
    {
      table.addColumnController(column);
    }

    TableColumnsManager<EmoteDescription> columnsManager=table.getColumnsManager();
    List<String> columnsIds=getColumnIds();
    columnsManager.setColumns(columnsIds);
    return table;
  }

  /**
   * Build the columns for a recipes table.
   * @return A list of columns for a recipes table.
   */
  public static List<DefaultTableColumnController<EmoteDescription,?>> buildColumns()
  {
    List<DefaultTableColumnController<EmoteDescription,?>> ret=new ArrayList<DefaultTableColumnController<EmoteDescription,?>>();
    // Icon column
    {
      CellDataProvider<EmoteDescription,Icon> iconCell=new CellDataProvider<EmoteDescription,Icon>()
      {
        @Override
        public Icon getData(EmoteDescription emote)
        {
          int id=emote.getIconId();
          Icon icon=LotroIconsManager.getEmoteIcon(id+".png");
          return icon;
        }
      };
      DefaultTableColumnController<EmoteDescription,Icon> iconColumn=new DefaultTableColumnController<EmoteDescription,Icon>(EmoteColumnIds.ICON.name(),"Icon",Icon.class,iconCell);
      iconColumn.setWidthSpecs(50,50,50);
      iconColumn.setSortable(false);
      ret.add(iconColumn);
    }
    // Identifier column
    {
      CellDataProvider<EmoteDescription,Integer> idCell=new CellDataProvider<EmoteDescription,Integer>()
      {
        @Override
        public Integer getData(EmoteDescription emote)
        {
          return Integer.valueOf(emote.getIdentifier());
        }
      };
      DefaultTableColumnController<EmoteDescription,Integer> idColumn=new DefaultTableColumnController<EmoteDescription,Integer>(EmoteColumnIds.ID.name(),"ID",Integer.class,idCell);
      idColumn.setWidthSpecs(80,80,80);
      ret.add(idColumn);
    }
    // Command column
    {
      CellDataProvider<EmoteDescription,String> commandCell=new CellDataProvider<EmoteDescription,String>()
      {
        @Override
        public String getData(EmoteDescription emote)
        {
          return emote.getCommand();
        }
      };
      DefaultTableColumnController<EmoteDescription,String> commandColumn=new DefaultTableColumnController<EmoteDescription,String>(EmoteColumnIds.COMMAND.name(),"Command",String.class,commandCell);
      commandColumn.setWidthSpecs(100,120,200);
      ret.add(commandColumn);
    }
    // Auto?
    {
      CellDataProvider<EmoteDescription,Boolean> autoCell=new CellDataProvider<EmoteDescription,Boolean>()
      {
        @Override
        public Boolean getData(EmoteDescription emote)
        {
          return Boolean.valueOf(emote.isAuto());
        }
      };
      DefaultTableColumnController<EmoteDescription,Boolean> autoColumn=new DefaultTableColumnController<EmoteDescription,Boolean>(EmoteColumnIds.AUTO.name(),"Auto",Boolean.class,autoCell);
      autoColumn.setWidthSpecs(30,30,30);
      ret.add(autoColumn);
    }
    // Description column
    {
      CellDataProvider<EmoteDescription,String> nameCell=new CellDataProvider<EmoteDescription,String>()
      {
        @Override
        public String getData(EmoteDescription emote)
        {
          return emote.getDescription();
        }
      };
      DefaultTableColumnController<EmoteDescription,String> nameColumn=new DefaultTableColumnController<EmoteDescription,String>(EmoteColumnIds.DESCRIPTION.name(),"Description",String.class,nameCell);
      nameColumn.setWidthSpecs(100,-1,200);
      ret.add(nameColumn);
    }
    return ret;
  }

  private List<String> getColumnIds()
  {
    List<String> columnIds=null;
    if (_prefs!=null)
    {
      columnIds=_prefs.getStringList(ItemChoiceWindowController.COLUMNS_PROPERTY);
    }
    if (columnIds==null)
    {
      columnIds=new ArrayList<String>();
      columnIds.add(EmoteColumnIds.ICON.name());
      columnIds.add(EmoteColumnIds.ID.name());
      columnIds.add(EmoteColumnIds.AUTO.name());
      columnIds.add(EmoteColumnIds.COMMAND.name());
      columnIds.add(EmoteColumnIds.DESCRIPTION.name());
    }
    return columnIds;
  }

  /**
   * Get the managed table controller.
   * @return the managed table controller.
   */
  public GenericTableController<EmoteDescription> getTableController()
  {
    return _tableController;
  }

  /**
   * Update managed filter.
   */
  public void updateFilter()
  {
    _tableController.filterUpdated();
  }

  /**
   * Get the total number of recipes.
   * @return A number of recipes.
   */
  public int getNbItems()
  {
    return _emotes.size();
  }

  /**
   * Get the number of filtered items in the managed table.
   * @return A number of items.
   */
  public int getNbFilteredItems()
  {
    int ret=_tableController.getNbFilteredItems();
    return ret;
  }

  private void reset()
  {
    _emotes.clear();
  }

  /**
   * Refresh table.
   */
  public void refresh()
  {
    init();
    if (_table!=null)
    {
      _tableController.refresh();
    }
  }

  private void init()
  {
    reset();
    File fromFile=new File("data/lore/emotes.xml").getAbsoluteFile();
    List<EmoteDescription> emotes=new EmoteXMLParser().parseXML(fromFile);
    for(EmoteDescription emote : emotes)
    {
      _emotes.add(emote);
    }
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
    if (_table==null)
    {
      _table=_tableController.getTable();
    }
    return _table;
  }

  /**
   * Add an action listener.
   * @param al Action listener to add.
   */
  public void addActionListener(ActionListener al)
  {
    _tableController.addActionListener(al);
  }

  /**
   * Remove an action listener.
   * @param al Action listener to remove.
   */
  public void removeActionListener(ActionListener al)
  {
    _tableController.removeActionListener(al);
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Preferences
    if (_prefs!=null)
    {
      List<String> columnIds=_tableController.getColumnsManager().getSelectedColumnsIds();
      _prefs.setStringList(ItemChoiceWindowController.COLUMNS_PROPERTY,columnIds);
      _prefs=null;
    }
    // GUI
    _table=null;
    if (_tableController!=null)
    {
      _tableController.dispose();
      _tableController=null;
    }
    // Data
    _emotes=null;
  }
}
