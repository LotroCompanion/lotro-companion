package delta.games.lotro.lore.recipes.index.io.xml;

import java.io.File;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

import delta.common.utils.xml.DOMParsingTools;
import delta.games.lotro.lore.recipes.index.RecipesIndex;

/**
 * Parser for recipe indexes stored in XML.
 * @author DAM
 */
public class RecipesIndexXMLParser
{
  /**
   * Parse the XML file.
   * @param source Source file.
   * @return Parsed recipes index or <code>null</code>.
   */
  public RecipesIndex parseXML(File source)
  {
    RecipesIndex index=null;
    Element root=DOMParsingTools.parse(source);
    if (root!=null)
    {
      index=parseRecipesIndex(root);
    }
    return index;
  }

  private RecipesIndex parseRecipesIndex(Element root)
  {
    RecipesIndex index=new RecipesIndex();

    // Professions
    List<Element> professionTags=DOMParsingTools.getChildTagsByName(root,RecipesIndexXMLConstants.PROFESSION_TAG);
    if (professionTags!=null)
    {
      for(Element professionTag : professionTags)
      {
        String profession=DOMParsingTools.getStringAttribute(professionTag.getAttributes(),RecipesIndexXMLConstants.PROFESSION_NAME_ATTR,null);
        if (profession!=null)
        {
          // Tiers
          List<Element> tierTags=DOMParsingTools.getChildTagsByName(root,RecipesIndexXMLConstants.TIER_TAG);
          if (tierTags!=null)
          {
            for(Element tierTag : tierTags)
            {
              int tier=DOMParsingTools.getIntAttribute(tierTag.getAttributes(),RecipesIndexXMLConstants.TIER_VALUE_ATTR,0);
              if (tier!=0)
              {
                // Recipes
                List<Element> recipeTags=DOMParsingTools.getChildTagsByName(tierTag,RecipesIndexXMLConstants.RECIPE_TAG);
                if (recipeTags!=null)
                {
                  for(Element recipeTag : recipeTags)
                  {
                    NamedNodeMap attrs=recipeTag.getAttributes();
                    String key=DOMParsingTools.getStringAttribute(attrs,RecipesIndexXMLConstants.RECIPE_KEY_ATTR,null);
                    String recipeName=DOMParsingTools.getStringAttribute(attrs,RecipesIndexXMLConstants.RECIPE_NAME_ATTR,null);
                    index.addRecipe(key,recipeName,profession,tier);
                  }
                }
              }
            }
          }
        }
      }
    }
    return index;
  }
}
