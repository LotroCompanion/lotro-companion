package delta.games.lotro.gui.lore.items.essences;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.labels.MultilineLabel2;
import delta.games.lotro.common.enums.SocketType;
import delta.games.lotro.lore.items.essences.Essence;
import delta.games.lotro.lore.items.essences.EssencesSet;

/**
 * Controller for a panel to display an essences set.
 * @author DAM
 */
public class EssencesSetDisplayController
{
  // Data
  private EssencesSet _essences;
  // Controllers
  private List<SingleEssenceDisplayController> _controllers;
  // UI
  private JPanel _panel;

  /**
   * Constructor.
   * @param essences Essences to display.
   */
  public EssencesSetDisplayController(EssencesSet essences)
  {
    _essences=essences;
    _controllers=new ArrayList<SingleEssenceDisplayController>();
    int nbSlots=essences.getSize();
    for(int i=0;i<nbSlots;i++)
   {
      SocketType type=essences.getType(i);
      SingleEssenceDisplayController controller=new SingleEssenceDisplayController(type);
      _controllers.add(controller);
    }
    _panel=build();
    update();
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
    for(SingleEssenceDisplayController controller : _controllers)
    {
      // Icon
      JButton icon=controller.getIcon();
      GridBagConstraints c=new GridBagConstraints(0,y,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
      panel.add(icon,c);
      // Text
      MultilineLabel2 text=controller.getLinesGadget();
      c=new GridBagConstraints(1,y,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,5),0,0);
      panel.add(text,c);
      y++;
    }
    return panel;
  }

  /**
   * Update gadgets to reflect the current state of the associated essences set.
   */
  public void update()
  {
    int size=_essences.getSize();
    for(int i=0;i<size;i++)
    {
      Essence essence=_essences.getEssence(i);
      _controllers.get(i).setEssence(essence);
    }
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Data
    _essences=null;
    // Controllers
    if (_controllers!=null)
    {
      for(SingleEssenceDisplayController controller : _controllers)
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
