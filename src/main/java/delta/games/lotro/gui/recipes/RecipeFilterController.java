package delta.games.lotro.gui.recipes;

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
import delta.games.lotro.gui.items.FilterUpdateListener;
import delta.games.lotro.lore.crafting.recipes.Recipe;
import delta.games.lotro.lore.crafting.recipes.filters.RecipeCategoryFilter;
import delta.games.lotro.lore.crafting.recipes.filters.RecipeNameFilter;
import delta.games.lotro.lore.crafting.recipes.filters.RecipeProfessionFilter;
import delta.games.lotro.lore.crafting.recipes.filters.RecipeTierFilter;

/**
 * Controller for a recipe filter edition panel.
 * @author DAM
 */
public class RecipeFilterController implements ActionListener
{
  // Data
  private RecipeFilter _filter;
  // GUI
  private JPanel _panel;
  private JButton _reset;
  // -- Recipe attributes UI --
  private JTextField _contains;
  private ComboBoxController<String> _profession;
  private ComboBoxController<Integer> _tier;
  private ComboBoxController<String> _category;
  // Controllers
  private DynamicTextEditionController _textController;
  private FilterUpdateListener _filterUpdateListener;

  /**
   * Constructor.
   * @param filter Managed filter.
   * @param filterUpdateListener Filter update listener.
   */
  public RecipeFilterController(RecipeFilter filter, FilterUpdateListener filterUpdateListener)
  {
    _filter=filter;
    _filterUpdateListener=filterUpdateListener;
  }

  /**
   * Get the managed filter.
   * @return the managed filter.
   */
  public Filter<Recipe> getFilter()
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
      _profession.selectItem(null);
      _tier.selectItem(null);
      _category.selectItem(null);
      _contains.setText("");
    }
  }

  private void setFilter()
  {
    // Name
    RecipeNameFilter nameFilter=_filter.getNameFilter();
    String contains=nameFilter.getPattern();
    if (contains!=null)
    {
      _contains.setText(contains);
    }
    // Profession
    RecipeProfessionFilter professionFilter=_filter.getProfessionFilter();
    String profession=professionFilter.getProfession();
    _profession.selectItem(profession);
    // Tier
    RecipeTierFilter tierFilter=_filter.getTierFilter();
    Integer tier=tierFilter.getTier();
    _tier.selectItem(tier);
    // Category
    RecipeCategoryFilter categoryFilter=_filter.getCategoryFilter();
    String category=categoryFilter.getCategory();
    _category.selectItem(category);
  }

  private JPanel build()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    int y=0;

    // Recipe attributes
    JPanel recipePanel=buildRecipePanel();
    Border border=GuiFactory.buildTitledBorder("Recipe");
    recipePanel.setBorder(border);
    GridBagConstraints c=new GridBagConstraints(0,y,1,1,0.0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(recipePanel,c);

    // Reset
    _reset=GuiFactory.buildButton("Reset");
    _reset.addActionListener(this);
    c=new GridBagConstraints(1,y,1,1,0.0,0,GridBagConstraints.SOUTHWEST,GridBagConstraints.NONE,new Insets(0,5,5,5),0,0);
    panel.add(_reset,c);
    y++;

    return panel;
  }

  private JPanel buildRecipePanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    int y=0;
    JPanel line1Panel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING,0,0));
    // Label filter
    {
      JPanel containsPanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING,5,0));
      containsPanel.add(GuiFactory.buildLabel("Name filter:"));
      _contains=GuiFactory.buildTextField("");
      _contains.setColumns(20);
      containsPanel.add(_contains);
      TextListener listener=new TextListener()
      {
        @Override
        public void textChanged(String newText)
        {
          if (newText.length()==0) newText=null;
          RecipeNameFilter nameFilter=_filter.getNameFilter();
          nameFilter.setPattern(newText);
          filterUpdated();
        }
      };
      _textController=new DynamicTextEditionController(_contains,listener);
      line1Panel.add(containsPanel);
    }
    GridBagConstraints c=new GridBagConstraints(0,y,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,5,0),0,0);
    panel.add(line1Panel,c);
    y++;

    JPanel line2Panel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING,5,0));
    // Profession
    {
      JLabel label=GuiFactory.buildLabel("Profession:");
      line2Panel.add(label);
      _profession=RecipeUiUtils.buildProfessionCombo();
      ItemSelectionListener<String> professionListener=new ItemSelectionListener<String>()
      {
        @Override
        public void itemSelected(String profession)
        {
          RecipeProfessionFilter professionFilter=_filter.getProfessionFilter();
          professionFilter.setProfession(profession);
          filterUpdated();
        }
      };
      _profession.addListener(professionListener);
      line2Panel.add(_profession.getComboBox());
    }
    // Type
    {
      JLabel label=GuiFactory.buildLabel("Tier:");
      line2Panel.add(label);
      _tier=RecipeUiUtils.buildTierCombo();
      ItemSelectionListener<Integer> typeListener=new ItemSelectionListener<Integer>()
      {
        @Override
        public void itemSelected(Integer tier)
        {
          RecipeTierFilter tierFilter=_filter.getTierFilter();
          tierFilter.setTier(tier);
          filterUpdated();
        }
      };
      _tier.addListener(typeListener);
      line2Panel.add(_tier.getComboBox());
    }
    // Category
    {
      JLabel label=GuiFactory.buildLabel("Category:");
      line2Panel.add(label);
      _category=RecipeUiUtils.buildCategoryCombo();
      ItemSelectionListener<String> categoryListener=new ItemSelectionListener<String>()
      {
        @Override
        public void itemSelected(String category)
        {
          RecipeCategoryFilter categoryFilter=_filter.getCategoryFilter();
          categoryFilter.setCategory(category);
          filterUpdated();
        }
      };
      _category.addListener(categoryListener);
      line2Panel.add(_category.getComboBox());
    }
    c=new GridBagConstraints(0,y,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,5,0),0,0);
    panel.add(line2Panel,c);
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
    if (_profession!=null)
    {
      _profession.dispose();
      _profession=null;
    }
    if (_tier!=null)
    {
      _tier.dispose();
      _tier=null;
    }
    if (_category!=null)
    {
      _category.dispose();
      _category=null;
    }
    _contains=null;
    _reset=null;
  }
}
