package delta.games.lotro.stats.traitPoints;

import delta.common.utils.collections.filters.Filter;

/**
 * Filter for trait points.
 * @author DAM
 */
public class TraitPointFilter implements Filter<TraitPoint>
{
  private String _category;

  /**
   * Constructor.
   */
  public TraitPointFilter()
  {
    _category=null;
  }

  /**
   * Get the category of selected items.
   * @return A category or <code>null</code> for no filter.
   */
  public String getCategory()
  {
    return _category;
  }
  
  /**
   * Set the category of selected items.
   * @param category A category or <code>null</code>.
   */
  public void setCategory(String category)
  {
    _category=category;
  }

  /**
   * Filter a trait point.
   * @param traitPoint Item to test.
   * @return <code>true</code> if it passes the filter, <code>false</code> otherwise.
   */
  public boolean accept(TraitPoint traitPoint)
  {
    boolean ret=true;
    if (_category!=null)
    {
      String category=traitPoint.getCategory();
      ret=_category.equals(category);
      if (!ret) return false;
    }
    return ret;
  }

  @Override
  public String toString()
  {
    StringBuilder sb=new StringBuilder();
    sb.append("Trait point filter: ");
    if (_category!=null)
    {
      sb.append(" Category: ").append(_category);
    }
    return sb.toString();
  }
}
