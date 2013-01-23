package delta.games.lotro.gui.stats.warbands;

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

import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.CharactersManager;
import delta.games.lotro.gui.character.CharactersSelectorWindowController;
import delta.games.lotro.gui.utils.GuiFactory;
import delta.games.lotro.lore.warbands.WarbandFilter;
import delta.games.lotro.stats.warbands.MultipleToonsWarbandsStats;

/**
 * Controller for a warbands statistics panel.
 * @author DAM
 */
public class WarbandsPanelController implements ActionListener
{
  // Controllers
  private WarbandsWindowController _parent;
  private WarbandsFilterController _filterController;
  private WarbandsTableController _tableController;
  // Data
  private WarbandFilter _filter;
  private MultipleToonsWarbandsStats _stats;
  // GUI
  private JPanel _panel;
  
  /**
   * Constructor.
   * @param parentController Parent controller.
   * @param stats Underlying warbands statistics.
   */
  public WarbandsPanelController(WarbandsWindowController parentController, MultipleToonsWarbandsStats stats)
  {
    _parent=parentController;
    _stats=stats;
    _filter=new WarbandFilter();
    _filterController=new WarbandsFilterController(_filter,this);
    _tableController=new WarbandsTableController(stats,_filter);
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
  
  public void actionPerformed(ActionEvent e)
  {
    CharactersManager manager=CharactersManager.getInstance();
    List<CharacterFile> toons=manager.getAllToons();
    List<CharacterFile> selectedToons=_stats.getToonsList();
    List<CharacterFile> newSelectedToons=CharactersSelectorWindowController.selectToons(_parent,toons,selectedToons);
    if (newSelectedToons!=null)
    {
      for(CharacterFile toon : newSelectedToons)
      {
        if (selectedToons.contains(toon))
        {
          selectedToons.remove(toon);
        }
        else
        {
          _stats.addToon(toon);
        }
      }
      for(CharacterFile removedToon : selectedToons)
      {
        _stats.removeToon(removedToon);
      }
      _tableController.refresh();
    }
  }

  private JPanel buildPanel()
  {
    JPanel panel=GuiFactory.buildBackgroundPanel(new BorderLayout());
    JPanel statsPanels=buildStatsPanel();
    panel.add(statsPanels,BorderLayout.CENTER);
    JPanel commandsPanel=buildCommandsPanel();
    panel.add(commandsPanel,BorderLayout.NORTH);
    return panel;
  }

  private JPanel buildCommandsPanel()
  {
    JPanel panel=GuiFactory.buildBackgroundPanel(new GridBagLayout());

    GridBagConstraints c=new GridBagConstraints(0,0,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(5,5,5,5),0,0);

    // Filter
    JPanel filterPanel=_filterController.getPanel();
    TitledBorder filterBorder=GuiFactory.buildTitledBorder("Filter");
    filterPanel.setBorder(filterBorder);
    panel.add(filterPanel,c);
    
    // Choose toons button
    JButton chooser=GuiFactory.buildButton("Choose toons...");
    chooser.addActionListener(this);
    c.gridx++;
    c.fill=GridBagConstraints.NONE;
    c.weightx=0.0;
    panel.add(chooser,c);
    return panel;
  }

  private JPanel buildStatsPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new BorderLayout());
    TitledBorder logFrameBorder=GuiFactory.buildTitledBorder("Warbands");
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
