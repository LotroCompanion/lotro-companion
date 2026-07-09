package delta.games.lotro.gui.kinship;

import java.util.ArrayList;
import java.util.List;

import delta.common.ui.swing.tables.CellDataProvider;
import delta.common.ui.swing.tables.ColumnsUtils;
import delta.common.ui.swing.tables.DefaultTableColumnController;
import delta.common.ui.swing.tables.ProxiedTableColumnController;
import delta.common.ui.swing.tables.TableColumnController;
import delta.games.lotro.common.CharacterSex;
import delta.games.lotro.gui.toon.CharacterSummaryColumnsBuilder;
import delta.games.lotro.gui.utils.l10n.Labels;
import delta.games.lotro.kinship.KinshipCharacterSummary;
import delta.games.lotro.kinship.KinshipMember;
import delta.games.lotro.kinship.KinshipRank;

/**
 * Builds column definitions for KinshipMember data.
 * @author DAM
 */
public class KinshipMemberColumnsBuilder
{
  /**
   * Build the columns to show a <code>KinshipMember</code>.
   * @return a list of columns.
   */
  public static List<TableColumnController<KinshipMember,?>> build()
  {
    List<TableColumnController<KinshipMember,?>> columns=new ArrayList<TableColumnController<KinshipMember,?>>();
    // Summary columns
    List<TableColumnController<KinshipCharacterSummary,?>> summaryColumns=buildSummaryColumns();
    CellDataProvider<KinshipMember,KinshipCharacterSummary> dataProvider=new CellDataProvider<KinshipMember,KinshipCharacterSummary>()
    {
      @Override
      public KinshipCharacterSummary getData(KinshipMember kinshipMember)
      {
        return kinshipMember.getSummary();
      }
    };
    for(TableColumnController<KinshipCharacterSummary,?> summaryColumn : summaryColumns)
    {
      @SuppressWarnings("unchecked")
      TableColumnController<KinshipCharacterSummary,Object> c=(TableColumnController<KinshipCharacterSummary,Object>)summaryColumn;
      TableColumnController<KinshipMember,Object> proxiedColumn=new ProxiedTableColumnController<KinshipMember,KinshipCharacterSummary,Object>(c,dataProvider);
      columns.add(proxiedColumn);
    }
    // Members columns
    List<TableColumnController<KinshipMember,?>> detailsColumns=getKinshipMemberSpecificColumns();
    columns.addAll(detailsColumns);
    return columns;
  }

  /**
   * Build the columns to show a <code>KinshipCharacterSummary</code>.
   * @return a list of columns.
   */
  public static List<TableColumnController<KinshipCharacterSummary,?>> buildSummaryColumns()
  {
    List<TableColumnController<KinshipCharacterSummary,?>> columns=new ArrayList<TableColumnController<KinshipCharacterSummary,?>>();
    List<TableColumnController<KinshipCharacterSummary,?>> summaryColumns=CharacterSummaryColumnsBuilder.buildBaseCharacterSummaryColumns(KinshipCharacterSummary.class);
    columns.addAll(summaryColumns);
    columns.addAll(getSummarySpecificColumns());
    return columns;
  }

  private static List<TableColumnController<KinshipCharacterSummary,?>> getSummarySpecificColumns()
  {
    List<TableColumnController<KinshipCharacterSummary,?>> ret=new ArrayList<TableColumnController<KinshipCharacterSummary,?>>();
    // Vocation
    {
      CellDataProvider<KinshipCharacterSummary,String> vocationCell=new CellDataProvider<KinshipCharacterSummary,String>()
      {
        @Override
        public String getData(KinshipCharacterSummary summary)
        {
          return summary.getVocation();
        }
      };
      String columnName=Labels.getLabel("kinship.members.table.column.vocation");
      DefaultTableColumnController<KinshipCharacterSummary,String> vocationColumn=new DefaultTableColumnController<KinshipCharacterSummary,String>(KinshipMembersColumnIds.VOCATION.name(),columnName,String.class,vocationCell);
      vocationColumn.setWidthSpecs(80,80,80);
      ret.add(vocationColumn);
    }
    // Last logout date
    {
      CellDataProvider<KinshipCharacterSummary,Long> lastLogoutCell=new CellDataProvider<KinshipCharacterSummary,Long>()
      {
        @Override
        public Long getData(KinshipCharacterSummary summary)
        {
          return summary.getLastLogoutDate();
        }
      };
      String columnName=Labels.getLabel("kinship.members.table.column.lastLogout");
      DefaultTableColumnController<KinshipCharacterSummary,Long> lastLogoutColumn=new DefaultTableColumnController<KinshipCharacterSummary,Long>(KinshipMembersColumnIds.LAST_LOGOUT_DATE.name(),columnName,Long.class,lastLogoutCell);
      ColumnsUtils.configureDateTimeColumn(lastLogoutColumn);
      ret.add(lastLogoutColumn);
    }
    // Area
    {
      CellDataProvider<KinshipCharacterSummary,String> areaCell=new CellDataProvider<KinshipCharacterSummary,String>()
      {
        @Override
        public String getData(KinshipCharacterSummary summary)
        {
          return summary.getArea();
        }
      };
      String columnName=Labels.getLabel("kinship.members.table.column.area");
      DefaultTableColumnController<KinshipCharacterSummary,String> areaColumn=new DefaultTableColumnController<KinshipCharacterSummary,String>(KinshipMembersColumnIds.AREA.name(),columnName,String.class,areaCell);
      areaColumn.setWidthSpecs(80,250,250);
      ret.add(areaColumn);
    }
    return ret;
  }

  private static List<TableColumnController<KinshipMember,?>> getKinshipMemberSpecificColumns()
  {
    List<TableColumnController<KinshipMember,?>> ret=new ArrayList<TableColumnController<KinshipMember,?>>();
    // Join date
    {
      CellDataProvider<KinshipMember,Long> joinDateCell=new CellDataProvider<KinshipMember,Long>()
      {
        @Override
        public Long getData(KinshipMember member)
        {
          return member.getJoinDate();
        }
      };
      String columnName=Labels.getLabel("kinship.members.table.column.joined");
      DefaultTableColumnController<KinshipMember,Long> joinDateColumn=new DefaultTableColumnController<KinshipMember,Long>(KinshipMembersColumnIds.JOIN_DATE.name(),columnName,Long.class,joinDateCell);
      ColumnsUtils.configureDateTimeColumn(joinDateColumn);
      ret.add(joinDateColumn);
    }
    // Rank
    {
      CellDataProvider<KinshipMember,String> rankCell=new CellDataProvider<KinshipMember,String>()
      {
        @Override
        public String getData(KinshipMember member)
        {
          KinshipRank rank=member.getRank();
          CharacterSex sex=member.getSummary().getCharacterSex();
          String text=KinshipRankRenderer.render(rank,sex);
          return text;
        }
      };
      String columnName=Labels.getLabel("kinship.members.table.column.rank");
      DefaultTableColumnController<KinshipMember,String> rankColumn=new DefaultTableColumnController<KinshipMember,String>(KinshipMembersColumnIds.RANK.name(),columnName,String.class,rankCell);
      rankColumn.setWidthSpecs(120,120,120);
      ret.add(rankColumn);
    }
    // Notes
    {
      CellDataProvider<KinshipMember,String> notesCell=new CellDataProvider<KinshipMember,String>()
      {
        @Override
        public String getData(KinshipMember member)
        {
          return member.getNotes();
        }
      };
      String columnName=Labels.getLabel("kinship.members.table.column.notes");
      DefaultTableColumnController<KinshipMember,String> notesColumn=new DefaultTableColumnController<KinshipMember,String>(KinshipMembersColumnIds.NOTES.name(),columnName,String.class,notesCell);
      notesColumn.setWidthSpecs(100,-1,200);
      ret.add(notesColumn);
    }
    return ret;
  }
}
