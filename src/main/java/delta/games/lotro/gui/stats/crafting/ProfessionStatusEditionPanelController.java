package delta.games.lotro.gui.stats.crafting;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.games.lotro.character.crafting.CraftingLevelStatus;
import delta.games.lotro.character.crafting.CraftingLevelTierStatus;
import delta.games.lotro.character.crafting.ProfessionStatus;
import delta.games.lotro.crafting.CraftingLevel;

/**
 * Controller for a profession status edition panel.
 * @author DAM
 */
public class ProfessionStatusEditionPanelController
{
  // Data
  private ProfessionStatus _status;
  // Controllers
  private List<CraftingLevelTierEditionGadgets> _proficiencyGadgets;
  private List<CraftingLevelTierEditionGadgets> _masteryGadgets;
  // UI
  private JPanel _panel;

  /**
   * Constructor.
   * @param status Status to edit.
   */
  public ProfessionStatusEditionPanelController(ProfessionStatus status)
  {
    _status=status;
    _proficiencyGadgets=new ArrayList<CraftingLevelTierEditionGadgets>();
    _masteryGadgets=new ArrayList<CraftingLevelTierEditionGadgets>();
    _panel=buildPanel();
    setStatus(status);
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
    JPanel panel=GuiFactory.buildBackgroundPanel(new GridBagLayout());

    // Header row 1
    GridBagConstraints c=new GridBagConstraints(0,0,1,2,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    JLabel tier=GuiFactory.buildLabel("Tier");
    panel.add(tier,c);
    c.gridx=1;c.gridwidth=3;c.gridheight=1;
    JLabel proficiency=GuiFactory.buildLabel("Proficiency");
    panel.add(proficiency,c);
    c.gridx=4;c.gridwidth=3;c.gridheight=1;
    JLabel mastery=GuiFactory.buildLabel("Mastery");
    panel.add(mastery,c);

    // Header row 2
    c.gridx=1;c.gridy=1;c.gridwidth=1;
    for(int i=0;i<2;i++)
    {
      panel.add(GuiFactory.buildLabel("Completed"),c);
      c.gridx++;
      panel.add(GuiFactory.buildLabel("XP"),c);
      c.gridx++;
      panel.add(GuiFactory.buildLabel("Completion date"),c);
      c.gridx++;
    }
    c.gridy++;
    // Data rows
    CraftingLevel[] levels=CraftingLevel.ALL_TIERS;
    for(CraftingLevel level : levels)
    {
      c.gridx=0;
      JLabel tierLabel=GuiFactory.buildLabel(level.toString());
      panel.add(tierLabel,c);
      c.gridx++;
      CraftingLevelTierEditionGadgets proficiencyGadgets=new CraftingLevelTierEditionGadgets();
      panel.add(proficiencyGadgets.getCompleted().getCheckbox(),c);
      c.gridx++;
      panel.add(proficiencyGadgets.getXp().getTextField(),c);
      c.gridx++;
      panel.add(proficiencyGadgets.getCompletionDate().getTextField(),c);
      c.gridx++;
      _proficiencyGadgets.add(proficiencyGadgets);
      CraftingLevelTierEditionGadgets masteryGadgets=new CraftingLevelTierEditionGadgets();
      panel.add(masteryGadgets.getCompleted().getCheckbox(),c);
      c.gridx++;
      panel.add(masteryGadgets.getXp().getTextField(),c);
      c.gridx++;
      panel.add(masteryGadgets.getCompletionDate().getTextField(),c);
      c.gridx++;
      _masteryGadgets.add(masteryGadgets);
      c.gridy++;
    }
    return panel;
  }

  /**
   * Set the displayed status.
   * @param status Status to display.
   */
  public void setStatus(ProfessionStatus status)
  {
    _status=status;
    CraftingLevel[] levels=CraftingLevel.ALL_TIERS;
    int index=0;
    for(CraftingLevel level : levels)
    {
      CraftingLevelStatus levelStatus=status.getLevelStatus(level);
      // Proficiency
      CraftingLevelTierStatus proficiencyStatus=levelStatus.getProficiency();
      CraftingLevelTierEditionGadgets proficiencyGadgets=_proficiencyGadgets.get(index);
      proficiencyGadgets.setStatus(proficiencyStatus);
      // Mastery
      CraftingLevelTierStatus masteryStatus=levelStatus.getMastery();
      CraftingLevelTierEditionGadgets masteryGadgets=_masteryGadgets.get(index);
      masteryGadgets.setStatus(masteryStatus);
      index++;
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
    if (_masteryGadgets!=null)
    {
      for(CraftingLevelTierEditionGadgets gadget : _masteryGadgets)
      {
        gadget.dispose();
      }
      _masteryGadgets.clear();
      _masteryGadgets=null;
    }
    if (_proficiencyGadgets!=null)
    {
      for(CraftingLevelTierEditionGadgets gadget : _proficiencyGadgets)
      {
        gadget.dispose();
      }
      _proficiencyGadgets.clear();
      _proficiencyGadgets=null;
    }
    _status=null;
  }
}
