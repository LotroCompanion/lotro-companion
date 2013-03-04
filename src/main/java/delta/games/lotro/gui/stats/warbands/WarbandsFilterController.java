package delta.games.lotro.gui.stats.warbands;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import delta.games.lotro.common.SIZE;
import delta.games.lotro.gui.utils.GuiFactory;
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
  private JComboBox _minLevel;
  private JComboBox _region;
  private JComboBox _size;
  private JButton _reset;
  private WarbandsPanelController _panelController;

  /**
   * Constructor.
   * @param filter Log filter.
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
    _region.setSelectedItem(region);
    Integer minLevel=_filter.getMinLevel();
    _minLevel.setSelectedItem(minLevel);
    SIZE size=_filter.getSize();
    _size.setSelectedItem(size);
  }

  public void actionPerformed(ActionEvent e)
  {
    Object source=e.getSource();
    if (source==_region)
    {
      String region=(String)_region.getSelectedItem();
      _filter.setRegion(region);
    }
    else if (source==_minLevel)
    {
      Integer minLevel=(Integer)_minLevel.getSelectedItem();
      _filter.setMinLevel(minLevel);
    }
    else if (source==_size)
    {
      SIZE size=(SIZE)_size.getSelectedItem();
      _filter.setSize(size);
    }
    else if (source==_reset)
    {
      _region.setSelectedItem(null);
      _filter.setRegion(null);
      _minLevel.setSelectedItem(null);
      _filter.setMinLevel(null);
      _size.setSelectedItem(null);
      _filter.setSize(null);
    }
    updateFilter();
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
    _region=buildCombo(regions,null);
    _region.addActionListener(this);
    panel.add(GuiFactory.buildLabel("Region:"));
    panel.add(_region);
    // Sizes
    SIZE[] sizes={SIZE.SOLO,SIZE.SMALL_FELLOWSHIP,SIZE.FELLOWSHIP,SIZE.RAID};
    _size=buildCombo(sizes,null);
    _size.addActionListener(this);
    panel.add(GuiFactory.buildLabel("Size:"));
    panel.add(_size);
    // Levels
    Integer[] minLevels=getMinLevels();
    _minLevel=buildCombo(minLevels,null);
    _minLevel.addActionListener(this);
    panel.add(GuiFactory.buildLabel("Minimum level:"));
    panel.add(_minLevel);
    // Reset
    _reset=GuiFactory.buildButton("Reset");
    _reset.addActionListener(this);
    panel.add(_reset);
    return panel;
  }

  private JComboBox buildCombo(final Object[] values, final String[] labels)
  {
    JComboBox combo=GuiFactory.buildComboBox();
    Object[] valuesWithNull=new Object[values.length+1];
    valuesWithNull[0]=null;
    System.arraycopy(values,0,valuesWithNull,1,values.length);
    DefaultComboBoxModel model=new DefaultComboBoxModel(valuesWithNull);
    combo.setModel(model);
    ListCellRenderer r=new DefaultListCellRenderer()
    {
      public Component getListCellRendererComponent(JList list, Object value, int modelIndex, boolean isSelected, boolean cellHasFocus)
      {
        String text=" ";
        if (value!=null)
        {
          text=value.toString();
          if (labels!=null)
          {
            for(int i=0;i<values.length;i++)
            {
              if (values[i]==value)
              {
                text=labels[i];
                break;
              }
            }
          }
        }
        Component c= super.getListCellRendererComponent(list, text, modelIndex, isSelected, cellHasFocus);
        setText(text);
        setToolTipText(text);
        return c;
      }
    };
    combo.setRenderer(r);
    return combo;
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
