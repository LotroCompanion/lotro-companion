package delta.games.lotro.gui.character.virtues;

import java.awt.Dimension;
import java.util.HashMap;

import javax.swing.JPanel;

import delta.games.lotro.character.stats.virtues.VirtuesSet;
import delta.games.lotro.common.VirtueId;
import delta.games.lotro.gui.utils.GuiFactory;

/**
 * Controller for a virtues edition panel.
 * @author DAM
 */
public class VirtuesEditionPanelController
{
  private JPanel _panel;
  private HashMap<VirtueId,VirtueEditionUiController> _virtues;

  /**
   * Constructor.
   */
  public VirtuesEditionPanelController()
  {
    _virtues=new HashMap<VirtueId,VirtueEditionUiController>();
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
    JPanel panel=GuiFactory.buildBackgroundPanel(null);
    int index=0;
    for(VirtueId virtueId : VirtueId.values())
    {
      VirtueEditionUiController ui=new VirtueEditionUiController(virtueId,panel);
      int[] position=getPosition(index);
      ui.setLocation(position[0],position[1]);
      _virtues.put(virtueId,ui);
      index++;
    }
    panel.setPreferredSize(new Dimension(650,380));
    return panel;
  }

  /**
   * Set the virtues to show.
   * @param set Virtues to show.
   */
  public void setVirtues(VirtuesSet set)
  {
    for(VirtueId virtueId : VirtueId.values())
    {
      VirtueEditionUiController ui=_virtues.get(virtueId);
      int tier=set.getVirtueRank(virtueId);
      ui.setTier(tier);
    }
  }

  /**
   * Get the current virtues values.
   * @return the currently displayed virtues definition.
   */
  public VirtuesSet getVirtues()
  {
    VirtuesSet ret=new VirtuesSet();
    for(VirtueId virtueId : VirtueId.values())
    {
      VirtueEditionUiController ui=_virtues.get(virtueId);
      int tier=ui.getTier();
      ret.setVirtueValue(virtueId,tier);
    }
    return ret;
  }

  private static final int CENTER_X=300;
  private static final int CENTER_Y=170;
  private static final int WIDTH=250;
  private static final int HEIGHT=150;

  private int[] getPosition(int index)
  {
    int[] ret=new int[2];
    double angle=90-18*index;
    int x=CENTER_X+(int)(WIDTH*Math.cos(Math.toRadians(angle)));
    int y=CENTER_Y-(int)(HEIGHT*Math.sin(Math.toRadians(angle)));
    ret[0]=x;
    ret[1]=y;
    return ret;
  }
}
