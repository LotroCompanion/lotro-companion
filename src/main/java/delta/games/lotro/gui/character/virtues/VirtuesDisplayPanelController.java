package delta.games.lotro.gui.character.virtues;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

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
  /**
   * Maximum number of slotted virtues.
   */
  public static final int MAX_VIRTUES=5;
  private VirtueIconController[] _virtues;
  private JLabel[] _virtueLabels;
  private JPanel _panel;

  /**
   * Constructor.
   */
  public VirtuesDisplayPanelController()
  {
    _virtues=new VirtueIconController[MAX_VIRTUES];
    _virtueLabels=new JLabel[MAX_VIRTUES];
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

  /**
   * Get the virtue icon controller at the given index.
   * @param index Targeted index, starting at 0.
   * @return A controller.
   */
  public VirtueIconController getVirtue(int index)
  {
    return _virtues[index];
  }

  /**
   * Indicates if this display shows the given virtue or not.
   * @param virtueId Targeted virtue.
   * @return <code>true</code> if it does, <code>false</code> otherwise.
   */
  public boolean hasVirtue(VirtueId virtueId)
  {
    for(int i=0;i<MAX_VIRTUES;i++)
    {
      if (_virtues[i].getVirtue()==virtueId)
      {
        return true;
      }
    }
    return false;
  }

  /**
   * Set the displayed virtue.
   * @param index Index of the slot to use.
   * @param virtueId Virtue to set.
   * @param tier Tier to set.
   */
  public void setVirtue(int index, VirtueId virtueId, int tier)
  {
    _virtues[index].setVirtue(virtueId);
    _virtues[index].setTier(tier);
    String virtueLabel=virtueId.name().toLowerCase();
    virtueLabel=virtueLabel.substring(0,1).toUpperCase()+virtueLabel.substring(1);
    _virtueLabels[index].setText(virtueLabel);
  }

  private JPanel build()
  {
    JPanel panel=GuiFactory.buildBackgroundPanel(new GridBagLayout());
    Font font=panel.getFont();
    for(int i=0;i<MAX_VIRTUES;i++)
    {
      _virtues[i]=new VirtueIconController(null,font);
      int left=(i>0)?3:0;
      GridBagConstraints c=new GridBagConstraints(i,0,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(0,left,0,0),0,0);
      _virtueLabels[i]=GuiFactory.buildLabel("");
      panel.add(_virtueLabels[i],c);
      c.gridy++;
      JLabel label=_virtues[i].getLabel();
      panel.add(label,c);
    }
    return panel;
  }

  /**
   * Update a virtue.
   * @param virtueId Targeted virtue.
   * @param tier New virtue tier.
   */
  public void updateVirtue(VirtueId virtueId, int tier)
  {
    for(int i=0;i<MAX_VIRTUES;i++)
    {
      if (_virtues[i].getVirtue()==virtueId)
      {
        _virtues[i].setTier(tier);
      }
    }
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
      int tier=virtues.getVirtueRank(virtueId);
      setVirtue(i,virtueId,tier);
    }
  }

  /**
   * Get the selected virtues.
   * @param virtues Storage for results.
   */
  public void getSelectedVirtues(VirtuesSet virtues)
  {
    for(int i=0;i<MAX_VIRTUES;i++)
    {
      VirtueIconController virtueController=_virtues[i];
      VirtueId virtue=virtueController.getVirtue();
      virtues.setSelectedVirtue(virtue,i);
    }
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
    if (_virtues!=null)
    {
      for(int i=0;i<MAX_VIRTUES;i++)
      {
        if (_virtues[i]!=null)
        {
          _virtues[i].dispose();
          _virtues[i]=null;
        }
      }
      _virtues=null;
    }
    _virtueLabels=null;
  }
}
