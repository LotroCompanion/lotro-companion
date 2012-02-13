package delta.games.lotro.character.log;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import delta.games.lotro.character.log.CharacterLogItem.LogItemType;

/**
 * Filter for character log items.
 * @author DAM
 */
public class CharacterLogItemsFilter
{
  private Long _minDate;
  private Long _maxDate;
  private String _contains;
  private Set<LogItemType> _selectedTypes;

  /**
   * Constructor.
   */
  public CharacterLogItemsFilter()
  {
    _minDate=null;
    _maxDate=null;
    _contains=null;
    _selectedTypes=null;
  }

  /**
   * Get the minimum date of selected items.
   * @return A date or <code>null</code> for no filter.
   */
  public Long getMinDate()
  {
    return _minDate;
  }
  
  /**
   * Set minimum date of selected items.
   * @param minDate A minimum date or <code>null</code>.
   */
  public void setMinDate(Long minDate)
  {
    _minDate=minDate;
  }

  /**
   * Get the maximum date of selected items.
   * @return A date or <code>null</code> for no filter.
   */
  public Long getMaxDate()
  {
    return _maxDate;
  }
  
  /**
   * Set maximum date of selected items.
   * @param maxDate A maximum date or <code>null</code>.
   */
  public void setMaxDate(Long maxDate)
  {
    _maxDate=maxDate;
  }

  /**
   * Get the label filter.
   * @return A label filter or <code>null</code> for no filter.
   */
  public String getLabelFilter()
  {
    return _contains;
  }

  /**
   * Set the pattern used to filter items on their label.
   * @param contains A pattern or <code>null</code>.
   */
  public void setLabelFilter(String contains)
  {
    if (contains!=null)
    {
      contains=contains.toLowerCase();
    }
    _contains=contains;
  }

  /**
   * Get the selected types.
   * @return A set of types or <code>null</code> for no filter.
   */
  public Set<LogItemType> getSelectedTypes()
  {
    Set<LogItemType> ret=null;
    if (_selectedTypes!=null)
    {
      ret=new HashSet<LogItemType>(_selectedTypes);
    }
    return ret;
  }

  /**
   * Set the types selection.
   * @param types A set of types to select or <code>null</code>.
   */
  public void setSelectedTypes(Set<LogItemType> types)
  {
    if (types==null)
    {
      _selectedTypes=null;
    }
    else
    {
      _selectedTypes=new HashSet<LogItemType>(types);      
    }
  }

  /**
   * Filter a log item.
   * @param item Item to test.
   * @return <code>true</code> if it passes the filter, <code>false</code> otherwise.
   */
  public boolean filterItem(CharacterLogItem item)
  {
    boolean ret=true;
    if (_contains!=null)
    {
      String label=item.getLabel().toLowerCase();
      ret=label.contains(_contains);
      if (!ret) return false;
    }
    if (_minDate!=null)
    {
      long itemDate=item.getDate();
      ret=(itemDate>=_minDate.longValue());
      if (!ret) return false;
    }
    if (_maxDate!=null)
    {
      long itemDate=item.getDate();
      ret=(itemDate<=_maxDate.longValue());
      if (!ret) return false;
    }
    LogItemType type=item.getLogItemType();
    if (_selectedTypes!=null)
    {
      ret=_selectedTypes.contains(type);
    }
    return ret;
  }

  @Override
  public String toString()
  {
    StringBuilder sb=new StringBuilder();
    sb.append("Types: ").append(_selectedTypes);
    if (_minDate!=null)
    {
      sb.append(", After: ").append(new Date(_minDate.longValue()));
    }
    if (_maxDate!=null)
    {
      sb.append(", before: ").append(new Date(_maxDate.longValue()));
    }
    if (_contains!=null)
    {
      sb.append(", contains: [").append(_contains).append(']');
    }
    return sb.toString();
  }
}
