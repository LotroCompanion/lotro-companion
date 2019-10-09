package delta.games.lotro.gui.items.legendary;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.labels.MultilineLabel2;
import delta.games.lotro.character.stats.BasicStatsSet;
import delta.games.lotro.common.effects.Effect;
import delta.games.lotro.common.stats.StatsProvider;
import delta.games.lotro.gui.items.legendary.non_imbued.NonImbuedLegendaryAttrsDisplayPanelController;
import delta.games.lotro.gui.items.legendary.relics.RelicsSetDisplayController;
import delta.games.lotro.gui.utils.StatDisplayUtils;
import delta.games.lotro.lore.items.legendary.LegendaryInstanceAttrs;
import delta.games.lotro.lore.items.legendary.non_imbued.DefaultNonImbuedLegacy;
import delta.games.lotro.lore.items.legendary.non_imbued.DefaultNonImbuedLegacyInstance;
import delta.games.lotro.lore.items.legendary.non_imbued.NonImbuedLegendaryInstanceAttrs;

/**
 * Panel to display the a legendary instance.
 * @author DAM
 */
public class LegendaryInstanceDisplayPanelController
{
  // UI
  private JPanel _panel;
  private MultilineLabel2 _passives;
  private MultilineLabel2 _defaultLegacyStats;
  // Controllers
  private NonImbuedLegendaryAttrsDisplayPanelController _nonImbuedAttrs;
  private RelicsSetDisplayController _relics;

  /**
   * Constructor.
   */
  public LegendaryInstanceDisplayPanelController()
  {
    _passives=new MultilineLabel2();
    _defaultLegacyStats=new MultilineLabel2();
    _nonImbuedAttrs=new NonImbuedLegendaryAttrsDisplayPanelController();
    _relics=new RelicsSetDisplayController();
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
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    int y=0;
    // Passives
    GridBagConstraints c=new GridBagConstraints(0,y,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,5,0,0),0,0);
    ret.add(_passives,c);
    y++;
    // Default legacy stats
    c=new GridBagConstraints(0,y,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,5,0,0),0,0);
    ret.add(_defaultLegacyStats,c);
    y++;
    // Legacies
    JPanel legaciesPanel=_nonImbuedAttrs.getPanel();
    c=new GridBagConstraints(0,y,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    ret.add(legaciesPanel,c);
    y++;
    // Relics
    JPanel relicsPanel=_relics.getPanel();
    c=new GridBagConstraints(0,y,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    ret.add(relicsPanel,c);
    y++;
    return ret;
  }

  /**
   * Set the data to display.
   * @param itemLevel Parent item level.
   * @param attrs Data to display.
   */
  public void setData(int itemLevel, LegendaryInstanceAttrs attrs)
  {
    // Passives
    BasicStatsSet passives=getPassives(itemLevel,attrs);
    _passives.setText(StatDisplayUtils.getStatsDisplayLines(passives));
    // Default legacy stats
    BasicStatsSet defaultLegacyStats=getDefaultStats(attrs);
    _defaultLegacyStats.setText(StatDisplayUtils.getStatsDisplayLines(defaultLegacyStats));
    // Legacies
    _nonImbuedAttrs.setData(attrs.getNonImbuedAttrs());
    // Relics
    _relics.setData(attrs.getRelicsSet());
  }

  private BasicStatsSet getPassives(int itemLevel, LegendaryInstanceAttrs attrs)
  {
    BasicStatsSet ret=new BasicStatsSet();
    List<Effect> passives=attrs.getPassives();
    for(Effect passive : passives)
    {
      StatsProvider statsProvider=passive.getStatsProvider();
      BasicStatsSet singlePassiveStats=statsProvider.getStats(1,itemLevel);
      ret.addStats(singlePassiveStats);
    }
    return ret;
  }

  private BasicStatsSet getDefaultStats(LegendaryInstanceAttrs attrs)
  {
    NonImbuedLegendaryInstanceAttrs nonImbuedAttrs=attrs.getNonImbuedAttrs();
    DefaultNonImbuedLegacyInstance defaultLegacyInstance=nonImbuedAttrs.getDefaultLegacy();
    DefaultNonImbuedLegacy defaultLegacy=defaultLegacyInstance.getLegacy();
    StatsProvider statsProvider=defaultLegacy.getEffect().getStatsProvider();
    int rank=defaultLegacyInstance.getRank();
    BasicStatsSet ret=statsProvider.getStats(1,rank);
    return ret;
  }
}
