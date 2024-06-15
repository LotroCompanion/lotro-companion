package delta.games.lotro.gui.character.status.hobbies;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.status.hobbies.HobbiesStatusManager;
import delta.games.lotro.character.status.hobbies.HobbyStatus;
import delta.games.lotro.common.comparators.NamedComparator;
import delta.games.lotro.lore.hobbies.HobbiesManager;
import delta.games.lotro.lore.hobbies.HobbyDescription;

/**
 * Controller for a panel to display hobbies status for a single character.
 * @author DAM
 */
public class HobbiesStatusPanelController
{
  // UI
  private JPanel _panel;
  // Controllers
  private List<HobbyStatusPanelController> _hobbyCtrls;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param statusMgr Status to show.
   */
  public HobbiesStatusPanelController(WindowController parent, HobbiesStatusManager statusMgr)
  {
    _hobbyCtrls=new ArrayList<HobbyStatusPanelController>();
    _panel=buildPanel(parent,statusMgr);
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  public JPanel getPanel()
  {
    return _panel;
  }

  private JPanel buildPanel(WindowController parent, HobbiesStatusManager statusMgr)
  {
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    List<HobbyDescription> hobbies=HobbiesManager.getInstance().getAll();
    Collections.sort(hobbies,new NamedComparator());
    int y=0;
    for(HobbyDescription hobby : hobbies)
    {
      HobbyStatus hobbyStatus=statusMgr.get(hobby,false);
      if (hobbyStatus==null)
      {
        hobbyStatus=new HobbyStatus(hobby);
        hobbyStatus.setValue(-1);
      }
      HobbyStatusPanelController statusCtrl=new HobbyStatusPanelController(parent,hobbyStatus);
      JPanel statusPanel=statusCtrl.getPanel();
      statusPanel.setBorder(GuiFactory.buildTitledBorder(""));
      int top=(y==0)?5:0;
      GridBagConstraints c=new GridBagConstraints(0,y,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(top,5,5,5),0,0);
      ret.add(statusPanel,c);
      y++;
    }
    return ret;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // UI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    // Controllers
    if (_hobbyCtrls!=null)
    {
      for(HobbyStatusPanelController hobbyStatusCtrl : _hobbyCtrls)
      {
        hobbyStatusCtrl.dispose();
      }
      _hobbyCtrls=null;
    }
  }
}
