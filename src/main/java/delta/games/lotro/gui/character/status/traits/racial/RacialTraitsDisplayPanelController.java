package delta.games.lotro.gui.character.status.traits.racial;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.panels.AbstractPanelController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.status.traits.shared.TraitSlotsStatus;
import delta.games.lotro.character.traits.TraitDescription;
import delta.games.lotro.character.traits.TraitsManager;
import delta.games.lotro.gui.lore.traits.TraitIconController;

/**
 * Controller for a panel to display selected traits.
 * @author DAM
 */
public class RacialTraitsDisplayPanelController extends AbstractPanelController
{
  /**
   * Maximum number of slotted traits.
   */
  public static final int MAX_TRAITS=5;
  private int _characterLevel;
  private TraitIconController[] _traits;
  private JPanel _panel;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param characterLevel Character level.
   */
  public RacialTraitsDisplayPanelController(WindowController parent, int characterLevel)
  {
    super(parent);
    _characterLevel=characterLevel;
    _traits=new TraitIconController[MAX_TRAITS];
    _panel=build();
  }

  @Override
  public JPanel getPanel()
  {
    return _panel;
  }

  /**
   * Get the trait icon controller at the given index.
   * @param index Targeted index, starting at 0.
   * @return A controller.
   */
  public TraitIconController getTrait(int index)
  {
    return _traits[index];
  }

  /**
   * Indicates if this display shows the given trait or not.
   * @param trait Targeted trait.
   * @return <code>true</code> if it does, <code>false</code> otherwise.
   */
  public boolean hasTrait(TraitDescription trait)
  {
    for(int i=0;i<MAX_TRAITS;i++)
    {
      if (_traits[i].getTrait()==trait)
      {
        return true;
      }
    }
    return false;
  }

  /**
   * Set the displayed trait.
   * @param index Index of the slot to use.
   * @param trait Trait to set.
   */
  public void setTrait(int index, TraitDescription trait)
  {
    _traits[index].setTrait(trait);
  }

  private JPanel build()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    for(int i=0;i<MAX_TRAITS;i++)
    {
      _traits[i]=new TraitIconController(getWindowController(),null,_characterLevel,true);
      int left=(i>0)?3:0;
      GridBagConstraints c=new GridBagConstraints(i,0,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(0,left,0,0),0,0);
      panel.add(_traits[i].getComponent(),c);
    }
    return panel;
  }

  /**
   * Set status to show.
   * @param status Status to show.
   */
  public void setStatus(TraitSlotsStatus status)
  {
    int nbSlots=status.getSlotsCount();
    for(int i=0;i<nbSlots;i++)
    {
      int traitID=status.getTraitAt(i);
      TraitDescription trait=null;
      if (traitID>0)
      {
        trait=TraitsManager.getInstance().getTrait(traitID);
      }
      setTrait(i,trait);
    }
  }

  /**
   * Get the selected traits.
   * @param status Storage for results.
   */
  public void getSelectedTraits(TraitSlotsStatus status)
  {
    for(int i=0;i<MAX_TRAITS;i++)
    {
      TraitIconController traitController=_traits[i];
      TraitDescription trait=traitController.getTrait();
      int traitID=(trait!=null)?trait.getIdentifier():0;
      status.setTraitAt(i,traitID);
    }
  }

  @Override
  public void dispose()
  {
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    if (_traits!=null)
    {
      for(int i=0;i<MAX_TRAITS;i++)
      {
        if (_traits[i]!=null)
        {
          _traits[i].dispose();
          _traits[i]=null;
        }
      }
      _traits=null;
    }
  }
}
