package delta.games.lotro.gui.stats.reputation;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import delta.common.ui.swing.GuiFactory;
import delta.games.lotro.character.reputation.FactionData;
import delta.games.lotro.character.reputation.ReputationData;
import delta.games.lotro.lore.reputation.Faction;
import delta.games.lotro.lore.reputation.FactionLevel;
import delta.games.lotro.lore.reputation.FactionsRegistry;

/**
 * Controller for a reputation panel.
 * @author DAM
 */
public class ReputationPanelController
{
  private JPanel _panel;
  private ReputationData _stats;
  private List<Faction> _factions;
  private List<Faction> _worldRenownedFactions;

  /**
   * Constructor.
   * @param stats Reputation stats to display.
   */
  public ReputationPanelController(ReputationData stats)
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
      FactionData stat=_stats.getFactionStat(faction);
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
    FactionsRegistry registry=FactionsRegistry.getInstance();
    _factions=registry.getFactionsForCategory("ERIADOR");
    _worldRenownedFactions=registry.getFactionsForDeed("World Renowned");
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
