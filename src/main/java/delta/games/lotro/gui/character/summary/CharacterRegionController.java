package delta.games.lotro.gui.character.summary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import delta.common.ui.swing.combobox.ComboBoxController;
import delta.games.lotro.common.Race;

/**
 * Controller for a character region combo box.
 * @author DAM
 */
public class CharacterRegionController
{
  private ComboBoxController<String> _regionController;
  private Race _race;

  private HashMap<Race,List<String>> _regionsByRace;

  /**
   * Constructor.
   */
  public CharacterRegionController()
  {
    initRegions();
    _regionController=new ComboBoxController<String>();
    setRace(Race.ALL_RACES[0]);
  }

  /**
   * Get the managed combobox controller.
   * @return a combobox controller.
   */
  public ComboBoxController<String> getComboBoxController()
  {
    return _regionController;
  }

  /**
   * Set the current race.
   * @param race Race to set.
   */
  public void setRace(Race race)
  {
    if (_race!=race)
    {
      List<String> regions=_regionsByRace.get(race);
      for(String region : regions)
      {
        _regionController.addItem(region,region);
      }
      if (_race!=null)
      {
        List<String> oldRegions=_regionsByRace.get(_race);
        for(String region : oldRegions)
        {
          _regionController.removeItem(region);
        }
      }
      _race=race;
    }
  }

  private void initRegions()
  {
    _regionsByRace=new HashMap<Race,List<String>>();
    // Men
    List<String> menRegions=new ArrayList<String>();
    menRegions.add("Bree-land");
    menRegions.add("Dale");
    menRegions.add("Gondor");
    menRegions.add("Rohan");
    _regionsByRace.put(Race.MAN,menRegions);

    // Elves
    List<String> elvesRegions=new ArrayList<String>();
    elvesRegions.add("Lindon");
    elvesRegions.add("Lórien");
    elvesRegions.add("Mirkwood");
    elvesRegions.add("Rivendell");
    elvesRegions.add("Edhellond");
    _regionsByRace.put(Race.ELF,elvesRegions);

    // Dwarves
    List<String> dwarvesRegions=new ArrayList<String>();
    dwarvesRegions.add("Blue Mountains");
    dwarvesRegions.add("Iron Hills");
    dwarvesRegions.add("Lonely Mountain");
    dwarvesRegions.add("Grey Mountains");
    dwarvesRegions.add("White Mountains");
    _regionsByRace.put(Race.DWARF,dwarvesRegions);

    // Hobbit
    List<String> hobbitsRegions=new ArrayList<String>();
    hobbitsRegions.add("Fallohides");
    hobbitsRegions.add("Harfoot");
    hobbitsRegions.add("Stoors");
    _regionsByRace.put(Race.HOBBIT,hobbitsRegions);

    // Beorning
    List<String> beorningsRegions=new ArrayList<String>();
    beorningsRegions.add("Vales of Anduin");
    _regionsByRace.put(Race.BEORNING,beorningsRegions);

    // High Elf
    List<String> highElvesRegions=new ArrayList<String>();
    highElvesRegions.add("Beleriand");
    highElvesRegions.add("Imladris");
    highElvesRegions.add("Nargothrond");
    highElvesRegions.add("Gondolin");
    highElvesRegions.add("Ossiriand");
    _regionsByRace.put(Race.HIGH_ELF,highElvesRegions);
  }
}
