package delta.games.lotro.gui.character.storage.wallet;

import java.awt.Dimension;

import javax.swing.JDialog;
import javax.swing.JPanel;

import delta.common.ui.swing.windows.DefaultDisplayDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.storage.wallet.Wallet;

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
  private WalletPanelController _fullPanel;
  private WalletDisplayPanelController _displayPanel;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param character Character to use.
   * @param ownWallet Own wallet.
   * @param sharedWallet Shared wallet.
   */
  public WalletWindowController(WindowController parent, CharacterFile character, Wallet ownWallet, Wallet sharedWallet)
  {
    super(parent,null);
    _character=character;
    _ownWallet=ownWallet;
    _accountWallet=sharedWallet;
  }

  @Override
  protected JPanel buildFormPanel()
  {
    _displayPanel=new WalletDisplayPanelController(this,_ownWallet,_accountWallet);
    _fullPanel=new WalletPanelController(_displayPanel);
    return _fullPanel.getPanel();
  }

  @Override
  public void configureWindow()
  {
    super.configureWindow();
    // Title
    String toonName=_character.getName();
    String serverName=_character.getServerName();
    String accountName=_character.getAccountName();
    String title="Wallet for "+toonName+"@"+serverName+" ("+accountName+")"; // I18n
    setTitle(title);
    // Dimensions
    JDialog dialog=getDialog();
    dialog.setMinimumSize(new Dimension(450,300));
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
    if (_fullPanel!=null)
    {
      _fullPanel.dispose();
      _fullPanel=null;
    }
  }
}
