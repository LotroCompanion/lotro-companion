package delta.games.lotro.gui.character.storage;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.combobox.ComboBoxController;
import delta.common.ui.swing.combobox.ItemSelectionListener;
import delta.common.ui.swing.text.DynamicTextEditionController;
import delta.common.ui.swing.text.TextListener;
import delta.common.utils.collections.filters.Filter;
import delta.games.lotro.character.storage.StoredItem;
import delta.games.lotro.character.storage.filters.StoredItemLocationFilter;
import delta.games.lotro.character.storage.filters.StoredItemOwnerFilter;
import delta.games.lotro.character.storage.location.StorageLocation;
import delta.games.lotro.common.filters.NamedFilter;
import delta.games.lotro.common.owner.AccountServerOwner;
import delta.games.lotro.common.owner.CharacterOwner;
import delta.games.lotro.common.owner.Owner;
import delta.games.lotro.gui.items.FilterUpdateListener;
import delta.games.lotro.gui.items.ItemUiTools;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemQuality;

/**
 * Controller for a storage filter edition panel.
 * @author DAM
 */
public class StorageFilterController implements ActionListener
{
  // Data
  private StorageFilter _filter;
  // GUI
  private JPanel _panel;
  private JButton _reset;
  // -- Filter UI --
  private JTextField _contains;
  private ComboBoxController<Owner> _owner;
  private ComboBoxController<StorageLocation> _location;
  private ComboBoxController<ItemQuality> _quality;
  // Controllers
  private DynamicTextEditionController _textController;
  private FilterUpdateListener _filterUpdateListener;

  /**
   * Constructor.
   * @param filter Managed filter.
   * @param filterUpdateListener Filter update listener.
   */
  public StorageFilterController(StorageFilter filter, FilterUpdateListener filterUpdateListener)
  {
    _filter=filter;
    _filterUpdateListener=filterUpdateListener;
  }

  /**
   * Get the managed filter.
   * @return the managed filter.
   */
  public Filter<StoredItem> getFilter()
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
    // Owner
    Owner owner=_owner.getSelectedItem();
    updateOwnerCombobox(_owner);
    _owner.selectItem(owner);
    // Location
    StorageLocation location=_location.getSelectedItem();
    updateLocationCombobox(_location);
    _location.selectItem(location);
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
      _owner.selectItem(null);
      _location.selectItem(null);
      _quality.selectItem(null);
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
    // Owner
    StoredItemOwnerFilter ownerFilter=_filter.getOwnerFilter();
    Owner owner=ownerFilter.getOwner();
    _owner.selectItem(owner);
    // Location
    StoredItemLocationFilter locationFilter=_filter.getLocationFilter();
    StorageLocation location=locationFilter.getLocation();
    _location.selectItem(location);
    // Quality
    ItemQuality quality=_filter.getQualityFilter().getQuality();
    _quality.selectItem(quality);
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
    _reset=GuiFactory.buildButton("Reset");
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
      containsPanel.add(GuiFactory.buildLabel("Name filter:"));
      _contains=GuiFactory.buildTextField("");
      _contains.setColumns(20);
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
    GridBagConstraints c=new GridBagConstraints(0,y,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,5,0),0,0);
    panel.add(line1Panel,c);
    y++;

    JPanel line2Panel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING,5,0));
    // Owner
    {
      JLabel label=GuiFactory.buildLabel("Owner:");
      line2Panel.add(label);
      _owner=buildOwnerCombobox();
      ItemSelectionListener<Owner> ownerListener=new ItemSelectionListener<Owner>()
      {
        @Override
        public void itemSelected(Owner owner)
        {
          StoredItemOwnerFilter ownerFilter=_filter.getOwnerFilter();
          ownerFilter.setOwner(owner);
          filterUpdated();
        }
      };
      _owner.addListener(ownerListener);
      line2Panel.add(_owner.getComboBox());
    }
    // Location
    {
      JLabel label=GuiFactory.buildLabel("Location:");
      line2Panel.add(label);
      _location=buildLocationCombobox();
      ItemSelectionListener<StorageLocation> categoryListener=new ItemSelectionListener<StorageLocation>()
      {
        @Override
        public void itemSelected(StorageLocation location)
        {
          StoredItemLocationFilter locationFilter=_filter.getLocationFilter();
          locationFilter.setLocation(location);
          filterUpdated();
        }
      };
      _location.addListener(categoryListener);
      line2Panel.add(_location.getComboBox());
    }
    // Quality
    {
      JPanel qualityPanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING));
      qualityPanel.add(GuiFactory.buildLabel("Quality:"));
      _quality=ItemUiTools.buildQualityCombo();
      ItemSelectionListener<ItemQuality> qualityListener=new ItemSelectionListener<ItemQuality>()
      {
        @Override
        public void itemSelected(ItemQuality quality)
        {
          _filter.getQualityFilter().setQuality(quality);
          filterUpdated();
        }
      };
      _quality.addListener(qualityListener);
      qualityPanel.add(_quality.getComboBox());
      line2Panel.add(qualityPanel);
    }

    c=new GridBagConstraints(0,y,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,5,0),0,0);
    panel.add(line2Panel,c);
    y++;

    return panel;
  }

  private ComboBoxController<Owner> buildOwnerCombobox()
  {
    ComboBoxController<Owner> ctrl=new ComboBoxController<Owner>();
    updateOwnerCombobox(ctrl);
    ctrl.selectItem(null);
    return ctrl;
  }

  private void updateOwnerCombobox(ComboBoxController<Owner> ctrl)
  {
    ctrl.removeAllItems();
    ctrl.addEmptyItem("");
    List<Owner> owners=_filter.getConfiguration().getOwners();
    for(Owner owner : owners)
    {
      ctrl.addItem(owner,getLabelForOwner(owner));
    }
  }

  /**
   * Get a simple label for a owner.
   * @param owner Owner to use.
   * @return A displayable string.
   */
  public static String getLabelForOwner(Owner owner)
  {
    if (owner instanceof AccountServerOwner)
    {
      return "Account";
    }
    if (owner instanceof CharacterOwner)
    {
      return ((CharacterOwner)owner).getCharacterName();
    }
    // Default case (not used)
    return owner.getLabel();
  }

  private ComboBoxController<StorageLocation> buildLocationCombobox()
  {
    ComboBoxController<StorageLocation> ctrl=new ComboBoxController<StorageLocation>();
    updateLocationCombobox(ctrl);
    ctrl.selectItem(null);
    return ctrl;
  }

  private void updateLocationCombobox(ComboBoxController<StorageLocation> ctrl)
  {
    ctrl.removeAllItems();
    ctrl.addEmptyItem("");
    List<StorageLocation> locations=_filter.getConfiguration().getLocations();
    for(StorageLocation location : locations)
    {
      ctrl.addItem(location,location.getLabel());
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
    // GUI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    if (_owner!=null)
    {
      _owner.dispose();
      _owner=null;
    }
    if (_location!=null)
    {
      _location.dispose();
      _location=null;
    }
    if (_quality!=null)
    {
      _quality.dispose();
      _quality=null;
    }
    _contains=null;
    _reset=null;
  }
}
