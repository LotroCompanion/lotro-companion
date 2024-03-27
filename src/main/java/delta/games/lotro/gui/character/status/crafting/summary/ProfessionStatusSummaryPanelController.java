package delta.games.lotro.gui.character.status.crafting.summary;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.panels.AbstractPanelController;
import delta.games.lotro.character.status.crafting.summary.ProfessionStatusSummary;
import delta.games.lotro.common.enums.CraftTier;
import delta.games.lotro.gui.LotroIconsManager;
import delta.games.lotro.lore.crafting.Profession;
import delta.games.lotro.lore.reputation.FactionLevel;

/**
 * Panel to show a profession status summary.
 * @author DAM
 */
public class ProfessionStatusSummaryPanelController extends AbstractPanelController
{
  private JLabel _profession;
  private JLabel _proficiency;
  private JLabel _mastery;
  private JLabel _guild;

  /**
   * Constructor.
   */
  public ProfessionStatusSummaryPanelController()
  {
    super();
    _profession=GuiFactory.buildLabel("");
    _proficiency=GuiFactory.buildLabel("");
    _mastery=GuiFactory.buildLabel("");
    _guild=GuiFactory.buildLabel("");
    setPanel(buildPanel());
  }

  private JPanel buildPanel()
  {
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    // - profession
    GridBagConstraints c=new GridBagConstraints(0,0,2,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    ret.add(_profession,c);
    // - mastery
    Icon masteryIcon=LotroIconsManager.getCraftingTierIcon(true);
    JLabel masteryLabel=GuiFactory.buildIconLabel(masteryIcon);
    c=new GridBagConstraints(0,1,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    ret.add(masteryLabel,c);
    c.gridx++;
    c.anchor=GridBagConstraints.WEST;
    ret.add(_mastery,c);
    // - proficiency
    Icon proficiencyIcon=LotroIconsManager.getCraftingTierIcon(false);
    JLabel proficiencyLabel=GuiFactory.buildIconLabel(proficiencyIcon);
    c=new GridBagConstraints(0,2,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    ret.add(proficiencyLabel,c);
    c.gridx++;
    c.anchor=GridBagConstraints.WEST;
    ret.add(_proficiency,c);
    // Guild
    c=new GridBagConstraints(0,3,2,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    ret.add(_guild,c);
    return ret;
  }

  /**
   * Set the status to show.
   * @param summary Status to show.
   */
  public void setStatus(ProfessionStatusSummary summary)
  {
    // - profession
    Profession profession=summary.getProfession();
    _profession.setText(profession.getName());
    // - proficiency
    CraftTier proficiencyTier=summary.getProficiency();
    String proficiencyLabel=(proficiencyTier!=null)?proficiencyTier.getLabel():"-";
    _proficiency.setText(proficiencyLabel);
    // - mastery
    CraftTier masteryTier=summary.getMastery();
    String masteryLabel=(masteryTier!=null)?masteryTier.getLabel():"-";
    _mastery.setText(masteryLabel);
    // Guild
    if (profession.hasGuild())
    {
      FactionLevel level=summary.getGuildLevel();
      String guildLabel=(level!=null)?level.getName():"-";
      _guild.setText(guildLabel);
      _guild.setVisible(true);
    }
    else
    {
      _guild.setVisible(false);
    }
  }
}
