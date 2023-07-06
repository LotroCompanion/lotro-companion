package delta.games.lotro.gui.character.status.reputation.form;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.area.AreaController;
import delta.common.ui.swing.panels.AbstractPanelController;
import delta.common.ui.swing.text.dates.DateEditionController;
import delta.common.ui.swing.text.dates.DateListener;
import delta.games.lotro.character.status.reputation.FactionLevelStatus;
import delta.games.lotro.character.status.reputation.FactionStatus;
import delta.games.lotro.lore.reputation.Faction;
import delta.games.lotro.lore.reputation.FactionLevel;
import delta.games.lotro.utils.strings.ContextRendering;

/**
 * Controller for a faction status edition panel.
 * @author DAM
 */
public class FactionHistoryEditionPanelController extends AbstractPanelController
{
  // Data
  private FactionStatus _status;
  // Controllers
  private List<FactionLevelEditionGadgets> _gadgets;
  private FactionHistoryChartPanelController _chart;
  // UI
  private Timer _updateTimer;

  /**
   * Constructor.
   * @param parent Parent controller.
   * @param status Status to edit.
   * @param chart Associated chart.
   */
  public FactionHistoryEditionPanelController(AreaController parent, FactionStatus status, FactionHistoryChartPanelController chart)
  {
    super(parent);
    _status=status;
    _chart=chart;
    _gadgets=new ArrayList<FactionLevelEditionGadgets>();
    JPanel panel=buildPanel();
    setPanel(panel);
  }

  private JPanel buildPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new BorderLayout());
    JPanel statusEditionPanel=buildStatusEditionPanel();
    panel.add(statusEditionPanel,BorderLayout.CENTER);
    updateUi();
    return panel;
  }

  private JPanel buildStatusEditionPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    // Header row
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    JLabel tier=GuiFactory.buildLabel("Rank"); // I18n
    panel.add(tier,c);
    c=new GridBagConstraints(1,0,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    panel.add(GuiFactory.buildLabel("Completion date"),c); // I18n
    c.gridx++;
    c.gridy++;

    DateListener dateListener=new DateListener()
    {
      @Override
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
      String rawTierName=level.getName();
      String tierName=ContextRendering.render(this,rawTierName);
      JLabel tierLabel=GuiFactory.buildLabel(tierName);
      panel.add(tierLabel,c);
      c.gridx++;
      FactionLevelEditionGadgets gadgets=new FactionLevelEditionGadgets(levelStatus);
      DateEditionController dateCompletion=gadgets.getCompletionDate();
      dateCompletion.addListener(dateListener);
      panel.add(dateCompletion.getTextField(),c);
      c.gridx++;
      _gadgets.add(gadgets);
      c.gridy++;
    }
    return panel;
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
        @Override
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
   * Release all managed resources.
   */
  public void dispose()
  {
    super.dispose();
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
