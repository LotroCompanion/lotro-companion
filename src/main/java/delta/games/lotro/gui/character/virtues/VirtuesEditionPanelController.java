package delta.games.lotro.gui.character.virtues;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.TransferHandler;

import delta.common.ui.swing.GuiFactory;
import delta.games.lotro.character.stats.virtues.VirtuesSet;
import delta.games.lotro.common.VirtueId;
import delta.games.lotro.gui.character.virtues.VirtueEditionUiController.TierValueListener;

/**
 * Controller for a virtues edition panel.
 * @author DAM
 */
public class VirtuesEditionPanelController implements TierValueListener
{
  private JPanel _panel;
  private HashMap<VirtueId,VirtueEditionUiController> _virtues;
  private VirtuesDisplayPanelController _selectedVirtues;
  private JButton _maxAll;

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
    // All virtues
    {
      int index=0;
      for(VirtueId virtueId : VirtueId.values())
      {
        VirtueEditionUiController ui=new VirtueEditionUiController(virtueId,panel);
        ui.setListener(this);
        int[] position=getPosition(index);
        ui.setLocation(position[0],position[1]);
        _virtues.put(virtueId,ui);
        index++;
      }
    }
    // Selected virtues
    {
      _selectedVirtues=new VirtuesDisplayPanelController();
      JPanel selectedVirtuesPanel=_selectedVirtues.getPanel();
      for(int i=0;i<VirtuesDisplayPanelController.MAX_VIRTUES;i++)
      {
        VirtueIconController iconController=_selectedVirtues.getVirtue(i);
        JLabel label=iconController.getLabel();
        TransferHandler handler=new DropTransferHandler();
        label.setTransferHandler(handler);
      }
      panel.add(selectedVirtuesPanel);
      layoutSelectedVirtues();
    }
    // Max all button
    _maxAll=GuiFactory.buildButton("Max all");
    ActionListener al=new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        maxAll();
      }
    };
    _maxAll.addActionListener(al);
    panel.add(_maxAll);
    _maxAll.setSize(_maxAll.getPreferredSize());
    int x=CENTER_X-(_maxAll.getWidth()/2);
    int y=CENTER_Y+10+(_selectedVirtues.getPanel().getHeight()+_maxAll.getHeight()/2);
    _maxAll.setLocation(x,y);
    panel.setPreferredSize(new Dimension(634,348));
    return panel;
  }

  private void layoutSelectedVirtues()
  {
    JPanel selectedVirtuesPanel=_selectedVirtues.getPanel();
    selectedVirtuesPanel.setSize(selectedVirtuesPanel.getPreferredSize());
    int x=CENTER_X-(selectedVirtuesPanel.getWidth()/2);
    int y=CENTER_Y-(selectedVirtuesPanel.getHeight()/2);
    selectedVirtuesPanel.setLocation(x,y);
  }

  private class DropTransferHandler extends TransferHandler
  {
    @Override
    public boolean canImport(TransferSupport support)
    {
      VirtueId virtueId=getVirtue(support);
      if (virtueId!=null)
      {
        return !_selectedVirtues.hasVirtue(virtueId);
      }
      return false;
    }

    private VirtueId getVirtue(TransferSupport support)
    {
      VirtueId virtueId=null;
      try
      {
        Transferable t=support.getTransferable();
        virtueId=(VirtueId)t.getTransferData(DataFlavor.stringFlavor);
      }
      catch(Exception e)
      {
        e.printStackTrace();
      }
      return virtueId;
    }

    @Override
    public boolean importData(TransferSupport support)
    {
      VirtueId virtueId=getVirtue(support);
      Component target=support.getComponent();
      for(int i=0;i<VirtuesDisplayPanelController.MAX_VIRTUES;i++)
      {
        JLabel label=_selectedVirtues.getVirtue(i).getLabel();
        if (label==target)
        {
          int tier=_virtues.get(virtueId).getTier();
          _selectedVirtues.setVirtue(i,virtueId,tier);
          layoutSelectedVirtues();
          break;
        }
      }
      return true;
    }
  }

  public void tierChanged(VirtueId virtueId, int tier)
  {
    _selectedVirtues.updateVirtue(virtueId,tier);
  }

  private void maxAll()
  {
    for(Map.Entry<VirtueId,VirtueEditionUiController> entry : _virtues.entrySet())
    {
      VirtueEditionUiController controller=entry.getValue();
      int tier=VirtueEditionUiController.MAX_TIER;
      controller.setTier(tier);
      VirtueId virtueId=entry.getKey();
      _selectedVirtues.updateVirtue(virtueId,tier);
    }
  }

  /**
   * Set the virtues to show.
   * @param set Virtues to show.
   */
  public void setVirtues(VirtuesSet set)
  {
    // Set tiers
    for(VirtueId virtueId : VirtueId.values())
    {
      VirtueEditionUiController ui=_virtues.get(virtueId);
      int tier=set.getVirtueRank(virtueId);
      ui.setTier(tier);
    }
    // Set selected virtues
    _selectedVirtues.setVirtues(set);
    layoutSelectedVirtues();
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
    _selectedVirtues.getSelectedVirtues(ret);
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
    if (_selectedVirtues!=null)
    {
      _selectedVirtues.dispose();
      _selectedVirtues=null;
    }
    _maxAll=null;
    _virtues=null;
  }
}
