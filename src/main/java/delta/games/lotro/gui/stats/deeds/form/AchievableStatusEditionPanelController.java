package delta.games.lotro.gui.stats.deeds.form;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.games.lotro.character.achievables.AchievableObjectiveStatus;
import delta.games.lotro.character.achievables.AchievableStatus;
import delta.games.lotro.lore.quests.Achievable;

/**
 * Controller for a panel to edit the status of an achievable.
 * @author DAM
 */
public class AchievableStatusEditionPanelController
{
  private AchievableStatus _status;
  private AchievableElementStateEditionController _stateCtrl;
  private List<ObjectiveStatusEditionPanelController> _objectiveStatusEditors;
  private JLabel _label;
  private JPanel _panel;

  /**
   * Constructor.
   * @param status Status to edit.
   */
  public AchievableStatusEditionPanelController(AchievableStatus status)
  {
    _status=status;
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
    // Head panel
    JPanel headPanel=buildHeadPanel();
    // Condition editors
    _objectiveStatusEditors=new ArrayList<ObjectiveStatusEditionPanelController>();
    for(AchievableObjectiveStatus objectiveStatus : _status.getObjectiveStatuses())
    {
      ObjectiveStatusEditionPanelController editor=new ObjectiveStatusEditionPanelController(objectiveStatus);
      _objectiveStatusEditors.add(editor);
    }
    // Assembly
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,1.0,0.0,GridBagConstraints.EAST,GridBagConstraints.HORIZONTAL,new Insets(2,5,2,5),0,0);
    panel.add(headPanel,c);
    c.gridy++;
    for(ObjectiveStatusEditionPanelController editor : _objectiveStatusEditors)
    {
      c.gridy++;
      JPanel editorPanel=editor.getPanel();
      panel.add(editorPanel,c);
    }
    return panel;
  }

  private JPanel buildHeadPanel()
  {
    // State
    _stateCtrl=new AchievableElementStateEditionController();
    // Label
    String label=getLabel();
    _label=GuiFactory.buildLabel(label);
    // Assembly
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.EAST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(_stateCtrl.getComponent(),c);
    c=new GridBagConstraints(1,0,1,1,1.0,0.0,GridBagConstraints.EAST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(_label,c);
    return panel;
  }

  private void setStatus()
  {
    _stateCtrl.setState(_status.getState());
  }

  private String getLabel()
  {
    Achievable achievable=_status.getAchievable();
    String label=achievable.getName();
    return label;
  }
}
