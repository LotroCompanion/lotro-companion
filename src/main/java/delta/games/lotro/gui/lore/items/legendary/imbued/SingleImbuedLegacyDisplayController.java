package delta.games.lotro.gui.lore.items.legendary.imbued;

import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.labels.MultilineLabel2;
import delta.games.lotro.character.stats.BasicStatsSet;
import delta.games.lotro.common.stats.StatUtils;
import delta.games.lotro.gui.LotroIconsManager;
import delta.games.lotro.lore.items.legendary.imbued.ImbuedLegacy;
import delta.games.lotro.lore.items.legendary.imbued.ImbuedLegacyInstance;

/**
 * Controller for the UI items to display a single imbued legacy.
 * @author DAM
 */
public class SingleImbuedLegacyDisplayController
{
  // UI
  // - legacy icon
  protected JLabel _icon;
  protected JLabel _rank;
  protected MultilineLabel2 _stats;

  /**
   * Constructor.
   */
  public SingleImbuedLegacyDisplayController()
  {
    // Icon
    _icon=GuiFactory.buildIconLabel(null);
    // Rank
    _rank=GuiFactory.buildLabel("?");
    // Stats
    _stats=new MultilineLabel2();
  }

  /**
   * Get the managed icon.
   * @return the managed icon.
   */
  public JLabel getIcon()
  {
    return _icon;
  }

  /**
   * Get the gadget for the rank.
   * @return a label.
   */
  public JLabel getRankGadget()
  {
    return _rank;
  }

  /**
   * Get the gadget for the stats.
   * @return a multi-lines label.
   */
  public MultilineLabel2 getStatsGadget()
  {
    return _stats;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // UI
    _icon=null;
    _rank=null;
    _stats=null;
  }

  /**
   * Set the data to display.
   * @param legacyInstance Legacy instance to display.
   */
  public void setLegacy(ImbuedLegacyInstance legacyInstance)
  {
    // Icon
    ImbuedLegacy legacy=legacyInstance.getLegacy();
    int iconId=legacy.getIconId();
    ImageIcon icon=LotroIconsManager.getLegacyIcon(iconId);
    _icon.setIcon(icon);
    // Rank
    int level=legacyInstance.getCurrentLevel();
    _rank.setText(String.valueOf(level));
    // Stats
    BasicStatsSet stats=legacyInstance.getStats();
    List<String> lines=StatUtils.getStatsForDisplay(stats);
    _stats.setText(lines);
  }
}
