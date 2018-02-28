package delta.games.lotro.gui.deed;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;
import javax.swing.JTextField;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.combobox.ComboBoxController;
import delta.common.ui.swing.combobox.ItemSelectionListener;
import delta.common.ui.swing.text.DynamicTextEditionController;
import delta.common.ui.swing.text.TextListener;
import delta.common.utils.collections.filters.Filter;
import delta.games.lotro.common.VirtueId;
import delta.games.lotro.common.rewards.filters.TitleRewardFilter;
import delta.games.lotro.common.rewards.filters.VirtueRewardFilter;
import delta.games.lotro.lore.deeds.DeedDescription;
import delta.games.lotro.lore.deeds.DeedType;
import delta.games.lotro.lore.deeds.filters.DeedCategoryFilter;
import delta.games.lotro.lore.deeds.filters.DeedNameFilter;
import delta.games.lotro.lore.deeds.filters.DeedTypeFilter;

/**
 * Controller for a deed filter edition panel.
 * @author DAM
 */
public class DeedFilterController
{
  // Data
  private DeedFilter _filter;
  // GUI
  private JPanel _panel;
  private JTextField _contains;
  private ComboBoxController<DeedType> _type;
  private ComboBoxController<String> _category;
  private ComboBoxController<String> _title;
  private ComboBoxController<VirtueId> _virtue;
  // Controllers
  private DynamicTextEditionController _textController;
  private DeedExplorerPanelController _panelController;

  /**
   * Constructor.
   * @param filter Managed filter.
   * @param panelController Deed explorer panel.
   */
  public DeedFilterController(DeedFilter filter, DeedExplorerPanelController panelController)
  {
    _filter=filter;
    _panelController=panelController;
  }

  /**
   * Get the managed filter.
   * @return the managed filter.
   */
  public Filter<DeedDescription> getFilter()
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
    _panelController.filterUpdated();
  }

  private void setFilter()
  {
    // Name
    DeedNameFilter nameFilter=_filter.getNameFilter();
    String contains=nameFilter.getPattern();
    if (contains!=null)
    {
      _contains.setText(contains);
    }
    // Type
    DeedTypeFilter typeFilter=_filter.getTypeFilter();
    DeedType type=typeFilter.getDeedType();
    _type.selectItem(type);
    // Category
    DeedCategoryFilter categoryFilter=_filter.getCategoryFilter();
    String category=categoryFilter.getDeedCategory();
    _category.selectItem(category);
    // Title
    TitleRewardFilter titleFilter=_filter.getTitleFilter();
    String title=titleFilter.getTitle();
    _title.selectItem(title);
    // Virtue
    VirtueRewardFilter virtueFilter=_filter.getVirtueFilter();
    VirtueId virtueId=virtueFilter.getVirtueId();
    _virtue.selectItem(virtueId);
  }

  private JPanel build()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    JPanel line1Panel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING,5,0));
    // Label filter
    {
      JPanel containsPanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING));
      containsPanel.add(GuiFactory.buildLabel("Label filter:"));
      _contains=GuiFactory.buildTextField("");
      _contains.setColumns(20);
      containsPanel.add(_contains);
      TextListener listener=new TextListener()
      {
        public void textChanged(String newText)
        {
          if (newText.length()==0) newText=null;
          DeedNameFilter nameFilter=_filter.getNameFilter();
          nameFilter.setPattern(newText);
          filterUpdated();
        }
      };
      _textController=new DynamicTextEditionController(_contains,listener);
      line1Panel.add(containsPanel);
    }
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(line1Panel,c);

    JPanel line2Panel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING,5,0));
    // Type
    {
      _type=DeedUiUtils.buildDeedTypeCombo();
      ItemSelectionListener<DeedType> typeListener=new ItemSelectionListener<DeedType>()
      {
        @Override
        public void itemSelected(DeedType type)
        {
          DeedTypeFilter typeFilter=_filter.getTypeFilter();
          typeFilter.setDeedType(type);
          filterUpdated();
        }
      };
      _type.addListener(typeListener);
      line2Panel.add(_type.getComboBox());
    }
    // Category
    {
      _category=DeedUiUtils.buildCategoryCombo();
      ItemSelectionListener<String> categoryListener=new ItemSelectionListener<String>()
      {
        @Override
        public void itemSelected(String category)
        {
          DeedCategoryFilter categoryFilter=_filter.getCategoryFilter();
          categoryFilter.setDeedCategory(category);
          filterUpdated();
        }
      };
      _category.addListener(categoryListener);
      line2Panel.add(_category.getComboBox());
    }
    c=new GridBagConstraints(0,1,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(line2Panel,c);

    JPanel line3Panel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING,5,0));
    // Title
    {
      _title=DeedUiUtils.buildTitleCombo();
      ItemSelectionListener<String> titleListener=new ItemSelectionListener<String>()
      {
        @Override
        public void itemSelected(String title)
        {
          TitleRewardFilter titleFilter=_filter.getTitleFilter();
          titleFilter.setTitle(title);
          filterUpdated();
        }
      };
      _title.addListener(titleListener);
      line3Panel.add(_title.getComboBox());
    }
    // Virtue
    {
      _virtue=DeedUiUtils.buildVirtueCombo();
      ItemSelectionListener<VirtueId> virtueListener=new ItemSelectionListener<VirtueId>()
      {
        @Override
        public void itemSelected(VirtueId virtueId)
        {
          VirtueRewardFilter virtueFilter=_filter.getVirtueFilter();
          virtueFilter.setVirtueId(virtueId);
          filterUpdated();
        }
      };
      _virtue.addListener(virtueListener);
      line3Panel.add(_virtue.getComboBox());
    }
    c=new GridBagConstraints(0,2,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(line3Panel,c);
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
    if (_type!=null)
    {
      _type.dispose();
      _type=null;
    }
    if (_category!=null)
    {
      _category.dispose();
      _category=null;
    }
    if (_title!=null)
    {
      _title.dispose();
      _title=null;
    }
    if (_virtue!=null)
    {
      _virtue.dispose();
      _virtue=null;
    }
    _contains=null;
  }
}
