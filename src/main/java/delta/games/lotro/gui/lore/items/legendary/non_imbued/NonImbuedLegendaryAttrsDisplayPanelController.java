package delta.games.lotro.gui.lore.items.legendary.non_imbued;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.labels.MultilineLabel2;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemInstance;
import delta.games.lotro.lore.items.legendary.LegendaryInstance;
import delta.games.lotro.lore.items.legendary.LegendaryInstanceAttrs;
import delta.games.lotro.lore.items.legendary.global.LegendarySystem;
import delta.games.lotro.lore.items.legendary.non_imbued.NonImbuedLegacyTier;
import delta.games.lotro.lore.items.legendary.non_imbued.NonImbuedLegendaryInstanceAttrs;
import delta.games.lotro.lore.items.legendary.non_imbued.TieredNonImbuedLegacyInstance;

/**
 * Panel to display the attributes of a non-imbued legendary item instance (upgrades, level, points, legacies).
 * @author DAM
 */
public class NonImbuedLegendaryAttrsDisplayPanelController
{
  // Data
  private ItemInstance<? extends Item> _itemInstance;
  // UI
  private JPanel _panel;
  private JLabel _available;
  private JLabel _spent;
  private JLabel _level;
  private JLabel _upgrades;
  // Infos
  private JPanel _infosPanel;
  // Legacies
  private JPanel _legaciesPanel;
  private JPanel _legaciesDisplayPanel;

  /**
   * Constructor.
   * @param itemInstance Item instance.
   */
  public NonImbuedLegendaryAttrsDisplayPanelController(ItemInstance<? extends Item> itemInstance)
  {
    _itemInstance=itemInstance;
    _available=GuiFactory.buildLabel("");
    _spent=GuiFactory.buildLabel("");
    _level=GuiFactory.buildLabel("");
    _upgrades=GuiFactory.buildLabel("");
    _infosPanel=buildInfoPanel();
    _legaciesPanel=buildLegaciesPanel();
  }

  /**
   * Get the infos panel.
   * @return the infos panel.
   */
  public JPanel getInfosPanel()
  {
    return _infosPanel;
  }

  /**
   * Get the legacies panel.
   * @return the legacies panel.
   */
  public JPanel getLegaciesPanel()
  {
    return _legaciesPanel;
  }

  private JPanel buildInfoPanel()
  {
    // Level and upgrades
    JPanel ret=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
    // - level
    ret.add(GuiFactory.buildLabel("Level:"));
    ret.add(_level);
    // - upgrades
    ret.add(GuiFactory.buildLabel("Upgrades:"));
    ret.add(_upgrades);
    return ret;
  }

  private JPanel buildLegaciesPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new BorderLayout());
    JPanel pointsPanel=buildPointsPanel();
    panel.add(pointsPanel,BorderLayout.NORTH);
    _legaciesDisplayPanel=GuiFactory.buildPanel(new GridBagLayout());
    panel.add(_legaciesDisplayPanel,BorderLayout.CENTER);
    return panel;
  }

  private JPanel buildPointsPanel()
  {
    JPanel ret=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
    // - available
    ret.add(GuiFactory.buildLabel("Points: Available:"));
    ret.add(_available);
    // - spent
    ret.add(GuiFactory.buildLabel(" / Spent:"));
    ret.add(_spent);
    return ret;
  }

  private void fillLegaciesPanel(NonImbuedLegendaryInstanceAttrs attrs)
  {
    LegendarySystem legendary=LegendarySystem.getInstance();
    _legaciesDisplayPanel.removeAll();
    // Legacies
    int lineIndex=0;
    List<TieredNonImbuedLegacyInstance> legacies=attrs.getLegacies();
    for(TieredNonImbuedLegacyInstance legacy : legacies)
    {
      NonImbuedLegacyTier legacyTier=legacy.getLegacyTier();
      if (legacyTier!=null)
      {
        SingleNonImbuedLegacyDisplayController legacyGadgets=new SingleNonImbuedLegacyDisplayController();
        legacyGadgets.setLegacy(legacy);
        // Icon
        JLabel icon=legacyGadgets.getIcon();
        GridBagConstraints c=new GridBagConstraints(0,lineIndex,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,5,2,0),0,0);
        _legaciesDisplayPanel.add(icon,c);
        // Rank
        JLabel rank=legacyGadgets.getRankGadget();
        c=new GridBagConstraints(1,lineIndex,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,2,0,5),0,0);
        _legaciesDisplayPanel.add(rank,c);
        Integer uiRank=legendary.computeUiRankForTieredLegacy(_itemInstance,legacy);
        String rankStr=(uiRank!=null)?uiRank.toString():"?";
        rank.setText(rankStr);
        // Stats
        MultilineLabel2 stats=legacyGadgets.getStatsGadget();
        c=new GridBagConstraints(2,lineIndex,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
        _legaciesDisplayPanel.add(stats,c);
        lineIndex++;
      }
    }
  }

  /**
   * Update the data to display.
   */
  public void update()
  {
    LegendaryInstance legendaryInstance=(LegendaryInstance)_itemInstance;
    LegendaryInstanceAttrs legendaryAttrs=legendaryInstance.getLegendaryAttributes();
    NonImbuedLegendaryInstanceAttrs attrs=legendaryAttrs.getNonImbuedAttrs();
    // Level
    int legendaryLevel=attrs.getLegendaryItemLevel();
    _level.setText(String.valueOf(legendaryLevel));
    // Upgrades
    int nbUpgrades=attrs.getNbUpgrades();
    _upgrades.setText(String.valueOf(nbUpgrades));
    // Points
    int pointsLeft=attrs.getPointsLeft();
    _available.setText(String.valueOf(pointsLeft));
    int pointsSpent=attrs.getPointsSpent();
    _spent.setText(String.valueOf(pointsSpent));

    // Legacies
    fillLegaciesPanel(attrs);
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Data
    _itemInstance=null;
    // UI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    _available=null;
    _spent=null;
    _level=null;
    _upgrades=null;
    if (_infosPanel!=null)
    {
      _infosPanel.removeAll();
      _infosPanel=null;
    }
    if (_legaciesPanel!=null)
    {
      _legaciesPanel.removeAll();
      _legaciesPanel=null;
    }
    if (_legaciesDisplayPanel!=null)
    {
      _legaciesDisplayPanel.removeAll();
      _legaciesDisplayPanel=null;
    }
  }
}
