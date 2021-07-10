package delta.games.lotro.gui.stats.achievables.filter;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.combobox.ComboBoxController;
import delta.common.ui.swing.combobox.ItemSelectionListener;
import delta.games.lotro.character.achievables.AchievableElementState;
import delta.games.lotro.character.achievables.filter.AchievableElementStateFilter;
import delta.games.lotro.character.achievables.filter.AchievableStatusFilter;
import delta.games.lotro.gui.items.FilterUpdateListener;

/**
 * Controller for a deed status filter edition panel.
 * @author DAM
 */
public class AchievableStatusFilterController implements ActionListener
{
  // Data
  private AchievableStatusFilter _filter;
  // GUI
  private JPanel _panel;
  private JButton _reset;
  // Controllers
  private ComboBoxController<AchievableElementState> _state;
  // Listeners
  private FilterUpdateListener _filterUpdateListener;

  /**
   * Constructor.
   * @param filter Managed filter.
   * @param filterUpdateListener Filter update listener.
   */
  public AchievableStatusFilterController(AchievableStatusFilter filter, FilterUpdateListener filterUpdateListener)
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
    _filterUpdateListener.filterUpdated();
  }

  @Override
  public void actionPerformed(ActionEvent e)
  {
    Object source=e.getSource();
    if (source==_reset)
    {
      _state.selectItem(null);
    }
  }

  private void setFilter()
  {
    // State
    AchievableElementStateFilter stateFilter=_filter.getStateFilter();
    AchievableElementState state=stateFilter.getState();
    _state.selectItem(state);
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
    c=new GridBagConstraints(1,y,1,1,0.0,0,GridBagConstraints.SOUTHWEST,GridBagConstraints.NONE,new Insets(0,5,5,0),0,0);
    panel.add(_reset,c);
    y++;

    return panel;
  }

  private JPanel buildStatusPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    int y=0;
    JPanel line1Panel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING,5,0));
    // Category
    {
      JLabel label=GuiFactory.buildLabel("State:");
      line1Panel.add(label);
      _state=buildStateCombobox();
      line1Panel.add(_state.getComboBox());
    }
    GridBagConstraints c=new GridBagConstraints(0,y,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,5,0),0,0);
    panel.add(line1Panel,c);
    y++;
    return panel;
  }

  private ComboBoxController<AchievableElementState> buildStateCombobox()
  {
    ComboBoxController<AchievableElementState> combo=new ComboBoxController<AchievableElementState>();
    combo.addEmptyItem("");
    for(AchievableElementState state : AchievableElementState.values())
    {
      String label=state.toString();
      combo.addItem(state,label);
    }
    combo.selectItem(null);
    ItemSelectionListener<AchievableElementState> listener=new ItemSelectionListener<AchievableElementState>()
    {
      @Override
      public void itemSelected(AchievableElementState state)
      {
        AchievableElementStateFilter stateFilter=_filter.getStateFilter();
        stateFilter.setState(state);
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
    // GUI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    // Controllers
    if (_state!=null)
    {
      _state.dispose();
      _state=null;
    }
    _reset=null;
  }
}
