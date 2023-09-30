package delta.games.lotro.gui.character.storage.wardrobe;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.combobox.ComboBoxController;
import delta.common.ui.swing.combobox.ItemSelectionListener;
import delta.common.ui.swing.text.DynamicTextEditionController;
import delta.common.ui.swing.text.TextListener;
import delta.common.utils.collections.filters.Filter;
import delta.games.lotro.character.storage.wardrobe.WardrobeItem;
import delta.games.lotro.common.enums.ItemClass;
import delta.games.lotro.common.filters.NamedFilter;
import delta.games.lotro.gui.lore.items.FilterUpdateListener;
import delta.games.lotro.gui.utils.l10n.Labels;
import delta.games.lotro.lore.items.EquipmentLocation;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.filters.ItemClassFilter;
import delta.games.lotro.lore.items.filters.ItemSlotFilter;

/**
 * Controller for a wardrobe filter edition panel.
 * @author DAM
 */
public class WardrobeFilterController implements ActionListener
{
  // Data
  private WardrobeFilter _filter;
  // GUI
  private JPanel _panel;
  private JButton _reset;
  // -- Filter UI --
  private JTextField _contains;
  private ComboBoxController<EquipmentLocation> _slot;
  private ComboBoxController<ItemClass> _category;
  // Controllers
  private DynamicTextEditionController _textController;
  private FilterUpdateListener _filterUpdateListener;

  /**
   * Constructor.
   * @param filter Managed filter.
   * @param filterUpdateListener Filter update listener.
   */
  public WardrobeFilterController(WardrobeFilter filter, FilterUpdateListener filterUpdateListener)
  {
    _filter=filter;
    _filterUpdateListener=filterUpdateListener;
  }

  /**
   * Get the managed filter.
   * @return the managed filter.
   */
  public Filter<WardrobeItem> getFilter()
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
   * Update the filter UI (for new items).
   */
  public void update()
  {
    // Slot
    EquipmentLocation location=_slot.getSelectedItem();
    updateSlotCombobox(_slot);
    _slot.selectItem(location);
    // Category
    ItemClass category=_category.getSelectedItem();
    updateCategoryCombobox(_category);
    _category.selectItem(category);
  }

  /**
   * Invoked when the managed filter has been updated.
   */
  protected void filterUpdated()
  {
    _filterUpdateListener.filterUpdated();
  }

  @Override
  public void actionPerformed(ActionEvent e)
  {
    Object source=e.getSource();
    if (source==_reset)
    {
      _slot.selectItem(null);
      _category.selectItem(null);
      _contains.setText("");
    }
  }

  private void setFilter()
  {
    // Name
    NamedFilter<Item> nameFilter=_filter.getNameFilter();
    String contains=nameFilter.getPattern();
    if (contains!=null)
    {
      _contains.setText(contains);
    }
    // Slot
    EquipmentLocation location=_filter.getSlotFilter().getLocation();
    _slot.selectItem(location);
    // Category
    ItemClass itemClass=_filter.getCategoryFilter().getItemClass();
    _category.selectItem(itemClass);
  }

  private JPanel build()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    int y=0;

    // Filter
    JPanel filter=buildFilterPanel();
    GridBagConstraints c=new GridBagConstraints(0,y,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(filter,c);

    // Reset
    _reset=GuiFactory.buildButton(Labels.getLabel("shared.reset"));
    _reset.addActionListener(this);
    c=new GridBagConstraints(1,y,1,1,0.0,0,GridBagConstraints.SOUTHWEST,GridBagConstraints.NONE,new Insets(0,5,5,5),0,0);
    panel.add(_reset,c);
    y++;

    return panel;
  }

  private JPanel buildFilterPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    int y=0;
    JPanel line1Panel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING,0,0));
    // Label filter
    {
      JPanel containsPanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING,5,0));
      containsPanel.add(GuiFactory.buildLabel("Name:")); // I18n
      _contains=GuiFactory.buildTextField("");
      _contains.setColumns(10);
      containsPanel.add(_contains);
      TextListener listener=new TextListener()
      {
        @Override
        public void textChanged(String newText)
        {
          if (newText.length()==0) newText=null;
          NamedFilter<Item> nameFilter=_filter.getNameFilter();
          nameFilter.setPattern(newText);
          filterUpdated();
        }
      };
      _textController=new DynamicTextEditionController(_contains,listener);
      line1Panel.add(containsPanel);
    }
    // Category
    {
      JPanel categoryPanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING));
      categoryPanel.add(GuiFactory.buildLabel("Category:")); // I18n
      _category=buildCategoryCombobox();
      categoryPanel.add(_category.getComboBox());
      line1Panel.add(categoryPanel);
    }
    // Location
    {
      JPanel slotPanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING));
      slotPanel.add(GuiFactory.buildLabel("Slot:")); // I18n
      _slot=buildSlotCombobox();
      slotPanel.add(_slot.getComboBox());
      line1Panel.add(slotPanel);
    }
    GridBagConstraints c=new GridBagConstraints(0,y,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(line1Panel,c);
    y++;

    return panel;
  }

  private ComboBoxController<EquipmentLocation> buildSlotCombobox()
  {
    ComboBoxController<EquipmentLocation> ctrl=new ComboBoxController<EquipmentLocation>();
    ctrl.addEmptyItem("");
    for(EquipmentLocation slot : EquipmentLocation.getAll())
    {
      ctrl.addItem(slot,slot.getLabel());
    }
    updateSlotCombobox(ctrl);
    ctrl.selectItem(null);
    ItemSelectionListener<EquipmentLocation> ownerListener=new ItemSelectionListener<EquipmentLocation>()
    {
      @Override
      public void itemSelected(EquipmentLocation slot)
      {
        ItemSlotFilter slotFilter=_filter.getSlotFilter();
        slotFilter.setLocation(slot);
        filterUpdated();
      }
    };
    ctrl.addListener(ownerListener);
    return ctrl;
  }

  private void updateSlotCombobox(ComboBoxController<EquipmentLocation> ctrl)
  {
    ctrl.removeAllItems();
    ctrl.addEmptyItem("");
    List<EquipmentLocation> slots=_filter.getConfiguration().getSlots();
    for(EquipmentLocation slot : slots)
    {
      ctrl.addItem(slot,slot.getLabel());
    }
  }

  private ComboBoxController<ItemClass> buildCategoryCombobox()
  {
    ComboBoxController<ItemClass> ctrl=new ComboBoxController<ItemClass>();
    updateCategoryCombobox(ctrl);
    ctrl.selectItem(null);
    ItemSelectionListener<ItemClass> categoryListener=new ItemSelectionListener<ItemClass>()
    {
      @Override
      public void itemSelected(ItemClass category)
      {
        ItemClassFilter categoryFilter=_filter.getCategoryFilter();
        categoryFilter.setItemClass(category);
        filterUpdated();
      }
    };
    ctrl.addListener(categoryListener);
    return ctrl;
  }

  private void updateCategoryCombobox(ComboBoxController<ItemClass> ctrl)
  {
    ctrl.removeAllItems();
    ctrl.addEmptyItem("");
    List<ItemClass> categories=_filter.getConfiguration().getCategories();
    for(ItemClass category : categories)
    {
      ctrl.addItem(category,category.getLabel());
    }
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Data
    _filter=null;
    // Controllers
    if (_textController!=null)
    {
      _textController.dispose();
      _textController=null;
    }
    if (_slot!=null)
    {
      _slot.dispose();
      _slot=null;
    }
    if (_category!=null)
    {
      _category.dispose();
      _category=null;
    }
    // GUI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    _contains=null;
    _reset=null;
  }
}
