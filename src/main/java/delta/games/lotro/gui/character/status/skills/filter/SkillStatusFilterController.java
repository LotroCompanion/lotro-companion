package delta.games.lotro.gui.character.status.skills.filter;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.combobox.ComboBoxController;
import delta.common.ui.swing.combobox.ItemSelectionListener;
import delta.common.ui.swing.text.DynamicTextEditionController;
import delta.common.ui.swing.text.TextListener;
import delta.common.utils.collections.filters.Filter;
import delta.games.lotro.character.skills.SkillDescription;
import delta.games.lotro.character.status.skills.SkillStatus;
import delta.games.lotro.character.status.skills.filters.KnownSkillFilter;
import delta.games.lotro.character.status.skills.filters.SkillStatusFilter;
import delta.games.lotro.common.filters.NamedFilter;
import delta.games.lotro.gui.lore.items.FilterUpdateListener;
import delta.games.lotro.gui.utils.SharedUiUtils;

/**
 * Controller for a skill status filter edition panel.
 * @author DAM
 */
public class SkillStatusFilterController
{
  // Data
  private SkillStatusFilter _filter;
  // GUI
  private JPanel _panel;
  // -- Filter UI --
  private JTextField _contains;
  private ComboBoxController<Boolean> _known;
  // Controllers
  private DynamicTextEditionController _textController;
  private FilterUpdateListener _filterUpdateListener;

  /**
   * Constructor.
   * @param filter Managed filter.
   * @param filterUpdateListener Filter update listener.
   */
  public SkillStatusFilterController(SkillStatusFilter filter, FilterUpdateListener filterUpdateListener)
  {
    _filter=filter;
    _filterUpdateListener=filterUpdateListener;
  }

  /**
   * Get the managed filter.
   * @return the managed filter.
   */
  public Filter<SkillStatus> getFilter()
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
  }

  /**
   * Apply filter data into UI.
   */
  public void setFilter()
  {
    // Name
    NamedFilter<SkillDescription> nameFilter=_filter.getNameFilter();
    String contains=nameFilter.getPattern();
    if (contains!=null)
    {
      _contains.setText(contains);
    }
    // Known
    KnownSkillFilter knownFilter=_filter.getKnownFilter();
    _known.selectItem(knownFilter.getIsKnownFlag());
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
          if (newText.length()==0) newText=null;
          NamedFilter<SkillDescription> nameFilter=_filter.getNameFilter();
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
        KnownSkillFilter filter=_filter.getKnownFilter();
        filter.setIsKnownFlag(value);
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
    _contains=null;
  }
}
