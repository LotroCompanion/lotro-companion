package delta.games.lotro.gui.stats.crafting.synopsis;

import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.crafting.GuildStatus;
import delta.games.lotro.character.crafting.ProfessionStatus;
import delta.games.lotro.character.reputation.FactionStatus;
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
  private GuildStatus _guildStatus;

  /**
   * Constructor.
   * @param character Targeted character.
   * @param professionStatus Profession status.
   * @param guildStatus Guild status.
   */
  public CraftingSynopsisItem(CharacterFile character, ProfessionStatus professionStatus, GuildStatus guildStatus)
  {
    _character=character;
    _status=professionStatus;
    _guildStatus=guildStatus;
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

  /**
   * Get the guild faction status.
   * @return A faction status or <code>null</code>.
   */
  public FactionStatus getGuildFaction()
  {
    if (_guildStatus!=null)
    {
      return _guildStatus.getFactionStatus();
    }
    return null;
  }
}
