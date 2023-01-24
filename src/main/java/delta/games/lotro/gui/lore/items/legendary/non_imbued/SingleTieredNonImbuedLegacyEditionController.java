package delta.games.lotro.gui.lore.items.legendary.non_imbued;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.combobox.ComboBoxController;
import delta.common.ui.swing.combobox.ItemSelectionListener;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.classes.ClassDescription;
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
  private JButton _chooseButton;
  private JButton _deleteButton;
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
    // - chooser button
    _chooseButton=GuiFactory.buildButton("...");
    {
      ActionListener listener=new ActionListener()
      {
        @Override
        public void actionPerformed(ActionEvent e)
        {
          handleChooseLegacy((JButton)e.getSource());
        }
      };
      _chooseButton.addActionListener(listener);
    }
    // - delete button
    _deleteButton=GuiFactory.buildIconButton("/resources/gui/icons/cross.png");
    {
      ActionListener listener=new ActionListener()
      {
        @Override
        public void actionPerformed(ActionEvent e)
        {
          handleDeleteLegacy((JButton)e.getSource());
        }
      };
      _deleteButton.addActionListener(listener);
    }
    // - tier
    _tier=buildTiersComboBox();
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
    else
    {
      storage.setLegacyTier(null);
    }
  }

  private void handleChooseLegacy(JButton button)
  {
    ClassDescription characterClass=_constraints.getCharacterClass();
    EquipmentLocation location=_constraints.getSlot();
    TieredNonImbuedLegacy oldLegacy=getTieredLegacy();
    TieredNonImbuedLegacy legacy=NonImbuedLegacyChooser.selectTieredNonImbuedLegacy(_parent,characterClass,location,oldLegacy);
    if (legacy!=null)
    {
      setLegacy(legacy);
    }
  }

  private void handleDeleteLegacy(JButton button)
  {
    setLegacy(null);
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

  private ComboBoxController<Integer> buildTiersComboBox()
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

  @Override
  protected void setupLegacy(AbstractNonImbuedLegacy legacy)
  {
    super.setupLegacy(legacy);
    TieredNonImbuedLegacy tieredLegacy=getLegacy();
    LegendarySystem legendarySystem=LegendarySystem.getInstance();
    if (tieredLegacy!=null)
    {
      Integer tierInt=_tier.getSelectedItem();
      int tier=tierInt!=null?tierInt.intValue():1;
      NonImbuedLegacyTier legacyTier=tieredLegacy.getTier(tier);
      ItemQuality quality=_itemReference.getQuality();
      int[] internalRanks=legendarySystem.getRanksForLegacyTier(_itemLevel,quality,legacyTier);
      updateRanksCombo(internalRanks);
    }
    else
    {
      int ranks=legendarySystem.getData().getMaxUiRank();
      updateRanksComboWithDefaultRanks(ranks);
    }
  }

  @Override
  protected void updateGadgetsState()
  {
    super.updateGadgetsState();
    boolean enabled=hasLegacy();
    _tier.getComboBox().setEnabled(enabled);
  }

  @Override
  protected StatsProvider getStatsProvider()
  {
    int tier=getTier();
    TieredNonImbuedLegacy legacy=getTieredLegacy();
    NonImbuedLegacyTier legacyTier=legacy.getTier(tier);
    StatsProvider statsProvider=legacyTier.getEffect().getStatsProvider();
    return statsProvider;
  }

  @Override
  protected void updateIcon()
  {
    ImageIcon icon=null;
    TieredNonImbuedLegacy legacy=getTieredLegacy();
    if (legacy!=null)
    {
      int tier=getTier();
      boolean major=legacy.isMajor();
      icon=LotroIconsManager.getLegacyIcon(tier,major);
    }
    _icon.setIcon(icon);
  }

  private int getTier()
  {
    Integer tier=_tier.getSelectedItem();
    return tier!=null?tier.intValue():1;
  }

  /**
   * Get the managed choose button.
   * @return the managed choose button.
   */
  public JButton getChooseButton()
  {
    return _chooseButton;
  }

  /**
   * Get the managed 'delete' button.
   * @return the managed 'delete' button.
   */
  public JButton getDeleteButton()
  {
    return _deleteButton;
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
  @Override
  public void dispose()
  {
    super.dispose();
    _chooseButton=null;
    _deleteButton=null;
    if (_tier!=null)
    {
      _tier.dispose();
      _tier=null;
    }
  }
}
