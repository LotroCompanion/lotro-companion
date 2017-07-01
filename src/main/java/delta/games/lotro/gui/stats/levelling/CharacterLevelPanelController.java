package delta.games.lotro.gui.stats.levelling;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.CharactersManager;
import delta.games.lotro.gui.character.CharacterSelectionChangedListener;
import delta.games.lotro.gui.character.CharactersSelectorPanelController;
import delta.games.lotro.gui.character.CharactersSelectorWindowController;
import delta.games.lotro.stats.level.MultipleToonsLevellingStats;

/**
 * Controller for a characters levelling panel.
 * @author DAM
 */
public class CharacterLevelPanelController implements CharacterSelectionChangedListener
{
  // GUI
  private JPanel _panel;
  // Controllers
  private WindowController _parentController;
  private CharacterLevelChartController _chartController;
  private CharactersSelectorPanelController _toonSelectionController;
  // Data
  private MultipleToonsLevellingStats _stats;

  /**
   * Constructor.
   * @param parentController Parent window controller.
   * @param stats Levelling stats to display.
   */
  public CharacterLevelPanelController(WindowController parentController, MultipleToonsLevellingStats stats)
  {
    _parentController=parentController;
    _stats=stats;
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

    _chartController=new CharacterLevelChartController(_stats);
    JPanel chartPanel=_chartController.getPanel();
    panel.add(chartPanel,BorderLayout.CENTER);

    JPanel toonsControlPanel=GuiFactory.buildPanel(new GridBagLayout());
    {
      // Toons show/hide
      List<CharacterFile> toons=_stats.getToonsList();
      _toonSelectionController=new CharactersSelectorPanelController(toons);
      for(CharacterFile toon : toons)
      {
        _toonSelectionController.setToonSelected(toon,true);
        _toonSelectionController.setToonEnabled(toon,true);
      }

      _toonSelectionController.setGridConfiguration(1,10);
      JPanel selectionPanel=_toonSelectionController.getPanel();
      GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
      toonsControlPanel.add(selectionPanel,c);
      _toonSelectionController.getListenersManager().addListener(this);

      // Choose toons button
      JButton chooser=GuiFactory.buildButton("Choose characters...");
      ActionListener al=new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          doChooseToons();
        }
      };
      chooser.addActionListener(al);
      chooser.setAlignmentY(Component.CENTER_ALIGNMENT);
      c.gridy++;
      toonsControlPanel.add(chooser,c);
    }
    panel.add(toonsControlPanel,BorderLayout.EAST);
    return panel;
  }

  private void doChooseToons()
  {
    CharactersManager manager=CharactersManager.getInstance();
    List<CharacterFile> toons=manager.getAllToons();
    List<CharacterFile> selectedToons=_stats.getToonsList();
    List<CharacterFile> enabledToons=new ArrayList<CharacterFile>();
    for(CharacterFile toon : toons)
    {
      //if (toon.hasLog())
      {
        enabledToons.add(toon);
      }
    }
    List<CharacterFile> newSelectedToons=CharactersSelectorWindowController.selectToons(_parentController,toons,selectedToons,enabledToons);
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
          _toonSelectionController.addToon(toon,true);
          _toonSelectionController.setToonEnabled(toon,true);
        }
      }
      for(CharacterFile removedToon : selectedToons)
      {
        _stats.removeToon(removedToon);
        _toonSelectionController.removeToon(removedToon);
      }
      _toonSelectionController.refresh();
      _chartController.refresh();
    }
  }

  public void selectionChanged(String toonId, boolean selected)
  {
    if (_chartController!=null)
    {
      _chartController.setVisible(toonId,selected);
    }
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    _parentController=null;
    if (_chartController!=null)
    {
      _chartController.dispose();
      _chartController=null;
    }
    if (_toonSelectionController!=null)
    {
      _toonSelectionController.dispose();
      _toonSelectionController=null;
    }
    _stats=null;
  }
}
