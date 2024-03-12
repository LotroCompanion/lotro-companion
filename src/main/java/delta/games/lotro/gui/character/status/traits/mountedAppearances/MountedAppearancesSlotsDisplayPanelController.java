package delta.games.lotro.gui.character.status.traits.mountedAppearances;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.labels.HyperLinkController;
import delta.common.ui.swing.navigator.PageIdentifier;
import delta.common.ui.swing.panels.AbstractPanelController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.status.traits.shared.TraitSlotsStatus;
import delta.games.lotro.character.traits.TraitDescription;
import delta.games.lotro.character.traits.TraitsManager;
import delta.games.lotro.common.enums.LotroEnum;
import delta.games.lotro.common.enums.LotroEnumsRegistry;
import delta.games.lotro.common.enums.TraitGroup;
import delta.games.lotro.gui.common.navigation.ReferenceConstants;
import delta.games.lotro.gui.lore.traits.TraitIconController;
import delta.games.lotro.gui.utils.NavigationUtils;

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
  private HyperLinkController[] _links;
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
    _links=new HyperLinkController[MAX_TRAITS];
    _panel=build(status);
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  public JPanel getPanel()
  {
    return _panel;
  }

  private JPanel build(TraitSlotsStatus status)
  {
    WindowController parent=getWindowController();
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    List<TraitGroup> groups=getTraitGroupsForSlots();
    int nbTraits=status.getSlotsCount();
    for(int i=0;i<MAX_TRAITS;i++)
    {
      int traitID=(i<nbTraits)?status.getTraitAt(i):0;
      TraitDescription trait=null;
      if (traitID!=0)
      {
        trait=TraitsManager.getInstance().getTrait(traitID);
      }
      // Trait group
      TraitGroup group=groups.get(i);
      JLabel groupLabel=GuiFactory.buildLabel(group.getLabel());
      GridBagConstraints c=new GridBagConstraints(0,i*2,2,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
      panel.add(groupLabel,c);
      // Icon
      _traits[i]=new TraitIconController(parent,trait,1,true);
      c=new GridBagConstraints(0,i*2+1,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
      panel.add(_traits[i].getComponent(),c);
      // Link
      if (trait!=null)
      {
        PageIdentifier pageId=ReferenceConstants.getTraitReference(trait.getIdentifier());
        String text=trait.getName();
        _links[i]=NavigationUtils.buildNavigationLink(parent,text,pageId);
        c=new GridBagConstraints(1,i*2+1,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
        panel.add(_links[i].getLabel(),c);
      }
    }
    return panel;
  }

  private static final int[] SLOT_CODES={
      8,  // HEAD
      9,  // BODY
      12, // SADDLE,
      13, // GEAR
      10, // LEFS
      11, // TAIL
      14  // HIDE
  };

  private List<TraitGroup> getTraitGroupsForSlots()
  {
    List<TraitGroup> ret=new ArrayList<TraitGroup>();
    LotroEnum<TraitGroup> traitGroupEnum=LotroEnumsRegistry.getInstance().get(TraitGroup.class);
    for(int code : SLOT_CODES)
    {
      TraitGroup group=traitGroupEnum.getEntry(code);
      ret.add(group);
    }
    return ret;
  }

  // UI order: 
  /*
    9 // BODY
    13 // GEAR
    8 // HEAD
    14 // HIDE
    10 // LEGS
    12 // SADDLE
    11 // TAIL
   */

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
        if (_links[i]!=null)
        {
          _links[i].dispose();
          _links[i]=null;
        }
      }
      _traits=null;
    }
  }
}
