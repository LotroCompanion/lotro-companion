package delta.games.lotro.gui.lore.loot.instances.filter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import delta.games.lotro.common.enums.Difficulty;
import delta.games.lotro.common.enums.GroupSize;
import delta.games.lotro.common.enums.comparator.LotroEnumEntryCodeComparator;

/**
 * Configuration of the instance loot filter.
 * @author DAM
 */
public class InstanceLootParametersConfiguration
{
  private Set<Difficulty> _difficulties;
  private Set<GroupSize> _groupSize;
  private Set<Integer> _levels;

  /**
   * Constructor.
   */
  public InstanceLootParametersConfiguration()
  {
    _difficulties=new HashSet<Difficulty>();
    _groupSize=new HashSet<GroupSize>();
    _levels=new HashSet<Integer>();
  }

  /**
   * Get the difficulties to use.
   * @return A set of difficulties.
   */
  public List<Difficulty> getDifficulties()
  {
    List<Difficulty> ret=new ArrayList<Difficulty>(_difficulties);
    Collections.sort(ret,new LotroEnumEntryCodeComparator<Difficulty>());
    return ret;
  }

  /**
   * Add a difficulty.
   * @param difficulty Difficulty to add.
   */
  public void addDifficulty(Difficulty difficulty)
  {
    _difficulties.add(difficulty);
  }

  /**
   * Get the group sizes to use.
   * @return A list of group sizes.
   */
  public List<GroupSize> getGroupSizes()
  {
    List<GroupSize> ret=new ArrayList<GroupSize>(_groupSize);
    Collections.sort(ret,new LotroEnumEntryCodeComparator<GroupSize>());
    return ret;
  }

  /**
   * Add a group size.
   * @param groupSize Group size to add.
   */
  public void addGroupSize(GroupSize groupSize)
  {
    _groupSize.add(groupSize);
  }

  /**
   * Get the levels to use.
   * @return A list of levels.
   */
  public List<Integer> getLevels()
  {
    List<Integer> ret=new ArrayList<Integer>(_levels);
    Collections.sort(ret);
    return ret;
  }

  /**
   * Add a level.
   * @param level Level to add.
   */
  public void addLevel(int level)
  {
    _levels.add(Integer.valueOf(level));
  }
}
