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
import delta.common.ui.swing.combobox.ComboBoxController;
import delta.common.ui.swing.combobox.ItemSelectionListener;
import delta.common.ui.swing.text.DynamicTextEditionController;
import delta.common.ui.swing.text.TextListener;
import delta.games.lotro.character.status.achievables.filter.QuestStatusFilter;
import delta.games.lotro.character.status.tasks.filter.TaskStatusFilter;
import delta.games.lotro.common.rewards.ReputationReward;
import delta.games.lotro.common.rewards.Rewards;
import delta.games.lotro.common.rewards.filters.ReputationRewardFilter;
import delta.games.lotro.gui.lore.items.FilterUpdateListener;
import delta.games.lotro.lore.quests.filter.QuestFilter;
import delta.games.lotro.lore.quests.filter.QuestNameFilter;
import delta.games.lotro.lore.reputation.Faction;
import delta.games.lotro.lore.reputation.FactionNameComparator;
import delta.games.lotro.lore.tasks.Task;
import delta.games.lotro.lore.tasks.TasksRegistry;

/**
 * Controller for a task filter edition panel.
 * @author DAM
 */
public class TaskFilterController implements ActionListener
{
  // Data
  private TaskStatusFilter _filter;
  // GUI
  private JPanel _panel;
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
   * @param filter Managed filter.
   * @param filterUpdateListener Filter update listener.
   * @param useRequirements Use requirements or not.
   */
  public TaskFilterController(TaskStatusFilter filter, FilterUpdateListener filterUpdateListener, boolean useRequirements)
  {
    _filter=filter;
    _filterUpdateListener=filterUpdateListener;
  }

  /**
   * Get the managed filter.
   * @return the managed filter.
   */
  public TaskStatusFilter getFilter()
  {
    return _filter;
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  public JPanel getPanel()
  {
    if (_panel==null)
    {
      _panel=build();
      setFilter();
      filterUpdated();
    }
    return _panel;
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
    QuestNameFilter nameFilter=questFilter.getNameFilter();
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
    taskPanel.setBorder(GuiFactory.buildTitledBorder("Task"));
    GridBagConstraints c=new GridBagConstraints(0,y,1,1,0.0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(taskPanel,c);

    // Reset
    _reset=GuiFactory.buildButton("Reset");
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
    containsPanel.add(GuiFactory.buildLabel("Name filter:"));
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
        QuestNameFilter nameFilter=questFilter.getNameFilter();
        nameFilter.setPattern(newText);
        filterUpdated();
      }
    };
    _textController=new DynamicTextEditionController(_contains,listener);
    linePanel.add(containsPanel);
    // Reputation
    linePanel.add(GuiFactory.buildLabel("Reputation:"));
    _reputation=buildReputationCombobox();
    linePanel.add(_reputation.getComboBox());
    GridBagConstraints c=new GridBagConstraints(0,y,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,5,5,0),0,0);
    panel.add(linePanel,c);
    y++;

    return panel;
  }

  private ComboBoxController<Faction> buildReputationCombobox()
  {
    ComboBoxController<Faction> combo=buildFactionCombo();
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

  /**
   * Build a combo-box controller to choose a faction.
   * @return A new combo-box controller.
   */
  public static ComboBoxController<Faction> buildFactionCombo()
  {
    ComboBoxController<Faction> ctrl=new ComboBoxController<Faction>();
    ctrl.addEmptyItem("");
    List<Faction> factions=buildFactions();
    for(Faction faction : factions)
    {
      ctrl.addItem(faction,faction.getName());
    }
    ctrl.selectItem(null);
    return ctrl;
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
    Collections.sort(ret,new FactionNameComparator());
    return ret;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Data
    _filter=null;
    // GUI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
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
