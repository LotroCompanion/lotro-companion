package delta.games.lotro.gui.utils;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.games.lotro.character.skills.SkillDescription;
import delta.games.lotro.character.traits.TraitDescription;
import delta.games.lotro.gui.LotroIconsManager;

/**
 * Factory for shared panels.
 * @author DAM
 */
public class SharedPanels
{
  /**
   * Build a skill link panel.
   * @param skill Skill to show.
   * @return A panel.
   */
  public static JPanel buildSkillPanel(SkillDescription skill)
  {
    if (skill==null)
    {
      return null;
    }
    // Icon
    int iconID=skill.getIconId();
    ImageIcon icon=LotroIconsManager.getSkillIcon(iconID);
    JLabel skillIconLabel=GuiFactory.buildIconLabel(icon);
    // Text
    JLabel skillName=GuiFactory.buildLabel(skill.getName());
    // Result panel;
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,5),0,0);
    ret.add(skillIconLabel,c);
    c=new GridBagConstraints(1,0,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    ret.add(skillName,c);
    return ret;
  }

  /**
   * Build a trait link panel.
   * @param trait Trait to show.
   * @return A panel.
   */
  public static JPanel buildTraitPanel(TraitDescription trait)
  {
    if (trait==null)
    {
      return null;
    }
    // Icon
    int iconID=trait.getIconId();
    ImageIcon icon=LotroIconsManager.getTraitIcon(iconID);
    JLabel traitIconLabel=GuiFactory.buildIconLabel(icon);
    // Text
    JLabel traitName=GuiFactory.buildLabel(trait.getName());
    // Result panel;
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,5),0,0);
    ret.add(traitIconLabel,c);
    c=new GridBagConstraints(1,0,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    ret.add(traitName,c);
    return ret;
  }
}
