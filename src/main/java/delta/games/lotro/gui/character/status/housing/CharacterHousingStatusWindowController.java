package delta.games.lotro.gui.character.status.housing;

import javax.swing.JFrame;
import javax.swing.JPanel;

import delta.common.ui.swing.windows.DefaultWindowController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.account.Account;
import delta.games.lotro.account.AccountOnServer;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.status.housing.AccountHousingData;
import delta.games.lotro.character.status.housing.io.HousingStatusIO;
import delta.games.lotro.character.utils.CharacterUtils;

/**
 * Controller for a window to show a house and its contents.
 * @author DAM
 */
public class CharacterHousingStatusWindowController extends DefaultWindowController
{
  private static final String IDENTIFIER="HOUSE-STATUS";

  // Data
  private CharacterFile _character;
  // Controllers
  private CharacterHousingStatusPanelController _status;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param character Character to use.
   */
  public CharacterHousingStatusWindowController(WindowController parent, CharacterFile character)
  {
    super(parent);
    _character=character;
  }

  @Override
  protected JFrame build()
  {
    JFrame frame=super.build();
    frame.setTitle("Housing Status");
    frame.pack();
    frame.setResizable(false);
    return frame;
  }

  @Override
  public void configureWindow()
  {
    automaticLocationSetup();
  }

  @Override
  public String getWindowIdentifier()
  {
    return IDENTIFIER;
  }

  @Override
  protected JPanel buildContents()
  {
    Account account=CharacterUtils.getAccount(_character);
    String server=_character.getServerName();
    AccountOnServer accountOnServer=account.getServer(server);
    AccountHousingData data=HousingStatusIO.loadAccountHousingData(accountOnServer);
    if (data==null)
    {
      data=new AccountHousingData();
    }
    _status=new CharacterHousingStatusPanelController(this,_character,data);
    return _status.getPanel();
  }

  @Override
  public void dispose()
  {
    saveBoundsPreferences();
    super.dispose();
    // Data
    _character=null;
    // Controllers
    if (_status!=null)
    {
      _status.dispose();
      _status=null;
    }
  }
}
