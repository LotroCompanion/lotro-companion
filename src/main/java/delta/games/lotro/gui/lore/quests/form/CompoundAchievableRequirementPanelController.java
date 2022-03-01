package delta.games.lotro.gui.lore.quests.form;

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
import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.common.utils.collections.filters.Operator;
import delta.games.lotro.common.requirements.AbstractAchievableRequirement;
import delta.games.lotro.common.requirements.CompoundQuestRequirement;

/**
 * Controller for a panel to show a simple achievable requirement.
 * @author DAM
 */
public class CompoundAchievableRequirementPanelController extends AbstractAchievableRequirementPanelController
{
  // Controllers
  private NavigatorWindowController _parent;
  private List<AbstractAchievableRequirementPanelController> _childPanels;
  // UI
  private JPanel _panel;
  // Data
  private CompoundQuestRequirement _requirement;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param requirement Requirement to show.
   */
  public CompoundAchievableRequirementPanelController(NavigatorWindowController parent, CompoundQuestRequirement requirement)
  {
    _parent=parent;
    _requirement=requirement;
    _childPanels=new ArrayList<AbstractAchievableRequirementPanelController>();
    _panel=buildPanel();
  }

  /**
   * Get the managed panel.
   * @return A panel.
   */
  public JPanel getPanel()
  {
    return _panel;
  }

  private JPanel buildPanel()
  {
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    // Operator
    Operator operator=_requirement.getOperator();
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,1.0,GridBagConstraints.WEST,GridBagConstraints.VERTICAL,new Insets(0,0,0,0),0,0);
    JLabel operatorLabel=GuiFactory.buildLabel(operator.name());
    ret.add(operatorLabel,c);
    // Filler
    JPanel filler=GuiFactory.buildPanel(null);
    filler.setMinimumSize(new Dimension(5,0));
    filler.setPreferredSize(new Dimension(5,0));
    filler.setMaximumSize(new Dimension(5,Short.MAX_VALUE));
    filler.setBackground(Color.BLACK);
    filler.setOpaque(true);
    c=new GridBagConstraints(1,0,1,1,0.0,1.0,GridBagConstraints.WEST,GridBagConstraints.VERTICAL,new Insets(5,5,5,0),0,0);
    ret.add(filler,c);
    // Child requirements
    JPanel childRequirementsPanel=buildChildRequirementsPanel();
    c=new GridBagConstraints(2,0,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    ret.add(childRequirementsPanel,c);
    return ret;
  }

  private JPanel buildChildRequirementsPanel()
  {
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    for(AbstractAchievableRequirement requirement : _requirement.getRequirements())
    {
      AbstractAchievableRequirementPanelController ctrl=AchievableRequirementsPanelFactory.buildAchievableRequirementPanelController(_parent,requirement);
      _childPanels.add(ctrl);
      ret.add(ctrl.getPanel(),c);
      c.gridy++;
    }
    return ret;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Controllers
    _parent=null;
    if (_childPanels!=null)
    {
      for(AbstractAchievableRequirementPanelController ctrl : _childPanels)
      {
        ctrl.dispose();
      }
      _childPanels=null;
    }
    // UI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
  }
}
