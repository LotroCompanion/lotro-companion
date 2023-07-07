package delta.games.lotro.gui.lore.emotes;

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
import delta.games.lotro.config.DataFiles;
import delta.games.lotro.config.LotroCoreConfig;
import delta.games.lotro.gui.LotroIconsManager;
import delta.games.lotro.gui.utils.UiConfiguration;
import delta.games.lotro.lore.emotes.EmoteDescription;
import delta.games.lotro.lore.emotes.io.xml.EmoteXMLParser;

/**
 * Builder for a table that shows emotes.
 * @author DAM
 */
public class EmotesTableBuilder
{
  /**
   * Build a table with emotes.
   * @return the new table.
   */
  public static GenericTableController<EmoteDescription> buildTable()
  {
    // Load data
    List<EmoteDescription> emotes=loadEmotes();

    // Build table
    ListDataProvider<EmoteDescription> provider=new ListDataProvider<EmoteDescription>(emotes);
    GenericTableController<EmoteDescription> table=new GenericTableController<EmoteDescription>(provider);
    List<DefaultTableColumnController<EmoteDescription,?>> columns=buildColumns();
    for(DefaultTableColumnController<EmoteDescription,?> column : columns)
    {
      table.addColumnController(column);
    }

    // Setup columns
    TableColumnsManager<EmoteDescription> columnsManager=table.getColumnsManager();
    List<String> columnIds=getDefaultColumnIds();
    columnsManager.setDefaultColumnsIds(columnIds);
    columnsManager.setColumns(columnIds);

    // Configure table
    JTable jtable=table.getTable();
    // Adjust table row height for icons (32 pixels)
    jtable.setRowHeight(32);

    return table;
  }

  /**
   * Build the columns for a table of emotes.
   * @return A list of columns for a table of emotes.
   */
  private static List<DefaultTableColumnController<EmoteDescription,?>> buildColumns()
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
          Icon icon=LotroIconsManager.getEmoteIcon(id);
          return icon;
        }
      };
      DefaultTableColumnController<EmoteDescription,Icon> iconColumn=new DefaultTableColumnController<EmoteDescription,Icon>(EmoteColumnIds.ICON.name(),"Icon",Icon.class,iconCell); // I18n
      iconColumn.setWidthSpecs(50,50,50);
      iconColumn.setSortable(false);
      ret.add(iconColumn);
    }
    // Identifier column
    if (UiConfiguration.showTechnicalColumns())
    {
      CellDataProvider<EmoteDescription,Integer> idCell=new CellDataProvider<EmoteDescription,Integer>()
      {
        @Override
        public Integer getData(EmoteDescription emote)
        {
          return Integer.valueOf(emote.getIdentifier());
        }
      };
      DefaultTableColumnController<EmoteDescription,Integer> idColumn=new DefaultTableColumnController<EmoteDescription,Integer>(EmoteColumnIds.ID.name(),"ID",Integer.class,idCell); // I18n
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
      DefaultTableColumnController<EmoteDescription,String> commandColumn=new DefaultTableColumnController<EmoteDescription,String>(EmoteColumnIds.COMMAND.name(),"Command",String.class,commandCell); // I18n
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
      DefaultTableColumnController<EmoteDescription,Boolean> autoColumn=new DefaultTableColumnController<EmoteDescription,Boolean>(EmoteColumnIds.AUTO.name(),"Auto",Boolean.class,autoCell); // I18n
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
      DefaultTableColumnController<EmoteDescription,String> nameColumn=new DefaultTableColumnController<EmoteDescription,String>(EmoteColumnIds.DESCRIPTION.name(),"Description",String.class,nameCell); // I18n
      nameColumn.setWidthSpecs(100,-1,200);
      ret.add(nameColumn);
    }
    return ret;
  }

  private static List<String> getDefaultColumnIds()
  {
    List<String> columnIds=new ArrayList<String>();
    columnIds.add(EmoteColumnIds.ICON.name());
    columnIds.add(EmoteColumnIds.ID.name());
    columnIds.add(EmoteColumnIds.AUTO.name());
    columnIds.add(EmoteColumnIds.COMMAND.name());
    columnIds.add(EmoteColumnIds.DESCRIPTION.name());
    return columnIds;
  }

  private static List<EmoteDescription> loadEmotes()
  {
    File fromFile=LotroCoreConfig.getInstance().getFile(DataFiles.EMOTES);
    List<EmoteDescription> emotes=new EmoteXMLParser().parseXML(fromFile);
    return emotes;
  }
}
