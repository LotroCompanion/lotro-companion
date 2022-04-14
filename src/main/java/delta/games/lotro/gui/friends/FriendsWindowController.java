package delta.games.lotro.gui.friends;

import java.awt.Dimension;

import javax.swing.JDialog;
import javax.swing.JPanel;

import delta.common.ui.swing.windows.DefaultDisplayDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.account.AccountOnServer;
import delta.games.lotro.character.social.friends.FriendsManager;

/**
 * Controller for a "friends" window.
 * @author DAM
 */
public class FriendsWindowController extends DefaultDisplayDialogController<FriendsManager>
{
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

  /**
   * Get the window identifier.
   * @return A window identifier.
   */
  public static String getIdentifier()
  {
    return "FRIENDS";
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
    frame.setTitle("Friends");
    frame.setMinimumSize(new Dimension(650,350));
    frame.setSize(850,600);
    frame.setResizable(true);
    return frame;
  }

  @Override
  public String getWindowIdentifier()
  {
    return getIdentifier();
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
