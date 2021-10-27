package delta.games.lotro.gui.lore.items.legendary.relics;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
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
    int y=0;
    for(SingleRelicDisplayController controller : _controllers)
    {
      // Icon
      JButton icon=controller.getIcon();
      GridBagConstraints c=new GridBagConstraints(0,y,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
      panel.add(icon,c);
      // Text
      MultilineLabel2 text=controller.getLinesGadget();
      c=new GridBagConstraints(1,y,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,5),0,0);
      panel.add(text,c);
      y++;
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
