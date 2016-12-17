package delta.games.lotro.gui.character.virtues;

import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

import delta.games.lotro.character.stats.virtues.VirtuesSet;
import delta.games.lotro.common.VirtueId;
import delta.games.lotro.gui.utils.GuiFactory;

/**
 * Controller for a panel to display selected virtues.
 * @author DAM
 */
public class VirtuesDisplayPanelController
{
  private static final int MAX_VIRTUES=5;
  private VirtueIconController[] _virtues;
  private JPanel _panel;

  /**
   * Constructor.
   */
  public VirtuesDisplayPanelController()
  {
    _virtues=new VirtueIconController[MAX_VIRTUES];
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
    JPanel panel=GuiFactory.buildBackgroundPanel(new FlowLayout(FlowLayout.LEFT));
    Font font=panel.getFont();
    for(int i=0;i<MAX_VIRTUES;i++)
    {
      _virtues[i]=new VirtueIconController(null,font);
      JLabel label=_virtues[i].getLabel();
      panel.add(label);
    }
    return panel;
  }

  /**
   * Set virtues to show.
   * @param virtues Virtues to show.
   */
  public void setVirtues(VirtuesSet virtues)
  {
    for(int i=0;i<MAX_VIRTUES;i++)
    {
      VirtueId virtueId=virtues.getSelectedVirtue(i);
      _virtues[i].setVirtue(virtueId);
      int tier=virtues.getVirtueRank(virtueId);
      _virtues[i].setTier(tier);
    }
  }
}
