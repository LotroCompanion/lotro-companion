package delta.games.lotro.lore.items.bonus;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import delta.games.lotro.lore.items.bonus.Bonus.BONUS_OCCURRENCE;
import delta.games.lotro.utils.LotroLoggers;

/**
 * Parser for raw bonus lines.
 * @author DAM
 */
public class RawBonusParser
{
  private static final Logger _logger=LotroLoggers.getLotroLogger();

  private static final String BR="<br />";
  private static final String ON_USE="On Use:";
  private static final String MAX="Max:";
  private static final String DURATION="Duration: ";
  private static final String END_OF_LINE="\\n";

  private BONUS_OCCURRENCE _context;
  private Bonus _lastBonus;
  private boolean _hasWarn;

  /**
   * Build a bonus manager from a series of raw bonus lines. 
   * @param bonuses Raw bonus lines. 
   * @return A bonus manager.
   */
  public BonusManager build(List<String> bonuses)
  {
    BonusManager ret=null;
    bonuses=normalizeBonuses(bonuses);
    if (bonuses.size()>0)
    {
      ret=new BonusManager();
      _context=BONUS_OCCURRENCE.ALWAYS;
      _lastBonus=null;
      for(String bonusStr : bonuses)
      {
        parseBonus(ret,bonusStr);
      }
    }
    return ret;
  }

  /**
   * Indicates if this parser raised some warnings.
   * @return <code>true</code> if it has, <code>false</code> otherwise.
   */
  public boolean hasWarn()
  {
    return _hasWarn;
  }

  private void parseBonus(BonusManager bonusMgr, String bonusStr)
  {
    if (bonusStr.equals(ON_USE))
    {
      _context=BONUS_OCCURRENCE.ON_USE;
    }
    else if (bonusStr.startsWith(DURATION))
    {
      String durationStr=bonusStr.substring(DURATION.length()).trim();
      if (_lastBonus!=null)
      {
        _lastBonus.setDuration(durationStr);
      }
      else
      {
        _logger.warn("Cannot set duration ["+durationStr+"] with no last bonus");
        _hasWarn=true;
      }
    }
    else
    {
      BONUS_OCCURRENCE oldContext=null;
      if (bonusStr.startsWith(ON_USE))
      {
        oldContext=_context;
        _context=BONUS_OCCURRENCE.ON_USE;
        bonusStr=bonusStr.substring(ON_USE.length()).trim();
      }
      if (bonusStr.length()>0)
      {
        Bonus bonus=parseBonus(bonusStr);
        _lastBonus=bonus;
        bonusMgr.add(bonus);
        if (oldContext!=null)
        {
          _context=oldContext;
        }
      }
    }
  }
  
  private Bonus parseBonus(String bonusStr)
  {
    List<BonusType> bonusTypes=BonusType.getAll();
    Bonus bonus=null;
    for(BonusType bonusType : bonusTypes)
    {
      String label=bonusType.getLabel();
      if (bonusStr.endsWith(label))
      {
        String valueStr=bonusStr.substring(0,bonusStr.length()-label.length()).trim();
        bonus=new Bonus(bonusType,_context);
        if (valueStr.startsWith(MAX))
        {
          valueStr=valueStr.substring(MAX.length()).trim();
        }
        Object value=bonusType.buildValueFromString(valueStr);
        bonus.setValue(value);
        break;
      }
    }
    if (bonus==null)
    {
      bonus=new Bonus(BonusType.OTHER,_context);
      bonus.setValue(bonusStr);
    }
    return bonus;
  }
  
  private List<String> normalizeBonuses(List<String> bonuses)
  {
    List<String> newList=new ArrayList<String>();
    for(String bonus : bonuses)
    {
      if (bonus.length()>0)
      {
        while(true)
        {
          if (bonus.endsWith(END_OF_LINE))
          {
            bonus=bonus.substring(0,bonus.length()-END_OF_LINE.length());
          }
          int index=bonus.indexOf(BR);
          if (index==-1)
          {
            newList.add(bonus);
            break;
          }
          String b1=bonus.substring(0,index);
          if (b1.length()>0)
          {
            newList.add(b1);
          }
          bonus=bonus.substring(index+BR.length());
        }
      }
    }
    return newList;
  }
}
