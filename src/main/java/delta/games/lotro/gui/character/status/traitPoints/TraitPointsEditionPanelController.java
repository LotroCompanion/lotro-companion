package delta.games.lotro.gui.character.status.traitPoints;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.BasicCharacterAttributes;
import delta.games.lotro.character.status.traitPoints.TraitPoint;
import delta.games.lotro.character.status.traitPoints.TraitPointFilter;
import delta.games.lotro.character.status.traitPoints.TraitPoints;
import delta.games.lotro.character.status.traitPoints.TraitPointsRegistry;
import delta.games.lotro.character.status.traitPoints.TraitPointsStatus;
import delta.games.lotro.common.CharacterClass;

/**
 * Controller for a trait points edition panel.
 * @author DAM
 */
public class TraitPointsEditionPanelController
{
  // Controllers
  private List<String> _labels;
  private List<TraitPointsTableController> _tableControllers;
  private TraitPointsSummaryPanelController _summaryController;
  // Data
  private BasicCharacterAttributes _attrs;
  private TraitPointsStatus _status;
  // GUI
  private JPanel _panel;

  /**
   * Constructor.
   * @param parent Parent window controller.
   * @param attrs Attributes of toon to use.
   * @param status Status to edit.
   */
  public TraitPointsEditionPanelController(WindowController parent,BasicCharacterAttributes attrs, TraitPointsStatus status)
  {
    _attrs=attrs;
    _status=status;
    _summaryController=new TraitPointsSummaryPanelController(_attrs,_status);
    buildTables(parent);
  }

  private void buildTables(WindowController parent)
  {
    _tableControllers=new ArrayList<TraitPointsTableController>();
    _labels=new ArrayList<String>();
    CharacterClass characterClass=_attrs.getCharacterClass();
    TraitPointsRegistry registry=TraitPoints.get().getRegistry();
    List<TraitPoint> points=registry.getPointsForClass(characterClass);

    TraitPointsStatusListener listener=new TraitPointsStatusListener()
    {
      @Override
      public void statusUpdated()
      {
        _summaryController.update();
      }
    };
    List<String> categories=getCategories();
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
      TraitPointsTableController tableController=new TraitPointsTableController(parent,_status,selectedPoints);
      _tableControllers.add(tableController);
      _labels.add(category);
      tableController.setListener(listener);
    }
  }

  private List<String> getCategories()
  {
    List<String> ret=new ArrayList<String>();
    List<TraitPoint> traitPoints=TraitPoints.get().getRegistry().getPointsForClass(_attrs.getCharacterClass());
    for(TraitPoint traitPoint : traitPoints)
    {
      String id=traitPoint.getId();
      if (!ret.contains(id))
      {
        ret.add(id);
      }
    }
    return ret;
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
    JPanel panel=GuiFactory.buildPanel(new BorderLayout());

    // Header
    JPanel summaryPanel=_summaryController.getPanel();
    panel.add(summaryPanel,BorderLayout.NORTH);
    // Tables
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
    if (_summaryController!=null)
    {
      _summaryController.dispose();
      _summaryController=null;
    }
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
    _attrs=null;
    _status=null;
  }
}
