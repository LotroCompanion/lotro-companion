package delta.games.lotro.gui.friends;

import java.awt.Dimension;

import javax.swing.JDialog;
import javax.swing.JPanel;

import delta.common.ui.swing.windows.DefaultDisplayDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.account.AccountOnServer;
import delta.games.lotro.account.status.friends.FriendsManager;

/**
 * Controller for a "friends" window.
 * @author DAM
 */
public class FriendsWindowController extends DefaultDisplayDialogController<FriendsManager>
{
  /**
   * Identifier for this window.
   */
  public static final String WINDOW_ID="FRIENDS";

  private FriendsPanelController _detailsPanel;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param accountOnServer Managed account/server.
   */
  public FriendsWindowController(WindowController parent, AccountOnServer accountOnServer)
  {
    super(parent,null);
    _detailsPanel=new FriendsPanelController(this,accountOnServer);
  }

  @Override
  protected JPanel buildFormPanel()
  {
    return _detailsPanel.getPanel();
  }

  @Override
  protected JDialog build()
  {
    JDialog frame=super.build();
    // Title
    frame.setTitle("Friends"); // I18n
    frame.setMinimumSize(new Dimension(650,350));
    frame.setSize(850,600);
    frame.setResizable(true);
    return frame;
  }

  @Override
  public String getWindowIdentifier()
  {
    return WINDOW_ID;
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    super.dispose();
    if (_detailsPanel!=null)
    {
      _detailsPanel.dispose();
      _detailsPanel=null;
    }
  }
}
