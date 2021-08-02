package delta.games.lotro.gui.character.status.crafting;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import delta.common.ui.swing.GuiFactory;
import delta.games.lotro.character.status.crafting.CraftingStatus;
import delta.games.lotro.character.status.crafting.GuildStatus;
import delta.games.lotro.character.status.crafting.ProfessionStatus;
import delta.games.lotro.gui.character.status.reputation.form.FactionStatusPanelController;
import delta.games.lotro.lore.crafting.CraftingData;
import delta.games.lotro.lore.crafting.CraftingSystem;
import delta.games.lotro.lore.crafting.Profession;
import delta.games.lotro.lore.crafting.Professions;
import delta.games.lotro.lore.crafting.Vocation;

/**
 * Controller for the vocation edition panel. This panel contains:
 * <ul>
 * <li>3 profession panels,
 * <li>0-2 guild panels.
 * </ul>
 * @author DAM
 */
public class VocationEditionPanelController
{
  // Controllers
  private HashMap<Profession,ProfessionStatusPanelController> _panels;
  private List<FactionStatusPanelController> _guildStatus;
  // UI
  private JPanel _vocationPanel;
  private JTabbedPane _tabbedPane;
  // Data
  private CraftingStatus _status;

  /**
   * Constructor.
   * @param status Crafting status to edit.
   */
  public VocationEditionPanelController(CraftingStatus status)
  {
    _panels=new HashMap<Profession,ProfessionStatusPanelController>();
    _status=status;
    _vocationPanel=GuiFactory.buildPanel(new BorderLayout());
    _guildStatus=new ArrayList<FactionStatusPanelController>();
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  public JPanel getPanel()
  {
    return _vocationPanel;
  }

  /**
   * Update UI display from underlying data.
   */
  public void updateUiFromData()
  {
    updateProfessionsUi();
    updateGuildUi(null);

    // Select first tab, if any
    if (_tabbedPane!=null)
    {
      _tabbedPane.setSelectedIndex(0);
    }
  }

  /**
   * Update the UI for professions.
   */
  private void updateProfessionsUi()
  {
    Vocation vocation=_status.getVocation();
    _vocationPanel.removeAll();
    JComponent centerComponent=null;
    List<Profession> currentProfessions=(vocation!=null)?vocation.getProfessions():null;
    if ((currentProfessions!=null) && (currentProfessions.size()>0))
    {
      _tabbedPane=GuiFactory.buildTabbedPane();
      // Professions
      for(Profession profession : currentProfessions)
      {
        ProfessionStatus stats=_status.getProfessionStatus(profession,true);
        ProfessionStatusPanelController craftingPanelController=_panels.get(profession);
        if (craftingPanelController==null)
        {
          craftingPanelController=new ProfessionStatusPanelController(stats);
          _panels.put(profession,craftingPanelController);
        }
        JPanel craftingPanel=craftingPanelController.getPanel();
        _tabbedPane.add(profession.getName(),craftingPanel);
      }
      // Clean other professions
      CraftingData crafting=CraftingSystem.getInstance().getData();
      Professions professions=crafting.getProfessionsRegistry();
      for(Profession profession : professions.getAll())
      {
        if (!currentProfessions.contains(profession))
        {
          _panels.remove(profession);
        }
      }
      centerComponent=_tabbedPane;
    }
    else
    {
      JLabel centerLabel=new JLabel("No vocation!");
      centerComponent=centerLabel;
      _tabbedPane=null;
    }
    _vocationPanel.add(centerComponent,BorderLayout.CENTER);
    _vocationPanel.revalidate();
    _vocationPanel.repaint();
  }

  /**
   * Update the UI for guild edition.
   * @param toShow Profession to show.
   */
  public void updateGuildUi(Profession toShow)
  {
    // Cleanup
    if (_tabbedPane!=null)
    {
      for(FactionStatusPanelController guildStatus : _guildStatus)
      {
        JPanel guildPanel=guildStatus.getPanel();
        _tabbedPane.remove(guildPanel);
        guildStatus.dispose();
      }
      _guildStatus.clear();
    }
    Vocation vocation=_status.getVocation();
    if (vocation==null)
    {
      return;
    }
    List<Profession> guildedProfessions=vocation.getAvailableGuilds();
    JPanel toSelect=null;
    for(Profession guildedProfession : guildedProfessions)
    {
      GuildStatus guildStatus=_status.getGuildStatus(guildedProfession,true);
      FactionStatusPanelController panelController=new FactionStatusPanelController(guildStatus.getFactionStatus());
      _guildStatus.add(panelController);
      JPanel guildPanel=panelController.getPanel();
      _tabbedPane.add("Guild: "+guildedProfession.getName(),guildPanel);
      if (guildedProfession==toShow)
      {
        toSelect=guildPanel;
      }
    }
    if (toSelect!=null)
    {
      _tabbedPane.setSelectedComponent(toSelect);
    }
  }

  /**
   * Update data from UI contents.
   */
  public void updateDataFromUi()
  {
    for(ProfessionStatusPanelController controller: _panels.values())
    {
      controller.updateDataFromUi();
    }
    for(FactionStatusPanelController controller : _guildStatus)
    {
      controller.updateData();
    }
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    if (_panels!=null)
    {
      for(ProfessionStatusPanelController controller : _panels.values())
      {
        controller.dispose();
      }
      _panels.clear();
      _panels=null;
    }
    if (_guildStatus!=null)
    {
      for(FactionStatusPanelController controller : _guildStatus)
      {
        controller.dispose();
      }
      _guildStatus=null;
    }
    if (_vocationPanel!=null)
    {
      _vocationPanel.removeAll();
      _vocationPanel=null;
    }
    _tabbedPane=null;
    _status=null;
  }
}
