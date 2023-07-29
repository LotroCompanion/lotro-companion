package delta.games.lotro.gui.account;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.DefaultWindowController;
import delta.games.lotro.account.Account;
import delta.games.lotro.account.AccountOnServer;
import delta.games.lotro.account.AccountSummary;
import delta.games.lotro.account.AccountUtils;
import delta.games.lotro.gui.utils.l10n.Labels;

/**
 * Controller for an "account" window.
 * @author DAM
 */
public class AccountWindowController extends DefaultWindowController
{
  private AccountSummaryPanelController _summaryController;
  private Account _account;
  private Map<String,AccountServerPanelController> _serverPanels;

  /**
   * Constructor.
   * @param account Managed account.
   */
  public AccountWindowController(Account account)
  {
    _account=account;
    _serverPanels=new HashMap<String,AccountServerPanelController>();
    _summaryController=new AccountSummaryPanelController(this,_account);
  }

  /**
   * Get the window identifier for a given account.
   * @param accountSummary Account summary.
   * @return A window identifier.
   */
  public static String getIdentifier(AccountSummary accountSummary)
  {
    String accountID=accountSummary.getAccountID().getExternalID();
    String id="ACCOUNT#"+accountID;
    return id;
  }

  @Override
  protected JComponent buildContents()
  {
    // Whole panel
    JPanel panel=GuiFactory.buildPanel(new BorderLayout());
    // Summary panel
    JPanel summaryPanel=_summaryController.getPanel();
    panel.add(summaryPanel,BorderLayout.NORTH);
    // Servers tab
    JTabbedPane tabbedPane=GuiFactory.buildTabbedPane();
    List<String> servers=AccountUtils.getServers(_account.getSummary());
    for(String server : servers)
    {
      JPanel serverPanel=buildServerPanel(server);
      tabbedPane.add(server,serverPanel);
    }
    panel.add(tabbedPane,BorderLayout.CENTER);
    return panel;
  }

  private JPanel buildServerPanel(String serverName)
  {
    AccountOnServer accountOnServer=_account.getServer(serverName);
    AccountServerPanelController controller=new AccountServerPanelController(this,accountOnServer);
    _serverPanels.put(serverName,controller);
    JPanel panel=controller.getPanel();
    return panel;
  }

  @Override
  protected JFrame build()
  {
    JFrame frame=super.build();
    // Title
    String name=_account.getDisplayName();
    String title=Labels.getLabel("account.window.title",new Object[] {name});
    frame.setTitle(title);
    frame.setMinimumSize(new Dimension(400,200));
    frame.setSize(800,350);
    frame.setResizable(true);
    return frame;
  }

  @Override
  public String getWindowIdentifier()
  {
    return getIdentifier(_account.getSummary());
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    super.dispose();
    if (_summaryController!=null)
    {
      _summaryController.dispose();
      _summaryController=null;
    }
    if (_serverPanels!=null)
    {
      for(AccountServerPanelController serverPanel : _serverPanels.values())
      {
        serverPanel.dispose();
      }
      _serverPanels=null;
    }
    _account=null;
  }
}
