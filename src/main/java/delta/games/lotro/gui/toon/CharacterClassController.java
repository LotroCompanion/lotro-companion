package delta.games.lotro.gui.toon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import delta.common.ui.swing.combobox.ComboBoxController;
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

  private HashMap<Race,List<CharacterClass>> _classesByRace;

  /**
   * Constructor.
   */
  public CharacterClassController()
  {
    initClasses();
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
      List<CharacterClass> classes=_classesByRace.get(race);
      for(CharacterClass cClass : classes)
      {
        _classController.addItem(cClass,cClass.getLabel());
      }
      if (_race!=null)
      {
        List<CharacterClass> oldClasses=_classesByRace.get(_race);
        for(CharacterClass cClass : oldClasses)
        {
          _classController.removeItem(cClass);
        }
      }
      _race=race;
    }
  }

  private void initClasses()
  {
    _classesByRace=new HashMap<Race,List<CharacterClass>>();
    // Men
    List<CharacterClass> menClasses=new ArrayList<CharacterClass>();
    menClasses.add(CharacterClass.BURGLAR);
    menClasses.add(CharacterClass.CAPTAIN);
    menClasses.add(CharacterClass.CHAMPION);
    menClasses.add(CharacterClass.GUARDIAN);
    menClasses.add(CharacterClass.HUNTER);
    menClasses.add(CharacterClass.LORE_MASTER);
    menClasses.add(CharacterClass.MINSTREL);
    menClasses.add(CharacterClass.WARDEN);
    _classesByRace.put(Race.MAN,menClasses);

    // Elves
    List<CharacterClass> elvesClasses=new ArrayList<CharacterClass>();
    elvesClasses.add(CharacterClass.CHAMPION);
    elvesClasses.add(CharacterClass.GUARDIAN);
    elvesClasses.add(CharacterClass.HUNTER);
    elvesClasses.add(CharacterClass.LORE_MASTER);
    elvesClasses.add(CharacterClass.MINSTREL);
    elvesClasses.add(CharacterClass.RUNE_KEEPER);
    elvesClasses.add(CharacterClass.WARDEN);
    _classesByRace.put(Race.ELF,elvesClasses);

    // Dwarves
    List<CharacterClass> dwarvesClasses=new ArrayList<CharacterClass>();
    dwarvesClasses.add(CharacterClass.CHAMPION);
    dwarvesClasses.add(CharacterClass.GUARDIAN);
    dwarvesClasses.add(CharacterClass.HUNTER);
    dwarvesClasses.add(CharacterClass.MINSTREL);
    dwarvesClasses.add(CharacterClass.RUNE_KEEPER);
    _classesByRace.put(Race.DWARF,dwarvesClasses);

    // Hobbit
    List<CharacterClass> hobbitsClasses=new ArrayList<CharacterClass>();
    hobbitsClasses.add(CharacterClass.BURGLAR);
    hobbitsClasses.add(CharacterClass.GUARDIAN);
    hobbitsClasses.add(CharacterClass.HUNTER);
    hobbitsClasses.add(CharacterClass.MINSTREL);
    hobbitsClasses.add(CharacterClass.WARDEN);
    _classesByRace.put(Race.HOBBIT,hobbitsClasses);

    // Beorning
    List<CharacterClass> beorningsClasses=new ArrayList<CharacterClass>();
    beorningsClasses.add(CharacterClass.BEORNING);
    _classesByRace.put(Race.BEORNING,beorningsClasses);

    // High Elf
    List<CharacterClass> highElvesClasses=new ArrayList<CharacterClass>();
    highElvesClasses.add(CharacterClass.CAPTAIN);
    highElvesClasses.add(CharacterClass.CHAMPION);
    highElvesClasses.add(CharacterClass.GUARDIAN);
    highElvesClasses.add(CharacterClass.HUNTER);
    highElvesClasses.add(CharacterClass.LORE_MASTER);
    highElvesClasses.add(CharacterClass.MINSTREL);
    highElvesClasses.add(CharacterClass.RUNE_KEEPER);
    highElvesClasses.add(CharacterClass.WARDEN);
    _classesByRace.put(Race.HIGH_ELF,highElvesClasses);
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
    _classesByRace=null;
    _race=null;
  }
}
