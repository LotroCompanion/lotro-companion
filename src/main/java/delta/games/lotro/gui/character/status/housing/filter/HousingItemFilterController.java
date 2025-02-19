package delta.games.lotro.gui.character.status.housing.filter;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.combobox.ComboBoxController;
import delta.common.ui.swing.combobox.ItemSelectionListener;
import delta.common.ui.swing.tables.panel.FilterUpdateListener;
import delta.common.ui.swing.text.DynamicTextEditionController;
import delta.common.ui.swing.text.TextListener;
import delta.common.utils.collections.filters.Filter;
import delta.games.lotro.character.status.housing.HousingItem;
import delta.games.lotro.character.status.housing.filter.HousingItemFilter;
import delta.games.lotro.character.status.housing.filter.HousingItemHookFilter;
import delta.games.lotro.common.enums.HousingHookID;
import delta.games.lotro.common.enums.ItemClass;
import delta.games.lotro.gui.lore.items.ItemUiTools;
import delta.games.lotro.gui.utils.l10n.Labels;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.filters.ItemClassFilter;
import delta.games.lotro.lore.items.filters.ItemNameFilter;

/**
 * Controller for a housing item filter edition panel.
 * @author DAM
 */
public class HousingItemFilterController implements ActionListener
{
  // Data
  private List<HousingItem> _items;
  private HousingItemFilter _filter;
  // GUI
  private JPanel _panel;
  private JButton _reset;
  private JTextField _contains;
  // Controllers
  private DynamicTextEditionController _textController;
  private ComboBoxController<ItemClass> _category;
  private ComboBoxController<String> _hook;
  // Listeners
  private FilterUpdateListener _filterUpdateListener;

  /**
   * Constructor.
   * @param filter Managed filter.
   * @param items Items to use.
   * @param filterUpdateListener Filter update listener.
   */
  public HousingItemFilterController(HousingItemFilter filter, List<HousingItem> items, FilterUpdateListener filterUpdateListener)
  {
    _filter=filter;
    _items=items;
    _filterUpdateListener=filterUpdateListener;
  }

  /**
   * Get the managed filter.
   * @return the managed filter.
   */
  public Filter<HousingItem> getFilter()
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
      reset();
    }
  }

  /**
   * Reset all gadgets.
   */
  private void reset()
  {
    _contains.setText("");
    _category.selectItem(null);
    _hook.selectItem(null);
  }

  private void setFilter()
  {
    // Item
    // - name
    ItemNameFilter nameFilter=_filter.getNameFilter();
    String contains=nameFilter.getPattern();
    if (contains!=null)
    {
      _contains.setText(contains);
    }
    // - category
    ItemClassFilter categoryFilter=_filter.getCategoryFilter();
    ItemClass category=categoryFilter.getItemClass();
    _category.selectItem(category);
    // Hook
    HousingItemHookFilter hookFilter=_filter.getHookFilter();
    String hook=hookFilter.getHook();
    _hook.selectItem(hook);
  }

  private JPanel build()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    int y=0;

    // Filter
    JPanel memberPanel=buildMemberPanel();
    Border memberBorder=GuiFactory.buildTitledBorder("Item"); // I18n
    memberPanel.setBorder(memberBorder);
    GridBagConstraints c=new GridBagConstraints(0,y,1,1,0.0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(memberPanel,c);

    // Reset
    _reset=GuiFactory.buildButton(Labels.getLabel("shared.reset"));
    _reset.addActionListener(this);
    c=new GridBagConstraints(1,y,1,1,1.0,0,GridBagConstraints.SOUTHEAST,GridBagConstraints.NONE,new Insets(0,5,5,5),0,0);
    panel.add(_reset,c);

    return panel;
  }

  private JPanel buildMemberPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    int y=0;
    JPanel linePanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING,5,0));
    // Name filter
    {
      linePanel.add(GuiFactory.buildLabel("Name:")); // I18n
      _contains=GuiFactory.buildTextField("");
      _contains.setColumns(10);
      linePanel.add(_contains);
      TextListener listener=new TextListener()
      {
        @Override
        public void textChanged(String newText)
        {
          if (newText.isEmpty()) newText=null;
          ItemNameFilter nameFilter=_filter.getNameFilter();
          nameFilter.setPattern(newText);
          filterUpdated();
        }
      };
      _textController=new DynamicTextEditionController(_contains,listener);
    }
    // Category filter
    {
      JLabel label=GuiFactory.buildLabel("Category:"); // I18n
      linePanel.add(label);
      _category=buildCategoryCombo();
      ItemSelectionListener<ItemClass> itemClassListener=new ItemSelectionListener<ItemClass>()
      {
        @Override
        public void itemSelected(ItemClass category)
        {
          _filter.getCategoryFilter().setItemClass(category);
          filterUpdated();
        }
      };
      _category.addListener(itemClassListener);
      linePanel.add(_category.getComboBox());
    }
    // Hook filter
    {
      JLabel label=GuiFactory.buildLabel("Hook:"); // I18n
      linePanel.add(label);
      _hook=buildHookCombo();
      ItemSelectionListener<String> listener=new ItemSelectionListener<String>()
      {
        @Override
        public void itemSelected(String hook)
        {
          HousingItemHookFilter hookFilter=_filter.getHookFilter();
          hookFilter.setHook(hook);
          filterUpdated();
        }
      };
      _hook.addListener(listener);
      linePanel.add(_hook.getComboBox());
    }
    GridBagConstraints c=new GridBagConstraints(0,y,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,5,0),0,0);
    panel.add(linePanel,c);
    return panel;
  }

  private ComboBoxController<ItemClass> buildCategoryCombo()
  {
    List<Item> items=new ArrayList<Item>();
    for(HousingItem housingItem : _items)
    {
      Item item=housingItem.getItem();
      items.add(item);
    }
    return ItemUiTools.buildCategoryCombo(items);
  }

  /**
   * Build a combo-box controller to choose a hook.
   * @return A new combo-box controller.
   */
  private ComboBoxController<String> buildHookCombo()
  {
    ComboBoxController<String> ctrl=new ComboBoxController<String>();
    ctrl.addEmptyItem("");
    for(String hook : buildHooks())
    {
      ctrl.addItem(hook,hook);
    }
    ctrl.selectItem(null);
    return ctrl;
  }

  private List<String> buildHooks()
  {
    Set<String> hooks=new HashSet<String>();
    for(HousingItem item : _items)
    {
      HousingHookID hookID=item.getHookID();
      if (hookID!=null)
      {
        String hookLabel=hookID.getLabel();
        if ((hookLabel!=null) && (!hookLabel.isEmpty()))
        {
          hooks.add(hookLabel);
        }
      }
    }
    List<String> ret=new ArrayList<String>(hooks);
    Collections.sort(ret);
    return ret;
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
    _reset=null;
    _contains=null;
    // Controllers
    if (_textController!=null)
    {
      _textController.dispose();
      _textController=null;
    }
    if (_category!=null)
    {
      _category.dispose();
      _category=null;
    }
    if (_hook!=null)
    {
      _hook.dispose();
      _hook=null;
    }
    // Listeners
    _filterUpdateListener=null;
  }
}
