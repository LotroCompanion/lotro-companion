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
import delta.games.lotro.character.storage.bags.BagsManager;
import delta.games.lotro.character.storage.bags.BagsSetup;
import delta.games.lotro.character.storage.bags.SingleBagSetup;
import delta.games.lotro.character.storage.bags.io.BagsIo;
import delta.games.lotro.gui.character.storage.bags.BagWindowController;
import delta.games.lotro.gui.character.storage.vault.VaultWindowController;
import delta.games.lotro.gui.character.storage.wallet.WalletWindowController;

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
  // Controllers
  private WindowController _parent;
  // Data
  private CharacterFile _character;
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
    _panel=buildPanel();
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  public JPanel getPanel()
  {
    return _panel;
  }

  private JPanel buildPanel()
  {
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    int y=0;
    // Bags
    BagsManager bags=BagsIo.load(_character);
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
      ret.add(button,c);
      y++;
    }
    // Wallet
    {
      JButton button=buildButton("Wallet",WALLET);
      GridBagConstraints c=new GridBagConstraints(0,y,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
      ret.add(button,c);
      y++;
    }
    // Own vault
    {
      JButton button=buildButton("Vault",VAULT);
      GridBagConstraints c=new GridBagConstraints(0,y,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
      ret.add(button,c);
      y++;
    }
    // Shared vault
    String accountName=_character.getAccountName();
    if (accountName.length()>0)
    {
      JButton button=buildButton("Shared Vault",SHARED_VAULT);
      GridBagConstraints c=new GridBagConstraints(0,y,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
      ret.add(button,c);
      y++;
    }
    return ret;
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
      WalletWindowController walletCtrl=new WalletWindowController(_parent,_character);
      walletCtrl.show();
    }
    else if ((VAULT.equals(command)) || (SHARED_VAULT.equals(command)))
    {
      boolean shared=(SHARED_VAULT.equals(command));
      VaultWindowController vaultCtrl=new VaultWindowController(_parent,_character,shared);
      vaultCtrl.show();
    } 
    else if (command.startsWith(BAG_SEED))
    {
      int index=NumericTools.parseInt(command.substring(BAG_SEED.length()),0);
      BagsManager bagsMgr=BagsIo.load(_character);
      BagWindowController bagCtrl=new BagWindowController(_parent,bagsMgr,index);
      bagCtrl.show();
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
