package delta.games.lotro.gui.lore.traits.form;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.traits.prerequisites.AbstractTraitPrerequisite;
import delta.games.lotro.character.traits.prerequisites.CompoundTraitPrerequisite;
import delta.games.lotro.character.traits.prerequisites.TraitLogicOperator;

/**
 * Controller for a panel to show a compound trait prerequisite.
 * @author DAM
 */
public class CompoundTraitPrerequisitePanelController extends AbstractTraitPrerequisitePanelController
{
  // Controllers
  private List<AbstractTraitPrerequisitePanelController> _childPanels;
  // Data
  private CompoundTraitPrerequisite _prerequisite;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param prerequisite Prerequisite to show.
   */
  public CompoundTraitPrerequisitePanelController(WindowController parent, CompoundTraitPrerequisite prerequisite)
  {
    super(parent);
    _prerequisite=prerequisite;
    _childPanels=new ArrayList<AbstractTraitPrerequisitePanelController>();
    JPanel panel=buildPanel();
    setPanel(panel);
  }

  private JPanel buildPanel()
  {
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    int x=0;
    GridBagConstraints c=new GridBagConstraints(x,0,1,1,0.0,1.0,GridBagConstraints.WEST,GridBagConstraints.VERTICAL,new Insets(0,0,0,0),0,0);
    // Operator
    TraitLogicOperator operator=_prerequisite.getOperator();
    JLabel operatorLabel=GuiFactory.buildLabel(getOperatorLabel(operator));
    c=new GridBagConstraints(x,0,1,1,0.0,1.0,GridBagConstraints.WEST,GridBagConstraints.VERTICAL,new Insets(0,0,0,0),0,0);
    ret.add(operatorLabel,c);
    x++;
    // Filler
    JPanel filler=GuiFactory.buildPanel(null);
    filler.setMinimumSize(new Dimension(5,0));
    filler.setPreferredSize(new Dimension(5,0));
    filler.setMaximumSize(new Dimension(5,Short.MAX_VALUE));
    filler.setBackground(Color.BLACK);
    filler.setOpaque(true);
    c=new GridBagConstraints(x,0,1,1,0.0,1.0,GridBagConstraints.WEST,GridBagConstraints.VERTICAL,new Insets(5,5,5,0),0,0);
    x++;
    ret.add(filler,c);
    // Child prerequisites
    JPanel childPrerequisitesPanel=buildChildPrerequisitesPanel();
    c=new GridBagConstraints(x,0,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    x++;
    ret.add(childPrerequisitesPanel,c);
    return ret;
  }

  private JPanel buildChildPrerequisitesPanel()
  {
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    WindowController parent=getParentWindowController();
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    for(AbstractTraitPrerequisite prerequisite : _prerequisite.getPrerequisites())
    {
      AbstractTraitPrerequisitePanelController ctrl=TraitPrerequisitesPanelFactory.buildTraitRequisitePanelController(parent,prerequisite);
      _childPanels.add(ctrl);
      ret.add(ctrl.getPanel(),c);
      c.gridy++;
    }
    return ret;
  }

  private String getOperatorLabel(TraitLogicOperator operator)
  {
    if (operator==TraitLogicOperator.ALL_OF) return "All Of";
    if (operator==TraitLogicOperator.ONE_OF) return "One Of";
    if (operator==TraitLogicOperator.NONE_OF) return "None Of";
    return "";
  }

  @Override
  public void dispose()
  {
    super.dispose();
    // Controllers
    if (_childPanels!=null)
    {
      for(AbstractTraitPrerequisitePanelController ctrl : _childPanels)
      {
        ctrl.dispose();
      }
      _childPanels=null;
    }
  }
}
