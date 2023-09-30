package delta.games.lotro.gui.character.status.titles.filter;

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
import delta.games.lotro.character.status.titles.TitleState;
import delta.games.lotro.character.status.titles.filter.TitleStateFilter;
import delta.games.lotro.character.status.titles.filter.TitleStatusFilter;
import delta.games.lotro.gui.lore.items.FilterUpdateListener;
import delta.games.lotro.gui.utils.l10n.Labels;

/**
 * Controller for an edition panel for a title status filter.
 * @author DAM
 */
public class TitleStatusFilterController implements ActionListener
{
  // Data
  private TitleStatusFilter _filter;
  // GUI
  private JPanel _panel;
  private JButton _reset;
  // Controllers
  private MultiCheckboxController<TitleState> _states;
  // Listeners
  private FilterUpdateListener _filterUpdateListener;

  /**
   * Constructor.
   * @param filter Managed filter.
   * @param filterUpdateListener Filter update listener.
   */
  public TitleStatusFilterController(TitleStatusFilter filter, FilterUpdateListener filterUpdateListener)
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
    _filter.getStateFilter().setStates(new HashSet<TitleState>(_states.getSelectedItems()));
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
    TitleStateFilter stateFilter=_filter.getStateFilter();
    Set<TitleState> states=stateFilter.getSelectedStates();
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
    c=new GridBagConstraints(1,y,1,1,0.0,0.0,GridBagConstraints.SOUTHWEST,GridBagConstraints.NONE,new Insets(0,0,5,5),0,0);
    panel.add(_reset,c);
    y++;

    return panel;
  }

  private JPanel buildStatusPanel()
  {
    _states=buildStateMultiCheckbox();
    return _states.getPanel();
  }

  private MultiCheckboxController<TitleState> buildStateMultiCheckbox()
  {
    final MultiCheckboxController<TitleState> multiCheckbox=new MultiCheckboxController<TitleState>();
    multiCheckbox.addItem(TitleState.ACQUIRED,"Acquired"); // I18n
    multiCheckbox.addItem(TitleState.SUPERSEDED,"Superseded"); // I18n
    multiCheckbox.addItem(TitleState.UNDEFINED,"Not acquired"); // I18n
    Set<TitleState> selectedStates=new HashSet<TitleState>();
    selectedStates.add(TitleState.ACQUIRED);
    selectedStates.add(TitleState.SUPERSEDED);
    multiCheckbox.setSelectedItems(selectedStates);
    ItemSelectionListener<TitleState> listener=new ItemSelectionListener<TitleState>()
    {
      @Override
      public void itemSelected(TitleState state)
      {
        Set<TitleState> states=new HashSet<TitleState>(multiCheckbox.getItems());
        TitleStateFilter stateFilter=_filter.getStateFilter();
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
    if (_states!=null)
    {
      _states.dispose();
      _states=null;
    }
    _reset=null;
  }
}
