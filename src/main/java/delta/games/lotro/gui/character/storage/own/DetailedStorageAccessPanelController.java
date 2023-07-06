package delta.games.lotro.gui.character.storage.own;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.icons.IconsManager;
import delta.common.ui.swing.windows.WindowController;
import delta.common.utils.NumericTools;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.storage.CharacterStorage;
import delta.games.lotro.character.storage.bags.BagsManager;
import delta.games.lotro.character.storage.bags.BagsSetup;
import delta.games.lotro.character.storage.bags.SingleBagSetup;
import delta.games.lotro.character.storage.carryAlls.CarryAllInstance;
import delta.games.lotro.character.storage.vaults.Vault;
import delta.games.lotro.character.storage.wallet.Wallet;
import delta.games.lotro.gui.LotroIconsManager;
import delta.games.lotro.gui.character.storage.bags.BagWindowController;
import delta.games.lotro.gui.character.storage.carryAlls.CarryAllWindowController;
import delta.games.lotro.gui.character.storage.vault.VaultWindowController;
import delta.games.lotro.gui.character.storage.wallet.WalletWindowController;
import delta.games.lotro.lore.items.carryalls.CarryAll;

/**
 * Controller for a panel to provide access to all the detailed
 * storage views.
 * @author DAM
 */
public class DetailedStorageAccessPanelController implements ActionListener
{
  // Constants
  private static final int MAX_CARRY_ALLS_IN_A_ROW=10;
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
  private List<CarryAllInstance> _carryAlls;
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
    _panel.setBorder(GuiFactory.buildTitledBorder("Details")); // I18n
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
    _carryAlls=_storage.getCarryAlls(true);
    _panel.removeAll();
    // Bags
    JPanel bagsPanel=buildBagsPanel();
    bagsPanel.setBorder(GuiFactory.buildTitledBorder("Bags")); // I18n
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    _panel.add(bagsPanel,c);
    // Wallet & vaults
    JPanel storagePanel=buildStoragePanel();
    storagePanel.setBorder(GuiFactory.buildTitledBorder("Wallet/Vaults")); // I18n
    c=new GridBagConstraints(1,0,1,1,0.0,0.0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    _panel.add(storagePanel,c);
    // Carry-alls
    JPanel carryAllsPanel=buildCarryAllsPanel();
    if (carryAllsPanel!=null)
    {
      carryAllsPanel.setBorder(GuiFactory.buildTitledBorder("Carry-alls")); // I18n
      c=new GridBagConstraints(0,1,2,1,1.0,0.0,GridBagConstraints.NORTHWEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
      _panel.add(carryAllsPanel,c);
    }
  }

  private JPanel buildBagsPanel()
  {
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    BagsManager bags=_storage.getBags();
    BagsSetup setup=bags.getBagsSetup();
    int x=0;
    for(int index=1;index<=6;index++)
    {
      SingleBagSetup bagSetup=setup.getBagSetup(index);
      boolean useBag=((bagSetup!=null) && (bagSetup.getSize()>0));
      String command=BAG_SEED+index;
      JButton button=buildButton("Bag #"+index,command,useBag); // I18n
      GridBagConstraints c=new GridBagConstraints(x,0,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
      ret.add(button,c);
      x++;
    }
    return ret;
  }

  private JPanel buildStoragePanel()
  {
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    int x=0;
    // Wallet
    {
      JButton button=buildButton("Wallet",WALLET,true); // I18n
      GridBagConstraints c=new GridBagConstraints(x,0,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
      ret.add(button,c);
      x++;
    }
    // Own vault
    {
      JButton button=buildButton("Vault",VAULT,true); // I18n
      GridBagConstraints c=new GridBagConstraints(x,0,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
      ret.add(button,c);
      x++;
    }
    // Shared vault
    {
      String accountName=_character.getAccountName();
      boolean useSharedVault=(accountName.length()>0);
      JButton button=buildButton("Shared Vault",SHARED_VAULT,useSharedVault); // I18n
      GridBagConstraints c=new GridBagConstraints(x,0,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
      ret.add(button,c);
      x++;
    }
    return ret;
  }

  private JPanel buildCarryAllsPanel()
  {
    int nbCarryAlls=_carryAlls.size();
    if (nbCarryAlls==0)
    {
      return null;
    }
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    int x=0;
    int y=0;
    for(int i=0;i<nbCarryAlls;i++)
    {
      String command=CARRY_ALL_SEED+i;
      CarryAllInstance carryAllInstance=_carryAlls.get(i);
      CarryAll carryAll=carryAllInstance.getReference();
      String name=carryAll.getName();
      JButton button=buildButton(name,command,true);
      GridBagConstraints c=new GridBagConstraints(x,y,1,1,0.0,0.0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
      ret.add(button,c);
      x++;
      if (x==MAX_CARRY_ALLS_IN_A_ROW)
      {
        x=0;
        y++;
      }
    }
    y++;
    GridBagConstraints c=new GridBagConstraints(0,y,MAX_CARRY_ALLS_IN_A_ROW+1,1,1.0,0.0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    ret.add(Box.createHorizontalGlue(),c);
    return ret;
  }

  private JButton buildButton(String label, String command, boolean state)
  {
    JButton button=GuiFactory.buildIconButton();
    Icon icon=getIcon(command);
    if (icon!=null)
    {
      button.setIcon(icon);
      button.setSize(icon.getIconWidth(),icon.getIconHeight());
    }
    button.setEnabled(state);
    button.setToolTipText(label);
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
      walletCtrl.getDialog().setLocationRelativeTo(_parent.getWindow());
      walletCtrl.show();
    }
    else if (VAULT.equals(command))
    {
      Vault vault=_storage.getOwnVault();
      VaultWindowController vaultCtrl=new VaultWindowController(_parent,_character,false,vault);
      vaultCtrl.getDialog().setLocationRelativeTo(_parent.getWindow());
      vaultCtrl.show();
    } 
    else if (SHARED_VAULT.equals(command))
    {
      Vault vault=_storage.getSharedVault();
      VaultWindowController vaultCtrl=new VaultWindowController(_parent,_character,true,vault);
      vaultCtrl.getDialog().setLocationRelativeTo(_parent.getWindow());
      vaultCtrl.show();
    } 
    else if (command.startsWith(BAG_SEED))
    {
      int index=NumericTools.parseInt(command.substring(BAG_SEED.length()),0);
      BagsManager bagsMgr=_storage.getBags();
      BagWindowController bagCtrl=new BagWindowController(_parent,bagsMgr,index);
      bagCtrl.getDialog().setLocationRelativeTo(_parent.getWindow());
      bagCtrl.show();
    }
    else if (command.startsWith(CARRY_ALL_SEED))
    {
      int index=NumericTools.parseInt(command.substring(CARRY_ALL_SEED.length()),0);
      CarryAllInstance carryAllInstance=_carryAlls.get(index);
      CarryAllWindowController carryAllCtrl=new CarryAllWindowController(_parent,carryAllInstance);
      carryAllCtrl.getDialog().setLocationRelativeTo(_parent.getWindow());
      carryAllCtrl.show();
    }
  }

  private ImageIcon getIcon(String command)
  {
    if (WALLET.equals(command)) return getImageIcon("wallet");
    else if (VAULT.equals(command)) return getImageIcon("ownVault");
    else if (SHARED_VAULT.equals(command)) return getImageIcon("sharedVault");
    else if (command.startsWith(BAG_SEED))
    {
      int index=NumericTools.parseInt(command.substring(BAG_SEED.length()),0);
      return getImageIcon("bag"+index);
    }
    else if (command.startsWith(CARRY_ALL_SEED))
    {
      int index=NumericTools.parseInt(command.substring(CARRY_ALL_SEED.length()),0);
      CarryAllInstance carryAllInstance=_carryAlls.get(index);
      CarryAll carryAll=carryAllInstance.getReference();
      return LotroIconsManager.getItemIcon(carryAll.getIcon());
    }
    return null;
  }

  private ImageIcon getImageIcon(String iconName)
  {
    return IconsManager.getIcon("/resources/gui/storage/"+iconName+".png");
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
