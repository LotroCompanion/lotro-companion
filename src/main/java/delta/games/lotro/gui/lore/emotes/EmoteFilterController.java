package delta.games.lotro.gui.lore.emotes;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
import delta.games.lotro.gui.lore.emotes.EmoteFilterConfiguration.State;
import delta.games.lotro.gui.utils.SharedUiUtils;
import delta.games.lotro.gui.utils.l10n.Labels;
import delta.games.lotro.lore.emotes.EmoteDescription;
import delta.games.lotro.lore.emotes.filters.EmoteAutoFilter;
import delta.games.lotro.lore.emotes.filters.EmoteCommandFilter;

/**
 * Controller for an emote filter edition panel.
 * @author DAM
 */
public class EmoteFilterController implements ActionListener
{
  // Data
  private EmoteFilter _filter;
  private EmoteFilterConfiguration _config;
  // GUI
  private JPanel _panel;
  private JButton _reset;
  // -- Emotes attributes UI --
  private JTextField _contains;
  private ComboBoxController<Boolean> _auto;
  // Controllers
  private DynamicTextEditionController _textController;
  private FilterUpdateListener _filterUpdateListener;

  /**
   * Constructor.
   * @param filter Managed filter.
   * @param configuration Configuration.
   * @param filterUpdateListener Filter update listener.
   */
  public EmoteFilterController(EmoteFilter filter, EmoteFilterConfiguration configuration, FilterUpdateListener filterUpdateListener)
  {
    _filter=filter;
    _config=configuration;
    _filterUpdateListener=filterUpdateListener;
  }

  /**
   * Get the managed filter.
   * @return the managed filter.
   */
  public Filter<EmoteDescription> getFilter()
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
      if (_config.getAutoState()==State.ENABLED)
      {
        _auto.selectItem(null);
      }
      _contains.setText("");
    }
  }

  private void setFilter()
  {
    // Command
    EmoteCommandFilter nameFilter=_filter.getCommandFilter();
    String contains=nameFilter.getPattern();
    if (contains!=null)
    {
      _contains.setText(contains);
    }
    // Auto
    EmoteAutoFilter autoFilter=_filter.getAutoFilter();
    Boolean auto=autoFilter.getAutoFlag();
    _auto.selectItem(auto);
  }

  private JPanel build()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    int y=0;

    // Emote attributes
    JPanel emotePanel=buildEmotePanel();
    Border border=GuiFactory.buildTitledBorder("Emote"); // I18n
    emotePanel.setBorder(border);
    GridBagConstraints c=new GridBagConstraints(0,y,1,1,0.0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(emotePanel,c);

    // Reset
    _reset=GuiFactory.buildButton(Labels.getLabel("shared.reset"));
    _reset.addActionListener(this);
    c=new GridBagConstraints(1,y,1,1,0.0,0,GridBagConstraints.SOUTHWEST,GridBagConstraints.NONE,new Insets(0,5,5,5),0,0);
    panel.add(_reset,c);
    y++;

    return panel;
  }

  private JPanel buildEmotePanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    int y=0;
    JPanel line1Panel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING,5,0));
    // Label filter
    {
      line1Panel.add(GuiFactory.buildLabel("Command filter:")); // I18n
      _contains=GuiFactory.buildTextField("");
      _contains.setColumns(20);
      line1Panel.add(_contains);
      TextListener listener=new TextListener()
      {
        @Override
        public void textChanged(String newText)
        {
          if (newText.length()==0) newText=null;
          EmoteCommandFilter commandFilter=_filter.getCommandFilter();
          commandFilter.setPattern(newText);
          filterUpdated();
        }
      };
      _textController=new DynamicTextEditionController(_contains,listener);
    }
    // Auto
    State autoState=_config.getAutoState();
    if (autoState!=State.HIDDEN)
    {
      JLabel label=GuiFactory.buildLabel("Auto:"); // I18n
      line1Panel.add(label);
      _auto=buildAutoCombobox();
      line1Panel.add(_auto.getComboBox());
      if (autoState==State.VISIBLE)
      {
        _auto.getComboBox().setEnabled(false);
      }
    }
    GridBagConstraints c=new GridBagConstraints(0,y,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,5,0),0,0);
    panel.add(line1Panel,c);
    y++;

    return panel;
  }

  private ComboBoxController<Boolean> buildAutoCombobox()
  {
    ComboBoxController<Boolean> combo=SharedUiUtils.build3StatesBooleanCombobox("","Auto","Earned"); // I18n
    ItemSelectionListener<Boolean> listener=new ItemSelectionListener<Boolean>()
    {
      @Override
      public void itemSelected(Boolean value)
      {
        EmoteAutoFilter filter=_filter.getAutoFilter();
        filter.setAutoFlag(value);
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
    if (_auto!=null)
    {
      _auto.dispose();
      _auto=null;
    }
    _contains=null;
    _reset=null;
  }
}
