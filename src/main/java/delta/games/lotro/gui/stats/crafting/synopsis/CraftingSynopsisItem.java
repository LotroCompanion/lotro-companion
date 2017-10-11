package delta.games.lotro.gui.stats.crafting.synopsis;

import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.crafting.ProfessionStatus;
import delta.games.lotro.crafting.CraftingLevel;
import delta.games.lotro.crafting.Profession;

/**
 * Gathers crafting data for a single toon and a single profession.
 * @author DAM
 */
public class CraftingSynopsisItem
{
  private CharacterFile _character;
  private ProfessionStatus _status;

  /**
   * Constructor.
   * @param character Targeted character.
   * @param professionStatus Profession status.
   */
  public CraftingSynopsisItem(CharacterFile character, ProfessionStatus professionStatus)
  {
    _character=character;
    _status=professionStatus;
  }

  /**
   * Get the targeted character.
   * @return a character.
   */
  public CharacterFile getCharacter()
  {
    return _character;
  }

  /**
   * Get the targeted profession.
   * @return a profession.
   */
  public Profession getProfession()
  {
    return _status.getProfession();
  }

  /**
   * Get the level reached.
   * @param mastery Mastery or proficiency level?
   * @return A crafting level.
   */
  public CraftingLevel getLevel(boolean mastery)
  {
    CraftingLevel level=mastery?_status.getMasteryLevel():_status.getProficiencyLevel();
    return level;
  }
}
