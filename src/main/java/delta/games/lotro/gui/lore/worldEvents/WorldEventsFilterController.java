package delta.games.lotro.gui.lore.worldEvents;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.combobox.ComboBoxController;
import delta.common.ui.swing.combobox.ItemSelectionListener;
import delta.games.lotro.gui.lore.items.FilterUpdateListener;
import delta.games.lotro.lore.quests.Achievable;
import delta.games.lotro.lore.worldEvents.WorldEventConditionsUtils;
import delta.games.lotro.lore.worldEvents.filter.WorldEventConditionFilter;

/**
 * Controller for a world events filter edition panel.
 * @author DAM
 */
public class WorldEventsFilterController
{
  // Data
  private List<? extends Achievable> _achievables;
  private WorldEventConditionFilter _filter;
  private FilterUpdateListener _filterUpdateListener;

  // GUI
  private JPanel _panel;

  // Controllers
  private ComboBoxController<String> _contexts;

  /**
   * Constructor.
   * @param achievables Achievables to use.
   * @param filter Managed filter.
   * @param filterUpdateListener Filter update listener.
   */
  public WorldEventsFilterController(List<? extends Achievable> achievables, WorldEventConditionFilter filter, FilterUpdateListener filterUpdateListener)
  {
    _achievables=achievables;
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
      _panel=buildManagedPanel();
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
    _contexts.selectItem(null);
  }

  /**
   * Apply current filter into the managed gadgets.
   */
  public void setFilter()
  {
    String context=_filter.getLabel();
    _contexts.selectItem(context);
  }

  private JPanel buildManagedPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    int y=0;

    GridBagConstraints c;
    {
      JPanel linePanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING,5,0));
      _contexts=buildContextCombobox();
      linePanel.add(_contexts.getComboBox());
      c=new GridBagConstraints(0,y,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(5,0,5,0),0,0);
      panel.add(linePanel,c);
      y++;
    }

    return panel;
  }

  private ComboBoxController<String> buildContextCombobox()
  {
    List<String> contexts=WorldEventConditionsUtils.getLabels(_achievables);
    ComboBoxController<String> combo=new ComboBoxController<String>();
    combo.addEmptyItem("");
    for(String context : contexts)
    {
      combo.addItem(context,context);
    }
    ItemSelectionListener<String> listener=new ItemSelectionListener<String>()
    {
      @Override
      public void itemSelected(String context)
      {
        _filter.setLabel(context);
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
    _achievables=null;
    _filter=null;
    // Controllers
    if (_contexts!=null)
    {
      _contexts.dispose();
      _contexts=null;
    }
    // GUI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
  }
}
