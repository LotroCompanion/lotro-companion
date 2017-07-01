package delta.games.lotro.gui.log;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import delta.common.ui.swing.GuiFactory;
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
  private JComboBox _minDate;
  private DefaultComboBoxModel _minDatesModel;
  private JComboBox _maxDate;
  private DefaultComboBoxModel _maxDatesModel;
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

  /**
   * Set a new character log.
   * @param log Character log to set.
   */
  public void setLog(CharacterLog log)
  {
    _log=log;
    // Update dates combo
    Long[] datesArray=getDates();
    _minDatesModel=buildOrUpdateModel(_minDatesModel,datesArray);
    _minDate.setModel(_minDatesModel);
    _maxDatesModel=buildOrUpdateModel(_maxDatesModel,datesArray);
    _maxDate.setModel(_maxDatesModel);
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
    _minDate.setSelectedItem(minDate);
    Long maxDate=_filter.getMaxDate();
    _maxDate.setSelectedItem(maxDate);
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

  private DefaultComboBoxModel buildOrUpdateModel(DefaultComboBoxModel model, Long[] dates)
  {
    DefaultComboBoxModel ret;
    Long selectedDate=null;
    if (model!=null)
    {
      selectedDate=(Long)model.getSelectedItem();
    }
    ret=new DefaultComboBoxModel(dates);
    Long toSelect=null;
    if (selectedDate!=null)
    {
      for(int i=0;i<dates.length;i++)
      {
        if ((dates[i]!=null) && (selectedDate.longValue()==dates[i].longValue()))
        {
          toSelect=dates[i];
          break;
        }
      }
    }
    ret.setSelectedItem(toSelect);
    return ret;
  }

  private Long[] getDates()
  {
    if (_log!=null)
    {
      List<Long> dates=_log.getDates();
      dates.add(0,null);
      Long[] datesArray=dates.toArray(new Long[dates.size()]);
      return datesArray;
    }
    return new Long[0];
  }

  private JPanel build()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    // Dates
    JPanel datesPanel=GuiFactory.buildPanel(new GridBagLayout());
    {
      GridBagConstraints c=new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
      Long[] datesArray=getDates();
      datesPanel.add(GuiFactory.buildLabel("After:"),c);
      _minDatesModel=buildOrUpdateModel(_minDatesModel,datesArray);
      _minDate=buildDatesCombo(_minDatesModel);
      c.gridx=1;
      datesPanel.add(_minDate,c);
      c.gridy=1;c.gridx=0;
      datesPanel.add(GuiFactory.buildLabel("Before:"),c);
      _maxDatesModel=buildOrUpdateModel(_maxDatesModel,datesArray);
      _maxDate=buildDatesCombo(_maxDatesModel);
      c.gridx=1;
      datesPanel.add(_maxDate,c);
    }
    // Label filter
    JPanel containsPanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING));
    {
      containsPanel.add(GuiFactory.buildLabel("Label filter:"));
      _contains=GuiFactory.buildTextField("");
      _contains.setColumns(20);
      containsPanel.add(_contains);
      DocumentListener dl=new DocumentListener()
      {
        public void removeUpdate(DocumentEvent e)
        {
          doIt();
        }

        public void insertUpdate(DocumentEvent e)
        {
          doIt();
        }

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
      String[] labels={"Quests","Deeds","Level-ups","Profession","Vocation","PvM"};
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

  private JComboBox buildDatesCombo(DefaultComboBoxModel datesModel)
  {
    JComboBox combo=GuiFactory.buildComboBox();
    combo.setModel(datesModel);
    ListCellRenderer r=new DefaultListCellRenderer()
    {
      public Component getListCellRendererComponent(JList list, Object value, int modelIndex, boolean isSelected, boolean cellHasFocus)
      {
        Long date=(Long)value;
        String text=" ";
        if (value!=null)
        {
          text=Formats.getDateString(date);
        }
        Component c= super.getListCellRendererComponent(list, text, modelIndex, isSelected, cellHasFocus);
        setText(text);
        setToolTipText(text);
        return c;
      }
    };
    combo.setRenderer(r);
    ActionListener l=new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        JComboBox source=(JComboBox)e.getSource();
        if (source==_minDate)
        {
          Long selected=(Long)source.getSelectedItem();
          _filter.setMinDate(selected);
        }
        else if (source==_maxDate)
        {
          Long selected=(Long)source.getSelectedItem();
          _filter.setMaxDate(selected);
        }
        updateFilter();
      }
    };
    combo.addActionListener(l);
    return combo;
  }

  public void itemStateChanged(ItemEvent e) {
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
    _minDate=null;
    _minDatesModel=null;
    _maxDate=null;
    _maxDatesModel=null;
    _contains=null;
    _types=null;
  }
}
