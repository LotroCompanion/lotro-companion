package delta.games.lotro.gui.character.status.collections.filter;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.combobox.ItemSelectionListener;
import delta.common.ui.swing.multicheckbox.MultiCheckboxController;
import delta.common.ui.swing.text.DynamicTextEditionController;
import delta.common.ui.swing.text.TextListener;
import delta.common.utils.collections.filters.Filter;
import delta.games.lotro.character.status.collections.CollectionState;
import delta.games.lotro.character.status.collections.CollectionStatus;
import delta.games.lotro.character.status.collections.filters.CollectionStateFilter;
import delta.games.lotro.character.status.collections.filters.CollectionsStatusFilter;
import delta.games.lotro.common.filters.NamedFilter;
import delta.games.lotro.gui.lore.items.FilterUpdateListener;
import delta.games.lotro.lore.collections.CollectionDescription;

/**
 * Controller for a skill status filter edition panel.
 * @author DAM
 */
public class CollectionStatusFilterController
{
  // Data
  private CollectionsStatusFilter _filter;
  // GUI
  private JPanel _panel;
  // -- Filter UI --
  private JTextField _contains;
  // Controllers
  private MultiCheckboxController<CollectionState> _states;
  private DynamicTextEditionController _textController;
  private FilterUpdateListener _filterUpdateListener;

  /**
   * Constructor.
   * @param filter Managed filter.
   * @param filterUpdateListener Filter update listener.
   */
  public CollectionStatusFilterController(CollectionsStatusFilter filter, FilterUpdateListener filterUpdateListener)
  {
    _filter=filter;
    _filterUpdateListener=filterUpdateListener;
  }

  /**
   * Get the managed filter.
   * @return the managed filter.
   */
  public Filter<CollectionStatus> getFilter()
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
    _states.selectAll();
    _contains.setText("");
  }

  /**
   * Apply filter data into UI.
   */
  public void setFilter()
  {
    // Name
    NamedFilter<CollectionDescription> nameFilter=_filter.getNameFilter();
    String contains=nameFilter.getPattern();
    if (contains!=null)
    {
      _contains.setText(contains);
    }
    // State
    CollectionStateFilter stateFilter=_filter.getStateFilter();
    _states.setSelectedItems(stateFilter.getSelectedStates());
  }

  private JPanel build()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    int y=0;
    // Label filter
    {
      JPanel linePanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING,5,0));
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
          NamedFilter<CollectionDescription> nameFilter=_filter.getNameFilter();
          nameFilter.setPattern(newText);
          filterUpdated();
        }
      };
      _textController=new DynamicTextEditionController(_contains,listener);
      GridBagConstraints c=new GridBagConstraints(0,y,1,1,0.0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
      panel.add(linePanel,c);
      y++;
    }
    // State
    {
      JPanel linePanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING,5,0));
      JLabel label=GuiFactory.buildLabel("State:"); // I18n
      linePanel.add(label);
      _states=buildStateMultiCheckbox();
      linePanel.add(_states.getPanel());
      GridBagConstraints c=new GridBagConstraints(0,y,1,1,0.0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
      panel.add(linePanel,c);
      y++;
    }
    return panel;
  }

  private MultiCheckboxController<CollectionState> buildStateMultiCheckbox()
  {
    final MultiCheckboxController<CollectionState> multiCheckbox=new MultiCheckboxController<CollectionState>();
    for(CollectionState state : CollectionState.values())
    {
      String label=state.toString();
      multiCheckbox.addItem(state,label);
    }
    multiCheckbox.selectAll();
    ItemSelectionListener<CollectionState> listener=new ItemSelectionListener<CollectionState>()
    {
      @Override
      public void itemSelected(CollectionState state)
      {
        Set<CollectionState> states=new HashSet<CollectionState>(_states.getSelectedItems());
        CollectionStateFilter stateFilter=_filter.getStateFilter();
        stateFilter.setStates(states);
        filterUpdated();
      }
    };
    multiCheckbox.addListener(listener);
    return multiCheckbox;
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
    if (_states!=null)
    {
      _states.dispose();
      _states=null;
    }
    _contains=null;
  }
}
