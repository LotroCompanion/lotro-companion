package delta.games.lotro.gui.toon;

import java.util.ArrayList;
import java.util.List;

import delta.common.ui.swing.tables.CellDataProvider;
import delta.common.ui.swing.tables.DefaultTableColumnController;
import delta.common.ui.swing.tables.TableColumnController;
import delta.games.lotro.character.BaseCharacterSummary;
import delta.games.lotro.character.CharacterSummary;
import delta.games.lotro.common.CharacterClass;
import delta.games.lotro.common.CharacterSex;
import delta.games.lotro.common.Race;

/**
 * Builds column definitions for CharacterSummary data.
 * @author DAM
 */
public class CharacterSummaryColumnsBuilder
{
  /**
   * Build the columns to show a <code>BaseCharacterSummary</code>.
   * @return a list of columns.
   */
  public static <T extends BaseCharacterSummary> List<TableColumnController<T,?>> buildBaseCharacterSummaryColumns()
  {
    List<TableColumnController<T,?>> ret=new ArrayList<TableColumnController<T,?>>();
    // Name column
    {
      CellDataProvider<T,String> nameCell=new CellDataProvider<T,String>()
      {
        @Override
        public String getData(T item)
        {
          return item.getName();
        }
      };
      DefaultTableColumnController<T,String> nameColumn=new DefaultTableColumnController<T,String>(ToonsTableColumnIds.NAME.name(),"Name",String.class,nameCell);
      nameColumn.setWidthSpecs(100,100,100);
      ret.add(nameColumn);
    }
    // Race column
    {
      CellDataProvider<T,Race> raceCell=new CellDataProvider<T,Race>()
      {
        @Override
        public Race getData(T item)
        {
          return item.getRace();
        }
      };
      DefaultTableColumnController<T,Race> raceColumn=new DefaultTableColumnController<T,Race>(ToonsTableColumnIds.RACE.name(),"Race",Race.class,raceCell);
      raceColumn.setWidthSpecs(100,100,100);
      ret.add(raceColumn);
    }
    // Class column
    {
      CellDataProvider<T,CharacterClass> classCell=new CellDataProvider<T,CharacterClass>()
      {
        @Override
        public CharacterClass getData(T item)
        {
          return item.getCharacterClass();
        }
      };
      DefaultTableColumnController<T,CharacterClass> classColumn=new DefaultTableColumnController<T,CharacterClass>(ToonsTableColumnIds.CLASS.name(),"Class",CharacterClass.class,classCell);
      classColumn.setWidthSpecs(100,100,100);
      ret.add(classColumn);
    }
    // Sex column
    {
      CellDataProvider<T,CharacterSex> sexCell=new CellDataProvider<T,CharacterSex>()
      {
        @Override
        public CharacterSex getData(T item)
        {
          return item.getCharacterSex();
        }
      };
      DefaultTableColumnController<T,CharacterSex> sexColumn=new DefaultTableColumnController<T,CharacterSex>(ToonsTableColumnIds.SEX.name(),"Sex",CharacterSex.class,sexCell);
      sexColumn.setWidthSpecs(80,80,80);
      ret.add(sexColumn);
    }
    // Level column
    {
      CellDataProvider<T,Integer> levelCell=new CellDataProvider<T,Integer>()
      {
        @Override
        public Integer getData(T item)
        {
          return Integer.valueOf(item.getLevel());
        }
      };
      DefaultTableColumnController<T,Integer> levelColumn=new DefaultTableColumnController<T,Integer>(ToonsTableColumnIds.LEVEL.name(),"Level",Integer.class,levelCell);
      levelColumn.setWidthSpecs(50,50,50);
      ret.add(levelColumn);
    }
    // Server column
    {
      CellDataProvider<T,String> serverCell=new CellDataProvider<T,String>()
      {
        @Override
        public String getData(T item)
        {
          return item.getServer();
        }
      };
      DefaultTableColumnController<T,String> serverColumn=new DefaultTableColumnController<T,String>(ToonsTableColumnIds.SERVER.name(),"Server",String.class,serverCell);
      serverColumn.setWidthSpecs(100,100,100);
      ret.add(serverColumn);
    }
    // Account column
    {
      CellDataProvider<T,String> accountCell=new CellDataProvider<T,String>()
      {
        @Override
        public String getData(T item)
        {
          return item.getAccountName();
        }
      };
      DefaultTableColumnController<T,String> accountColumn=new DefaultTableColumnController<T,String>(ToonsTableColumnIds.ACCOUNT.name(),"Account",String.class,accountCell);
      accountColumn.setWidthSpecs(100,100,100);
      ret.add(accountColumn);
    }
    return ret;
  }

  /**
   * Build columns for a character summary.
   * @return A list of columns.
   */
  public static List<TableColumnController<CharacterSummary,?>> buildCharacterSummaryColumns()
  {
    List<TableColumnController<CharacterSummary,?>> ret=buildBaseCharacterSummaryColumns();
    // Region column
    {
      CellDataProvider<CharacterSummary,String> regionCell=new CellDataProvider<CharacterSummary,String>()
      {
        @Override
        public String getData(CharacterSummary item)
        {
          return item.getRegion();
        }
      };
      DefaultTableColumnController<CharacterSummary,String> regionColumn=new DefaultTableColumnController<CharacterSummary,String>(ToonsTableColumnIds.REGION.name(),"Region",String.class,regionCell);
      regionColumn.setWidthSpecs(100,100,100);
      ret.add(regionColumn);
    }
    return ret;
  }
}