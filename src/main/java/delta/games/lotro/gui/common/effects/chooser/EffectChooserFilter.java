package delta.games.lotro.gui.common.effects.chooser;

import java.util.ArrayList;
import java.util.List;

import delta.common.utils.collections.filters.CompoundFilter;
import delta.common.utils.collections.filters.Filter;
import delta.common.utils.collections.filters.Operator;
import delta.games.lotro.common.effects.Effect;
import delta.games.lotro.common.effects.filters.EffectIDFilter;
import delta.games.lotro.common.filters.NamedFilter;

/**
 * Filter on effects for the effects chooser.
 * @author DAM
 */
public class EffectChooserFilter implements Filter<Effect>
{
  // Data
  private Filter<Effect> _filter;
  private NamedFilter<Effect> _nameFilter;
  private EffectIDFilter _mobFilter;
  private EffectIDFilter _skillFilter;

  /**
   * Constructor.
   */
  public EffectChooserFilter()
  {
    List<Filter<Effect>> filters=new ArrayList<Filter<Effect>>();
    // Name
    _nameFilter=new NamedFilter<Effect>();
    filters.add(_nameFilter);
    // Mob effects
    _mobFilter=new EffectIDFilter();
    filters.add(_mobFilter);
    // Skill effects
    _skillFilter=new EffectIDFilter();
    filters.add(_skillFilter);
    _filter=new CompoundFilter<Effect>(Operator.AND,filters);
  }

  /**
   * Get the name filter.
   * @return a name filter.
   */
  public NamedFilter<Effect> getNameFilter()
  {
    return _nameFilter;
  }

  /**
   * Get the mob filter.
   * @return the mob filter.
   */
  public EffectIDFilter getMobFilter()
  {
    return _mobFilter;
  }

  /**
   * Get the skill filter.
   * @return the skill filter.
   */
  public EffectIDFilter getSkillFilter()
  {
    return _skillFilter;
  }

  @Override
  public boolean accept(Effect item)
  {
    return _filter.accept(item);
  }
}
