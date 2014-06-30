package delta.games.lotro.lore.recipes;

import java.util.ArrayList;
import java.util.List;

import delta.common.utils.text.EndOfLine;

/**
 * Recipe description.
 * @author DAM
 */
public class Recipe
{
  private int _identifier;
  private String _key;
  private String _name;
  private String _profession;
  private int _tier;
  private boolean _oneTimeUse;
  private List<Ingredient> _ingredients;
  private List<RecipeVersion> _versions;
  private ItemReference _recipeScroll;
  
  /**
   * Constructor.
   */
  public Recipe()
  {
    _identifier=0;
    _key=null;
    _name=null;
    _profession=null;
    _tier=1;
    _oneTimeUse=false;
    _ingredients=new ArrayList<Ingredient>();
    _versions=new ArrayList<RecipeVersion>();
    _recipeScroll=null;
  }

  /**
   * Get the identifier of this recipe.
   * @return a recipe identifier.
   */
  public int getIdentifier()
  {
    return _identifier;
  }

  /**
   * Set the identifier of this recipe.
   * @param identifier the identifier to set.
   */
  public void setIdentifier(int identifier)
  {
    _identifier=identifier;
  }

  /**
   * Get the key of this recipe.
   * @return a recipe key.
   */
  public String getKey()
  {
    return _key;
  }

  /**
   * Set the key of this recipe.
   * @param key the key to set.
   */
  public void setKey(String key)
  {
    _key=key;
  }

  /**
   * Get the name of this recipe.
   * @return a recipe name.
   */
  public String getName()
  {
    return _name;
  }

  /**
   * Set the name of this recipe.
   * @param name the name to set.
   */
  public void setName(String name)
  {
    _name=name;
  }

  /**
   * Get the profession of this recipe.
   * @return a profession identifier.
   */
  public String getProfession()
  {
    return _profession;
  }

  /**
   * Set the profession of this recipe.
   * @param profession the profession to set.
   */
  public void setProfession(String profession)
  {
    _profession=profession;
  }

  /**
   * Get the recipe tier.
   * @return A tier.
   */
  public int getTier()
  {
    return _tier;
  }

  /**
   * Set the recipe tier.
   * @param tier the tier to set.
   */
  public void setTier(int tier)
  {
    _tier=tier;
  }

  /**
   * Indicates if this recipe can only be used once or not.
   * @return <code>true</code> if it is used once, <code>false</code> otherwise.
   */
  public boolean isOneTimeUse()
  {
    return _oneTimeUse;
  }

  /**
   * Set the 'one time use' flag for this recipe.
   * @param oneTimeUse the value to set.
   */
  public void setOneTimeUse(boolean oneTimeUse)
  {
    _oneTimeUse=oneTimeUse;
  }

  /**
   * Get the ingredients for this recipes.
   * @return a list of ingredients.
   */
  public List<Ingredient> getIngredients()
  {
    return _ingredients;
  }

  /**
   * Set the ingredients for this recipe.
   * @param ingredients the ingredients to set.
   */
  public void setIngredients(List<Ingredient> ingredients)
  {
    _ingredients.clear();
    if (ingredients!=null)
    {
      _ingredients.addAll(ingredients);
    }
  }

  /**
   * Get the versions of this recipe.
   * @return a list of recipe versions.
   */
  public List<RecipeVersion> getVersions()
  {
    return _versions;
  }

  /**
   * Set the version of this recipe.
   * @param versions the versions to set.
   */
  public void setVersions(List<RecipeVersion> versions)
  {
    _versions.clear();
    if (versions!=null)
    {
      _versions.addAll(versions);
    }
  }

  /**
   * Get the reference to the recipe scroll item.
   * @return an item reference or <code>null</code>.
   */
  public ItemReference getRecipeScroll()
  {
    return _recipeScroll;
  }

  /**
   * Set the recipe scroll item reference.
   * @param recipeScroll the reference to set.
   */
  public void setRecipeScroll(ItemReference recipeScroll)
  {
    _recipeScroll=recipeScroll;
  }

  /**
   * Dump the contents of this item as a string.
   * @return A readable string.
   */
  public String dump()
  {
    StringBuilder sb=new StringBuilder();
    sb.append("Name: ").append(_name);
    if (_identifier!=0)
    {
      sb.append(" (id=");
      sb.append(_identifier);
      sb.append(')');
    }
    if (_key!=null)
    {
      sb.append(" (key=");
      sb.append(_key);
      sb.append(')');
    }
    if (_profession!=null)
    {
      sb.append(" (profession=");
      sb.append(_profession);
      sb.append(')');
    }
    if (_tier!=0)
    {
      sb.append(" (tier=");
      sb.append(_tier);
      sb.append(')');
    }
    if (_oneTimeUse)
    {
      sb.append(" (one time use)");
    }
    if (_recipeScroll!=null)
    {
      sb.append(" (scroll=");
      sb.append(_recipeScroll);
      sb.append(')');
    }
    sb.append(EndOfLine.NATIVE_EOL);
    sb.append("Ingredients:");
    sb.append(EndOfLine.NATIVE_EOL);
    for(Ingredient ingredient : _ingredients)
    {
      sb.append('\t').append(ingredient);
      sb.append(EndOfLine.NATIVE_EOL);
    }
    sb.append("Results:");
    sb.append(EndOfLine.NATIVE_EOL);
    for(RecipeVersion results : _versions)
    {
      sb.append('\t').append(results);
      sb.append(EndOfLine.NATIVE_EOL);
    }
    return sb.toString().trim();
  }

  @Override
  public String toString() {
    return _name;
  }

  /**
   * Item reference.
   * @author DAM
   */
  public static class ItemReference
  {
    private int _itemId;
    private String _itemKey;
    private String _name;
    private String _icon;
    
    /**
     * Constructor.
     */
    public ItemReference()
    {
      _itemId=0;
      _itemKey=null;
      _name=null;
      _icon=null;
    }

    /**
     * Get the item identifier.
     * @return an item key.
     */
    public int getItemId()
    {
      return _itemId;
    }

    /**
     * Set the item identifier.
     * @param itemId the identifier to set.
     */
    public void setItemId(int itemId)
    {
      _itemId=itemId;
    }

    /**
     * Get the item key.
     * @return an item key.
     */
    public String getItemKey()
    {
      return _itemKey;
    }

    /**
     * Set the item key.
     * @param itemKey the key to set.
     */
    public void setItemKey(String itemKey)
    {
      _itemKey=itemKey;
    }

    /**
     * Get the name of the referenced item.
     * @return an item name.
     */
    public String getName()
    {
      return _name;
    }

    /**
     * Set the name of the referenced item.
     * @param name the name to set.
     */
    public void setName(String name)
    {
      _name=name;
    }

    /**
     * Get the icon of the referenced item.
     * @return an icon path.
     */
    public String getIcon()
    {
      return _icon;
    }

    /**
     * Set the icon of the referenced item.
     * @param icon the icon path to set.
     */
    public void setIcon(String icon)
    {
      _icon=icon;
    }

    @Override
    public String toString()
    {
      return _name+" ("+_itemId+") ("+_itemKey+") ("+_icon+")";
    }
  }

  /**
   * Ingredient of a recipe.
   * @author DAM
   */
  public static class Ingredient
  {
    private int _quantity;
    private boolean _optional;
    private ItemReference _item;

    /**
     * Constructor.
     */
    public Ingredient()
    {
      _quantity=1;
      _optional=false;
      _item=null;
    }

    /**
     * Get the quantity for this ingredient.
     * @return A quantity.
     */
    public int getQuantity()
    {
      return _quantity;
    }
    
    /**
     * Set the quantity for this ingredient.
     * @param quantity the quantity to set.
     */
    public void setQuantity(int quantity)
    {
      _quantity=quantity;
    }

    /**
     * Indicates if this ingredient is optional or not.
     * @return <code>true</code> if this ingredient is optional, <code>false</code> if it is mandatory.
     */
    public boolean isOptional()
    {
      return _optional;
    }

    /**
     * Set the value of the 'optional' flags.
     * @param optional <code>true</code> means 'optional', <code>false</code> means 'mandatory'.
     */
    public void setOptional(boolean optional)
    {
      _optional=optional;
    }

    /**
     * Get the ingredient item reference.
     * @return an item reference.
     */
    public ItemReference getItem()
    {
      return _item;
    }

    /**
     * Set the ingredient item reference.
     * @param item the item reference to set.
     */
    public void setItem(ItemReference item)
    {
      _item=item;
    }
    
    @Override
    public String toString()
    {
      return _item+" x"+_quantity+(_optional?" (optional)":"");
    }
  }

  /**
   * Version of a recipe.
   * @author DAM
   */
  public static class RecipeVersion
  {
    private CraftingResult _regular;
    private CraftingResult _critical;

    /**
     * Get the regular result of this recipe version.
     * @return A result.
     */
    public CraftingResult getRegular()
    {
      return _regular;
    }

    /**
     * Set the regular result for this recipe version.
     * @param regular Result to set.
     */
    public void setRegular(CraftingResult regular)
    {
      _regular=regular;
    }

    /**
     * Get the critical result of this recipe version.
     * @return A result or <code>null</code> if there's none.
     */
    public CraftingResult getCritical()
    {
      return _critical;
    }

    /**
     * Set the critical result for this recipe version.
     * @param critical Result to set (may be <code>null</code>).
     */
    public void setCritical(CraftingResult critical)
    {
      _critical=critical;
    }

    @Override
    public String toString()
    {
      StringBuilder sb=new StringBuilder();
      if (_regular!=null)
      {
        sb.append("Regular: ").append(_regular);
      }
      if (_critical!=null)
      {
        if (sb.length()>0) sb.append(" / ");
        sb.append("Critical: ").append(_critical);
      }
      return sb.toString();
    }
  }

  /**
   * Result of a recipe version.
   * @author DAM
   */
  public static class CraftingResult
  {
    private int _quantity;
    private ItemReference _item;
    private boolean _isCriticalResult;

    /**
     * Get the quantity of result items.
     * @return A quantity.
     */
    public int getQuantity()
    {
      return _quantity;
    }

    /**
     * Set the quantity of result items.
     * @param quantity the quantity to set.
     */
    public void setQuantity(int quantity)
    {
      _quantity=quantity;
    }

    /**
     * Get the result item reference.
     * @return an item reference.
     */
    public ItemReference getItem()
    {
      return _item;
    }

    /**
     * Set the result item reference.
     * @param item the item reference to set.
     */
    public void setItem(ItemReference item)
    {
      _item=item;
    }

    /**
     * Indicates if this is the critical result or
     * the regular one.
     * @return <code>true</code> indicates a critical result,
     * <code>false</code> a regular result.
     */
    public boolean isCriticalResult()
    {
      return _isCriticalResult;
    }

    /**
     * Set the critical result flag.
     * @param isCriticalResult Value to set.
     */
    public void setCriticalResult(boolean isCriticalResult)
    {
      _isCriticalResult=isCriticalResult;
    }
    
    @Override
    public String toString()
    {
      return _item+" x"+_quantity+(_isCriticalResult?" (critical)":"");
    }
  }
}
