package delta.games.lotro.gui.account;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.account.Account;
import delta.games.lotro.account.AccountSummary;
import delta.games.lotro.account.events.AccountEvent;
import delta.games.lotro.account.events.AccountEventType;
import delta.games.lotro.gui.utils.l10n.Labels;
import delta.games.lotro.utils.events.EventsManager;
import delta.games.lotro.utils.events.GenericEventsListener;

/**
 * Controller for an account summary panel.
 * @author DAM
 */
public class AccountSummaryPanelController implements GenericEventsListener<AccountEvent>
{
  // Data
  private Account _account;
  private AccountSummary _summary;
  // Controllers
  private WindowController _parent;
  // UI
  private JPanel _panel;
  private JLabel _accountNameLabel;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param account Account to display.
   */
  public AccountSummaryPanelController(WindowController parent, Account account)
  {
    _parent=parent;
    _account=account;
    _summary=account.getSummary();
    EventsManager.addListener(AccountEvent.class,this);
  }

  /**
   * Get the managed panel.
   * @return a panel.
   */
  public JPanel getPanel()
  {
    if (_panel==null)
    {
      _panel=buildPanel();
    }
    return _panel;
  }

  private JPanel buildPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(2,2,2,2),0,0);

    // Name
    _accountNameLabel=GuiFactory.buildLabel("",28.0f);
    panel.add(_accountNameLabel,c);
    // Edit...
    String editLabel=Labels.getLabel("account.summary.buttons.edit");
    JButton edit=GuiFactory.buildButton(editLabel);
    ActionListener al=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        editSummary();
      }
    };
    edit.addActionListener(al);
    c.gridx=1;
    c.weightx=0.0;c.fill=GridBagConstraints.NONE;
    panel.add(edit,c);
    update();
    return panel;
  }

  private void editSummary()
  {
    AccountSummaryDialogController dialog=new AccountSummaryDialogController(_parent,_summary);
    AccountSummary summary=dialog.editModal();
    if (summary!=null)
    {
      _account.saveSummary(_summary);
      AccountEvent event=new AccountEvent(AccountEventType.ACCOUNT_SUMMARY_UPDATED,_account);
      EventsManager.invokeEvent(event);
    }
  }

  /**
   * Handle account events.
   * @param event Source event.
   */
  @Override
  public void eventOccurred(AccountEvent event)
  {
    AccountEventType type=event.getType();
    if (type==AccountEventType.ACCOUNT_SUMMARY_UPDATED)
    {
      Account account=event.getAccount();
      if (account==_account)
      {
        update();
      }
    }
  }

  /**
   * Update contents.
   */
  public void update()
  {
    if (_summary!=null)
    {
      // Account name
      String name=_summary.getName();
      _accountNameLabel.setText(name);
    }
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Listeners
    EventsManager.removeListener(AccountEvent.class,this);
    // Controllers
    _parent=null;
    // UI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    // Data
    _account=null;
    _summary=null;
  }
}
