package delta.games.lotro.gui.stats.crafting;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.HashMap;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.combobox.ComboBoxController;
import delta.common.ui.swing.combobox.ItemSelectionListener;
import delta.common.ui.swing.windows.DefaultFormDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.crafting.CraftingStatus;
import delta.games.lotro.character.crafting.GuildStatus;
import delta.games.lotro.character.crafting.ProfessionStatus;
import delta.games.lotro.character.events.CharacterEvent;
import delta.games.lotro.character.events.CharacterEventType;
import delta.games.lotro.character.events.CharacterEventsManager;
import delta.games.lotro.crafting.Profession;
import delta.games.lotro.crafting.Vocation;
import delta.games.lotro.crafting.Vocations;
import delta.games.lotro.gui.stats.reputation.form.FactionHistoryPanelController;

/**
 * Controller for a "crafting stats" window.
 * @author DAM
 */
public class CraftingWindowController extends DefaultFormDialogController<CraftingStatus>
{
  private CharacterFile _toon;
  private CraftingStatus _stats;
  private ComboBoxController<Vocation> _vocation;
  private ComboBoxController<Profession> _guild;
  private JPanel _professionsPanel;
  private HashMap<Profession,ProfessionPanelController> _panels;
  private FactionHistoryPanelController _guildHistory;
  private JTabbedPane _tabbedPane;

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
    _panels=new HashMap<Profession,ProfessionPanelController>();
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

      ItemSelectionListener<Vocation> vocationListener=new ItemSelectionListener<Vocation>()
      {
        public void itemSelected(Vocation selectedVocation)
        {
          updateProfessionPanel(selectedVocation);
          updateGuildCombo(selectedVocation);
        }
      };
      _vocation.addListener(vocationListener);
    }
    // Guild panel
    JPanel guildPanel=GuiFactory.buildPanel(new FlowLayout());
    {
      _guild=new ComboBoxController<Profession>();
      JLabel guildLabel=GuiFactory.buildLabel("Guild:");
      guildPanel.add(guildLabel);
      guildPanel.add(_guild.getComboBox());
    }

    // Professions panel
    _professionsPanel=GuiFactory.buildPanel(new BorderLayout());

    // Assembly
    JPanel topPanel=GuiFactory.buildPanel(new FlowLayout());
    topPanel.add(vocationPanel);
    topPanel.add(guildPanel);
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,10,0,10),0,0);
    panel.add(topPanel,c);
    GridBagConstraints c2=new GridBagConstraints(0,1,1,1,1.0,1.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(5,10,5,10),0,0);
    panel.add(_professionsPanel,c2);

    // Init vocation and guild
    Vocation vocation=_stats.getVocation();
    Profession currentGuild=_stats.getGuildStatus().getProfession();
    _vocation.selectItem(vocation);

    ItemSelectionListener<Profession> guildListener=new ItemSelectionListener<Profession>()
    {
      public void itemSelected(Profession selectedGuild)
      {
        updateGuildUi(selectedGuild);
      }
    };
    // Init guild
    _guild.addListener(guildListener);
    _guild.selectItem(currentGuild);

    // Select first tab, if any
    if (_tabbedPane!=null)
    {
      _tabbedPane.setSelectedIndex(0);
    }

    return panel;
  }

  private void updateProfessionPanel(Vocation selectedVocation)
  {
    JComponent centerComponent=null;
    boolean wasEmpty=(_professionsPanel.getComponentCount()==0);
    _professionsPanel.removeAll();
    _tabbedPane=null;
    Vocation currentVocation=_stats.getVocation();
    if (currentVocation!=selectedVocation)
    {
      long now=System.currentTimeMillis();
      _stats.resetVocation(selectedVocation,now);
      disposeProfessionPanels();
    }
    Profession[] professions=(selectedVocation!=null)?selectedVocation.getProfessions():null;
    if ((professions!=null) && (professions.length>0))
    {
      _tabbedPane=GuiFactory.buildTabbedPane();
      // Professions
      for(Profession profession : professions)
      {
        ProfessionStatus stats=_stats.getProfessionStatus(profession,true);
        ProfessionPanelController craftingPanelController=_panels.get(profession);
        if (craftingPanelController==null)
        {
          craftingPanelController=new ProfessionPanelController(stats);
          _panels.put(profession,craftingPanelController);
        }
        JPanel craftingPanel=craftingPanelController.getPanel();
        _tabbedPane.add(profession.getLabel(),craftingPanel);
      }
      centerComponent=_tabbedPane;
    }
    else
    {
      JLabel centerLabel=new JLabel("No vocation!");
      centerComponent=centerLabel;
    }
    _professionsPanel.add(centerComponent,BorderLayout.CENTER);
    _professionsPanel.revalidate();
    _professionsPanel.repaint();
    if (!wasEmpty)
    {
      getWindow().pack();
    }
  }

  private void updateGuildUi(Profession guild)
  {
    // Cleanup
    if (_guildHistory!=null)
    {
      if (_tabbedPane!=null)
      {
        JPanel guildPanel=_guildHistory.getPanel();
        _tabbedPane.remove(guildPanel);
      }
    }
    GuildStatus guildStatus=_stats.getGuildStatus();
    Profession current=guildStatus.getProfession();
    // Add tab if needed
    if (guild!=null)
    {
      if (current!=guild)
      {
        long now=System.currentTimeMillis();
        guildStatus.initGuild(now);
      }
      guildStatus.setProfession(guild);
      _guildHistory=new FactionHistoryPanelController(guildStatus.getFactionData());
      JPanel guildPanel=_guildHistory.getPanel();
      _tabbedPane.add("Guild",guildPanel);
      _tabbedPane.setSelectedComponent(guildPanel);
    }
    else
    {
      guildStatus.setProfession(null);
      guildStatus.getFactionData().reset();
    }
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
    for(ProfessionPanelController controller: _panels.values())
    {
      controller.updateDataFromUi();
    }
    _guildHistory.updateDataFromUi();
    _toon.saveCrafting();
    // Broadcast crafting update event...
    CharacterEvent event=new CharacterEvent(_toon,null);
    CharacterEventsManager.invokeEvent(CharacterEventType.CHARACTER_CRAFTING_UPDATED,event);
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
    _guild.removeAllItems();
    _guild.addEmptyItem("");
    if (vocation!=null)
    {
      for(Profession profession : vocation.getProfessions())
      {
        if (profession.hasGuild())
        {
          _guild.addItem(profession,profession.getLabel());
        }
      }
    }
  }

  private void disposeProfessionPanels()
  {
    for(ProfessionPanelController controller : _panels.values())
    {
      controller.dispose();
    }
    _panels.clear();
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    super.dispose();
    if (_panels!=null)
    {
      disposeProfessionPanels();
      _panels=null;
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
    if (_guildHistory!=null)
    {
      _guildHistory.dispose();
      _guildHistory=null;
    }
    _tabbedPane=null;
    _stats=null;
    _toon=null;
  }
}
