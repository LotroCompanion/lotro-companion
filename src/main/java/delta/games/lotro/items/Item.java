package delta.games.lotro.items;

import java.util.List;

import delta.common.utils.text.EndOfLine;
import delta.games.lotro.common.Money;

/**
 * Item description.
 * @author DAM
 */
public class Item
{
  // Item identifier: "Jacket_of_the_Impossible_Shot", ...
  private String _id;
  // Item name "Jacket of the Impossible Shot"
  private String _name;
  // Item category: Armour, Tool, ...
  private ItemCategory _category;
  // Item sub-category: "Medium Armour", "Craft Tool"
  private String _subCategory;
  // Item binding: "Bind on Acquire", ...
  private ItemBinding _binding;
  // Is item unique or not?
  private boolean _unique;
  // Bonuses
  private List<String> _bonus;
  // Durability
  private Integer _durability;
  // Sturdiness (may be null)
  private ItemSturdiness _sturdiness;
  // Minimum level (may be null)
  private Integer _minLevel;
  // Class (may be null)
  // TODO Use a class enum
  private String _class;
  // Full description
  private String _description;
  // Value
  private Money _value;
  // Stacking information
  private Integer _stackMax;

  /**
   * Constructor.
   */
  public Item()
  {
    // TODO
  }

  /**
   * Get the identifier of this item.
   * @return an item identifier.
   */
  public String getIdentifier()
  {
    return _id;
  }

  /**
   * Set the identifier of the item.
   * @param id the identifier to set.
   */
  public void setIdentifier(String id)
  {
    _id=id;
  }

  /**
   * Get the name of this item.
   * @return an item name.
   */
  public String getName()
  {
    return _name;
  }

  /**
   * Set the name of this item.
   * @param name the name to set.
   */
  public void setName(String name)
  {
    _name=name;
  }

  /**
   * Get the category of this item.
   * @return an item category.
   */
  public ItemCategory getCategory()
  {
    return _category;
  }

  /**
   * Set the category of this item.
   * @param category the category to set.
   */
  public void setCategory(ItemCategory category)
  {
    _category=category;
  }

  /**
   * Get the sub-category of this item.
   * @return a sub-category.
   */
  public String getSubCategory()
  {
    return _subCategory;
  }

  /**
   * Set the sub-category of this item.
   * @param subCategory the sub-category to set.
   */
  public void setSubCategory(String subCategory)
  {
    _subCategory=subCategory;
  }

  /**
   * Get the item binding.
   * @return an item binding.
   */
  public ItemBinding getBinding()
  {
    return _binding;
  }

  /**
   * Set the binding of this item.
   * @param binding the binding to set.
   */
  public void setBinding(ItemBinding binding)
  {
    _binding=binding;
  }

  /**
   * Indicates if this item is unique or not.
   * @return <code>true</code> if it is, <code>false</code> otherwise.
   */
  public boolean isUnique()
  {
    return _unique;
  }

  /**
   * Set the unicity of this item.
   * @param unique <code>true</code> to make this item unique, <code>false</code> otherwise.
   */
  public void setUnique(boolean unique)
  {
    _unique=unique;
  }

  /**
   * Get the list of bonus for this item.
   * @return a list of bonus.
   */
  public List<String> getBonus()
  {
    // TODO encapsulation
    return _bonus;
  }

  /**
   * Set the list of bonus for this item.
   * @param bonus the bonus to set.
   */
  public void setBonus(List<String> bonus)
  {
    _bonus.clear();
    if (bonus!=null)
    {
      _bonus.addAll(bonus);
    }
  }

  /**
   * Get the durability of this item.
   * @return a durability value.
   */
  public Integer getDurability()
  {
    return _durability;
  }

  /**
   * Set the durability of this item.
   * @param durability the durability to set.
   */
  public void setDurability(Integer durability)
  {
    _durability=durability;
  }

  /**
   * Get the sturdiness of this item.
   * @return a sturdiness value.
   */
  public ItemSturdiness getSturdiness()
  {
    return _sturdiness;
  }

  /**
   * Set the sturdiness of this item.
   * @param sturdiness the sturdiness to set.
   */
  public void setSturdiness(ItemSturdiness sturdiness)
  {
    _sturdiness=sturdiness;
  }

  /**
   * Get the minimum level required to use this item.
   * @return a minimum level value or <code>null</code>.
   */
  public Integer getMinLevel()
  {
    return _minLevel;
  }

  /**
   * Set the minimum level required to use this item.
   * @param minLevel the minimum level as an integer value,
   * or <code>null</code> for no restriction.
   */
  public void setMinLevel(Integer minLevel)
  {
    _minLevel=minLevel;
  }

  /**
   * Get the required class to use this item.
   * @return a class name or <code>null</code>.
   */
  public String getRequiredClass()
  {
    return _class;
  }

  /**
   * Set the required class for this item.
   * @param toonClass a class name or <code>null</code> for no restriction.
   */
  public void setRequiredClass(String toonClass)
  {
    _class=toonClass;
  }

  /**
   * Get description of this item.
   * @return an item description.
   */
  public String getDescription()
  {
    return _description;
  }

  /**
   * Set the description of this item.
   * @param description the description to set.
   */
  public void setDescription(String description)
  {
    _description=description;
  }

  /**
   * Get the value of this item.
   * @return an amount of money.
   */
  public Money getValue()
  {
    return _value;
  }

  /**
   * Set the value of this item.
   * @param value the value to set.
   */
  public void setValue(Money value)
  {
    _value=value;
  }

  /**
   * Get the maximum stackability of this item.
   * @return an integer value or <code>null</code> if it is not stackable.
   */
  public Integer getStackMax()
  {
    return _stackMax;
  }

  /**
   * Set the maximum stackability of this item.
   * @param stackMax the maximum size of stacks as an integer,
   * or <code>null</code> if it is not stackable.
   */
  public void setStackMax(Integer stackMax)
  {
    _stackMax=stackMax;
  }

  /**
   * Dump the contents of this quest as a string.
   * @return A readable string.
   */
  public String dump()
  {
    StringBuilder sb=new StringBuilder();
    sb.append("Name: ").append(_name);
    if (_id!=null)
    {
      sb.append(" (id=");
      sb.append(_id);
      sb.append(')');
    }
    if (_category!=null)
    {
      sb.append(" (");
      sb.append(_category);
      sb.append(')');
    }
    if (_subCategory!=null)
    {
      sb.append(" (");
      sb.append(_subCategory);
      sb.append(')');
    }
    if (_durability!=null)
    {
      sb.append(" (Durability=");
      sb.append(_durability);
      sb.append(')');
    }
    if (_sturdiness!=null)
    {
      sb.append(" (");
      sb.append(_sturdiness);
      sb.append(')');
    }
    if (_unique)
    {
      sb.append(" (unique)");
    }
    if (_binding!=null)
    {
      sb.append(" (");
      sb.append(_binding);
      sb.append(')');
    }
    if (_minLevel!=null)
    {
      sb.append(" (Min level=");
      sb.append(_minLevel);
      sb.append(')');
    }
    if (_value!=null)
    {
      sb.append(" (Value=");
      sb.append(_value);
      sb.append(')');
    }
    if (_stackMax!=null)
    {
      sb.append(" (Stacks=");
      sb.append(_stackMax);
      sb.append(')');
    }
    if (_class!=null)
    {
      sb.append(" (Required class=");
      sb.append(_class);
      sb.append(')');
    }
    sb.append(EndOfLine.NATIVE_EOL);
    if ((_bonus!=null) && (_bonus.size()>0))
    {
      for(String bonus : _bonus)
      {
        sb.append(bonus).append(EndOfLine.NATIVE_EOL);
      }
    }
    sb.append(_description);
    return sb.toString();
  }

  @Override
  public String toString()
  {
    return _name;
  }
}
