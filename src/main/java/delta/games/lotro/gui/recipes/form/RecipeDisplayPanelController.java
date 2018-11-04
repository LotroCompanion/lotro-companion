package delta.games.lotro.gui.recipes.form;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.icons.IconWithText;
import delta.games.lotro.common.Duration;
import delta.games.lotro.gui.LotroIconsManager;
import delta.games.lotro.gui.recipes.RecipeIcons;
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
    // Ingredients
    JPanel ingredientsPanel=buildIngredientsPanel();
    ingredientsPanel.setBorder(GuiFactory.buildTitledBorder("Ingredients"));
    c=new GridBagConstraints(0,1,1,1,1,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(2,2,2,2),0,0);
    panel.add(ingredientsPanel,c);
    // Results
    JPanel resultsPanel=buildResultsPanel();
    resultsPanel.setBorder(GuiFactory.buildTitledBorder("Results"));
    c=new GridBagConstraints(0,2,1,1,1,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(2,2,2,2),0,0);
    panel.add(resultsPanel,c);
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
    // Single use and cooldown
    String singleUseAndCooldownStr=getSingleUseAndCooldown();
    JLabel singleUseAndCooldownLabel=GuiFactory.buildLabel(singleUseAndCooldownStr);

    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,2,2,2),0,0);
    panel.add(recipeIconLabel,c);
    c=new GridBagConstraints(1,0,1,1,1,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(2,2,2,2),0,0);
    panel.add(nameLabel,c);
    c=new GridBagConstraints(0,1,2,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,2,2,2),0,0);
    panel.add(professionAndTierLabel,c);
    c=new GridBagConstraints(0,2,2,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,2,2,2),0,0);
    panel.add(categoryAndXpLabel,c);
    if (singleUseAndCooldownStr.length()>0)
    {
      c=new GridBagConstraints(0,3,2,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,2,2,2),0,0);
      panel.add(singleUseAndCooldownLabel,c);
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

  private String getSingleUseAndCooldown()
  {
    StringBuilder sb=new StringBuilder();
    boolean singleUse=_recipe.isOneTimeUse();
    if (singleUse)
    {
      sb.append("Single Use");
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

  private JPanel buildIngredientsPanel()
  {
    List<ItemDisplayGadgets> ingredientsGadgets=initIngredientsGadgets();
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

  private List<ItemDisplayGadgets> initIngredientsGadgets()
  {
    List<ItemDisplayGadgets> ret=new ArrayList<ItemDisplayGadgets>();
    List<Ingredient> ingredients=_recipe.getIngredients();
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

  private JPanel buildResultsPanel()
  {
    int nbVersions=_recipe.getVersions().size();
    if (nbVersions==1)
    {
      return buildResultsPanel(_recipe.getVersions().get(0));
    }
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    int y=0;
    for(RecipeVersion version : _recipe.getVersions())
    {
      String name=version.getRegular().getItem().getName();
      JPanel resultsPanel=buildResultsPanel(version);
      resultsPanel.setBorder(GuiFactory.buildTitledBorder(name));
      GridBagConstraints c=new GridBagConstraints(0,y,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(2,2,2,2),0,0);
      panel.add(resultsPanel,c);
      y++;
    }
    return panel;
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
