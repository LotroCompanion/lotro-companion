package delta.games.lotro.gui.character.storage.account;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.DefaultDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.account.Account;
import delta.games.lotro.account.AccountOnServer;
import delta.games.lotro.account.AccountUtils;
import delta.games.lotro.account.events.AccountEvent;
import delta.games.lotro.account.events.AccountEventType;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.events.CharacterEvent;
import delta.games.lotro.character.events.CharacterEventType;
import delta.games.lotro.character.storage.AccountServerStorage;
import delta.games.lotro.character.storage.CharacterStorage;
import delta.games.lotro.character.storage.StorageUtils;
import delta.games.lotro.character.storage.StoragesIO;
import delta.games.lotro.character.storage.StoredItem;
import delta.games.lotro.character.storage.vaults.Vault;
import delta.games.lotro.character.storage.vaults.io.VaultsIo;
import delta.games.lotro.character.storage.wallet.Wallet;
import delta.games.lotro.character.storage.wallet.io.xml.WalletsIO;
import delta.games.lotro.gui.character.storage.StorageDisplayPanelController;
import delta.games.lotro.gui.character.storage.StorageFilter;
import delta.games.lotro.gui.character.storage.StorageFilterController;
import delta.games.lotro.gui.character.storage.own.StorageButtonsPanelController;
import delta.games.lotro.gui.lore.items.FilterUpdateListener;
import delta.games.lotro.utils.events.EventsManager;
import delta.games.lotro.utils.events.GenericEventsListener;

/**
 * Controller for an "account/server storage" window.
 * @author DAM
 */
public class AccountStorageDisplayWindowController extends DefaultDialogController implements GenericEventsListener<CharacterEvent>,FilterUpdateListener
{
  /**
   * Window identifier.
   */
  public static final String IDENTIFIER="ACCOUNT_STORAGE";

  // Data
  private AccountOnServer _accountOnServer;
  private List<CharacterFile> _characters;
  private StorageFilter _filter;
  private List<StoredItem> _items;
  // Controllers
  private AccountStorageSummaryPanelController _summaryController;
  private StorageDisplayPanelController _panelController;
  private StorageFilterController _filterController;
  private StorageButtonsPanelController _buttonsController;
  // Listeners
  private GenericEventsListener<AccountEvent> _accountEventsListener;

  /**
   * Constructor.
   * @param parent Parent controller.
   * @param accountOnServer Account/server to use.
   */
  public AccountStorageDisplayWindowController(WindowController parent, AccountOnServer accountOnServer)
  {
    super(parent);
    _accountOnServer=accountOnServer;
    _characters=AccountUtils.getCharacters(accountOnServer);
    _filter=new StorageFilter();
    _items=new ArrayList<StoredItem>();
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
          if (event.getAccount()==_accountOnServer.getAccount())
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
    // Buttons
    _buttonsController=new StorageButtonsPanelController(this,_filter,_items);
    // Filter
    _filterController=new StorageFilterController(_filter,this);
    JPanel filterPanel=_filterController.getPanel();
    TitledBorder filterBorder=GuiFactory.buildTitledBorder("Filter"); // I18n
    filterPanel.setBorder(filterBorder);
    // Whole panel
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(_summaryController.getPanel(),c);
    // Buttons panel
    JPanel buttonsPanel=_buttonsController.getPanel();
    // - filter+buttons panel
    JPanel filterAndButtonsPanel=GuiFactory.buildPanel(new GridBagLayout());
    {
      c=new GridBagConstraints(0,0,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
      filterAndButtonsPanel.add(filterPanel,c);
      c=new GridBagConstraints(1,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,5,0,5),0,0);
      filterAndButtonsPanel.add(buttonsPanel,c);
    }
    c=new GridBagConstraints(0,1,2,1,1,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(filterAndButtonsPanel,c);
    c=new GridBagConstraints(0,2,2,1,1,1,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
    panel.add(tablePanel,c);
    return panel;
  }

  @Override
  protected JDialog build()
  {
    JDialog dialog=super.build();
    dialog.setMinimumSize(new Dimension(850,350));
    dialog.setSize(850,700);
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

  @Override
  public void filterUpdated()
  {
    _panelController.filterUpdated();
    _buttonsController.filterUpdated();
  }

  /**
   * Update contents.
   */
  private void updateContents()
  {
    // Title
    Account account=_accountOnServer.getAccount();
    String accountName=account.getAccountName();
    String serverName=_accountOnServer.getServerName();
    String title="Storage for account "+accountName+" @ "+serverName; // I18n
    getDialog().setTitle(title);
    // Update storage
    AccountServerStorage storage=new AccountServerStorage(accountName,serverName);
    Vault sharedVault=VaultsIo.load(account,serverName);
    storage.setSharedVault(sharedVault);
    Wallet sharedWallet=WalletsIO.loadAccountSharedWallet(account,serverName);
    storage.setSharedWallet(sharedWallet);
    for(CharacterFile character : _characters)
    {
      CharacterStorage characterStorage=StoragesIO.loadCharacterStorage(character);
      storage.addStorage(character.getName(),characterStorage);
    }
    _summaryController.update(storage);
    AccountOnServer accountOnServer=account.getServer(serverName);
    _items=StorageUtils.buildAccountItems(accountOnServer);
    _panelController.update(_items);
    _buttonsController.update(_items);
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
    _accountOnServer=null;
    _characters=null;
    _filter=null;
    _items=null;
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
    if (_buttonsController!=null)
    {
      _buttonsController.dispose();
      _buttonsController=null;
    }
    super.dispose();
  }
}
