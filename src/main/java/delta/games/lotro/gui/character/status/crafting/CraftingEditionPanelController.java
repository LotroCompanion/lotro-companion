package delta.games.lotro.gui.character.status.crafting;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.area.AreaController;
import delta.common.ui.swing.panels.AbstractPanelController;
import delta.games.lotro.character.status.crafting.CraftingStatus;
import delta.games.lotro.character.status.crafting.GuildStatus;
import delta.games.lotro.character.status.crafting.ProfessionStatus;
import delta.games.lotro.gui.character.status.reputation.form.FactionStatusPanelController;
import delta.games.lotro.lore.crafting.CraftingData;
import delta.games.lotro.lore.crafting.CraftingSystem;
import delta.games.lotro.lore.crafting.Profession;
import delta.games.lotro.lore.crafting.Professions;

/**
 * Controller for the crafting history edition panel. This panel contains:
 * <ul>
 * <li>3 profession panels,
 * <li>0-2 guild panels.
 * </ul>
 * @author DAM
 */
public class CraftingEditionPanelController extends AbstractPanelController
{
  // Controllers
  private HashMap<Profession,ProfessionStatusPanelController> _panels;
  private List<FactionStatusPanelController> _guildStatus;
  // UI
  private JTabbedPane _tabbedPane;
  // Data
  private CraftingStatus _status;

  /**
   * Constructor.
   * @param parent Parent controller.
   * @param status Crafting status to edit.
   */
  public CraftingEditionPanelController(AreaController parent, CraftingStatus status)
  {
    super(parent);
    _panels=new HashMap<Profession,ProfessionStatusPanelController>();
    _status=status;
    JPanel mainPanel=GuiFactory.buildPanel(new BorderLayout());
    setPanel(mainPanel);
    _guildStatus=new ArrayList<FactionStatusPanelController>();
  }

  /**
   * Update UI display from underlying data.
   */
  public void updateUiFromData()
  {
    updateProfessionsUi();
    updateGuildUi(null);

    // Select first tab, if any
    if ((_tabbedPane!=null) && (_tabbedPane.getTabCount()>0))
    {
      _tabbedPane.setSelectedIndex(0);
    }
  }

  /**
   * Update the UI for professions.
   */
  private void updateProfessionsUi()
  {
    JPanel panel=getPanel();
    panel.removeAll();
    List<Profession> currentProfessions=_status.getKnownProfessions();
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
    panel.add(_tabbedPane,BorderLayout.CENTER);
    panel.revalidate();
    panel.repaint();
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
    List<Profession> guildedProfessions=new ArrayList<Profession>();
    for(Profession profession : _status.getKnownProfessions())
    {
      if (profession.hasGuild())
      {
        guildedProfessions.add(profession);
      }
    }
    JPanel toSelect=null;
    for(Profession guildedProfession : guildedProfessions)
    {
      GuildStatus guildStatus=_status.getGuildStatus(guildedProfession,true);
      FactionStatusPanelController panelController=new FactionStatusPanelController(this,guildStatus.getFactionStatus());
      _guildStatus.add(panelController);
      JPanel guildPanel=panelController.getPanel();
      _tabbedPane.add("Guild: "+guildedProfession.getName(),guildPanel); // I18n
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

  @Override
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
    _tabbedPane=null;
    _status=null;
    super.dispose();
  }
}
