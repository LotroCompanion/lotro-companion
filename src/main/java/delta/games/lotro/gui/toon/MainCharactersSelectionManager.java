package delta.games.lotro.gui.toon;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import delta.common.ui.swing.windows.WindowController;
import delta.common.utils.collections.filters.Filter;
import delta.common.utils.misc.Preferences;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.Config;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.CharactersManager;
import delta.games.lotro.gui.character.chooser.CharactersChooserController;

/**
 * Manages the selection of "main" characters (those displayed in the main table).
 * @author DAM
 */
public class MainCharactersSelectionManager implements Filter<CharacterFile>
{
  private static final String MAIN_CHARACTERS_PREFERENCES_NAME="mainCharacters";
  private static final String HIDDEN_CHARACTERS_PREFERENCE="hidden.characters";

  private Set<String> _hiddenIds;

  /**
   * Constructor.
   */
  public MainCharactersSelectionManager()
  {
    _hiddenIds=new HashSet<String>();
    init();
  }

  private void init()
  {
    Preferences preferences=Config.getInstance().getPreferences();
    TypedProperties props=preferences.getPreferences(MAIN_CHARACTERS_PREFERENCES_NAME);
    List<String> toonIds=props.getStringList(HIDDEN_CHARACTERS_PREFERENCE);
    if ((toonIds!=null) && (!toonIds.isEmpty()))
    {
      _hiddenIds.addAll(toonIds);
    }
  }

  @Override
  public boolean accept(CharacterFile item)
  {
    String id=item.getIdentifier();
    if (_hiddenIds.contains(id))
    {
      return false;
    }
    return true;
  }

  /**
   * Choose the characters to show.
   * @param parent Parent window.
   * @return <code>true</code> if selection was validated, <code>false</code> otherwise.
   */
  public boolean chooseCharacters(WindowController parent)
  {
    CharactersManager manager=CharactersManager.getInstance();
    List<CharacterFile> toons=manager.getAllToons();
    List<CharacterFile> selectedToons=new ArrayList<CharacterFile>();
    for(CharacterFile toon : toons)
    {
      if (accept(toon))
      {
        selectedToons.add(toon);
      }
    }
    List<CharacterFile> newSelectedToons=CharactersChooserController.selectToons(parent,toons,selectedToons);
    if (newSelectedToons!=null)
    {
      updateHiddenCharacters(newSelectedToons);
      Preferences preferences=Config.getInstance().getPreferences();
      TypedProperties props=preferences.getPreferences(MAIN_CHARACTERS_PREFERENCES_NAME);
      props.setStringList(HIDDEN_CHARACTERS_PREFERENCE,new ArrayList<String>(_hiddenIds));
      return true;
    }
    return false;
  }

  private void updateHiddenCharacters(List<CharacterFile> selectedToons)
  {
    _hiddenIds.clear();
    CharactersManager manager=CharactersManager.getInstance();
    List<CharacterFile> toons=manager.getAllToons();
    for(CharacterFile toon : toons)
    {
      String id=toon.getIdentifier();
      _hiddenIds.add(id);
    }
    for(CharacterFile toon : selectedToons)
    {
      String id=toon.getIdentifier();
      _hiddenIds.remove(id);
    }
  }
}
