package delta.games.lotro.gui.character.status.crafting;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.games.lotro.character.status.crafting.ProfessionStatus;
import delta.games.lotro.lore.crafting.CraftingLevel;

/**
 * Controller for a panel that displays the history of a single profession.
 * @author DAM
 */
public class ProfessionHistoryChartPanelController
{
  // GUI
  private JPanel _panel;
  private JLabel _masteryLabel;
  private JLabel _proficiencyLabel;
  private CraftingHistoryChartController _history;
  // Data
  private ProfessionStatus _stats;

  /**
   * Constructor.
   * @param stats Profession stats to display.
   */
  public ProfessionHistoryChartPanelController(ProfessionStatus stats)
  {
    _stats=stats;
  }

  /**
   * Get the managed panel.
   * @return a panel.
   */
  public JPanel getPanel()
  {
    if (_panel==null)
    {
      _panel=buildPanel();
    }
    return _panel;
  }

  private JPanel buildPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    // Mastery label
    CraftingLevel mastery=_stats.getMasteryLevel();
    String masteryStr=mastery.getCraftTier().getLabel()+" ("+mastery.getTier()+")";
    _masteryLabel=GuiFactory.buildLabel("Mastery: "+masteryStr); // I18n
    // Proficiency label
    CraftingLevel proficiency=_stats.getProficiencyLevel();
    String proficiencyStr=proficiency.getCraftTier().getLabel()+" ("+proficiency.getTier()+")";
    _proficiencyLabel=GuiFactory.buildLabel("Proficiency: "+proficiencyStr); // I18n
    // History chart
    _history=new CraftingHistoryChartController(_stats,false);
    JPanel historyPanel=_history.getPanel();

    // Assembly
    JPanel labelsPanel=GuiFactory.buildPanel(null);
    labelsPanel.setLayout(new BoxLayout(labelsPanel,BoxLayout.PAGE_AXIS));
    _masteryLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
    labelsPanel.add(_masteryLabel);
    _proficiencyLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
    labelsPanel.add(_proficiencyLabel);
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,10,0,10),0,0);
    panel.add(labelsPanel,c);
    GridBagConstraints c2=new GridBagConstraints(0,1,1,1,1.0,1.0,GridBagConstraints.CENTER,GridBagConstraints.BOTH,new Insets(5,10,5,10),0,0);
    panel.add(historyPanel,c2);

    return panel;
  }

  /**
   * Update graph data.
   */
  public void updateData()
  {
    if (_history!=null)
    {
      _history.updateData();
    }
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
    _masteryLabel=null;
    _proficiencyLabel=null;
    if (_history!=null)
    {
      _history.dispose();
      _history=null;
    }
    _stats=null;
  }
}
