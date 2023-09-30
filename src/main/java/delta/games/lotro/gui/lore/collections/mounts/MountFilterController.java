package delta.games.lotro.gui.lore.collections.mounts;

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
import delta.common.utils.collections.filters.Filter;
import delta.games.lotro.common.enums.MountType;
import delta.games.lotro.common.enums.SkillCharacteristicSubCategory;
import delta.games.lotro.common.filters.NamedFilter;
import delta.games.lotro.gui.lore.items.FilterUpdateListener;
import delta.games.lotro.gui.utils.l10n.Labels;
import delta.games.lotro.lore.collections.mounts.MountDescription;
import delta.games.lotro.lore.collections.mounts.filters.MountCategoryFilter;
import delta.games.lotro.lore.collections.mounts.filters.MountTypeFilter;

/**
 * Controller for a mount filter edition panel.
 * @author DAM
 */
public class MountFilterController implements ActionListener
{
  // Data
  private MountFilter _filter;
  // GUI
  private JPanel _panel;
  private JButton _reset;
  // -- Mount attributes UI --
  private JTextField _contains;
  private ComboBoxController<SkillCharacteristicSubCategory> _category;
  private ComboBoxController<MountType> _mountType;
  // Controllers
  private DynamicTextEditionController _textController;
  private FilterUpdateListener _filterUpdateListener;

  /**
   * Constructor.
   * @param filter Managed filter.
   * @param filterUpdateListener Filter update listener.
   */
  public MountFilterController(MountFilter filter, FilterUpdateListener filterUpdateListener)
  {
    _filter=filter;
    _filterUpdateListener=filterUpdateListener;
  }

  /**
   * Get the managed filter.
   * @return the managed filter.
   */
  public Filter<MountDescription> getFilter()
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
      _category.selectItem(null);
      _mountType.selectItem(null);
      _contains.setText("");
    }
  }

  private void setFilter()
  {
    // Name
    NamedFilter<MountDescription> nameFilter=_filter.getNameFilter();
    String contains=nameFilter.getPattern();
    if (contains!=null)
    {
      _contains.setText(contains);
    }
    // Category
    MountCategoryFilter categoryFilter=_filter.getCategoryFilter();
    SkillCharacteristicSubCategory category=categoryFilter.getCategory();
    _category.selectItem(category);
    // Mount type
    MountTypeFilter typeFilter=_filter.getTypeFilter();
    MountType type=typeFilter.getType();
    _mountType.selectItem(type);
  }

  private JPanel build()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    int y=0;

    // Mount attributes
    JPanel mountPanel=buildMountPanel();
    Border border=GuiFactory.buildTitledBorder("Mount"); // 18n
    mountPanel.setBorder(border);
    GridBagConstraints c=new GridBagConstraints(0,y,1,1,0.0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(mountPanel,c);

    // Reset
    _reset=GuiFactory.buildButton(Labels.getLabel("shared.reset"));
    _reset.addActionListener(this);
    c=new GridBagConstraints(1,y,1,1,0.0,0,GridBagConstraints.SOUTHWEST,GridBagConstraints.NONE,new Insets(0,5,5,5),0,0);
    panel.add(_reset,c);
    y++;

    return panel;
  }

  private JPanel buildMountPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    int y=0;
    JPanel line1Panel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING,5,0));
    // Label filter
    {
      line1Panel.add(GuiFactory.buildLabel("Name filter:")); // 18n
      _contains=GuiFactory.buildTextField("");
      _contains.setColumns(20);
      line1Panel.add(_contains);
      TextListener listener=new TextListener()
      {
        @Override
        public void textChanged(String newText)
        {
          if (newText.length()==0) newText=null;
          NamedFilter<MountDescription> nameFilter=_filter.getNameFilter();
          nameFilter.setPattern(newText);
          filterUpdated();
        }
      };
      _textController=new DynamicTextEditionController(_contains,listener);
    }
    // Category
    {
      JLabel label=GuiFactory.buildLabel("Category:"); // 18n
      line1Panel.add(label);
      _category=MountUiUtils.buildCategoryCombo();
      ItemSelectionListener<SkillCharacteristicSubCategory> categoryListener=new ItemSelectionListener<SkillCharacteristicSubCategory>()
      {
        @Override
        public void itemSelected(SkillCharacteristicSubCategory category)
        {
          MountCategoryFilter categoryFilter=_filter.getCategoryFilter();
          categoryFilter.setCategory(category);
          filterUpdated();
        }
      };
      _category.addListener(categoryListener);
      line1Panel.add(_category.getComboBox());
    }
    // Type
    {
      JLabel label=GuiFactory.buildLabel("Type:"); // 18n
      line1Panel.add(label);
      _mountType=MountUiUtils.buildTypeCombo();
      ItemSelectionListener<MountType> typeListener=new ItemSelectionListener<MountType>()
      {
        @Override
        public void itemSelected(MountType type)
        {
          MountTypeFilter typeFilter=_filter.getTypeFilter();
          typeFilter.setType(type);
          filterUpdated();
        }
      };
      _mountType.addListener(typeListener);
      line1Panel.add(_mountType.getComboBox());
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
    if (_mountType!=null)
    {
      _mountType.dispose();
      _mountType=null;
    }
    _contains=null;
    _reset=null;
  }
}
