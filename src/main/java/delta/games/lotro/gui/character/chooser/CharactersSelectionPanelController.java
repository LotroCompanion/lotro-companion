package delta.games.lotro.gui.character.chooser;

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
import delta.common.utils.ListenersManager;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.CharactersManager;

/**
 * Controller for a panel to edit a selection of characters.
 * It provides:
 * <ul>
 * <li>a button a bring a character chooser.
 * <li>a panel to edit the selection state of each character.
 * </ul>
 * @author DAM
 */
public class CharactersSelectionPanelController
{
  // GUI
  private JPanel _panel;
  // Controllers
  private WindowController _parentController;
  private CharactersSelectionStateEditionPanelController _toonSelectionController;
  // Data
  private List<CharacterFile> _selectedToons;
  // Listeners
  private ListenersManager<CharacterSelectionStructureListener> _listeners;

  /**
   * Constructor.
   * @param parentController Parent window controller.
   * @param selectedToons Characters to use.
   */
  public CharactersSelectionPanelController(WindowController parentController, List<CharacterFile> selectedToons)
  {
    _parentController=parentController;
    _toonSelectionController=new CharactersSelectionStateEditionPanelController(selectedToons);
    _selectedToons=new ArrayList<CharacterFile>(selectedToons);
    _listeners=new ListenersManager<CharacterSelectionStructureListener>();
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
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    // Toons show/hide
    for(CharacterFile toon : _selectedToons)
    {
      _toonSelectionController.setToonSelected(toon,true);
      _toonSelectionController.setToonEnabled(toon,true);
    }

    _toonSelectionController.setGridConfiguration(1,10);
    JPanel selectionPanel=_toonSelectionController.getPanel();
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    ret.add(selectionPanel,c);

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
    chooser.setAlignmentY(Component.CENTER_ALIGNMENT);
    c.gridy++;
    ret.add(chooser,c);
    return ret;
  }

  private void doChooseToons()
  {
    CharactersManager manager=CharactersManager.getInstance();
    List<CharacterFile> toons=manager.getAllToons();
    List<CharacterFile> newSelectedToons=CharactersChooserController.selectToons(_parentController,toons,_selectedToons);
    if (newSelectedToons!=null)
    {
      CharacterSelectionStructureChangeEvent event=new CharacterSelectionStructureChangeEvent(_selectedToons,newSelectedToons);
      for(CharacterFile addedToon : event.getAddedToons())
      {
        _toonSelectionController.addToon(addedToon,true);
        _toonSelectionController.setToonEnabled(addedToon,true);
      }
      for(CharacterFile removedToon : event.getRemovedToons())
      {
        _toonSelectionController.removeToon(removedToon);
      }
      _toonSelectionController.refresh();
      _selectedToons.clear();
      _selectedToons.addAll(newSelectedToons);
      broadcastToonSelectionStructureChange(event);
    }
  }

  private void broadcastToonSelectionStructureChange(CharacterSelectionStructureChangeEvent event)
  {
    for(CharacterSelectionStructureListener listener : _listeners)
    {
      listener.selectionStructureChanged(event);
    }
  }

  /**
   * Get the 'character selection state change' listeners manager.
   * @return A listeners manager.
   */
  public ListenersManager<CharacterSelectionStateListener> getStateListenersManager()
  {
    return _toonSelectionController.getListenersManager();
  }

  /**
   * Get the 'character selection structure change' listeners manager.
   * @return A listeners manager.
   */
  public ListenersManager<CharacterSelectionStructureListener> getStructureListenersManager()
  {
    return _listeners;
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
    _parentController=null;
    if (_toonSelectionController!=null)
    {
      _toonSelectionController.dispose();
      _toonSelectionController=null;
    }
    // Data
    _selectedToons=null;
    // Listeners
    _listeners=null;
  }
}
