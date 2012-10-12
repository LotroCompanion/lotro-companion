package delta.games.lotro.gui.stats.reputation;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import org.apache.log4j.Logger;

import delta.common.utils.text.EncodingNames;
import delta.common.utils.text.TextUtils;
import delta.games.lotro.Config;
import delta.games.lotro.common.Faction;
import delta.games.lotro.common.FactionLevel;
import delta.games.lotro.common.Factions;
import delta.games.lotro.stats.reputation.FactionStat;
import delta.games.lotro.stats.reputation.ReputationStats;
import delta.games.lotro.utils.LotroLoggers;

/**
 * Controller for a reputation panel.
 * @author DAM
 */
public class ReputationPanelController
{
  private static final Logger _logger=LotroLoggers.getLotroLogger();

  private JPanel _panel;
  private ReputationStats _stats;
  
  /**
   * Constructor.
   * @param stats Reputation stats to display.
   */
  public ReputationPanelController(ReputationStats stats)
  {
    _stats=stats;
  }

  /**
   * Get the managed panel.
   * @return a panel.
   */
  public JPanel getPanel()
  {
    if (_panel==null)
    {
      _panel=buildPanel();
    }
    return _panel;
  }
  
  private JPanel buildPanel()
  {
    JPanel panel=new JPanel(new GridBagLayout());
    panel.setBackground(Color.BLACK);

    GridBagConstraints cLabel=new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    GridBagConstraints cBar=new GridBagConstraints(1,0,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(5,5,5,5),0,0);
    
    Faction[] factions=getFactions();
    int y=0;
    for(Faction faction : factions)
    {
      FactionLevel current;
      FactionStat stat=_stats.getFactionStat(faction);
      if (stat!=null)
      {
        current=stat.getFactionLevel();
      }
      else
      {
        current=faction.getInitialLevel();
      }
      // Label
      String name=faction.getName();
      JLabel label=new JLabel(name);
      label.setForeground(Color.LIGHT_GRAY);
      cLabel.gridy=y;
      panel.add(label,cLabel);
      // Bar
      String levelName=current.getName();
      FactionLevel[] levels=faction.getLevels();
      int min=0;
      int max=levels[levels.length-1].getValue();
      JProgressBar bar=new JProgressBar(JProgressBar.HORIZONTAL,min,max);
      int value=current.getValue();
      Color background=Color.BLUE;
      if (value<0)
      {
        background=Color.RED;
        value=-value;
      }
      bar.setForeground(background);
      bar.setBackground(Color.BLACK);
      bar.setBorderPainted(true);
      bar.setStringPainted(true);
      bar.setString(levelName);
      bar.setValue(value);
      cBar.gridy=y;
      panel.add(bar,cBar);
      y++;
    }
    
    return panel;
  }
  
  private Faction[] getFactions()
  {
    File cfgDir=Config.getInstance().getConfigDir();
    File factionFiles=new File(cfgDir,"reputation-order.txt"); 
    List<String> lines=TextUtils.readAsLines(factionFiles,EncodingNames.UTF_8);
    List<Faction> factions=new ArrayList<Faction>();
    if (lines!=null)
    {
      for(String line : lines)
      {
        Faction faction=Factions.getInstance().getByName(line);
        if (faction!=null)
        {
          factions.add(faction);
        }
        else
        {
          _logger.warn("Unknown faction ["+line+"]!");
        }
      }
    }
    Faction[] ret=factions.toArray(new Faction[factions.size()]);
    return ret;
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
    _stats=null;
  }
}
