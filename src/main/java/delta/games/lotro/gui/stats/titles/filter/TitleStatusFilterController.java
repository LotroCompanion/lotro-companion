package delta.games.lotro.gui.stats.titles.filter;

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
import delta.games.lotro.character.titles.filter.TitleStateFilter;
import delta.games.lotro.character.titles.filter.TitleStatusFilter;
import delta.games.lotro.gui.items.FilterUpdateListener;

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
  private MultiCheckboxController<Boolean> _states;
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
    _filter.getStateFilter().setStates(new HashSet<Boolean>(_states.getSelectedItems()));
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
    Set<Boolean> states=stateFilter.getSelectedStates();
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
    _reset=GuiFactory.buildButton("Reset");
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

  private MultiCheckboxController<Boolean> buildStateMultiCheckbox()
  {
    final MultiCheckboxController<Boolean> multiCheckbox=new MultiCheckboxController<Boolean>();
    multiCheckbox.addItem(Boolean.TRUE,"Acquired");
    multiCheckbox.addItem(Boolean.FALSE,"Not acquired");
    multiCheckbox.selectAll();
    ItemSelectionListener<Boolean> listener=new ItemSelectionListener<Boolean>()
    {
      @Override
      public void itemSelected(Boolean state)
      {
        Set<Boolean> states=new HashSet<Boolean>(multiCheckbox.getItems());
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
