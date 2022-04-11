package delta.games.lotro.gui.friends;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.WindowController;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.character.social.friends.FriendsManager;
import delta.games.lotro.character.social.friends.filters.FriendFilter;
import delta.games.lotro.gui.friends.filter.FriendFilterController;
import delta.games.lotro.gui.main.GlobalPreferences;

/**
 * Controller for a panel to display friends.
 * @author DAM
 */
public class FriendsPanelController
{
  // Data
  private FriendsManager _friendsMgr;
  private FriendFilter _filter;
  // UI
  private JPanel _panel;
  // Controllers
  private WindowController _parent;
  private FriendFilterController _filterController;
  private FriendsTableController _membersTable;
  private FriendsTablePanelController _membersPanel;

  /**
   * Constructor.
   * @param parent Parent window controller.
   * @param friendsMgr Managed friends.
   */
  public FriendsPanelController(WindowController parent, FriendsManager friendsMgr)
  {
    _parent=parent;
    _friendsMgr=friendsMgr;
    _filter=new FriendFilter();
  }

  /**
   * Get the managed panel.
   * @return a panel.
   */
  public JPanel getPanel()
  {
    if (_panel==null)
    {
      _panel=buildPanel();
    }
    return _panel;
  }

  private JPanel buildPanel()
  {
    JPanel panel=GuiFactory.buildBackgroundPanel(new BorderLayout());
    // Characters table
    JPanel tablePanel=buildTablePanel();
    tablePanel.setBorder(GuiFactory.buildTitledBorder("Friends"));
    panel.add(tablePanel,BorderLayout.CENTER);
    // Top panel
    _filterController=new FriendFilterController(_filter,_membersPanel);
    JPanel filterPanel=_filterController.getPanel();
    TitledBorder filterBorder=GuiFactory.buildTitledBorder("Filter");
    filterPanel.setBorder(filterBorder);
    panel.add(filterPanel,BorderLayout.NORTH);
    return panel;
  }

  private JPanel buildTablePanel()
  {
    _membersTable=buildMembersTable();
    _membersPanel=new FriendsTablePanelController(_parent,_membersTable);
    return _membersPanel.getPanel();
  }

  private FriendsTableController buildMembersTable()
  {
    TypedProperties prefs=GlobalPreferences.getGlobalProperties("FriendsTable");
    FriendsTableController tableController=new FriendsTableController(prefs,_filter);
    tableController.setFriends(_friendsMgr.getAll());
    return tableController;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Data
    _friendsMgr=null;
    _filter=null;
    // UI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    // Controllers
    _parent=null;
    if (_filterController!=null)
    {
      _filterController.dispose();
      _filterController=null;
    }
    if (_membersTable!=null)
    {
      _membersTable.dispose();
      _membersTable=null;
    }
    if (_membersPanel!=null)
    {
      _membersPanel.dispose();
      _membersPanel=null;
    }
  }
}
