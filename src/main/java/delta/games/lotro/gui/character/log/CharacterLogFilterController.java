package delta.games.lotro.gui.character.log;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.combobox.ComboBoxController;
import delta.common.ui.swing.combobox.ItemSelectionListener;
import delta.games.lotro.character.log.CharacterLog;
import delta.games.lotro.character.log.CharacterLogItem.LogItemType;
import delta.games.lotro.character.log.CharacterLogItemsFilter;
import delta.games.lotro.utils.Formats;

/**
 * Controller for a character log filter edition panel.
 * @author DAM
 */
public class CharacterLogFilterController implements ItemListener
{
  // Data
  private CharacterLog _log;
  private CharacterLogItemsFilter _filter;
  // GUI
  private JPanel _panel;
  private ComboBoxController<Long> _minDates;
  private ComboBoxController<Long> _maxDates;
  private JTextField _contains;
  private HashMap<LogItemType,JCheckBox> _types;
  private CharacterLogPanelController _panelController;

  /**
   * Constructor.
   * @param log Managed log.
   * @param filter Log filter.
   * @param panelController Associated log panel controller.
   */
  public CharacterLogFilterController(CharacterLog log, CharacterLogItemsFilter filter, CharacterLogPanelController panelController)
  {
    _log=log;
    _types=new HashMap<LogItemType,JCheckBox>();
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
    Long minDate=_filter.getMinDate();
    _minDates.selectItem(minDate);
    Long maxDate=_filter.getMaxDate();
    _maxDates.selectItem(maxDate);
    String contains=_filter.getLabelFilter();
    if (contains!=null)
    {
      _contains.setText(contains);
    }
    Set<LogItemType> types=_filter.getSelectedTypes();
    for(Map.Entry<LogItemType,JCheckBox> item : _types.entrySet())
    {
      boolean selected=(types==null) || (types.contains(item.getKey()));
      item.getValue().setSelected(selected);
    }
  }

  private ComboBoxController<Long> buildDatesChooser(List<Long> dates)
  {
    ComboBoxController<Long> ret=new ComboBoxController<Long>();
    ret.addEmptyItem(" ");
    for(Long date : dates)
    {
      ret.addItem(date,Formats.getDateString(date));
    }
    return ret;
  }

  private JPanel build()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    // Dates
    JPanel datesPanel=GuiFactory.buildPanel(new GridBagLayout());
    {
      GridBagConstraints c=new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
      List<Long> dates=_log.getDates();
      datesPanel.add(GuiFactory.buildLabel("After:"),c); // I18n
      _minDates=buildDatesChooser(dates);
      c.gridx=1;
      datesPanel.add(_minDates.getComboBox(),c);
      c.gridy=1;c.gridx=0;
      datesPanel.add(GuiFactory.buildLabel("Before:"),c); // I18n
      _maxDates=buildDatesChooser(dates);
      c.gridx=1;
      datesPanel.add(_maxDates.getComboBox(),c);
      ItemSelectionListener<Long> listenerMinDates=new ItemSelectionListener<Long>()
      {
        @Override
        public void itemSelected(Long selected)
        {
          _filter.setMinDate(selected);
          updateFilter();
        }
      };
      _minDates.addListener(listenerMinDates);
      ItemSelectionListener<Long> listenerMaxDates=new ItemSelectionListener<Long>()
      {
        @Override
        public void itemSelected(Long selected)
        {
          _filter.setMaxDate(selected);
          updateFilter();
        }
      };
      _maxDates.addListener(listenerMaxDates);
    }
    // Label filter
    JPanel containsPanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING));
    {
      containsPanel.add(GuiFactory.buildLabel("Label filter:")); // I18n
      _contains=GuiFactory.buildTextField("");
      _contains.setColumns(20);
      containsPanel.add(_contains);
      DocumentListener dl=new DocumentListener()
      {
        @Override
        public void removeUpdate(DocumentEvent e)
        {
          doIt();
        }

        @Override
        public void insertUpdate(DocumentEvent e)
        {
          doIt();
        }

        @Override
        public void changedUpdate(DocumentEvent e)
        {
          doIt();
        }

        private void doIt()
        {
          String text=_contains.getText();
          if (text.length()==0) text=null;
          _filter.setLabelFilter(text);
          updateFilter();
        }
      };
      _contains.getDocument().addDocumentListener(dl);
    }
    // Checkboxes
    JPanel cbPanel=GuiFactory.buildPanel(new GridBagLayout());
    {
      LogItemType[] types={
          LogItemType.QUEST,LogItemType.DEED,LogItemType.LEVELUP,
          LogItemType.PROFESSION, LogItemType.VOCATION, LogItemType.PVMP};
      String[] labels={"Quests","Deeds","Level-ups","Profession","Vocation","PvM"}; // I18n
      _types.clear();
      int nbInRow=3;
      int nbTypes=types.length;
      for(int i=0;i<nbTypes;i++)
      {
        LogItemType type=types[i];
        JCheckBox checkbox=GuiFactory.buildCheckbox(labels[i]);
        _types.put(type,checkbox);
        checkbox.addItemListener(this);
        GridBagConstraints c=new GridBagConstraints(i%nbInRow,i/nbInRow,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
        cbPanel.add(checkbox,c);
      }
    }

    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(datesPanel,c);
    c=new GridBagConstraints(1,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(cbPanel,c);
    c=new GridBagConstraints(0,1,2,1,1,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(containsPanel,c);
    return panel;
  }

  /**
   * Invoked when a 'type' checkbox state has changed.
   * @param event Source event.
   */
  @Override
  public void itemStateChanged(ItemEvent event)
  {
    Set<LogItemType> types=new HashSet<LogItemType>();
    for(Map.Entry<LogItemType,JCheckBox> entry : _types.entrySet())
    {
      JCheckBox checkbox=entry.getValue();
      LogItemType type=entry.getKey();
      if (checkbox.isSelected())
      {
        types.add(type);
      }
    }
    _filter.setSelectedTypes(types);
    updateFilter();
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Data
    _log=null;
    _filter=null;
    // Controllers
    _panelController=null;
    // GUI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    if (_minDates!=null)
    {
      _minDates.dispose();
      _minDates=null;
    }
    if (_maxDates!=null)
    {
      _maxDates.dispose();
      _maxDates=null;
    }
    _contains=null;
    _types=null;
  }
}
