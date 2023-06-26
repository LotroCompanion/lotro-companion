package delta.games.lotro.gui.character.status.achievables.form;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.area.AreaController;
import delta.common.ui.swing.editors.numbers.ProgressAndNumberEditorController;
import delta.common.ui.swing.panels.AbstractPanelController;
import delta.games.lotro.character.status.achievables.ObjectiveConditionStatus;
import delta.games.lotro.lore.quests.objectives.ObjectiveCondition;

/**
 * Controller for a panel to edit the status of a condition of an objective.
 * @author DAM
 */
public class ObjectiveConditionStatusEditionPanelController extends AbstractPanelController
{
  // Data
  private ObjectiveConditionStatus _conditionStatus;
  private AchievableFormConfig _config;
  // Controllers
  private AchievableElementStateEditionController _stateCtrl;
  private ProgressAndNumberEditorController _countEditor;
  // UI
  private JLabel _label;
  // Utils
  private AchievableStatusUtils _utils;

  /**
   * Constructor.
   * @param parent Parent controller.
   * @param conditionStatus Status to edit.
   * @param icon Icon to use.
   * @param config UI configuration.
   */
  public ObjectiveConditionStatusEditionPanelController(AreaController parent, ObjectiveConditionStatus conditionStatus, Icon icon, AchievableFormConfig config)
  {
    super(parent);
    _conditionStatus=conditionStatus;
    _config=config;
    _utils=new AchievableStatusUtils(null);
    JPanel panel=build(icon);
    setPanel(panel);
    updateUi();
  }

  /**
   * Get the managed status.
   * @return the managed status.
   */
  public ObjectiveConditionStatus getStatus()
  {
    return _conditionStatus;
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
   * Get the count editor.
   * @return the count editor.
   */
  public ProgressAndNumberEditorController getCountEditor()
  {
    return _countEditor;
  }

  private JPanel build(Icon icon)
  {
    // State
    _stateCtrl=new AchievableElementStateEditionController(icon,_config);
    // Label
    String label=getLabel();
    _label=GuiFactory.buildLabel(label);
    // Count?
    ObjectiveCondition condition=_conditionStatus.getCondition();
    int count=condition.getCount();
    if (count>1)
    {
      _countEditor=new ProgressAndNumberEditorController();
      _countEditor.setRange(0,count);
      _countEditor.getEditor().setState(true,_config.isEditable());
    }
    // Assembly
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.EAST,GridBagConstraints.NONE,new Insets(2,5,2,2),0,0);
    panel.add(_stateCtrl.getComponent(),c);
    c=new GridBagConstraints(1,0,1,1,1.0,0.0,GridBagConstraints.EAST,GridBagConstraints.HORIZONTAL,new Insets(2,2,2,5),0,0);
    panel.add(_label,c);
    if (_countEditor!=null)
    {
      c=new GridBagConstraints(0,1,2,1,1.0,0.0,GridBagConstraints.EAST,GridBagConstraints.HORIZONTAL,new Insets(0,5,2,5),0,0);
      panel.add(_countEditor.getPanel(),c);
    }
    // Compute the panel visibility
    boolean showProgress=condition.isShowProgressText();
    boolean showBillboard=condition.isShowBillboardText();
    String progressOverride=condition.getProgressOverride();
    boolean hasProgressOverride=((progressOverride!=null) && (progressOverride.length()>0));
    boolean visible=((showProgress&&showBillboard)||hasProgressOverride);
    panel.setVisible(visible);
    return panel;
  }

  /**
   * Update UI from data.
   */
  public void updateUi()
  {
    // State
    _stateCtrl.setState(_conditionStatus.getState());
    // Count
    Integer count=_conditionStatus.getCount();
    if ((_countEditor!=null) && (count!=null))
    {
      _countEditor.setValue(count.intValue());
      updateCount();
    }
  }

  private String getLabel()
  {
    ObjectiveCondition condition=_conditionStatus.getCondition();
    String label=_utils.getConditionLabel(condition);
    return label;
  }

  /**
   * Update count display.
   */
  public void updateCount()
  {
    Integer value=_countEditor.getValue();
    if (value!=null)
    {
      ObjectiveCondition condition=_conditionStatus.getCondition();
      int count=condition.getCount();
      String templateLabel=getLabel();
      String resultLabel=templateLabel.replace("${NUMBER}",value.toString());
      resultLabel=resultLabel.replace("${TOTAL}",String.valueOf(count));
      _label.setText(resultLabel);
    }
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    super.dispose();
    // Data
    _conditionStatus=null;
    _config=null;
    // Controllers
    if (_stateCtrl!=null)
    {
      _stateCtrl.dispose();
      _stateCtrl=null;
    }
    if (_countEditor!=null)
    {
      _countEditor.dispose();
      _countEditor=null;
    }
    // UI
    _label=null;
    // Utils
    _utils=null;
  }
}
