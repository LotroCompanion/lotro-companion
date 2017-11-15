package delta.games.lotro.gui.character.stats.contribs;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.combobox.ComboBoxController;
import delta.common.ui.swing.combobox.ItemSelectionListener;
import delta.games.lotro.character.CharacterData;
import delta.games.lotro.character.stats.CharacterStatsComputer;
import delta.games.lotro.character.stats.STAT;
import delta.games.lotro.character.stats.contribs.ContribsByStat;
import delta.games.lotro.character.stats.contribs.StatsContributionsManager;

/**
 * Controller for a panel that displays the contributions of to stats.
 * @author DAM
 */
public class StatContribsPanelController
{
  // Utils
  private CharacterStatsComputer _statsComputer;
  private StatsContributionsManager _contribs;
  // UI
  private JPanel _panel;
  // Controllers
  private ComboBoxController<STAT> _statChooser;
  private StatContribsChartPanelController _chartPanel;

  /**
   * Constructor.
   */
  public StatContribsPanelController()
  {
    _contribs=new StatsContributionsManager();
    _statsComputer=new CharacterStatsComputer(_contribs);
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

  /**
   * Update values.
   * @param data Data to use.
   */
  public void update(CharacterData data)
  {
    _statsComputer.getStats(data);
    _contribs.compute();
    updateStatCombo();
  }

  private JPanel buildPanel()
  {
    JPanel panel=GuiFactory.buildBackgroundPanel(new BorderLayout());
    // Top panel
    panel.add(buildComboPanel(),BorderLayout.NORTH);
    // Center panel
    _chartPanel=new StatContribsChartPanelController();
    panel.add(_chartPanel.getPanel(),BorderLayout.CENTER);
    return panel;
  }

  private JPanel buildComboPanel()
  {
    _statChooser=buildStatCombo();
    JPanel panel=GuiFactory.buildPanel(new FlowLayout());
    panel.add(GuiFactory.buildLabel("Stat:"));
    panel.add(_statChooser.getComboBox());
    return panel;
  }

  private void updateStatCombo()
  {
    STAT currentStat=_statChooser.getSelectedItem();
    _statChooser.removeAllItems();
    for(STAT stat : STAT.values())
    {
      ContribsByStat contribs=_contribs.getContribs(stat);
      if (contribs!=null)
      {
        _statChooser.addItem(stat,stat.getName());
      }
    }
    if (currentStat!=null)
    {
      _statChooser.selectItem(currentStat);
    }
  }

  private ComboBoxController<STAT> buildStatCombo()
  {
    ComboBoxController<STAT> controller=new ComboBoxController<STAT>();
    ItemSelectionListener<STAT> listener=new ItemSelectionListener<STAT>()
    {
      public void itemSelected(STAT stat)
      {
        updateStat(stat);
      }
    };
    controller.addListener(listener);
    return controller;
  }

  private void updateStat(STAT stat)
  {
    ContribsByStat contribs=_contribs.getContribs(stat);
    if (contribs==null)
    {
      contribs=new ContribsByStat(stat);
    }
    _chartPanel.setContributions(contribs);
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    _panel=null;
 }
}
