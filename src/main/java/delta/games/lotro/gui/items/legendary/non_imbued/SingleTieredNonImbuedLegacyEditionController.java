package delta.games.lotro.gui.items.legendary.non_imbued;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.combobox.ComboBoxController;
import delta.common.ui.swing.combobox.ItemSelectionListener;
import delta.common.ui.swing.labels.MultilineLabel2;
import delta.common.ui.swing.text.IntegerEditionController;
import delta.common.ui.swing.text.NumberEditionController;
import delta.common.ui.swing.text.NumberListener;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.stats.BasicStatsSet;
import delta.games.lotro.common.CharacterClass;
import delta.games.lotro.common.constraints.ClassAndSlot;
import delta.games.lotro.common.stats.StatDescription;
import delta.games.lotro.common.stats.StatsProvider;
import delta.games.lotro.gui.LotroIconsManager;
import delta.games.lotro.gui.character.stats.StatDisplayUtils;
import delta.games.lotro.lore.items.EquipmentLocation;
import delta.games.lotro.lore.items.legendary.imbued.ImbuedLegacyInstance;
import delta.games.lotro.lore.items.legendary.non_imbued.NonImbuedLegacyTier;
import delta.games.lotro.lore.items.legendary.non_imbued.TieredNonImbuedLegacy;
import delta.games.lotro.lore.items.legendary.non_imbued.TieredNonImbuedLegacyInstance;
import delta.games.lotro.utils.FixedDecimalsInteger;

/**
 * Controller for the UI items of a single imbued legacy.
 * @author DAM
 */
public class SingleTieredNonImbuedLegacyEditionController
{
  // Data
  private TieredNonImbuedLegacyInstance _legacyInstance;
  private TieredNonImbuedLegacy _legacy;
  private ClassAndSlot _constraints;
  // Controllers
  private WindowController _parent;
  // GUI
  private JLabel _icon;
  private MultilineLabel2 _value;
  private JButton _chooseButton;
  // Current level
  private IntegerEditionController _currentRank;
  // Tier
  private ComboBoxController<Integer> _tier;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param legacyInstance Legacy to edit.
   * @param constraints Constraints.
   */
  public SingleTieredNonImbuedLegacyEditionController(WindowController parent, TieredNonImbuedLegacyInstance legacyInstance, ClassAndSlot constraints)
  {
    _parent=parent;
    _legacyInstance=legacyInstance;
    if (_legacyInstance!=null)
    {
      _legacy=_legacyInstance.getLegacy();
    }
    _constraints=constraints;
    // UI
    // - icon
    _icon=GuiFactory.buildTransparentIconlabel(32);
    // - value display
    _value=new MultilineLabel2();
    Dimension dimension=new Dimension(200,32);
    _value.setMinimumSize(dimension);
    _value.setSize(dimension);
    // - chooser button
    _chooseButton=GuiFactory.buildButton("...");
    ActionListener listener=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        handleButtonClick((JButton)e.getSource());
      }
    };
    _chooseButton.addActionListener(listener);
    // - current level
    JTextField rankTextField=GuiFactory.buildTextField("");
    _currentRank=new IntegerEditionController(rankTextField);
    NumberListener<Integer> rankListener=new NumberListener<Integer>()
    {
      @Override
      public void valueChanged(NumberEditionController<Integer> source, Integer newValue)
      {
        if (newValue!=null)
        {
          handleCurrentRankUpdate(newValue.intValue());
        }
      }
    };
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
    // - init listeners
    _currentRank.addValueListener(rankListener);
    _tier.addListener(tierListener);
  }

  /**
   * Extract data from UI to the given storage.
   * @param storage Storage to use.
   */
  public void getData(ImbuedLegacyInstance storage)
  {
    // Put UI data into the given storage
  }

  private void handleButtonClick(JButton button)
  {
    CharacterClass characterClass=_constraints.getCharacterClass();
    EquipmentLocation location=_constraints.getSlot();
    TieredNonImbuedLegacy legacy=NonImbuedLegacyChooser.selectTieredNonImbuedLegacy(_parent,characterClass,location,_legacy);
    if (legacy!=null)
    {
      setLegacy(legacy);
    }
  }

  private void handleCurrentRankUpdate(int rank)
  {
    updateStats();
  }

  private void handleTierUpdate(int tier)
  {
    updateIcon();
    updateStats();
  }

  private boolean hasLegacy()
  {
    return _legacy!=null;
  }

  private void setLegacy(TieredNonImbuedLegacy legacy)
  {
    _legacy=legacy;
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

  /**
   * Get the managed legacy.
   * @return the managed legacy.
   */
  public TieredNonImbuedLegacyInstance getLegacyInstance()
  {
    return _legacyInstance;
  }

  /**
   * Update UI to show the internal legacy data.
   */
  public void setUiFromLegacy()
  {
    // Update gadgets state
    updateGadgetsState();
    // Update UI to reflect the internal legacy data
    if (hasLegacy())
    {
      // - Update tier
      NonImbuedLegacyTier legacyTier=_legacyInstance.getLegacyTier();
      if (legacyTier!=null)
      {
        _tier.selectItem(Integer.valueOf(legacyTier.getTier()));
      }
      // - Update current rank
      int rank=_legacyInstance.getRank();
      _currentRank.setValue(Integer.valueOf(rank));
      // - Update derived UI
      updateIcon();
      updateStats();
    }
    else
    {
      // - Update stats
      updateStats();
    }
  }

  private void updateGadgetsState()
  {
    if (hasLegacy())
    {
      _tier.getComboBox().setEnabled(true);
      _currentRank.setState(true,true);
    }
    else
    {
      _tier.getComboBox().setEnabled(false);
      _currentRank.setState(false,false);
    }
  }

  private void updateStats()
  {
    if (hasLegacy())
    {
      int tier=getTier();
      NonImbuedLegacyTier legacyTier=_legacy.getTier(tier);
      StatsProvider statsProvider=legacyTier.getEffect().getStatsProvider();
      int rank=getRank();
      BasicStatsSet stats=statsProvider.getStats(1,rank);
      List<StatDescription> statDescriptions=stats.getSortedStats();
      int nbStats=statDescriptions.size();
      String[] lines=new String[nbStats];
      for(int i=0;i<nbStats;i++)
      {
        StatDescription stat=statDescriptions.get(i);
        String statName=stat.getName();
        FixedDecimalsInteger value=stats.getStat(stat);
        String valueStr=StatDisplayUtils.getStatDisplay(value,stat.isPercentage());
        String line=valueStr+" "+statName;
        lines[i]=line;
      }
      _value.setText(lines);
    }
    else
    {
      _value.setText(new String[]{});
    }
  }

  private void updateIcon()
  {
    int tier=getTier();
    boolean major=_legacy.isMajor();
    ImageIcon icon=LotroIconsManager.getLegacyIcon(tier,major);
    _icon.setIcon(icon);
  }

  private int getTier()
  {
    Integer tier=_tier.getSelectedItem();
    return tier!=null?tier.intValue():1;
  }

  private int getRank()
  {
    Integer rank=_currentRank.getValue();
    return rank!=null?rank.intValue():1;
  }

  /**
   * Get the icon gadget.
   * @return the icon gadget.
   */
  public JLabel getIcon()
  {
    return _icon;
  }

  /**
   * Get the label to display the legacy stats.
   * @return a multiline label.
   */
  public MultilineLabel2 getValueLabel()
  {
    return _value;
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
   * Get the edition controller for the current rank.
   * @return an integer edition controller.
   */
  public IntegerEditionController getCurrentRankEditionController()
  {
    return _currentRank;
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
    // Data
    _legacyInstance=null;
    _legacy=null;
    _constraints=null;
    // Controllers
    _parent=null;
    // UI
    _icon=null;
    _value=null;
    _chooseButton=null;
    if (_currentRank!=null)
    {
      _currentRank.dispose();
      _currentRank=null;
    }
    if (_tier!=null)
    {
      _tier.dispose();
      _tier=null;
    }
  }
}
