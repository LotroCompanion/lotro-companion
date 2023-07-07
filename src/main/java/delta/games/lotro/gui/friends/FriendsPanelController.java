package delta.games.lotro.gui.friends;

import java.awt.BorderLayout;
import java.io.File;

import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.WindowController;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.account.AccountOnServer;
import delta.games.lotro.account.events.AccountEvent;
import delta.games.lotro.account.events.AccountEventProperties;
import delta.games.lotro.account.events.AccountEventType;
import delta.games.lotro.account.status.friends.FriendsManager;
import delta.games.lotro.account.status.friends.filters.FriendFilter;
import delta.games.lotro.account.status.friends.io.xml.FriendsIO;
import delta.games.lotro.gui.friends.filter.FriendFilterController;
import delta.games.lotro.gui.main.GlobalPreferences;
import delta.games.lotro.utils.events.EventsManager;
import delta.games.lotro.utils.events.GenericEventsListener;

/**
 * Controller for a panel to display friends.
 * @author DAM
 */
public class FriendsPanelController implements GenericEventsListener<AccountEvent>
{
  // Data
  private AccountOnServer _accountOnServer;
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
   * @param accountOnServer Managed account/server.
   */
  public FriendsPanelController(WindowController parent, AccountOnServer accountOnServer)
  {
    _parent=parent;
    _accountOnServer=accountOnServer;
    _filter=new FriendFilter();
    EventsManager.addListener(AccountEvent.class,this);
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
    tablePanel.setBorder(GuiFactory.buildTitledBorder("Friends")); // I18n
    panel.add(tablePanel,BorderLayout.CENTER);
    // Top panel
    _filterController=new FriendFilterController(_filter,_membersPanel);
    JPanel filterPanel=_filterController.getPanel();
    TitledBorder filterBorder=GuiFactory.buildTitledBorder("Filter"); // I18n
    filterPanel.setBorder(filterBorder);
    panel.add(filterPanel,BorderLayout.NORTH);
    return panel;
  }

  private JPanel buildTablePanel()
  {
    _membersTable=buildFriendsTable();
    updateFriends();
    _membersPanel=new FriendsTablePanelController(_parent,_membersTable);
    return _membersPanel.getPanel();
  }

  private FriendsTableController buildFriendsTable()
  {
    TypedProperties prefs=GlobalPreferences.getGlobalProperties("FriendsTable");
    FriendsTableController tableController=new FriendsTableController(prefs,_filter);
    return tableController;
  }

  /**
   * Handle friends events.
   * @param event Source event.
   */
  @Override
  public void eventOccurred(AccountEvent event)
  {
    AccountEventType type=event.getType();
    if (type==AccountEventType.FRIENDS_UPDATED)
    {
      String serverName=event.getProperties().getStringProperty(AccountEventProperties.SERVER_NAME,null);
      if (_accountOnServer.getServerName().equals(serverName))
      {
        updateFriends();
      }
    }
  }

  private void updateFriends()
  {
    File rootDir=_accountOnServer.getRootDir();
    File friendsFile=FriendsIO.getFriendsFile(rootDir);
    FriendsManager friendsMgr=FriendsIO.loadFriends(friendsFile);
    if (friendsMgr==null)
    {
      friendsMgr=new FriendsManager();
    }
    _membersTable.setFriends(friendsMgr.getAll());
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Listeners
    EventsManager.removeListener(AccountEvent.class,this);
    // Data
    _accountOnServer=null;
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
