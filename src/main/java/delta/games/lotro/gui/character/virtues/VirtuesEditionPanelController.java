package delta.games.lotro.gui.character.virtues;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.TransferHandler;
import javax.swing.border.TitledBorder;

import org.apache.log4j.Logger;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.stats.virtues.VirtuesSet;
import delta.games.lotro.character.status.virtues.VirtuesStatus;
import delta.games.lotro.character.virtues.VirtueDescription;
import delta.games.lotro.character.virtues.VirtuesManager;
import delta.games.lotro.config.LotroCoreConfig;
import delta.games.lotro.gui.character.virtues.VirtueEditionUiController.TierValueListener;

/**
 * Controller for a virtues edition panel.
 * @author DAM
 */
public class VirtuesEditionPanelController implements TierValueListener,ActionListener
{
  private static final Logger LOGGER=Logger.getLogger(VirtuesEditionPanelController.class);

  private static final String REMOVE_COMMAND="remove";

  // Controllers
  private WindowController _parent;
  private HashMap<VirtueDescription,VirtueEditionUiController> _virtues;
  private VirtuesDisplayPanelController _selectedVirtues;
  private VirtuesStatsPanelController _stats;
  private VirtuesStatisticsPanelController _statistics;
  // UI
  private JPanel _panel;
  private JButton _maxAll;
  private JButton _maxAllSlotted;
  private JPopupMenu _contextMenu;
  // Data
  private VirtuesStatus _status;
  private VirtuesSet _virtuesSet;
  private int _characterLevel;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param characterLevel Character level.
   * @param status Current virtues status.
   */
  public VirtuesEditionPanelController(WindowController parent, int characterLevel, VirtuesStatus status)
  {
    _parent=parent;
    _virtues=new HashMap<VirtueDescription,VirtueEditionUiController>();
    _characterLevel=characterLevel;
    _status=status;
    _panel=build();
    _contextMenu=buildContextualMenu();
  }

  private JPopupMenu buildContextualMenu()
  {
    JPopupMenu popup=new JPopupMenu();
    JMenuItem remove=new JMenuItem("Remove");
    remove.setActionCommand(REMOVE_COMMAND);
    remove.addActionListener(this);
    popup.add(remove);
    return popup;
  }

  /**
   * Callback for managed commands:
   * <ul>
   * <li>Remove.
   * </ul>
   * @param event Source event.
   */
  @Override
  public void actionPerformed(ActionEvent event)
  {
    String cmd=event.getActionCommand();
    if (REMOVE_COMMAND.equals(cmd))
    {
      // From contextual menu
      Component invoker=_contextMenu.getInvoker();
      handleRemove(invoker);
    }
  }

  private MouseListener buildRightClickListener()
  {
    class PopClickListener extends MouseAdapter
    {
      @Override
      public void mousePressed(MouseEvent e)
      {
        if (e.isPopupTrigger()) doPop(e);
      }
  
      @Override
      public void mouseReleased(MouseEvent e)
      {
        if (e.isPopupTrigger()) doPop(e);
      }
  
      private void doPop(MouseEvent e)
      {
        _contextMenu.show(e.getComponent(),e.getX(),e.getY());
      }
    }
    return new PopClickListener();
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
    JPanel selectedVirtuesPanel=buildSelectedVirtuesPanel();
    c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    panel.add(selectedVirtuesPanel,c);
    // Side panel
    JPanel sidePanel=buildSidePanel();
    c=new GridBagConstraints(1,1,1,1,1.0,1.0,GridBagConstraints.NORTH,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(sidePanel,c);
    return panel;
  }

  private JPanel buildSelectedVirtuesPanel()
  {
    _selectedVirtues=new VirtuesDisplayPanelController();
    JPanel selectedVirtuesPanel=_selectedVirtues.getPanel();
    for(int i=0;i<VirtuesDisplayPanelController.MAX_VIRTUES;i++)
    {
      VirtueIconController iconController=_selectedVirtues.getVirtue(i);
      JLabel label=iconController.getLabel();
      TransferHandler handler=new DropTransferHandler();
      label.setTransferHandler(handler);
      MouseListener popupListener=buildRightClickListener();
      label.addMouseListener(popupListener);
    }
    TitledBorder border=GuiFactory.buildTitledBorder("Selected Virtues");
    selectedVirtuesPanel.setBorder(border);
    return selectedVirtuesPanel;
  }

  private JPanel buildVirtuesEditionPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    int index=0;
    List<VirtueDescription> virtues=VirtuesManager.getInstance().getAll();
    for(VirtueDescription virtue : virtues)
    {
      VirtueEditionUiController ui=new VirtueEditionUiController(_parent,virtue,_characterLevel);
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
    int y=0;
    // Stats
    _stats=new VirtuesStatsPanelController();
    GridBagConstraints c=new GridBagConstraints(0,y,1,1,1.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL,new Insets(5,5,5,5),0,0);
    panel.add(_stats.getPanel(),c);
    y++;
    // Statistics
    _statistics=new VirtuesStatisticsPanelController(_status);
    c=new GridBagConstraints(0,y,1,1,1.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL,new Insets(5,5,5,5),0,0);
    panel.add(_statistics.getPanel(),c);
    y++;
    // Buttons
    JPanel buttonsPanel=buildButtonsPanel();
    c=new GridBagConstraints(0,y,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    panel.add(buttonsPanel,c);
    y++;
    // Strut
    Component strut=Box.createHorizontalStrut(200);
    c=new GridBagConstraints(0,y,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(1,1,1,1),0,0);
    panel.add(strut,c);
    y++;
    return panel;
  }

  private JPanel buildButtonsPanel()
  {
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
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
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    ret.add(_maxAll,c);
    // Max all slotted button
    _maxAllSlotted=GuiFactory.buildButton("Max all slotted");
    ActionListener al2=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        maxAllSlotted();
      }
    };
    _maxAllSlotted.addActionListener(al2);
    c=new GridBagConstraints(1,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,0,5,5),0,0);
    ret.add(_maxAllSlotted,c);
    return ret;
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

  private void handleRemove(Object source)
  {
    for(int i=0;i<VirtuesDisplayPanelController.MAX_VIRTUES;i++)
    {
      JLabel label=_selectedVirtues.getVirtue(i).getLabel();
      if (label==source)
      {
        _virtuesSet.setSelectedVirtue(null,i);
        _selectedVirtues.setVirtue(i,null,0,0);
        break;
      }
    }
    updateStats();
  }

  private void updateStats()
  {
    VirtuesSet virtues=getVirtues();
    _stats.update(virtues);
    _statistics.update(virtues);
  }

  private void maxAll()
  {
    int globalMaxRank=LotroCoreConfig.getInstance().getMaxVirtueRank();
    for(Map.Entry<VirtueDescription,VirtueEditionUiController> entry : _virtues.entrySet())
    {
      VirtueEditionUiController controller=entry.getValue();
      VirtueDescription virtue=entry.getKey();
      int maxRank=virtue.getMaxRank(_characterLevel);
      int rankToUse=Math.min(maxRank,globalMaxRank);
      controller.setTier(rankToUse);
      _selectedVirtues.updateVirtue(virtue,rankToUse);
    }
  }

  private void maxAllSlotted()
  {
    VirtuesSet selectedVirtues=new VirtuesSet();
    _selectedVirtues.getSelectedVirtues(selectedVirtues);
    int globalMaxRank=LotroCoreConfig.getInstance().getMaxVirtueRank();
    for(int i=0;i<VirtuesSet.MAX_VIRTUES;i++)
    {
      VirtueDescription virtue=selectedVirtues.getSelectedVirtue(i);
      if (virtue!=null)
      {
        int maxRank=virtue.getMaxRank(_characterLevel);
        int rankToUse=Math.min(maxRank,globalMaxRank);
        VirtueEditionUiController controller=_virtues.get(virtue);
        controller.setTier(rankToUse);
        _selectedVirtues.updateVirtue(virtue,rankToUse);
      }
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
    // Update statistics
    _statistics.update(set);
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
    // Controllers
    _parent=null;
    if (_selectedVirtues!=null)
    {
      _selectedVirtues.dispose();
      _selectedVirtues=null;
    }
    if (_stats!=null)
    {
      _stats.dispose();
      _stats=null;
    }
    if (_statistics!=null)
    {
      _statistics.dispose();
      _statistics=null;
    }
    if (_virtues!=null)
    {
      for(VirtueEditionUiController editionUi : _virtues.values())
      {
        editionUi.dispose();
      }
      _virtues.clear();
      _virtues=null;
    }
    // UI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    _maxAll=null;
    _maxAllSlotted=null;
    _contextMenu=null;
    // Data
    _status=null;
    _virtuesSet=null;
  }
}
