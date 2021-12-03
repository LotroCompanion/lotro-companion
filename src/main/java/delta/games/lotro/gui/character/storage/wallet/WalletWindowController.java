package delta.games.lotro.gui.character.storage.wallet;

import java.awt.Dimension;

import javax.swing.JDialog;
import javax.swing.JPanel;

import delta.common.ui.swing.windows.DefaultDisplayDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.account.Account;
import delta.games.lotro.account.AccountsManager;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.storage.wallet.Wallet;
import delta.games.lotro.character.storage.wallet.io.xml.WalletsIO;

/**
 * Controller for a window to display the wallet of a single character.
 * @author DAM
 */
public class WalletWindowController extends DefaultDisplayDialogController<Void>
{
  private static final int MAX_HEIGHT=600;
  // Data
  private CharacterFile _character;
  private Wallet _ownWallet;
  private Wallet _accountWallet;
  // Controllers
  private WalletDisplayPanelController _displayPanel;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param character Character to use.
   */
  public WalletWindowController(WindowController parent, CharacterFile character)
  {
    super(parent,null);
    _character=character;
    initData(character);
  }

  private void initData(CharacterFile toon)
  {
    _ownWallet=WalletsIO.loadCharacterWallet(toon);
    String accountName=toon.getAccountName();
    String serverName=toon.getServerName();
    if ((accountName.length()>0) && (serverName.length()>0))
    {
      AccountsManager accountsMgr=AccountsManager.getInstance();
      Account account=accountsMgr.getAccountByName(accountName);
      _accountWallet=WalletsIO.loadAccountSharedWallet(account,serverName);
    }
  }

  @Override
  protected JPanel buildFormPanel()
  {
    _displayPanel=new WalletDisplayPanelController(this,_ownWallet,_accountWallet);
    return _displayPanel.getPanel();
  }

  @Override
  public void configureWindow()
  {
    super.configureWindow();
    // Title
    String toonName=_character.getName();
    String serverName=_character.getServerName();
    String accountName=_character.getAccountName();
    String title="Wallet for "+toonName+"@"+serverName+" ("+accountName+")";
    setTitle(title);
    // Dimensions
    JDialog dialog=getDialog();
    dialog.setResizable(true);
    pack();
    Dimension size=dialog.getSize();
    if (size.height>MAX_HEIGHT)
    {
      dialog.setSize(size.width+30,MAX_HEIGHT);
    }
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    super.dispose();
    // Data
    _character=null;
    _ownWallet=null;
    _accountWallet=null;
    // Controllers
    if (_displayPanel!=null)
    {
      _displayPanel.dispose();
      _displayPanel=null;
    }
  }
}
