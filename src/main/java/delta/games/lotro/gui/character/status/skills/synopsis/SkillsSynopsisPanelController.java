package delta.games.lotro.gui.character.status.skills.synopsis;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.tables.panel.FilterUpdateListener;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.CharactersManager;
import delta.games.lotro.character.events.CharacterEvent;
import delta.games.lotro.character.events.CharacterEventType;
import delta.games.lotro.character.skills.SkillDescription;
import delta.games.lotro.gui.character.chooser.CharactersChooserController;
import delta.games.lotro.gui.lore.skills.SkillFilterController;
import delta.games.lotro.gui.lore.skills.SkillUiUtils;
import delta.games.lotro.gui.lore.skills.form.SkillFilter;
import delta.games.lotro.utils.events.EventsManager;
import delta.games.lotro.utils.events.GenericEventsListener;

/**
 * Controller for a skills synopsis panel.
 * @author DAM
 */
public class SkillsSynopsisPanelController implements GenericEventsListener<CharacterEvent>,FilterUpdateListener
{
  // Controllers
  private SkillsSynopsisWindowController _parent;
  private SkillFilterController _filterController;
  private SkillsSynopsisTableController _tableController;
  // Data
  private SkillFilter _filter;
  // GUI
  private JPanel _panel;

  /**
   * Constructor.
   * @param parentController Parent controller.
   * @param skills Skills to use.
   */
  public SkillsSynopsisPanelController(SkillsSynopsisWindowController parentController, List<SkillDescription> skills)
  {
    _parent=parentController;
    _filter=new SkillFilter();
    _filterController=new SkillFilterController(_filter,this);
    _tableController=new SkillsSynopsisTableController(skills,_filter);
    EventsManager.addListener(CharacterEvent.class,this);
  }

  /**
   * Get the table controller.
   * @return the table controller.
   */
  public SkillsSynopsisTableController getTableController()
  {
    return _tableController;
  }

  /**
   * Get the managed panel.
   * @return a panel.
   */
  public JPanel getPanel()
  {
    if (_panel==null)
    {
      _panel=buildPanel();
    }
    return _panel;
  }

  private void doChooseToons()
  {
    CharactersManager manager=CharactersManager.getInstance();
    List<CharacterFile> toons=manager.getAllToons();
    List<CharacterFile> selectedToons=_tableController.getToons();
    List<CharacterFile> newSelectedToons=CharactersChooserController.selectToons(_parent,toons,selectedToons);
    if (newSelectedToons!=null)
    {
      _tableController.setToons(newSelectedToons);
    }
  }

  private JPanel buildPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new BorderLayout());
    JPanel statsPanels=buildSynopsisPanel();
    panel.add(statsPanels,BorderLayout.CENTER);
    JPanel commandsPanel=buildTopPanel();
    panel.add(commandsPanel,BorderLayout.NORTH);
    return panel;
  }

  private JPanel buildTopPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(5,5,5,5),0,0);

    // Filter
    JPanel filterPanel=_filterController.getPanel();
    TitledBorder filterBorder=GuiFactory.buildTitledBorder("Filter"); // I18n
    filterPanel.setBorder(filterBorder);
    panel.add(filterPanel,c);

    // Choose toons button
    JButton chooser=GuiFactory.buildButton("Choose characters..."); // I18n
    ActionListener al=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        doChooseToons();
      }
    };
    chooser.addActionListener(al);
    c=new GridBagConstraints(1,0,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    panel.add(chooser,c);
    return panel;
  }

  private JPanel buildSynopsisPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new BorderLayout());
    TitledBorder border=GuiFactory.buildTitledBorder("Skills synopsis"); // I18n
    panel.setBorder(border);

    // Table
    JTable table=_tableController.getTable();
    JScrollPane scroll=GuiFactory.buildScrollPane(table);
    panel.add(scroll,BorderLayout.CENTER);

    // Action listener to show the emote form
    ActionListener al=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent event)
      {
        String action=event.getActionCommand();
        if (GenericTableController.DOUBLE_CLICK.equals(action))
        {
          SkillDescription skill=(SkillDescription)event.getSource();
          SkillUiUtils.showSkillWindow(SkillsSynopsisPanelController.this._parent,skill.getIdentifier());
        }
      }
    };
    _tableController.getGenericTable().addActionListener(al);
    return panel;
  }

  /**
   * Update filter.
   */
  public void filterUpdated()
  {
    _tableController.updateFilter();
  }

  /**
   * Handle character events.
   * @param event Source event.
   */
  @Override
  public void eventOccurred(CharacterEvent event)
  {
    CharacterEventType type=event.getType();
    if (type==CharacterEventType.CHARACTER_SKILLS_UPDATED)
    {
      CharacterFile toon=event.getToonFile();
      List<CharacterFile> currentToons=_tableController.getToons();
      if (currentToons.contains(toon))
      {
        _tableController.updateToon(toon);
      }
    }
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    EventsManager.removeListener(CharacterEvent.class,this);
    // GUI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    // Controllers
    if (_filterController!=null)
    {
      _filterController.dispose();
      _filterController=null;
    }
    if (_tableController!=null)
    {
      _tableController.dispose();
      _tableController=null;
    }
    _parent=null;
    // Data
    _filter=null;
  }
}
