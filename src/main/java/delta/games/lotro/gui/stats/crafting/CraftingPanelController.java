package delta.games.lotro.gui.stats.crafting;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import delta.games.lotro.crafting.CraftingLevel;
import delta.games.lotro.stats.crafting.ProfessionStat;

/**
 * Controller for a crafting panel (one profession).
 * @author DAM
 */
public class CraftingPanelController
{
  // GUI
  private JPanel _panel;
  private JLabel _masteryLabel;
  private JLabel _proficiencyLabel;
  private CraftingHistoryChartController _history;
  // Data
  private ProfessionStat _stats;
  
  /**
   * Constructor.
   * @param stats Profession stats to display.
   */
  public CraftingPanelController(ProfessionStat stats)
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
    JPanel panel=new JPanel(new GridBagLayout());
    panel.setBackground(Color.BLACK);
    
    // Mastery label
    int masteryTier=_stats.getMasteryTier();
    CraftingLevel mastery=CraftingLevel.getByTier(masteryTier);
    String masteryStr=mastery.getMasteryLabel()+" ("+masteryTier+")";
    _masteryLabel=new JLabel("Mastery: "+masteryStr);
    _masteryLabel.setForeground(Color.WHITE);
    // Proficiency label
    int proficiencyTier=_stats.getProficiencyTier();
    CraftingLevel proficiency=CraftingLevel.getByTier(proficiencyTier);
    String proficiencyStr=proficiency.getProficiencyLabel()+" ("+proficiencyTier+")";
    _proficiencyLabel=new JLabel("Proficiency: "+proficiencyStr);
    _proficiencyLabel.setForeground(Color.WHITE);
    // History chart
    _history=new CraftingHistoryChartController(_stats,false);
    JPanel historyPanel=_history.getPanel();

    // Assembly
    JPanel labelsPanel=new JPanel();
    labelsPanel.setBackground(Color.BLACK);
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
