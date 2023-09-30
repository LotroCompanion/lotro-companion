package delta.games.lotro.gui.character.storage.wallet;

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
import delta.games.lotro.common.enums.PaperItemCategory;
import delta.games.lotro.common.filters.NamedFilter;
import delta.games.lotro.gui.utils.SharedUiUtils;
import delta.games.lotro.gui.utils.l10n.Labels;
import delta.games.lotro.lore.items.paper.PaperItem;
import delta.games.lotro.lore.items.paper.filters.PaperItemCategoryFilter;
import delta.games.lotro.lore.items.paper.filters.PaperItemFilter;
import delta.games.lotro.lore.items.paper.filters.PaperItemIsAccountSharedFilter;

/**
 * Controller for a paper item filter edition panel.
 * @author DAM
 */
public class WalletFilterController implements ActionListener
{
  // Data
  private PaperItemFilter _filter;
  // GUI
  private JPanel _panel;
  private JButton _reset;
  // -- Paper item attributes UI --
  private JTextField _contains;
  private ComboBoxController<Boolean> _shared;
  private ComboBoxController<PaperItemCategory> _category;
  // Controllers
  private DynamicTextEditionController _textController;
  private FilterUpdateListener _filterUpdateListener;

  /**
   * Constructor.
   * @param filter Managed filter.
   * @param filterUpdateListener Filter update listener.
   */
  public WalletFilterController(PaperItemFilter filter, FilterUpdateListener filterUpdateListener)
  {
    _filter=filter;
    _filterUpdateListener=filterUpdateListener;
  }

  /**
   * Get the managed filter.
   * @return the managed filter.
   */
  public Filter<PaperItem> getFilter()
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
      _shared.selectItem(null);
      _contains.setText("");
      _category.setSelectedItem(null);
    }
  }

  private void setFilter()
  {
    // Name
    NamedFilter<PaperItem> nameFilter=_filter.getNameFilter();
    String contains=nameFilter.getPattern();
    if (contains!=null)
    {
      _contains.setText(contains);
    }
    // Shared
    PaperItemIsAccountSharedFilter sharedFilter=_filter.getSharedFilter();
    Boolean shared=sharedFilter.getSharedFlag();
    _shared.selectItem(shared);
    // Category
    PaperItemCategoryFilter categoryFilter=_filter.getCategoryFilter();
    PaperItemCategory category=categoryFilter.getCategory();
    _category.selectItem(category);
  }

  private JPanel build()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    int y=0;

    // Paper items attributes
    JPanel paperItemPanel=buildPaperItemPanel();
    Border border=GuiFactory.buildTitledBorder("Filter"); // I18n
    paperItemPanel.setBorder(border);
    GridBagConstraints c=new GridBagConstraints(0,y,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(paperItemPanel,c);

    // Reset
    _reset=GuiFactory.buildButton(Labels.getLabel("shared.reset"));
    _reset.addActionListener(this);
    c=new GridBagConstraints(1,y,1,1,0.0,0,GridBagConstraints.SOUTHWEST,GridBagConstraints.NONE,new Insets(0,5,5,5),0,0);
    panel.add(_reset,c);
    y++;

    return panel;
  }

  private JPanel buildPaperItemPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    JPanel namePanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING,5,0));
    // Label filter
    {
      namePanel.add(GuiFactory.buildLabel("Name:")); // I18n
      _contains=GuiFactory.buildTextField("");
      _contains.setColumns(10);
      namePanel.add(_contains);
      TextListener listener=new TextListener()
      {
        @Override
        public void textChanged(String newText)
        {
          if (newText.length()==0) newText=null;
          NamedFilter<PaperItem> nameFilter=_filter.getNameFilter();
          nameFilter.setPattern(newText);
          filterUpdated();
        }
      };
      _textController=new DynamicTextEditionController(_contains,listener);
    }
    // Shared
    JPanel sharedPanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING,5,0));
    {
      JLabel label=GuiFactory.buildLabel("Shared:"); // I18n
      sharedPanel.add(label);
      _shared=buildSharedCombobox();
      sharedPanel.add(_shared.getComboBox());
    }
    // Category
    JPanel categoryPanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING,5,0));
    {
      JLabel label=GuiFactory.buildLabel("Category:"); // I18n
      categoryPanel.add(label);
      _category=WalletUiUtils.buildCategoryCombo();
      ItemSelectionListener<PaperItemCategory> categoryListener=new ItemSelectionListener<PaperItemCategory>()
      {
        @Override
        public void itemSelected(PaperItemCategory category)
        {
          PaperItemCategoryFilter categoryFilter=_filter.getCategoryFilter();
          categoryFilter.setCategory(category);
          filterUpdated();
        }
      };
      _category.addListener(categoryListener);
      categoryPanel.add(_category.getComboBox());
    }
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(namePanel,c);
    c=new GridBagConstraints(1,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(sharedPanel,c);
    c=new GridBagConstraints(0,1,2,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(5,0,5,0),0,0);
    panel.add(categoryPanel,c);

    return panel;
  }

  private ComboBoxController<Boolean> buildSharedCombobox()
  {
    ComboBoxController<Boolean> combo=SharedUiUtils.build3StatesBooleanCombobox();
    ItemSelectionListener<Boolean> listener=new ItemSelectionListener<Boolean>()
    {
      @Override
      public void itemSelected(Boolean value)
      {
        PaperItemIsAccountSharedFilter filter=_filter.getSharedFilter();
        filter.setShared(value);
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
    if (_shared!=null)
    {
      _shared.dispose();
      _shared=null;
    }
    _contains=null;
    _reset=null;
  }
}
