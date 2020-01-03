package delta.games.lotro.gui.recipes.form;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.icons.IconWithText;
import delta.games.lotro.common.Duration;
import delta.games.lotro.gui.LotroIconsManager;
import delta.games.lotro.gui.recipes.RecipeIcons;
import delta.games.lotro.lore.crafting.CraftingData;
import delta.games.lotro.lore.crafting.CraftingLevel;
import delta.games.lotro.lore.crafting.CraftingSystem;
import delta.games.lotro.lore.crafting.Profession;
import delta.games.lotro.lore.crafting.recipes.CraftingResult;
import delta.games.lotro.lore.crafting.recipes.Ingredient;
import delta.games.lotro.lore.crafting.recipes.Recipe;
import delta.games.lotro.lore.crafting.recipes.RecipeVersion;
import delta.games.lotro.lore.items.ItemProxy;

/**
 * Controller for a recipe display panel.
 * @author DAM
 */
public class RecipeDisplayPanelController
{
  // Data
  private Recipe _recipe;
  // GUI
  private JPanel _panel;

  /**
   * Constructor.
   * @param recipe Recipe to show.
   */
  public RecipeDisplayPanelController(Recipe recipe)
  {
    _recipe=recipe;
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
    }
    return _panel;
  }

  private JPanel build()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    // Recipe attributes
    JPanel attributesPanel=buildAttributesPanel();
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,2,2,2),0,0);
    panel.add(attributesPanel,c);

    int nbVersions=_recipe.getVersions().size();
    JComponent versionsComponent;
    if (nbVersions>1)
    {
      JTabbedPane tabbedPane=GuiFactory.buildTabbedPane();
      int index=1;
      for(RecipeVersion version : _recipe.getVersions())
      {
        JPanel versionPanel=buildVersionPanel(version);
        tabbedPane.add("Output #"+index,versionPanel);
        index++;
      }
      versionsComponent=tabbedPane;
    }
    else
    {
      versionsComponent=buildVersionPanel(_recipe.getVersions().get(0));
    }
    c=new GridBagConstraints(0,1,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(2,2,2,2),0,0);
    panel.add(versionsComponent,c);
    return panel;
  }

  private JPanel buildAttributesPanel()
  {
    // Recipe icon
    String recipeIconId=RecipeIcons.getIcon(_recipe.getProfession(),_recipe.getTier());
    Icon recipeIcon=LotroIconsManager.getItemIcon(recipeIconId);
    JLabel recipeIconLabel=GuiFactory.buildIconLabel(recipeIcon);
    // Name
    JLabel nameLabel=GuiFactory.buildLabel(_recipe.getName(), 28f);
    // Profession and tier
    String professionAndTier=_recipe.getProfession()+", tier "+_recipe.getTier();
    JLabel professionAndTierLabel=GuiFactory.buildLabel(professionAndTier);
    // Category and XP
    JLabel categoryAndXpLabel=GuiFactory.buildLabel(getCategoryAndXp());
    // Attributes
    String attributesStr=getAttributesString();
    JLabel attributesLabel=GuiFactory.buildLabel(attributesStr);
    // Recipe item
    JLabel recipeItemIconLabel=null;
    JLabel recipeItemNameLabel=null;
    ItemProxy recipeItemProxy=_recipe.getRecipeScroll();
    if (recipeItemProxy!=null)
    {
      // - icon
      String recipeItemIconId=recipeItemProxy.getIcon();
      Icon recipeItemIcon=LotroIconsManager.getItemIcon(recipeItemIconId);
      recipeItemIconLabel=GuiFactory.buildIconLabel(recipeItemIcon);
      // - name
      recipeItemNameLabel=GuiFactory.buildLabel(recipeItemProxy.getName(),16f);
    }

    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    // Recipe
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,2,2,2),0,0);
    panel.add(recipeIconLabel,c);
    c=new GridBagConstraints(1,0,1,1,1,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(2,2,2,2),0,0);
    panel.add(nameLabel,c);
    // Profession and tier
    c=new GridBagConstraints(0,1,2,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,2,2,2),0,0);
    panel.add(professionAndTierLabel,c);
    // Category and XP
    c=new GridBagConstraints(0,2,2,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,2,2,2),0,0);
    panel.add(categoryAndXpLabel,c);
    // Attributes
    if (attributesStr.length()>0)
    {
      c=new GridBagConstraints(0,3,2,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,2,2,2),0,0);
      panel.add(attributesLabel,c);
    }
    if ((recipeItemIconLabel!=null) && (recipeItemNameLabel!=null))
    {
      c=new GridBagConstraints(0,4,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,2,2,2),0,0);
      panel.add(recipeItemIconLabel,c);
      c=new GridBagConstraints(1,4,1,1,1,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(2,2,2,2),0,0);
      panel.add(recipeItemNameLabel,c);
    }
    return panel;
  }

  private String getCategoryAndXp()
  {
    StringBuilder sb=new StringBuilder();
    String category=_recipe.getCategory();
    if (category.length()>0)
    {
      sb.append("Category: ");
      sb.append(category);
    }
    int xp=_recipe.getXP();
    if (xp>0)
    {
      if (sb.length()>0)
      {
        sb.append(", ");
      }
      sb.append("XP: ");
      sb.append(xp);
    }
    return sb.toString();
  }

  private String getAttributesString()
  {
    StringBuilder sb=new StringBuilder();
    boolean singleUse=_recipe.isOneTimeUse();
    if (singleUse)
    {
      sb.append("Single Use");
    }
    boolean autoBestowed=isAutobestowed(_recipe);
    if (autoBestowed)
    {
      if (sb.length()>0)
      {
        sb.append(", ");
      }
      sb.append("Auto-bestowed");
    }
    boolean guildRequired=_recipe.isGuildRequired();
    if (guildRequired)
    {
      if (sb.length()>0)
      {
        sb.append(", ");
      }
      sb.append("Guild recipe");
    }
    int cooldown=_recipe.getCooldown();
    if (cooldown>0)
    {
      if (sb.length()>0)
      {
        sb.append(", ");
      }
      sb.append("Cooldown: ");
      String cooldownStr=Duration.getDurationString(cooldown);
      sb.append(cooldownStr);
    }
    return sb.toString();
  }

  private boolean isAutobestowed(Recipe recipe)
  {
    boolean ret=false;
    CraftingData craftingData=CraftingSystem.getInstance().getData();
    Profession profession=craftingData.getProfessionsRegistry().getProfessionByName(recipe.getProfession());
    int tier=recipe.getTier();
    CraftingLevel level=profession.getByTier(tier);
    if (level!=null)
    {
      ret=level.isAutobestowed(recipe.getIdentifier());
    }
    return ret;
  }

  private JPanel buildVersionPanel(RecipeVersion version)
  {
    JPanel versionPanel=GuiFactory.buildBackgroundPanel(new GridBagLayout());
    // Ingredients
    JPanel ingredientsPanel=buildIngredientsPanel(version);
    ingredientsPanel.setBorder(GuiFactory.buildTitledBorder("Ingredients"));
    GridBagConstraints c=new GridBagConstraints(0,1,1,1,1,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(2,2,2,2),0,0);
    versionPanel.add(ingredientsPanel,c);
    // Results
    JPanel resultsPanel=buildResultsPanel(version);
    resultsPanel.setBorder(GuiFactory.buildTitledBorder("Results"));
    c=new GridBagConstraints(0,2,1,1,1,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(2,2,2,2),0,0);
    versionPanel.add(resultsPanel,c);
    return versionPanel;
  }

  private JPanel buildIngredientsPanel(RecipeVersion version)
  {
    List<ItemDisplayGadgets> ingredientsGadgets=initIngredientsGadgets(version);
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    int y=0;
    for(ItemDisplayGadgets ingredientsGadget : ingredientsGadgets)
    {
      GridBagConstraints c=new GridBagConstraints(0,y,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,2,2,2),0,0);
      // Icon
      panel.add(ingredientsGadget.getIcon(),c);
      // Name
      c=new GridBagConstraints(1,y,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(2,2,2,2),0,0);
      panel.add(ingredientsGadget.getName(),c);
      // Comment
      c=new GridBagConstraints(2,y,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,2,2,2),0,0);
      panel.add(ingredientsGadget.getComment(),c);
      y++;
    }
    return panel;
  }

  private List<ItemDisplayGadgets> initIngredientsGadgets(RecipeVersion version)
  {
    List<ItemDisplayGadgets> ret=new ArrayList<ItemDisplayGadgets>();
    List<Ingredient> ingredients=version.getIngredients();
    for(Ingredient ingredient : ingredients)
    {
      ItemDisplayGadgets gadgets=new ItemDisplayGadgets();
      // Item and quantity
      int quantity=ingredient.getQuantity();
      ItemProxy proxy=ingredient.getItem();
      Icon icon=LotroIconsManager.getItemIcon(proxy.getIcon());
      String text=(quantity!=1)?String.valueOf(quantity):"";
      IconWithText iconWithText=new IconWithText(icon,text,Color.WHITE);
      // Name
      String name=ingredient.getName();
      // Comment
      String comment="";
      boolean optional=ingredient.isOptional();
      if (optional)
      {
        comment="Optional";
        Integer critBonus=ingredient.getCriticalChanceBonus();
        if (critBonus!=null)
        {
          comment=comment+", gives +"+critBonus.toString()+"% critical chance";
        }
      }
      gadgets.set(iconWithText,name,comment);
      ret.add(gadgets);
    }
    return ret;
  }

  private JPanel buildResultsPanel(RecipeVersion version)
  {
    List<ItemDisplayGadgets> resultGadgets=initResultGadgets(version);
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    int y=0;
    for(ItemDisplayGadgets resultGadget : resultGadgets)
    {
      // Comment
      GridBagConstraints c=new GridBagConstraints(0,y,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,2,2,2),0,0);
      panel.add(resultGadget.getComment(),c);
      // Icon
      c=new GridBagConstraints(1,y,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,2,2,2),0,0);
      panel.add(resultGadget.getIcon(),c);
      // Name
      c=new GridBagConstraints(2,y,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(2,2,2,2),0,0);
      panel.add(resultGadget.getName(),c);
      y++;
    }
    return panel;
  }

  private List<ItemDisplayGadgets> initResultGadgets(RecipeVersion version)
  {
    List<ItemDisplayGadgets> ret=new ArrayList<ItemDisplayGadgets>();
    CraftingResult regularResult=version.getRegular();
    ItemDisplayGadgets regularResultGadgets=buildResultGadget(regularResult);
    ret.add(regularResultGadgets);
    CraftingResult criticalResult=version.getCritical();
    if (criticalResult!=null)
    {
      ItemDisplayGadgets criticalResultGadgets=buildResultGadget(criticalResult);
      ret.add(criticalResultGadgets);
    }
    return ret;
  }

  private ItemDisplayGadgets buildResultGadget(CraftingResult result)
  {
    // Icon
    int quantity=result.getQuantity();
    ItemProxy proxy=result.getItem();
    Icon icon=LotroIconsManager.getItemIcon(proxy.getIcon());
    String text=(quantity!=1)?String.valueOf(quantity):"";
    IconWithText iconWithText=new IconWithText(icon,text,Color.WHITE);
    // Name
    String name=proxy.getName();
    ItemDisplayGadgets ret=new ItemDisplayGadgets();
    // Comment
    String comment=result.isCriticalResult()?"Critical result: ":"Regular result: ";
    ret.set(iconWithText,name,comment);
    return ret;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Data
    _recipe=null;
    // UI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
  }
}
