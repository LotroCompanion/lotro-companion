package delta.games.lotro.gui.character.status.traits.racial;

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

import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.TransferHandler;
import javax.swing.border.TitledBorder;

import org.apache.log4j.Logger;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.BasicCharacterAttributes;
import delta.games.lotro.character.races.RaceDescription;
import delta.games.lotro.character.status.traits.shared.TraitSlotsStatus;
import delta.games.lotro.character.traits.TraitDescription;
import delta.games.lotro.character.traits.TraitsManager;
import delta.games.lotro.gui.lore.traits.TraitIconController;

/**
 * Controller for a racial traits status edition panel.
 * @author DAM
 */
public class RacialTraitsEditionPanelController implements ActionListener
{
  private static final Logger LOGGER=Logger.getLogger(RacialTraitsEditionPanelController.class);

  private static final String REMOVE_COMMAND="remove";

  // Controllers
  private WindowController _parent;
  private HashMap<TraitDescription,RacialTraitIconController> _traits;
  private RacialTraitsDisplayPanelController _selectedTraits;
  // UI
  private JPanel _panel;
  private JPopupMenu _contextMenu;
  // Data
  private TraitSlotsStatus _status;
  private BasicCharacterAttributes _character;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param character Character attributes.
   * @param status Current traits status.
   */
  public RacialTraitsEditionPanelController(WindowController parent, BasicCharacterAttributes character, TraitSlotsStatus status)
  {
    _parent=parent;
    _traits=new HashMap<TraitDescription,RacialTraitIconController>();
    _character=character;
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
    // All traits
    JPanel allTraits=buildTraitsPanel();
    GridBagConstraints c=new GridBagConstraints(0,1,1,1,0.0,0.0,GridBagConstraints.NORTHWEST,GridBagConstraints.HORIZONTAL,new Insets(5,5,5,5),0,0);
    panel.add(allTraits,c);
    // Selected traits
    JPanel selectedTraitsPanel=buildSelectedTraitsPanel();
    c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    panel.add(selectedTraitsPanel,c);
    return panel;
  }

  private JPanel buildSelectedTraitsPanel()
  {
    _selectedTraits=new RacialTraitsDisplayPanelController(_parent,_character.getLevel());
    _selectedTraits.setStatus(_status);
    JPanel selectedTraitsPanel=_selectedTraits.getPanel();
    for(int i=0;i<RacialTraitsDisplayPanelController.MAX_TRAITS;i++)
    {
      TraitIconController iconController=_selectedTraits.getTrait(i);
      JButton label=iconController.getIcon();
      TransferHandler handler=new DropTransferHandler();
      label.setTransferHandler(handler);
      MouseListener popupListener=buildRightClickListener();
      label.addMouseListener(popupListener);
    }
    TitledBorder border=GuiFactory.buildTitledBorder("Selected Traits");
    selectedTraitsPanel.setBorder(border);
    return selectedTraitsPanel;
  }

  private JPanel buildTraitsPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    int index=0;
    RaceDescription race=_character.getRace();
    int characterLevel=_character.getLevel();
    for(TraitDescription trait : race.getEarnableTraits())
    {
      RacialTraitIconController ui=new RacialTraitIconController(_parent,trait,characterLevel);
      int x=index%3;
      int y=index/3;
      GridBagConstraints c=new GridBagConstraints(x,y+1,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
      JPanel traitPanel=ui.getPanel();
      panel.add(traitPanel,c);
      _traits.put(trait,ui);
      index++;
    }
    TitledBorder border=GuiFactory.buildTitledBorder("Traits");
    panel.setBorder(border);
    return panel;
  }

  private class DropTransferHandler extends TransferHandler
  {
    @Override
    public boolean canImport(TransferSupport support)
    {
      TraitDescription trait=getTrait(support);
      if (trait!=null)
      {
        return !_selectedTraits.hasTrait(trait);
      }
      return false;
    }

    private TraitDescription getTrait(TransferSupport support)
    {
      TraitDescription trait=null;
      try
      {
        Transferable t=support.getTransferable();
        String id=(String)t.getTransferData(DataFlavor.stringFlavor);
        trait=TraitsManager.getInstance().getTrait(Integer.parseInt(id));
      }
      catch(Exception e)
      {
        LOGGER.warn("Could not get trait from DnD input data", e);
      }
      return trait;
    }

    @Override
    public boolean importData(TransferSupport support)
    {
      TraitDescription trait=getTrait(support);
      Component target=support.getComponent();
      for(int i=0;i<RacialTraitsDisplayPanelController.MAX_TRAITS;i++)
      {
        Component label=_selectedTraits.getTrait(i).getIcon();
        if (label==target)
        {
          _selectedTraits.setTrait(i,trait);
          break;
        }
      }
      return true;
    }
  }

  private void handleRemove(Object source)
  {
    for(int i=0;i<RacialTraitsDisplayPanelController.MAX_TRAITS;i++)
    {
      Component label=_selectedTraits.getTrait(i).getIcon();
      if (label==source)
      {
        _selectedTraits.setTrait(i,null);
        break;
      }
    }
  }

  /**
   * Set the traits to show.
   * @param status Traits to show.
   */
  public void setTraits(TraitSlotsStatus status)
  {
    _status=status;
    _selectedTraits.setStatus(status);
  }

  /**
   * Get the current trait values.
   * @return the currently displayed traits definition.
   */
  public TraitSlotsStatus getTraits()
  {
    TraitSlotsStatus ret=new TraitSlotsStatus();
    int [] traitIDs=new int[RacialTraitsDisplayPanelController.MAX_TRAITS];
    ret.setTraits(traitIDs);
    _selectedTraits.getSelectedTraits(ret);
    return ret;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Controllers
    _parent=null;
    if (_selectedTraits!=null)
    {
      _selectedTraits.dispose();
      _selectedTraits=null;
    }
    if (_traits!=null)
    {
      for(RacialTraitIconController editionUi : _traits.values())
      {
        editionUi.dispose();
      }
      _traits.clear();
      _traits=null;
    }
    // UI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    _contextMenu=null;
    // Data
    _status=null;
  }
}
