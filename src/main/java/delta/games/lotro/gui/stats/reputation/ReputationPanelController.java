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

import delta.common.ui.swing.GuiFactory;
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
  private List<Faction> _factions;
  private List<Faction> _worldRenownedFactions;

  /**
   * Constructor.
   * @param stats Reputation stats to display.
   */
  public ReputationPanelController(ReputationStats stats)
  {
    _stats=stats;
    init();
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
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    GridBagConstraints cLabel=new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    GridBagConstraints cBar=new GridBagConstraints(1,0,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(5,5,5,5),0,0);

    JLabel assessment=new JLabel("Reputation assessment:");
    assessment.setToolTipText("Assessment of reputation based on recorded character log deed items. It may be wrong sometimes!");
    GridBagConstraints cTitle=new GridBagConstraints(0,0,1,1,0.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(5,5,5,5),0,0);
    panel.add(assessment,cTitle);

    int nbWorldRenowned=0;
    int y=1;
    for(Faction faction : _factions)
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
      if (_worldRenownedFactions.contains(faction))
      {
        name="(*) "+name;
        if (current==FactionLevel.KINDRED) nbWorldRenowned++;
      }
      JLabel label=GuiFactory.buildLabel(name);
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
      bar.setBackground(GuiFactory.getBackgroundColor());
      bar.setBorderPainted(true);
      bar.setStringPainted(true);
      bar.setString(levelName);
      bar.setValue(value);
      cBar.gridy=y;
      panel.add(bar,cBar);
      y++;
    }
    String wrLabel="World Renowned(*): "+nbWorldRenowned+" / "+_worldRenownedFactions.size();
    JLabel worldRenowned=GuiFactory.buildLabel(wrLabel);
    worldRenowned.setToolTipText("Status for the 'World Renowned' deed");
    GridBagConstraints cWR=new GridBagConstraints(1,0,1,1,0,0,GridBagConstraints.EAST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    panel.add(worldRenowned,cWR);

    return panel;
  }

  private void init()
  {
    File cfgDir=Config.getInstance().getConfigDir();
    File factionFiles=new File(cfgDir,"reputation-order.txt");
    List<String> lines=TextUtils.readAsLines(factionFiles,EncodingNames.UTF_8);
    _factions=new ArrayList<Faction>();
    _worldRenownedFactions=new ArrayList<Faction>();
    if (lines!=null)
    {
      for(String line : lines)
      {
        boolean worldRenowned=false;
        if (line.startsWith("(R)"))
        {
          line=line.substring(3);
          worldRenowned=true;
        }
        Faction faction=Factions.getInstance().getByName(line);
        if (faction!=null)
        {
          _factions.add(faction);
          if (worldRenowned)
          {
            _worldRenownedFactions.add(faction);
          }
        }
        else
        {
          _logger.warn("Unknown faction ["+line+"]!");
        }
      }
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
    _stats=null;
  }
}
