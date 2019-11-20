package delta.games.lotro.gui.stats.crafting;

import java.awt.BorderLayout;
import java.util.HashMap;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import delta.common.ui.swing.GuiFactory;
import delta.games.lotro.character.crafting.CraftingStatus;
import delta.games.lotro.character.crafting.GuildStatus;
import delta.games.lotro.character.crafting.ProfessionStatus;
import delta.games.lotro.gui.stats.reputation.form.FactionStatusPanelController;
import delta.games.lotro.lore.crafting.CraftingData;
import delta.games.lotro.lore.crafting.CraftingSystem;
import delta.games.lotro.lore.crafting.Profession;
import delta.games.lotro.lore.crafting.Professions;
import delta.games.lotro.lore.crafting.Vocation;

/**
 * Controller for the vocation edition panel. This panel contains:
 * <ul>
 * <li>3 profession panels,
 * <li>an optional guild panel.
 * </ul>
 * @author DAM
 */
public class VocationEditionPanelController
{
  // Controllers
  private HashMap<Profession,ProfessionStatusPanelController> _panels;
  private FactionStatusPanelController[] _guildStatus;
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
    _guildStatus=new FactionStatusPanelController[CraftingStatus.NB_GUILDS];
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
    updateGuildUi(-1);

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
   * @param guildIndex Index of the updated guild.
   */
  public void updateGuildUi(int guildIndex)
  {
    // Cleanup
    if (_tabbedPane!=null)
    {
      for(int i=0;i<CraftingStatus.NB_GUILDS;i++)
      {
        if (_guildStatus[i]!=null)
        {
          JPanel guildPanel=_guildStatus[i].getPanel();
          _tabbedPane.remove(guildPanel);
          _guildStatus[i].dispose();
          _guildStatus[i]=null;
        }
      }
    }
    JPanel toSelect=null;
    for(int i=0;i<CraftingStatus.NB_GUILDS;i++)
    {
      GuildStatus guildStatus=_status.getGuildStatus(i);
      Profession guild=guildStatus.getProfession();
      // Add tab if needed
      if (guild!=null)
      {
        _guildStatus[i]=new FactionStatusPanelController(guildStatus.getFactionStatus());
        JPanel guildPanel=_guildStatus[i].getPanel();
        _tabbedPane.add("Guild: "+guild.getName(),guildPanel);
        if (i==guildIndex)
        {
          toSelect=guildPanel;
        }
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
    for(int i=0;i<CraftingStatus.NB_GUILDS;i++)
    {
      if (_guildStatus[i]!=null)
      {
        _guildStatus[i].updateDataFromUi();
      }
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
      for(int i=0;i<CraftingStatus.NB_GUILDS;i++)
      {
        if (_guildStatus[i]!=null)
        {
          _guildStatus[i].dispose();
          _guildStatus[i]=null;
        }
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
