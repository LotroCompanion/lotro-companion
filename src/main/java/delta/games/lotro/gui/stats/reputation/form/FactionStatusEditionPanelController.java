package delta.games.lotro.gui.stats.reputation.form;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.checkbox.CheckboxController;
import delta.common.ui.swing.text.dates.DateEditionController;
import delta.common.ui.swing.text.dates.DateListener;
import delta.games.lotro.character.reputation.FactionData;
import delta.games.lotro.character.reputation.FactionLevelStatus;
import delta.games.lotro.lore.reputation.Faction;
import delta.games.lotro.lore.reputation.FactionLevel;

/**
 * Controller for a faction status edition panel.
 * @author DAM
 */
public class FactionStatusEditionPanelController
{
  // Data
  private FactionData _status;
  // Controllers
  private List<FactionLevelEditionGadgets> _gadgets;
  private FactionHistoryChartPanelController _chart;
  // UI
  private JPanel _panel;
  private Timer _updateTimer;

  /**
   * Constructor.
   * @param status Status to edit.
   * @param chart Associated chart.
   */
  public FactionStatusEditionPanelController(FactionData status, FactionHistoryChartPanelController chart)
  {
    _status=status;
    _chart=chart;
    _gadgets=new ArrayList<FactionLevelEditionGadgets>();
    _panel=buildPanel();
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  public JPanel getPanel()
  {
    return _panel;
  }

  private JPanel buildPanel()
  {
    JPanel panel=GuiFactory.buildBackgroundPanel(new BorderLayout());
    JPanel statusEditionPanel=buildStatusEditionPanel();
    panel.add(statusEditionPanel,BorderLayout.CENTER);
    updateUi();
    return panel;
  }

  private JPanel buildStatusEditionPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    // Header row 1
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    JLabel tier=GuiFactory.buildLabel("Rank");
    panel.add(tier,c);

    // Header row 2
    c.gridx=1;c.gridy=1;c.gridwidth=1;
    panel.add(GuiFactory.buildLabel("Completed"),c);
    c.gridx++;
    panel.add(GuiFactory.buildLabel("XP"),c);
    c.gridx++;
    panel.add(GuiFactory.buildLabel("Completion date"),c);
    c.gridx++;
    c.gridy++;

    ActionListener l=new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        handleCompletionChange(e.getSource());
      }
    };
    DateListener dateListener=new DateListener()
    {
      public void dateChanged(DateEditionController controller, Long newDate)
      {
        long date=(newDate!=null)?newDate.longValue():0;
        handleDateChange(controller,date);
      }
    };

    // Data rows
    Faction faction=_status.getFaction();

    for(FactionLevel level : faction.getLevels())
    {
      FactionLevelStatus levelStatus=_status.getStatusForLevel(level);
      c.gridx=0;
      JLabel tierLabel=GuiFactory.buildLabel(level.toString());
      panel.add(tierLabel,c);
      c.gridx++;
      FactionLevelEditionGadgets gadgets=new FactionLevelEditionGadgets(levelStatus);
      CheckboxController checkboxCtrl=gadgets.getCompleted();
      JCheckBox checkbox=checkboxCtrl.getCheckbox();
      panel.add(checkbox,c);
      checkbox.addActionListener(l);
      c.gridx++;
      panel.add(gadgets.getXp().getTextField(),c);
      c.gridx++;
      DateEditionController dateCompletion=gadgets.getCompletionDate();
      dateCompletion.addListener(dateListener);
      panel.add(dateCompletion.getTextField(),c);
      c.gridx++;
      _gadgets.add(gadgets);
      c.gridy++;

      if (level==faction.getInitialLevel())
      {
        checkboxCtrl.setState(false);
      }
    }
    return panel;
  }

  private void handleCompletionChange(Object source)
  {
    int index=0;
    Faction faction=_status.getFaction();
    for(FactionLevel level : faction.getLevels())
    {
      FactionLevelEditionGadgets gadgets=_gadgets.get(index);
      if (source==gadgets.getCompleted().getCheckbox())
      {
        boolean completed=gadgets.getCompleted().isSelected();
        _status.setCompletionStatus(level,completed);
        _status.updateCurrentLevel();
        triggerChartUpdateTimer();
        break;
      }
      index++;
    }
    updateUi();
  }

  private void handleDateChange(DateEditionController source, long completionDate)
  {
    int index=0;
    Faction faction=_status.getFaction();
    for(FactionLevel level : faction.getLevels())
    {
      FactionLevelStatus status=_status.getStatusForLevel(level);
      FactionLevelEditionGadgets proficiency=_gadgets.get(index);
      if (source==proficiency.getCompletionDate())
      {
        status.setCompletionDate(completionDate);
      }
      index++;
    }
    triggerChartUpdateTimer();
  }

  private void triggerChartUpdateTimer()
  {
    if (_updateTimer==null)
    {
      ActionListener updateChart = new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          _chart.updateData();
        }
      };
      _updateTimer=new Timer(100,updateChart);
      _updateTimer.setRepeats(false);
    }
    _updateTimer.restart();
  }

  private void updateUi()
  {
    int nbTiers=_gadgets.size();
    for(int i=0;i<nbTiers;i++)
    {
      _gadgets.get(i).updateUi();
    }
  }

  /**
   * Update data from UI contents.
   */
  public void updateDatafromUi()
  {
    int nbTiers=_gadgets.size();
    for(int i=0;i<nbTiers;i++)
    {
      _gadgets.get(i).updateDatafromUi();
    }
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    if (_gadgets!=null)
    {
      for(FactionLevelEditionGadgets gadget : _gadgets)
      {
        gadget.dispose();
      }
      _gadgets.clear();
      _gadgets=null;
    }
    _status=null;
  }
}
