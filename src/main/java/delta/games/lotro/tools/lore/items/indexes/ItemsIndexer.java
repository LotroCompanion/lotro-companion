package delta.games.lotro.tools.lore.items.indexes;


import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import delta.common.utils.NumericTools;
import delta.common.utils.files.filter.ExtensionPredicate;
import delta.games.lotro.Config;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemsManager;
import delta.games.lotro.lore.recipes.Recipe;
import delta.games.lotro.lore.recipes.Recipe.CraftingResult;
import delta.games.lotro.lore.recipes.Recipe.Ingredient;
import delta.games.lotro.lore.recipes.Recipe.ItemReference;
import delta.games.lotro.lore.recipes.Recipe.RecipeVersion;
import delta.games.lotro.lore.recipes.RecipesManager;

/**
 * @author DAM
 */
public class ItemsIndexer
{
  /**
   * @param args
   */
  public static void main(String[] args)
  {
    new ItemsIndexer().doIt();
  }
  
  private void doIt()
  {
    HashMap<String,List<Integer>> ids=loadFileIds();
    handleRecipes(ids);
  }

  /**
   * Load map (keys/names)->list of item ids
   * @return a map.
   */
  private HashMap<String,List<Integer>> loadFileIds()
  {
    HashMap<String,List<Integer>> idStr2Id=new HashMap<String,List<Integer>>(); 
    ItemsManager mgr=ItemsManager.getInstance();
    File itemsDir=Config.getInstance().getItemsDir();
    FileFilter fileFilter=new ExtensionPredicate("xml");
    File[] itemFiles=itemsDir.listFiles(fileFilter);
    if (itemFiles!=null)
    {
      //Set<String> subCategories=new HashSet<String>();
      //HashMap<String,List<String>> itemsByCategory=new HashMap<String,List<String>>();
      for(File itemFile : itemFiles)
      {
        String idStr=itemFile.getName();
        idStr=idStr.substring(0,idStr.length()-4);
        int id=NumericTools.parseInt(idStr,-1);
        if (id!=-1)
        {
          Item item=mgr.getItem(Integer.valueOf(id));
          /*
          // Sub category
          String subCategory=item.getSubCategory();
          if (subCategory!=null)
          {
            subCategories.add(subCategory);
            String name=item.getName();
            if (name!=null)
            {
              List<String> names=itemsByCategory.get(subCategory);
              if (names==null)
              {
                names=new ArrayList<String>();
                itemsByCategory.put(subCategory,names);
              }
              names.add(name);
            }
          }
          */
          String key=item.getKey();
          if (key!=null)
          {
            List<Integer> ids=idStr2Id.get(key);
            if (ids==null)
            {
              ids=new ArrayList<Integer>();
              idStr2Id.put(key,ids);
            }
            ids.add(Integer.valueOf(id));
          }
          String name=item.getName();
          if (name!=null)
          {
            List<Integer> ids=idStr2Id.get(name);
            if (ids==null)
            {
              ids=new ArrayList<Integer>();
              idStr2Id.put(name,ids);
            }
            ids.add(Integer.valueOf(id));
          }
        }
      }
      // Dump keys
      List<String> keys=new ArrayList<String>(idStr2Id.keySet());
      Collections.sort(keys);
      for(String key : keys)
      {
        List<Integer> ids=idStr2Id.get(key);
        //if (ids.size()>1)
        {
          System.out.println("*************** "+key+" ******************");
          Collections.sort(ids);
          for(Integer id : ids)
          {
            System.out.println("\t"+id);
          }
        }
      }
    }
    return idStr2Id;
  }

  private HashMap<String,Integer> loadCompletionMap()
  {
    HashMap<String,Integer> ret=new HashMap<String,Integer>();
    ret.put("Item:Aquamarine",Integer.valueOf(1879132460));
    ret.put("Item:Large_Master_Carving",Integer.valueOf(1879125799));
    ret.put("Item:Large_Master_Crest",Integer.valueOf(1879125746));
    ret.put("Item:Large_Master_Emblem",Integer.valueOf(00000));
    ret.put("Item:Large_Master_Pattern",Integer.valueOf(00000));
    ret.put("Item:Large_Master_Repast",Integer.valueOf(00000));
    ret.put("Item:Large_Master_Scroll",Integer.valueOf(00000));
    ret.put("Item:Large_Master_Symbol",Integer.valueOf(00000));
    ret.put("Item:Large_Supreme_Carving",Integer.valueOf(00000));
    ret.put("Item:Large_Supreme_Crest",Integer.valueOf(00000));
    ret.put("Item:Large_Supreme_Emblem",Integer.valueOf(00000));
    ret.put("Item:Large_Supreme_Pattern",Integer.valueOf(00000));
    ret.put("Item:Large_Supreme_Repast",Integer.valueOf(00000));
    ret.put("Item:Large_Supreme_Scroll",Integer.valueOf(00000));
    ret.put("Item:Large_Supreme_Symbol",Integer.valueOf(00000));
    ret.put("Item:Large_Westfold_Carving",Integer.valueOf(00000));
    ret.put("Item:Large_Westfold_Crest",Integer.valueOf(00000));
    ret.put("Item:Large_Westfold_Emblem",Integer.valueOf(00000));
    ret.put("Item:Large_Westfold_Pattern",Integer.valueOf(00000));
    ret.put("Item:Large_Westfold_Repast",Integer.valueOf(00000));
    ret.put("Item:Large_Westfold_Scroll",Integer.valueOf(00000));
    ret.put("Item:Large_Westfold_Symbol",Integer.valueOf(00000));
    ret.put("Item:Medium_Artisan_Carving",Integer.valueOf(00000));
    ret.put("Item:Medium_Artisan_Crest",Integer.valueOf(00000));
    ret.put("Item:Medium_Artisan_Emblem",Integer.valueOf(00000));
    ret.put("Item:Medium_Artisan_Pattern",Integer.valueOf(00000));
    ret.put("Item:Medium_Artisan_Repast",Integer.valueOf(00000));
    ret.put("Item:Medium_Artisan_Scroll",Integer.valueOf(00000));
    ret.put("Item:Medium_Artisan_Symbol",Integer.valueOf(00000));
    ret.put("Item:Medium_Expert_Carving",Integer.valueOf(00000));
    ret.put("Item:Medium_Expert_Crest",Integer.valueOf(00000));
    ret.put("Item:Medium_Expert_Emblem",Integer.valueOf(00000));
    ret.put("Item:Medium_Expert_Pattern",Integer.valueOf(00000));
    ret.put("Item:Medium_Expert_Repast",Integer.valueOf(00000));
    ret.put("Item:Medium_Expert_Scroll",Integer.valueOf(00000));
    ret.put("Item:Medium_Expert_Symbol",Integer.valueOf(00000));
    ret.put("Item:Polished_Aquamarine",Integer.valueOf(00000));
    ret.put("Item:Small_Artisan_Carving",Integer.valueOf(00000));
    ret.put("Item:Small_Artisan_Crest",Integer.valueOf(00000));
    ret.put("Item:Small_Artisan_Emblem",Integer.valueOf(00000));
    ret.put("Item:Small_Artisan_Pattern",Integer.valueOf(00000));
    ret.put("Item:Small_Artisan_Repast",Integer.valueOf(00000));
    ret.put("Item:Small_Artisan_Scroll",Integer.valueOf(00000));
    ret.put("Item:Small_Artisan_Symbol",Integer.valueOf(00000));
    ret.put("Item:Small_Expert_Carving",Integer.valueOf(00000));
    ret.put("Item:Small_Expert_Crest",Integer.valueOf(00000));
    ret.put("Item:Small_Expert_Emblem",Integer.valueOf(00000));
    ret.put("Item:Small_Expert_Pattern",Integer.valueOf(00000));
    ret.put("Item:Small_Expert_Repast",Integer.valueOf(00000));
    ret.put("Item:Small_Expert_Scroll",Integer.valueOf(00000));
    ret.put("Item:Small_Expert_Symbol",Integer.valueOf(00000));
    ret.put("Item:Small_Master_Carving",Integer.valueOf(00000));
    ret.put("Item:Small_Master_Crest",Integer.valueOf(00000));
    ret.put("Item:Small_Master_Emblem",Integer.valueOf(00000));
    ret.put("Item:Small_Master_Pattern",Integer.valueOf(00000));
    ret.put("Item:Small_Master_Repast",Integer.valueOf(00000));
    ret.put("Item:Small_Master_Scroll",Integer.valueOf(00000));
    ret.put("Item:Small_Master_Symbol",Integer.valueOf(00000));
    ret.put("Item:Small_Supreme_Carving",Integer.valueOf(00000));
    ret.put("Item:Small_Supreme_Crest",Integer.valueOf(00000));
    ret.put("Item:Small_Supreme_Emblem",Integer.valueOf(00000));
    ret.put("Item:Small_Supreme_Pattern",Integer.valueOf(00000));
    ret.put("Item:Small_Supreme_Repast",Integer.valueOf(00000));
    ret.put("Item:Small_Supreme_Scroll",Integer.valueOf(00000));
    ret.put("Item:Small_Supreme_Symbol",Integer.valueOf(00000));
    ret.put("Item:Small_Westfold_Scroll",Integer.valueOf(00000));
    return ret;
  }

  private void handleRecipes(HashMap<String,List<Integer>> ids)
  {
    // Load recipes
    RecipesManager rMgr=RecipesManager.getInstance();
    File recipesDir=Config.getInstance().getRecipesDir();
    FileFilter fileFilter=new ExtensionPredicate("xml");
    File[] recipeFiles=recipesDir.listFiles(fileFilter);
    if (recipeFiles!=null)
    {
      Set<String> missingKeys=new HashSet<String>();
      for(File recipeFile : recipeFiles)
      {
        String idStr=recipeFile.getName();
        idStr=idStr.substring(0,idStr.length()-4);
        int id=NumericTools.parseInt(idStr,-1);
        if (id!=-1)
        {
          Recipe recipe=rMgr.getRecipe(Integer.valueOf(id));
          List<Ingredient> ingredients=recipe.getIngredients();
          for(Ingredient ingredient : ingredients)
          {
            ItemReference itemRef=ingredient.getItem();
            handleItemRef(ids,missingKeys,itemRef);
          }
          /*
          ItemReference scroll=recipe.getRecipeScroll();
          if (scroll!=null)
          {
            handleItemRef(ids,missingKeys,scroll);
          }
          */
          List<RecipeVersion> versions=recipe.getVersions();
          for(RecipeVersion version : versions)
          {
            CraftingResult regular=version.getRegular();
            if (regular!=null)
            {
              ItemReference ref=regular.getItem();
              handleItemRef(ids,missingKeys,ref);
            }
            CraftingResult critical=version.getCritical();
            if (critical!=null)
            {
              ItemReference ref=critical.getItem();
              handleItemRef(ids,missingKeys,ref);
            }
          }
        }
      }
      
      List<String> sortedKeys=new ArrayList<String>(missingKeys);
      Collections.sort(sortedKeys);
      for(String missingKey : sortedKeys)
      {
        System.out.println("Missing : "+missingKey);
      }
      System.out.println("Missing : "+sortedKeys.size());
    }
  }
  
  private void handleItemRef(HashMap<String,List<Integer>> ids, Set<String> missingKeys, ItemReference itemRef)
  {
    String key=itemRef.getItemKey();
    key=key.replace("'","%27");
    key=key.replace("â","%C3%A2");
    key=key.replace("ú","%C3%BA");
    key=key.replace("ó","%C3%B3");
    key=key.replace("û","%C3%BB");
    
    List<Integer> intIds=ids.get(key);
    if (intIds!=null)
    {
      if (intIds.size()>1)
      {
        System.out.println("Warn: "+key+" : "+intIds.size()+" : "+intIds);
      }
      else
      {
        int id=intIds.get(0).intValue();
        itemRef.setItemId(id);
      }
    }
    else
    {
      if (key.startsWith("Item:"))
      {
        key=key.substring(5);
        key=key.replace("_"," ");
        intIds=ids.get(key);
        if (intIds==null)
        {
          missingKeys.add(key);
        }
      }
      //System.out.println("No item for key : "+key);
    }
  }
}
