package delta.games.lotro.gui.character.chooser;

import java.util.ArrayList;
import java.util.List;

import delta.games.lotro.character.CharacterFile;

/**
 * Character selection change event.
 * @author DAM
 */
public class CharacterSelectionStructureChangeEvent
{
  private List<CharacterFile> _oldToons;
  private List<CharacterFile> _newToons;
  private List<CharacterFile> _removedToons;
  private List<CharacterFile> _addedToons;

  /**
   * Constructor.
   * @param before Contents before event.
   * @param after Contents after event.
   */
  public CharacterSelectionStructureChangeEvent(List<CharacterFile> before, List<CharacterFile> after)
  {
    _oldToons=new ArrayList<CharacterFile>(before);
    _newToons=new ArrayList<CharacterFile>(after);
    _removedToons=new ArrayList<CharacterFile>();
    _addedToons=new ArrayList<CharacterFile>();
    computeAddAndRemoves();
  }

  private void computeAddAndRemoves()
  {
    _removedToons.addAll(_oldToons);
    for(CharacterFile toon : _newToons)
    {
      if (_oldToons.contains(toon))
      {
        _removedToons.remove(toon);
      }
      else
      {
        _addedToons.add(toon);
      }
    }
  }

  /**
   * Get the old toons selection.
   * @return the old toons selection.
   */
  public List<CharacterFile> getOldToons()
  {
    return _oldToons;
  }

  /**
   * Get the new toons selection.
   * @return the new toons selection.
   */
  public List<CharacterFile> getNewToons()
  {
    return _newToons;
  }

  /**
   * Get the removed toons.
   * @return a list of removed toons.
   */
  public List<CharacterFile> getRemovedToons()
  {
    return _removedToons;
  }

  /**
   * Get the added toons.
   * @return a list of added toons.
   */
  public List<CharacterFile> getAddedToons()
  {
    return _addedToons;
  }
}
