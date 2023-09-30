package delta.games.lotro.gui.lore.instances;

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
import delta.common.ui.swing.text.DynamicTextEditionController;
import delta.common.ui.swing.text.TextListener;
import delta.games.lotro.common.enums.WJEncounterCategory;
import delta.games.lotro.gui.lore.items.FilterUpdateListener;
import delta.games.lotro.gui.utils.SharedUiUtils;
import delta.games.lotro.gui.utils.l10n.Labels;
import delta.games.lotro.lore.instances.filters.InstanceTreeEntryCategoryNameFilter;
import delta.games.lotro.lore.instances.filters.PrivateEncounterCategoryFilter;
import delta.games.lotro.lore.instances.filters.PrivateEncounterNameFilter;
import delta.games.lotro.lore.instances.filters.PrivateEncounterScalableFilter;

/**
 * Controller for an instances filter edition panel.
 * @author DAM
 */
public class InstancesFilterController implements ActionListener
{
  // Data
  private InstanceEntriesFilter _filter;
  // GUI
  private JPanel _panel;
  private JButton _reset;
  // -- Instances attributes UI --
  private JTextField _contains;
  private ComboBoxController<String> _group;
  private ComboBoxController<WJEncounterCategory> _category;
  private ComboBoxController<Boolean> _scalable;
  // Controllers
  private DynamicTextEditionController _textController;
  private FilterUpdateListener _filterUpdateListener;

  /**
   * Constructor.
   * @param filter Managed filter.
   * @param filterUpdateListener Filter update listener.
   */
  public InstancesFilterController(InstanceEntriesFilter filter, FilterUpdateListener filterUpdateListener)
  {
    _filter=filter;
    _filterUpdateListener=filterUpdateListener;
  }

  /**
   * Get the managed filter.
   * @return the managed filter.
   */
  public InstanceEntriesFilter getFilter()
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
      _group.selectItem(null);
      _category.selectItem(null);
      _contains.setText("");
      _scalable.selectItem(null);
    }
  }

  private void setFilter()
  {
    // Category name filter:
    InstanceTreeEntryCategoryNameFilter categoryNameFilter=_filter.getNameFilter();
    categoryNameFilter.setCategoryName(null);
    // Instances filter:
    InstancesFilter filter=_filter.getInstancesFilter();
    // Name
    PrivateEncounterNameFilter nameFilter=filter.getNameFilter();
    String contains=nameFilter.getPattern();
    if (contains!=null)
    {
      _contains.setText(contains);
    }
    // Category
    PrivateEncounterCategoryFilter categoryFilter=filter.getCategoryFilter();
    WJEncounterCategory category=categoryFilter.getCategory();
    _category.selectItem(category);
    // Scalable
    PrivateEncounterScalableFilter scalableFilter=filter.getScalableFilter();
    _scalable.selectItem(scalableFilter.getScalable());
  }

  private JPanel build()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    int y=0;

    // Instance attributes
    JPanel instancePanel=buildInstancePanel();
    Border border=GuiFactory.buildTitledBorder("Instance"); // I18n
    instancePanel.setBorder(border);
    GridBagConstraints c=new GridBagConstraints(0,y,1,1,0.0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(instancePanel,c);

    // Reset
    _reset=GuiFactory.buildButton(Labels.getLabel("shared.reset"));
    _reset.addActionListener(this);
    c=new GridBagConstraints(1,y,1,1,0.0,0,GridBagConstraints.SOUTHWEST,GridBagConstraints.NONE,new Insets(0,5,5,5),0,0);
    panel.add(_reset,c);
    y++;

    return panel;
  }

  private JPanel buildInstancePanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    final InstancesFilter filter=_filter.getInstancesFilter();
    int y=0;
    JPanel line1Panel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING,5,0));
    // Label filter
    {
      line1Panel.add(GuiFactory.buildLabel("Name filter:")); // I18n
      _contains=GuiFactory.buildTextField("");
      _contains.setColumns(20);
      line1Panel.add(_contains);
      TextListener listener=new TextListener()
      {
        @Override
        public void textChanged(String newText)
        {
          if (newText.length()==0) newText=null;
          PrivateEncounterNameFilter nameFilter=filter.getNameFilter();
          nameFilter.setPattern(newText);
          filterUpdated();
        }
      };
      _textController=new DynamicTextEditionController(_contains,listener);
    }
    // Group
    {
      JLabel label=GuiFactory.buildLabel("Group:"); // I18n
      line1Panel.add(label);
      _group=InstancesUiUtils.buildInstanceCategoriesCombo();
      ItemSelectionListener<String> categoryListener=new ItemSelectionListener<String>()
      {
        @Override
        public void itemSelected(String category)
        {
          InstanceTreeEntryCategoryNameFilter categoryFilter=_filter.getNameFilter();
          categoryFilter.setCategoryName(category);
          filterUpdated();
        }
      };
      _group.addListener(categoryListener);
      line1Panel.add(_group.getComboBox());
    }
    // Category
    {
      JLabel label=GuiFactory.buildLabel("Category:"); // I18n
      line1Panel.add(label);
      _category=InstancesUiUtils.buildCategoryCombo();
      ItemSelectionListener<WJEncounterCategory> categoryListener=new ItemSelectionListener<WJEncounterCategory>()
      {
        @Override
        public void itemSelected(WJEncounterCategory category)
        {
          PrivateEncounterCategoryFilter categoryFilter=filter.getCategoryFilter();
          categoryFilter.setCategory(category);
          filterUpdated();
        }
      };
      _category.addListener(categoryListener);
      line1Panel.add(_category.getComboBox());
    }
    // Scalable
    {
      JLabel label=GuiFactory.buildLabel("Scalable:"); // I18n
      line1Panel.add(label);
      _scalable=SharedUiUtils.build3StatesBooleanCombobox();
      ItemSelectionListener<Boolean> scalableListener=new ItemSelectionListener<Boolean>()
      {
        @Override
        public void itemSelected(Boolean scalable)
        {
          PrivateEncounterScalableFilter scalableFilter=filter.getScalableFilter();
          scalableFilter.setScalable(scalable);
          filterUpdated();
        }
      };
      _scalable.addListener(scalableListener);
      line1Panel.add(_scalable.getComboBox());
    }
    GridBagConstraints c=new GridBagConstraints(0,y,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,5,0),0,0);
    panel.add(line1Panel,c);
    y++;

    return panel;
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
    if (_category!=null)
    {
      _category.dispose();
      _category=null;
    }
    if (_scalable!=null)
    {
      _scalable.dispose();
      _scalable=null;
    }
    _contains=null;
    _reset=null;
  }
}
