package delta.games.lotro.gui.stats.traitPoints;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;

import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.CharacterSummary;
import delta.games.lotro.common.CharacterClass;
import delta.games.lotro.gui.utils.GuiFactory;
import delta.games.lotro.stats.traitPoints.TraitPoint;
import delta.games.lotro.stats.traitPoints.TraitPointFilter;
import delta.games.lotro.stats.traitPoints.TraitPoints;
import delta.games.lotro.stats.traitPoints.TraitPointsRegistry;
import delta.games.lotro.stats.traitPoints.TraitPointsStatus;
import delta.games.lotro.utils.gui.WindowController;

/**
 * Controller for a trait points edition panel.
 * @author DAM
 */
public class TraitPointsEditionPanelController
{
  // Controllers
  private WindowController _parent;
  private List<String> _labels;
  private List<TraitPointsTableController> _tableControllers;
  // Data
  private CharacterFile _character;
  // GUI
  private JPanel _panel;

  /**
   * Constructor.
   * @param parentController Parent controller.
   * @param character Character to edit.
   */
  public TraitPointsEditionPanelController(WindowController parentController, CharacterFile character)
  {
    _parent=parentController;
    _character=character;
    buildTables();
  }

  private void buildTables()
  {
    _tableControllers=new ArrayList<TraitPointsTableController>();
    _labels=new ArrayList<String>();
    CharacterSummary summary=_character.getSummary();
    CharacterClass characterClass=summary.getCharacterClass();
    TraitPointsRegistry registry=TraitPoints.get().getRegistry();
    List<TraitPoint> points=registry.getPointsForClass(characterClass);
    TraitPointsStatus pointsStatus=TraitPoints.get().load(_character);

    String[] categories={"Class", "Epic", "Quests", "Deeds"};
    for(String category : categories)
    {
      TraitPointFilter filter=new TraitPointFilter();
      filter.setCategory(category);
      List<TraitPoint> selectedPoints=new ArrayList<TraitPoint>();
      for(TraitPoint point : points)
      {
        if (filter.accept(point))
        {
          selectedPoints.add(point);
        }
      }
      TraitPointsTableController tableController=new TraitPointsTableController(pointsStatus,selectedPoints);
      _tableControllers.add(tableController);
      _labels.add(category);
    }
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

  private JPanel buildPanel()
  {
    JPanel panel=GuiFactory.buildBackgroundPanel(new BorderLayout());

    JTabbedPane tabbedPane=GuiFactory.buildTabbedPane();
    int nbTables=_tableControllers.size();
    for(int i=0;i<nbTables;i++)
    {
      TraitPointsTableController tableController=_tableControllers.get(i);
      String label=_labels.get(i);
      JTable table=tableController.getTable();
      JScrollPane scroll=GuiFactory.buildScrollPane(table);
      JPanel tablePanel=GuiFactory.buildBackgroundPanel(new BorderLayout());
      tablePanel.add(scroll,BorderLayout.CENTER);
      tabbedPane.add(label,tablePanel);
    }
    panel.add(tabbedPane,BorderLayout.CENTER);
    return panel;
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
    _parent=null;
    if (_tableControllers!=null)
    {
      for(TraitPointsTableController controller : _tableControllers)
      {
        controller.dispose();
      }
      _tableControllers.clear();
      _tableControllers=null;
    }
    // Data
    _character=null;
  }
}
