package delta.games.lotro.gui.items.legendary.non_imbued;

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
import delta.games.lotro.lore.items.legendary.non_imbued.NonImbuedLegacyTier;
import delta.games.lotro.lore.items.legendary.non_imbued.NonImbuedLegendaryInstanceAttrs;
import delta.games.lotro.lore.items.legendary.non_imbued.TieredNonImbuedLegacyInstance;

/**
 * Panel to display the attributes of a non-imbued legendary item instance (upgrades, level, points, legacies).
 * @author DAM
 */
public class NonImbuedLegendaryAttrsDisplayPanelController
{
  // UI
  private JPanel _panel;
  private JLabel _available;
  private JLabel _spent;
  private JLabel _level;
  private JLabel _upgrades;
  // Legacies
  private JPanel _legaciesPanel;

  /**
   * Constructor.
   */
  public NonImbuedLegendaryAttrsDisplayPanelController()
  {
    _available=GuiFactory.buildLabel("");
    _spent=GuiFactory.buildLabel("");
    _level=GuiFactory.buildLabel("");
    _upgrades=GuiFactory.buildLabel("");
    _legaciesPanel=GuiFactory.buildPanel(new GridBagLayout());
    _panel=buildPanel();
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  public JPanel getPanel()
  {
    return _panel;
  }

  private JPanel buildPanel()
  {
    JPanel ret=GuiFactory.buildPanel(new BorderLayout());
    // Info
    JPanel infoPanel=buildInfoPanel();
    ret.add(infoPanel,BorderLayout.NORTH);
    // Legacies
    ret.add(_legaciesPanel,BorderLayout.SOUTH);
    return ret;
  }

  private JPanel buildInfoPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);

    // Line 1: level and upgrades
    JPanel line1Panel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
    // - level
    line1Panel.add(GuiFactory.buildLabel("Level:"));
    line1Panel.add(_level);
    // - upgrades
    line1Panel.add(GuiFactory.buildLabel("Upgrades:"));
    line1Panel.add(_upgrades);
    panel.add(line1Panel,c);
    c.gridy++;

    // Line 2: points
    JPanel line2Panel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
    // - available
    line2Panel.add(GuiFactory.buildLabel("Available:"));
    line2Panel.add(_available);
    // - spent
    line2Panel.add(GuiFactory.buildLabel("Spent:"));
    line2Panel.add(_spent);
    panel.add(line2Panel,c);
    c.gridy++;
    return panel;
  }

  private void fillLegaciesPanel(NonImbuedLegendaryInstanceAttrs attrs)
  {
    _legaciesPanel.removeAll();
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
        _legaciesPanel.add(icon,c);
        // Rank
        JLabel rank=legacyGadgets.getRankGadget();
        c=new GridBagConstraints(1,lineIndex,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,2,0,5),0,0);
        _legaciesPanel.add(rank,c);
        // Stats
        MultilineLabel2 stats=legacyGadgets.getStatsGadget();
        c=new GridBagConstraints(2,lineIndex,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
        _legaciesPanel.add(stats,c);
        lineIndex++;
      }
    }
  }

  /**
   * Set the data to display.
   * @param attrs Data to display.
   */
  public void setData(NonImbuedLegendaryInstanceAttrs attrs)
  {
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
}
