package delta.games.lotro.gui.character.status.allegiances.summary;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.status.allegiances.AllegiancesStatusManager;
import delta.games.lotro.common.enums.AllegianceGroup;
import delta.games.lotro.lore.allegiances.AllegiancesManager;

/**
 * Controller for a panel that displays the status of allegiances for a character.
 * @author DAM
 */
public class AllegiancesStatusSummaryPanelController
{
  // UI
  private JPanel _panel;
  // Controllers
  private WindowController _parent;
  private List<AllegiancesGroupStatusPanelController> _groups;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param statusMgr Data to show.
   */
  public AllegiancesStatusSummaryPanelController(WindowController parent, AllegiancesStatusManager statusMgr)
  {
    _parent=parent;
    _groups=new ArrayList<AllegiancesGroupStatusPanelController>();
    init(statusMgr);
    _panel=buildPanel();
  }

  private void init(AllegiancesStatusManager statusMgr)
  {
    AllegiancesManager mgr=AllegiancesManager.getInstance();
    for(AllegianceGroup group : mgr.getAllegiancesGroups())
    {
      AllegiancesGroupStatusPanelController ctrl=new AllegiancesGroupStatusPanelController(_parent,group,statusMgr);
      _groups.add(ctrl);
    }
  }

  private JPanel buildPanel()
  {
    JPanel ret=GuiFactory.buildBackgroundPanel(new GridBagLayout());
    int y=0;
    for(AllegiancesGroupStatusPanelController gadgets : _groups)
    {
      int top=(y==0)?5:2;
      int bottom=(y==_groups.size()-1)?5:2;
      GridBagConstraints c=new GridBagConstraints(0,y,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(top,5,bottom,5),0,0);
      ret.add(gadgets.getPanel(),c);
      y++;
    }
    return ret;
  }

  /**
   * Get the managed panel.
   * @return a panel.
   */
  public JPanel getPanel()
  {
    return _panel;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    _parent=null;
    if (_groups!=null)
    {
      for(AllegiancesGroupStatusPanelController groupCtrl : _groups)
      {
        groupCtrl.dispose();
      }
      _groups.clear();
      _groups=null;
    }
  }
}
