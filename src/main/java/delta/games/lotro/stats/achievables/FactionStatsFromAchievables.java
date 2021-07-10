package delta.games.lotro.stats.achievables;

import delta.games.lotro.lore.reputation.Faction;

/**
 * Statistics for a single faction.
 * @author DAM
 */
public class FactionStatsFromAchievables
{
  private Faction _faction;
  private int _points;
  private int _achievablesCount;
  private int _completionsCount;

  /**
   * Constructor.
   * @param faction Managed faction.
   */
  public FactionStatsFromAchievables(Faction faction)
  {
    _faction=faction;
  }

  /**
   * Get the managed faction.
   * @return a faction.
   */
  public Faction getFaction()
  {
    return _faction;
  }

  /**
   * Add a reputation amount for a single achievable.
   * @param completionCount Completion count.
   * @param amountByCompletion Reputation amount by completion.
   */
  public void addCompletions(int completionCount, int amountByCompletion)
  {
    _points+=(amountByCompletion*completionCount);
    _achievablesCount++;
    _completionsCount+=completionCount;
  }

  /**
   * Get the total reputation points earned for this faction
   * by the registered achievables.
   * @return A points count.
   */
  public int getPoints()
  {
    return _points;
  }

  /**
   * Get the number of unique achievables used to build this reputation.
   * @return A count.
   */
  public int getAchievablesCount()
  {
    return _achievablesCount;
  }

  /**
   * Get the number of completions used to build this reputation.
   * @return A count.
   */
  public int getCompletionsCount()
  {
    return _completionsCount;
  }
}
