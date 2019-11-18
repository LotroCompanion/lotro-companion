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
import java.util.List;
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
import delta.games.lotro.character.virtues.VirtueDescription;
import delta.games.lotro.character.virtues.VirtuesManager;
import delta.games.lotro.gui.character.virtues.VirtueEditionUiController.TierValueListener;

/**
 * Controller for a virtues edition panel.
 * @author DAM
 */
public class VirtuesEditionPanelController implements TierValueListener
{
  private static final Logger LOGGER=Logger.getLogger(VirtuesEditionPanelController.class);

  private JPanel _panel;
  private HashMap<VirtueDescription,VirtueEditionUiController> _virtues;
  private VirtuesDisplayPanelController _selectedVirtues;
  private VirtuesStatsPanelController _stats;
  private JButton _maxAll;
  // Data
  private VirtuesSet _virtuesSet;
  private int _characterLevel;

  /**
   * Constructor.
   * @param characterLevel Character level.
   */
  public VirtuesEditionPanelController(int characterLevel)
  {
    _virtues=new HashMap<VirtueDescription,VirtueEditionUiController>();
    _characterLevel=characterLevel;
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
    }
    // Side panel
    JPanel sidePanel=buildSidePanel();
    c=new GridBagConstraints(1,1,1,1,1.0,1.0,GridBagConstraints.NORTH,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(sidePanel,c);
    return panel;
  }

  private JPanel buildVirtuesEditionPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    int index=0;
    List<VirtueDescription> virtues=VirtuesManager.getInstance().getAll();
    for(VirtueDescription virtue : virtues)
    {
      VirtueEditionUiController ui=new VirtueEditionUiController(virtue,_characterLevel);
      ui.setListener(this);
      int x=index/7;
      int y=index%7;
      GridBagConstraints c=new GridBagConstraints(x,y+1,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
      JPanel virtuePanel=ui.getPanel();
      panel.add(virtuePanel,c);
      _virtues.put(virtue,ui);
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
      VirtueDescription virtue=getVirtue(support);
      if (virtue!=null)
      {
        return !_selectedVirtues.hasVirtue(virtue);
      }
      return false;
    }

    private VirtueDescription getVirtue(TransferSupport support)
    {
      VirtueDescription virtue=null;
      try
      {
        Transferable t=support.getTransferable();
        Integer id=(Integer)t.getTransferData(DataFlavor.stringFlavor);
        virtue=VirtuesManager.getInstance().getVirtue(id.intValue());
      }
      catch(Exception e)
      {
        LOGGER.warn("Could not get virtue from DnD input data", e);
      }
      return virtue;
    }

    @Override
    public boolean importData(TransferSupport support)
    {
      VirtueDescription virtue=getVirtue(support);
      Component target=support.getComponent();
      for(int i=0;i<VirtuesDisplayPanelController.MAX_VIRTUES;i++)
      {
        JLabel label=_selectedVirtues.getVirtue(i).getLabel();
        if (label==target)
        {
          VirtueEditionUiController controller=_virtues.get(virtue);
          int tier=controller.getTier();
          int bonus=controller.getBonus();
          _selectedVirtues.setVirtue(i,virtue,tier,bonus);
          break;
        }
      }
      updateStats();
      return true;
    }
  }

  @Override
  public void tierChanged(VirtueDescription virtue, int tier)
  {
    _selectedVirtues.updateVirtue(virtue,tier);
    updateStats();
  }

  private void updateStats()
  {
    VirtuesSet virtues=getVirtues();
    _stats.update(virtues);
  }

  private void maxAll()
  {
    for(Map.Entry<VirtueDescription,VirtueEditionUiController> entry : _virtues.entrySet())
    {
      VirtueEditionUiController controller=entry.getValue();
      VirtueDescription virtue=entry.getKey();
      int maxRank=virtue.getMaxRank(_characterLevel);
      controller.setTier(maxRank);
      _selectedVirtues.updateVirtue(virtue,maxRank);
    }
  }

  /**
   * Set the virtues to show.
   * @param set Virtues to show.
   */
  public void setVirtues(VirtuesSet set)
  {
    _virtuesSet=set;
    // Set tiers
    List<VirtueDescription> virtues=VirtuesManager.getInstance().getAll();
    for(VirtueDescription virtue : virtues)
    {
      VirtueEditionUiController ui=_virtues.get(virtue);
      int tier=set.getVirtueRank(virtue);
      int bonus=set.getVirtueBonusRank(virtue);
      ui.init(tier,bonus);
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
    // Copy to get buffs
    ret.copyFrom(_virtuesSet);
    List<VirtueDescription> virtues=VirtuesManager.getInstance().getAll();
    for(VirtueDescription virtue : virtues)
    {
      VirtueEditionUiController ui=_virtues.get(virtue);
      int tier=ui.getTier();
      ret.setVirtueValue(virtue,tier);
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
