package delta.games.lotro.utils.strings;

import delta.games.lotro.character.BaseCharacterSummary;
import delta.games.lotro.character.classes.ClassDescription;
import delta.games.lotro.character.races.RaceDescription;
import delta.games.lotro.common.CharacterSex;
import delta.games.lotro.dat.data.strings.renderer.VariableValueProvider;

/**
 * A variable value provider that handles the variable found in lore objects (quests, deeds, titles, ...):
 * <ul>
 * <li>PLAYER,
 * <li>PLAYERNAME,
 * <li>NAME,
 * <li>SURNAME,
 * <li>RANK,
 * <li>CLASS,
 * <li>RACE
 * </ul>
 * @author DAM
 */
public class ContextVariableValueProvider implements VariableValueProvider
{
  private VariableValueProvider _parent;
  // Character name + tag for gender
  private String _name;
  private String _surname;
  private String _rank;
  // Class name + tag ([x])
  private String _class;
  // Race name + tag ([x])
  private String _race;

  /**
   * Constructor.
   * @param parent Parent provider.
   */
  public ContextVariableValueProvider(VariableValueProvider parent)
  {
    _parent=parent;
  }

  /**
   * Setup this provider with the character attributes.
   * @param attrs Attributes to use.
   */
  public void setup(BaseCharacterSummary attrs)
  {
    // Gender
    CharacterSex gender=attrs.getCharacterSex();
    char genderTag=(gender==CharacterSex.FEMALE)?'f':'m';
    // Name
    String name=attrs.getName();
    _name=name+"["+genderTag+"]";
    // Surname
    // TODO
    _surname="";
    // Rank
    // TODO
    _rank="";
    // Class
    ClassDescription characterClass=attrs.getCharacterClass();
    if (characterClass!=null)
    {
      String className=characterClass.getName();
      String classTag=characterClass.getTag();
      _class=className+"["+classTag+"]";
    }
    // Race
    RaceDescription race=attrs.getRace();
    if (race!=null)
    {
      String raceName=race.getName();
      String raceTag=race.getTag();
      _race=raceName+"["+raceTag+"]";
    }
  }

  @Override
  public String getVariable(String variableName)
  {
    if ("NAME".equals(variableName)) return _name;
    if ("RANK".equals(variableName)) return _rank;
    if ("SURNAME".equals(variableName)) return _surname;
    if ("CLASS".equals(variableName)) return _class;
    if ("RACE".equals(variableName)) return _race;
    if ("PLAYER".equals(variableName)) return _name;
    if ("PLAYERNAME".equals(variableName)) return _name;
    // Unmanaged:
    /*
    if ("NUMBER".equals(variableName)) return "n";
    if ("TOTAL".equals(variableName)) return "t";
    if ("MAX".equals(variableName)) return "m";
    if ("CURRENT".equals(variableName)) return "c";
    if ("VALUE".equals(variableName)) return "v";
    if ("NOS".equals(variableName)) return "?";
    */
    return null;
  }
}
