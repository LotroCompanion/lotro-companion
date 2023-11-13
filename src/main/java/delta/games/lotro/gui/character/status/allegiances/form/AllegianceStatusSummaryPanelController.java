package delta.games.lotro.gui.character.status.allegiances.form;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

import delta.common.ui.swing.GuiFactory;
import delta.games.lotro.character.status.allegiances.AllegianceStatus;
import delta.games.lotro.lore.allegiances.Points2LevelCurve;

/**
 * Controller for a panel to display status summary.
 * @author DAM
 */
public class AllegianceStatusSummaryPanelController
{
  // Data
  private AllegianceStatus _status;
  // GUI
  private JPanel _panel;

  /**
   * Constructor.
   * @param status Allegiance status.
   */
  public AllegianceStatusSummaryPanelController(AllegianceStatus status)
  {
    _status=status;
    _panel=build();
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  public JPanel getPanel()
  {
    return _panel;
  }

  private JPanel build()
  {
    Points2LevelCurve curve=_status.getPoints2LevelCurve();
    if (curve==null)
    {
      return null;
    }
    // Components
    // - level
    JProgressBar levels=buildProgressBar();
    Integer currentLevel=_status.getCurrentLevel();
    int levelToShow=currentLevel.intValue();
    int maxLevel=_status.getMaxLevel();
    setBar(levels,0,maxLevel,levelToShow);
    // - points
    int currentPoints=_status.getPointsEarned();
    int maxPoints=curve.getMaxPoints();
    JProgressBar points=buildProgressBar();
    setBar(points,0,maxPoints,currentPoints);
    // Assembly
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    // - level
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    panel.add(GuiFactory.buildLabel("Level:"),c); // I18n
    c=new GridBagConstraints(1,0,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(5,0,5,5),0,0);
    panel.add(levels,c);
    // - points
    c=new GridBagConstraints(0,1,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    panel.add(GuiFactory.buildLabel("Points:"),c); // I18n
    c=new GridBagConstraints(1,1,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(5,0,5,5),0,0);
    panel.add(points,c);
    return panel;
  }

  private JProgressBar buildProgressBar()
  {
    JProgressBar bar=new JProgressBar(SwingConstants.HORIZONTAL,0,1);
    bar.setBackground(GuiFactory.getBackgroundColor());
    bar.setForeground(Color.BLUE);
    bar.setBorderPainted(true);
    bar.setStringPainted(true);
    bar.setPreferredSize(new Dimension(200,25));
    return bar;
  }

  private void setBar(JProgressBar bar, int min, int max, int current)
  {
    bar.setMinimum(min);
    bar.setMaximum(max);
    int value=(current>max)?max:current;
    bar.setValue(value);
    String label=current+"/"+max;
    bar.setString(label);
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Data
    _status=null;
    // UI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
  }
}
