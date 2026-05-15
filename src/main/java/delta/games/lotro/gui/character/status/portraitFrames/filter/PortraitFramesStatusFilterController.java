package delta.games.lotro.gui.character.status.portraitFrames.filter;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

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
import delta.games.lotro.character.status.portraitFrames.filters.PortraitFramesStatusFilter;
import delta.games.lotro.common.filters.NamedFilter;
import delta.games.lotro.gui.utils.SharedUiUtils;
import delta.games.lotro.gui.utils.l10n.Labels;
import delta.games.lotro.lore.portraitFrames.PortraitFrameDescription;

/**
 * Controller for a portrait frames status filter edition panel.
 * @author DAM
 */
public class PortraitFramesStatusFilterController
{
  // Data
  private PortraitFramesStatusFilter _filter;
  // GUI
  private JPanel _panel;
  // -- Filter UI --
  private JTextField _contains;
  // Controllers
  private ComboBoxController<Boolean> _unlocked;
  private DynamicTextEditionController _textController;
  private FilterUpdateListener _filterUpdateListener;

  /**
   * Constructor.
   * @param filter Managed filter.
   * @param filterUpdateListener Filter update listener.
   */
  public PortraitFramesStatusFilterController(PortraitFramesStatusFilter filter, FilterUpdateListener filterUpdateListener)
  {
    _filter=filter;
    _filterUpdateListener=filterUpdateListener;
  }

  /**
   * Get the managed filter.
   * @return the managed filter.
   */
  public Filter<PortraitFrameDescription> getFilter()
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
    _unlocked.selectItem(null);
    _contains.setText("");
  }

  /**
   * Apply filter data into UI.
   */
  public void setFilter()
  {
    // Name
    NamedFilter<PortraitFrameDescription> nameFilter=_filter.getNameFilter();
    String contains=nameFilter.getPattern();
    if (contains!=null)
    {
      _contains.setText(contains);
    }
    // State
    Boolean unlocked=_filter.getSelectedState();
    _unlocked.setSelectedItem(unlocked);
  }

  private JPanel build()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    int y=0;
    // Label filter
    {
      GridBagConstraints c=new GridBagConstraints(0,y,1,1,0.0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,5,5,5),0,0);
      panel.add(GuiFactory.buildLabel("Name:"),c); // I18n
      _contains=GuiFactory.buildTextField("");
      _contains.setColumns(10);
      c=new GridBagConstraints(1,y,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,5,5),0,0);
      panel.add(_contains,c);
      TextListener listener=new TextListener()
      {
        @Override
        public void textChanged(String newText)
        {
          if (newText.isEmpty())
          {
            newText=null;
          }
          NamedFilter<PortraitFrameDescription> nameFilter=_filter.getNameFilter();
          nameFilter.setPattern(newText);
          filterUpdated();
        }
      };
      _textController=new DynamicTextEditionController(_contains,listener);
      y++;
    }
    // State filter
    {
      JLabel label=GuiFactory.buildLabel("Unlocked:"); // I18n
      GridBagConstraints c=new GridBagConstraints(0,y,1,1,0.0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,5,5,5),0,0);
      panel.add(label,c);
      _unlocked=buildStateComboBox();
      c=new GridBagConstraints(1,y,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,5,5),0,0);
      panel.add(_unlocked.getComboBox(),c);
    }
    return panel;
  }

  private ComboBoxController<Boolean> buildStateComboBox()
  {
    ComboBoxController<Boolean> ctrl=SharedUiUtils.build3StatesBooleanCombobox(Labels.getLabel("shared.both"));
    ItemSelectionListener<Boolean> listener=new ItemSelectionListener<Boolean>()
    {
      @Override
      public void itemSelected(Boolean state)
      {
        _filter.setSelectedState(state);
        filterUpdated();
      }
    };
    ctrl.addListener(listener);
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
    if (_unlocked!=null)
    {
      _unlocked.dispose();
      _unlocked=null;
    }
    _contains=null;
  }
}
