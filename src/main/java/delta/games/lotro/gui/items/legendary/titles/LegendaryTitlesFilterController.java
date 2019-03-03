package delta.games.lotro.gui.items.legendary.titles;

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
import delta.games.lotro.lore.items.legendary.titles.LegendaryTitle;
import delta.games.lotro.lore.items.legendary.titles.LegendaryTitleFilter;
import delta.games.lotro.lore.items.legendary.titles.LegendaryTitlesManager;
import delta.games.lotro.utils.gui.filter.ObjectFilterPanelController;

/**
 * Controller for a legendary title filter edition panel.
 * @author DAM
 */
public class LegendaryTitlesFilterController extends ObjectFilterPanelController
{
  // Data
  private LegendaryTitleFilter _filter;
  // GUI
  private JPanel _panel;
  private ComboBoxController<String> _category;
  private JTextField _nameContains;
  private JTextField _statsContains;

  /**
   * Constructor.
   * @param filter Filter.
   */
  public LegendaryTitlesFilterController(LegendaryTitleFilter filter)
  {
    _filter=filter;
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

    // Category panel
    JPanel categoryPanel=GuiFactory.buildPanel(new FlowLayout());
    categoryPanel.add(GuiFactory.buildLabel("Category:"));
    categoryPanel.add(_category.getComboBox());

    // Global assembly
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
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
      @Override
      public void itemSelected(String category)
      {
        _filter.setCategory(category);
        filterUpdated();
      }
    };
    ctrl.addListener(l);
    return ctrl;
  }

  private List<String> getCategories()
  {
    LegendaryTitlesManager titlesMgr=LegendaryTitlesManager.getInstance();
    Set<String> categories=new HashSet<String>();
    for(LegendaryTitle title : titlesMgr.getAll())
    {
      categories.add(title.getCategory());
    }
    List<String> ret=new ArrayList<String>(categories);
    Collections.sort(ret);
    return ret;
  }

  private JTextField buildReactiveTextField()
  {
    final JTextField textField=GuiFactory.buildTextField("");
    textField.setColumns(20);
    DocumentListener dl=new DocumentListener()
    {
      @Override
      public void removeUpdate(DocumentEvent e)
      {
        textFieldUpdated(textField);
      }
      @Override
      public void insertUpdate(DocumentEvent e)
      {
        textFieldUpdated(textField);
      }
      @Override
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
    filterUpdated();
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
    _category=null;
    _nameContains=null;
    _statsContains=null;
  }
}
