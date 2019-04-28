package delta.games.lotro.gui.quests.filter;

import java.util.ArrayList;
import java.util.List;

import delta.common.utils.collections.filters.CompoundFilter;
import delta.common.utils.collections.filters.Filter;
import delta.common.utils.collections.filters.Operator;
import delta.common.utils.collections.filters.ProxyFilter;
import delta.common.utils.collections.filters.ProxyValueResolver;
import delta.games.lotro.common.requirements.UsageRequirement;
import delta.games.lotro.common.requirements.filters.UsageRequirementFilter;
import delta.games.lotro.common.rewards.Rewards;
import delta.games.lotro.gui.common.rewards.filter.RewardsFilter;
import delta.games.lotro.lore.quests.QuestDescription;
import delta.games.lotro.lore.quests.filter.QuestArcFilter;
import delta.games.lotro.lore.quests.filter.QuestCategoryFilter;
import delta.games.lotro.lore.quests.filter.QuestNameFilter;

/**
 * Quest filter.
 * @author DAM
 */
public class QuestFilter implements Filter<QuestDescription>
{
  private Filter<QuestDescription> _filter;

  private QuestNameFilter _nameFilter;
  private QuestCategoryFilter _categoryFilter;
  private QuestArcFilter _questArcFilter;
  // Requirements
  private UsageRequirementFilter _requirementsFilter;
  // Rewards
  private RewardsFilter _rewardsFilter;

  /**
   * Constructor.
   */
  public QuestFilter()
  {
    List<Filter<QuestDescription>> filters=new ArrayList<Filter<QuestDescription>>();
    // Name
    _nameFilter=new QuestNameFilter();
    filters.add(_nameFilter);
    // Category
    _categoryFilter=new QuestCategoryFilter(null);
    filters.add(_categoryFilter);
    // Quest arc
    _questArcFilter=new QuestArcFilter(null);
    filters.add(_questArcFilter);
    // Requirements
    {
      _requirementsFilter=new UsageRequirementFilter(null,null);
      ProxyValueResolver<QuestDescription,UsageRequirement> resolver=new ProxyValueResolver<QuestDescription,UsageRequirement>()
      {
        public UsageRequirement getValue(QuestDescription pojo)
        {
          return pojo.getUsageRequirement();
        }
      };
      ProxyFilter<QuestDescription,UsageRequirement> questRequirementsFilter=new ProxyFilter<QuestDescription,UsageRequirement>(resolver,_requirementsFilter);
      filters.add(questRequirementsFilter);
    }
    // Rewards
    {
      _rewardsFilter=new RewardsFilter();
      ProxyValueResolver<QuestDescription,Rewards> resolver=new ProxyValueResolver<QuestDescription,Rewards>()
      {
        public Rewards getValue(QuestDescription pojo)
        {
          return pojo.getQuestRewards();
        }
      };
      ProxyFilter<QuestDescription,Rewards> questRequirementsFilter=new ProxyFilter<QuestDescription,Rewards>(resolver,_rewardsFilter);
      filters.add(questRequirementsFilter);
    }
    _filter=new CompoundFilter<QuestDescription>(Operator.AND,filters);
  }

  /**
   * Get the filter on quest name.
   * @return a quest name filter.
   */
  public QuestNameFilter getNameFilter()
  {
    return _nameFilter;
  }

  /**
   * Get the filter on quest category.
   * @return a quest category filter.
   */
  public QuestCategoryFilter getCategoryFilter()
  {
    return _categoryFilter;
  }

  /**
   * Get the filter on quest arc.
   * @return a quest arc filter.
   */
  public QuestArcFilter getQuestArcFilter()
  {
    return _questArcFilter;
  }

  /**
   * Get the filter on requirements.
   * @return the requirements filter.
   */
  public UsageRequirementFilter getRequirementsFilter()
  {
    return _requirementsFilter;
  }

  /**
   * Get the filter on rewards.
   * @return the rewards filter.
   */
  public RewardsFilter getRewardsFilter()
  {
    return _rewardsFilter;
  }

  @Override
  public boolean accept(QuestDescription item)
  {
    return _filter.accept(item);
  }
}
