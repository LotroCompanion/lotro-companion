package delta.games.lotro.gui.friends.filter;

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
import delta.games.lotro.account.status.friends.Friend;
import delta.games.lotro.account.status.friends.filters.FriendFilter;
import delta.games.lotro.character.classes.ClassDescription;
import delta.games.lotro.character.filters.CharacterClassFilter;
import delta.games.lotro.character.filters.CharacterNameFilter;
import delta.games.lotro.gui.character.summary.CharacterUiUtils;
import delta.games.lotro.gui.lore.items.FilterUpdateListener;

/**
 * Controller for a friend filter edition panel.
 * @author DAM
 */
public class FriendSummaryFilterController
{
  // Data
  private FriendFilter _filter;
  // GUI
  private JPanel _panel;
  // -- Character attributes UI --
  private JTextField _contains;
  private ComboBoxController<ClassDescription> _class;
  // Controllers
  private DynamicTextEditionController _textController;
  private FilterUpdateListener _filterUpdateListener;

  /**
   * Constructor.
   * @param filter Managed filter.
   * @param filterUpdateListener Filter update listener.
   */
  public FriendSummaryFilterController(FriendFilter filter, FilterUpdateListener filterUpdateListener)
  {
    _filter=filter;
    _filterUpdateListener=filterUpdateListener;
  }

  /**
   * Get the managed filter.
   * @return the managed filter.
   */
  public Filter<Friend> getFilter()
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
    _contains.setText("");
  }

  /**
   * Apply filter data into UI.
   */
  public void setFilter()
  {
    // Name
    CharacterNameFilter<Friend> nameFilter=_filter.getNameFilter();
    String contains=nameFilter.getPattern();
    if (contains!=null)
    {
      _contains.setText(contains);
    }
    // Class
    CharacterClassFilter<Friend> classFilter=_filter.getClassFilter();
    ClassDescription characterClass=classFilter.getCharacterClass();
    _class.selectItem(characterClass);
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
          CharacterNameFilter<Friend> nameFilter=_filter.getNameFilter();
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
          CharacterClassFilter<Friend> classFilter=_filter.getClassFilter();
          classFilter.setCharacterClass(characterClass);
          filterUpdated();
        }
      };
      _class.addListener(classListener);
      linePanel.add(_class.getComboBox());
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
    _contains=null;
  }
}
