package delta.games.lotro.gui.lore.loot.instances.filter;

import delta.games.lotro.common.enums.Difficulty;
import delta.games.lotro.common.enums.GroupSize;

/**
 * Instance parameters.
 * @author DAM
 */
public class InstanceParameters
{
  private Difficulty _difficulty;
  private GroupSize _size;
  private int _level;

  /**
   * Constructor.
   * @param difficulty Difficulty.
   * @param size Size.
   * @param level Level.
   */
  public InstanceParameters(Difficulty difficulty, GroupSize size, int level)
  {
    _difficulty=difficulty;
    _size=size;
    _level=level;
  }

  /**
   * Get the difficulty.
   * @return the difficulty.
   */
  public Difficulty getDifficulty()
  {
    return _difficulty;
  }

  /**
   * Set the difficulty.
   * @param difficulty the difficulty to set.
   */
  public void setDifficulty(Difficulty difficulty)
  {
    _difficulty=difficulty;
  }

  /**
   * Get the group size.
   * @return the size.
   */
  public GroupSize getSize()
  {
    return _size;
  }

  /**
   * Set the group size.
   * @param size the size to set.
   */
  public void setSize(GroupSize size)
  {
    _size=size;
  }

  /**
   * Get the level.
   * @return the level.
   */
  public int getLevel()
  {
    return _level;
  }

  /**
   * Set the level.
   * @param level the level to set.
   */
  public void setLevel(int level)
  {
    _level=level;
  }

  @Override
  public String toString()
  {
    return "Difficulty: "+_difficulty+", size="+_size+", level="+_level;
  }
}
