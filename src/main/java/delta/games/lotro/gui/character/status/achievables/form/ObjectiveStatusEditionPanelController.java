package delta.games.lotro.gui.character.status.achievables.form;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.area.AreaController;
import delta.common.ui.swing.panels.AbstractPanelController;
import delta.games.lotro.character.status.achievables.AchievableElementState;
import delta.games.lotro.character.status.achievables.AchievableObjectiveStatus;
import delta.games.lotro.character.status.achievables.ObjectiveConditionStatus;
import delta.games.lotro.gui.character.status.achievables.AchievableUIMode;
import delta.games.lotro.lore.quests.objectives.Objective;

/**
 * Controller for a panel to edit the status of an objective of an achievable.
 * @author DAM
 */
public class ObjectiveStatusEditionPanelController extends AbstractPanelController
{
  // Data
  private AchievableObjectiveStatus _objectiveStatus;
  private AchievableFormConfig _config;
  // Controllers
  private AchievableElementStateEditionController _stateCtrl;
  private List<ObjectiveConditionStatusEditionPanelController> _conditionStatusEditors;
  // UI
  private JLabel _label;

  /**
   * Constructor.
   * @param parent Parent controller.
   * @param objectiveStatus Status to edit.
   * @param icon Icon to use.
   * @param config UI configuration.
   */
  public ObjectiveStatusEditionPanelController(AreaController parent, AchievableObjectiveStatus objectiveStatus, Icon icon, AchievableFormConfig config)
  {
    super(parent);
    _objectiveStatus=objectiveStatus;
    _config=config;
    JPanel panel=build(icon);
    setPanel(panel);
    setStatus();
  }

  /**
   * Get the managed status.
   * @return the managed status.
   */
  public AchievableObjectiveStatus getStatus()
  {
    return _objectiveStatus;
  }

  /**
   * Get the state controller.
   * @return the state controller.
   */
  public AchievableElementStateEditionController getStateController()
  {
    return _stateCtrl;
  }

  /**
   * Get the managed condition controllers.
   * @return a list of condition controllers.
   */
  public List<ObjectiveConditionStatusEditionPanelController> getConditionControllers()
  {
    return _conditionStatusEditors;
  }

  /**
   * Update UI from data.
   */
  public void updateUi()
  {
    _stateCtrl.setState(_objectiveStatus.getState());
    for(ObjectiveConditionStatusEditionPanelController conditionCtrl : _conditionStatusEditors)
    {
      conditionCtrl.updateUi();
    }
    updateLabel();
  }

  private void updateLabel()
  {
    int completed=0;
    int max=0;
    for(ObjectiveConditionStatus conditionStatus : _objectiveStatus.getConditionStatuses())
    {
      AchievableElementState status=conditionStatus.getState();
      if (status==AchievableElementState.COMPLETED)
      {
        completed++;
      }
      max++;
    }
    String templateLabel=getLabel();
    String resultLabel=templateLabel.replace("${CURRENT}",String.valueOf(completed));
    resultLabel=resultLabel.replace("${MAX}",String.valueOf(max));
    _label.setText(resultLabel);
  }

  private JPanel build(Icon icon)
  {
    // Head panel
    JPanel headPanel=buildHeadPanel(icon);
    // Condition editors
    _conditionStatusEditors=new ArrayList<ObjectiveConditionStatusEditionPanelController>();
    for(ObjectiveConditionStatus conditionStatus : _objectiveStatus.getConditionStatuses())
    {
      ObjectiveConditionStatusEditionPanelController editor=new ObjectiveConditionStatusEditionPanelController(this,conditionStatus,icon,_config);
      _conditionStatusEditors.add(editor);
    }
    // Assembly
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,1.0,0.0,GridBagConstraints.EAST,GridBagConstraints.HORIZONTAL,new Insets(2,5,2,5),0,0);
    panel.add(headPanel,c);
    c.gridy++;
    for(ObjectiveConditionStatusEditionPanelController editor : _conditionStatusEditors)
    {
      c.gridy++;
      JPanel editorPanel=editor.getPanel();
      panel.add(editorPanel,c);
    }
    return panel;
  }

  private JPanel buildHeadPanel(Icon icon)
  {
    // State
    _stateCtrl=new AchievableElementStateEditionController(icon,_config);
    // Label
    String label=getLabel();
    _label=GuiFactory.buildLabel("");
    updateLabel();
    // Assembly
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.EAST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    panel.add(_stateCtrl.getComponent(),c);
    c=new GridBagConstraints(1,0,1,1,1.0,0.0,GridBagConstraints.EAST,GridBagConstraints.HORIZONTAL,new Insets(5,0,5,5),0,0);
    panel.add(_label,c);
    if (label.length()==0)
    {
      panel.setVisible(false);
    }
    return panel;
  }

  private void setStatus()
  {
    // State
    _stateCtrl.setState(_objectiveStatus.getState());
  }

  private String getLabel()
  {
    Objective objective=_objectiveStatus.getObjective();
    String objectiveOverride=objective.getProgressOverride();
    String ret=objectiveOverride;
    if (_config.getMode()==AchievableUIMode.QUEST)
    {
      ret="Objective #"+objective.getIndex();
      if (objectiveOverride.length()>0)
      {
        ret=ret+": "+objectiveOverride;
      }
    }
    return ret;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    super.dispose();
    // Data
    _objectiveStatus=null;
    _config=null;
    // Controllers
    if (_stateCtrl!=null)
    {
      _stateCtrl.dispose();
      _stateCtrl=null;
    }
    if (_conditionStatusEditors!=null)
    {
      for(ObjectiveConditionStatusEditionPanelController ctrl : _conditionStatusEditors)
      {
        ctrl.dispose();
      }
      _conditionStatusEditors.clear();
      _conditionStatusEditors=null;
    }
    // UI
    _label=null;
  }
}
