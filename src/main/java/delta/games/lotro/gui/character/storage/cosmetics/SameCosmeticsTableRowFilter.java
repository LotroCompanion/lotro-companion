package delta.games.lotro.gui.character.storage.cosmetics;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import delta.common.utils.collections.filters.Filter;
import delta.games.lotro.gui.character.storage.StorageFilter;

/**
 * Filter for the 'same cosmetics' rows.
 * @author DAM
 */
public class SameCosmeticsTableRowFilter implements Filter<SameCosmeticsTableRow>
{
  private StorageFilter _filter;
  private List<SameCosmeticsTableRow> _rows;
  private Set<Integer> _selectedGroups;

  /**
   * Constructor.
   */
  public SameCosmeticsTableRowFilter()
  {
    _filter=new StorageFilter();
    _rows=new ArrayList<SameCosmeticsTableRow>();
    _selectedGroups=new HashSet<Integer>();
  }

  /**
   * Get the managed 'storage' filter.
   * @return the managed 'storage' filter.
   */
  public StorageFilter getStorageFilter()
  {
    return _filter;
  }

  /**
   * Set the managed rows.
   * @param rows Rows to set.
   */
  public void setRows(List<SameCosmeticsTableRow> rows)
  {
    _rows.clear();
    _rows.addAll(rows);
  }

  /**
   * Update the managed filter.
   */
  public void updateFilter()
  {
    Set<Integer> selectedGroupOds=getSelectedGroups();
    _selectedGroups.clear();
    _selectedGroups.addAll(selectedGroupOds);
  }

  private Set<Integer> getSelectedGroups()
  {
    Set<Integer> ret=new HashSet<Integer>();
    for(SameCosmeticsTableRow row : _rows)
    {
      if (_filter.accept(row.getStoredItem()))
      {
        int groupID=row.getGroup().getGroupID();
        ret.add(Integer.valueOf(groupID));
      }
    }
    return ret;
  }

  @Override
  public boolean accept(SameCosmeticsTableRow item)
  {
    int groupID=item.getGroup().getGroupID();
    return _selectedGroups.contains(Integer.valueOf(groupID));
  }
}
