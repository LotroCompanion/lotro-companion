package delta.games.lotro.gui.account;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.toolbar.ToolbarController;
import delta.common.ui.swing.toolbar.ToolbarIconItem;
import delta.common.ui.swing.toolbar.ToolbarModel;
import delta.common.ui.swing.windows.WindowController;
import delta.common.ui.swing.windows.WindowsManager;
import delta.games.lotro.account.Account;
import delta.games.lotro.account.AccountsManager;
import delta.games.lotro.account.events.AccountEvent;
import delta.games.lotro.account.events.AccountEventType;
import delta.games.lotro.gui.utils.l10n.Labels;
import delta.games.lotro.utils.events.EventsManager;
import delta.games.lotro.utils.events.GenericEventsListener;

/**
 * Controller for the accounts management panel.
 * @author DAM
 */
public class AccountsManagementController implements ActionListener,GenericEventsListener<AccountEvent>
{
  private static final String NEW_ACCOUNT_ID="newAccount";
  private static final String REMOVE_ACCOUNT_ID="removeAccount";
  private JPanel _panel;
  private WindowController _parentController;
  private AccountsTableController _accountsTable;
  private ToolbarController _toolbar;
  private NewAccountDialogController _newAccountDialog;
  private WindowsManager _mainWindowsManager;

  /**
   * Constructor.
   * @param parentController Parent controller.
   */
  public AccountsManagementController(WindowController parentController)
  {
    _parentController=parentController;
    _mainWindowsManager=new WindowsManager();
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  public JPanel getPanel()
  {
    if (_panel==null)
    {
      _panel=buildPanel();
      EventsManager.addListener(AccountEvent.class,this);
    }
    return _panel;
  }

  private JPanel buildPanel()
  {
    JPanel ret=GuiFactory.buildBackgroundPanel(new BorderLayout());
    _toolbar=buildToolBar();
    JToolBar toolbar=_toolbar.getToolBar();
    ret.add(toolbar,BorderLayout.NORTH);
    _accountsTable=buildAccountsTable();
    JTable table=_accountsTable.getTable();
    JScrollPane scroll=GuiFactory.buildScrollPane(table);
    ret.add(scroll,BorderLayout.CENTER);
    return ret;
  }

  /**
   * Handle account events.
   * @param event Source event.
   */
  @Override
  public void eventOccurred(AccountEvent event)
  {
    AccountEventType type=event.getType();
    if ((type==AccountEventType.ACCOUNT_ADDED) || (type==AccountEventType.ACCOUNT_REMOVED))
    {
      _accountsTable.refresh();
    }
  }

  private String getToolbarIconPath(String iconName)
  {
    String imgLocation="/resources/gui/icons/"+iconName+"-icon.png";
    return imgLocation;
  }

  private AccountsTableController buildAccountsTable()
  {
    AccountsTableController tableController=new AccountsTableController();
    tableController.addActionListener(this);
    return tableController;
  }

  private ToolbarController buildToolBar()
  {
    ToolbarController controller=new ToolbarController();
    ToolbarModel model=controller.getModel();
    // New icon
    String newIconPath=getToolbarIconPath("new");
    ToolbarIconItem newIconItem=new ToolbarIconItem(NEW_ACCOUNT_ID,newIconPath,NEW_ACCOUNT_ID,"Create a new account...","New");
    model.addToolbarIconItem(newIconItem);
    // Remove icon
    String deleteIconPath=getToolbarIconPath("delete");
    ToolbarIconItem deleteIconItem=new ToolbarIconItem(REMOVE_ACCOUNT_ID,deleteIconPath,REMOVE_ACCOUNT_ID,"Remove the selected account...","Remove");
    model.addToolbarIconItem(deleteIconItem);
    controller.addActionListener(this);
    return controller;
  }

  /**
   * Action implementation.
   * @param event Source event.
   */
  @Override
  public void actionPerformed(ActionEvent event)
  {
    String action=event.getActionCommand();
    if (NEW_ACCOUNT_ID.equals(action))
    {
      startNewAccount();
    }
    else if (REMOVE_ACCOUNT_ID.equals(action))
    {
      deleteAccount();
    }
    else if (AccountsTableController.DOUBLE_CLICK.equals(action))
    {
      Account account=(Account)event.getSource();
      showAccount(account);
    }
  }

  private void showAccount(Account account)
  {
    String id=AccountWindowController.getIdentifier(account.getSummary());
    WindowController controller=_mainWindowsManager.getWindow(id);
    if (controller==null)
    {
      controller=new AccountWindowController(account);
      _mainWindowsManager.registerWindow(controller);
      controller.getWindow().setLocationRelativeTo(getPanel());
    }
    controller.bringToFront();
  }

  private void startNewAccount()
  {
    if (_newAccountDialog==null)
    {
      _newAccountDialog=new NewAccountDialogController(_parentController);
    }
    _newAccountDialog.getDialog().setLocationRelativeTo(getPanel());
    _newAccountDialog.show(true);
  }

  private void deleteAccount()
  {
    GenericTableController<Account> controller=_accountsTable.getTableController();
    Account account=controller.getSelectedItem();
    if (account!=null)
    {
      String accountName=account.getDisplayName();
      // Check deletion
      String text=Labels.getLabel("accounts.management.deleteQuestion",new Object[]{accountName});
      String title=Labels.getLabel("accounts.management.delete.title");
      int result=GuiFactory.showQuestionDialog(_parentController.getWindow(),text,title,JOptionPane.YES_NO_OPTION);
      if (result==JOptionPane.OK_OPTION)
      {
        String id=AccountWindowController.getIdentifier(account.getSummary());
        WindowController windowController=_mainWindowsManager.getWindow(id);
        if (windowController!=null)
        {
          windowController.dispose();
        }
        AccountsManager manager=AccountsManager.getInstance();
        manager.removeAccount(account);
      }
    }
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    if (_mainWindowsManager!=null)
    {
      _mainWindowsManager.disposeAll();
      _mainWindowsManager=null;
    }
    EventsManager.removeListener(AccountEvent.class,this);
    if (_accountsTable!=null)
    {
      _accountsTable.dispose();
      _accountsTable=null;
    }
    if (_toolbar!=null)
    {
      _toolbar.dispose();
      _toolbar=null;
    }
    if (_newAccountDialog!=null)
    {
      _newAccountDialog.dispose();
      _newAccountDialog=null;
    }
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
  }
}
