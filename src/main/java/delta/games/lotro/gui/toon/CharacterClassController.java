package delta.games.lotro.gui.toon;

import java.util.ArrayList;
import java.util.List;

import delta.common.ui.swing.combobox.ComboBoxController;
import delta.games.lotro.character.races.RaceDescription;
import delta.games.lotro.character.races.RacesManager;
import delta.games.lotro.common.CharacterClass;
import delta.games.lotro.common.Race;

/**
 * Controller for a character class combo box.
 * @author DAM
 */
public class CharacterClassController
{
  private ComboBoxController<CharacterClass> _classController;
  private Race _race;

  /**
   * Constructor.
   */
  public CharacterClassController()
  {
    _classController=new ComboBoxController<CharacterClass>();
    setRace(Race.ALL_RACES[0]);
  }

  /**
   * Get the managed combobox controller.
   * @return a combobox controller.
   */
  public ComboBoxController<CharacterClass> getComboBoxController()
  {
    return _classController;
  }

  /**
   * Set the current race.
   * @param race Race to set.
   */
  public void setRace(Race race)
  {
    if (_race!=race)
    {
      List<CharacterClass> classes=getClassesForRace(race);
      for(CharacterClass cClass : classes)
      {
        _classController.addItem(cClass,cClass.getLabel());
      }
      if (_race!=null)
      {
        List<CharacterClass> oldClasses=getClassesForRace(_race);
        for(CharacterClass cClass : oldClasses)
        {
          _classController.removeItem(cClass);
        }
      }
      _race=race;
    }
  }

  private List<CharacterClass> getClassesForRace(Race race)
  {
    List<CharacterClass> ret=new ArrayList<CharacterClass>();
    RaceDescription description=RacesManager.getInstance().getRaceDescription(race);
    if (description!=null)
    {
      ret.addAll(description.getAllowedClasses());
    }
    return ret;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    if (_classController!=null)
    {
      _classController.dispose();
      _classController=null;
    }
    _race=null;
  }
}
