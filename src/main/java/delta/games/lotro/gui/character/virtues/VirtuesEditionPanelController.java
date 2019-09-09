package delta.games.lotro.gui.character.virtues;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.TransferHandler;
import javax.swing.border.TitledBorder;

import org.apache.log4j.Logger;

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
  private static final Logger LOGGER=Logger.getLogger(VirtuesEditionPanelController.class);

  private JPanel _panel;
  private HashMap<VirtueId,VirtueEditionUiController> _virtues;
  private VirtuesDisplayPanelController _selectedVirtues;
  private VirtuesStatsPanelController _stats;
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
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    // All virtues
    JPanel allVirtues=buildVirtuesEditionPanel();
    GridBagConstraints c=new GridBagConstraints(0,1,1,1,0.0,0.0,GridBagConstraints.NORTHWEST,GridBagConstraints.HORIZONTAL,new Insets(5,5,5,5),0,0);
    panel.add(allVirtues,c);

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
      TitledBorder border=GuiFactory.buildTitledBorder("Selected Virtues");
      selectedVirtuesPanel.setBorder(border);
      c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
      panel.add(selectedVirtuesPanel,c);
      //selectedVirtuesPanel.setBackground(Color.GREEN);
      //selectedVirtuesPanel.setOpaque(true);
    }
    // Side panel
    JPanel sidePanel=buildSidePanel();
    c=new GridBagConstraints(1,1,1,1,1.0,1.0,GridBagConstraints.NORTH,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(sidePanel,c);
    //sidePanel.setBackground(Color.PINK);
    //sidePanel.setOpaque(true);
    return panel;
  }

  private JPanel buildVirtuesEditionPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    int index=0;
    for(VirtueId virtueId : VirtueId.values())
    {
      VirtueEditionUiController ui=new VirtueEditionUiController(virtueId);
      ui.setListener(this);
      int x=index/7;
      int y=index%7;
      GridBagConstraints c=new GridBagConstraints(x,y+1,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
      JPanel virtuePanel=ui.getPanel();
      panel.add(virtuePanel,c);
      _virtues.put(virtueId,ui);
      index++;
    }
    TitledBorder border=GuiFactory.buildTitledBorder("Virtues");
    panel.setBorder(border);
    return panel;
  }

  private JPanel buildSidePanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    // Stats
    _stats=new VirtuesStatsPanelController();
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,1.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL,new Insets(5,5,5,5),0,0);
    panel.add(_stats.getPanel(),c);
    // Max all button
    _maxAll=GuiFactory.buildButton("Max all");
    ActionListener al=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        maxAll();
      }
    };
    _maxAll.addActionListener(al);
    c=new GridBagConstraints(0,1,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    panel.add(_maxAll,c);
    // Strut
    Component strut=Box.createHorizontalStrut(200);
    c=new GridBagConstraints(0,2,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(1,1,1,1),0,0);
    panel.add(strut,c);
    return panel;
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
        LOGGER.warn("Could not get virtue ID from DnD input data", e);
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
          break;
        }
      }
      updateStats();
      return true;
    }
  }

  @Override
  public void tierChanged(VirtueId virtueId, int tier)
  {
    _selectedVirtues.updateVirtue(virtueId,tier);
    updateStats();
  }

  private void updateStats()
  {
    VirtuesSet virtues=getVirtues();
    _stats.update(virtues);
  }

  private void maxAll()
  {
    for(Map.Entry<VirtueId,VirtueEditionUiController> entry : _virtues.entrySet())
    {
      VirtueEditionUiController controller=entry.getValue();
      int tier=VirtuesSet.MAX_TIER;
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
    // Update stats
    _stats.update(set);
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
    if (_virtues!=null)
    {
      for(VirtueEditionUiController editionUi : _virtues.values())
      {
        editionUi.dispose();
      }
      _virtues.clear();
      _virtues=null;
    }
  }
}
