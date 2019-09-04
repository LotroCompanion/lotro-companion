package delta.games.lotro.gui.items.legendary.non_imbued;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.common.constraints.ClassAndSlot;
import delta.games.lotro.common.stats.StatsProvider;
import delta.games.lotro.gui.LotroIconsManager;
import delta.games.lotro.lore.items.legendary.global.LegendarySystem;
import delta.games.lotro.lore.items.legendary.non_imbued.AbstractNonImbuedLegacy;
import delta.games.lotro.lore.items.legendary.non_imbued.DefaultNonImbuedLegacy;
import delta.games.lotro.lore.items.legendary.non_imbued.DefaultNonImbuedLegacyInstance;

/**
 * Controller for the UI items of a single default non-imbued legacy.
 * @author DAM
 */
public class SingleDefaultNonImbuedLegacyEditionController extends SingleNonImbuedLegacyEditionController<DefaultNonImbuedLegacyInstance>
{
  /**
   * Constructor.
   * @param parent Parent window.
   * @param constraints Constraints.
   */
  public SingleDefaultNonImbuedLegacyEditionController(WindowController parent, ClassAndSlot constraints)
  {
    super(parent,constraints);
  }

  private DefaultNonImbuedLegacy getDefaultLegacy()
  {
    return (DefaultNonImbuedLegacy)_legacy;
  }

  @Override
  public void getData(DefaultNonImbuedLegacyInstance storage)
  {
    super.getData(storage);
    DefaultNonImbuedLegacy legacy=getDefaultLegacy();
    storage.setLegacy(legacy);
  }

  protected void handleButtonClick(JButton button)
  {
    //
  }

  protected void setupLegacy(AbstractNonImbuedLegacy legacy)
  {
    super.setupLegacy(legacy);
    DefaultNonImbuedLegacy defaultLegacy=getDefaultLegacy();
    if (defaultLegacy!=null)
    {
      LegendarySystem legendarySystem=new LegendarySystem();
      int[] internalRanks=legendarySystem.getRanksForMainLegacy(_itemReference,_itemLevel);
      updateRanksCombo(internalRanks);
    }
  }

  protected StatsProvider getStatsProvider()
  {
    DefaultNonImbuedLegacy legacy=getDefaultLegacy();
    StatsProvider statsProvider=legacy.getEffect().getStatsProvider();
    return statsProvider;
  }

  protected void updateIcon()
  {
    DefaultNonImbuedLegacy legacy=getDefaultLegacy();
    int iconId=legacy.getIconId();
    ImageIcon icon=LotroIconsManager.getLegacyIcon(iconId);
    _icon.setIcon(icon);
  }
}
