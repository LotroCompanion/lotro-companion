package delta.games.lotro.gui.account;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.DefaultWindowController;
import delta.common.ui.swing.windows.WindowsManager;
import delta.games.lotro.account.Account;
import delta.games.lotro.account.AccountUtils;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.gui.character.storage.AccountStorageDisplayWindowController;
import delta.games.lotro.gui.toon.ToonsTableController;

/**
 * Controller for an "account" window.
 * @author DAM
 */
public class AccountWindowController extends DefaultWindowController implements ActionListener
{
  private static final String STORAGE_COMMAND="storage";
  private static final String SERVER="Landroval";

  private AccountSummaryPanelController _summaryController;
  private ToonsTableController _toonsTable;
  private Account _account;
  private WindowsManager _windowsManager;

  /**
   * Constructor.
   * @param account Managed account.
   */
  public AccountWindowController(Account account)
  {
    _account=account;
    _windowsManager=new WindowsManager();
    _summaryController=new AccountSummaryPanelController(this,_account);
  }

  /**
   * Get the window identifier for a given account.
   * @param accountName Account name.
   * @return A window identifier.
   */
  public static String getIdentifier(String accountName)
  {
    String id="ACCOUNT#"+accountName;
    return id;
  }

  @Override
  protected JComponent buildContents()
  {
    // Whole panel
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    // Summary panel
    JPanel summaryPanel=_summaryController.getPanel();
    GridBagConstraints c=new GridBagConstraints(0,0,2,1,1,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(3,5,3,5),0,0);
    panel.add(summaryPanel,c);
    // Characters table
    JPanel tablePanel=buildTablePanel();
    c=new GridBagConstraints(0,1,1,1,1.0,1.0,GridBagConstraints.NORTHWEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
    panel.add(tablePanel,c);
    tablePanel.setBorder(GuiFactory.buildTitledBorder("Characters"));
    // Command buttons
    JPanel commandsPanel=buildCommandsPanel();
    c=new GridBagConstraints(0,2,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(commandsPanel,c);
    return panel;
  }

  @Override
  protected JFrame build()
  {
    JFrame frame=super.build();
    // Title
    String name=_account.getName();
    String title="Account: "+name;
    frame.setTitle(title);
    frame.setMinimumSize(new Dimension(400,200));
    frame.setSize(800,350);
    frame.setResizable(true);
    return frame;
  }

  @Override
  public String getWindowIdentifier()
  {
    String accountName=_account.getName();
    String id=getIdentifier(accountName);
    return id;
  }

  private JPanel buildCommandsPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    // Storage
    JButton storageButton=buildCommandButton("Storage",STORAGE_COMMAND);
    panel.add(storageButton,c);
    c.gridx++;

    return panel;
  }

  private JButton buildCommandButton(String label, String command)
  {
    JButton b=GuiFactory.buildButton(label);
    b.setActionCommand(command);
    b.addActionListener(this);
    return b;
  }

  /**
   * Handle button actions.
   * @param e Source event.
   */
  @Override
  public void actionPerformed(ActionEvent e)
  {
    String command=e.getActionCommand();
    if (STORAGE_COMMAND.equals(command))
    {
      showStorage();
    }
  }

  private JPanel buildTablePanel()
  {
    JPanel ret=GuiFactory.buildBackgroundPanel(new BorderLayout());
    _toonsTable=buildToonsTable();
    JTable table=_toonsTable.getTable();
    JScrollPane scroll=GuiFactory.buildScrollPane(table);
    ret.add(scroll,BorderLayout.CENTER);
    return ret;
  }

  private ToonsTableController buildToonsTable()
  {
    ToonsTableController tableController=new ToonsTableController();
    List<CharacterFile> characters=AccountUtils.getCharacters(_account.getName(),SERVER);
    tableController.setToons(characters);
    return tableController;
  }

  private void showStorage()
  {
    AccountStorageDisplayWindowController summaryController=(AccountStorageDisplayWindowController)_windowsManager.getWindow(AccountStorageDisplayWindowController.IDENTIFIER);
    if (summaryController==null)
    {
      summaryController=new AccountStorageDisplayWindowController(this,_account,SERVER);
      _windowsManager.registerWindow(summaryController);
      summaryController.getWindow().setLocationRelativeTo(getWindow());
    }
    summaryController.bringToFront();
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    if (_windowsManager!=null)
    {
      _windowsManager.disposeAll();
      _windowsManager=null;
    }
    super.dispose();
    if (_summaryController!=null)
    {
      _summaryController.dispose();
      _summaryController=null;
    }
    if (_toonsTable!=null)
    {
      _toonsTable.dispose();
      _toonsTable=null;
    }
    if (_account!=null)
    {
      //_account.gc();
      _account=null;
    }
  }
}
