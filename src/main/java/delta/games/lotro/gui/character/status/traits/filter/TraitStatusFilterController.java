package delta.games.lotro.gui.character.status.traits.filter;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.combobox.ComboBoxController;
import delta.common.ui.swing.combobox.ItemSelectionListener;
import delta.common.ui.swing.tables.panel.FilterUpdateListener;
import delta.common.ui.swing.text.DynamicTextEditionController;
import delta.common.ui.swing.text.TextListener;
import delta.common.utils.collections.filters.Filter;
import delta.games.lotro.character.status.traits.TraitStatus;
import delta.games.lotro.character.status.traits.filters.KnownTraitFilter;
import delta.games.lotro.character.status.traits.filters.TraitStatusFilter;
import delta.games.lotro.character.traits.TraitDescription;
import delta.games.lotro.character.traits.filters.TraitGroupFilter;
import delta.games.lotro.common.enums.TraitGroup;
import delta.games.lotro.common.filters.NamedFilter;
import delta.games.lotro.gui.character.status.traits.TraitGroupsUtils;
import delta.games.lotro.gui.utils.SharedUiUtils;

/**
 * Controller for a trait status filter edition panel.
 * @author DAM
 */
public class TraitStatusFilterController
{
  // Data
  private TraitStatusFilter _filter;
  // GUI
  private JPanel _panel;
  // -- Filter UI --
  private JTextField _contains;
  private ComboBoxController<Boolean> _known;
  private ComboBoxController<TraitGroup> _group;
  // Controllers
  private DynamicTextEditionController _textController;
  private FilterUpdateListener _filterUpdateListener;

  /**
   * Constructor.
   * @param filter Managed filter.
   * @param filterUpdateListener Filter update listener.
   */
  public TraitStatusFilterController(TraitStatusFilter filter, FilterUpdateListener filterUpdateListener)
  {
    _filter=filter;
    _filterUpdateListener=filterUpdateListener;
  }

  /**
   * Get the managed filter.
   * @return the managed filter.
   */
  public Filter<TraitStatus> getFilter()
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

  /**
   * Reset all gadgets.
   */
  public void reset()
  {
    _known.selectItem(null);
    _contains.setText("");
    _group.selectItem(null);
  }

  /**
   * Apply filter data into UI.
   */
  public void setFilter()
  {
    // Name
    NamedFilter<TraitDescription> nameFilter=_filter.getNameFilter();
    String contains=nameFilter.getPattern();
    if (contains!=null)
    {
      _contains.setText(contains);
    }
    // Known
    KnownTraitFilter knownFilter=_filter.getKnownFilter();
    _known.selectItem(knownFilter.getIsKnownFlag());
    // Group
    TraitGroupFilter groupFilter=_filter.getGroupFilter();
    _group.selectItem(groupFilter.getGroup());
  }

  private JPanel build()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    int y=0;
    JPanel linePanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING,5,0));
    // Label filter
    {
      linePanel.add(GuiFactory.buildLabel("Name filter:")); // I18n
      _contains=GuiFactory.buildTextField("");
      _contains.setColumns(10);
      linePanel.add(_contains);
      TextListener listener=new TextListener()
      {
        @Override
        public void textChanged(String newText)
        {
          if (newText.isEmpty())
          {
            newText=null;
          }
          NamedFilter<TraitDescription> nameFilter=_filter.getNameFilter();
          nameFilter.setPattern(newText);
          filterUpdated();
        }
      };
      _textController=new DynamicTextEditionController(_contains,listener);
    }
    // Known
    {
      JLabel label=GuiFactory.buildLabel("Known:"); // I18n
      linePanel.add(label);
      _known=buildKnownCombobox();
      linePanel.add(_known.getComboBox());
    }
    GridBagConstraints c=new GridBagConstraints(0,y,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(linePanel,c);
    y++;
    JPanel line2Panel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING,5,0));
    // Group
    {
      JLabel label=GuiFactory.buildLabel("Slot:"); // I18n
      line2Panel.add(label);
      _group=buildGroupCombobox();
      line2Panel.add(_group.getComboBox());
    }
    c=new GridBagConstraints(0,y,2,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(3,0,5,0),0,0);
    panel.add(line2Panel,c);
    return panel;
  }

  private ComboBoxController<Boolean> buildKnownCombobox()
  {
    ComboBoxController<Boolean> combo=SharedUiUtils.build3StatesBooleanCombobox();
    ItemSelectionListener<Boolean> listener=new ItemSelectionListener<Boolean>()
    {
      @Override
      public void itemSelected(Boolean value)
      {
        KnownTraitFilter filter=_filter.getKnownFilter();
        filter.setIsKnownFlag(value);
        filterUpdated();
      }
    };
    combo.addListener(listener);
    return combo;
  }

  private ComboBoxController<TraitGroup> buildGroupCombobox()
  {
    ComboBoxController<TraitGroup> combo=buildTraitGroupCombobox();
    ItemSelectionListener<TraitGroup> listener=new ItemSelectionListener<TraitGroup>()
    {
      @Override
      public void itemSelected(TraitGroup value)
      {
        TraitGroupFilter filter=_filter.getGroupFilter();
        filter.setGroup(value);
        filterUpdated();
      }
    };
    combo.addListener(listener);
    return combo;
  }

  /**
   * Build a combo-box controller to choose a trait group.
   * @return A new combo-box controller.
   */
  private ComboBoxController<TraitGroup> buildTraitGroupCombobox()
  {
    ComboBoxController<TraitGroup> ctrl=new ComboBoxController<TraitGroup>();
    ctrl.addEmptyItem("(all)"); // I18n
    List<TraitGroup> groups=TraitGroupsUtils.getTraitGroupsForSlots();
    for(TraitGroup group : groups)
    {
      ctrl.addItem(group,group.getLabel());
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
    _filter=null;
    // GUI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    // Controllers
    if (_textController!=null)
    {
      _textController.dispose();
      _textController=null;
    }
    if (_known!=null)
    {
      _known.dispose();
      _known=null;
    }
    if (_group!=null)
    {
      _group.dispose();
      _group=null;
    }
    _contains=null;
    _filterUpdateListener=null;
  }
}
