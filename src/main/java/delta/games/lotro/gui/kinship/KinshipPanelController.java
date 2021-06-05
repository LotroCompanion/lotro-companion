package delta.games.lotro.gui.kinship;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.WindowController;
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
  // Controllers
  private WindowController _parent;
  private KinshipMembersTableController _membersTable;
  private KinshipMembersPanelController _membersPanel;

  /**
   * Constructor.
   * @param parent Parent window controller.
   * @param kinship Managed account.
   */
  public KinshipPanelController(WindowController parent, Kinship kinship)
  {
    _parent=parent;
    _kinship=kinship;
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
    _membersTable=buildMembersTable();
    _membersPanel=new KinshipMembersPanelController(_parent,_membersTable);
    return _membersPanel.getPanel();
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
    _parent=null;
    if (_membersTable!=null)
    {
      _membersTable.dispose();
      _membersTable=null;
    }
    if (_membersPanel!=null)
    {
      _membersPanel.dispose();
      _membersPanel=null;
    }
  }
}
