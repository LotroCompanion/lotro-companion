package delta.games.lotro.gui.character.status.tasks.filter;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.area.AreaController;
import delta.common.ui.swing.combobox.ComboBoxController;
import delta.common.ui.swing.combobox.ItemSelectionListener;
import delta.common.ui.swing.panels.AbstractPanelController;
import delta.common.ui.swing.text.DynamicTextEditionController;
import delta.common.ui.swing.text.TextListener;
import delta.games.lotro.character.status.achievables.filter.QuestStatusFilter;
import delta.games.lotro.character.status.tasks.filter.TaskStatusFilter;
import delta.games.lotro.common.comparators.NamedComparator;
import delta.games.lotro.common.filters.NamedFilter;
import delta.games.lotro.common.rewards.ReputationReward;
import delta.games.lotro.common.rewards.Rewards;
import delta.games.lotro.common.rewards.filters.ReputationRewardFilter;
import delta.games.lotro.gui.lore.items.FilterUpdateListener;
import delta.games.lotro.gui.utils.SharedUiUtils;
import delta.games.lotro.gui.utils.l10n.Labels;
import delta.games.lotro.lore.quests.QuestDescription;
import delta.games.lotro.lore.quests.filter.QuestFilter;
import delta.games.lotro.lore.reputation.Faction;
import delta.games.lotro.lore.tasks.Task;
import delta.games.lotro.lore.tasks.TasksRegistry;

/**
 * Controller for a task filter edition panel.
 * @author DAM
 */
public class TaskFilterController extends AbstractPanelController implements ActionListener
{
  // Data
  private TaskStatusFilter _filter;
  // GUI
  private JButton _reset;
  // Name
  private JTextField _contains;
  // Reputation reward
  private ComboBoxController<Faction> _reputation;
  // Controllers
  private DynamicTextEditionController _textController;
  private FilterUpdateListener _filterUpdateListener;

  /**
   * Constructor.
   * @param parent Parent controller.
   * @param filter Managed filter.
   * @param filterUpdateListener Filter update listener.
   */
  public TaskFilterController(AreaController parent, TaskStatusFilter filter, FilterUpdateListener filterUpdateListener)
  {
    super(parent);
    _filter=filter;
    _filterUpdateListener=filterUpdateListener;
    JPanel panel=buildPanel();
    setPanel(panel);
  }

  /**
   * Get the managed filter.
   * @return the managed filter.
   */
  public TaskStatusFilter getFilter()
  {
    return _filter;
  }

  private JPanel buildPanel()
  {
    JPanel panel=build();
    setFilter();
    filterUpdated();
    return panel;
  }

  /**
   * Invoked when the managed filter has been updated.
   */
  private void filterUpdated()
  {
    _filterUpdateListener.filterUpdated();
  }

  @Override
  public void actionPerformed(ActionEvent e)
  {
    Object source=e.getSource();
    if (source==_reset)
    {
      _contains.setText("");
      _reputation.selectItem(null);
    }
  }

  private void setFilter()
  {
    QuestStatusFilter questStatusFilter=_filter.getQuestStatusFilter();
    QuestFilter questFilter=questStatusFilter.getQuestFilter();
    // Name
    NamedFilter<QuestDescription> nameFilter=questFilter.getNameFilter();
    String contains=nameFilter.getPattern();
    if (contains!=null)
    {
      _contains.setText(contains);
    }
    // Reputation
    ReputationRewardFilter factionFilter=questFilter.getRewardsFilter().getReputationFilter();
    Faction faction=factionFilter.getFaction();
    _reputation.selectItem(faction);
  }

  private JPanel build()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    int y=0;

    // Task attributes
    JPanel taskPanel=buildTaskPanel();
    taskPanel.setBorder(GuiFactory.buildTitledBorder("Task")); // I18n
    GridBagConstraints c=new GridBagConstraints(0,y,1,1,0.0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(taskPanel,c);

    // Reset
    _reset=GuiFactory.buildButton(Labels.getLabel("shared.reset"));
    _reset.addActionListener(this);
    c=new GridBagConstraints(1,y,1,1,0.0,0,GridBagConstraints.SOUTHWEST,GridBagConstraints.NONE,new Insets(0,5,5,0),0,0);
    panel.add(_reset,c);
    y++;

    return panel;
  }

  private JPanel buildTaskPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    int y=0;
    JPanel linePanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING,0,0));
    // Label filter
    JPanel containsPanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING,5,0));
    containsPanel.add(GuiFactory.buildLabel("Name filter:")); // I18n
    _contains=GuiFactory.buildTextField("");
    _contains.setColumns(20);
    containsPanel.add(_contains);
    TextListener listener=new TextListener()
    {
      @Override
      public void textChanged(String newText)
      {
        if (newText.length()==0) newText=null;
        QuestStatusFilter questStatusFilter=_filter.getQuestStatusFilter();
        QuestFilter questFilter=questStatusFilter.getQuestFilter();
        NamedFilter<QuestDescription> nameFilter=questFilter.getNameFilter();
        nameFilter.setPattern(newText);
        filterUpdated();
      }
    };
    _textController=new DynamicTextEditionController(_contains,listener);
    linePanel.add(containsPanel);
    JPanel repPanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING,5,0));
    // Reputation
    repPanel.add(GuiFactory.buildLabel("Reputation:")); // I18n
    _reputation=buildReputationCombobox();
    repPanel.add(_reputation.getComboBox());
    linePanel.add(repPanel);
    GridBagConstraints c=new GridBagConstraints(0,y,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,5,5,0),0,0);
    panel.add(linePanel,c);
    y++;

    return panel;
  }

  private ComboBoxController<Faction> buildReputationCombobox()
  {
    List<Faction> factions=buildFactions();
    ComboBoxController<Faction> combo=SharedUiUtils.buildFactionCombo(this,factions);
    ItemSelectionListener<Faction> listener=new ItemSelectionListener<Faction>()
    {
      @Override
      public void itemSelected(Faction faction)
      {
        QuestStatusFilter questStatusFilter=_filter.getQuestStatusFilter();
        QuestFilter questFilter=questStatusFilter.getQuestFilter();
        ReputationRewardFilter filter=questFilter.getRewardsFilter().getReputationFilter();
        filter.setFaction(faction);
        filterUpdated();
      }
    };
    combo.addListener(listener);
    return combo;
  }

  private static List<Faction> buildFactions()
  {
    Set<Faction> factions=new HashSet<Faction>();
    List<Task> tasks=TasksRegistry.getInstance().getTasks();
    for(Task task : tasks)
    {
      Rewards rewards=task.getQuest().getRewards();
      List<ReputationReward> repRewards=rewards.getRewardElementsOfClass(ReputationReward.class);
      if (repRewards.size()>0)
      {
        for(ReputationReward repReward : repRewards)
        {
          factions.add(repReward.getFaction());
        }
      }
    }
    List<Faction> ret=new ArrayList<Faction>(factions);
    Collections.sort(ret,new NamedComparator());
    return ret;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    super.dispose();
    // Data
    _filter=null;
    // Controllers
    if (_textController!=null)
    {
      _textController.dispose();
      _textController=null;
    }
    if (_reputation!=null)
    {
      _reputation.dispose();
      _reputation=null;
    }
    _contains=null;
    _reset=null;
  }
}
