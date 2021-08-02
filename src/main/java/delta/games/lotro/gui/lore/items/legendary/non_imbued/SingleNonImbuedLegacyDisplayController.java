package delta.games.lotro.gui.lore.items.legendary.non_imbued;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.labels.MultilineLabel2;
import delta.games.lotro.character.stats.BasicStatsSet;
import delta.games.lotro.common.stats.StatUtils;
import delta.games.lotro.gui.LotroIconsManager;
import delta.games.lotro.lore.items.legendary.non_imbued.NonImbuedLegacyTier;
import delta.games.lotro.lore.items.legendary.non_imbued.TieredNonImbuedLegacy;
import delta.games.lotro.lore.items.legendary.non_imbued.TieredNonImbuedLegacyInstance;

/**
 * Controller for the UI items to display a single non imbued legacy.
 * @author DAM
 */
public class SingleNonImbuedLegacyDisplayController
{
  // UI
  // - legacy icon
  protected JLabel _icon;
  protected JLabel _rank;
  protected MultilineLabel2 _stats;

  /**
   * Constructor.
   */
  public SingleNonImbuedLegacyDisplayController()
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
   * @param legacy Legacy to display.
   */
  public void setLegacy(TieredNonImbuedLegacyInstance legacy)
  {
    // Icon
    TieredNonImbuedLegacy tieredLegacy=legacy.getLegacy();
    NonImbuedLegacyTier legacyTier=legacy.getLegacyTier();
    int tier=legacyTier.getTier();
    boolean major=tieredLegacy.isMajor();
    ImageIcon icon=LotroIconsManager.getLegacyIcon(tier,major);
    _icon.setIcon(icon);
    // Rank
    Integer rank=legacy.getUiRank();
    String rankStr=(rank!=null)?rank.toString():"?";
    _rank.setText(rankStr);
    // Stats
    BasicStatsSet stats=legacy.getStats();
    String[] lines=StatUtils.getStatsDisplayLines(stats);
    _stats.setText(lines);
  }
}
