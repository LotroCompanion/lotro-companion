package delta.games.lotro.lore.recipes.io.xml;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.xml.sax.helpers.AttributesImpl;

import delta.common.utils.io.StreamTools;
import delta.games.lotro.lore.recipes.Recipe;
import delta.games.lotro.lore.recipes.Recipe.CraftingResult;
import delta.games.lotro.lore.recipes.Recipe.Ingredient;
import delta.games.lotro.lore.recipes.Recipe.ItemReference;
import delta.games.lotro.lore.recipes.Recipe.RecipeVersion;
import delta.games.lotro.utils.LotroLoggers;

/**
 * Writes LOTRO recipes to XML files.
 * @author DAM
 */
public class RecipeXMLWriter
{
  private static final Logger _logger=LotroLoggers.getLotroLogger();

  private static final String CDATA="CDATA";
  
  /**
   * Write a recipe to a XML file.
   * @param outFile Output file.
   * @param recipe Recipe to write.
   * @param encoding Encoding to use.
   * @return <code>true</code> if it succeeds, <code>false</code> otherwise.
   */
  public boolean write(File outFile, Recipe recipe, String encoding)
  {
    boolean ret;
    FileOutputStream fos=null;
    try
    {
      fos=new FileOutputStream(outFile);
      SAXTransformerFactory tf=(SAXTransformerFactory)TransformerFactory.newInstance();
      TransformerHandler hd=tf.newTransformerHandler();
      Transformer serializer=hd.getTransformer();
      serializer.setOutputProperty(OutputKeys.ENCODING,encoding);
      serializer.setOutputProperty(OutputKeys.INDENT,"yes");

      StreamResult streamResult=new StreamResult(fos);
      hd.setResult(streamResult);
      hd.startDocument();
      write(hd,recipe);
      hd.endDocument();
      ret=true;
    }
    catch (Exception exception)
    {
      _logger.error("",exception);
      ret=false;
    }
    finally
    {
      StreamTools.close(fos);
    }
    return ret;
  }
  
  private void write(TransformerHandler hd, Recipe recipe) throws Exception
  {
    AttributesImpl recipeAttrs=new AttributesImpl();

    int id=recipe.getIdentifier();
    if (id!=0)
    {
      recipeAttrs.addAttribute("","",RecipeXMLConstants.RECIPE_ID_ATTR,CDATA,String.valueOf(id));
    }
    String key=recipe.getKey();
    if (key!=null)
    {
      recipeAttrs.addAttribute("","",RecipeXMLConstants.RECIPE_KEY_ATTR,CDATA,key);
    }
    String name=recipe.getName();
    if (name!=null)
    {
      recipeAttrs.addAttribute("","",RecipeXMLConstants.RECIPE_NAME_ATTR,CDATA,name);
    }
    String profession=recipe.getProfession();
    if (profession!=null)
    {
      recipeAttrs.addAttribute("","",RecipeXMLConstants.RECIPE_PROFESSION_ATTR,CDATA,profession);
    }
    int tier=recipe.getTier();
    recipeAttrs.addAttribute("","",RecipeXMLConstants.RECIPE_TIER_ATTR,CDATA,String.valueOf(tier));
    hd.startElement("","",RecipeXMLConstants.RECIPE_TAG,recipeAttrs);
    ItemReference ref=recipe.getRecipeScroll();
    if (ref!=null)
    {
      writeItemRef(hd,ref,RecipeXMLConstants.SCROLL_ITEM_TAG);
    }
    // Ingredients
    List<Ingredient> ingredients=recipe.getIngredients();
    if (ingredients!=null)
    {
      for(Ingredient ingredient : ingredients)
      {
        AttributesImpl attrs=new AttributesImpl();
        int quantity=ingredient.getQuantity();
        if (quantity!=1)
        {
          attrs.addAttribute("","",RecipeXMLConstants.INGREDIENT_QUANTITY_ATTR,CDATA,String.valueOf(quantity));
        }
        if (ingredient.isOptional())
        {
          attrs.addAttribute("","",RecipeXMLConstants.INGREDIENT_OPTIONAL_ATTR,CDATA,"true");
        }
        hd.startElement("","",RecipeXMLConstants.INGREDIENT_TAG,attrs);
        ItemReference item=ingredient.getItem();
        if (item!=null)
        {
          writeItemRef(hd,item,RecipeXMLConstants.INGREDIENT_ITEM_TAG);
        }
        hd.endElement("","",RecipeXMLConstants.INGREDIENT_TAG);
      }
    }
    
    // Results
    List<RecipeVersion> results=recipe.getVersions();
    if (results!=null)
    {
      for(RecipeVersion result : results)
      {
        hd.startElement("","",RecipeXMLConstants.RECIPE_RESULT_TAG,new AttributesImpl());
        CraftingResult regular=result.getRegular();
        if (regular!=null)
        {
          writeCraftingResult(hd,regular);
        }
        CraftingResult critical=result.getCritical();
        if (critical!=null)
        {
          writeCraftingResult(hd,critical);
        }
        hd.endElement("","",RecipeXMLConstants.RECIPE_RESULT_TAG);
      }
    }
    
    hd.endElement("","",RecipeXMLConstants.RECIPE_TAG);
  }

  private void writeCraftingResult(TransformerHandler hd, CraftingResult result) throws Exception
  {
    AttributesImpl attrs=new AttributesImpl();
    int quantity=result.getQuantity();
    if (quantity!=1)
    {
      attrs.addAttribute("","",RecipeXMLConstants.RESULT_QUANTITY_ATTR,CDATA,String.valueOf(quantity));
    }
    if (result.isCriticalResult())
    {
      attrs.addAttribute("","",RecipeXMLConstants.RESULT_CRITICAL_ATTR,CDATA,"true");
    }
    hd.startElement("","",RecipeXMLConstants.RESULT_TAG,attrs);
    ItemReference itemResult=result.getItem();
    if (itemResult!=null)
    {
      writeItemRef(hd,itemResult,RecipeXMLConstants.RESULT_ITEM_TAG);
    }
    hd.endElement("","",RecipeXMLConstants.RESULT_TAG);
  }

  private void writeItemRef(TransformerHandler hd, ItemReference ref, String tagName) throws Exception
  {
    AttributesImpl attrs=new AttributesImpl();
    int id=ref.getItemId();
    if (id!=0)
    {
      attrs.addAttribute("","",RecipeXMLConstants.RECIPE_ITEM_ID_ATTR,CDATA,String.valueOf(id));
    }
    String key=ref.getItemKey();
    if (key!=null)
    {
      attrs.addAttribute("","",RecipeXMLConstants.RECIPE_ITEM_KEY_ATTR,CDATA,key);
    }
    String name=ref.getName();
    if (name!=null)
    {
      attrs.addAttribute("","",RecipeXMLConstants.RECIPE_ITEM_NAME_ATTR,CDATA,name);
    }
    String icon=ref.getIcon();
    if (icon!=null)
    {
      attrs.addAttribute("","",RecipeXMLConstants.RECIPE_ITEM_ICON_ATTR,CDATA,icon);
    }
    hd.startElement("","",tagName,attrs);
    hd.endElement("","",tagName);
  }
}
