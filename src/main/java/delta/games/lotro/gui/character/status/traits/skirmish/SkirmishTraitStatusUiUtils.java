package delta.games.lotro.gui.character.status.traits.skirmish;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;

import delta.common.ui.swing.GuiFactory;
import delta.games.lotro.common.enums.LotroEnum;
import delta.games.lotro.common.enums.LotroEnumsRegistry;
import delta.games.lotro.common.enums.TraitNature;
import delta.games.lotro.gui.utils.IconController;

/**
 * Utility methods for the UI of skirmish traits status.
 * @author DAM
 */
public class SkirmishTraitStatusUiUtils
{
  /**
   * Build a panel with a border and an icon.
   * @param borderIcon Border icon.
   * @param iconCtrl Inside icon.
   * @return A layered pane.
   */
  public static JLayeredPane buildBorderAndIconPanel(ImageIcon borderIcon, IconController iconCtrl)
  {
    JLayeredPane ret=new JLayeredPane();
    // Border
    JLabel borderLabel=GuiFactory.buildIconLabel(borderIcon);
    ret.add(borderLabel,Integer.valueOf(0));
    borderLabel.setBounds(0,0,borderIcon.getIconWidth(),borderIcon.getIconHeight());
    // Icon
    JButton buttonIcon=iconCtrl.getIcon();
    ret.add(buttonIcon,Integer.valueOf(1));
    Icon icon=buttonIcon.getIcon();
    buttonIcon.setBounds(4,4,icon.getIconWidth(),icon.getIconHeight());
    ret.setSize(borderIcon.getIconWidth(),borderIcon.getIconHeight());
    Dimension size=new Dimension(borderIcon.getIconWidth(),borderIcon.getIconHeight());
    ret.setPreferredSize(size);
    return ret;
  }

  /**
   * Get the trait natures in the display order.
   * @return A list of trait natures.
   */
  public static List<TraitNature> getOrderedTraitNature()
  {
    List<TraitNature> ret=new ArrayList<TraitNature>();
    LotroEnum<TraitNature> natureEnum=LotroEnumsRegistry.getInstance().get(TraitNature.class);
    ret.add(natureEnum.getEntry(8)); // Attribute
    ret.add(natureEnum.getEntry(9)); // Skill
    ret.add(natureEnum.getEntry(11)); // Training
    ret.add(natureEnum.getEntry(10)); // Personal
    return ret;
  }
}
