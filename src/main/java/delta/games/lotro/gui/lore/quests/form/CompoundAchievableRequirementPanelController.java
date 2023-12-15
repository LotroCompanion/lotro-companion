package delta.games.lotro.gui.lore.quests.form;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.icons.IconsManager;
import delta.common.ui.swing.windows.WindowController;
import delta.common.utils.collections.filters.Operator;
import delta.games.lotro.character.status.achievables.QuestRequirementStateComputer;
import delta.games.lotro.common.requirements.AbstractAchievableRequirement;
import delta.games.lotro.common.requirements.CompoundQuestRequirement;

/**
 * Controller for a panel to show a simple achievable requirement.
 * @author DAM
 */
public class CompoundAchievableRequirementPanelController extends AbstractAchievableRequirementPanelController
{
  // Controllers
  private List<AbstractAchievableRequirementPanelController> _childPanels;
  // Data
  private CompoundQuestRequirement _requirement;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param computer State computer.
   * @param requirement Requirement to show.
   */
  public CompoundAchievableRequirementPanelController(WindowController parent, QuestRequirementStateComputer computer, CompoundQuestRequirement requirement)
  {
    super(parent,computer);
    _requirement=requirement;
    _childPanels=new ArrayList<AbstractAchievableRequirementPanelController>();
    JPanel panel=buildPanel();
    setPanel(panel);
  }

  private JPanel buildPanel()
  {
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    int x=0;
    GridBagConstraints c=new GridBagConstraints(x,0,1,1,0.0,1.0,GridBagConstraints.WEST,GridBagConstraints.VERTICAL,new Insets(0,0,0,0),0,0);
    if (_stateComputer!=null)
    {
      boolean ok=_stateComputer.assess(_requirement);
      Icon stateIcon=IconsManager.getIcon("/resources/gui/icons/state/"+(ok?"ok":"ko")+".png");
      JLabel icon=GuiFactory.buildIconLabel(stateIcon);
      ret.add(icon,c);
      x++;
    }
    // Operator
    Operator operator=_requirement.getOperator();
    JLabel operatorLabel=GuiFactory.buildLabel(operator.name());
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
    // Child requirements
    JPanel childRequirementsPanel=buildChildRequirementsPanel();
    c=new GridBagConstraints(x,0,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    x++;
    ret.add(childRequirementsPanel,c);
    return ret;
  }

  private JPanel buildChildRequirementsPanel()
  {
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    WindowController parent=getParentWindowController();
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    for(AbstractAchievableRequirement requirement : _requirement.getRequirements())
    {
      AbstractAchievableRequirementPanelController ctrl=AchievableRequirementsPanelFactory.buildAchievableRequirementPanelController(parent,_stateComputer,requirement);
      _childPanels.add(ctrl);
      ret.add(ctrl.getPanel(),c);
      c.gridy++;
    }
    return ret;
  }

  @Override
  public void dispose()
  {
    super.dispose();
    // Controllers
    if (_childPanels!=null)
    {
      for(AbstractAchievableRequirementPanelController ctrl : _childPanels)
      {
        ctrl.dispose();
      }
      _childPanels=null;
    }
  }
}
