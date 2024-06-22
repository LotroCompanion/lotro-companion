package delta.games.lotro.gui.lore.items.legendary.relics;

import java.util.Collections;
import java.util.List;

import javax.swing.Icon;

import delta.games.lotro.character.stats.BasicStatsSet;
import delta.games.lotro.common.stats.StatUtils;
import delta.games.lotro.gui.LotroIconsManager;
import delta.games.lotro.gui.lore.items.utils.IconNameStatsBundle;
import delta.games.lotro.lore.items.legendary.relics.Relic;

/**
 * Controller for the UI items to display a single relic.
 * @author DAM
 */
public class SingleRelicDisplayController extends IconNameStatsBundle
{
  /**
   * Constructor.
   */
  public SingleRelicDisplayController()
  {
    super();
    // Initialize with no relic
    setRelic(null);
  }

  /**
   * Set current relic.
   * @param relic Relic to set.
   */
  public void setRelic(Relic relic)
  {
    // Set relic icon
    Icon icon=null;
    if (relic!=null)
    {
      String filename=relic.getIconFilename();
      icon=LotroIconsManager.getRelicIcon(filename);
    }
    else
    {
      icon=LotroIconsManager.getDefaultItemIcon();
    }
    _icon.setIcon(icon);
    // Text
    String text="";
    if (relic!=null)
    {
      text=relic.getName();
    }
    setName(text);
    // Stats
    if (relic!=null)
    {
      BasicStatsSet stats=relic.getStats();
      List<String> lines=StatUtils.getStatsForDisplay(stats);
      setStats(lines);
    }
    else
    {
      setStats(Collections.emptyList());
    }
  }
}
