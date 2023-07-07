package delta.games.lotro.gui.kinship;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.WindowController;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.gui.kinship.filter.KinshipMemberFilterController;
import delta.games.lotro.gui.main.GlobalPreferences;
import delta.games.lotro.kinship.Kinship;
import delta.games.lotro.kinship.filters.KinshipMemberFilter;

/**
 * Controller for a panel to display kinship data.
 * @author DAM
 */
public class KinshipPanelController
{
  // Data
  private Kinship _kinship;
  private KinshipMemberFilter _filter;
  // UI
  private JPanel _panel;
  // Controllers
  private WindowController _parent;
  private KinshipMemberFilterController _filterController;
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
    _filter=new KinshipMemberFilter();
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
    tablePanel.setBorder(GuiFactory.buildTitledBorder("Characters")); // I18n
    panel.add(tablePanel,BorderLayout.CENTER);
    // Top panel
    _filterController=new KinshipMemberFilterController(_kinship,_filter,_membersPanel);
    JPanel filterPanel=_filterController.getPanel();
    TitledBorder filterBorder=GuiFactory.buildTitledBorder("Filter"); // I18n
    filterPanel.setBorder(filterBorder);
    panel.add(filterPanel,BorderLayout.NORTH);
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
    KinshipMembersTableController tableController=new KinshipMembersTableController(prefs,_filter);
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
    _filter=null;
    // UI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    // Controllers
    _parent=null;
    if (_filterController!=null)
    {
      _filterController.dispose();
      _filterController=null;
    }
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
