package delta.games.lotro.gui.common.stats;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.combobox.ComboBoxController;
import delta.common.ui.swing.combobox.ItemSelectionListener;
import delta.common.ui.swing.text.NumberEditionController;
import delta.common.ui.swing.text.NumberListener;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.stats.BasicStatsSet;
import delta.games.lotro.character.stats.StatsSetElement;
import delta.games.lotro.common.stats.CustomStatsMergeMode;
import delta.games.lotro.common.stats.StatDescription;
import delta.games.lotro.common.stats.StatsManager;
import delta.games.lotro.common.stats.StatsProvider;

/**
 * Controller for a panel to edit custom stats.
 * @author DAM
 */
public class CustomStatsEditionPanelController
{
  private static final int MAX_STATS = 10;
  // Data
  private StatsManager _statsManager;
  private StatsProvider _provider;
  // GUI
  private JPanel _panel;
  private JPanel _autoStats;
  private JPanel _resultStats;
  // Controllers
  private WindowController _parent;
  private ComboBoxController<CustomStatsMergeMode> _mode;
  private List<SingleStatsEditionGadgetsController> _statControllers;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param statsManager Stats manager.
   * @param provider Stats provider.
   */
  public CustomStatsEditionPanelController(WindowController parent, StatsManager statsManager, StatsProvider provider)
  {
    _provider=provider;
    _mode=buildModesCombo();
    _statControllers=new ArrayList<SingleStatsEditionGadgetsController>();
    _panel=buildPanel();
    init(statsManager);
    _parent=parent;
  }

  /**
   * Initialize the managed panel with the given data.
   * @param statsManager Stats manager to use.
   */
  private void init(StatsManager statsManager)
  {
    // Stats manager
    _statsManager=new StatsManager(statsManager);
    // Mode
    CustomStatsMergeMode mode=statsManager.getMode();
    _mode.selectItem(mode);
    // Auto stats
    StatsPanel.fillStatsPanel(_autoStats,_statsManager.getDefault(),_provider);
    // Custom stats
    BasicStatsSet statsSet=statsManager.getCustom();
    List<StatsSetElement> usedStats=statsSet.getStatElements();
    int nbStats=usedStats.size();
    for(int i=0;i<MAX_STATS;i++)
    {
      SingleStatsEditionGadgetsController ctrl=_statControllers.get(i);
      if (i<nbStats)
      {
        StatsSetElement statElement=usedStats.get(i);
        StatDescription stat=statElement.getStat();
        Number value=statElement.getValue();
        ctrl.setStat(stat,value);
      }
      else
      {
        ctrl.clear();
      }
    }
    // Result stats
    StatsPanel.fillStatsPanel(_resultStats,statsManager.getResult(),_provider);
  }

  /**
   * Get the data from the UI.
   * @param storage Storage for loaded data.
   */
  public void getData(StatsManager storage)
  {
    // Mode
    CustomStatsMergeMode mode=_mode.getSelectedItem();
    storage.setMode(mode);
    // Stats
    BasicStatsSet stats=getStats();
    BasicStatsSet customStats=storage.getCustom();
    customStats.setStats(stats);
    storage.apply();
  }

  /**
   * Get the current stats.
   * @return the current stats.
   */
  private BasicStatsSet getStats()
  {
    BasicStatsSet stats=new BasicStatsSet();
    for(SingleStatsEditionGadgetsController ctrl : _statControllers)
    {
      StatDescription stat=ctrl.getStat();
      if (stat!=null)
      {
        Float value=ctrl.getValue();
        if (value!=null)
        {
          stats.addStat(stat,value);
        }
      }
    }
    return stats;
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
    // Automatic stats
    _autoStats=GuiFactory.buildPanel(new GridBagLayout());
    _autoStats.setBorder(GuiFactory.buildTitledBorder("Automatic stats")); // I18n

    // Result stats
    _resultStats=GuiFactory.buildPanel(new GridBagLayout());
    _resultStats.setBorder(GuiFactory.buildTitledBorder("Result stats")); // I18n

    // Custom stats
    JPanel customStatsPanel=buildCustomStatsPanel();
    customStatsPanel.setBorder(GuiFactory.buildTitledBorder("Custom stats")); // I18n

    // Assembly
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,0,1,2,1.0,0.0,GridBagConstraints.NORTHWEST,GridBagConstraints.HORIZONTAL,new Insets(5,5,5,0),0,0);
    panel.add(customStatsPanel,c);
    c=new GridBagConstraints(1,0,1,1,1.0,0.0,GridBagConstraints.NORTHWEST,GridBagConstraints.HORIZONTAL,new Insets(5,5,5,5),0,0);
    panel.add(_autoStats,c);
    c=new GridBagConstraints(1,1,1,1,1.0,0.0,GridBagConstraints.NORTHWEST,GridBagConstraints.HORIZONTAL,new Insets(0,5,5,5),0,0);
    panel.add(_resultStats,c);
    return panel;
  }

  private JPanel buildCustomStatsPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    // Mode
    JPanel modePanel=buildModePanel();
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(5,5,0,5),0,0);
    panel.add(modePanel,c);

    // Stats
    JPanel statsPanel=buildStatsPanel();
    c=new GridBagConstraints(0,1,1,1,1.0,1.0,GridBagConstraints.NORTHWEST,GridBagConstraints.BOTH,new Insets(5,5,5,5),0,0);
    panel.add(statsPanel,c);

    return panel;
  }

  private JPanel buildModePanel()
  {
    JPanel panel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
    panel.add(GuiFactory.buildLabel("Mode:")); // I18n
    panel.add(_mode.getComboBox());
    _mode.addListener(buildModeChoiceListener());
    return panel;
  }

  private JPanel buildStatsPanel()
  {
    NumberListener<Float> valueListener=buildValueListener();
    ItemSelectionListener<StatDescription> statListener=buildStatChoiceListener();
    ActionListener deleteListener=buildDeleteListener();
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    for(int i=0;i<MAX_STATS;i++)
    {
      SingleStatsEditionGadgetsController ctrl=new SingleStatsEditionGadgetsController();
      _statControllers.add(ctrl);

      GridBagConstraints c=new GridBagConstraints(0,i,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
      // Value editor
      JTextField value=ctrl.getValueField();
      panel.add(value,c);
      ctrl.getValueEditor().addValueListener(valueListener);
      // Stat chooser
      c.gridx++;
      ComboBoxController<StatDescription> statChooser=ctrl.getStatComboController();
      panel.add(statChooser.getComboBox(),c);
      statChooser.addListener(statListener);
      // Stat unit label
      JLabel unit=ctrl.getUnit();
      c.gridx++;
      panel.add(unit,c);
      // Delete button
      JButton deleteButton=ctrl.getDeleteButton();
      deleteButton.addActionListener(deleteListener);
      c.gridx++;
      panel.add(deleteButton,c);
    }
    return panel;
  }

  private ItemSelectionListener<CustomStatsMergeMode> buildModeChoiceListener()
  {
    ItemSelectionListener<CustomStatsMergeMode> valueListener=new ItemSelectionListener<CustomStatsMergeMode>()
    {
      @Override
      public void itemSelected(CustomStatsMergeMode newValue)
      {
        update();
      }
    };
    return valueListener;
  }

  private NumberListener<Float> buildValueListener()
  {
    NumberListener<Float> valueListener=new NumberListener<Float>()
    {
      @Override
      public void valueChanged(NumberEditionController<Float> source, Float newValue)
      {
        update();
      }
    };
    return valueListener;
  }

  private ItemSelectionListener<StatDescription> buildStatChoiceListener()
  {
    ItemSelectionListener<StatDescription> valueListener=new ItemSelectionListener<StatDescription>()
    {
      @Override
      public void itemSelected(StatDescription newValue)
      {
        update();
      }
    };
    return valueListener;
  }

  private ActionListener buildDeleteListener()
  {
    ActionListener listener=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        update();
      }
    };
    return listener;
  }

  private void update()
  {
    // Load custom stats from UI
    getData(_statsManager);
    // Update 'result' stats
    StatsPanel.fillStatsPanel(_resultStats,_statsManager.getResult(),_provider);
    _resultStats.revalidate();
    _resultStats.repaint();
    // Pack parent window
    _panel.revalidate();
    _panel.repaint();
    if (_parent!=null)
    {
      Window window=_parent.getWindow();
      window.setMinimumSize(window.getPreferredSize());
      window.pack();
    }
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Data
    _statsManager=null;
    _provider=null;
    // UI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    _autoStats=null;
    _resultStats=null;
    // Controllers
    _parent=null;
    if (_statControllers!=null)
    {
      for(SingleStatsEditionGadgetsController controller : _statControllers)
      {
        controller.dispose();
      }
      _statControllers=null;
    }
    if (_mode!=null)
    {
      _mode.dispose();
      _mode=null;
    }
  }


  /**
   * Build a controller for a combo box to choose custom stats merge mode.
   * @return A new controller.
   */
  private ComboBoxController<CustomStatsMergeMode> buildModesCombo()
  {
    ComboBoxController<CustomStatsMergeMode> ctrl=new ComboBoxController<CustomStatsMergeMode>();
    ctrl.addItem(CustomStatsMergeMode.AUTO,"Automatic");
    ctrl.addItem(CustomStatsMergeMode.ADD,"Add");
    ctrl.addItem(CustomStatsMergeMode.SET,"Set");
    ctrl.addItem(CustomStatsMergeMode.MERGE,"Merge");
    ctrl.selectItem(CustomStatsMergeMode.AUTO);
    return ctrl;
  }
}
