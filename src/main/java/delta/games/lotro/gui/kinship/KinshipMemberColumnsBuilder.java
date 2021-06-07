package delta.games.lotro.gui.kinship;

import java.util.ArrayList;
import java.util.List;

import delta.common.ui.swing.tables.CellDataProvider;
import delta.common.ui.swing.tables.DefaultTableColumnController;
import delta.common.ui.swing.tables.GenericTableController.DateRenderer;
import delta.common.ui.swing.tables.ProxiedTableColumnController;
import delta.common.ui.swing.tables.TableColumnController;
import delta.games.lotro.common.CharacterSex;
import delta.games.lotro.gui.toon.CharacterSummaryColumnsBuilder;
import delta.games.lotro.kinship.KinshipCharacterSummary;
import delta.games.lotro.kinship.KinshipMember;
import delta.games.lotro.kinship.KinshipRank;
import delta.games.lotro.utils.Formats;

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
    for(TableColumnController<KinshipCharacterSummary,?> summaryColumn:summaryColumns)
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
    List<TableColumnController<KinshipCharacterSummary,?>> summaryColumns=CharacterSummaryColumnsBuilder.buildBaseCharacterSummaryColumns();
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
        public String getData(KinshipCharacterSummary file)
        {
          Integer vocationId=file.getVocationID();
          // TODO Display
          String vocationName=vocationId!=null?vocationId.toString():null;
          return vocationName;
        }
      };
      DefaultTableColumnController<KinshipCharacterSummary,String> vocationColumn=new DefaultTableColumnController<KinshipCharacterSummary,String>(KinshipMembersColumnIds.VOCATION.name(),"Vocation",String.class,vocationCell);
      vocationColumn.setWidthSpecs(80,80,80);
      ret.add(vocationColumn);
    }
    // Last logout date
    {
      CellDataProvider<KinshipCharacterSummary,Long> lastLogoutCell=new CellDataProvider<KinshipCharacterSummary,Long>()
      {
        public Long getData(KinshipCharacterSummary file)
        {
          return file.getLastLogoutDate();
        }
      };
      DefaultTableColumnController<KinshipCharacterSummary,Long> lastLogoutColumn=new DefaultTableColumnController<KinshipCharacterSummary,Long>(KinshipMembersColumnIds.LAST_LOGOUT_DATE.name(),"Last logout",Long.class,lastLogoutCell);
      lastLogoutColumn.setWidthSpecs(120,120,120);
      lastLogoutColumn.setCellRenderer(new DateRenderer(Formats.DATE_TIME_PATTERN));
      ret.add(lastLogoutColumn);
    }
    // Area
    {
      CellDataProvider<KinshipCharacterSummary,String> areaCell=new CellDataProvider<KinshipCharacterSummary,String>()
      {
        @Override
        public String getData(KinshipCharacterSummary file)
        {
          Integer areaId=file.getAreaID();
          // TODO Display
          String areaName=areaId!=null?areaId.toString():null;
          return areaName;
        }
      };
      DefaultTableColumnController<KinshipCharacterSummary,String> areaColumn=new DefaultTableColumnController<KinshipCharacterSummary,String>(KinshipMembersColumnIds.AREA.name(),"Area",String.class,areaCell);
      areaColumn.setWidthSpecs(80,80,80);
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
        public Long getData(KinshipMember member)
        {
          return member.getJoinDate();
        }
      };
      DefaultTableColumnController<KinshipMember,Long> joinDateColumn=new DefaultTableColumnController<KinshipMember,Long>(KinshipMembersColumnIds.JOIN_DATE.name(),"Joined",Long.class,joinDateCell);
      joinDateColumn.setWidthSpecs(120,120,120);
      joinDateColumn.setCellRenderer(new DateRenderer(Formats.DATE_TIME_PATTERN));
      ret.add(joinDateColumn);
    }
    // Rank
    {
      CellDataProvider<KinshipMember,String> rankCell=new CellDataProvider<KinshipMember,String>()
      {
        public String getData(KinshipMember member)
        {
          KinshipRank rank=member.getRank();
          CharacterSex sex=member.getSummary().getCharacterSex();
          String text=KinshipRankRenderer.render(rank,sex);
          return text;
        }
      };
      DefaultTableColumnController<KinshipMember,String> rankColumn=new DefaultTableColumnController<KinshipMember,String>(KinshipMembersColumnIds.RANK.name(),"Rank",String.class,rankCell);
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
      DefaultTableColumnController<KinshipMember,String> notesColumn=new DefaultTableColumnController<KinshipMember,String>(KinshipMembersColumnIds.NOTES.name(),"Notes",String.class,notesCell);
      notesColumn.setWidthSpecs(100,-1,200);
      ret.add(notesColumn);
    }
    return ret;
  }
}
