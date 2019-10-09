package delta.games.lotro.gui.items.legendary.relics;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.labels.MultilineLabel2;
import delta.games.lotro.lore.items.legendary.relics.Relic;
import delta.games.lotro.lore.items.legendary.relics.RelicsSet;

/**
 * Controller for a panel to display a relics set.
 * @author DAM
 */
public class RelicsSetDisplayController
{
  // Controllers
  private List<SingleRelicDisplayController> _controllers;
  // UI
  private JPanel _panel;

  /**
   * Constructor.
   */
  public RelicsSetDisplayController()
  {
    _controllers=new ArrayList<SingleRelicDisplayController>();
    for(int i=0;i<4;i++)
    {
      SingleRelicDisplayController controller=new SingleRelicDisplayController();
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
    for(SingleRelicDisplayController controller : _controllers)
    {
      // Icon
      JLabel icon=controller.getIcon();
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
   * Set the data to display.
   * @param relicsSet Data to display.
   */
  public void setData(RelicsSet relicsSet)
  {
    List<Relic> relics=relicsSet.getAll();
    for(int i=0;i<4;i++)
    {
      Relic relic=relics.get(i);
      _controllers.get(i).setRelic(relic);
    }
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Controllers
    if (_controllers!=null)
    {
      for(SingleRelicDisplayController controller : _controllers)
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
