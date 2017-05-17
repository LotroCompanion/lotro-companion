package delta.games.lotro.gui.character.buffs;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import delta.games.lotro.character.stats.buffs.Buff;
import delta.games.lotro.character.stats.buffs.BuffFilter;
import delta.games.lotro.gui.utils.GuiFactory;
import delta.games.lotro.utils.gui.combobox.ComboBoxController;
import delta.games.lotro.utils.gui.combobox.ItemSelectionListener;

/**
 * Controller for a buff filter edition panel.
 * @author DAM
 */
public class BuffsFilterController
{
  // Data
  private List<Buff> _buffs;
  private BuffFilter _filter;
  // GUI
  private JPanel _panel;
  private ComboBoxController<String> _category;
  private JTextField _nameContains;
  private BuffChoicePanelController _panelController;

  /**
   * Constructor.
   * @param buffs Buffs.
   * @param filter Filter.
   * @param panelController Associated panel controller.
   */
  public BuffsFilterController(List<Buff> buffs, BuffFilter filter, BuffChoicePanelController panelController)
  {
    _buffs=buffs;
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

  /**
   * Set filter.
   */
  public void setFilter()
  {
    // Category
    String category=_filter.getCategory();
    _category.selectItem(category);
    // Name
    String contains=_filter.getNameFilter();
    if (contains!=null)
    {
      _nameContains.setText(contains);
    }
  }

  private JPanel build()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    // Category
    _category=buildCategoriesCombo();
    // Name filter
    JPanel nameContainsPanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING));
    {
      nameContainsPanel.add(GuiFactory.buildLabel("Name filter:"));
      _nameContains=buildReactiveTextField();
      nameContainsPanel.add(_nameContains);
    }
    JPanel containsPanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING));
    containsPanel.add(nameContainsPanel);

    // Category panel
    JPanel categoryPanel=GuiFactory.buildPanel(new FlowLayout());
    categoryPanel.add(GuiFactory.buildLabel("Category:"));
    categoryPanel.add(_category.getComboBox());

    // Global assembly
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    //panel.add(typePanel,c);
    c=new GridBagConstraints(1,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(categoryPanel,c);
    c=new GridBagConstraints(0,1,2,1,1,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(containsPanel,c);
    return panel;
  }

  private ComboBoxController<String> buildCategoriesCombo()
  {
    final ComboBoxController<String> ctrl=new ComboBoxController<String>();
    ctrl.addEmptyItem("");
    List<String> categories=getCategories();
    for(String category : categories)
    {
      ctrl.addItem(category,category);
    }
    ItemSelectionListener<String> l=new ItemSelectionListener<String>()
    {
      public void itemSelected(String category)
      {
        _filter.setCategory(category);
        updateFilter();
      }
    };
    ctrl.addListener(l);
    return ctrl;
  }

  private List<String> getCategories()
  {
    Set<String> categories=new HashSet<String>();
    for(Buff buff : _buffs)
    {
      String category=buff.getCategory();
      if (category!=null)
      {
        categories.add(category);
      }
    }
    List<String> ret=new ArrayList<String>();
    ret.addAll(categories);
    Collections.sort(ret);
    return ret;
  }

  private JTextField buildReactiveTextField()
  {
    final JTextField textField=GuiFactory.buildTextField("");
    textField.setColumns(20);
    DocumentListener dl=new DocumentListener()
    {
      public void removeUpdate(DocumentEvent e)
      {
        textFieldUpdated(textField);
      }
      public void insertUpdate(DocumentEvent e)
      {
        textFieldUpdated(textField);
      }
      public void changedUpdate(DocumentEvent e)
      {
        textFieldUpdated(textField);
      }
    };
    textField.getDocument().addDocumentListener(dl);
    return textField;
  }

  private void textFieldUpdated(JTextField textField)
  {
    String text=textField.getText();
    if (text.length()==0) text=null;
    if (textField==_nameContains)
    {
      _filter.setNameFilter(text);
    }
    updateFilter();
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Data
    _buffs=null;
    _filter=null;
    // Controllers
    _panelController=null;
    // GUI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    _category=null;
    _nameContains=null;
  }
}
