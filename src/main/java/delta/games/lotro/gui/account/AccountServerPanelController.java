package delta.games.lotro.gui.account;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.WindowController;
import delta.common.ui.swing.windows.WindowsManager;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.account.Account;
import delta.games.lotro.account.AccountUtils;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.gui.character.storage.AccountStorageDisplayWindowController;
import delta.games.lotro.gui.main.GlobalPreferences;
import delta.games.lotro.gui.toon.ToonsTableController;

/**
 * Controller for a panel to display account/server data.
 * @author DAM
 */
public class AccountServerPanelController implements ActionListener
{
  private static final String STORAGE_COMMAND="storage";

  // Data
  private Account _account;
  private String _server;
  // UI
  private JPanel _panel;
  private WindowsManager _windowsManager;
  // Controllers
  private WindowController _parent;
  private ToonsTableController _toonsTable;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param account Managed account.
   * @param serverName Managed server.
   */
  public AccountServerPanelController(WindowController parent, Account account, String serverName)
  {
    _parent=parent;
    _account=account;
    _server=serverName;
    _windowsManager=new WindowsManager();
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
    JPanel panel=GuiFactory.buildBackgroundPanel(new BorderLayout());
    // Characters table
    JPanel tablePanel=buildTablePanel();
    tablePanel.setBorder(GuiFactory.buildTitledBorder("Characters"));
    panel.add(tablePanel,BorderLayout.CENTER);
    // Command buttons
    JPanel commandsPanel=buildCommandsPanel();
    panel.add(commandsPanel,BorderLayout.SOUTH);
    return panel;
  }

  private JPanel buildTablePanel()
  {
    JPanel ret=GuiFactory.buildPanel(new BorderLayout());
    _toonsTable=buildToonsTable();
    JTable table=_toonsTable.getTable();
    JScrollPane scroll=GuiFactory.buildScrollPane(table);
    ret.add(scroll,BorderLayout.CENTER);
    return ret;
  }

  private ToonsTableController buildToonsTable()
  {
    TypedProperties prefs=GlobalPreferences.getGlobalProperties("AccountServerCharTable");
    ToonsTableController tableController=new ToonsTableController(prefs);
    List<CharacterFile> characters=AccountUtils.getCharacters(_account.getName(),_server);
    tableController.setToons(characters);
    return tableController;
  }

  private JPanel buildCommandsPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
    // Storage
    JButton storageButton=buildCommandButton("Storage",STORAGE_COMMAND);
    panel.add(storageButton);

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

  private void showStorage()
  {
    AccountStorageDisplayWindowController summaryController=(AccountStorageDisplayWindowController)_windowsManager.getWindow(AccountStorageDisplayWindowController.IDENTIFIER);
    if (summaryController==null)
    {
      summaryController=new AccountStorageDisplayWindowController(_parent,_account,_server);
      _windowsManager.registerWindow(summaryController);
      summaryController.getWindow().setLocationRelativeTo(_parent.getWindow());
    }
    summaryController.bringToFront();
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Data
    _account=null;
    _server=null;
    // UI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    // Controllers
    if (_windowsManager!=null)
    {
      _windowsManager.disposeAll();
      _windowsManager=null;
    }
    if (_toonsTable!=null)
    {
      _toonsTable.dispose();
      _toonsTable=null;
    }
  }
}
