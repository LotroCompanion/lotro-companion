package delta.games.lotro.gui.character.storage.vault;

import java.awt.Dimension;

import javax.swing.JDialog;
import javax.swing.JPanel;

import delta.common.ui.swing.windows.DefaultDisplayDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.account.Account;
import delta.games.lotro.account.AccountsManager;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.storage.vaults.Vault;
import delta.games.lotro.character.storage.vaults.io.VaultsIo;

/**
 * Controller for a window to display the (shared?) vault of a single character.
 * @author DAM
 */
public class VaultWindowController extends DefaultDisplayDialogController<Void>
{
  private static final int MAX_HEIGHT=600;
  // Data
  private CharacterFile _character;
  private boolean _shared;
  private Vault _vault;
  // Controllers
  private VaultDisplayPanelController _displayPanel;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param character Character to use.
   * @param shared <code>true</code> to use the shared vault, use the own vault otherwise.
   */
  public VaultWindowController(WindowController parent, CharacterFile character, boolean shared)
  {
    super(parent,null);
    _character=character;
    _shared=shared;
    initData();
  }

  private void initData()
  {
    if (_shared)
    {
      String accountName=_character.getAccountName();
      String serverName=_character.getServerName();
      if ((accountName.length()>0) && (serverName.length()>0))
      {
        AccountsManager accountsMgr=AccountsManager.getInstance();
        Account account=accountsMgr.getAccountByName(accountName);
        if (account!=null)
        {
          _vault=VaultsIo.load(account,serverName);
        }
      }
    }
    else
    {
      _vault=VaultsIo.load(_character);
    }
  }

  @Override
  protected JPanel buildFormPanel()
  {
    _displayPanel=new VaultDisplayPanelController(this,_vault);
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
    String seed=(_shared?"Shared vault":"Vault");
    String title=seed+" for "+toonName+"@"+serverName+" ("+accountName+")";
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
    _vault=null;
    // Controllers
    if (_displayPanel!=null)
    {
      _displayPanel.dispose();
      _displayPanel=null;
    }
  }
}
