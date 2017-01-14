package delta.games.lotro.gui.character;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import delta.common.utils.ListenersManager;
import delta.games.lotro.character.CharacterData;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.CharacterSummary;
import delta.games.lotro.gui.utils.GuiFactory;

/**
 * Controller for characters selector panel.
 * @author DAM
 */
public class CharactersSelectorPanelController implements ItemListener
{
  // Constants
  private static final int NB_MAX_COLUMNS=2;
  private static final int PREFERRED_NB_ITEMS_PER_COLUMN=10;

  // Data
  private List<CharacterFile> _toons;
  private HashMap<String,CharacterFile> _toonsMap;
  private Set<CharacterFile> _selectedToons;
  private Set<CharacterFile> _enabledToons;
  // GUI
  private HashMap<String,JCheckBox> _checkboxes;
  private JPanel _panel;
  // Configuration
  private int _nbMaxColumns;
  private int _nbPreferredNbItemsPerColumn;
  // Listeners
  private ListenersManager<CharacterSelectionChangedListener> _listeners;
  
  /**
   * Constructor.
   * @param toons Managed toons.
   */
  public CharactersSelectorPanelController(List<CharacterFile> toons)
  {
    _nbMaxColumns=NB_MAX_COLUMNS;
    _nbPreferredNbItemsPerColumn=PREFERRED_NB_ITEMS_PER_COLUMN;
    _listeners=new ListenersManager<CharacterSelectionChangedListener>();
    _toons=new ArrayList<CharacterFile>();
    _toonsMap=new HashMap<String,CharacterFile>();
    _selectedToons=new HashSet<CharacterFile>();
    _enabledToons=new HashSet<CharacterFile>();
    _checkboxes=new HashMap<String,JCheckBox>();
    for(CharacterFile toon : toons)
    {
      addToon(toon,false);
    }
  }

  /**
   * Grid size configuration.
   * @param nbMaxColumns Maximum number of columns.
   * @param nbPreferredItemsPerColumn Preferred number of items per columns.
   */
  public void setGridConfiguration(int nbMaxColumns, int nbPreferredItemsPerColumn)
  {
    _nbMaxColumns=nbMaxColumns;
    _nbPreferredNbItemsPerColumn=nbPreferredItemsPerColumn;
  }

  /**
   * Add a toon to this selection panel.
   * @param toon Toon to add.
   * @param selected Indicates if this toon is pre-selected or not.
   */
  public void addToon(CharacterFile toon, boolean selected)
  {
    if (toon!=null)
    {
      String id=toon.getIdentifier();
      _toons.add(toon);
      _toonsMap.put(id,toon);
      setToonSelected(toon,selected);
    }
  }

  /**
   * Remove a toon from this selection panel.
   * @param toon Toon to remove.
   */
  public void removeToon(CharacterFile toon)
  {
    if (toon!=null)
    {
      String id=toon.getIdentifier();
      JCheckBox cb=_checkboxes.get(id);
      _panel.remove(cb);
      _checkboxes.remove(id);
      _toons.remove(toon);
      _toonsMap.remove(id);
      _selectedToons.remove(toon);
      _enabledToons.remove(toon);
    }
  }

  /**
   * Get the 'character selection change' listeners manager. 
   * @return A listeners manager.
   */
  public ListenersManager<CharacterSelectionChangedListener> getListenersManager()
  {
    return _listeners;
  }

  public void itemStateChanged(ItemEvent e)
  {
    JCheckBox source = (JCheckBox)e.getItemSelectable();
    for(Map.Entry<String,JCheckBox> entry : _checkboxes.entrySet())
    {
      JCheckBox cb=entry.getValue();
      if (cb==source)
      {
        String id=entry.getKey();
        boolean selected = (e.getStateChange()==ItemEvent.SELECTED);
        CharacterFile toon=_toonsMap.get(id);
        if (selected)
        {
          _selectedToons.add(toon);
        }
        else
        {
          _selectedToons.remove(toon);
        }
        broadcastToonSelectionChange(id,selected);
        break;
      }
    }
  }

  private void broadcastToonSelectionChange(String toonId, boolean selected)
  {
    for(CharacterSelectionChangedListener listener : _listeners)
    {
      listener.selectionChanged(toonId,selected);
    }
  }

  /**
   * Select a toon.
   * @param toon Targeted toon.
   * @param selected Selection state to set.
   */
  public void setToonSelected(CharacterFile toon, boolean selected)
  {
    String toonId=toon.getIdentifier();
    JCheckBox cb=_checkboxes.get(toonId);
    if (cb!=null)
    {
      cb.setSelected(selected);
    }
    if (selected)
    {
      _selectedToons.add(toon);
    }
    else
    {
      _selectedToons.remove(toon);
    }
  }

  /**
   * Enable a toon.
   * @param toon Targeted toon.
   * @param enabled Enabled state to set.
   */
  public void setToonEnabled(CharacterFile toon, boolean enabled)
  {
    String toonId=toon.getIdentifier();
    JCheckBox cb=_checkboxes.get(toonId);
    if (cb!=null)
    {
      cb.setEnabled(enabled);
    }
    if (enabled)
    {
      _enabledToons.add(toon);
    }
    else
    {
      _enabledToons.remove(toon);
    }
  }

  /**
   * Get the list of selected toons.
   * @return A possibly empty list of toons.
   */
  public List<CharacterFile> getSelectedToons()
  {
    List<CharacterFile> selectedToons;
    if (_selectedToons!=null)
    {
      selectedToons=new ArrayList<CharacterFile>();
      for(CharacterFile toon : _toons)
      {
        boolean selected=isSelected(toon);
        if (selected)
        {
          selectedToons.add(toon);
        }
      }
    }
    else
    {
      selectedToons=null;
    }
    return selectedToons;
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
    _panel=GuiFactory.buildPanel(new GridBagLayout());
    refresh();
    return _panel;
  }
  
  /**
   * Refresh the managed panel (to synchronize with new/removed toons).
   */
  public void refresh()
  {
    if (_panel!=null)
    {
      _panel.removeAll();
      GridBagConstraints c=new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,2,2,2),0,0);
      int nbItems=_toons.size();
      int[] size=getGridSize(nbItems);
      for(CharacterFile toon : _toons)
      {
        String toonID=toon.getIdentifier();
        JCheckBox cb=_checkboxes.get(toonID);
        if (cb==null)
        {
          cb=buildToonCheckbox(toon);
        }
        if (cb!=null)
        {
          _panel.add(cb,c);
          c.gridy++;
          if (size[1]==c.gridy)
          {
            c.gridx++;
            c.gridy=0;
          }
        }
      }
      _panel.revalidate();
      _panel.repaint();
    }
  }

  private int[] getGridSize(int nbItems)
  {
    int nbColumns;
    int nbItemsPerColumn;
    int preferredMaxItems=_nbMaxColumns*_nbPreferredNbItemsPerColumn;
    if (nbItems<=preferredMaxItems)
    {
      nbColumns=nbItems/PREFERRED_NB_ITEMS_PER_COLUMN+((nbItems%PREFERRED_NB_ITEMS_PER_COLUMN!=0)?1:0);
      nbItemsPerColumn=PREFERRED_NB_ITEMS_PER_COLUMN;
    }
    else
    {
      nbColumns=NB_MAX_COLUMNS;
      nbItemsPerColumn=nbItems/NB_MAX_COLUMNS+((nbItems%NB_MAX_COLUMNS!=0)?1:0);
    }
    return new int[]{nbColumns,nbItemsPerColumn};
  }

  /**
   * Indicates if the given toon is selected or not.
   * @param toon Targeted toon.
   * @return <code>true</code> if it is selected, <code>false</code> otherwise.
   */
  private boolean isSelected(CharacterFile toon)
  {
    return _selectedToons.contains(toon);
  }

  private JCheckBox buildToonCheckbox(CharacterFile toon)
  {
    JCheckBox ret=null;
    CharacterSummary summary=toon.getSummary();
    if (summary!=null)
    {
      String name=summary.getName();
      ret=GuiFactory.buildCheckbox(name);
      String id=toon.getIdentifier();
      _checkboxes.put(id,ret);
      boolean selected=isSelected(toon);
      ret.setSelected(selected);
      boolean enabled=_enabledToons.contains(toon);
      ret.setEnabled(enabled);
      ret.addItemListener(this);
    }
    return ret;
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
    _checkboxes=null;
    // Data
    _toons=null;
    _toonsMap=null;
    _selectedToons=null;
    _enabledToons=null;
  }
}
