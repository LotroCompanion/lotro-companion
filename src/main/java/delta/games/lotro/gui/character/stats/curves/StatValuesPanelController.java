package delta.games.lotro.gui.character.stats.curves;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.games.lotro.character.stats.BasicStatsSet;
import delta.games.lotro.character.stats.STAT;
import delta.games.lotro.character.stats.ratings.RatingCurve;
import delta.games.lotro.gui.character.stats.StatDisplayUtils;
import delta.games.lotro.utils.FixedDecimalsInteger;

/**
 * Controller for a panel to display stat values.
 * @author DAM
 */
public class StatValuesPanelController
{
  // UI
  private JPanel _panel;
  // Data
  private StatCurvesChartConfiguration _config;

  /**
   * Constructor.
   * @param config Curves configuration.
   */
  public StatValuesPanelController(StatCurvesChartConfiguration config)
  {
    _config=config;
    _panel=GuiFactory.buildPanel(new GridBagLayout());
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
   * Update this panel with new stats.
   * @param values Stats to display.
   */
  public void update(BasicStatsSet values)
  {
    // Clear
    _panel.removeAll();

    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,5,2,5),0,0);
    // First line: Base stat
    // - First cell blank
    c.gridx++;
    // - Stat name
    STAT baseStat=_config.getBaseStat();
    JLabel baseStatNameLabel=GuiFactory.buildLabel(baseStat.getName());
    _panel.add(baseStatNameLabel,c);
    // - Stat value
    c.gridx++;
    FixedDecimalsInteger baseStatValue=values.getStat(baseStat);
    JLabel baseStatValueLabel=GuiFactory.buildLabel(StatDisplayUtils.getStatDisplay(baseStatValue,baseStat.isPercentage()));
    _panel.add(baseStatValueLabel,c);
    c.gridy++;

    // Stat lines
    int level=_config.getLevel();
    List<SingleStatCurveConfiguration> curveConfigs=_config.getCurveConfigurations();
    int nbCurves=curveConfigs.size();
    for(int curveIndex=0;curveIndex<nbCurves;curveIndex++)
    {
      SingleStatCurveConfiguration curveConfig=curveConfigs.get(curveIndex);
      Color color=StatCurveChartPanelController.LINE_COLORS[curveIndex];
      List<STAT> stats=curveConfig.getStats();
      for(STAT stat : stats)
      {
        c.gridx=0;
        if (nbCurves>1)
        {
          // Add color
          JLabel coloredLabel=buildColoredLabel(color);
          _panel.add(coloredLabel,c);
        }
        c.gridx++;
        // Stat name
        JLabel statNameLabel=GuiFactory.buildLabel(stat.getName());
        _panel.add(statNameLabel,c);
        c.gridx++;
        // Stat value
        FixedDecimalsInteger statValueFromCurve=getStatValue(curveConfig,level,baseStatValue);
        FixedDecimalsInteger statValue=values.getStat(stat);
        String statValueFromCurveStr=StatDisplayUtils.getStatDisplay(statValueFromCurve,stat.isPercentage());
        JLabel statValueFromCurveLabel=GuiFactory.buildLabel(statValueFromCurveStr);
        _panel.add(statValueFromCurveLabel,c);
        c.gridx++;
        // Bonus
        FixedDecimalsInteger deltaValue=null;
        if ((statValueFromCurve!=null) && (statValue!=null))
        {
          int delta=statValue.getInternalValue()-statValueFromCurve.getInternalValue();
          if (delta!=0)
          {
            deltaValue=new FixedDecimalsInteger();
            deltaValue.setRawValue(delta);
          }
        }
        if (deltaValue!=null)
        {
          String deltaValueStr=StatDisplayUtils.getStatDisplay(deltaValue,stat.isPercentage());
          if (deltaValue.getInternalValue()>0) deltaValueStr="+"+deltaValueStr;
          String totalValueStr=StatDisplayUtils.getStatDisplay(statValue,stat.isPercentage());
          String bonusStr=deltaValueStr+" = "+totalValueStr;
          JLabel bonusLabel=GuiFactory.buildLabel(bonusStr);
          _panel.add(bonusLabel,c);
          c.gridx++;
        }
        c.gridy++;
      }
    }
  }

  private FixedDecimalsInteger getStatValue(SingleStatCurveConfiguration config, int level, FixedDecimalsInteger baseStatValue)
  {
    RatingCurve curve=config.getCurve();
    double value=(baseStatValue!=null)?baseStatValue.doubleValue():0.0;
    Double result=curve.getPercentage(value,level);
    FixedDecimalsInteger ret=(result!=null)?new FixedDecimalsInteger(result.floatValue()):null;
    return ret;
  }

  private JLabel buildColoredLabel(Color color)
  {
    JLabel label=GuiFactory.buildLabel("     ");
    label.setBackground(color);
    label.setOpaque(true);
    return label;
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
    _config=null;
  }
}
