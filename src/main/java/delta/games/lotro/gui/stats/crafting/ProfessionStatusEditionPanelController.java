package delta.games.lotro.gui.stats.crafting;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBox;
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

    ActionListener l=new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        handleUpdate(e.getSource());
      }
    };

    // Data rows
    CraftingLevel[] levels=CraftingLevel.ALL_TIERS;
    for(CraftingLevel level : levels)
    {
      CraftingLevelStatus levelStatus=_status.getLevelStatus(level);
      c.gridx=0;
      JLabel tierLabel=GuiFactory.buildLabel(level.toString());
      panel.add(tierLabel,c);
      c.gridx++;
      // Proficiency
      CraftingLevelTierStatus proficiencyStatus=levelStatus.getProficiency();
      CraftingLevelTierEditionGadgets proficiencyGadgets=new CraftingLevelTierEditionGadgets(proficiencyStatus);
      JCheckBox checkbox=proficiencyGadgets.getCompleted().getCheckbox();
      panel.add(checkbox,c);
      checkbox.addActionListener(l);
      c.gridx++;
      panel.add(proficiencyGadgets.getXp().getTextField(),c);
      c.gridx++;
      panel.add(proficiencyGadgets.getCompletionDate().getTextField(),c);
      c.gridx++;
      _proficiencyGadgets.add(proficiencyGadgets);

      // Mastery
      CraftingLevelTierStatus masteryStatus=levelStatus.getMastery();
      CraftingLevelTierEditionGadgets masteryGadgets=new CraftingLevelTierEditionGadgets(masteryStatus);
      checkbox=masteryGadgets.getCompleted().getCheckbox();
      panel.add(checkbox,c);
      checkbox.addActionListener(l);
      c.gridx++;
      panel.add(masteryGadgets.getXp().getTextField(),c);
      c.gridx++;
      panel.add(masteryGadgets.getCompletionDate().getTextField(),c);
      c.gridx++;
      _masteryGadgets.add(masteryGadgets);
      c.gridy++;

      if (level==CraftingLevel.BEGINNER)
      {
        proficiencyGadgets.getCompleted().setState(false);
        masteryGadgets.getCompleted().setState(false);
      }
    }
    return panel;
  }

  private void handleUpdate(Object source)
  {
    CraftingLevel[] levels=CraftingLevel.ALL_TIERS;
    int index=0;
    for(CraftingLevel level : levels)
    {
      CraftingLevelTierEditionGadgets proficiency=_proficiencyGadgets.get(index);
      if (source==proficiency.getCompleted().getCheckbox())
      {
        boolean completed=proficiency.getCompleted().isSelected();
        _status.setCompletionStatus(level,false,completed);
      }
      CraftingLevelTierEditionGadgets mastery=_masteryGadgets.get(index);
      if (source==mastery.getCompleted().getCheckbox())
      {
        boolean completed=mastery.getCompleted().isSelected();
        _status.setCompletionStatus(level,true,completed);
      }
      index++;
    }
    updateUi();
  }

  private void updateUi()
  {
    int nbTiers=_proficiencyGadgets.size();
    for(int i=0;i<nbTiers;i++)
    {
      _proficiencyGadgets.get(i).updateUi();
      _masteryGadgets.get(i).updateUi();
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
