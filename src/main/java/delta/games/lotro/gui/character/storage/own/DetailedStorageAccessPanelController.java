package delta.games.lotro.gui.character.storage.own;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.WindowController;
import delta.common.utils.NumericTools;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.storage.CharacterStorage;
import delta.games.lotro.character.storage.bags.BagsManager;
import delta.games.lotro.character.storage.bags.BagsSetup;
import delta.games.lotro.character.storage.bags.SingleBagSetup;
import delta.games.lotro.character.storage.vaults.Vault;
import delta.games.lotro.character.storage.wallet.Wallet;
import delta.games.lotro.gui.character.storage.bags.BagWindowController;
import delta.games.lotro.gui.character.storage.carryAlls.CarryAllWindowController;
import delta.games.lotro.gui.character.storage.vault.VaultWindowController;
import delta.games.lotro.gui.character.storage.wallet.WalletWindowController;
import delta.games.lotro.lore.items.carryalls.CarryAllInstance;

/**
 * Controller for a panel to provide access to all the detailed
 * storage views.
 * @author DAM
 */
public class DetailedStorageAccessPanelController implements ActionListener
{
  // Commands
  private static final String BAG_SEED="BAG_";
  private static final String WALLET="WALLET";
  private static final String VAULT="VAULT";
  private static final String SHARED_VAULT="SHARED_VAULT";
  private static final String CARRY_ALL_SEED="CARRY_ALL_";
  // Controllers
  private WindowController _parent;
  // Data
  private CharacterFile _character;
  private CharacterStorage _storage;
  // UI
  private JPanel _panel;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param character Character to use.
   */
  public DetailedStorageAccessPanelController(WindowController parent, CharacterFile character)
  {
    _parent=parent;
    _character=character;
    _storage=null;
    _panel=GuiFactory.buildPanel(new GridBagLayout());
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  public JPanel getPanel()
  {
    return _panel;
  }

  /**
   * Update the contents of this panel using the given storage.
   * @param characterStorage Character storage.
   */
  public void update(CharacterStorage characterStorage)
  {
    _storage=characterStorage;
    _panel.removeAll();
    int y=0;
    // Bags
    BagsManager bags=characterStorage.getBags();
    BagsSetup setup=bags.getBagsSetup();
    List<Integer> indexes=setup.getBagIndexes();
    for(Integer index : indexes)
    {
      SingleBagSetup bagSetup=setup.getBagSetup(index.intValue());
      int size=bagSetup.getSize();
      if (size==0)
      {
        continue;
      }
      String command=BAG_SEED+index;
      JButton button=buildButton("Bag #"+index,command);
      GridBagConstraints c=new GridBagConstraints(0,y,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
      _panel.add(button,c);
      y++;
    }
    // Wallet
    {
      JButton button=buildButton("Wallet",WALLET);
      GridBagConstraints c=new GridBagConstraints(0,y,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
      _panel.add(button,c);
      y++;
    }
    // Own vault
    {
      JButton button=buildButton("Vault",VAULT);
      GridBagConstraints c=new GridBagConstraints(0,y,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
      _panel.add(button,c);
      y++;
    }
    // Shared vault
    String accountName=_character.getAccountName();
    if (accountName.length()>0)
    {
      JButton button=buildButton("Shared Vault",SHARED_VAULT);
      GridBagConstraints c=new GridBagConstraints(0,y,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
      _panel.add(button,c);
      y++;
    }
    // Carry-alls
    List<CarryAllInstance> carryAlls=characterStorage.getCarryAlls(true);
    int nbCarryAlls=carryAlls.size();
    for(int i=0;i<nbCarryAlls;i++)
    {
      String command=CARRY_ALL_SEED+i;
      JButton button=buildButton("Carry All #"+(i+1),command);
      GridBagConstraints c=new GridBagConstraints(0,y,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
      _panel.add(button,c);
      y++;
    }
  }

  private JButton buildButton(String label, String command)
  {
    JButton button=GuiFactory.buildButton(label);
    button.setActionCommand(command);
    button.addActionListener(this);
    return button;
  }

  @Override
  public void actionPerformed(ActionEvent event)
  {
    String command=event.getActionCommand();
    if (WALLET.equals(command))
    {
      Wallet ownWallet=_storage.getWallet();
      Wallet sharedWallet=_storage.getSharedWallet();
      WalletWindowController walletCtrl=new WalletWindowController(_parent,_character,ownWallet,sharedWallet);
      walletCtrl.show();
    }
    else if (VAULT.equals(command))
    {
      Vault vault=_storage.getOwnVault();
      VaultWindowController vaultCtrl=new VaultWindowController(_parent,_character,false,vault);
      vaultCtrl.show();
    } 
    else if (SHARED_VAULT.equals(command))
    {
      Vault vault=_storage.getSharedVault();
      VaultWindowController vaultCtrl=new VaultWindowController(_parent,_character,true,vault);
      vaultCtrl.show();
    } 
    else if (command.startsWith(BAG_SEED))
    {
      int index=NumericTools.parseInt(command.substring(BAG_SEED.length()),0);
      BagsManager bagsMgr=_storage.getBags();
      BagWindowController bagCtrl=new BagWindowController(_parent,bagsMgr,index);
      bagCtrl.show();
    }
    else if (command.startsWith(CARRY_ALL_SEED))
    {
      int index=NumericTools.parseInt(command.substring(CARRY_ALL_SEED.length()),0);
      CarryAllInstance carryAll=_storage.getCarryAlls(true).get(index);
      CarryAllWindowController carryAllCtrl=new CarryAllWindowController(_parent,carryAll);
      carryAllCtrl.show();
    }
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Controllers
    _parent=null;
    // Data
    _character=null;
    // UI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
  }
}
