package delta.games.lotro.gui.items.relics;

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

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.combobox.ComboBoxController;
import delta.common.ui.swing.combobox.ItemSelectionListener;
import delta.games.lotro.lore.items.legendary.relics.Relic;
import delta.games.lotro.lore.items.legendary.relics.RelicFilter;
import delta.games.lotro.lore.items.legendary.relics.RelicType;

/**
 * Controller for a relic filter edition panel.
 * @author DAM
 */
public class RelicsFilterController
{
  // Data
  private List<Relic> _relics;
  private RelicFilter _filter;
  // GUI
  private JPanel _panel;
  private ComboBoxController<String> _category;
  private ComboBoxController<RelicType> _type;
  private JTextField _nameContains;
  private JTextField _statsContains;
  private RelicChoicePanelController _panelController;

  /**
   * Constructor.
   * @param relics Relics.
   * @param filter Filter.
   * @param panelController Associated panel controller.
   */
  public RelicsFilterController(List<Relic> relics, RelicFilter filter, RelicChoicePanelController panelController)
  {
    _relics=relics;
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
    // Type
    RelicType type=_filter.getRelicType();
    _type.selectItem(type);
    if (type!=null)
    {
      _type.getComboBox().setEnabled(false);
    }
    // Category
    String category=_filter.getRelicCategory();
    _category.selectItem(category);
    // Name
    String contains=_filter.getNameFilter();
    if (contains!=null)
    {
      _nameContains.setText(contains);
    }
    // Stats
    String statsContains=_filter.getStatsFilter();
    if (statsContains!=null)
    {
      _statsContains.setText(statsContains);
    }
  }

  private JPanel build()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    // Type
    _type=buildTypesCombo();
    // Category
    _category=buildCategoriesCombo();
    // Name filter
    JPanel nameContainsPanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING));
    {
      nameContainsPanel.add(GuiFactory.buildLabel("Name filter:"));
      _nameContains=buildReactiveTextField();
      nameContainsPanel.add(_nameContains);
    }
    // Stats filter
    JPanel statsContainsPanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING));
    {
      statsContainsPanel.add(GuiFactory.buildLabel("Stats filter:"));
      _statsContains=buildReactiveTextField();
      statsContainsPanel.add(_statsContains);
    }
    JPanel containsPanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING));
    containsPanel.add(nameContainsPanel);
    containsPanel.add(statsContainsPanel);

    // Type panel
    JPanel typePanel=GuiFactory.buildPanel(new FlowLayout());
    typePanel.add(GuiFactory.buildLabel("Type:"));
    typePanel.add(_type.getComboBox());
    // Category panel
    JPanel categoryPanel=GuiFactory.buildPanel(new FlowLayout());
    categoryPanel.add(GuiFactory.buildLabel("Category:"));
    categoryPanel.add(_category.getComboBox());

    // Global assembly
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(typePanel,c);
    c=new GridBagConstraints(1,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(categoryPanel,c);
    c=new GridBagConstraints(0,1,2,1,1,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(containsPanel,c);
    return panel;
  }

  private ComboBoxController<RelicType> buildTypesCombo()
  {
    final ComboBoxController<RelicType> ctrl=new ComboBoxController<RelicType>();
    ctrl.addEmptyItem("");
    for(RelicType type : RelicType.values())
    {
      ctrl.addItem(type,type.name());
    }
    ItemSelectionListener<RelicType> l=new ItemSelectionListener<RelicType>()
    {
      public void itemSelected(RelicType type)
      {
        _filter.setRelicType(type);
        updateFilter();
      }
    };
    ctrl.addListener(l);
    return ctrl;
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
        _filter.setRelicCategory(category);
        updateFilter();
      }
    };
    ctrl.addListener(l);
    return ctrl;
  }

  private List<String> getCategories()
  {
    Set<String> categories=new HashSet<String>();
    for(Relic relic : _relics)
    {
      String category=relic.getCategory();
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
    if (textField==_statsContains)
    {
      _filter.setStatsFilter(text);
    }
    updateFilter();
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Data
    _relics=null;
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
    _type=null;
    _nameContains=null;
    _statsContains=null;
  }
}
