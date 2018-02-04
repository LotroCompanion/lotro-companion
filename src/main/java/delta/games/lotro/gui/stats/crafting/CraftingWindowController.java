package delta.games.lotro.gui.stats.crafting;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.combobox.ComboBoxController;
import delta.common.ui.swing.combobox.ItemSelectionListener;
import delta.common.ui.swing.windows.DefaultFormDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.crafting.CraftingStatus;
import delta.games.lotro.character.events.CharacterEvent;
import delta.games.lotro.character.events.CharacterEventType;
import delta.games.lotro.lore.crafting.Profession;
import delta.games.lotro.lore.crafting.Vocation;
import delta.games.lotro.lore.crafting.Vocations;
import delta.games.lotro.utils.events.EventsManager;

/**
 * Controller for a "crafting stats" window.
 * @author DAM
 */
public class CraftingWindowController extends DefaultFormDialogController<CraftingStatus>
{
  // Data
  private CharacterFile _toon;
  private CraftingStatus _stats;
  // Controllers
  private ComboBoxController<Vocation> _vocation;
  private ComboBoxController<Profession> _guild;
  private VocationEditionPanelController _vocationController;

  /**
   * Constructor.
   * @param parentController Parent controller.
   * @param toon Managed toon.
   */
  public CraftingWindowController(WindowController parentController, CharacterFile toon)
  {
    super(parentController,toon.getCraftingStatus());
    _toon=toon;
    _stats=toon.getCraftingStatus();
    _vocationController=new VocationEditionPanelController(_stats);
  }

  @Override
  protected JPanel buildFormPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    // Vocation panel
    JPanel vocationPanel=GuiFactory.buildPanel(new FlowLayout());
    {
      _vocation=buildVocationCombo();
      JLabel vocationLabel=GuiFactory.buildLabel("Vocation:");
      vocationPanel.add(vocationLabel);
      vocationPanel.add(_vocation.getComboBox());
    }
    // Guild panel
    JPanel guildPanel=GuiFactory.buildPanel(new FlowLayout());
    {
      _guild=new ComboBoxController<Profession>();
      JLabel guildLabel=GuiFactory.buildLabel("Guild:");
      guildPanel.add(guildLabel);
      guildPanel.add(_guild.getComboBox());
    }

    // Vocation panel
    JPanel vocationEditionPanel=_vocationController.getPanel();

    // Assembly
    JPanel topPanel=GuiFactory.buildPanel(new FlowLayout());
    topPanel.add(vocationPanel);
    topPanel.add(guildPanel);
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,10,0,10),0,0);
    panel.add(topPanel,c);
    GridBagConstraints c2=new GridBagConstraints(0,1,1,1,1.0,1.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(5,10,5,10),0,0);
    panel.add(vocationEditionPanel,c2);

    // Update vocation edition panel
    _vocationController.updateUiFromData();
    Vocation vocation=_stats.getVocation();
    // Init combos
    Profession currentGuild=_stats.getGuildStatus().getProfession();
    _vocation.selectItem(vocation);
    _guild.addEmptyItem("");
    updateGuildCombo(vocation);
    _guild.selectItem(currentGuild);

    // Init vocation combo
    ItemSelectionListener<Vocation> vocationListener=new ItemSelectionListener<Vocation>()
    {
      public void itemSelected(Vocation selectedVocation)
      {
        handleVocationUpdate(selectedVocation);
      }
    };
    _vocation.addListener(vocationListener);
    // Init guild combo
    ItemSelectionListener<Profession> guildListener=new ItemSelectionListener<Profession>()
    {
      public void itemSelected(Profession selectedGuild)
      {
        handleGuildUpdate(selectedGuild);
      }
    };
    _guild.addListener(guildListener);

    return panel;
  }

  private void handleVocationUpdate(Vocation selectedVocation)
  {
    // TODO warn of possible profession history loss
    boolean changed=updateVocation(selectedVocation);
    if (changed)
    {
      updateGuildCombo(selectedVocation);
      updateVocationPanel(selectedVocation);
    }
  }

  private boolean updateVocation(Vocation selectedVocation)
  {
    boolean changed=false;
    Vocation currentVocation=_stats.getVocation();
    if (currentVocation!=selectedVocation)
    {
      long now=System.currentTimeMillis();
      _stats.changeVocation(selectedVocation,now);
      changed=true;
    }
    return changed;
  }

  private void handleGuildUpdate(Profession selectedGuild)
  {
    // TODO warn of guild history loss
    boolean changed=updateGuild(selectedGuild);
    if (changed)
    {
      _vocationController.updateGuildUi();
    }
  }

  private boolean updateGuild(Profession selectedGuild)
  {
    boolean changed=false;
    Profession currentProfession=_stats.getGuildStatus().getProfession();
    if (currentProfession!=selectedGuild)
    {
      long now=System.currentTimeMillis();
      _stats.getGuildStatus().changeProfession(selectedGuild,now);
      changed=true;
    }
    return changed;
  }

  private void updateVocationPanel(Vocation selectedVocation)
  {
    _vocationController.updateDataFromUi();
    _vocationController.updateUiFromData();
    getWindow().pack();
  }

  @Override
  protected JDialog build()
  {
    JDialog dialog=super.build();
    // Title
    String name=_toon.getName();
    String serverName=_toon.getServerName();
    String title="Crafting history editor for "+name+" @ "+serverName;
    dialog.setTitle(title);
    // Minimum size
    dialog.setMinimumSize(new Dimension(500,380));
    return dialog;
  }

  @Override
  protected void okImpl()
  {
    _vocationController.updateDataFromUi();
    _toon.saveCrafting();
    // Broadcast crafting update event...
    CharacterEvent event=new CharacterEvent(CharacterEventType.CHARACTER_CRAFTING_UPDATED,_toon,null);
    EventsManager.invokeEvent(event);
  }

  @Override
  protected void cancelImpl()
  {
    _toon.revertCrafting();
  }

  private ComboBoxController<Vocation> buildVocationCombo()
  {
    ComboBoxController<Vocation> ret=new ComboBoxController<Vocation>();
    ret.addEmptyItem("");
    List<Vocation> vocations=Vocations.getInstance().getAll();
    for(Vocation vocation : vocations)
    {
      ret.addItem(vocation,vocation.getName());
    }
    return ret;
  }

  private void updateGuildCombo(Vocation vocation)
  {
    List<Profession> oldProfessions=_guild.getItems();
    if (vocation!=null)
    {
      // Add new professions
      for(Profession profession : vocation.getProfessions())
      {
        if (profession.hasGuild())
        {
          if (oldProfessions.contains(profession))
          {
            oldProfessions.remove(profession);
          }
          else
          {
            _guild.addItem(profession,profession.getLabel());
          }
        }
      }
      // Remove obsolete professions
      for(Profession oldProfession : oldProfessions)
      {
        if (oldProfession!=null)
        {
          _guild.removeItem(oldProfession);
        }
      }
    }
    else
    {
      _guild.removeAllItems();
      _guild.addEmptyItem("");
    }
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    super.dispose();
    if (_vocationController!=null)
    {
      _vocationController.dispose();
      _vocationController=null;
    }
    if (_vocation!=null)
    {
      _vocation.dispose();
      _vocation=null;
    }
    if (_guild!=null)
    {
      _guild.dispose();
      _guild=null;
    }
    _stats=null;
    _toon=null;
  }
}
