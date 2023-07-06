package delta.games.lotro.gui.character.status.crafting;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
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
import delta.common.ui.swing.text.dates.DateEditionController;
import delta.common.ui.swing.text.dates.DateListener;
import delta.games.lotro.character.status.crafting.CraftingLevelStatus;
import delta.games.lotro.character.status.crafting.CraftingLevelTierStatus;
import delta.games.lotro.character.status.crafting.ProfessionStatus;
import delta.games.lotro.gui.utils.l10n.DateFormat;
import delta.games.lotro.lore.crafting.CraftingLevel;
import delta.games.lotro.lore.crafting.Profession;

/**
 * Controller for a profession status edition panel.
 * @author DAM
 */
public class ProfessionStatusEditionPanelController
{
  // Data
  private ProfessionStatus _status;
  // Controllers
  private List<CraftingLevelTierEditionGadgets> _proficiencyGadgets;
  private List<CraftingLevelTierEditionGadgets> _masteryGadgets;
  private ProfessionHistoryChartPanelController _chart;
  private DateEditionController _validityDate;
  // UI
  private JPanel _panel;
  private Timer _updateTimer;

  /**
   * Constructor.
   * @param status Status to edit.
   * @param chart Associated chart.
   */
  public ProfessionStatusEditionPanelController(ProfessionStatus status, ProfessionHistoryChartPanelController chart)
  {
    _status=status;
    _chart=chart;
    _proficiencyGadgets=new ArrayList<CraftingLevelTierEditionGadgets>();
    _masteryGadgets=new ArrayList<CraftingLevelTierEditionGadgets>();
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
    JPanel panel=GuiFactory.buildPanel(new BorderLayout());
    JPanel topPanel=buildValidityDatePanel();
    panel.add(topPanel,BorderLayout.NORTH);
    JPanel statusEditionPanel=buildStatusEditionPanel();
    panel.add(statusEditionPanel,BorderLayout.CENTER);
    updateUiFromData();
    return panel;
  }

  private JPanel buildValidityDatePanel()
  {
    _validityDate=new DateEditionController(DateFormat.getDateTimeCodec());
    JPanel panel=GuiFactory.buildPanel(new FlowLayout());
    panel.add(GuiFactory.buildLabel("Validity date:"));
    panel.add(_validityDate.getTextField());
    DateListener dateListener=new DateListener()
    {
      @Override
      public void dateChanged(DateEditionController controller, Long newDate)
      {
        _status.setValidityDate(newDate);
        triggerChartUpdateTimer();
      }
    };
    _validityDate.addListener(dateListener);
    return panel;
  }

  private JPanel buildStatusEditionPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    // Header row 1
    GridBagConstraints c=new GridBagConstraints(0,0,1,2,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    JLabel tier=GuiFactory.buildLabel("Tier"); // I18n
    panel.add(tier,c);
    c.gridx=1;c.gridwidth=3;c.gridheight=1;
    JLabel proficiency=GuiFactory.buildLabel("Proficiency"); // I18n
    panel.add(proficiency,c);
    c.gridx=4;c.gridwidth=3;c.gridheight=1;
    JLabel mastery=GuiFactory.buildLabel("Mastery"); // I18n
    panel.add(mastery,c);

    // Header row 2
    c.gridx=1;c.gridy=1;c.gridwidth=1;
    for(int i=0;i<2;i++)
    {
      panel.add(GuiFactory.buildLabel("Completed"),c); // I18n
      c.gridx++;
      panel.add(GuiFactory.buildLabel("XP"),c); // I18n
      c.gridx++;
      panel.add(GuiFactory.buildLabel("Completion date"),c); // I18n
      c.gridx++;
    }
    c.gridy++;

    ActionListener l=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        handleCompletionChange(e.getSource());
      }
    };
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
    Profession profession=_status.getProfession();
    for(CraftingLevel level : profession.getLevels())
    {
      CraftingLevelStatus levelStatus=_status.getLevelStatus(level);
      c.gridx=0;
      String tierName=level.getCraftTier().getLabel();
      JLabel tierLabel=GuiFactory.buildLabel(tierName);
      panel.add(tierLabel,c);
      c.gridx++;
      // Proficiency
      CraftingLevelTierStatus proficiencyStatus=levelStatus.getProficiency();
      CraftingLevelTierEditionGadgets proficiencyGadgets=new CraftingLevelTierEditionGadgets(proficiencyStatus);
      JCheckBox checkbox=proficiencyGadgets.getCompleted().getCheckbox();
      panel.add(checkbox,c);
      checkbox.addActionListener(l);
      c.gridx++;
      panel.add(proficiencyGadgets.getXp().getTextField(),c);
      c.gridx++;
      DateEditionController dateCompletion=proficiencyGadgets.getCompletionDate();
      dateCompletion.addListener(dateListener);
      panel.add(dateCompletion.getTextField(),c);
      c.gridx++;
      _proficiencyGadgets.add(proficiencyGadgets);

      // Mastery
      CraftingLevelTierStatus masteryStatus=levelStatus.getMastery();
      CraftingLevelTierEditionGadgets masteryGadgets=new CraftingLevelTierEditionGadgets(masteryStatus);
      checkbox=masteryGadgets.getCompleted().getCheckbox();
      panel.add(checkbox,c);
      checkbox.addActionListener(l);
      c.gridx++;
      panel.add(masteryGadgets.getXp().getTextField(),c);
      c.gridx++;
      dateCompletion=masteryGadgets.getCompletionDate();
      dateCompletion.addListener(dateListener);
      panel.add(dateCompletion.getTextField(),c);
      c.gridx++;
      _masteryGadgets.add(masteryGadgets);
      c.gridy++;

      if (level.getTier()==0) // Beginner
      {
        proficiencyGadgets.getCompleted().setState(false);
        masteryGadgets.getCompleted().setState(false);
      }
    }
    return panel;
  }

  private void handleCompletionChange(Object source)
  {
    int index=0;
    Profession profession=_status.getProfession();
    for(CraftingLevel level : profession.getLevels())
    {
      CraftingLevelTierEditionGadgets proficiency=_proficiencyGadgets.get(index);
      if (source==proficiency.getCompleted().getCheckbox())
      {
        boolean completed=proficiency.getCompleted().isSelected();
        _status.setCompletionStatus(level,false,completed);
      }
      CraftingLevelTierEditionGadgets mastery=_masteryGadgets.get(index);
      if (source==mastery.getCompleted().getCheckbox())
      {
        boolean completed=mastery.getCompleted().isSelected();
        _status.setCompletionStatus(level,true,completed);
      }
      index++;
    }
    updateUiFromData();
  }

  private void handleDateChange(DateEditionController source, long completionDate)
  {
    int index=0;
    Profession profession=_status.getProfession();
    for(CraftingLevel level : profession.getLevels())
    {
      CraftingLevelTierEditionGadgets proficiency=_proficiencyGadgets.get(index);
      if (source==proficiency.getCompletionDate())
      {
        _status.getLevelStatus(level).getProficiency().setCompletionDate(completionDate);
      }
      CraftingLevelTierEditionGadgets mastery=_masteryGadgets.get(index);
      if (source==mastery.getCompletionDate())
      {
        _status.getLevelStatus(level).getMastery().setCompletionDate(completionDate);
      }
      index++;
    }
    Long validityDate=_status.getValidityDate();
    if (validityDate==null)
    {
      updateValidityDate();
    }
    triggerChartUpdateTimer();
  }

  /**
   * Update validity date.
   */
  private void updateValidityDate()
  {
    long[] range=_status.getDateRange();
    if (range!=null)
    {
      long validityDate=range[1];
      long now=System.currentTimeMillis();
      if (validityDate>now)
      {
        validityDate=now;
      }
      _status.setValidityDate(Long.valueOf(validityDate));
      _validityDate.setDate(_status.getValidityDate());
    }
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

  /**
   * Update UI from data.
   */
  public void updateUiFromData()
  {
    int nbTiers=_proficiencyGadgets.size();
    for(int i=0;i<nbTiers;i++)
    {
      _proficiencyGadgets.get(i).updateUiFromData();
      _masteryGadgets.get(i).updateUiFromData();
    }
    _validityDate.setDate(_status.getValidityDate());
    triggerChartUpdateTimer();
  }

  /**
   * Update data from UI contents.
   */
  public void updateDataFromUi()
  {
    int nbTiers=_proficiencyGadgets.size();
    for(int i=0;i<nbTiers;i++)
    {
      _proficiencyGadgets.get(i).updateDataFromUi();
      _masteryGadgets.get(i).updateDataFromUi();
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
    if (_masteryGadgets!=null)
    {
      for(CraftingLevelTierEditionGadgets gadget : _masteryGadgets)
      {
        gadget.dispose();
      }
      _masteryGadgets.clear();
      _masteryGadgets=null;
    }
    if (_proficiencyGadgets!=null)
    {
      for(CraftingLevelTierEditionGadgets gadget : _proficiencyGadgets)
      {
        gadget.dispose();
      }
      _proficiencyGadgets.clear();
      _proficiencyGadgets=null;
    }
    _status=null;
  }
}
