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
import delta.games.lotro.account.Account;
import delta.games.lotro.account.AccountsManager;
import delta.games.lotro.character.events.CharacterEvent;
import delta.games.lotro.utils.events.EventsManager;
import delta.games.lotro.utils.events.GenericEventsListener;

/**
 * Controller for the toons management panel.
 * @author DAM
 */
public class AccountsManagementController implements ActionListener,GenericEventsListener<CharacterEvent>
{
  private static final String NEW_ACCOUNT_ID="newAccount";
  private static final String REMOVE_ACCOUNT_ID="removeAccount";
  private JPanel _panel;
  private WindowController _parentController;
  private AccountsTableController _accountsTable;
  private ToolbarController _toolbar;
  private NewAccountDialogController _newToonDialog;

  /**
   * Constructor.
   * @param parentController Parent controller.
   */
  public AccountsManagementController(WindowController parentController)
  {
    _parentController=parentController;
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
      EventsManager.addListener(CharacterEvent.class,this);
    }
    return _panel;
  }

  private JPanel buildPanel()
  {
    JPanel ret=GuiFactory.buildBackgroundPanel(new BorderLayout());
    _toolbar=buildToolBar();
    JToolBar toolbar=_toolbar.getToolBar();
    ret.add(toolbar,BorderLayout.NORTH);
    _accountsTable=buildToonsTable();
    JTable table=_accountsTable.getTable();
    JScrollPane scroll=GuiFactory.buildScrollPane(table);
    ret.add(scroll,BorderLayout.CENTER);
    return ret;
  }

  /**
   * Handle character events.
   * @param event Source event.
   */
  @Override
  public void eventOccurred(CharacterEvent event)
  {
    // TODO
    /*
    if ((type==CharacterEventType.CHARACTER_ADDED) || (type==CharacterEventType.CHARACTER_REMOVED))
    {
      _accountsTable.refresh();
    }
    */
  }

  private String getToolbarIconPath(String iconName)
  {
    String imgLocation="/resources/gui/icons/"+iconName+"-icon.png";
    return imgLocation;
  }

  private AccountsTableController buildToonsTable()
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
    // TODO
  }

  private void startNewAccount()
  {
    if (_newToonDialog==null)
    {
      _newToonDialog=new NewAccountDialogController(_parentController);
    }
    _newToonDialog.getDialog().setLocationRelativeTo(getPanel());
    _newToonDialog.show(true);
  }

  private void deleteAccount()
  {
    GenericTableController<Account> controller=_accountsTable.getTableController();
    Account account=controller.getSelectedItem();
    if (account!=null)
    {
      String accountName=account.getName();
      // Check deletion
      int result=GuiFactory.showQuestionDialog(_parentController.getWindow(),"Do you really want to delete account " + accountName + "?","Delete?",JOptionPane.YES_NO_OPTION);
      if (result==JOptionPane.OK_OPTION)
      {
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
    EventsManager.removeListener(CharacterEvent.class,this);
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
    if (_newToonDialog!=null)
    {
      _newToonDialog.dispose();
      _newToonDialog=null;
    }
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
  }
}
