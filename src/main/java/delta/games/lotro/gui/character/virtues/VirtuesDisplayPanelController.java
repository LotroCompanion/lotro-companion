package delta.games.lotro.gui.character.virtues;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.games.lotro.character.stats.virtues.VirtuesSet;
import delta.games.lotro.character.virtues.VirtueDescription;

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
  // Config
  private boolean _showVirtueName;

  /**
   * Constructor.
   * @param showVirtueName Show the name of the virtue or not.
   */
  public VirtuesDisplayPanelController(boolean showVirtueName)
  {
    _virtues=new VirtueIconController[MAX_VIRTUES];
    _virtueLabels=new JLabel[MAX_VIRTUES];
    _showVirtueName=showVirtueName;
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
   * @param virtue Targeted virtue.
   * @return <code>true</code> if it does, <code>false</code> otherwise.
   */
  public boolean hasVirtue(VirtueDescription virtue)
  {
    for(int i=0;i<MAX_VIRTUES;i++)
    {
      if (_virtues[i].getVirtue()==virtue)
      {
        return true;
      }
    }
    return false;
  }

  /**
   * Set the displayed virtue.
   * @param index Index of the slot to use.
   * @param virtue Virtue to set.
   * @param tier Tier to set.
   * @param bonus Bonus to set.
   */
  public void setVirtue(int index, VirtueDescription virtue, int tier, int bonus)
  {
    _virtues[index].setVirtue(virtue);
    _virtues[index].setTier(tier);
    _virtues[index].setBonus(bonus);
    String virtueLabel="";
    if (virtue!=null)
    {
      virtueLabel=virtue.getName();
    }
    _virtueLabels[index].setText(virtueLabel);
  }

  private JPanel build()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    for(int i=0;i<MAX_VIRTUES;i++)
    {
      _virtues[i]=new VirtueIconController(null,true);
      int left=(i>0)?3:0;
      _virtueLabels[i]=GuiFactory.buildLabel("");
      GridBagConstraints c=new GridBagConstraints(i,0,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(0,left,0,0),0,0);
      if (_showVirtueName)
      {
        panel.add(_virtueLabels[i],c);
        c.gridy++;
      }
      JLabel label=_virtues[i].getLabel();
      panel.add(label,c);
    }
    return panel;
  }

  /**
   * Update a virtue.
   * @param virtue Targeted virtue.
   * @param tier New virtue tier.
   */
  public void updateVirtue(VirtueDescription virtue, int tier)
  {
    for(int i=0;i<MAX_VIRTUES;i++)
    {
      if (_virtues[i].getVirtue()==virtue)
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
      VirtueDescription virtue=virtues.getSelectedVirtue(i);
      int tier=virtues.getVirtueRank(virtue);
      int bonus=virtues.getVirtueBonusRank(virtue);
      setVirtue(i,virtue,tier,bonus);
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
      VirtueDescription virtue=virtueController.getVirtue();
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
