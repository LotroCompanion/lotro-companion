package delta.games.lotro.gui.maps.resources;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.combobox.ComboBoxController;
import delta.common.ui.swing.combobox.ComboBoxItem;
import delta.common.ui.swing.combobox.ItemSelectionListener;
import delta.games.lotro.gui.common.crafting.CraftingUiUtils;
import delta.games.lotro.lore.crafting.CraftingLevel;
import delta.games.lotro.lore.crafting.Profession;
import delta.games.lotro.lore.crafting.ProfessionComparator;
import delta.games.lotro.lore.maps.resources.ResourcesMapDescriptor;
import delta.games.lotro.lore.maps.resources.ResourcesMapsManager;

/**
 * Controller for a panel to choose a crafting level (profession+tier).
 * @author DAM
 */
public class CraftingLevelSelectionPanelController
{
  // Data
  private CraftingLevel _currentLevel;
  private Map<Profession,List<CraftingLevel>> _supportedTiers;
  // Controllers
  private ComboBoxController<Profession> _profession;
  private ComboBoxController<Integer> _tier;
  private MapSelectionPanelController _mapSelectionCtrl;
  // UI
  private JPanel _panel;

  /**
   * Constructor.
   * @param mapSelectionPanelController The associated map selection panel.
   */
  public CraftingLevelSelectionPanelController(MapSelectionPanelController mapSelectionPanelController)
  {
    _currentLevel=null;
    initSupportedTiers();
    _mapSelectionCtrl=mapSelectionPanelController;
    _panel=null;
  }

  /**
   * Get the current crafting level.
   * @return the current crafting level.
   */
  public CraftingLevel getCraftingLevel()
  {
    return _currentLevel;
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  public JPanel getPanel()
  {
    if (_panel==null)
    {
      _panel=buildSelectionPanel();
    }
    return _panel;
  }

  private JPanel buildSelectionPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    JPanel line2Panel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING,5,0));
    List<Profession> professions=getSupportedProfessions();
    // Profession
    {
      JLabel label=GuiFactory.buildLabel("Profession:"); // I18n
      line2Panel.add(label);
      _profession=CraftingUiUtils.buildProfessionCombo(professions,false);
      ItemSelectionListener<Profession> professionListener=new ItemSelectionListener<Profession>()
      {
        @Override
        public void itemSelected(Profession profession)
        {
          updateTiersCombo(profession);
          select(profession,_tier.getSelectedItem());
        }
      };
      _profession.addListener(professionListener);
      line2Panel.add(_profession.getComboBox());
    }
    // Tier
    List<CraftingLevel> tiers=getSupportedLevels(professions.get(0));
    {
      JLabel label=GuiFactory.buildLabel("Tier:"); // I18n
      line2Panel.add(label);
      _tier=CraftingUiUtils.buildTierCombo(tiers,false);
      ItemSelectionListener<Integer> typeListener=new ItemSelectionListener<Integer>()
      {
        @Override
        public void itemSelected(Integer tier)
        {
          select(_profession.getSelectedItem(),tier);
        }
      };
      _tier.addListener(typeListener);
      line2Panel.add(_tier.getComboBox());
    }
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,5,0),0,0);
    panel.add(line2Panel,c);
    _profession.selectItem(professions.get(0));
    _tier.selectItem(Integer.valueOf(tiers.get(0).getTier()));
    return panel;
  }

  private void initSupportedTiers()
  {
    _supportedTiers=new HashMap<Profession,List<CraftingLevel>>();
    ResourcesMapsManager mapsMgr=ResourcesMapsManager.getInstance();
    for(ResourcesMapDescriptor mapDescriptor : mapsMgr.getResourcesMaps())
    {
      CraftingLevel level=mapDescriptor.getLevel();
      Profession profession=level.getProfession();
      List<CraftingLevel> levels=_supportedTiers.get(profession);
      if (levels==null)
      {
        levels=new ArrayList<CraftingLevel>();
        _supportedTiers.put(profession,levels);
      }
      levels.add(level);
    }
  }

  private List<Profession> getSupportedProfessions()
  {
    List<Profession> professions=new ArrayList<Profession>();
    professions.addAll(_supportedTiers.keySet());
    Collections.sort(professions, new ProfessionComparator());
    return professions;
  }

  private List<CraftingLevel> getSupportedLevels(Profession profession)
  {
    List<CraftingLevel> ret=_supportedTiers.get(profession);
    return ret;
  }

  private void updateTiersCombo(Profession profession)
  {
    List<ComboBoxItem<Integer>> newItems=new ArrayList<ComboBoxItem<Integer>>();
    List<CraftingLevel> levels=getSupportedLevels(profession);
    for(CraftingLevel level : levels)
    {
      int tier=level.getTier();
      String name=level.getName();
      ComboBoxItem<Integer> item=new ComboBoxItem<Integer>(Integer.valueOf(tier),name);
      newItems.add(item);
    }
    _tier.updateItems(newItems);
  }

  private void select(Profession profession, Integer tier)
  {
    if ((profession==null) || (tier==null))
    {
      return;
    }
    if ((_currentLevel==null) || (profession!=_currentLevel.getProfession()) || (tier.intValue()!=_currentLevel.getTier()))
    {
      CraftingLevel level=profession.getByTier(tier.intValue());
      _currentLevel=level;
      _mapSelectionCtrl.initMaps(_currentLevel);
    }
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    _currentLevel=null;
    _supportedTiers=null;
    if (_profession!=null)
    {
      _profession.dispose();
      _profession=null;
    }
    if (_tier!=null)
    {
      _tier.dispose();
      _tier=null;
    }
    _mapSelectionCtrl=null;
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
  }
}
