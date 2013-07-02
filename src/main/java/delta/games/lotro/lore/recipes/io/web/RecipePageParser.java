package delta.games.lotro.lore.recipes.io.web;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import net.htmlparser.jericho.CharacterReference;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Segment;
import net.htmlparser.jericho.Source;
import net.htmlparser.jericho.StartTag;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import delta.common.utils.NumericTools;
import delta.common.utils.xml.DOMParsingTools;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.io.web.ItemPageParser;
import delta.games.lotro.lore.recipes.Recipe;
import delta.games.lotro.lore.recipes.Recipe.CraftingResult;
import delta.games.lotro.lore.recipes.Recipe.Ingredient;
import delta.games.lotro.lore.recipes.Recipe.ItemReference;
import delta.games.lotro.lore.recipes.Recipe.RecipeVersion;
import delta.games.lotro.utils.DownloadService;
import delta.games.lotro.utils.JerichoHtmlUtils;
import delta.games.lotro.utils.LotroLoggers;

/**
 * Parser for MyLotro.com recipe page.
 * @author DAM
 */
public class RecipePageParser
{
  private static final Logger _logger=LotroLoggers.getWebInputLogger();
  private static final String RECIPE_SEED="Recipe:";
  private static final String RECIPE_URL_SEED="/wiki/Recipe:";
  private static final String WIKI_URL_SEED="/wiki/";
  private static final String FULL_WIKI_URL_SEED="http://lorebook.lotro.com/wiki/";
  private static final String OPTIONAL_MARKER="(optional)";
  private static final String CATEGORY_TIER="Category:Tier";

  private Recipe _recipe;
  private String _key;

  private String extractItemIdentifier(String url) {
    String ret=null;
    if (url!=null)
    {
      if (url.startsWith(WIKI_URL_SEED))
      {
        ret=url.substring(WIKI_URL_SEED.length()).trim();
      }
      else if (url.startsWith(FULL_WIKI_URL_SEED))
      {
        ret=url.substring(FULL_WIKI_URL_SEED.length()).trim();
      }
    }
    return ret;
  }

  private int parseQuantityString(String quantityStr)
  {
    int ret=0;
    if (quantityStr!=null)
    {
      quantityStr=quantityStr.trim();
      if (quantityStr.startsWith("x"))
      {
        quantityStr=quantityStr.substring(1).trim();
      }
      if (quantityStr.endsWith(OPTIONAL_MARKER))
      {
        quantityStr=quantityStr.substring(0,quantityStr.length()-OPTIONAL_MARKER.length());
      }
      ret=NumericTools.parseInt(quantityStr,0);
    }
    return ret;
  }

  private void parseRecipeSummary(Element summarySection)
  {
    List<Element> divs=summarySection.getAllElements(HTMLElementName.DIV);
    for(Element div : divs)
    {
      String contents=CharacterReference.decodeCollapseWhiteSpace(div.getContent());
      if (contents.contains("Recipe for")) {
        // Profession
        String profession=null;
        Element a=JerichoHtmlUtils.findElementByTagName(div,HTMLElementName.A);
        if (a!=null)
        {
          profession=CharacterReference.decodeCollapseWhiteSpace(a.getContent());
          _recipe.setProfession(profession);
        }
      }
      else if (contents.contains("One time use")) {
        _recipe.setOneTimeUse(true);
      }
      else if (contents.contains("Learned from")) {
        Element a=JerichoHtmlUtils.findElementByTagName(div,HTMLElementName.A);
        String itemURL=a.getAttributeValue("href");
        String itemId=extractItemIdentifier(itemURL);
        if (itemId!=null)
        {
          ItemReference scroll=new ItemReference();
          String scrollName=CharacterReference.decodeCollapseWhiteSpace(a.getContent());
          scroll.setName(scrollName);
          scroll.setItemId(itemId);
          _recipe.setRecipeScroll(scroll);
        }
      }
    }
    // Recipe name
    String recipeName=null;
    Element recipeNameElement=JerichoHtmlUtils.findElementByTagNameAndAttributeValue(summarySection,HTMLElementName.DIV,"class","lorebooktitle");
    if (recipeNameElement!=null)
    {
      recipeName=CharacterReference.decodeCollapseWhiteSpace(recipeNameElement.getContent());
      if (recipeName!=null)
      {
        if (recipeName.startsWith(RECIPE_SEED))
        {
          recipeName=recipeName.substring(RECIPE_SEED.length()).trim();
        }
      }
    }
    _recipe.setName(recipeName);
    //_recipe.setIdentifier(identifier);
    //_recipe.setTier(tier);
  }

  private List<Ingredient> parseIngredients(Element ingredientsSection)
  {
    /*
<tr class="ingredientrow">
<td class="ingicon"><a href="/wiki/Item:Ancient_Steel_Ingot"><img class="icon" rel="" src="http://content.turbine.com/sites/lorebook.lotro.com/images/icons/item/component/it_craft_ancient_steel_ingot.png"></img></a></td>
<td class="ingquan">x4 (optional)</td>
<td class="ingname"><a href="/wiki/Item:Ancient_Steel_Ingot"></a></td>
</tr>
     */
    List<Ingredient> ingredients=new ArrayList<Ingredient>();
    List<Element> ingredientRows=JerichoHtmlUtils.findElementsByTagNameAndAttributeValue(ingredientsSection,HTMLElementName.TR,"class","ingredientrow");
    if ((ingredientRows!=null) && (ingredientRows.size()>0))
    {
      for(Element ingredientRow : ingredientRows)
      {
        Ingredient ingredient=parseIngredient(ingredientRow,false);
        ingredients.add(ingredient);
      }
    }
    List<Element> optionalIngredientRows=JerichoHtmlUtils.findElementsByTagNameAndAttributeValue(ingredientsSection,HTMLElementName.TR,"class","ingredientrow optional");
    if ((optionalIngredientRows!=null) && (optionalIngredientRows.size()>0))
    {
      for(Element optionalIngredientRow : optionalIngredientRows)
      {
        Ingredient ingredient=parseIngredient(optionalIngredientRow,true);
        ingredients.add(ingredient);
      }
    }
    return ingredients;
  }

  private Ingredient parseIngredient(Element ingredientRow, boolean optional)
  {
    Ingredient ingredient=new Ingredient();
    ingredient.setOptional(optional);
    ItemReference itemRef=new ItemReference();
    ingredient.setItem(itemRef);
    Element ingredientIcon=JerichoHtmlUtils.findElementByTagNameAndAttributeValue(ingredientRow,HTMLElementName.TD,"class","ingicon");
    if (ingredientIcon!=null)
    {
      Element img=JerichoHtmlUtils.findElementByTagName(ingredientIcon,HTMLElementName.IMG);
      if (img!=null)
      {
        String itemIcon=img.getAttributeValue("src");
        itemRef.setIcon(itemIcon);
        //System.out.println("Icon: "+itemIcon);
      }
    }
    Element ingredientQuantity=JerichoHtmlUtils.findElementByTagNameAndAttributeValue(ingredientRow,HTMLElementName.TD,"class","ingquan");
    if (ingredientQuantity!=null)
    {
      String quantityStr=CharacterReference.decodeCollapseWhiteSpace(ingredientQuantity.getContent());
      int quantity=parseQuantityString(quantityStr);
      ingredient.setQuantity(quantity);
      //System.out.println("Quantity: "+quantity);
    }
    Element ingredientName=JerichoHtmlUtils.findElementByTagNameAndAttributeValue(ingredientRow,HTMLElementName.TD,"class","ingname");
    if (ingredientName!=null)
    {
      Element a=JerichoHtmlUtils.findElementByTagName(ingredientName,HTMLElementName.A);
      String itemURL=a.getAttributeValue("href");
      String itemId=extractItemIdentifier(itemURL);
      itemRef.setItemId(itemId);
      String itemName=CharacterReference.decodeCollapseWhiteSpace(a.getContent());
      itemRef.setName(itemName);
    }
    return ingredient;
  }

  private List<RecipeVersion> parseResults(Element resultsSection)
  {
    /*
<tr class="resultrow"> // or "resultrow critical"
<td class="resicon"><a href="/wiki/Tool:Ancient_Steel_Scholar's_Glass"><img class="icon" rel="" src="http://content.turbine.com/sites/lorebook.lotro.com/images/icons/item/tool/eq_craft_tool_rare_scholars_glass_tier5.png"></img></a></td>
<td class="restype">regular success</td>  // or critical success
<td class="resquan">x1</td>
<td class="resname"><a href="/wiki/Tool:Ancient_Steel_Scholar's_Glass"></a></td></tr>
     */
    List<RecipeVersion> versions = new ArrayList<RecipeVersion>();
    RecipeVersion version = new RecipeVersion();
    versions.add(version);
    List<Element> rows=resultsSection.getAllElements(HTMLElementName.TR);
    for(Element row : rows) {
      StartTag tag=row.getStartTag();
      String value=tag.getAttributeValue("class");
      if ("resultrow".equals(value)) {
        Element header=JerichoHtmlUtils.findElementByTagName(row,HTMLElementName.TH);
        if (header!=null)
        {
          version = new RecipeVersion();
          versions.add(version);
          //System.out.println("OR");
        }
        else
        {
          CraftingResult regular=parseResultItem(row,false);
          version.setRegular(regular);
        }
      }
      else if ("resultrow critical".equals(value)) {
        CraftingResult critical=parseResultItem(row,true);
        version.setCritical(critical);
      }
    }
    return versions;
  }

  private CraftingResult parseResultItem(Element resultRow, boolean critical)
  {
    CraftingResult result=new CraftingResult();
    result.setCriticalResult(critical);
    ItemReference itemRef = new ItemReference();
    result.setItem(itemRef);
    Element ingredientIcon=JerichoHtmlUtils.findElementByTagNameAndAttributeValue(resultRow,HTMLElementName.TD,"class","resicon");
    if (ingredientIcon!=null)
    {
      Element img=JerichoHtmlUtils.findElementByTagName(ingredientIcon,HTMLElementName.IMG);
      if (img!=null)
      {
        String itemIcon=img.getAttributeValue("src");
        //System.out.println("Icon: "+itemIcon);
        itemRef.setIcon(itemIcon);
      }
    }
    Element ingredientQuantity=JerichoHtmlUtils.findElementByTagNameAndAttributeValue(resultRow,HTMLElementName.TD,"class","resquan");
    if (ingredientQuantity!=null)
    {
      String quantityStr=CharacterReference.decodeCollapseWhiteSpace(ingredientQuantity.getContent());
      int quantity=parseQuantityString(quantityStr);
      //System.out.println("Quantity: "+quantity);
      result.setQuantity(quantity);
    }
    Element ingredientName=JerichoHtmlUtils.findElementByTagNameAndAttributeValue(resultRow,HTMLElementName.TD,"class","resname");
    if (ingredientName!=null)
    {
      Element a=JerichoHtmlUtils.findElementByTagName(ingredientName,HTMLElementName.A);
      String itemURL=a.getAttributeValue("href");
      String itemId=extractItemIdentifier(itemURL);
      itemRef.setItemId(itemId);
      String itemName=CharacterReference.decodeCollapseWhiteSpace(a.getContent());
      itemRef.setName(itemName);
    }
    //System.out.println(critical?"Critical result":"Regular result");
    return result;
  }

  private Recipe parseRecipeSection(Element recipeSection)
  {
    Recipe ret=null;
    try
    {
      _recipe=new Recipe();
      
      //Element mainIconElement=JerichoHtmlUtils.findElementByTagNameAndAttributeValue(recipeSection,HTMLElementName.DIV,"class","mainicon");
      List<Element> officialSections=JerichoHtmlUtils.findElementsByTagNameAndAttributeValue(recipeSection,HTMLElementName.DIV,"class","officialsection");
      // 1 - summary
      // 2 - ingredients
      // 3 - results
      int nbSections=officialSections.size();
      if (nbSections!=3)
      {
        System.out.println("Warning: found "+nbSections+" sections!");
      }

      Element summarySection=officialSections.get(0);
      parseRecipeSummary(summarySection);
      
      //System.out.println("Ingredients:");
      Element ingredientsSection=officialSections.get(1);
      List<Ingredient> ingredients=parseIngredients(ingredientsSection);
      _recipe.setIngredients(ingredients);

      //System.out.println("Results:");
      Element resultsSection=officialSections.get(2);
      List<RecipeVersion> results=parseResults(resultsSection);
      _recipe.setVersions(results);
      
      ret=_recipe;
      _recipe=null;
    }
    catch(Exception e)
    {
      ret=null;
      _logger.error("Recipe ["+_key+"]. Cannot parse recipe section!",e);
    }
    return ret;
  }

  private void findIdentifiers(List<Recipe> recipes)
  {
    String url="http://lorebook.lotro.com/index.php?title=Recipe:"+_key+"&action=edit";
    DownloadService downloader=DownloadService.getInstance();
    try
    {
      String page=downloader.getPage(url);
      Source s=new Source(page);
      //<textarea id="wpTextbox1"
      Element pageSource=JerichoHtmlUtils.findElementByTagNameAndAttributeValue(s,HTMLElementName.TEXTAREA,"id","wpTextbox1");
      if (pageSource!=null)
      {
        String text=JerichoHtmlUtils.getTextFromTag(pageSource);
        parsePageSource(text,recipes);
      }
      else
      {
        _logger.warn("Cannot find identifiers!");
      }
    }
    catch(Exception e)
    {
      _logger.error("Parsing error",e);
    }
  }

  private void parsePageSource(String text, List<Recipe> recipes)
  {
    try
    {
      String seed="</noedit>";
      int index=text.indexOf(seed);
      if (index!=-1)
      {
        text=text.substring(0,index+seed.length());
      }
      text=text.replace("&"," ");
      DocumentBuilder builder=DocumentBuilderFactory.newInstance().newDocumentBuilder();
      InputSource is=new InputSource(new StringReader(text));
      Document doc=builder.parse(is);
      org.w3c.dom.Element root=doc.getDocumentElement();
      List<org.w3c.dom.Element> recipeTags=DOMParsingTools.getChildTagsByName(root,"recipe",true);
      int nbRecipeTags=0;
      if (recipeTags!=null)
      {
        nbRecipeTags=recipeTags.size();
      }
      int nbRecipes=recipes.size();
      if (nbRecipeTags==nbRecipes)
      {
        int recipeIndex=0;
        for(org.w3c.dom.Element recipeTag : recipeTags)
        {
          String idStr=DOMParsingTools.getStringAttribute(recipeTag.getAttributes(),"id",null);
          int id=NumericTools.parseInt(idStr,-1);
          if (id!=-1)
          {
            recipes.get(recipeIndex).setIdentifier(id);
          }
          else
          {
            _logger.error("Bad identifier ["+idStr+"] for recipe key ["+_key+"] index "+recipeIndex);
          }
          recipeIndex++;
        }
      }
      else
      {
        _logger.error("Bad number of recipe identifiers for recipe key ["+_key+"]. Expected "+nbRecipes+", got "+nbRecipeTags);
      }
    }
    catch (Exception e)
    {
      _logger.error("Parsing error",e);
    }
  }

  private int findTier(Segment root)
  {
    int tier=0;
    Element links=JerichoHtmlUtils.findElementByTagNameAndAttributeValue(root,HTMLElementName.DIV,"id","mw-normal-catlinks");
    if (links!=null)
    {
      List<Element> as=JerichoHtmlUtils.findElementsByTagName(links,HTMLElementName.A);
      for(Element a : as)
      {
        String title=a.getAttributeValue("title");
        if (title!=null)
        {
          int index=title.indexOf(CATEGORY_TIER);
          if (index!=-1)
          {
            int startIndex=index+CATEGORY_TIER.length();
            String tierStr=title.substring(startIndex,startIndex+2).trim();
            tier=NumericTools.parseInt(tierStr,0);
            break;
          }
        }
      }
    }
    return tier;
  }

  /**
   * Parse the recipe page at the given URL.
   * @param url URL of recipe page.
   * @return A list of recipes or <code>null</code> if an error occurred.
   */
  public List<Recipe> parseRecipePage(String url)
  {
    List<Recipe> recipes=null;
    try
    {
      //url="http://lorebook.lotro.com/wiki/Recipe:Ancient_Steel_Scholar%27s_Glass";
      //url="http://lorebook.lotro.com/wiki/Recipe:Heavy_Cotton_Armour"; // Multiple output
      DownloadService downloader=DownloadService.getInstance();
      String page=downloader.getPage(url);
      //File f=new File("D:\\dam\\dev\\perso\\lotro\\docs\\recipes\\Glass.html");
      //File f=new File("D:\\dam\\dev\\perso\\lotro\\docs\\recipes\\Chisel.html");
      //String page=TextUtils.loadTextFile(f,EncodingNames.ISO8859_1);
      Source source=new Source(page);

      //<div id="lorebookNoedit">
      Element lorebook=JerichoHtmlUtils.findElementByTagNameAndAttributeValue(source,HTMLElementName.DIV,"id","lorebookNoedit");
      if (lorebook!=null)
      {
        // identifier
        // <a id="ca-nstab-recipe" class="lorebook_action_link" href="/wiki/Recipe:Sage%5C%27s_Sharp_Chisel">Article</a></div>
        _key=null;
        Element articleLink=JerichoHtmlUtils.findElementByTagNameAndAttributeValue(source,HTMLElementName.A,"id","ca-nstab-recipe");
        if (articleLink!=null)
        {
          String thisURL=articleLink.getAttributeValue("href");
          if ((thisURL!=null) && (thisURL.startsWith(RECIPE_URL_SEED)))
          {
            _key=thisURL.substring(RECIPE_URL_SEED.length()).trim();
          }
        }
        
        int tier=findTier(source);

        recipes=new ArrayList<Recipe>();
        List<Element> recipeSections=JerichoHtmlUtils.findElementsByTagNameAndAttributeValue(lorebook,HTMLElementName.DIV,"class","lorebookclass");
        if ((recipeSections!=null) && (recipeSections.size()>0))
        {
          for(Element recipeSection : recipeSections)
          {
            Recipe recipe=parseRecipeSection(recipeSection);
            if (recipe!=null)
            {
              recipes.add(recipe);
              recipe.setKey(_key);
              if (tier!=0)
              {
                recipe.setTier(tier);
              }
            }
          }
        }
        findIdentifiers(recipes);
        for(Recipe recipe : recipes)
        {
          System.out.println("Recipe: ");
          System.out.println(recipe.dump());
        }
      }
    }
    catch(Exception e)
    {
      recipes=null;
      _logger.error("Cannot parse quest page ["+url+"]",e);
    }
    return recipes;
  }

  public static void main(String[] args) {
    /*
    RecipePageParser parser=new RecipePageParser();
    List<Recipe> r=parser.parseRecipePage("");
    */
    ItemPageParser parser=new ItemPageParser();
    List<Item> r=parser.parseItemPage("http://lorebook.lotro.com/wiki/Resource%3AWell-tended_Amaranth_Field");
  }
}
