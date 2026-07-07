package delta.games.lotro.gui.toon;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import delta.common.ui.swing.tables.CellDataProvider;
import delta.common.ui.swing.tables.ColumnsUtils;
import delta.common.ui.swing.tables.DefaultTableColumnController;
import delta.common.ui.swing.tables.TableColumnController;
import delta.games.lotro.account.AccountReference;
import delta.games.lotro.character.BaseCharacterSummary;
import delta.games.lotro.character.CharacterReference;
import delta.games.lotro.character.CharacterSummary;
import delta.games.lotro.character.classes.ClassDescription;
import delta.games.lotro.character.races.RaceDescription;
import delta.games.lotro.common.CharacterSex;
import delta.games.lotro.common.id.InternalGameId;
import delta.games.lotro.gui.utils.UiConfiguration;
import delta.games.lotro.gui.utils.l10n.Labels;
import delta.games.lotro.kinship.Kinship;
import delta.games.lotro.kinship.KinshipsManager;

/**
 * Builds column definitions for CharacterSummary data.
 * @author DAM
 */
public class CharacterSummaryColumnsBuilder
{
  /**
   * Build the columns to show a <code>BaseCharacterSummary</code>.
   * @param clazz Class of managed type.
   * @return a list of columns.
   */
  public static <T extends CharacterReference> List<TableColumnController<T,?>> buildCharacterReferenceColumns(Class<T> clazz) // NOSONAR
  {
    List<TableColumnController<T,?>> ret=new ArrayList<TableColumnController<T,?>>();
    // ID column
    if (UiConfiguration.showTechnicalColumns())
    {
      CellDataProvider<T,String> iidCell=new CellDataProvider<T,String>()
      {
        @Override
        public String getData(T item)
        {
          InternalGameId iid=item.getId();
          if (iid!=null)
          {
            return iid.asPersistedString();
          }
          return null;
        }
      };
      String columnName=Labels.getLabel("characters.table.column.iid");
      DefaultTableColumnController<T,String> iidColumn=new DefaultTableColumnController<T,String>(ToonsTableColumnIds.IID.name(),columnName,String.class,iidCell);
      iidColumn.setWidthSpecs(130,130,130);
      ret.add(iidColumn);
    }
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
      String columnName=Labels.getLabel("characters.table.column.name");
      DefaultTableColumnController<T,String> nameColumn=new DefaultTableColumnController<T,String>(ToonsTableColumnIds.NAME.name(),columnName,String.class,nameCell);
      nameColumn.setWidthSpecs(100,100,100);
      ret.add(nameColumn);
    }
    // Class column
    {
      CellDataProvider<T,ClassDescription> classCell=new CellDataProvider<T,ClassDescription>()
      {
        @Override
        public ClassDescription getData(T item)
        {
          return item.getCharacterClass();
        }
      };
      String columnName=Labels.getLabel("characters.table.column.class");
      DefaultTableColumnController<T,ClassDescription> classColumn=new DefaultTableColumnController<T,ClassDescription>(ToonsTableColumnIds.CLASS.name(),columnName,ClassDescription.class,classCell);
      classColumn.setWidthSpecs(100,100,100);
      ret.add(classColumn);
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
      String columnName=Labels.getLabel("characters.table.column.level");
      DefaultTableColumnController<T,Integer> levelColumn=new DefaultTableColumnController<T,Integer>(ToonsTableColumnIds.LEVEL.name(),columnName,Integer.class,levelCell);
      levelColumn.setWidthSpecs(50,50,50);
      ret.add(levelColumn);
    }
    return ret;
  }

  /**
   * Build the columns to show a <code>BaseCharacterSummary</code>.
   * @param clazz Class of managed type.
   * @return a list of columns.
   */
  public static <T extends BaseCharacterSummary> List<TableColumnController<T,?>> buildBaseCharacterSummaryColumns(Class<T> clazz)
  {
    List<TableColumnController<T,?>> ret=new ArrayList<TableColumnController<T,?>>();
    // Character reference columns
    for(TableColumnController<T,?> column : buildCharacterReferenceColumns(clazz))
    {
      ret.add(column);
    }
    // Race column
    {
      CellDataProvider<T,RaceDescription> raceCell=new CellDataProvider<T,RaceDescription>()
      {
        @Override
        public RaceDescription getData(T item)
        {
          return item.getRace();
        }
      };
      String columnName=Labels.getLabel("characters.table.column.race");
      DefaultTableColumnController<T,RaceDescription> raceColumn=new DefaultTableColumnController<T,RaceDescription>(ToonsTableColumnIds.RACE.name(),columnName,RaceDescription.class,raceCell);
      raceColumn.setWidthSpecs(100,100,100);
      ret.add(raceColumn);
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
      String columnName=Labels.getLabel("characters.table.column.gender");
      DefaultTableColumnController<T,CharacterSex> sexColumn=new DefaultTableColumnController<T,CharacterSex>(ToonsTableColumnIds.SEX.name(),columnName,CharacterSex.class,sexCell);
      sexColumn.setWidthSpecs(80,80,80);
      ret.add(sexColumn);
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
      String columnName=Labels.getLabel("characters.table.column.server");
      DefaultTableColumnController<T,String> serverColumn=new DefaultTableColumnController<T,String>(ToonsTableColumnIds.SERVER.name(),columnName,String.class,serverCell);
      serverColumn.setWidthSpecs(100,100,100);
      ret.add(serverColumn);
    }
    // Account name column
    {
      CellDataProvider<T,String> accountCell=new CellDataProvider<T,String>()
      {
        @Override
        public String getData(T item)
        {
          AccountReference id=item.getAccountID();
          return (id!=null)?id.getAccountName():"";
        }
      };
      String columnName=Labels.getLabel("characters.table.column.account");
      DefaultTableColumnController<T,String> accountColumn=new DefaultTableColumnController<T,String>(ToonsTableColumnIds.ACCOUNT.name(),columnName,String.class,accountCell);
      accountColumn.setWidthSpecs(100,100,100);
      ret.add(accountColumn);
    }
    // Subscription column
    {
      CellDataProvider<T,String> subscriptionCell=new CellDataProvider<T,String>()
      {
        @Override
        public String getData(T item)
        {
          AccountReference id=item.getAccountID();
          return (id!=null)?id.getSubscriptionKey():"";
        }
      };
      String columnName=Labels.getLabel("characters.table.column.subscription");
      DefaultTableColumnController<T,String> accountColumn=new DefaultTableColumnController<T,String>(ToonsTableColumnIds.SUBSCRIPTION.name(),columnName,String.class,subscriptionCell);
      accountColumn.setWidthSpecs(230,230,230);
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
    List<TableColumnController<CharacterSummary,?>> ret=buildBaseCharacterSummaryColumns(CharacterSummary.class);
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
      String columnName=Labels.getLabel("characters.table.column.region");
      DefaultTableColumnController<CharacterSummary,String> regionColumn=new DefaultTableColumnController<CharacterSummary,String>(ToonsTableColumnIds.REGION.name(),columnName,String.class,regionCell);
      regionColumn.setWidthSpecs(100,100,100);
      ret.add(regionColumn);
    }
    // Kinship column
    {
      CellDataProvider<CharacterSummary,String> kinshipCell=new CellDataProvider<CharacterSummary,String>()
      {
        @Override
        public String getData(CharacterSummary summary)
        {
          InternalGameId kinshipID=summary.getKinshipID();
          if (kinshipID==null)
          {
            return null;
          }
          Kinship kinship=KinshipsManager.getInstance().getKinshipByID(kinshipID.asLong());
          if (kinship!=null)
          {
            return kinship.getName();
          }
          return null;
        }
      };
      String columnName=Labels.getLabel("characters.table.column.kinship");
      DefaultTableColumnController<CharacterSummary,String> kinshipColumn=new DefaultTableColumnController<CharacterSummary,String>(ToonsTableColumnIds.KINSHIP.name(),columnName,String.class,kinshipCell);
      kinshipColumn.setWidthSpecs(120,-1,120);
      ret.add(kinshipColumn);
    }
    // Import date column
    {
      CellDataProvider<CharacterSummary,Date> importDateCell=new CellDataProvider<CharacterSummary,Date>()
      {
        @Override
        public Date getData(CharacterSummary summary)
        {
          Long importDate=summary.getImportDate();
          return (importDate!=null)?new Date(importDate.longValue()):null;
        }
      };
      String columnName=Labels.getLabel("characters.table.column.importDate");
      DefaultTableColumnController<CharacterSummary,Date> importDateColumn=new DefaultTableColumnController<CharacterSummary,Date>(ToonsTableColumnIds.IMPORT_DATE.name(),columnName,Date.class,importDateCell);
      ColumnsUtils.configureDateTimeColumn(importDateColumn);
      ret.add(importDateColumn);
    }
    return ret;
  }
}
