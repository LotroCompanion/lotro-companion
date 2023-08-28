package delta.games.lotro.gui.toon;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import delta.common.ui.swing.tables.CellDataProvider;
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
import delta.games.lotro.gui.utils.l10n.StatColumnsUtils;
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
  public static <T extends CharacterReference> List<TableColumnController<T,?>> buildCharacterReferenceColumns(Class<T> clazz)
  {
    List<TableColumnController<T,?>> ret=new ArrayList<TableColumnController<T,?>>();
    // ID column
    if (UiConfiguration.showTechnicalColumns())
    {
      CellDataProvider<T,String> iidCell=new CellDataProvider<T,String>()
      {
        @Override
        public String getData(T kinship)
        {
          InternalGameId iid=kinship.getId();
          if (iid!=null)
          {
            return iid.asPersistedString();
          }
          return null;
        }
      };
      DefaultTableColumnController<T,String> iidColumn=new DefaultTableColumnController<T,String>(ToonsTableColumnIds.IID.name(),"ID",String.class,iidCell); // I18n
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
      DefaultTableColumnController<T,String> nameColumn=new DefaultTableColumnController<T,String>(ToonsTableColumnIds.NAME.name(),"Name",String.class,nameCell); // I18n
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
      DefaultTableColumnController<T,ClassDescription> classColumn=new DefaultTableColumnController<T,ClassDescription>(ToonsTableColumnIds.CLASS.name(),"Class",ClassDescription.class,classCell); // I18n
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
      DefaultTableColumnController<T,Integer> levelColumn=new DefaultTableColumnController<T,Integer>(ToonsTableColumnIds.LEVEL.name(),"Level",Integer.class,levelCell); // I18n
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
      DefaultTableColumnController<T,RaceDescription> raceColumn=new DefaultTableColumnController<T,RaceDescription>(ToonsTableColumnIds.RACE.name(),"Race",RaceDescription.class,raceCell); // I18n
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
      DefaultTableColumnController<T,CharacterSex> sexColumn=new DefaultTableColumnController<T,CharacterSex>(ToonsTableColumnIds.SEX.name(),"Sex",CharacterSex.class,sexCell); // I18n
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
      DefaultTableColumnController<T,String> serverColumn=new DefaultTableColumnController<T,String>(ToonsTableColumnIds.SERVER.name(),"Server",String.class,serverCell); // I18n
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
      DefaultTableColumnController<T,String> accountColumn=new DefaultTableColumnController<T,String>(ToonsTableColumnIds.ACCOUNT.name(),"Account",String.class,accountCell); // I18n
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
      DefaultTableColumnController<T,String> accountColumn=new DefaultTableColumnController<T,String>(ToonsTableColumnIds.SUBSCRIPTION.name(),"Subscription",String.class,subscriptionCell); // I18n
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
      DefaultTableColumnController<CharacterSummary,String> regionColumn=new DefaultTableColumnController<CharacterSummary,String>(ToonsTableColumnIds.REGION.name(),"Region",String.class,regionCell); // I18n
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
      DefaultTableColumnController<CharacterSummary,String> kinshipColumn=new DefaultTableColumnController<CharacterSummary,String>(ToonsTableColumnIds.KINSHIP.name(),"Kinship",String.class,kinshipCell); // I18n
      kinshipColumn.setWidthSpecs(120,120,120);
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
      DefaultTableColumnController<CharacterSummary,Date> importDateColumn=new DefaultTableColumnController<CharacterSummary,Date>(ToonsTableColumnIds.IMPORT_DATE.name(),"Import Date",Date.class,importDateCell); // I18n
      StatColumnsUtils.configureDateTimeColumn(importDateColumn);
      ret.add(importDateColumn);
    }
    return ret;
  }
}
