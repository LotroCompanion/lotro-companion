package delta.games.lotro.gui.lore.webStoreItems;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.combobox.ComboBoxController;
import delta.common.ui.swing.combobox.ItemSelectionListener;
import delta.games.lotro.common.comparators.NamedComparator;
import delta.games.lotro.gui.lore.items.FilterUpdateListener;
import delta.games.lotro.lore.quests.Achievable;
import delta.games.lotro.lore.quests.filter.WebStoreItemFilter;
import delta.games.lotro.lore.webStore.WebStoreItem;

/**
 * Controller for a web store items filter edition panel.
 * @param <T> Type of achievables.
 * @author DAM
 */
public class WebStoreItemsFilterController<T extends Achievable>
{
  // Data
  private List<T> _achievables;
  private WebStoreItemFilter<T> _filter;
  private FilterUpdateListener _filterUpdateListener;

  // GUI
  private JPanel _panel;

  // Controllers
  private ComboBoxController<WebStoreItem> _contentPacks;

  /**
   * Constructor.
   * @param achievables Achievables to use.
   * @param filter Managed filter.
   * @param filterUpdateListener Filter update listener.
   */
  public WebStoreItemsFilterController(List<T> achievables, WebStoreItemFilter<T> filter, FilterUpdateListener filterUpdateListener)
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
    _contentPacks.selectItem(null);
  }

  /**
   * Apply current filter into the managed gadgets.
   */
  public void setFilter()
  {
    WebStoreItem context=_filter.getContentsPack();
    _contentPacks.selectItem(context);
  }

  private JPanel buildManagedPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    int y=0;

    GridBagConstraints c;
    {
      JPanel linePanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING,5,0));
      _contentPacks=buildContextCombobox();
      linePanel.add(_contentPacks.getComboBox());
      c=new GridBagConstraints(0,y,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(5,0,5,0),0,0);
      panel.add(linePanel,c);
      y++;
    }

    return panel;
  }

  private ComboBoxController<WebStoreItem> buildContextCombobox()
  {
    ComboBoxController<WebStoreItem> combo=buildCombo(_achievables);
    ItemSelectionListener<WebStoreItem> listener=new ItemSelectionListener<WebStoreItem>()
    {
      @Override
      public void itemSelected(WebStoreItem item)
      {
        _filter.setContentsPack(item);
        filterUpdated();
      }
    };
    combo.addListener(listener);
    return combo;
  }

  /**
   * Build a combo-box controller to choose a web store item.
   * @param achievables Achievables to use.
   * @return A new combo-box controller.
   */
  private ComboBoxController<WebStoreItem> buildCombo(List<T> achievables)
  {
    ComboBoxController<WebStoreItem> ctrl=new ComboBoxController<WebStoreItem>();
    ctrl.addEmptyItem("");
    Set<WebStoreItem> items=new HashSet<WebStoreItem>();
    for(T achievable : achievables)
    {
      WebStoreItem webStoreItem=achievable.getWebStoreItem();
      if (webStoreItem!=null)
      {
        items.add(webStoreItem);
      }
    }
    List<WebStoreItem> sortedList=new ArrayList<WebStoreItem>(items);
    Collections.sort(sortedList,new NamedComparator());
    for(WebStoreItem webStoreItem : sortedList)
    {
      ctrl.addItem(webStoreItem,webStoreItem.getName());
    }
    ctrl.selectItem(null);
    return ctrl;
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
    if (_contentPacks!=null)
    {
      _contentPacks.dispose();
      _contentPacks=null;
    }
    // GUI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
  }
}
