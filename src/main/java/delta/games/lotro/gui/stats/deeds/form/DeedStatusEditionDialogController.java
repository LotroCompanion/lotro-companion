package delta.games.lotro.gui.stats.deeds.form;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.checkbox.ThreeState;
import delta.common.ui.swing.checkbox.ThreeStateCheckbox;
import delta.common.ui.swing.text.dates.DateCodec;
import delta.common.ui.swing.text.dates.DateEditionController;
import delta.common.ui.swing.windows.DefaultFormDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.achievables.AchievableElementState;
import delta.games.lotro.character.achievables.AchievableStatus;
import delta.games.lotro.lore.deeds.DeedDescription;
import delta.games.lotro.lore.quests.Achievable;
import delta.games.lotro.utils.DateFormat;

/**
 * Controller for the "deed status edition" dialog.
 * @author DAM
 */
public class DeedStatusEditionDialogController extends DefaultFormDialogController<AchievableStatus>
{
  // Data
  private DeedDescription _deed;
  // Controllers
  private ThreeStateCheckbox _completed;
  private DateEditionController _completionDate;
  private DeedSummaryPanelController _summaryPanel;
  private DeedGeoStatusEditionPanelController _geoEditor;

  /**
   * Constructor.
   * @param parentController Parent controller.
   * @param status Status to edit.
   */
  public DeedStatusEditionDialogController(AchievableStatus status, WindowController parentController)
  {
    super(parentController,status);
    _deed=(DeedDescription)status.getAchievable();
    _summaryPanel=new DeedSummaryPanelController(_deed,this);
  }

  @Override
  protected JDialog build()
  {
    JDialog dialog=super.build();
    dialog.setTitle("Deed status edition...");
    dialog.setResizable(false);
    return dialog;
  }

  @Override
  protected JPanel buildFormPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    TitledBorder pathsBorder=GuiFactory.buildTitledBorder("Deed status");
    panel.setBorder(pathsBorder);

    GridBagConstraints c=new GridBagConstraints(0,0,2,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    // Deed summary line
    JPanel deedSummaryLine=_summaryPanel.getPanel();
    panel.add(deedSummaryLine,c);
    c.gridy++;

    // Completed
    _completed=new ThreeStateCheckbox("Completed");
    ActionListener l=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        boolean state=(_completed.isSelected());
        _completionDate.setState(state,state);
      }
    };
    _completed.addActionListener(l);
    // Completion date
    DateCodec codec=DateFormat.getDateTimeCodec();
    _completionDate=new DateEditionController(codec);

    Insets insets=new Insets(5,5,5,5);
    GridBagConstraints gbc=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,insets,0,0);
    gbc.gridx=0; gbc.gridy=1;
    panel.add(_completed,gbc);
    gbc.gridx=0; gbc.gridy=2;
    panel.add(GuiFactory.buildLabel("Completion date:"),gbc);
    gbc.weightx=1.0; gbc.fill=GridBagConstraints.HORIZONTAL;
    gbc.gridx=1;
    panel.add(_completionDate.getTextField(),gbc);

    Achievable achievable=_data.getAchievable();
    boolean hasGeoData=achievable.hasGeoData();
    if (hasGeoData)
    {
      JPanel geoPanel=buildGeoStatusEditionPanel();
      GridBagConstraints geoC=new GridBagConstraints(0,3,2,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,insets,0,0);
      panel.add(geoPanel,geoC);
      JButton toggleMap=GuiFactory.buildButton("Map");
      ActionListener mapActionListener=new ActionListener()
      {
        @Override
        public void actionPerformed(ActionEvent e)
        {
          _geoEditor.showMaps();
        }
      };
      toggleMap.addActionListener(mapActionListener);
      geoC.gridy++;
      panel.add(toggleMap,geoC);
    }
    // Set values
    AchievableElementState state=_data.getState();
    setState(state);
    _completionDate.setDate(_data.getCompletionDate());
    if (_geoEditor!=null)
    {
      _geoEditor.setStatusData();
    }
    return panel;
  }

  private JPanel buildGeoStatusEditionPanel()
  {
    _geoEditor=new DeedGeoStatusEditionPanelController(this,_data);

    List<DeedGeoPointStatusGadgetsController> gadgets=_geoEditor.getGadgets();
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    TitledBorder pathsBorder=GuiFactory.buildTitledBorder("Geographic status");
    panel.setBorder(pathsBorder);

    int lineIndex=0;
    for(DeedGeoPointStatusGadgetsController gadget : gadgets)
    {
      GridBagConstraints c=new GridBagConstraints(0,lineIndex,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
      panel.add(gadget.getCheckbox().getCheckbox(),c);
      c.gridx++;
      panel.add(gadget.getDateEditor().getTextField(),c);
      lineIndex++;
    }
    return panel;
  }

  @Override
  protected void okImpl()
  {
    // Completed?
    AchievableElementState state=getState();
    _data.setState(state);
    // Completion date
    Long completionDate=_completionDate.getDate();
    _data.setCompletionDate(completionDate);
  }

  private AchievableElementState getState()
  {
    ThreeState state=_completed.getState();
    if (state==ThreeState.SELECTED) return AchievableElementState.COMPLETED;
    if (state==ThreeState.HALF_SELECTED) return AchievableElementState.UNDERWAY;
    return AchievableElementState.UNDEFINED;
  }

  private void setState(AchievableElementState state)
  {
    if (state==AchievableElementState.COMPLETED)
    {
      _completed.setState(ThreeState.SELECTED);
      _completionDate.setState(true,true);
    }
    else if (state==AchievableElementState.UNDERWAY)
    {
      _completed.setState(ThreeState.HALF_SELECTED);
      _completionDate.setState(false,false);
    }
    else
    {
      _completed.setState(ThreeState.NOT_SELECTED);
      _completionDate.setState(false,false);
    }
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    super.dispose();
    if (_completed!=null)
    {
      _completed=null;
    }
    if (_completionDate!=null)
    {
      _completionDate.dispose();
      _completionDate=null;
    }
    if (_geoEditor!=null)
    {
      _geoEditor.dispose();
      _geoEditor=null;
    }
  }
}
