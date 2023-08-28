package delta.games.lotro.gui.friends;

import java.util.ArrayList;
import java.util.List;

import delta.common.ui.swing.tables.CellDataProvider;
import delta.common.ui.swing.tables.DefaultTableColumnController;
import delta.common.ui.swing.tables.TableColumnController;
import delta.games.lotro.account.status.friends.Friend;
import delta.games.lotro.gui.toon.CharacterSummaryColumnsBuilder;
import delta.games.lotro.gui.utils.l10n.StatColumnsUtils;

/**
 * Build for columns of the friends tables.
 * @author DAM
 */
public class FriendColumnsBuilder
{
  /**
   * Build the columns to show a <code>Friend</code>.
   * @return a list of columns.
   */
  public static List<TableColumnController<Friend,?>> build()
  {
    List<TableColumnController<Friend,?>> ret=new ArrayList<TableColumnController<Friend,?>>();
    // Character reference columns
    for(TableColumnController<Friend,?> column : CharacterSummaryColumnsBuilder.buildCharacterReferenceColumns(Friend.class))
    {
      ret.add(column);
    }
    // Vocation
    {
      CellDataProvider<Friend,String> vocationCell=new CellDataProvider<Friend,String>()
      {
        @Override
        public String getData(Friend friend)
        {
          return friend.getVocation();
        }
      };
      DefaultTableColumnController<Friend,String> vocationColumn=new DefaultTableColumnController<Friend,String>(FriendsColumnIds.VOCATION.name(),"Vocation",String.class,vocationCell); // I18n
      vocationColumn.setWidthSpecs(80,80,80);
      ret.add(vocationColumn);
    }
    // Last logout date
    {
      CellDataProvider<Friend,Long> lastLogoutCell=new CellDataProvider<Friend,Long>()
      {
        public Long getData(Friend friend)
        {
          return friend.getLastLogoutDate();
        }
      };
      DefaultTableColumnController<Friend,Long> lastLogoutColumn=new DefaultTableColumnController<Friend,Long>(FriendsColumnIds.LAST_LOGOUT_DATE.name(),"Last logout",Long.class,lastLogoutCell); // I18n
      StatColumnsUtils.configureDateTimeColumn(lastLogoutColumn);
      ret.add(lastLogoutColumn);
    }
    // Area
    {
      CellDataProvider<Friend,String> areaCell=new CellDataProvider<Friend,String>()
      {
        @Override
        public String getData(Friend friend)
        {
          return friend.getArea();
        }
      };
      DefaultTableColumnController<Friend,String> areaColumn=new DefaultTableColumnController<Friend,String>(FriendsColumnIds.AREA.name(),"Area",String.class,areaCell); // I18n
      areaColumn.setWidthSpecs(80,250,250);
      ret.add(areaColumn);
    }
    // Notes
    {
      CellDataProvider<Friend,String> notesCell=new CellDataProvider<Friend,String>()
      {
        @Override
        public String getData(Friend friend)
        {
          return friend.getNote();
        }
      };
      DefaultTableColumnController<Friend,String> notesColumn=new DefaultTableColumnController<Friend,String>(FriendsColumnIds.NOTES.name(),"Notes",String.class,notesCell); // I18n
      notesColumn.setWidthSpecs(100,-1,200);
      ret.add(notesColumn);
    }
    return ret;
  }
}
