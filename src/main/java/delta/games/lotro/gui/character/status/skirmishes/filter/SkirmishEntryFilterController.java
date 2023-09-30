package delta.games.lotro.gui.character.status.skirmishes.filter;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.combobox.ComboBoxController;
import delta.common.ui.swing.combobox.ItemSelectionListener;
import delta.common.ui.swing.multicheckbox.MultiCheckboxController;
import delta.games.lotro.character.status.skirmishes.SkirmishLevel;
import delta.games.lotro.character.status.skirmishes.filter.SkirmishEntryFilter;
import delta.games.lotro.character.status.skirmishes.filter.SkirmishEntryLevelFilter;
import delta.games.lotro.character.status.skirmishes.filter.SkirmishEntrySizeFilter;
import delta.games.lotro.character.status.skirmishes.filter.SkirmishEntrySkirmishFilter;
import delta.games.lotro.common.enums.GroupSize;
import delta.games.lotro.common.enums.LotroEnum;
import delta.games.lotro.common.enums.LotroEnumsRegistry;
import delta.games.lotro.gui.lore.items.FilterUpdateListener;
import delta.games.lotro.gui.utils.l10n.Labels;
import delta.games.lotro.lore.instances.PrivateEncountersManager;
import delta.games.lotro.lore.instances.SkirmishPrivateEncounter;

/**
 * Controller for an edition panel for a skirmish entry filter.
 * @author DAM
 */
public class SkirmishEntryFilterController implements ActionListener
{
  // Data
  private SkirmishEntryFilter _filter;
  // GUI
  private JPanel _panel;
  private JButton _reset;
  // Controllers
  private MultiCheckboxController<GroupSize> _sizes;
  private MultiCheckboxController<SkirmishLevel> _levels;
  private ComboBoxController<SkirmishPrivateEncounter> _skirmish;
  // Listeners
  private FilterUpdateListener _filterUpdateListener;

  /**
   * Constructor.
   * @param filter Managed filter.
   * @param filterUpdateListener Filter update listener.
   */
  public SkirmishEntryFilterController(SkirmishEntryFilter filter, FilterUpdateListener filterUpdateListener)
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
    _filter.getSkirmishFilter().setSkirmish(_skirmish.getSelectedItem());
    _filter.getGroupSizeFilter().setSizes(new HashSet<GroupSize>(_sizes.getSelectedItems()));
    _filter.getLevelFilter().setLevels(new HashSet<SkirmishLevel>(_levels.getSelectedItems()));
    _filterUpdateListener.filterUpdated();
  }

  @Override
  public void actionPerformed(ActionEvent e)
  {
    Object source=e.getSource();
    if (source==_reset)
    {
      _skirmish.setSelectedItem(null);
      _levels.selectAll();
      _sizes.selectAll();
      filterUpdated();
    }
  }

  private void setFilter()
  {
    // Skirmish
    SkirmishEntrySkirmishFilter skirmishFilter=_filter.getSkirmishFilter();
    SkirmishPrivateEncounter skirmish=skirmishFilter.getSkirmish();
    _skirmish.selectItem(skirmish);
    // Size
    SkirmishEntrySizeFilter sizeFilter=_filter.getGroupSizeFilter();
    Set<GroupSize> sizes=sizeFilter.getSelectedSizes();
    _sizes.setSelectedItems(sizes);
    // Level
    SkirmishEntryLevelFilter levelFilter=_filter.getLevelFilter();
    Set<SkirmishLevel> levels=levelFilter.getSelectedLevels();
    _levels.setSelectedItems(levels);
  }

  private JPanel build()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    JPanel statusPanel=buildFilterPanel();
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(statusPanel,c);

    // Reset
    _reset=GuiFactory.buildButton(Labels.getLabel("shared.reset"));
    _reset.addActionListener(this);
    c=new GridBagConstraints(1,0,1,1,0.0,0,GridBagConstraints.SOUTHWEST,GridBagConstraints.NONE,new Insets(0,0,5,5),0,0);
    panel.add(_reset,c);

    return panel;
  }

  private JPanel buildFilterPanel()
  {
    // Build UI elements
    _skirmish=buildSkirmishCombobox();
    _sizes=buildSizeMultiCheckbox();
    _levels=buildLevelMultiCheckbox();

    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    // First line: skirmish combo and levels
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    JComboBox<?> skirmishCombo=_skirmish.getComboBox();
    TitledBorder border=GuiFactory.buildTitledBorder("Skirmish"); // I18n
    skirmishCombo.setBorder(border);
    skirmishCombo.setOpaque(false);
    panel.add(skirmishCombo,c);
    c=new GridBagConstraints(1,0,1,1,0.0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    JPanel levelsPanel=_levels.getPanel();
    levelsPanel.setBorder(GuiFactory.buildTitledBorder("Level")); // I18n
    panel.add(levelsPanel,c);

    // Second line: group sizes
    c=new GridBagConstraints(0,1,2,1,0.0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    JPanel sizesPanel=_sizes.getPanel();
    sizesPanel.setBorder(GuiFactory.buildTitledBorder("Group size")); // I18n
    panel.add(sizesPanel,c);

    return panel;
  }

  private ComboBoxController<SkirmishPrivateEncounter> buildSkirmishCombobox()
  {
    ComboBoxController<SkirmishPrivateEncounter> combo=buildSkirmishCombo();
    ItemSelectionListener<SkirmishPrivateEncounter> listener=new ItemSelectionListener<SkirmishPrivateEncounter>()
    {
      @Override
      public void itemSelected(SkirmishPrivateEncounter skirmish)
      {
        _filter.getSkirmishFilter().setSkirmish(skirmish);
        filterUpdated();
      }
    };
    combo.addListener(listener);
    return combo;
  }

  /**
   * Build a combo-box controller to choose a skirmish.
   * @return A new combo-box controller.
   */
  private static ComboBoxController<SkirmishPrivateEncounter> buildSkirmishCombo()
  {
    ComboBoxController<SkirmishPrivateEncounter> ctrl=new ComboBoxController<SkirmishPrivateEncounter>();
    ctrl.addEmptyItem("(all)");
    PrivateEncountersManager peMgr=PrivateEncountersManager.getInstance();
    List<SkirmishPrivateEncounter> skirmishs=peMgr.getSkirmishPrivateEncounters();
    for(SkirmishPrivateEncounter skirmish : skirmishs)
    {
      ctrl.addItem(skirmish,skirmish.getName());
    }
    ctrl.selectItem(null);
    return ctrl;
  }

  private MultiCheckboxController<GroupSize> buildSizeMultiCheckbox()
  {
    final MultiCheckboxController<GroupSize> multiCheckbox=new MultiCheckboxController<GroupSize>();
    LotroEnum<GroupSize> groupSizesMgr=LotroEnumsRegistry.getInstance().get(GroupSize.class);
    for(GroupSize size : groupSizesMgr.getAll())
    {
      String label=size.toString();
      multiCheckbox.addItem(size,label);
    }
    multiCheckbox.selectAll();
    ItemSelectionListener<GroupSize> listener=new ItemSelectionListener<GroupSize>()
    {
      @Override
      public void itemSelected(GroupSize size)
      {
        Set<GroupSize> sizes=new HashSet<GroupSize>(multiCheckbox.getItems());
        SkirmishEntrySizeFilter sizeFilter=_filter.getGroupSizeFilter();
        sizeFilter.setSizes(sizes);
        filterUpdated();
      }
    };
    multiCheckbox.addListener(listener);
    return multiCheckbox;
  }

  private MultiCheckboxController<SkirmishLevel> buildLevelMultiCheckbox()
  {
    final MultiCheckboxController<SkirmishLevel> multiCheckbox=new MultiCheckboxController<SkirmishLevel>();
    for(SkirmishLevel level : SkirmishLevel.values())
    {
      String label=level.toString();
      multiCheckbox.addItem(level,label);
    }
    multiCheckbox.selectAll();
    ItemSelectionListener<SkirmishLevel> listener=new ItemSelectionListener<SkirmishLevel>()
    {
      @Override
      public void itemSelected(SkirmishLevel level)
      {
        Set<SkirmishLevel> levels=new HashSet<SkirmishLevel>(multiCheckbox.getItems());
        SkirmishEntryLevelFilter levelFilter=_filter.getLevelFilter();
        levelFilter.setLevels(levels);
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
    _reset=null;
    // Controllers
    if (_skirmish!=null)
    {
      _skirmish.dispose();
      _skirmish=null;
    }
    if (_sizes!=null)
    {
      _sizes.dispose();
      _sizes=null;
    }
    if (_levels!=null)
    {
      _levels.dispose();
      _levels=null;
    }
    // Listeners
    _filterUpdateListener=null;
  }
}
