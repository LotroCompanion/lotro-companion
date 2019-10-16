package delta.games.lotro.gui.items.legendary.imbued;

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
import delta.games.lotro.lore.items.legendary.imbued.ImbuedLegacy;
import delta.games.lotro.lore.items.legendary.imbued.ImbuedLegacyInstance;
import delta.games.lotro.lore.items.legendary.imbued.ImbuedLegendaryInstanceAttrs;

/**
 * Panel to display the attributes of an imbued legendary item instance (levels, legacies).
 * @author DAM
 */
public class ImbuedLegendaryAttrsDisplayPanelController
{
  // UI
  private JPanel _panel;
  private JLabel _levels;
  // Legacies
  private JPanel _legaciesPanel;

  /**
   * Constructor.
   */
  public ImbuedLegendaryAttrsDisplayPanelController()
  {
    _levels=GuiFactory.buildLabel("");
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

    // Line 1: levels
    JPanel line1Panel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
    // - level
    line1Panel.add(GuiFactory.buildLabel("Levels:"));
    line1Panel.add(_levels);
    panel.add(line1Panel,c);
    c.gridy++;

    return panel;
  }

  private void fillLegaciesPanel(ImbuedLegendaryInstanceAttrs attrs)
  {
    _legaciesPanel.removeAll();
    // Legacies
    int lineIndex=0;
    List<ImbuedLegacyInstance> legacyInstances=attrs.getStandardLegacies();
    for(ImbuedLegacyInstance legacyInstance : legacyInstances)
    {
      ImbuedLegacy legacy=legacyInstance.getLegacy();
      if (legacy!=null)
      {
        SingleImbuedLegacyDisplayController legacyGadgets=new SingleImbuedLegacyDisplayController();
        legacyGadgets.setLegacy(legacyInstance);
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
  public void setData(ImbuedLegendaryInstanceAttrs attrs)
  {
    // Levels
    int tiers=attrs.getTotalTiers();
    int maxTiers=attrs.getMaxTotalTiers();
    _levels.setText(tiers+" / "+maxTiers);
    // Legacies
    fillLegaciesPanel(attrs);
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    _levels=null;
    if (_legaciesPanel!=null)
    {
      _legaciesPanel.removeAll();
      _legaciesPanel=null;
    }
  }
}
