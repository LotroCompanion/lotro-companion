package delta.games.lotro.gui.kinship.filter;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.combobox.ComboBoxController;
import delta.common.ui.swing.combobox.ItemSelectionListener;
import delta.common.ui.swing.text.DynamicTextEditionController;
import delta.common.ui.swing.text.TextListener;
import delta.common.utils.collections.filters.Filter;
import delta.games.lotro.character.BaseCharacterSummary;
import delta.games.lotro.character.classes.ClassDescription;
import delta.games.lotro.character.filters.CharacterClassFilter;
import delta.games.lotro.character.filters.CharacterNameFilter;
import delta.games.lotro.character.filters.CharacterSexFilter;
import delta.games.lotro.character.filters.CharacterSummaryFilter;
import delta.games.lotro.character.filters.RaceFilter;
import delta.games.lotro.character.races.RaceDescription;
import delta.games.lotro.common.CharacterSex;
import delta.games.lotro.gui.character.summary.CharacterUiUtils;
import delta.games.lotro.gui.lore.items.FilterUpdateListener;

/**
 * Controller for a kinship member filter edition panel.
 * @author DAM
 */
public class CharacterSummaryFilterController
{
  // Data
  private CharacterSummaryFilter _filter;
  // GUI
  private JPanel _panel;
  // -- Character attributes UI --
  private JTextField _contains;
  private ComboBoxController<ClassDescription> _class;
  private ComboBoxController<RaceDescription> _race;
  private ComboBoxController<CharacterSex> _sex;
  // Controllers
  private DynamicTextEditionController _textController;
  private FilterUpdateListener _filterUpdateListener;

  /**
   * Constructor.
   * @param filter Managed filter.
   * @param filterUpdateListener Filter update listener.
   */
  public CharacterSummaryFilterController(CharacterSummaryFilter filter, FilterUpdateListener filterUpdateListener)
  {
    _filter=filter;
    _filterUpdateListener=filterUpdateListener;
  }

  /**
   * Get the managed filter.
   * @return the managed filter.
   */
  public Filter<BaseCharacterSummary> getFilter()
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

  /**
   * Reset all gadgets.
   */
  public void reset()
  {
    _class.selectItem(null);
    _race.selectItem(null);
    _sex.selectItem(null);
    _contains.setText("");
  }

  /**
   * Apply filter data into UI.
   */
  public void setFilter()
  {
    // Name
    CharacterNameFilter<BaseCharacterSummary> nameFilter=_filter.getNameFilter();
    String contains=nameFilter.getPattern();
    if (contains!=null)
    {
      _contains.setText(contains);
    }
    // Class
    CharacterClassFilter<BaseCharacterSummary> classFilter=_filter.getClassFilter();
    ClassDescription characterClass=classFilter.getCharacterClass();
    _class.selectItem(characterClass);
    // Race
    RaceFilter raceFilter=_filter.getRaceFilter();
    RaceDescription race=raceFilter.getRace();
    _race.selectItem(race);
    // Sex
    CharacterSexFilter sexFilter=_filter.getSexFilter();
    CharacterSex sex=sexFilter.getSex();
    _sex.selectItem(sex);
  }

  private JPanel build()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    int y=0;

    // Summary attributes
    JPanel summaryPanel=buildSummaryPanel();
    GridBagConstraints c=new GridBagConstraints(0,y,1,1,0.0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(summaryPanel,c);

    return panel;
  }

  private JPanel buildSummaryPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    int y=0;
    JPanel linePanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING,5,0));
    // Label filter
    {
      linePanel.add(GuiFactory.buildLabel("Name filter:")); // I18n
      _contains=GuiFactory.buildTextField("");
      _contains.setColumns(10);
      linePanel.add(_contains);
      TextListener listener=new TextListener()
      {
        @Override
        public void textChanged(String newText)
        {
          if (newText.length()==0) newText=null;
          CharacterNameFilter<BaseCharacterSummary> nameFilter=_filter.getNameFilter();
          nameFilter.setPattern(newText);
          filterUpdated();
        }
      };
      _textController=new DynamicTextEditionController(_contains,listener);
    }
    // Class
    {
      JLabel label=GuiFactory.buildLabel("Class:"); // I18n
      linePanel.add(label);
      _class=CharacterUiUtils.buildCharacterClassCombo(true);
      ItemSelectionListener<ClassDescription> classListener=new ItemSelectionListener<ClassDescription>()
      {
        @Override
        public void itemSelected(ClassDescription characterClass)
        {
          CharacterClassFilter<BaseCharacterSummary> classFilter=_filter.getClassFilter();
          classFilter.setCharacterClass(characterClass);
          filterUpdated();
        }
      };
      _class.addListener(classListener);
      linePanel.add(_class.getComboBox());
    }
    // Race
    {
      JLabel label=GuiFactory.buildLabel("Race:"); // I18n
      linePanel.add(label);
      _race=CharacterUiUtils.buildRaceCombo(true);
      ItemSelectionListener<RaceDescription> raceListener=new ItemSelectionListener<RaceDescription>()
      {
        @Override
        public void itemSelected(RaceDescription race)
        {
          RaceFilter raceFilter=_filter.getRaceFilter();
          raceFilter.setRace(race);
          filterUpdated();
        }
      };
      _race.addListener(raceListener);
      linePanel.add(_race.getComboBox());
    }
    // Sex
    {
      JLabel label=GuiFactory.buildLabel("Sex:"); // I18n
      linePanel.add(label);
      _sex=CharacterUiUtils.buildSexCombo(true);
      ItemSelectionListener<CharacterSex> sexListener=new ItemSelectionListener<CharacterSex>()
      {
        @Override
        public void itemSelected(CharacterSex sex)
        {
          CharacterSexFilter sexFilter=_filter.getSexFilter();
          sexFilter.setSex(sex);
          filterUpdated();
        }
      };
      _sex.addListener(sexListener);
      linePanel.add(_sex.getComboBox());
    }
    GridBagConstraints c=new GridBagConstraints(0,y,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,5,0),0,0);
    panel.add(linePanel,c);
    y++;
    return panel;
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
    if (_class!=null)
    {
      _class.dispose();
      _class=null;
    }
    if (_race!=null)
    {
      _race.dispose();
      _race=null;
    }
    if (_sex!=null)
    {
      _sex.dispose();
      _sex=null;
    }
    _contains=null;
  }
}
