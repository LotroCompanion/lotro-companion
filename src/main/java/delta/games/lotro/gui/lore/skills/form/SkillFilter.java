package delta.games.lotro.gui.lore.skills.form;

import java.util.ArrayList;
import java.util.List;

import delta.common.utils.collections.filters.CompoundFilter;
import delta.common.utils.collections.filters.Filter;
import delta.common.utils.collections.filters.Operator;
import delta.games.lotro.character.skills.SkillDescription;
import delta.games.lotro.character.skills.filters.SkillNameFilter;

/**
 * Skill filter.
 * @author DAM
 */
public class SkillFilter implements Filter<SkillDescription>
{
  private Filter<SkillDescription> _filter;

  private SkillNameFilter _nameFilter;

  /**
   * Constructor.
   */
  public SkillFilter()
  {
    List<Filter<SkillDescription>> filters=new ArrayList<Filter<SkillDescription>>();
    // Command
    _nameFilter=new SkillNameFilter();
    filters.add(_nameFilter);
    _filter=new CompoundFilter<SkillDescription>(Operator.AND,filters);
  }

  /**
   * Get the filter on skill name.
   * @return a skill name command filter.
   */
  public SkillNameFilter getNameFilter()
  {
    return _nameFilter;
  }

  @Override
  public boolean accept(SkillDescription item)
  {
    return _filter.accept(item);
  }
}
