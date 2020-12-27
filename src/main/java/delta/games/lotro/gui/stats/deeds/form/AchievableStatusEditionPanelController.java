package delta.games.lotro.gui.stats.deeds.form;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.achievables.AchievableObjectiveStatus;
import delta.games.lotro.character.achievables.AchievableStatus;
import delta.games.lotro.gui.LotroIconsManager;
import delta.games.lotro.lore.deeds.DeedDescription;
import delta.games.lotro.lore.deeds.DeedType;

/**
 * Controller for a panel to edit the status of an achievable.
 * @author DAM
 */
public class AchievableStatusEditionPanelController
{
  private AchievableStatus _status;
  private AchievableElementStateEditionController _stateCtrl;
  private List<ObjectiveStatusEditionPanelController> _objectiveStatusEditors;
  private DeedLinkController _linkCtrl;
  private JPanel _panel;

  /**
   * Constructor.
   * @param parent Parent controller.
   * @param status Status to edit.
   */
  public AchievableStatusEditionPanelController(WindowController parent, AchievableStatus status)
  {
    _status=status;
    _panel=build(parent);
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

  private JPanel build(WindowController parent)
  {
    // Icon
    Icon icon=getIcon();
    // Head panel
    JPanel headPanel=buildHeadPanel(icon,parent);
    // Condition editors
    _objectiveStatusEditors=new ArrayList<ObjectiveStatusEditionPanelController>();
    for(AchievableObjectiveStatus objectiveStatus : _status.getObjectiveStatuses())
    {
      ObjectiveStatusEditionPanelController editor=new ObjectiveStatusEditionPanelController(objectiveStatus,icon);
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

  private JPanel buildHeadPanel(Icon icon, WindowController parent)
  {
    // State
    _stateCtrl=new AchievableElementStateEditionController(icon);
    // Deed link
    DeedDescription deed=(DeedDescription)_status.getAchievable();
    _linkCtrl=new DeedLinkController(deed,parent);
    // Assembly
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.EAST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(_stateCtrl.getComponent(),c);
    c=new GridBagConstraints(1,0,1,1,1.0,0.0,GridBagConstraints.EAST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(_linkCtrl.getLabel(),c);
    return panel;
  }

  private void setStatus()
  {
    _stateCtrl.setState(_status.getState());
  }

  private Icon getIcon()
  {
    DeedDescription deed=(DeedDescription)_status.getAchievable();
    DeedType type=deed.getType();
    ImageIcon icon=LotroIconsManager.getDeedTypeIcon(type);
    return icon;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Data
    _status=null;
    // Controllers
    if (_stateCtrl!=null)
    {
      _stateCtrl.dispose();
      _stateCtrl=null;
    }
    if (_objectiveStatusEditors!=null)
    {
      for(ObjectiveStatusEditionPanelController ctrl : _objectiveStatusEditors)
      {
        ctrl.dispose();
      }
      _objectiveStatusEditors.clear();
      _objectiveStatusEditors=null;
    }
    if (_linkCtrl!=null)
    {
      _linkCtrl.dispose();
      _linkCtrl=null;
    }
    // UI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
  }
}
