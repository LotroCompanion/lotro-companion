package delta.games.lotro.gui.character.status.traits.mountedAppearances;

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
 * Controller for a panel to display the slotted mounted appearances traits.
 * @author DAM
 */
public class MountedAppearancesSlotsDisplayPanelController extends AbstractPanelController
{
  /**
   * Maximum number of slotted traits.
   */
  public static final int MAX_TRAITS=7;
  private TraitIconController[] _traits;
  private JPanel _panel;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param status Status to show.
   */
  public MountedAppearancesSlotsDisplayPanelController(WindowController parent, TraitSlotsStatus status)
  {
    super(parent);
    _traits=new TraitIconController[MAX_TRAITS];
    _panel=build();
    setTraits(status);
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
    for(int i=0;i<MAX_TRAITS;i++)
    {
      _traits[i]=new TraitIconController(getWindowController(),null,1,true);
      int left=(i>0)?3:0;
      GridBagConstraints c=new GridBagConstraints(i,0,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(0,left,0,0),0,0);
      panel.add(_traits[i].getComponent(),c);
    }
    return panel;
  }

  /**
   * Set the status to show.
   * @param status Status to show.
   */
  private void setTraits(TraitSlotsStatus status)
  {
    int nbTraits=status.getSlotsCount();
    for(int i=0;i<MAX_TRAITS;i++)
    {
      int traitID=(i<nbTraits)?status.getTraitAt(i):0;
      TraitDescription trait=null;
      if (traitID!=0)
      {
        trait=TraitsManager.getInstance().getTrait(traitID);
      }
      _traits[i].setTrait(trait);
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
