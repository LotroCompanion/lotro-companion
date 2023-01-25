package delta.games.lotro.gui.toon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import delta.common.ui.swing.combobox.ComboBoxController;
import delta.games.lotro.character.classes.ClassDescription;
import delta.games.lotro.character.classes.ClassesManager;
import delta.games.lotro.character.races.RaceDescription;
import delta.games.lotro.character.races.RacesManager;
import delta.games.lotro.common.comparators.NamedComparator;

/**
 * Controller for a character class combo box.
 * @author DAM
 */
public class CharacterClassController
{
  private ComboBoxController<ClassDescription> _classController;
  private RaceDescription _race;

  /**
   * Constructor.
   */
  public CharacterClassController()
  {
    _classController=new ComboBoxController<ClassDescription>();
    setRace(RacesManager.getInstance().getAll().get(0));
  }

  /**
   * Get the managed combobox controller.
   * @return a combobox controller.
   */
  public ComboBoxController<ClassDescription> getComboBoxController()
  {
    return _classController;
  }

  /**
   * Set the current race.
   * @param race Race to set.
   */
  public void setRace(RaceDescription race)
  {
    if (_race!=race)
    {
      List<ClassDescription> characterClasses=getClassesForRace(race);
      for(ClassDescription characterClass : characterClasses)
      {
        _classController.addItem(characterClass,characterClass.getName());
      }
      if (_race!=null)
      {
        List<ClassDescription> oldClasses=getClassesForRace(_race);
        for(ClassDescription cClass : oldClasses)
        {
          _classController.removeItem(cClass);
        }
      }
      _race=race;
    }
  }

  private List<ClassDescription> getClassesForRace(RaceDescription race)
  {
    List<ClassDescription> ret=new ArrayList<ClassDescription>();
    ClassesManager mgr=ClassesManager.getInstance();
    for(String classKey : race.getAllowedClasses())
    {
      ClassDescription characterClass=mgr.getCharacterClassByKey(classKey);
      if (characterClass!=null)
      {
        ret.add(characterClass);
      }
    }
    Collections.sort(ret,new NamedComparator());
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
