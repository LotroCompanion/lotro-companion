package delta.games.lotro.gui.character;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import delta.games.lotro.character.Character;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.gui.utils.GuiFactory;
import delta.games.lotro.gui.utils.OKCancelPanelController;

/**
 * Controller for characters selector panel.
 * @author DAM
 */
public class CharactersSelectorPanelController
{
  // Constants
  private static final int NB_MAX_COLUMNS=2;
  private static final int PREFERRED_NB_ITEMS_PER_COLUMN=10;
  private static final int PREFERRED_MAX_ITEMS=NB_MAX_COLUMNS*PREFERRED_NB_ITEMS_PER_COLUMN;

  // Data
  private List<CharacterFile> _toons;
  private HashMap<String,CharacterFile> _toonsMap;
  private Set<CharacterFile> _selectedToons;
  // GUI
  private HashMap<String,JCheckBox> _checkboxes;
  private JPanel _panel;
  private JPanel _checkBoxesPanel;
  // Controllers
  private OKCancelPanelController _okCancelController;
  
  /**
   * Constructor.
   * @param toons Managed toons.
   * @param selectedToons Selected toons.
   */
  public CharactersSelectorPanelController(List<CharacterFile> toons, List<CharacterFile> selectedToons)
  {
    _toons=new ArrayList<CharacterFile>();
    _toonsMap=new HashMap<String,CharacterFile>();
    _selectedToons=new HashSet<CharacterFile>();
    _checkboxes=new HashMap<String,JCheckBox>();
    _okCancelController=new OKCancelPanelController();
    //getPanel();
    for(CharacterFile toon : toons)
    {
      boolean selected=selectedToons.contains(toon);
      addToon(toon,selected);
    }
  }

  /**
   * Add a toon to this
   * @param toon Toon to add.
   * @param selected Indicates if this toon is pre-selected or not.
   */
  private void addToon(CharacterFile toon, boolean selected)
  {
    if (toon!=null)
    {
      String id=toon.getIdentifier();
      _toons.add(toon);
      _toonsMap.put(id,toon);
      setToonSelected(id,selected);
    }
  }

  /**
   * Select a toon.
   * @param toonId Identifier of the targeted toon.
   * @param selected Selection state to set.
   */
  private void setToonSelected(String toonId, boolean selected)
  {
    JCheckBox cb=_checkboxes.get(toonId);
    if (cb!=null)
    {
      cb.setSelected(selected);
    }
    CharacterFile toon=_toonsMap.get(toonId);
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
   * Get the managed ok/cancel controller.
   * @return A ok/cancel controller.
   */
  public OKCancelPanelController getOKCancelController()
  {
    return _okCancelController;
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
    JPanel panel=GuiFactory.buildPanel(new BorderLayout());
    _checkBoxesPanel=GuiFactory.buildPanel(new GridBagLayout());
    panel.add(_checkBoxesPanel,BorderLayout.CENTER);
    fillPanel();
    JPanel okCancelPanel=_okCancelController.getPanel();
    panel.add(okCancelPanel,BorderLayout.SOUTH);
    return panel;
  }

  private int[] getGridSize(int nbItems)
  {
    int nbColumns;
    int nbItemsPerColumn;
    if (nbItems<=PREFERRED_MAX_ITEMS)
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

  private void fillPanel()
  {
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,2,2,2),0,0);
    int nbItems=_toons.size();
    int[] size=getGridSize(nbItems);
    for(CharacterFile toon : _toons)
    {
      JCheckBox cb=buildToonCheckbox(toon);
      if (cb!=null)
      {
        _checkBoxesPanel.add(cb,c);
        c.gridy++;
        if (size[1]==c.gridy)
        {
          c.gridx++;
          c.gridy=0;
        }
      }
    }
  }

  /**
   * Indicates if the given toon is selected or not.
   * @param toon Targeted toon.
   * @return <code>true</code> if it is selected, <code>false</code> otherwise.
   */
  public boolean isSelected(CharacterFile toon)
  {
    return _selectedToons.contains(toon);
  }

  void updateSelection()
  {
    _selectedToons.clear();
    for(Map.Entry<String,JCheckBox> entry : _checkboxes.entrySet())
    {
      String id=entry.getKey();
      JCheckBox cb=entry.getValue();
      if (cb.isSelected())
      {
        CharacterFile toon=_toonsMap.get(id);
        _selectedToons.add(toon);
      }
    }
  }

  private JCheckBox buildToonCheckbox(CharacterFile toon)
  {
    JCheckBox ret=null;
    Character c=toon.getLastCharacterInfo();
    if (c!=null)
    {
      String name=c.getName();
      ret=GuiFactory.buildCheckbox(name);
      String id=toon.getIdentifier();
      _checkboxes.put(id,ret);
      boolean selected=isSelected(toon);
      ret.setSelected(selected);
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
    if (_checkBoxesPanel!=null)
    {
      _checkBoxesPanel.removeAll();
      _checkBoxesPanel=null;
    }
    _checkboxes=null;
    // Data
    _toons=null;
    _toonsMap=null;
    _selectedToons=null;
    // Controllers
    if (_okCancelController!=null)
    {
      _okCancelController.dispose();
      _okCancelController=null;
    }
  }
}
