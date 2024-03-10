package delta.games.lotro.gui.lore.traits;

import javax.swing.Icon;

import delta.common.ui.swing.icons.IconsManager;
import delta.common.utils.text.EndOfLine;
import delta.games.lotro.character.stats.BasicStatsSet;
import delta.games.lotro.character.traits.TraitDescription;
import delta.games.lotro.common.stats.StatUtils;
import delta.games.lotro.common.stats.StatsProvider;
import delta.games.lotro.gui.LotroIconsManager;

/**
 * @author dm
 */
public class TraitIconUtils
{
  private static final String NO_TRAIT_ICON="/resources/gui/traits/noTrait.png";

  /**
   * Get the icon for the given virtue.
   * @param trait A virtue identifier or <code>null</code>.
   * @return An icon with embedded text to display tier.
   */
  public static Icon buildTraitIcon(TraitDescription trait)
  {
    Icon icon=null;
    if (trait!=null)
    {
      icon=LotroIconsManager.getTraitIcon(trait.getIconId());
    }
    else
    {
      icon=IconsManager.getIcon(NO_TRAIT_ICON);
    }
    return icon;
  }

  /**
   * Compute a tooltip for a trait.
   * @param trait Trait to use.
   * @param level Character level.
   * @return the HTML tooltip.
   */
  public static String buildToolTip(TraitDescription trait, int level)
  {
    StringBuilder sb=new StringBuilder();
    sb.append(trait.getName()).append(EndOfLine.NATIVE_EOL);
    StatsProvider statsProvider=trait.getStatsProvider();
    if (statsProvider!=null)
    {
      BasicStatsSet stats=statsProvider.getStats(1,level);
      addStatsTooltipText(stats,sb);
    }
    String text=sb.toString().trim();
    String html="<html>"+text.replace(EndOfLine.NATIVE_EOL,"<br>")+"</html>";
    return html;
  }

  private static void addStatsTooltipText(BasicStatsSet stats, StringBuilder sb)
  {
    if (stats!=null)
    {
      String[] lines=StatUtils.getStatsDisplayLines(stats);
      for(String line : lines)
      {
        sb.append(line).append(EndOfLine.NATIVE_EOL);
      }
    }
  }

  //JLabel iconLabel=GuiFactory.buildIconLabel(icon);
}
