package delta.games.lotro.gui.lore.items.legendary.titles;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.labels.MultilineLabel2;
import delta.games.lotro.character.stats.BasicStatsSet;
import delta.games.lotro.common.enums.Genus;
import delta.games.lotro.common.stats.StatUtils;
import delta.games.lotro.lore.items.DamageType;
import delta.games.lotro.lore.items.DamageTypes;
import delta.games.lotro.lore.items.legendary.titles.LegendaryTitle;

/**
 * Panel to display a legendary title.
 * @author DAM
 */
public class LegendaryTitleDisplayPanelController
{
  // UI
  private JPanel _panel;
  private JLabel _name;
  private JLabel _damageType;
  private JLabel _slayer;
  private MultilineLabel2 _stats;

  /**
   * Constructor.
   */
  public LegendaryTitleDisplayPanelController()
  {
    _name=GuiFactory.buildLabel("");
    _damageType=GuiFactory.buildLabel("");
    _slayer=GuiFactory.buildLabel("");
    _stats=new MultilineLabel2();
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
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    // Name
    panel.add(_name,c);
    c.gridy++;
    // Damage type
    panel.add(_damageType,c);
    c.gridy++;
    // Slayer
    panel.add(_slayer,c);
    c.gridy++;
    // Stats
    panel.add(_stats,c);
    c.gridy++;
    return panel;
  }

  /**
   * Set the data to display.
   * @param title Data to display.
   */
  public void setData(LegendaryTitle title)
  {
    if (title!=null)
    {
      // Name
      String name=title.getName();
      _name.setText(name);
      // Damage type
      String damageTypeStr="";
      DamageType damageType=title.getDamageType();
      if ((damageType!=null) && (damageType!=DamageTypes.COMMON))
      {
        damageTypeStr="Damage type: "+damageType.getName();
      }
      _damageType.setText(damageTypeStr);
      // Slayer
      Genus slayer=title.getSlayerGenusType();
      String slayerStr=(slayer!=null)?"Slayer: "+slayer:"";
      _slayer.setText(slayerStr);
      // Stats
      BasicStatsSet stats=title.getStats();
      String[] lines=StatUtils.getStatsDisplayLines(stats);
      _stats.setText(lines);
    }
    else
    {
      _name.setText("");
      _damageType.setText("");
      _slayer.setText("");
      _stats.setText(new String[]{""});
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
    _name=null;
    _damageType=null;
    _slayer=null;
    _stats=null;
  }
}
