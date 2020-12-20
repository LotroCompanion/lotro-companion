package delta.games.lotro.gui.stats.deeds.form;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.editors.numbers.TextFieldAndMaxLabel;
import delta.games.lotro.character.achievables.ObjectiveConditionStatus;
import delta.games.lotro.lore.quests.objectives.ObjectiveCondition;
import delta.games.lotro.lore.quests.objectives.ObjectivesConstants;

/**
 * Controller for a panel to edit the status of a condition of an objective.
 * @author DAM
 */
public class ObjectiveConditionStatusEditionPanelController
{
  private ObjectiveConditionStatus _conditionStatus;
  private AchievableElementStateEditionController _stateCtrl;
  private TextFieldAndMaxLabel _countEditor;
  private JLabel _label;
  private JPanel _panel;

  /**
   * Constructor.
   * @param conditionStatus Status to edit.
   */
  public ObjectiveConditionStatusEditionPanelController(ObjectiveConditionStatus conditionStatus)
  {
    _conditionStatus=conditionStatus;
    _panel=build();
    setStatus();
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
    // State
    _stateCtrl=new AchievableElementStateEditionController();
    // Label
    String label=getLabel();
    _label=GuiFactory.buildLabel(label);
    // Count?
    ObjectiveCondition condition=_conditionStatus.getCondition();
    int count=condition.getCount();
    if (count>1)
    {
      _countEditor=new TextFieldAndMaxLabel();
      _countEditor.setMax(count);
    }
    // Assembly
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.EAST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(_stateCtrl.getComponent(),c);
    c=new GridBagConstraints(1,0,1,1,1.0,0.0,GridBagConstraints.EAST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(_label,c);
    if (_countEditor!=null)
    {
      c=new GridBagConstraints(0,1,2,1,1.0,0.0,GridBagConstraints.EAST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
      panel.add(_countEditor.getPanel(),c);
    }
    return panel;
  }

  private void setStatus()
  {
    // State
    _stateCtrl.setState(_conditionStatus.getState());
    // Count
    Integer count=_conditionStatus.getCount();
    if ((_countEditor!=null) && (count!=null))
    {
      _countEditor.setValue(count);
      updateCount();
    }
  }

  private String getLabel()
  {
    ObjectiveCondition condition=_conditionStatus.getCondition();
    String label=condition.getProgressOverride();
    if ((label==null) || (label.length()==0))
    {
      label=AchievableStatusUtils.getConditionLabel(condition);
    }
    return label;
  }

  private void updateCount()
  {
    if (_countEditor!=null)
    {
      Integer value=_countEditor.getValue();
      if (value!=null)
      {
        ObjectiveCondition condition=_conditionStatus.getCondition();
        int count=condition.getCount();
        String countStatus=value.intValue()+"/"+count;
        String templateLabel=getLabel();
        String resultLabel=templateLabel.replace(ObjectivesConstants.COUNT_PATTERN,countStatus);
        _label.setText(resultLabel);
      }
    }
  }
}
