package delta.games.lotro.gui.lore.crafting.recipes;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.combobox.ComboBoxController;
import delta.common.ui.swing.combobox.ItemSelectionListener;
import delta.common.ui.swing.text.DynamicTextEditionController;
import delta.common.ui.swing.text.TextListener;
import delta.common.utils.collections.filters.Filter;
import delta.games.lotro.common.enums.CraftingUICategory;
import delta.games.lotro.gui.common.crafting.CraftingUiUtils;
import delta.games.lotro.gui.lore.items.FilterUpdateListener;
import delta.games.lotro.gui.utils.SharedUiUtils;
import delta.games.lotro.gui.utils.l10n.Labels;
import delta.games.lotro.lore.crafting.Profession;
import delta.games.lotro.lore.crafting.recipes.Recipe;
import delta.games.lotro.lore.crafting.recipes.filters.RecipeCategoryFilter;
import delta.games.lotro.lore.crafting.recipes.filters.RecipeFilter;
import delta.games.lotro.lore.crafting.recipes.filters.RecipeHasCooldownFilter;
import delta.games.lotro.lore.crafting.recipes.filters.RecipeIngredientFilter;
import delta.games.lotro.lore.crafting.recipes.filters.RecipeIsGuildFilter;
import delta.games.lotro.lore.crafting.recipes.filters.RecipeNameFilter;
import delta.games.lotro.lore.crafting.recipes.filters.RecipeProfessionFilter;
import delta.games.lotro.lore.crafting.recipes.filters.RecipeSingleUseFilter;
import delta.games.lotro.lore.crafting.recipes.filters.RecipeTierFilter;

/**
 * Controller for a recipe filter edition panel.
 * @author DAM
 */
public class RecipeFilterController implements ActionListener
{
  // Data
  private List<Recipe> _recipes;
  private RecipeFilter _filter;
  // GUI
  private JPanel _panel;
  private JButton _reset;
  // -- Recipe attributes UI --
  private JTextField _contains;
  private ComboBoxController<Profession> _profession;
  private ComboBoxController<Integer> _tier;
  private ComboBoxController<CraftingUICategory> _category;
  private ComboBoxController<Integer> _ingredient;
  private ComboBoxController<Boolean> _singleUse;
  private ComboBoxController<Boolean> _cooldown;
  private ComboBoxController<Boolean> _guild;
  // Controllers
  private DynamicTextEditionController _textController;
  private FilterUpdateListener _filterUpdateListener;

  /**
   * Constructor.
   * @param recipes Recipes to use.
   * @param filter Managed filter.
   * @param filterUpdateListener Filter update listener.
   */
  public RecipeFilterController(List<Recipe> recipes, RecipeFilter filter, FilterUpdateListener filterUpdateListener)
  {
    _recipes=new ArrayList<Recipe>(recipes);
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
      _contains.setText("");
      _profession.selectItem(null);
      _tier.selectItem(null);
      _category.selectItem(null);
      _ingredient.selectItem(null);
      _singleUse.selectItem(null);
      _cooldown.selectItem(null);
      _guild.selectItem(null);
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
    Profession profession=professionFilter.getProfession();
    _profession.selectItem(profession);
    // Tier
    RecipeTierFilter tierFilter=_filter.getTierFilter();
    Integer tier=tierFilter.getTier();
    _tier.selectItem(tier);
    // Category
    RecipeCategoryFilter categoryFilter=_filter.getCategoryFilter();
    CraftingUICategory category=categoryFilter.getCategory();
    _category.selectItem(category);
    // Ingredient
    RecipeIngredientFilter ingredientFilter=_filter.getIngredientFilter();
    Integer itemId=ingredientFilter.getItemId();
    _ingredient.selectItem(itemId);
    // Single Use
    RecipeSingleUseFilter singleUseFilter=_filter.getSingleUseFilter();
    _singleUse.selectItem(singleUseFilter.getSingleUseFlag());
    // Cooldown
    RecipeHasCooldownFilter cooldownFilter=_filter.getCooldownFilter();
    _cooldown.selectItem(cooldownFilter.getCooldownFlag());
    // Guild
    RecipeIsGuildFilter guildFilter=_filter.getGuildFilter();
    _guild.selectItem(guildFilter.getGuildFlag());
  }

  private JPanel build()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    int y=0;

    // Recipe attributes
    JPanel recipePanel=buildRecipePanel();
    Border border=GuiFactory.buildTitledBorder("Recipe"); // 18n
    recipePanel.setBorder(border);
    GridBagConstraints c=new GridBagConstraints(0,y,1,1,0.0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(recipePanel,c);

    // Reset
    _reset=GuiFactory.buildButton(Labels.getLabel("shared.reset"));
    _reset.addActionListener(this);
    c=new GridBagConstraints(1,y,1,1,0.0,0,GridBagConstraints.SOUTHWEST,GridBagConstraints.NONE,new Insets(0,5,5,5),0,0);
    panel.add(_reset,c);
    y++;

    return panel;
  }

  private JPanel buildRecipePanel()
  {
    buildNameFilter();
    buildProfessionFilter();
    buildTierFilter();
    buildCategoryFilter();
    _ingredient=buildIngredientsCombobox();
    _singleUse=buildSingleUseCombobox();
    _cooldown=buildCooldownCombobox();
    _guild=buildGuildCombobox();

    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    int y=0;
    // Label filter
    JPanel line1Panel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING,5,0));
    line1Panel.add(GuiFactory.buildLabel("Name filter:")); // 18n
    line1Panel.add(_contains);
    // Profession
    line1Panel.add(GuiFactory.buildLabel("Profession:")); // 18n
    line1Panel.add(_profession.getComboBox());
    // Tier
    line1Panel.add(GuiFactory.buildLabel("Tier:")); // 18n
    line1Panel.add(_tier.getComboBox());

    GridBagConstraints c=new GridBagConstraints(0,y,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,5,0),0,0);
    panel.add(line1Panel,c);
    y++;

    JPanel line2Panel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING,5,0));
    // Category
    line2Panel.add(GuiFactory.buildLabel("Category:")); // 18n
    line2Panel.add(_category.getComboBox());
    // Ingredient
    line2Panel.add(GuiFactory.buildLabel("Ingredient:")); // 18n
    line2Panel.add(_ingredient.getComboBox());
    c=new GridBagConstraints(0,y,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,5,0),0,0);
    panel.add(line2Panel,c);
    y++;

    JPanel line3Panel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING,5,0));
    // Single Use
    line3Panel.add(GuiFactory.buildLabel("Single Use:")); // 18n
    line3Panel.add(_singleUse.getComboBox());
    // Cooldown
    line3Panel.add(GuiFactory.buildLabel("Cooldown:")); // 18n
    line3Panel.add(_cooldown.getComboBox());
    // Guild
    line3Panel.add(GuiFactory.buildLabel("Guild:")); // 18n
    line3Panel.add(_guild.getComboBox());
    c=new GridBagConstraints(0,y,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,5,0),0,0);
    panel.add(line3Panel,c);
    y++;

    return panel;
  }

  private void buildNameFilter()
  {
    _contains=GuiFactory.buildTextField("");
    _contains.setColumns(20);
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
  }

  private void buildProfessionFilter()
  {
    _profession=CraftingUiUtils.buildProfessionCombo(getProfessions(),true);
    ItemSelectionListener<Profession> professionListener=new ItemSelectionListener<Profession>()
    {
      @Override
      public void itemSelected(Profession profession)
      {
        RecipeProfessionFilter professionFilter=_filter.getProfessionFilter();
        professionFilter.setProfession(profession);
        filterUpdated();
      }
    };
    _profession.addListener(professionListener);
  }

  private List<Profession> getProfessions()
  {
    Set<Profession> ret=new HashSet<Profession>();
    for(Recipe recipe : _recipes)
    {
      ret.add(recipe.getProfession());
    }
    return new ArrayList<Profession>(ret);
  }

  private void buildTierFilter()
  {
    _tier=CraftingUiUtils.buildTierCombo();
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
  }

  private void buildCategoryFilter()
  {
    _category=RecipeUiUtils.buildCategoryCombo(_recipes);
    ItemSelectionListener<CraftingUICategory> categoryListener=new ItemSelectionListener<CraftingUICategory>()
    {
      @Override
      public void itemSelected(CraftingUICategory category)
      {
        RecipeCategoryFilter categoryFilter=_filter.getCategoryFilter();
        categoryFilter.setCategory(category);
        filterUpdated();
      }
    };
    _category.addListener(categoryListener);
  }

  private ComboBoxController<Integer> buildIngredientsCombobox()
  {
    ComboBoxController<Integer> combo=RecipeUiUtils.buildIngredientsCombo(_recipes);
    ItemSelectionListener<Integer> listener=new ItemSelectionListener<Integer>()
    {
      @Override
      public void itemSelected(Integer itemId)
      {
        RecipeIngredientFilter filter=_filter.getIngredientFilter();
        filter.setItemId(itemId);
        filterUpdated();
      }
    };
    combo.addListener(listener);
    return combo;
  }

  private ComboBoxController<Boolean> buildSingleUseCombobox()
  {
    ComboBoxController<Boolean> combo=SharedUiUtils.build3StatesBooleanCombobox();
    ItemSelectionListener<Boolean> listener=new ItemSelectionListener<Boolean>()
    {
      @Override
      public void itemSelected(Boolean value)
      {
        RecipeSingleUseFilter filter=_filter.getSingleUseFilter();
        filter.setSingleUseFlag(value);
        filterUpdated();
      }
    };
    combo.addListener(listener);
    return combo;
  }

  private ComboBoxController<Boolean> buildCooldownCombobox()
  {
    ComboBoxController<Boolean> combo=SharedUiUtils.build3StatesBooleanCombobox();
    ItemSelectionListener<Boolean> listener=new ItemSelectionListener<Boolean>()
    {
      @Override
      public void itemSelected(Boolean value)
      {
        RecipeHasCooldownFilter filter=_filter.getCooldownFilter();
        filter.setCooldownFlag(value);
        filterUpdated();
      }
    };
    combo.addListener(listener);
    return combo;
  }

  private ComboBoxController<Boolean> buildGuildCombobox()
  {
    ComboBoxController<Boolean> combo=SharedUiUtils.build3StatesBooleanCombobox();
    ItemSelectionListener<Boolean> listener=new ItemSelectionListener<Boolean>()
    {
      @Override
      public void itemSelected(Boolean value)
      {
        RecipeIsGuildFilter filter=_filter.getGuildFilter();
        filter.setGuildFlag(value);
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
    if (_ingredient!=null)
    {
      _ingredient.dispose();
      _ingredient=null;
    }
    if (_singleUse!=null)
    {
      _singleUse.dispose();
      _singleUse=null;
    }
    if (_cooldown!=null)
    {
      _cooldown.dispose();
      _cooldown=null;
    }
    if (_guild!=null)
    {
      _guild.dispose();
      _guild=null;
    }
    _contains=null;
    _reset=null;
  }
}
