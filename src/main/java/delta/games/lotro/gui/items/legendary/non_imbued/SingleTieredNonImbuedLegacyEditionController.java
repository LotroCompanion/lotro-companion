package delta.games.lotro.gui.items.legendary.non_imbued;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import delta.common.ui.swing.combobox.ComboBoxController;
import delta.common.ui.swing.combobox.ItemSelectionListener;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.common.CharacterClass;
import delta.games.lotro.common.constraints.ClassAndSlot;
import delta.games.lotro.common.stats.StatsProvider;
import delta.games.lotro.gui.LotroIconsManager;
import delta.games.lotro.lore.items.EquipmentLocation;
import delta.games.lotro.lore.items.ItemQuality;
import delta.games.lotro.lore.items.legendary.global.LegendarySystem;
import delta.games.lotro.lore.items.legendary.non_imbued.AbstractNonImbuedLegacy;
import delta.games.lotro.lore.items.legendary.non_imbued.NonImbuedLegacyTier;
import delta.games.lotro.lore.items.legendary.non_imbued.TieredNonImbuedLegacy;
import delta.games.lotro.lore.items.legendary.non_imbued.TieredNonImbuedLegacyInstance;

/**
 * Controller for the UI items of a single tiered non-imbued legacy.
 * @author DAM
 */
public class SingleTieredNonImbuedLegacyEditionController extends SingleNonImbuedLegacyEditionController<TieredNonImbuedLegacyInstance>
{
  // GUI
  // - tier
  private ComboBoxController<Integer> _tier;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param constraints Constraints.
   */
  public SingleTieredNonImbuedLegacyEditionController(WindowController parent, ClassAndSlot constraints)
  {
    super(parent,constraints);
    // UI
    // - tier
    _tier=buildTiersCombos();
    ItemSelectionListener<Integer> tierListener=new ItemSelectionListener<Integer>()
    {
      @Override
      public void itemSelected(Integer tier)
      {
        if (tier!=null)
        {
          handleTierUpdate(tier.intValue());
        }
      }
    };
    _tier.addListener(tierListener);
  }

  private TieredNonImbuedLegacy getTieredLegacy()
  {
    return (TieredNonImbuedLegacy)_legacy;
  }

  @Override
  public void getData(TieredNonImbuedLegacyInstance storage)
  {
    super.getData(storage);
    TieredNonImbuedLegacy legacy=getTieredLegacy();
    if (legacy!=null)
    {
      int tier=_tier.getSelectedItem().intValue();
      NonImbuedLegacyTier legacyTier=legacy.getTier(tier);
      storage.setLegacyTier(legacyTier);
    }
  }

  protected void handleButtonClick(JButton button)
  {
    CharacterClass characterClass=_constraints.getCharacterClass();
    EquipmentLocation location=_constraints.getSlot();
    TieredNonImbuedLegacy oldLegacy=getTieredLegacy();
    TieredNonImbuedLegacy legacy=NonImbuedLegacyChooser.selectTieredNonImbuedLegacy(_parent,characterClass,location,oldLegacy);
    if (legacy!=null)
    {
      setLegacy(legacy);
    }
  }

  private void handleTierUpdate(int tier)
  {
    updateIcon();
    updateStats();
  }

  private void setLegacy(TieredNonImbuedLegacy legacy)
  {
    setupLegacy(legacy);
    updateGadgetsState();
    Integer tier=_tier.getSelectedItem();
    handleTierUpdate(tier!=null?tier.intValue():1);
  }

  private ComboBoxController<Integer> buildTiersCombos()
  {
    ComboBoxController<Integer> ret=new ComboBoxController<Integer>();
    for(int i=1;i<=6;i++)
    {
      ret.addItem(Integer.valueOf(i),"Tier "+i);
    }
    return ret;
  }

  @Override
  public void updateUiFromLegacy(TieredNonImbuedLegacyInstance legacyInstance)
  {
    // Update UI to reflect the internal legacy data
    if (hasLegacy())
    {
      // - Update tier
      NonImbuedLegacyTier legacyTier=legacyInstance.getLegacyTier();
      if (legacyTier!=null)
      {
        _tier.selectItem(Integer.valueOf(legacyTier.getTier()));
      }
    }
    super.updateUiFromLegacy(legacyInstance);
  }

  private TieredNonImbuedLegacy getLegacy()
  {
    return (TieredNonImbuedLegacy)_legacy;
  }

  protected void setupLegacy(AbstractNonImbuedLegacy legacy)
  {
    super.setupLegacy(legacy);
    TieredNonImbuedLegacy tieredLegacy=getLegacy();
    if (tieredLegacy!=null)
    {
      Integer tierInt=_tier.getSelectedItem();
      int tier=tierInt!=null?tierInt.intValue():1;
      NonImbuedLegacyTier legacyTier=tieredLegacy.getTier(tier);
      LegendarySystem legendarySystem=new LegendarySystem();
      ItemQuality quality=_itemReference.getQuality();
      int[] internalRanks=legendarySystem.getRanksForLegacyTier(_itemLevel,quality,legacyTier);
      updateRanksCombo(internalRanks);
    }
  }

  protected void updateGadgetsState()
  {
    super.updateGadgetsState();
    if (hasLegacy())
    {
      _tier.getComboBox().setEnabled(true);
    }
    else
    {
      _tier.getComboBox().setEnabled(false);
    }
  }

  protected StatsProvider getStatsProvider()
  {
    int tier=getTier();
    TieredNonImbuedLegacy legacy=getTieredLegacy();
    NonImbuedLegacyTier legacyTier=legacy.getTier(tier);
    StatsProvider statsProvider=legacyTier.getEffect().getStatsProvider();
    return statsProvider;
  }

  protected void updateIcon()
  {
    int tier=getTier();
    TieredNonImbuedLegacy legacy=getTieredLegacy();
    boolean major=legacy.isMajor();
    ImageIcon icon=LotroIconsManager.getLegacyIcon(tier,major);
    _icon.setIcon(icon);
  }

  private int getTier()
  {
    Integer tier=_tier.getSelectedItem();
    return tier!=null?tier.intValue():1;
  }

  /**
   * Get the combo-box controller for the tier.
   * @return a combo-box controller.
   */
  public ComboBoxController<Integer> getTierCombo()
  {
    return _tier;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    super.dispose();
    if (_tier!=null)
    {
      _tier.dispose();
      _tier=null;
    }
  }
}
