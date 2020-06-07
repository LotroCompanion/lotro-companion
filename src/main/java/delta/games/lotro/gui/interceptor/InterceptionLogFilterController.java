package delta.games.lotro.gui.interceptor;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.combobox.ComboBoxController;
import delta.common.ui.swing.combobox.ItemSelectionListener;
import delta.common.utils.collections.filters.Filter;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.gui.items.FilterUpdateListener;
import delta.games.lotro.interceptor.data.monitoring.InterceptionLog;
import delta.games.lotro.interceptor.data.monitoring.InterceptionLogEntry;
import delta.games.lotro.interceptor.data.monitoring.LogLevel;
import delta.games.lotro.interceptor.data.monitoring.filters.CharacterFileFilter;
import delta.games.lotro.interceptor.data.monitoring.filters.InterceptionLogFilter;
import delta.games.lotro.interceptor.data.monitoring.filters.LogLevelFilter;

/**
 * Controller for an interception log filter edition panel.
 * @author DAM
 */
public class InterceptionLogFilterController implements ActionListener
{
  // Data
  private InterceptionLogFilter _filter;
  // GUI
  private JPanel _panel;
  private JButton _reset;
  // Filter UI
  private ComboBoxController<CharacterFile> _character;
  private ComboBoxController<LogLevel> _level;
  // Listener
  private FilterUpdateListener _filterUpdateListener;
  // Data
  private List<CharacterFile> _knownCharacters;
  private InterceptionLog _log;

  /**
   * Constructor.
   * @param filter Managed filter.
   * @param filterUpdateListener Filter update listener.
   * @param log Interception log.
   */
  public InterceptionLogFilterController(InterceptionLogFilter filter, FilterUpdateListener filterUpdateListener, InterceptionLog log)
  {
    _filter=filter;
    _filterUpdateListener=filterUpdateListener;
    _log=log;
    _knownCharacters=new ArrayList<CharacterFile>();
  }

  /**
   * Get the managed filter.
   * @return the managed filter.
   */
  public Filter<InterceptionLogEntry> getFilter()
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
   * Refresh filter contents.
   */
  public void refresh()
  {
    List<CharacterFile> characters=_log.buildCharactersList();
    if (!characters.equals(_knownCharacters))
    {
      _knownCharacters=characters;
      updateCharactersCombo();
    }
  }

  private void updateCharactersCombo()
  {
    CharacterFile selectedFile=_character.getSelectedItem();
    _character.removeAllItems();
    _character.addEmptyItem("");
    for(CharacterFile character : _knownCharacters)
    {
      String name=character.getName();
      String server=character.getServerName();
      String id=name+"@"+server;
      _character.addItem(character,id);
    }
    if (selectedFile!=null)
    {
      if (_knownCharacters.contains(selectedFile))
      {
        _character.selectItem(selectedFile);
      }
    }
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
      _level.selectItem(null);
      _character.selectItem(null);
    }
  }

  private void setFilter()
  {
    // Level
    LogLevelFilter logLevelFilter=_filter.getLogLevelFilter();
    _level.selectItem(logLevelFilter.getLogLevel());
    // Character
    CharacterFileFilter characterFilter=_filter.getCharacterFilter();
    _character.selectItem(characterFilter.getCharacter());
  }

  private JPanel build()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    int y=0;

    // Log entry attributes
    JPanel logEntryPanel=buildLogEntryPanel();
    Border border=GuiFactory.buildTitledBorder("Log entry attributes");
    logEntryPanel.setBorder(border);
    GridBagConstraints c=new GridBagConstraints(0,y,1,1,0.0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(logEntryPanel,c);

    // Reset
    _reset=GuiFactory.buildButton("Reset");
    _reset.addActionListener(this);
    c=new GridBagConstraints(1,y,1,1,0.0,0,GridBagConstraints.SOUTHWEST,GridBagConstraints.NONE,new Insets(0,5,5,5),0,0);
    panel.add(_reset,c);
    y++;

    return panel;
  }

  private JPanel buildLogEntryPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    int y=0;
    JPanel line1Panel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING,5,2));
    // Log level
    {
      JLabel label=GuiFactory.buildLabel("Level:");
      line1Panel.add(label);
      _level=buildLogLevelCombobox();
      line1Panel.add(_level.getComboBox());
    }
    // Character
    {
      JLabel label=GuiFactory.buildLabel("Character:");
      line1Panel.add(label);
      _character=buildCharacterCombobox();
      line1Panel.add(_character.getComboBox());
    }
    GridBagConstraints c=new GridBagConstraints(0,y,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,5,0),0,0);
    panel.add(line1Panel,c);
    y++;

    return panel;
  }

  /**
   * Build a combo-box controller to choose level.
   * @return A new combo-box controller.
   */
  private ComboBoxController<LogLevel> buildLogLevelCombobox()
  {
    ComboBoxController<LogLevel> ctrl=new ComboBoxController<LogLevel>();
    ctrl.addEmptyItem("");
    ctrl.addItem(LogLevel.DEBUG,LogLevel.DEBUG.getLabel());
    ctrl.addItem(LogLevel.INFO,LogLevel.INFO.getLabel());
    ctrl.addItem(LogLevel.WARNING,LogLevel.WARNING.getLabel());
    ctrl.addItem(LogLevel.ERROR,LogLevel.ERROR.getLabel());
    ctrl.selectItem(null);

    ItemSelectionListener<LogLevel> listener=new ItemSelectionListener<LogLevel>()
    {
      @Override
      public void itemSelected(LogLevel value)
      {
        LogLevelFilter filter=_filter.getLogLevelFilter();
        filter.setLogLevel(value);
        filterUpdated();
      }
    };
    ctrl.addListener(listener);
    return ctrl;
  }


  /**
   * Build a combo-box controller to choose character.
   * @return A new combo-box controller.
   */
  private ComboBoxController<CharacterFile> buildCharacterCombobox()
  {
    ComboBoxController<CharacterFile> ctrl=new ComboBoxController<CharacterFile>();
    ctrl.addEmptyItem("");
    ctrl.selectItem(null);

    ItemSelectionListener<CharacterFile> listener=new ItemSelectionListener<CharacterFile>()
    {
      @Override
      public void itemSelected(CharacterFile value)
      {
        CharacterFileFilter filter=_filter.getCharacterFilter();
        filter.setCharacter(value);
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
    // Controllers
    if (_level!=null)
    {
      _level.dispose();
      _level=null;
    }
    if (_character!=null)
    {
      _character.dispose();
      _character=null;
    }
    // GUI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    _reset=null;
  }
}
