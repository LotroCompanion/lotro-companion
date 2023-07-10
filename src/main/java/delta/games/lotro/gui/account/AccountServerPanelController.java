package delta.games.lotro.gui.account;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.WindowController;
import delta.common.ui.swing.windows.WindowsManager;
import delta.common.utils.misc.Preferences;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.account.Account;
import delta.games.lotro.account.AccountOnServer;
import delta.games.lotro.account.AccountUtils;
import delta.games.lotro.account.status.rewardsTrack.RewardsTracksStatusManager;
import delta.games.lotro.account.status.rewardsTrack.io.RewardsTracksStatusIo;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.gui.account.status.rewardsTracks.summary.RewardsTracksStatusSummaryWindowController;
import delta.games.lotro.gui.character.status.currencies.SingleCharacterCurrencyHistoryWindowController;
import delta.games.lotro.gui.character.storage.account.AccountStorageDisplayWindowController;
import delta.games.lotro.gui.character.storage.wardrobe.WardrobeDisplayWindowController;
import delta.games.lotro.gui.friends.FriendsWindowController;
import delta.games.lotro.gui.main.GlobalPreferences;
import delta.games.lotro.gui.toon.ToonsTableController;
import delta.games.lotro.gui.utils.l10n.Labels;

/**
 * Controller for a panel to display account/server data.
 * @author DAM
 */
public class AccountServerPanelController implements ActionListener
{
  private static final String STORAGE_COMMAND="storage";
  private static final String CURRENCIES_COMMAND="currencies";
  private static final String FRIENDS_COMMAND="friends";
  private static final String WARDROBE_COMMAND="wardrobe";
  private static final String REWARDS_TRACKS_COMMAND="rewardsTracks";

  // Data
  private AccountOnServer _accountOnServer;
  // UI
  private JPanel _panel;
  private WindowsManager _windowsManager;
  // Controllers
  private WindowController _parent;
  private ToonsTableController _toonsTable;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param accountOnServer Managed account/server.
   */
  public AccountServerPanelController(WindowController parent, AccountOnServer accountOnServer)
  {
    _parent=parent;
    _accountOnServer=accountOnServer;
    _windowsManager=new WindowsManager();
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
    tablePanel.setBorder(GuiFactory.buildTitledBorder(Labels.getLabel("account.server.characters.tab")));
    panel.add(tablePanel,BorderLayout.CENTER);
    // Command buttons
    JPanel commandsPanel=buildCommandsPanel();
    panel.add(commandsPanel,BorderLayout.SOUTH);
    return panel;
  }

  private JPanel buildTablePanel()
  {
    JPanel ret=GuiFactory.buildPanel(new BorderLayout());
    _toonsTable=buildToonsTable();
    JTable table=_toonsTable.getTable();
    JScrollPane scroll=GuiFactory.buildScrollPane(table);
    ret.add(scroll,BorderLayout.CENTER);
    return ret;
  }

  private ToonsTableController buildToonsTable()
  {
    TypedProperties prefs=GlobalPreferences.getGlobalProperties("AccountServerCharTable");
    ToonsTableController tableController=new ToonsTableController(prefs,null);
    Account account=_accountOnServer.getAccount();
    String serverName=_accountOnServer.getServerName();
    List<CharacterFile> characters=AccountUtils.getCharacters(account.getSummary(),serverName);
    tableController.setToons(characters);
    return tableController;
  }

  private JPanel buildCommandsPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
    // Storage
    JButton storageButton=buildCommandButton("storage",STORAGE_COMMAND);
    panel.add(storageButton);
    // Currencies
    JButton currenciesButton=buildCommandButton("currencies",CURRENCIES_COMMAND);
    panel.add(currenciesButton);
    // Friends
    JButton friendsButton=buildCommandButton("friends",FRIENDS_COMMAND);
    panel.add(friendsButton);
    // Wardrobe
    JButton wardrobeButton=buildCommandButton("wardrobe",WARDROBE_COMMAND);
    panel.add(wardrobeButton);
    // Rewards tracks
    JButton rewardsTracksButton=buildCommandButton("rewardsTracks",REWARDS_TRACKS_COMMAND);
    panel.add(rewardsTracksButton);

    return panel;
  }

  private JButton buildCommandButton(String key, String command)
  {
    JButton b=GuiFactory.buildButton(Labels.getLabel("account.server.buttons."+key));
    b.setActionCommand(command);
    b.addActionListener(this);
    return b;
  }

  /**
   * Handle button actions.
   * @param e Source event.
   */
  @Override
  public void actionPerformed(ActionEvent e)
  {
    String command=e.getActionCommand();
    if (STORAGE_COMMAND.equals(command))
    {
      showStorage();
    }
    else if (CURRENCIES_COMMAND.equals(command))
    {
      showCurrencies();
    }
    else if (FRIENDS_COMMAND.equals(command))
    {
      showFriends();
    }
    else if (WARDROBE_COMMAND.equals(command))
    {
      showWardrobe();
    }
    else if (REWARDS_TRACKS_COMMAND.equals(command))
    {
      showRewardsTracks();
    }
  }

  private void showStorage()
  {
    AccountStorageDisplayWindowController summaryController=(AccountStorageDisplayWindowController)_windowsManager.getWindow(AccountStorageDisplayWindowController.IDENTIFIER);
    if (summaryController==null)
    {
      summaryController=new AccountStorageDisplayWindowController(_parent,_accountOnServer);
      _windowsManager.registerWindow(summaryController);
      summaryController.getWindow().setLocationRelativeTo(_parent.getWindow());
    }
    summaryController.bringToFront();
  }

  private void showCurrencies()
  {
    SingleCharacterCurrencyHistoryWindowController summaryController=(SingleCharacterCurrencyHistoryWindowController)_windowsManager.getWindow(SingleCharacterCurrencyHistoryWindowController.getIdentifier());
    if (summaryController==null)
    {
      summaryController=new SingleCharacterCurrencyHistoryWindowController(_parent,_accountOnServer);
      _windowsManager.registerWindow(summaryController);
      summaryController.getWindow().setLocationRelativeTo(_parent.getWindow());
    }
    summaryController.bringToFront();
  }

  private void showFriends()
  {
    FriendsWindowController friendsWindow=(FriendsWindowController)_windowsManager.getWindow(FriendsWindowController.WINDOW_ID);
    if (friendsWindow==null)
    {
      friendsWindow=new FriendsWindowController(_parent,_accountOnServer);
      _windowsManager.registerWindow(friendsWindow);
      friendsWindow.getWindow().setLocationRelativeTo(_parent.getWindow());
    }
    friendsWindow.bringToFront();
  }

  private void showWardrobe()
  {
    WardrobeDisplayWindowController wardrobeWindow=(WardrobeDisplayWindowController)_windowsManager.getWindow(WardrobeDisplayWindowController.IDENTIFIER);
    if (wardrobeWindow==null)
    {
      wardrobeWindow=new WardrobeDisplayWindowController(_parent,_accountOnServer);
      _windowsManager.registerWindow(wardrobeWindow);
      wardrobeWindow.getWindow().setLocationRelativeTo(_parent.getWindow());
    }
    wardrobeWindow.bringToFront();
  }

  private void showRewardsTracks()
  {
    RewardsTracksStatusSummaryWindowController window=(RewardsTracksStatusSummaryWindowController)_windowsManager.getWindow(RewardsTracksStatusSummaryWindowController.IDENTIFIER);
    if (window==null)
    {
      RewardsTracksStatusManager statusMgr=RewardsTracksStatusIo.load(_accountOnServer);
      window=new RewardsTracksStatusSummaryWindowController(_parent,statusMgr);
      _windowsManager.registerWindow(window);
      window.getWindow().setLocationRelativeTo(_parent.getWindow());
    }
    window.bringToFront();
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // UI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    // Controllers
    if (_windowsManager!=null)
    {
      _windowsManager.disposeAll();
      _windowsManager=null;
    }
    if (_toonsTable!=null)
    {
      _toonsTable.dispose();
      _toonsTable=null;
    }
    // Data
    Preferences preferences=_accountOnServer.getPreferences();
    preferences.saveAllPreferences();
    _accountOnServer=null;
  }
}
