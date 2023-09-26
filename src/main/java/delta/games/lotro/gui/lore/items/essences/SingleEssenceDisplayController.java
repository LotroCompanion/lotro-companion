package delta.games.lotro.gui.lore.items.essences;

import javax.swing.Icon;

import delta.games.lotro.character.stats.BasicStatsSet;
import delta.games.lotro.common.enums.SocketType;
import delta.games.lotro.common.stats.StatUtils;
import delta.games.lotro.gui.LotroIconsManager;
import delta.games.lotro.gui.lore.items.ItemUiTools;
import delta.games.lotro.gui.lore.items.utils.IconNameStatsBundle;
import delta.games.lotro.lore.items.essences.Essence;

/**
 * Controller for the UI items to display a single essence.
 * @author DAM
 */
public class SingleEssenceDisplayController extends IconNameStatsBundle
{
  private SocketType _type;

  /**
   * Constructor.
   * @param type Socket type.
   */
  public SingleEssenceDisplayController(SocketType type)
  {
    super();
    _type=type;
    _icon.setEnabled(false);
    // Initialize with no essence
    setEssence(null);
  }

  /**
   * Set current essence.
   * @param essence Essence to set.
   */
  public void setEssence(Essence essence)
  {
    // Set essence icon
    Icon icon=null;
    if (essence!=null)
    {
      icon=ItemUiTools.buildItemIcon(essence);
    }
    else
    {
      icon=LotroIconsManager.getDefaultEssenceIcon(_type.getCode());
    }
    _icon.setIcon(icon);
    _icon.setDisabledIcon(icon);
    // Text
    String text="";
    if (essence!=null)
    {
      text=essence.getName();
    }
    setName(text);
    // Stats
    if (essence!=null)
    {
      BasicStatsSet stats=essence.getStats();
      String[] lines=StatUtils.getStatsDisplayLines(stats);
      setStats(lines);
    }
    else
    {
      setStats(new String[0]);
    }
  }
}
