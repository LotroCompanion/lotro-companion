package delta.games.lotro.gui.character.status.warbands;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;

import delta.common.ui.swing.GuiFactory;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.CharactersManager;
import delta.games.lotro.gui.character.chooser.CharactersChooserController;
import delta.games.lotro.lore.warbands.WarbandFilter;

/**
 * Controller for a warbands statistics panel.
 * @author DAM
 */
public class WarbandsPanelController
{
  // Controllers
  private WarbandsWindowController _parent;
  private WarbandsFilterController _filterController;
  private WarbandsTableController _tableController;
  // Data
  private WarbandFilter _filter;
  // GUI
  private JPanel _panel;

  /**
   * Constructor.
   * @param parentController Parent controller.
   */
  public WarbandsPanelController(WarbandsWindowController parentController)
  {
    _parent=parentController;
    _filter=new WarbandFilter();
    _filterController=new WarbandsFilterController(_filter,this);
    _tableController=new WarbandsTableController(_filter);
  }

  /**
   * Get the table controller.
   * @return the table controller.
   */
  public WarbandsTableController getTableController()
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
    List<CharacterFile> enabledToons=new ArrayList<CharacterFile>();
    for(CharacterFile toon : toons)
    {
      if (toon.hasLog())
      {
        enabledToons.add(toon);
      }
    }
    List<CharacterFile> newSelectedToons=CharactersChooserController.selectToons(_parent,enabledToons,selectedToons);
    if (newSelectedToons!=null)
    {
      _tableController.refresh(newSelectedToons);
    }
  }

  private JPanel buildPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new BorderLayout());
    JPanel statsPanels=buildStatsPanel();
    panel.add(statsPanels,BorderLayout.CENTER);
    JPanel commandsPanel=buildCommandsPanel();
    panel.add(commandsPanel,BorderLayout.NORTH);
    return panel;
  }

  private JPanel buildCommandsPanel()
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

  private JPanel buildStatsPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new BorderLayout());
    TitledBorder logFrameBorder=GuiFactory.buildTitledBorder("Warbands"); // I18n
    panel.setBorder(logFrameBorder);

    // Table
    JTable table=_tableController.getTable();
    JScrollPane scroll=GuiFactory.buildScrollPane(table);
    panel.add(scroll,BorderLayout.CENTER);
    return panel;
  }

  /**
   * Update filter.
   */
  public void updateFilter()
  {
    _tableController.updateFilter();
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
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
    // Data
    _filter=null;
  }
}
