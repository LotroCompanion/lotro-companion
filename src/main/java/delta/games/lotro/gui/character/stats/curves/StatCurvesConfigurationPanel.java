package delta.games.lotro.gui.character.stats.curves;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.combobox.ComboBoxController;
import delta.common.ui.swing.combobox.ItemSelectionListener;
import delta.common.ui.swing.text.IntegerEditionController;
import delta.games.lotro.gui.character.summary.CharacterUiUtils;

/**
 * Controller for a stat curves configuration panel.
 * @author DAM
 */
public class StatCurvesConfigurationPanel
{
  // Data
  private Integer _initialLevel;
  private StatCurvesChartConfiguration _config;
  // Controllers
  private ComboBoxController<Integer> _level;
  private IntegerEditionController _maxRating;
  private StatCurveChartPanelController _chart;
  private StatValuesPanelController _values;
  // UI
  private JPanel _panel;

  /**
   * Constructor.
   * @param config Curves configuration.
   * @param chart Associated chart.
   * @param values Associated 'values' panel.
   */
  public StatCurvesConfigurationPanel(StatCurvesChartConfiguration config, StatCurveChartPanelController chart, StatValuesPanelController values)
  {
    _initialLevel=null;
    _config=config;
    _chart=chart;
    _values=values;
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
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    // Level
    panel.add(GuiFactory.buildLabel("Level:"),c); // I18n
    c.gridx++;
    _level=CharacterUiUtils.buildLevelCombo();
    _level.selectItem(Integer.valueOf(_config.getLevel()));
    panel.add(_level.getComboBox(),c);
    ItemSelectionListener<Integer> listener=new ItemSelectionListener<Integer>()
    {
      @Override
      public void itemSelected(Integer item)
      {
        updateLevel(item.intValue());
      }
    };
    _level.addListener(listener);
    // Max rating
    c.gridx=0;
    c.gridy++;
    panel.add(GuiFactory.buildLabel("Max rating:"),c); // I18n
    c.gridx++;
    _maxRating=new IntegerEditionController(GuiFactory.buildTextField(""));
    _maxRating.setValueRange(Integer.valueOf(1),Integer.valueOf(5000000));
    _maxRating.setValue(Integer.valueOf((int)_config.getMaxRating()));
    _maxRating.getTextField().setColumns(5);
    panel.add(_maxRating.getTextField(),c);
    ActionListener al=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        updateMaxRating();
      }
    };
    _maxRating.getTextField().addActionListener(al);
    c.gridx++;
    return panel;
  }

  private void updateLevel(int level)
  {
    _config.setLevel(level);
    double maxRating=_config.getAutoMaxRating();
    _config.setMaxRating(maxRating);
    _maxRating.setValue(Integer.valueOf((int)maxRating));
    if (_chart!=null)
    {
      _chart.update();
    }
    _values.update();
  }

  private void updateMaxRating()
  {
    Integer value=_maxRating.getValue();
    if (value!=null)
    {
      _config.setMaxRating(value.doubleValue());
      if (_chart!=null)
      {
        _chart.update();
      }
    }
  }

  /**
   * Update this panel with new data.
   * @param level New level.
   */
  public void update(int level)
  {
    Integer newLevel=Integer.valueOf(level);
    if (!Objects.equals(_initialLevel,newLevel))
    {
      _initialLevel=newLevel;
      _level.selectItem(newLevel);
    }
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    if (_level!=null)
    {
      _level.dispose();
      _level=null;
    }
    if (_maxRating!=null)
    {
      _maxRating.dispose();
      _level=null;
    }
  }
}
