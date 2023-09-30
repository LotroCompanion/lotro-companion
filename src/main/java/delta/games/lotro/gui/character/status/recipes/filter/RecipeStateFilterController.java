package delta.games.lotro.gui.character.status.recipes.filter;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.combobox.ItemSelectionListener;
import delta.common.ui.swing.multicheckbox.MultiCheckboxController;
import delta.games.lotro.character.status.recipes.RecipeState;
import delta.games.lotro.character.status.recipes.filter.RecipeStateFilter;
import delta.games.lotro.gui.lore.items.FilterUpdateListener;
import delta.games.lotro.gui.utils.l10n.Labels;

/**
 * Controller for an edition panel for an achievable status filter.
 * @author DAM
 */
public class RecipeStateFilterController implements ActionListener
{
  // Data
  private RecipeStateFilter _filter;
  // GUI
  private JPanel _panel;
  private JButton _reset;
  // Controllers
  private MultiCheckboxController<RecipeState> _states;
  // Listeners
  private FilterUpdateListener _filterUpdateListener;

  /**
   * Constructor.
   * @param filter Managed filter.
   * @param filterUpdateListener Filter update listener.
   */
  public RecipeStateFilterController(RecipeStateFilter filter, FilterUpdateListener filterUpdateListener)
  {
    _filter=filter;
    _filterUpdateListener=filterUpdateListener;
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
    _filter.setStates(new HashSet<RecipeState>(_states.getSelectedItems()));
    _filterUpdateListener.filterUpdated();
  }

  @Override
  public void actionPerformed(ActionEvent e)
  {
    Object source=e.getSource();
    if (source==_reset)
    {
      _states.selectAll();
    }
  }

  private void setFilter()
  {
    // State
    Set<RecipeState> states=_filter.getSelectedStates();
    _states.setSelectedItems(states);
  }

  private JPanel build()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    int y=0;

    // Status attributes
    JPanel statusPanel=buildStatusPanel();
    GridBagConstraints c=new GridBagConstraints(0,y,1,1,0.0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(statusPanel,c);

    // Reset
    _reset=GuiFactory.buildButton(Labels.getLabel("shared.reset"));
    _reset.addActionListener(this);
    c=new GridBagConstraints(1,y,1,1,0.0,0,GridBagConstraints.SOUTHWEST,GridBagConstraints.NONE,new Insets(0,0,5,5),0,0);
    panel.add(_reset,c);
    y++;

    return panel;
  }

  private JPanel buildStatusPanel()
  {
    _states=buildStateMultiCheckbox();
    return _states.getPanel();
  }

  private MultiCheckboxController<RecipeState> buildStateMultiCheckbox()
  {
    final MultiCheckboxController<RecipeState> multiCheckbox=new MultiCheckboxController<RecipeState>();
    for(RecipeState state : RecipeState.values())
    {
      String label=state.toString();
      multiCheckbox.addItem(state,label);
    }
    multiCheckbox.selectAll();
    ItemSelectionListener<RecipeState> listener=new ItemSelectionListener<RecipeState>()
    {
      @Override
      public void itemSelected(RecipeState state)
      {
        Set<RecipeState> states=new HashSet<RecipeState>(multiCheckbox.getItems());
        _filter.setStates(states);
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
    if (_states!=null)
    {
      _states.dispose();
      _states=null;
    }
    _reset=null;
  }
}
