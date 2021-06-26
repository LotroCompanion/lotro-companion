package delta.games.lotro.gui.stats.deeds.form;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.editors.numbers.ProgressAndNumberEditorController;
import delta.common.ui.swing.text.NumberEditionController;
import delta.common.ui.swing.text.NumberListener;
import delta.common.ui.swing.text.dates.DateCodec;
import delta.common.ui.swing.text.dates.DateEditionController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.achievables.AchievableElementState;
import delta.games.lotro.character.achievables.AchievableObjectiveStatus;
import delta.games.lotro.character.achievables.AchievableStatus;
import delta.games.lotro.character.achievables.AchievableStatusBusinessRules;
import delta.games.lotro.character.achievables.ObjectiveConditionStatus;
import delta.games.lotro.character.achievables.edition.AchievableGeoStatusManager;
import delta.games.lotro.character.achievables.edition.AchievableStatusGeoItem;
import delta.games.lotro.character.achievables.edition.GeoPointChangeListener;
import delta.games.lotro.gui.LotroIconsManager;
import delta.games.lotro.gui.stats.achievables.form.AchievableElementStateEditionController;
import delta.games.lotro.gui.stats.achievables.form.AchievableLinkController;
import delta.games.lotro.gui.stats.achievables.form.ObjectiveConditionStatusEditionPanelController;
import delta.games.lotro.gui.stats.achievables.form.ObjectiveStatusEditionPanelController;
import delta.games.lotro.gui.stats.achievables.map.AchievableGeoStatusEditionController;
import delta.games.lotro.lore.deeds.DeedDescription;
import delta.games.lotro.lore.deeds.DeedType;
import delta.games.lotro.lore.quests.Achievable;
import delta.games.lotro.utils.DateFormat;

/**
 * Controller for a panel to edit the status of an achievable.
 * @author DAM
 */
public class AchievableStatusEditionPanelController implements GeoPointChangeListener
{
  // Data
  private AchievableStatus _status;
  private boolean _editable;
  // Controllers
  private AchievableElementStateEditionController _stateCtrl;
  private List<ObjectiveStatusEditionPanelController> _objectiveStatusEditors;
  private AchievableLinkController _linkCtrl;
  private DateEditionController _completionDate;
  private AchievableGeoStatusEditionController _geoController;
  // UI
  private JPanel _panel;

  /**
   * Constructor.
   * @param parent Parent controller.
   * @param status Status to edit.
   * @param editable Indicates if this component is editable or not.
   */
  public AchievableStatusEditionPanelController(WindowController parent, AchievableStatus status, boolean editable)
  {
    _status=status;
    _editable=editable;
    _panel=build(parent);
    if (_editable)
    {
      setupCallbacks();
    }
    updateOwnUi();
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
    // Objectives panel
    JPanel objectivesPanel=buildObjectivesPanel(icon);
    // Assembly
    JPanel panel=GuiFactory.buildPanel(new BorderLayout());
    panel.add(headPanel,BorderLayout.NORTH);
    JScrollPane center=GuiFactory.buildScrollPane(objectivesPanel);
    center.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    panel.add(center,BorderLayout.CENTER);
    return panel;
  }

  private JPanel buildHeadPanel(Icon icon, WindowController parent)
  {
    // State
    _stateCtrl=new AchievableElementStateEditionController(icon,_editable);
    // Achievable link
    Achievable achievable=_status.getAchievable();
    _linkCtrl=new AchievableLinkController(achievable,parent);
    // Next line (completion date and map button)
    JPanel nextLine=buildDateAndMapPanel(parent);
    // Assembly
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(_stateCtrl.getComponent(),c);
    c=new GridBagConstraints(1,0,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(_linkCtrl.getLabel(),c);
    c=new GridBagConstraints(0,1,2,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(nextLine,c);
    return panel;
  }

  private JPanel buildDateAndMapPanel(WindowController parent)
  {
    JPanel datePanel=buildCompletionDatePanel();
    // Geo edition
    JButton mapButton=buildMapsButton(parent);
    if (mapButton==null)
    {
      return datePanel;
    }
    JPanel panel=GuiFactory.buildPanel(new FlowLayout());
    panel.add(datePanel);
    panel.add(mapButton);
    return panel;
  }

  private JPanel buildCompletionDatePanel()
  {
    JPanel panel=GuiFactory.buildPanel(new FlowLayout());
    panel.add(GuiFactory.buildLabel("Completion date:"));
    DateCodec codec=DateFormat.getDateTimeCodec();
    _completionDate=new DateEditionController(codec);
    panel.add(_completionDate.getTextField());
    return panel;
  }

  private JPanel buildObjectivesPanel(Icon icon)
  {
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,1.0,0.0,GridBagConstraints.EAST,GridBagConstraints.HORIZONTAL,new Insets(2,5,2,5),0,0);
    // Objective editors
    _objectiveStatusEditors=new ArrayList<ObjectiveStatusEditionPanelController>();
    for(AchievableObjectiveStatus objectiveStatus : _status.getObjectiveStatuses())
    {
      ObjectiveStatusEditionPanelController editor=new ObjectiveStatusEditionPanelController(objectiveStatus,icon,_editable);
      _objectiveStatusEditors.add(editor);
      ret.add(editor.getPanel(),c);
      c.gridy++;
    }
    return ret;
  }

  private JButton buildMapsButton(WindowController parent)
  {
    JButton toggleMap=null;
    Achievable achievable=_status.getAchievable();
    boolean hasGeoData=achievable.hasGeoData();
    if (hasGeoData)
    {
      AchievableGeoStatusManager geoStatusManager=new AchievableGeoStatusManager(_status,this);
      _geoController=new AchievableGeoStatusEditionController(parent,geoStatusManager);
      toggleMap=GuiFactory.buildButton("Map");
      ActionListener mapActionListener=new ActionListener()
      {
        @Override
        public void actionPerformed(ActionEvent e)
        {
          _geoController.showMaps();
        }
      };
      toggleMap.addActionListener(mapActionListener);
    }
    return toggleMap;
  }

  @Override
  public void handlePointChange(AchievableStatusGeoItem point, boolean completed)
  {
    updateUi();
  }

  private void updateOwnUi()
  {
    // State
    AchievableElementState state=_status.getState();
    _stateCtrl.setState(state);
    // Date
    _completionDate.setDate(_status.getCompletionDate());
    if (state==AchievableElementState.COMPLETED)
    {
      _completionDate.setState(true,true);
    }
    else
    {
      _completionDate.setState(false,false);
    }
  }

  /**
   * Get all current data on form validation.
   */
  public void onOkImpl()
  {
    // Completion date
    Long completionDate=_completionDate.getDate();
    _status.setCompletionDate(completionDate);
  }

  private Icon getIcon()
  {
    ImageIcon icon=null;
    Achievable achievable=_status.getAchievable();
    if (achievable instanceof DeedDescription)
    {
      DeedDescription deed=(DeedDescription)achievable;
      DeedType type=deed.getType();
      icon=LotroIconsManager.getDeedTypeIcon(type);
    }
    else
    {
      icon=LotroIconsManager.getDeedTypeIcon(DeedType.LORE);
    }
    return icon;
  }

  private void setupCallbacks()
  {
    // Main status icon/button
    JButton achievableButton=_stateCtrl.getButton();
    ActionListener alAchievable=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        handleAchievableClick();
      }
    };
    achievableButton.addActionListener(alAchievable);
    // For each objective: icon/button
    for(final ObjectiveStatusEditionPanelController objectiveCtrl : _objectiveStatusEditors)
    {
      JButton objectiveButton=objectiveCtrl.getStateController().getButton();
      ActionListener alObjective=new ActionListener()
      {
        @Override
        public void actionPerformed(ActionEvent e)
        {
          handleObjectiveClick(objectiveCtrl);
        }
      };
      objectiveButton.addActionListener(alObjective);
      // For each condition: icon/button
      for(final ObjectiveConditionStatusEditionPanelController conditionCtrl : objectiveCtrl.getConditionControllers())
      {
        JButton conditionButton=conditionCtrl.getStateController().getButton();
        ActionListener alCondition=new ActionListener()
        {
          @Override
          public void actionPerformed(ActionEvent e)
          {
            handleConditionClick(conditionCtrl);
          }
        };
        conditionButton.addActionListener(alCondition);
        ProgressAndNumberEditorController countEditor=conditionCtrl.getCountEditor();
        if (countEditor!=null)
        {
          NumberListener<Integer> listener=new NumberListener<Integer>()
          {
            @Override
            public void valueChanged(NumberEditionController<Integer> source, Integer newValue)
            {
              handleConditionCountChange(conditionCtrl,newValue);
            }
          };
          countEditor.getEditor().addValueListener(listener);
        }
      }
    }
  }

  private void handleAchievableClick()
  {
    AchievableElementState nextState=getNextAchievableState(_status.getState());
    AchievableStatusBusinessRules.setAchievableState(nextState,_status);
    updateUi();
  }

  private void handleObjectiveClick(ObjectiveStatusEditionPanelController objectiveController)
  {
    AchievableObjectiveStatus status=objectiveController.getStatus();
    AchievableElementState nextState=getNextObjectiveState(status.getParentStatus(),status.getState());
    AchievableStatusBusinessRules.setObjectiveState(nextState,status);
    updateUi();
  }

  private void handleConditionClick(ObjectiveConditionStatusEditionPanelController conditionCtrl)
  {
    ObjectiveConditionStatus status=conditionCtrl.getStatus();
    AchievableElementState nextState=getNextConditionState(status.getParentStatus(),status.getState());
    AchievableStatusBusinessRules.setConditionState(nextState,status);
    updateUi();
  }

  private void handleConditionCountChange(ObjectiveConditionStatusEditionPanelController conditionCtrl, Integer newValue)
  {
    ObjectiveConditionStatus status=conditionCtrl.getStatus();
    AchievableStatusBusinessRules.setConditionCount(newValue,status);
    updateUi();
  }

  private AchievableElementState getNextAchievableState(AchievableElementState state)
  {
    if (state==AchievableElementState.COMPLETED) return AchievableElementState.UNDEFINED;
    if (state==AchievableElementState.UNDERWAY) return AchievableElementState.COMPLETED;
    if (state==AchievableElementState.UNDEFINED) return AchievableElementState.UNDERWAY;
    return null;
  }

  private AchievableElementState getNextObjectiveState(AchievableStatus parent, AchievableElementState state)
  {
    if (state==AchievableElementState.COMPLETED) return AchievableElementState.UNDERWAY;
    if (state==AchievableElementState.UNDERWAY) return AchievableElementState.COMPLETED;
    if (state==AchievableElementState.UNDEFINED) return AchievableElementState.UNDERWAY;
    return null;
  }

  private AchievableElementState getNextConditionState(AchievableObjectiveStatus parent, AchievableElementState state)
  {
    if (state==AchievableElementState.COMPLETED) return AchievableElementState.UNDERWAY;
    if (state==AchievableElementState.UNDERWAY) return AchievableElementState.COMPLETED;
    if (state==AchievableElementState.UNDEFINED) return AchievableElementState.UNDERWAY;
    return null;
  }

  private void updateUi()
  {
    updateOwnUi();
    for(ObjectiveStatusEditionPanelController objectiveCtrl : _objectiveStatusEditors)
    {
      objectiveCtrl.updateUi();
    }
    if (_geoController!=null)
    {
      _geoController.updateUi();
    }
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
    if (_completionDate!=null)
    {
      _completionDate.dispose();
      _completionDate=null;
    }
    if (_geoController!=null)
    {
      _geoController.dispose();
      _geoController=null;
    }
    // UI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
  }
}
