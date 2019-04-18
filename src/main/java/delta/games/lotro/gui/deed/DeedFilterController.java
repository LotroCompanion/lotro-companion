package delta.games.lotro.gui.deed;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.combobox.ComboBoxController;
import delta.common.ui.swing.combobox.ItemSelectionListener;
import delta.common.ui.swing.text.DynamicTextEditionController;
import delta.common.ui.swing.text.TextListener;
import delta.common.utils.collections.filters.Filter;
import delta.games.lotro.common.CharacterClass;
import delta.games.lotro.common.Race;
import delta.games.lotro.common.rewards.RewardsExplorer;
import delta.games.lotro.gui.character.summary.CharacterUiUtils;
import delta.games.lotro.gui.items.FilterUpdateListener;
import delta.games.lotro.gui.rewards.RewardsFilterController;
import delta.games.lotro.lore.deeds.DeedDescription;
import delta.games.lotro.lore.deeds.DeedType;
import delta.games.lotro.lore.deeds.DeedsManager;
import delta.games.lotro.lore.deeds.filters.DeedCategoryFilter;
import delta.games.lotro.lore.deeds.filters.DeedClassRequirementFilter;
import delta.games.lotro.lore.deeds.filters.DeedNameFilter;
import delta.games.lotro.lore.deeds.filters.DeedRaceRequirementFilter;
import delta.games.lotro.lore.deeds.filters.DeedTypeFilter;

/**
 * Controller for a deed filter edition panel.
 * @author DAM
 */
public class DeedFilterController implements ActionListener
{
  // Data
  private DeedFilter _filter;
  // GUI
  private JPanel _panel;
  private JButton _reset;
  // -- Deed attributes UI --
  private JTextField _contains;
  private ComboBoxController<DeedType> _type;
  private ComboBoxController<String> _category;
  // -- Requirements UI --
  private ComboBoxController<CharacterClass> _class;
  private ComboBoxController<Race> _race;
  // -- Rewards UI --
  private RewardsFilterController _rewards;
  // Controllers
  private DynamicTextEditionController _textController;
  private FilterUpdateListener _filterUpdateListener;

  /**
   * Constructor.
   * @param filter Managed filter.
   * @param filterUpdateListener Filter update listener.
   */
  public DeedFilterController(DeedFilter filter, FilterUpdateListener filterUpdateListener)
  {
    _filter=filter;
    _filterUpdateListener=filterUpdateListener;
    RewardsExplorer explorer=DeedsManager.getInstance().buildRewardsExplorer();
    _rewards=new RewardsFilterController(filter.getRewardsFilter(),filterUpdateListener,explorer);
  }

  /**
   * Get the managed filter.
   * @return the managed filter.
   */
  public Filter<DeedDescription> getFilter()
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
      _type.selectItem(null);
      _category.selectItem(null);
      _class.selectItem(null);
      _race.selectItem(null);
      _rewards.reset();
      _contains.setText("");
    }
  }

  private void setFilter()
  {
    // Name
    DeedNameFilter nameFilter=_filter.getNameFilter();
    String contains=nameFilter.getPattern();
    if (contains!=null)
    {
      _contains.setText(contains);
    }
    // Type
    DeedTypeFilter typeFilter=_filter.getTypeFilter();
    DeedType type=typeFilter.getDeedType();
    _type.selectItem(type);
    // Category
    DeedCategoryFilter categoryFilter=_filter.getCategoryFilter();
    String category=categoryFilter.getDeedCategory();
    _category.selectItem(category);

    // Requirements:
    // - class
    DeedClassRequirementFilter classFilter=_filter.getClassFilter();
    CharacterClass requiredClass=classFilter.getCharacterClass();
    _class.selectItem(requiredClass);
    // - race
    DeedRaceRequirementFilter raceFilter=_filter.getRaceFilter();
    Race requiredRace=raceFilter.getRace();
    _race.selectItem(requiredRace);

    // Rewards:
    _rewards.setFilter();
  }

  private JPanel build()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    int y=0;

    // Deed attributes
    JPanel deedPanel=buildDeedPanel();
    Border deedBorder=GuiFactory.buildTitledBorder("Deed");
    deedPanel.setBorder(deedBorder);
    GridBagConstraints c=new GridBagConstraints(0,y,1,1,0.0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(deedPanel,c);

    // Reset
    _reset=GuiFactory.buildButton("Reset");
    _reset.addActionListener(this);
    c=new GridBagConstraints(1,y,1,1,0.0,0,GridBagConstraints.SOUTHWEST,GridBagConstraints.NONE,new Insets(0,5,5,0),0,0);
    panel.add(_reset,c);
    y++;

    // Requirements
    JPanel requirementsPanel=buildRequirementsPanel();
    Border requirementsBorder=GuiFactory.buildTitledBorder("Requirements");
    requirementsPanel.setBorder(requirementsBorder);
    c=new GridBagConstraints(0,y,2,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(requirementsPanel,c);
    y++;

    // Rewards
    JPanel rewardsPanel=_rewards.getPanel();
    Border rewardsBorder=GuiFactory.buildTitledBorder("Rewards");
    rewardsPanel.setBorder(rewardsBorder);
    c=new GridBagConstraints(0,y,2,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(rewardsPanel,c);
    y++;

    return panel;
  }

  private JPanel buildDeedPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    int y=0;
    JPanel line1Panel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING,0,0));
    // Label filter
    {
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
          DeedNameFilter nameFilter=_filter.getNameFilter();
          nameFilter.setPattern(newText);
          filterUpdated();
        }
      };
      _textController=new DynamicTextEditionController(_contains,listener);
      line1Panel.add(containsPanel);
    }
    GridBagConstraints c=new GridBagConstraints(0,y,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,5,0),0,0);
    panel.add(line1Panel,c);
    y++;

    JPanel line2Panel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING,5,0));
    // Type
    {
      JLabel label=GuiFactory.buildLabel("Type:");
      line2Panel.add(label);
      _type=DeedUiUtils.buildDeedTypeCombo();
      ItemSelectionListener<DeedType> typeListener=new ItemSelectionListener<DeedType>()
      {
        @Override
        public void itemSelected(DeedType type)
        {
          DeedTypeFilter typeFilter=_filter.getTypeFilter();
          typeFilter.setDeedType(type);
          filterUpdated();
        }
      };
      _type.addListener(typeListener);
      line2Panel.add(_type.getComboBox());
    }
    // Category
    {
      JLabel label=GuiFactory.buildLabel("Category:");
      line2Panel.add(label);
      _category=DeedUiUtils.buildCategoryCombo();
      ItemSelectionListener<String> categoryListener=new ItemSelectionListener<String>()
      {
        @Override
        public void itemSelected(String category)
        {
          DeedCategoryFilter categoryFilter=_filter.getCategoryFilter();
          categoryFilter.setDeedCategory(category);
          filterUpdated();
        }
      };
      _category.addListener(categoryListener);
      line2Panel.add(_category.getComboBox());
    }
    c=new GridBagConstraints(0,y,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,5,0),0,0);
    panel.add(line2Panel,c);
    y++;

    return panel;
  }

  private JPanel buildRequirementsPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    int y=0;

    GridBagConstraints c;
    {
      JPanel linePanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING,5,0));
      // Class
      linePanel.add(GuiFactory.buildLabel("Class:"));
      _class=buildCharacterClassCombobox();
      linePanel.add(_class.getComboBox());
      // Race
      linePanel.add(GuiFactory.buildLabel("Race:"));
      _race=buildRaceCombobox();
      linePanel.add(_race.getComboBox());
      c=new GridBagConstraints(0,y,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(5,0,0,0),0,0);
      panel.add(linePanel,c);
      y++;
    }

    return panel;
  }

  private ComboBoxController<CharacterClass> buildCharacterClassCombobox()
  {
    ComboBoxController<CharacterClass> combo=CharacterUiUtils.buildClassCombo(true);
    ItemSelectionListener<CharacterClass> listener=new ItemSelectionListener<CharacterClass>()
    {
      @Override
      public void itemSelected(CharacterClass requiredClass)
      {
        DeedClassRequirementFilter filter=_filter.getClassFilter();
        filter.setCharacterClass(requiredClass);
        filterUpdated();
      }
    };
    combo.addListener(listener);
    return combo;
  }

  private ComboBoxController<Race> buildRaceCombobox()
  {
    ComboBoxController<Race> combo=CharacterUiUtils.buildRaceCombo(true);
    ItemSelectionListener<Race> listener=new ItemSelectionListener<Race>()
    {
      @Override
      public void itemSelected(Race requiredRace)
      {
        DeedRaceRequirementFilter filter=_filter.getRaceFilter();
        filter.setRace(requiredRace);
        filterUpdated();
      }
    };
    combo.addListener(listener);
    return combo;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Data
    _filter=null;
    // Controllers
    if (_textController!=null)
    {
      _textController.dispose();
      _textController=null;
    }
    // GUI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    if (_type!=null)
    {
      _type.dispose();
      _type=null;
    }
    if (_category!=null)
    {
      _category.dispose();
      _category=null;
    }
    if (_rewards!=null)
    {
      _rewards.dispose();
    }
    _contains=null;
    _reset=null;
  }
}
