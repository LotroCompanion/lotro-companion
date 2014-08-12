package delta.games.lotro.lore.items.bonus;

import java.util.ArrayList;
import java.util.List;

import delta.common.utils.text.EndOfLine;

/**
 * Bonus manager. Manages a set of bonus for an item.
 * @author DAM
 */
public class BonusManager
{
  private List<Bonus> _bonus;
  
  /**
   * Constructor.
   */
  public BonusManager()
  {
    _bonus=new ArrayList<Bonus>();
  }

  /**
   * Get the number of bonus. 
   * @return the number of bonus.
   */
  public int getNumberOfBonus()
  {
    return _bonus.size();
  }

  /**
   * Get a bonus. 
   * @param index Index of bonus to get (starting at 0).
   * @return a bonus.
   */
  public Bonus getBonusAt(int index)
  {
    return _bonus.get(index);
  }

  /**
   * Add a bonus.
   * @param bonus Bonus to add.
   */
  public void add(Bonus bonus)
  {
    _bonus.add(bonus);
  }

  /**
   * Dump the contents of this object as a string.
   * @return A readable string.
   */
  public String dump()
  {
    StringBuilder sb=new StringBuilder();
    sb.append("Bonuses:").append(EndOfLine.NATIVE_EOL);
    for(Bonus bonus : _bonus)
    {
      sb.append(bonus).append(EndOfLine.NATIVE_EOL);
    }
    String ret=sb.toString().trim();
    return ret;
  }
}
