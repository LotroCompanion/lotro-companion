package delta.games.lotro.gui.kinship;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.WindowsManager;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.gui.main.GlobalPreferences;
import delta.games.lotro.kinship.Kinship;

/**
 * Controller for a panel to display kinship data.
 * @author DAM
 */
public class KinshipPanelController
{
  // Data
  private Kinship _kinship;
  // UI
  private JPanel _panel;
  private WindowsManager _windowsManager;
  // Controllers
  private KinshipMembersTableController _membersTable;

  /**
   * Constructor.
   * @param kinship Managed account.
   */
  public KinshipPanelController(Kinship kinship)
  {
    _kinship=kinship;
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
    return panel;
  }

  private JPanel buildTablePanel()
  {
    JPanel ret=GuiFactory.buildPanel(new BorderLayout());
    _membersTable=buildMembersTable();
    JTable table=_membersTable.getTable();
    JScrollPane scroll=GuiFactory.buildScrollPane(table);
    ret.add(scroll,BorderLayout.CENTER);
    return ret;
  }

  private KinshipMembersTableController buildMembersTable()
  {
    TypedProperties prefs=GlobalPreferences.getGlobalProperties("KinshipMembersTable");
    KinshipMembersTableController tableController=new KinshipMembersTableController(prefs,null);
    tableController.setMembers(_kinship.getRoster().getAllMembers());
    return tableController;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Data
    _kinship=null;
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
    if (_membersTable!=null)
    {
      _membersTable.dispose();
      _membersTable=null;
    }
  }
}
