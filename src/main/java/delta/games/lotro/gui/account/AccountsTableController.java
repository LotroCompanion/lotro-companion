package delta.games.lotro.gui.account;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;

import delta.common.ui.swing.tables.CellDataProvider;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.tables.ListDataProvider;
import delta.common.ui.swing.tables.DefaultTableColumnController;
import delta.games.lotro.account.Account;
import delta.games.lotro.account.AccountSummary;
import delta.games.lotro.account.AccountsManager;

/**
 * Controller for a table that shows all available accounts.
 * @author DAM
 */
public class AccountsTableController
{
  /**
   * Double-click action command.
   */
  public static final String DOUBLE_CLICK="double click";
  // Data
  private List<Account> _accounts;
  // GUI
  private JTable _table;
  private GenericTableController<Account> _tableController;

  /**
   * Constructor.
   */
  public AccountsTableController()
  {
    _accounts=new ArrayList<Account>();
    init();
    _tableController=buildTable();
  }

  private GenericTableController<Account> buildTable()
  {
    ListDataProvider<Account> provider=new ListDataProvider<Account>(_accounts);
    GenericTableController<Account> table=new GenericTableController<Account>(provider);

    // Name column
    {
      CellDataProvider<Account,String> nameCell=new CellDataProvider<Account,String>()
      {
        @Override
        public String getData(Account item)
        {
          AccountSummary data=getDataForAccount(item);
          return data.getName();
        }
      };
      DefaultTableColumnController<Account,String> nameColumn=new DefaultTableColumnController<Account,String>("Name",String.class,nameCell);
      nameColumn.setWidthSpecs(100,100,100);
      table.addColumnController(nameColumn);
    }
    return table;
  }

  /**
   * Get the managed table controller.
   * @return the managed table controller.
   */
  public GenericTableController<Account> getTableController()
  {
    return _tableController;
  }

  private AccountSummary getDataForAccount(Account account)
  {
    return account.getSummary();
  }

  private void reset()
  {
    _accounts.clear();
  }

  /**
   * Refresh accounts table.
   */
  public void refresh()
  {
    init();
    if (_table!=null)
    {
      _tableController.refresh();
    }
  }

  /**
   * Refresh accounts table.
   * @param account Account to refresh.
   */
  public void refresh(Account account)
  {
    if (_table!=null)
    {
      _tableController.refresh(account);
    }
  }

  private void init()
  {
    reset();
    AccountsManager manager=AccountsManager.getInstance();
    List<Account> accounts=manager.getAllAccounts();
    for(Account account : accounts)
    {
      loadAccount(account);
    }
  }

  private void loadAccount(Account account)
  {
    AccountSummary summary=account.getSummary();
    if (summary!=null)
    {
      _accounts.add(account);
    }
  }

  /**
   * Get the managed table.
   * @return the managed table.
   */
  public JTable getTable()
  {
    if (_table==null)
    {
      _table=_tableController.getTable();
    }
    return _table;
  }

  /**
   * Add an action listener.
   * @param al Action listener to add.
   */
  public void addActionListener(ActionListener al)
  {
    _tableController.addActionListener(al);
  }

  /**
   * Remove an action listener.
   * @param al Action listener to remove.
   */
  public void removeActionListener(ActionListener al)
  {
    _tableController.removeActionListener(al);
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // GUI
    if (_table!=null)
    {
      _table=null;
    }
    if (_tableController!=null)
    {
      _tableController.dispose();
      _tableController=null;
    }
    // Data
    _accounts=null;
  }
}
