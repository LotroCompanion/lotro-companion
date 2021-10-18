package delta.games.lotro.gui.lore.items.legendary2;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.labels.MultilineLabel2;
import delta.games.lotro.lore.items.legendary2.SocketEntryInstance;
import delta.games.lotro.lore.items.legendary2.SocketsSetup;
import delta.games.lotro.lore.items.legendary2.SocketsSetupInstance;

/**
 * Controller for a panel to display a traceries set.
 * @author DAM
 */
public class TraceriesSetDisplayController
{
  // Data
  private SocketsSetupInstance _traceries;
  // Controllers
  private List<SingleTraceryDisplayController> _controllers;
  // UI
  private JPanel _panel;

  /**
   * Constructor.
   * @param traceries Essences to display.
   */
  public TraceriesSetDisplayController(SocketsSetupInstance traceries)
  {
    _traceries=traceries;
    _controllers=new ArrayList<SingleTraceryDisplayController>();
    SocketsSetup socketsSetup=traceries.getSetupTemplate();
    int nbSlots=socketsSetup.getSocketsCount();
    for(int i=0;i<nbSlots;i++)
    {
      SingleTraceryDisplayController controller=new SingleTraceryDisplayController();
      _controllers.add(controller);
    }
    _panel=build();
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  public JPanel getPanel()
  {
    return _panel;
  }

  private JPanel build()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    int baseLine=0;
    for(SingleTraceryDisplayController controller : _controllers)
    {
      // Icon
      JButton icon=controller.getIcon();
      GridBagConstraints c=new GridBagConstraints(0,baseLine,1,2,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
      panel.add(icon,c);
      // Label
      MultilineLabel2 label=controller.getNameGadget();
      c=new GridBagConstraints(1,baseLine,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,5),0,0);
      panel.add(label,c);
      // Stats
      MultilineLabel2 stats=controller.getStatsGadget();
      c=new GridBagConstraints(1,baseLine+1,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,5),0,0);
      panel.add(stats,c);
      baseLine+=2;
    }
    return panel;
  }

  /**
   * Update gadgets to reflect the current state of the associated sockets setup.
   */
  public void update()
  {
    int size=_controllers.size();
    for(int i=0;i<size;i++)
    {
      SocketEntryInstance entry=_traceries.getEntry(i);
      _controllers.get(i).setTracery(entry);
    }
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Data
    _traceries=null;
    // Controllers
    if (_controllers!=null)
    {
      for(SingleTraceryDisplayController controller : _controllers)
      {
        controller.dispose();
      }
      _controllers=null;
    }
    // UI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
  }
}
