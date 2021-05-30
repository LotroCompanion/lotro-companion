package delta.games.lotro.gui.character.storage.account;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.DefaultDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.account.Account;
import delta.games.lotro.account.AccountUtils;
import delta.games.lotro.account.events.AccountEvent;
import delta.games.lotro.account.events.AccountEventType;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.events.CharacterEvent;
import delta.games.lotro.character.events.CharacterEventType;
import delta.games.lotro.character.storage.AccountServerStorage;
import delta.games.lotro.character.storage.CharacterStorage;
import delta.games.lotro.character.storage.StoragesIO;
import delta.games.lotro.character.storage.StoredItem;
import delta.games.lotro.character.storage.vaults.Vault;
import delta.games.lotro.character.storage.vaults.io.VaultsIo;
import delta.games.lotro.character.storage.wallet.Wallet;
import delta.games.lotro.character.storage.wallet.io.xml.WalletsIO;
import delta.games.lotro.gui.character.storage.StorageDisplayPanelController;
import delta.games.lotro.gui.character.storage.StorageFilter;
import delta.games.lotro.gui.character.storage.StorageFilterController;
import delta.games.lotro.gui.character.storage.StorageUtils;
import delta.games.lotro.utils.events.EventsManager;
import delta.games.lotro.utils.events.GenericEventsListener;

/**
 * Controller for an "account/server storage" window.
 * @author DAM
 */
public class AccountStorageDisplayWindowController extends DefaultDialogController implements GenericEventsListener<CharacterEvent>
{
  /**
   * Window identifier.
   */
  public static final String IDENTIFIER="ACCOUNT_STORAGE";

  // Data
  private Account _account;
  private String _serverName;
  private List<CharacterFile> _characters;
  private StorageFilter _filter;
  // Controllers
  private AccountStorageSummaryPanelController _summaryController;
  private StorageDisplayPanelController _panelController;
  private StorageFilterController _filterController;
  // Listeners
  private GenericEventsListener<AccountEvent> _accountEventsListener;

  /**
   * Constructor.
   * @param parent Parent controller.
   * @param account Account to use.
   * @param server Server to use.
   */
  public AccountStorageDisplayWindowController(WindowController parent, Account account, String server)
  {
    super(parent);
    _account=account;
    _serverName=server;
    _characters=AccountUtils.getCharacters(account.getName(),server);
    _filter=new StorageFilter();
    updateContents();
    EventsManager.addListener(CharacterEvent.class,this);
    _accountEventsListener=new GenericEventsListener<AccountEvent>()
    {
      @Override
      public void eventOccurred(AccountEvent event)
      {
        AccountEventType type=event.getType();
        if (type==AccountEventType.STORAGE_UPDATED)
        {
          if (event.getAccount()==_account)
          {
            updateContents();
          }
        }
      }
    };
    EventsManager.addListener(AccountEvent.class,_accountEventsListener);
  }

  @Override
  protected JComponent buildContents()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    // Summary
    _summaryController=new AccountStorageSummaryPanelController();
    // Display
    _panelController=new StorageDisplayPanelController(this,_filter);
    // Table
    JPanel tablePanel=_panelController.getPanel();
    // Filter
    _filterController=new StorageFilterController(_filter,_panelController);
    JPanel filterPanel=_filterController.getPanel();
    TitledBorder filterBorder=GuiFactory.buildTitledBorder("Filter");
    filterPanel.setBorder(filterBorder);
    // Whole panel
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(_summaryController.getPanel(),c);
    c=new GridBagConstraints(0,1,2,1,1,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(filterPanel,c);
    c.gridy++;c.weighty=1;c.fill=GridBagConstraints.BOTH;
    panel.add(tablePanel,c);
    return panel;
  }

  @Override
  protected JDialog build()
  {
    JDialog dialog=super.build();
    dialog.setMinimumSize(new Dimension(700,350));
    dialog.setSize(750,700);
    dialog.setResizable(true);
    return dialog;
  }

  @Override
  public String getWindowIdentifier()
  {
    return IDENTIFIER;
  }

  /**
   * Handle character events.
   * @param event Source event.
   */
  @Override
  public void eventOccurred(CharacterEvent event)
  {
    CharacterEventType type=event.getType();
    if (type==CharacterEventType.CHARACTER_STORAGE_UPDATED)
    {
      CharacterFile toon=event.getToonFile();
      if (_characters.contains(toon))
      {
        updateContents();
      }
    }
  }

  /**
   * Update contents.
   */
  private void updateContents()
  {
    // Title
    String name=_account.getName();
    String title="Storage for account "+name+" @ "+_serverName;
    getDialog().setTitle(title);
    // Update storage
    AccountServerStorage storage=new AccountServerStorage(_account.getName(),_serverName);
    Vault sharedVault=VaultsIo.load(_account,_serverName);
    storage.setSharedVault(sharedVault);
    Wallet sharedWallet=WalletsIO.loadAccountSharedWallet(_account,_serverName);
    storage.setSharedWallet(sharedWallet);
    for(CharacterFile character : _characters)
    {
      CharacterStorage characterStorage=StoragesIO.loadCharacterStorage(character);
      storage.addStorage(character.getName(),characterStorage);
    }
    _summaryController.update(storage);
    List<StoredItem> items=StorageUtils.buildAccountItems(_account,_serverName);
    _panelController.update(items);
    _filterController.update();
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    // Listeners
    EventsManager.removeListener(CharacterEvent.class,this);
    EventsManager.removeListener(AccountEvent.class,_accountEventsListener);
    _accountEventsListener=null;
    // Data
    _account=null;
    _serverName=null;
    _characters=null;
    _filter=null;
    // Controllers
    if (_summaryController!=null)
    {
      _summaryController.dispose();
      _summaryController=null;
    }
    if (_panelController!=null)
    {
      _panelController.dispose();
      _panelController=null;
    }
    if (_filterController!=null)
    {
      _filterController.dispose();
      _filterController=null;
    }
    super.dispose();
  }
}
