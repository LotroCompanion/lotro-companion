package delta.games.lotro.gui.character.storage;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.tables.TableColumnsChooserController;
import delta.common.ui.swing.windows.WindowController;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.storage.CharacterStorage;
import delta.games.lotro.character.storage.Chest;
import delta.games.lotro.character.storage.ItemsContainer;
import delta.games.lotro.character.storage.StoredItem;
import delta.games.lotro.character.storage.Vault;
import delta.games.lotro.character.storage.Wallet;
import delta.games.lotro.character.storage.location.BagLocation;
import delta.games.lotro.character.storage.location.StorageLocation;
import delta.games.lotro.character.storage.location.VaultLocation;
import delta.games.lotro.character.storage.location.WalletLocation;
import delta.games.lotro.common.owner.AccountOwner;
import delta.games.lotro.common.owner.AccountServerOwner;
import delta.games.lotro.common.owner.CharacterOwner;
import delta.games.lotro.common.owner.Owner;
import delta.games.lotro.gui.items.FilterUpdateListener;
import delta.games.lotro.gui.main.GlobalPreferences;
import delta.games.lotro.lore.items.CountedItem;

/**
 * Controller for the storage display panel.
 * @author DAM
 */
public class StorageDisplayPanelController implements FilterUpdateListener
{
  // Data
  private CharacterFile _character;
  private List<StoredItem> _items;
  private StorageFilter _filter;
  // GUI
  private JPanel _panel;
  private JLabel _statsLabel;
  // Controllers
  private StoredItemsTableController _tableController;
  private WindowController _parent;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param character Character to show.
   * @param filter FIlter of stored items.
   */
  public StorageDisplayPanelController(WindowController parent, CharacterFile character, StorageFilter filter)
  {
    _parent=parent;
    _character=character;
    _filter=filter;
    _items=new ArrayList<StoredItem>();
    TypedProperties prefs=GlobalPreferences.getGlobalProperties("StorageDisplay");
    _tableController=new StoredItemsTableController(prefs,_items,filter);
    getPanel();
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  public JPanel getPanel()
  {
    if (_panel==null)
    {
      _panel=build();
    }
    return _panel;
  }

  private JPanel build()
  {
    JPanel panel=GuiFactory.buildBackgroundPanel(new BorderLayout());
    TitledBorder border=GuiFactory.buildTitledBorder("Items");
    panel.setBorder(border);

    // Table
    JTable table=_tableController.getTable();
    JScrollPane scroll=GuiFactory.buildScrollPane(table);
    panel.add(scroll,BorderLayout.CENTER);
    // Stats
    JPanel statsPanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
    _statsLabel=GuiFactory.buildLabel("-");
    statsPanel.add(_statsLabel);
    JButton choose=GuiFactory.buildButton("Choose columns...");
    ActionListener al=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        TableColumnsChooserController<StoredItem> chooser=new TableColumnsChooserController<StoredItem>(_parent,_tableController.getTableController());
        chooser.editModal();
      }
    };
    choose.addActionListener(al);
    statsPanel.add(choose);
    panel.add(statsPanel,BorderLayout.NORTH);
    return panel;
  }

  /**
   * Update display.
   * @param characterStorage Storage to show.
   */
  public void update(CharacterStorage characterStorage)
  {
    updateStorage(characterStorage);
    updateStatsLabel();
    _tableController.update();
  }

  /**
   * Update filter.
   */
  public void filterUpdated()
  {
    _tableController.updateFilter();
    updateStatsLabel();
  }

  private void updateStatsLabel()
  {
    int nbFiltered=_tableController.getNbFilteredItems();
    int nbItems=_tableController.getNbItems();
    String label="";
    if (nbFiltered==nbItems)
    {
      label="Item(s): "+nbItems;
    }
    else
    {
      label="Item(s): "+nbFiltered+"/"+nbItems;
    }
    _statsLabel.setText(label);
  }

  private void updateStorage(CharacterStorage characterStorage)
  {
    _items.clear();

    // Build owner
    AccountOwner accountOwner=new AccountOwner("???");
    String server=_character.getServerName();
    AccountServerOwner accountServer=new AccountServerOwner(accountOwner,server);
    String characterName=_character.getName();
    CharacterOwner owner=new CharacterOwner(accountServer,characterName);

    // Own bags
    {
      Vault container=characterStorage.getBags();
      List<StoredItem> storedItems=getAllItems(owner,container,true);
      _items.addAll(storedItems);
    }
    // Own vault
    {
      Vault container=characterStorage.getOwnVault();
      List<StoredItem> storedItems=getAllItems(owner,container,false);
      _items.addAll(storedItems);
    }
    // Own wallet
    {
      Wallet ownWallet=characterStorage.getWallet();
      WalletLocation location=new WalletLocation();
      List<StoredItem> storedItems=getAllItems(owner,location,ownWallet);
      _items.addAll(storedItems);
    }
    _filter.getConfiguration().setItems(_items);
  }

  private List<StoredItem> getAllItems(Owner owner, Vault container, boolean isBag)
  {
    List<StoredItem> items=new ArrayList<StoredItem>();
    int chests=container.getChestCount();
    for(int i=0;i<chests;i++)
    {
      Chest chest=container.getChest(i);
      if (chest!=null)
      {
        String chestName=chest.getName();
        StorageLocation location=isBag?new BagLocation(chestName):new VaultLocation(chestName);
        List<CountedItem> chestItems=chest.getAllItemsByName();
        for(CountedItem chestItem : chestItems)
        {
          StoredItem storedItem=new StoredItem(chestItem.getProxy(),chestItem.getQuantity());
          storedItem.setOwner(owner);
          storedItem.setLocation(location);
          items.add(storedItem);
        }
      }
    }
    return items;
  }

  private List<StoredItem> getAllItems(Owner owner, StorageLocation location, ItemsContainer container)
  {
    List<StoredItem> items=new ArrayList<StoredItem>();
    for(CountedItem countedItem : container.getAllItemsByName())
    {
      StoredItem storedItem=new StoredItem(countedItem.getProxy(),countedItem.getQuantity());
      storedItem.setOwner(owner);
      storedItem.setLocation(location);
      items.add(storedItem);
    }
    return items;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Data
    _character=null;
    _items=null;
    // GUI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    _statsLabel=null;
    // Controllers
    _tableController=null;
    _parent=null;
  }
}
