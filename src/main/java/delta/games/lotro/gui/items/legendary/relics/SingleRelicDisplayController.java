package delta.games.lotro.gui.items.legendary.relics;

import javax.swing.Icon;

import delta.common.ui.swing.icons.IconsManager;
import delta.games.lotro.character.stats.BasicStatsSet;
import delta.games.lotro.common.stats.StatUtils;
import delta.games.lotro.gui.LotroIconsManager;
import delta.games.lotro.gui.items.utils.IconNameStatsBundle;
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
      icon=IconsManager.getIcon(ITEM_WITH_NO_ICON);
    }
    _icon.setIcon(icon);
    // Text
    String text="";
    if (relic!=null)
    {
      text=relic.getName();
    }
    _name.setText(text,1);
    // Stats
    if (relic!=null)
    {
      BasicStatsSet stats=relic.getStats();
      String[] lines=StatUtils.getStatsDisplayLines(stats);
      _stats.setText(lines);
    }
    else
    {
      _stats.setText(new String[0]);
    }
  }
}
