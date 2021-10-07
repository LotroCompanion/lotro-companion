package delta.games.lotro.gui.lore.items.legendary2.traceries;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.combobox.ComboBoxController;
import delta.common.ui.swing.combobox.ItemSelectionListener;
import delta.common.ui.swing.text.DynamicTextEditionController;
import delta.common.ui.swing.text.TextListener;
import delta.common.utils.collections.filters.Filter;
import delta.games.lotro.gui.lore.items.FilterUpdateListener;
import delta.games.lotro.gui.lore.items.ItemUiTools;
import delta.games.lotro.gui.utils.SharedUiUtils;
import delta.games.lotro.lore.items.ItemQuality;
import delta.games.lotro.lore.items.filters.ItemNameFilter;
import delta.games.lotro.lore.items.filters.ItemQualityFilter;
import delta.games.lotro.lore.items.legendary2.Tracery;
import delta.games.lotro.lore.items.legendary2.filters.TraceryFilter;
import delta.games.lotro.lore.items.legendary2.filters.TraceryTierFilter;
import delta.games.lotro.utils.gui.filter.ObjectFilterPanelController;

/**
 * Controller for a traceries filter edition panel.
 * @author DAM
 */
public class TraceriesFilterController extends ObjectFilterPanelController implements ActionListener
{
  // Data
  private TraceryFilter _filter;
  private List<Tracery> _traceries;
  // GUI
  private JPanel _panel;
  private JButton _reset;
  // -- Mob attributes UI --
  private JTextField _contains;
  private ComboBoxController<Integer> _tier;
  private ComboBoxController<ItemQuality> _quality;
  // Controllers
  private DynamicTextEditionController _textController;

  /**
   * Constructor.
   * @param filter Managed filter.
   * @param traceries Managed traceries.
   * @param filterUpdateListener Filter update listener.
   */
  public TraceriesFilterController(TraceryFilter filter, List<Tracery> traceries, FilterUpdateListener filterUpdateListener)
  {
    super();
    _filter=filter;
    _traceries=traceries;
    setFilterUpdateListener(filterUpdateListener);
  }

  /**
   * Get the managed filter.
   * @return the managed filter.
   */
  public Filter<Tracery> getFilter()
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

  @Override
  public void actionPerformed(ActionEvent e)
  {
    Object source=e.getSource();
    if (source==_reset)
    {
      _quality.selectItem(null);
      _tier.selectItem(null);
      _contains.setText("");
    }
  }

  private void setFilter()
  {
    // Name
    ItemNameFilter nameFilter=_filter.getNameFilter();
    String contains=nameFilter.getPattern();
    if (contains!=null)
    {
      _contains.setText(contains);
    }
    // Tier
    TraceryTierFilter tierFilter=_filter.getTierFilter();
    Integer tier=tierFilter.getTier();
    _tier.selectItem(tier);
    // Quality
    ItemQualityFilter qualityFilter=_filter.getQualityFilter();
    ItemQuality quality=qualityFilter.getQuality();
    _quality.selectItem(quality);
  }

  private JPanel build()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    int y=0;
    // Tracery attributes
    JPanel traceryPanel=buildTraceryPanel();
    traceryPanel.setBorder(GuiFactory.buildTitledBorder("Tracery"));
    GridBagConstraints c=new GridBagConstraints(0,y,1,1,0.0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(traceryPanel,c);

    // Reset
    _reset=GuiFactory.buildButton("Reset");
    _reset.addActionListener(this);
    c=new GridBagConstraints(1,y,1,1,0.0,0.0,GridBagConstraints.SOUTHWEST,GridBagConstraints.HORIZONTAL,new Insets(0,5,5,5),0,0);
    panel.add(_reset,c);

    // Glue
    Component glue=Box.createGlue();
    c=new GridBagConstraints(2,y,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(glue,c);
    y++;

    return panel;
  }

  private JPanel buildTraceryPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    int y=0;
    JPanel line1Panel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING,5,0));
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
          ItemNameFilter nameFilter=_filter.getNameFilter();
          nameFilter.setPattern(newText);
          filterUpdated();
        }
      };
      _textController=new DynamicTextEditionController(_contains,listener);
      line1Panel.add(containsPanel);
    }
    // Tier
    {
      line1Panel.add(GuiFactory.buildLabel("Tier:"));
      _tier=buildTierCombo(TraceryUiUtils.getTiers(_traceries));
      line1Panel.add(_tier.getComboBox());
    }
    // Quality
    {
      line1Panel.add(GuiFactory.buildLabel("Quality:"));
      _quality=buildQualityCombo();
      line1Panel.add(_quality.getComboBox());
    }
    GridBagConstraints c=new GridBagConstraints(0,y,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,5,0),0,0);
    panel.add(line1Panel,c);
    y++;

    return panel;
  }

  private ComboBoxController<ItemQuality> buildQualityCombo()
  {
    ComboBoxController<ItemQuality> ret=ItemUiTools.buildQualityCombo();
    ItemSelectionListener<ItemQuality> listener=new ItemSelectionListener<ItemQuality>()
    {
      @Override
      public void itemSelected(ItemQuality selected)
      {
        _filter.getQualityFilter().setQuality(selected);
        filterUpdated();
      }
    };
    ret.addListener(listener);
    return ret;
  }

  private ComboBoxController<Integer> buildTierCombo(List<Integer> values)
  {
    ComboBoxController<Integer> ret=SharedUiUtils.buildIntegerCombo(values,true);
    ret.setSelectedItem(null);
    ItemSelectionListener<Integer> listener=new ItemSelectionListener<Integer>()
    {
      @Override
      public void itemSelected(Integer selected)
      {
        _filter.getTierFilter().setTier(selected);
        filterUpdated();
      }
    };
    ret.addListener(listener);
    return ret;
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
    if (_quality!=null)
    {
      _quality.dispose();
      _quality=null;
    }
    if (_tier!=null)
    {
      _tier.dispose();
      _tier=null;
    }
    _contains=null;
    _reset=null;
  }
}
