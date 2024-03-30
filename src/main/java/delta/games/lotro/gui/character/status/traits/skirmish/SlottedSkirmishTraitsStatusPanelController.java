package delta.games.lotro.gui.character.status.traits.skirmish;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.icons.IconsManager;
import delta.common.ui.swing.panels.AbstractPanelController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.status.traits.skirmish.SkirmishTraitsStatus;
import delta.games.lotro.character.traits.TraitDescription;
import delta.games.lotro.character.traits.TraitsManager;
import delta.games.lotro.common.enums.TraitNature;
import delta.games.lotro.gui.utils.IconController;
import delta.games.lotro.gui.utils.IconControllerFactory;

/**
 * Panel to show slotted skirmish traits.
 * @author DAM
 */
public class SlottedSkirmishTraitsStatusPanelController extends AbstractPanelController
{
  // Data
  private SkirmishTraitsStatus _status;
  // Controllers
  private List<IconController> _controllers;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param status Status to show.
   */
  public SlottedSkirmishTraitsStatusPanelController(WindowController parent, SkirmishTraitsStatus status)
  {
    super(parent);
    _status=status;
    _controllers=new ArrayList<IconController>();
    setPanel(buildPanel(parent));
  }

  private JPanel buildPanel(WindowController parent)
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    int y=0;
    for(TraitNature nature : SkirmishTraitStatusUiUtils.getOrderedTraitNature())
    {
      fillNature(parent,panel,nature,y);
      y+=2;
    }
    return panel;
  }

  private void fillNature(WindowController parent, JPanel panel, TraitNature nature, int baseY)
  {
    ImageIcon borderIcon=IconsManager.getIcon("/resources/gui/traits/border.png");

    TraitsManager mgr=TraitsManager.getInstance();
    int[] slottedTraits=_status.getSlottedTraits(nature);
    int nbSlots=slottedTraits.length;
    String natureLabel=nature.getLabel();
    GridBagConstraints c=new GridBagConstraints(0,baseY,nbSlots,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(5,5,2,5),0,0);
    panel.add(GuiFactory.buildLabel(natureLabel),c);
    for(int i=0;i<nbSlots;i++)
    {
      int left=(i==0)?5:0;
      c=new GridBagConstraints(i,baseY+1,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(2,left,2,5),0,0);
      if (slottedTraits[i]!=0)
      {
        TraitDescription trait=mgr.getTrait(slottedTraits[i]);
        int rank=_status.getTraitRank(slottedTraits[i]);
        IconController iconCtrl=IconControllerFactory.buildSkirmishTraitIcon(parent,trait,rank);
        _controllers.add(iconCtrl);
        JLayeredPane layeredPane=SkirmishTraitStatusUiUtils.buildBorderAndIconPanel(borderIcon,iconCtrl);
        panel.add(layeredPane,c);
      }
      else
      {
        JLabel borderLabel=GuiFactory.buildIconLabel(borderIcon);
        panel.add(borderLabel,c);
      }
    }
  }

  @Override
  public void dispose()
  {
    super.dispose();
    _status=null;
    if (_controllers!=null)
    {
      for(IconController ctrl : _controllers)
      {
        ctrl.dispose();
      }
      _controllers.clear();
      _controllers=null;
    }
  }
}
