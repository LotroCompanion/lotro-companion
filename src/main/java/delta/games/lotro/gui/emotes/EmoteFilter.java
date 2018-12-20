package delta.games.lotro.gui.emotes;

import java.util.ArrayList;
import java.util.List;

import delta.common.utils.collections.filters.CompoundFilter;
import delta.common.utils.collections.filters.Filter;
import delta.common.utils.collections.filters.Operator;
import delta.games.lotro.lore.emotes.EmoteDescription;
import delta.games.lotro.lore.emotes.filters.EmoteAutoFilter;
import delta.games.lotro.lore.emotes.filters.EmoteCommandFilter;

/**
 * Emote filter.
 * @author DAM
 */
public class EmoteFilter implements Filter<EmoteDescription>
{
  private Filter<EmoteDescription> _filter;

  private EmoteCommandFilter _nameFilter;
  private EmoteAutoFilter _autoFilter;

  /**
   * Constructor.
   */
  public EmoteFilter()
  {
    List<Filter<EmoteDescription>> filters=new ArrayList<Filter<EmoteDescription>>();
    // Command
    _nameFilter=new EmoteCommandFilter();
    filters.add(_nameFilter);
    // Auto
    _autoFilter=new EmoteAutoFilter(null);
    filters.add(_autoFilter);
    _filter=new CompoundFilter<EmoteDescription>(Operator.AND,filters);
  }

  /**
   * Get the filter on emote command.
   * @return an emote command filter.
   */
  public EmoteCommandFilter getCommandFilter()
  {
    return _nameFilter;
  }

  /**
   * Get the filter on the auto flag of emotes.
   * @return an emote auto filter.
   */
  public EmoteAutoFilter getAutoFilter()
  {
    return _autoFilter;
  }

  @Override
  public boolean accept(EmoteDescription item)
  {
    return _filter.accept(item);
  }
}
