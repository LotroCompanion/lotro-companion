package delta.games.lotro.stats.achievables;

import delta.games.lotro.lore.quests.Achievable;

/**
 * Statistics for a single virtue.
 * @author DAM
 */
public class VirtueXPStatsFromAchievable
{
  private Achievable _achievable;
  private int _points;
  private int _completionsCount;

  /**
   * Constructor.
   * @param achievable Achievable.
   */
  public VirtueXPStatsFromAchievable(Achievable achievable)
  {
    _achievable=achievable;
  }

  /**
   * Get the managed achievable.
   * @return an achievable.
   */
  public Achievable getAchievable()
  {
    return _achievable;
  }

  /**
   * Add a reputation amount for a single achievable.
   * @param completionCount Completion count.
   * @param amountByCompletion Reputation amount by completion.
   */
  public void setCompletions(int completionCount, int amountByCompletion)
  {
    _points+=(amountByCompletion*completionCount);
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
   * Get the number of completions used to build this reputation.
   * @return A count.
   */
  public int getCompletionsCount()
  {
    return _completionsCount;
  }
}
