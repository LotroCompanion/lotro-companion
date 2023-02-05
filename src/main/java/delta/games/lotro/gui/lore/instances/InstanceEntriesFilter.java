package delta.games.lotro.gui.lore.instances;

import java.util.ArrayList;
import java.util.List;

import delta.common.utils.collections.filters.CompoundFilter;
import delta.common.utils.collections.filters.Filter;
import delta.common.utils.collections.filters.Operator;
import delta.common.utils.collections.filters.ProxyFilter;
import delta.common.utils.collections.filters.ProxyValueResolver;
import delta.games.lotro.lore.instances.InstanceTreeEntry;
import delta.games.lotro.lore.instances.SkirmishPrivateEncounter;
import delta.games.lotro.lore.instances.filters.InstanceTreeEntryCategoryNameFilter;

/**
 * Instance filter.
 * @author DAM
 */
public class InstanceEntriesFilter implements Filter<InstanceTreeEntry>
{
  private Filter<InstanceTreeEntry> _filter;
  private InstancesFilter _instancesFilter;
  private InstanceTreeEntryCategoryNameFilter _nameFilter;

  /**
   * Constructor.
   */
  public InstanceEntriesFilter()
  {
    List<Filter<InstanceTreeEntry>> filters=new ArrayList<Filter<InstanceTreeEntry>>();
    // Name
    _nameFilter=new InstanceTreeEntryCategoryNameFilter();
    filters.add(_nameFilter);
    // Instance filter
    _instancesFilter=new InstancesFilter();
    ProxyValueResolver<InstanceTreeEntry,SkirmishPrivateEncounter> peProvider=new ProxyValueResolver<InstanceTreeEntry,SkirmishPrivateEncounter>()
    {
      public SkirmishPrivateEncounter getValue(InstanceTreeEntry entry)
      {
        return entry.getInstance();
      }
    };
    ProxyFilter<InstanceTreeEntry,SkirmishPrivateEncounter> proxyFilter=new ProxyFilter<>(peProvider,_instancesFilter);
    filters.add(proxyFilter);
    _filter=new CompoundFilter<InstanceTreeEntry>(Operator.AND,filters);
  }

  /**
   * Get the filter on category name.
   * @return a category name filter.
   */
  public InstanceTreeEntryCategoryNameFilter getNameFilter()
  {
    return _nameFilter;
  }

  /**
   * Get the filter on instances.
   * @return an instances filter.
   */
  public InstancesFilter getInstancesFilter()
  {
    return _instancesFilter;
  }

  @Override
  public boolean accept(InstanceTreeEntry item)
  {
    return _filter.accept(item);
  }
}
