package delta.games.lotro.lore.recipes.io.xml;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

import delta.common.utils.xml.DOMParsingTools;
import delta.games.lotro.lore.recipes.Recipe;
import delta.games.lotro.lore.recipes.Recipe.CraftingResult;
import delta.games.lotro.lore.recipes.Recipe.Ingredient;
import delta.games.lotro.lore.recipes.Recipe.ItemReference;
import delta.games.lotro.lore.recipes.Recipe.RecipeVersion;

/**
 * Parser for quest descriptions stored in XML.
 * @author DAM
 */
public class RecipeXMLParser
{
  /**
   * Parse the XML file.
   * @param source Source file.
   * @return Parsed recipe or <code>null</code>.
   */
  public Recipe parseXML(File source)
  {
    Recipe recipe=null;
    Element root=DOMParsingTools.parse(source);
    if (root!=null)
    {
      recipe=parseRecipe(root);
    }
    return recipe;
  }

  /**
   * Parse the XML stream.
   * @param source Source stream.
   * @return Parsed recipe or <code>null</code>.
   */
  public Recipe parseXML(InputStream source)
  {
    Recipe recipe=null;
    Element root=DOMParsingTools.parse(source);
    if (root!=null)
    {
      recipe=parseRecipe(root);
    }
    return recipe;
  }

  private Recipe parseRecipe(Element root)
  {
    Recipe r=new Recipe();

    NamedNodeMap attrs=root.getAttributes();
    // Identifier
    int id=DOMParsingTools.getIntAttribute(attrs,RecipeXMLConstants.RECIPE_ID_ATTR,0);
    r.setIdentifier(id);
    // Key
    String key=DOMParsingTools.getStringAttribute(attrs,RecipeXMLConstants.RECIPE_KEY_ATTR,null);
    r.setKey(key);
    // Name
    String name=DOMParsingTools.getStringAttribute(attrs,RecipeXMLConstants.RECIPE_NAME_ATTR,null);
    r.setName(name);
    // Profession
    String profession=DOMParsingTools.getStringAttribute(attrs,RecipeXMLConstants.RECIPE_PROFESSION_ATTR,null);
    r.setProfession(profession);
    // Tier
    int tier=DOMParsingTools.getIntAttribute(attrs,RecipeXMLConstants.RECIPE_TIER_ATTR,0);
    r.setTier(tier);

    Element scrollItemElement=DOMParsingTools.getChildTagByName(root,RecipeXMLConstants.SCROLL_ITEM_TAG);
    if (scrollItemElement!=null)
    {
      ItemReference ref=parseItemRef(scrollItemElement);
      r.setRecipeScroll(ref);
    }

    // Ingredients
    List<Ingredient> ingredients=new ArrayList<Ingredient>();
    List<Element> ingredientElements=DOMParsingTools.getChildTagsByName(root,RecipeXMLConstants.INGREDIENT_TAG);
    for(Element ingredientElement : ingredientElements)
    {
      Ingredient ingredient=new Ingredient();
      NamedNodeMap ingredientAttrs=ingredientElement.getAttributes();
      // Quantity
      int quantity=DOMParsingTools.getIntAttribute(ingredientAttrs,RecipeXMLConstants.INGREDIENT_QUANTITY_ATTR,1);
      ingredient.setQuantity(quantity);
      // Optional
      boolean optional=DOMParsingTools.getBooleanAttribute(ingredientAttrs,RecipeXMLConstants.INGREDIENT_OPTIONAL_ATTR,false);
      ingredient.setOptional(optional);
      // Item reference
      Element ingredientItemRef=DOMParsingTools.getChildTagByName(ingredientElement,RecipeXMLConstants.INGREDIENT_ITEM_TAG);
      if (ingredientItemRef!=null)
      {
        ItemReference ingredientRef=parseItemRef(ingredientItemRef);
        ingredient.setItem(ingredientRef);
      }
      ingredients.add(ingredient);
    }
    r.setIngredients(ingredients);
    
    // Results
    List<RecipeVersion> versions=new ArrayList<RecipeVersion>();
    List<Element> versionElements=DOMParsingTools.getChildTagsByName(root,RecipeXMLConstants.RECIPE_RESULT_TAG);
    for(Element versionElement : versionElements)
    {
      RecipeVersion version=new RecipeVersion();
      List<Element> resultElements=DOMParsingTools.getChildTagsByName(versionElement,RecipeXMLConstants.RESULT_TAG);
      for(Element resultElement : resultElements)
      {
        CraftingResult result=parseResult(resultElement);
        if (result!=null)
        {
          boolean isCritical=result.isCriticalResult();
          if (isCritical)
          {
            version.setCritical(result);
          }
          else
          {
            version.setRegular(result);
          }
        }
      }
      versions.add(version);
    }
    r.setVersions(versions);
    return r;
  }

  private CraftingResult parseResult(Element resultElement)
  {
    NamedNodeMap attrs=resultElement.getAttributes();
    CraftingResult result=new CraftingResult();
    // Quantity
    int quantity=DOMParsingTools.getIntAttribute(attrs,RecipeXMLConstants.RESULT_QUANTITY_ATTR,1);
    result.setQuantity(quantity);
    // Critical
    boolean critical=DOMParsingTools.getBooleanAttribute(attrs,RecipeXMLConstants.RESULT_CRITICAL_ATTR,false);
    result.setCriticalResult(critical);
    // Item reference
    Element resultItemRefElement=DOMParsingTools.getChildTagByName(resultElement,RecipeXMLConstants.RESULT_ITEM_TAG);
    if (resultItemRefElement!=null)
    {
      ItemReference resultItemRef=parseItemRef(resultItemRefElement);
      result.setItem(resultItemRef);
    }
    return result;
  }

  private ItemReference parseItemRef(Element itemRef)
  {
    NamedNodeMap attrs=itemRef.getAttributes();
    ItemReference ref=new ItemReference();
    // Item id
    int id=DOMParsingTools.getIntAttribute(attrs,RecipeXMLConstants.RECIPE_ITEM_ID_ATTR,0);
    ref.setItemId(id);
    // Item key
    String key=DOMParsingTools.getStringAttribute(attrs,RecipeXMLConstants.RECIPE_ITEM_KEY_ATTR,null);
    ref.setItemKey(key);
    // Name
    String name=DOMParsingTools.getStringAttribute(attrs,RecipeXMLConstants.RECIPE_ITEM_NAME_ATTR,null);
    ref.setName(name);
    // Icon
    String icon=DOMParsingTools.getStringAttribute(attrs,RecipeXMLConstants.RECIPE_ITEM_ICON_ATTR,null);
    ref.setIcon(icon);
    return ref;
  }
}
