package delta.games.lotro.gui.character.status.warbands;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.combobox.ComboBoxController;
import delta.common.ui.swing.combobox.ItemSelectionListener;
import delta.games.lotro.common.Size;
import delta.games.lotro.gui.utils.l10n.Labels;
import delta.games.lotro.lore.warbands.WarbandDefinition;
import delta.games.lotro.lore.warbands.WarbandFilter;
import delta.games.lotro.lore.warbands.WarbandsRegistry;

/**
 * Controller for a warbands filter edition panel.
 * @author DAM
 */
public class WarbandsFilterController implements ActionListener
{
  // Data
  private WarbandFilter _filter;
  // GUI
  private JPanel _panel;
  private ComboBoxController<Integer> _minLevel;
  private ComboBoxController<String> _region;
  private ComboBoxController<Size> _size;
  private JButton _reset;
  private WarbandsPanelController _panelController;

  /**
   * Constructor.
   * @param filter Warband filter.
   * @param panelController Associated warbands panel controller.
   */
  public WarbandsFilterController(WarbandFilter filter, WarbandsPanelController panelController)
  {
    _filter=filter;
    _panelController=panelController;
  }

  private void updateFilter()
  {
    _panelController.updateFilter();
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
      updateFilter();
    }
    return _panel;
  }

  private void setFilter()
  {
    String region=_filter.getRegion();
    _region.selectItem(region);
    Integer minLevel=_filter.getMinLevel();
    _minLevel.selectItem(minLevel);
    Size size=_filter.getSize();
    _size.selectItem(size);
  }

  @Override
  public void actionPerformed(ActionEvent e)
  {
    Object source=e.getSource();
    if (source==_reset)
    {
      _region.selectItem(null);
      _filter.setRegion(null);
      _minLevel.selectItem(null);
      _filter.setMinLevel(null);
      _size.selectItem(null);
      _filter.setSize(null);
      updateFilter();
    }
  }

  private String[] getRegions()
  {
    WarbandDefinition[] warbands=WarbandsRegistry.getWarbandsRegistry().getAllWarbands();
    Set<String> regionsSets=new HashSet<String>();
    for(WarbandDefinition warband : warbands)
    {
      String region=warband.getRegion();
      regionsSets.add(region);
    }
    List<String> regions=new ArrayList<String>(regionsSets);
    Collections.sort(regions);
    String[] ret=regions.toArray(new String[regions.size()]);
    return ret;
  }

  private Integer[] getMinLevels()
  {
    WarbandDefinition[] warbands=WarbandsRegistry.getWarbandsRegistry().getAllWarbands();
    Set<Integer> levelSets=new HashSet<Integer>();
    for(WarbandDefinition warband : warbands)
    {
      Integer minLevel=warband.getLevel();
      if (minLevel!=null)
      {
        levelSets.add(minLevel);
      }
    }
    List<Integer> minLevels=new ArrayList<Integer>(levelSets);
    Collections.sort(minLevels);
    Integer[] ret=minLevels.toArray(new Integer[minLevels.size()]);
    return ret;
  }

  private JPanel build()
  {
    JPanel panel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING));

    // Regions
    String[] regions=getRegions();
    _region=new ComboBoxController<String>();
    _region.addEmptyItem(" ");
    for(String region : regions)
    {
      _region.addItem(region,region);
    }
    ItemSelectionListener<String> regionListener=new ItemSelectionListener<String>()
    {
      @Override
      public void itemSelected(String region)
      {
        _filter.setRegion(region);
        updateFilter();
      }
    };
    _region.addListener(regionListener);
    panel.add(GuiFactory.buildLabel("Region:")); // I18n
    panel.add(_region.getComboBox());
    // Sizes
    Size[] sizes={Size.SOLO,Size.SMALL_FELLOWSHIP,Size.FELLOWSHIP,Size.RAID};
    _size=new ComboBoxController<Size>();
    _size.addEmptyItem(" ");
    for(Size size : sizes)
    {
      _size.addItem(size,size.toString());
    }
    ItemSelectionListener<Size> sizeListener=new ItemSelectionListener<Size>()
    {
      @Override
      public void itemSelected(Size size)
      {
        _filter.setSize(size);
        updateFilter();
      }
    };
    _size.addListener(sizeListener);
    panel.add(GuiFactory.buildLabel("Size:")); // I18n
    panel.add(_size.getComboBox());
    // Levels
    Integer[] minLevels=getMinLevels();
    _minLevel=new ComboBoxController<Integer>();
    _minLevel.addEmptyItem(" ");
    for(Integer minLevel : minLevels)
    {
      _minLevel.addItem(minLevel,minLevel.toString());
    }
    ItemSelectionListener<Integer> minLevelListener=new ItemSelectionListener<Integer>()
    {
      @Override
      public void itemSelected(Integer minLevel)
      {
        _filter.setMinLevel(minLevel);
        updateFilter();
      }
    };
    _minLevel.addListener(minLevelListener);
    panel.add(GuiFactory.buildLabel("Minimum level:")); // I18n
    panel.add(_minLevel.getComboBox());
    // Reset
    _reset=GuiFactory.buildButton(Labels.getLabel("shared.reset"));
    _reset.addActionListener(this);
    panel.add(_reset);
    return panel;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Data
    _filter=null;
    // Controllers
    _panelController=null;
    if (_region!=null)
    {
      _region.dispose();
      _region=null;
    }
    if (_size!=null)
    {
      _size.dispose();
      _size=null;
    }
    if (_minLevel!=null)
    {
      _minLevel.dispose();
      _minLevel=null;
    }
    // GUI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    _minLevel=null;
    _region=null;
    _size=null;
    _reset=null;
  }
}
