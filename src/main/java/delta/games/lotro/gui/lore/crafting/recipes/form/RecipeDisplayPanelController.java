package delta.games.lotro.gui.lore.crafting.recipes.form;

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
import delta.common.ui.swing.labels.HyperLinkController;
import delta.common.ui.swing.navigator.NavigablePanelController;
import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.common.ui.swing.navigator.PageIdentifier;
import delta.games.lotro.common.Duration;
import delta.games.lotro.common.enums.CraftingUICategory;
import delta.games.lotro.gui.LotroIconsManager;
import delta.games.lotro.gui.lore.crafting.recipes.RecipeIcons;
import delta.games.lotro.gui.lore.items.ItemUiTools;
import delta.games.lotro.gui.navigation.NavigationParameters;
import delta.games.lotro.gui.utils.IconController;
import delta.games.lotro.gui.utils.IconControllerFactory;
import delta.games.lotro.gui.utils.ItemDisplayGadgets;
import delta.games.lotro.gui.utils.SharedUiUtils;
import delta.games.lotro.gui.utils.UiConfiguration;
import delta.games.lotro.gui.utils.items.ItemIconController;
import delta.games.lotro.gui.utils.navigation.NavigationHyperLink;
import delta.games.lotro.lore.crafting.CraftingLevel;
import delta.games.lotro.lore.crafting.Profession;
import delta.games.lotro.lore.crafting.recipes.CraftingResult;
import delta.games.lotro.lore.crafting.recipes.Ingredient;
import delta.games.lotro.lore.crafting.recipes.IngredientPack;
import delta.games.lotro.lore.crafting.recipes.Recipe;
import delta.games.lotro.lore.crafting.recipes.RecipeVersion;
import delta.games.lotro.lore.items.Item;

/**
 * Controller for a recipe display panel.
 * @author DAM
 */
public class RecipeDisplayPanelController implements NavigablePanelController
{
  // Data
  private Recipe _recipe;
  // GUI
  private JPanel _panel;
  // Controllers
  private NavigatorWindowController _parent;
  private List<ItemDisplayGadgets> _itemIcons;
  private IconController _recipeItemIcon;
  private HyperLinkController _recipeItemLabel;
  private IconController _ingredientPackIcon;
  private HyperLinkController _ingredientPackLabel;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param recipe Recipe to show.
   */
  public RecipeDisplayPanelController(NavigatorWindowController parent, Recipe recipe)
  {
    _parent=parent;
    _recipe=recipe;
    _itemIcons=new ArrayList<ItemDisplayGadgets>();
  }

  @Override
  public String getTitle()
  {
    return "Recipe: "+_recipe.getName(); // I18n
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  @Override
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
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,5,2,2),0,0);
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
        tabbedPane.add("Output #"+index,versionPanel); // I18n
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
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    int y=0;
    // Recipe
    // - icon
    String recipeIconId=RecipeIcons.getIcon(_recipe.getProfession(),_recipe.getTier());
    Icon recipeIcon=LotroIconsManager.getItemIcon(recipeIconId);
    JLabel recipeIconLabel=GuiFactory.buildIconLabel(recipeIcon);
    GridBagConstraints c=new GridBagConstraints(0,y,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,2,2,2),0,0);
    panel.add(recipeIconLabel,c);
    c=new GridBagConstraints(1,y,1,1,1,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(2,2,2,2),0,0);
    // - name
    JLabel nameLabel=GuiFactory.buildLabel(_recipe.getName(), 28f);
    panel.add(nameLabel,c);
    y++;
    if (UiConfiguration.showTechnicalColumns())
    {
      JComponent idLine=SharedUiUtils.buildSelectableLabel("ID: "+_recipe.getIdentifier());
      c=new GridBagConstraints(0,y,2,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,2,2,2),0,0);
      panel.add(idLine,c);
      y++;
    }
    // Profession and tier
    String professionAndTier=_recipe.getProfession()+", tier "+_recipe.getTier(); // I18n
    JLabel professionAndTierLabel=GuiFactory.buildLabel(professionAndTier);
    c=new GridBagConstraints(0,y,2,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,2,2,2),0,0);
    panel.add(professionAndTierLabel,c);
    y++;
    // Category and XP
    JLabel categoryAndXpLabel=GuiFactory.buildLabel(getCategoryAndXp());
    c=new GridBagConstraints(0,y,2,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,2,2,2),0,0);
    panel.add(categoryAndXpLabel,c);
    y++;
    // Attributes
    String attributesStr=getAttributesString();
    if (!attributesStr.isEmpty())
    {
      JLabel attributesLabel=GuiFactory.buildLabel(attributesStr);
      c=new GridBagConstraints(0,y,2,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,2,2,2),0,0);
      panel.add(attributesLabel,c);
      y++;
    }
    // Recipe item
    Item recipeItem=_recipe.getRecipeScroll();
    if (recipeItem!=null)
    {
      JPanel scrollPanel=GuiFactory.buildPanel(new GridBagLayout());
      // - icon
      _recipeItemIcon=IconControllerFactory.buildItemIcon(_parent,recipeItem,1);
      c=new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,2,2,2),0,0);
      scrollPanel.add(_recipeItemIcon.getIcon(),c);
      // - name
      _recipeItemLabel=ItemUiTools.buildItemLink(_parent,recipeItem);
      c=new GridBagConstraints(1,0,1,1,1,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(2,2,2,2),0,0);
      scrollPanel.add(_recipeItemLabel.getLabel(),c);
      scrollPanel.setBorder(GuiFactory.buildTitledBorder("Scroll")); // I18n
      c=new GridBagConstraints(0,y,2,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,2,2,2),0,0);
      panel.add(scrollPanel,c);
      y++;
    }
    // Ingredient pack
    IngredientPack ingredientPack=_recipe.getIngredientPack();
    if (ingredientPack!=null)
    {
      JPanel ingredientPackPanel=GuiFactory.buildPanel(new GridBagLayout());
      // - icon
      Item ingredientPackItem=ingredientPack.getItem();
      int count=ingredientPack.getCount();
      _ingredientPackIcon=IconControllerFactory.buildItemIcon(_parent,ingredientPackItem,count);
      c=new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,2,2,2),0,0);
      ingredientPackPanel.add(_ingredientPackIcon.getIcon(),c);
      // - name
      _ingredientPackLabel=ItemUiTools.buildItemLink(_parent,ingredientPackItem);
      c=new GridBagConstraints(1,0,1,1,1,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(2,2,2,2),0,0);
      ingredientPackPanel.add(_ingredientPackLabel.getLabel(),c);
      ingredientPackPanel.setBorder(GuiFactory.buildTitledBorder("Ingredient Pack")); // I18n
      c=new GridBagConstraints(0,y,2,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,2,2,2),0,0);
      panel.add(ingredientPackPanel,c);
      y++;
    }
    return panel;
  }

  private String getCategoryAndXp()
  {
    StringBuilder sb=new StringBuilder();
    CraftingUICategory category=_recipe.getCategory();
    if (category!=null)
    {
      sb.append("Category: "); // I18n
      sb.append(category.getLabel());
    }
    int xp=_recipe.getXP();
    if (xp>0)
    {
      if (sb.length()>0)
      {
        sb.append(", ");
      }
      sb.append("XP: "); // I18n
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
      sb.append("Single Use"); // I18n
    }
    boolean autoBestowed=isAutobestowed(_recipe);
    if (autoBestowed)
    {
      if (sb.length()>0)
      {
        sb.append(", ");
      }
      sb.append("Auto-bestowed"); // I18n
    }
    boolean guildRequired=_recipe.isGuildRequired();
    if (guildRequired)
    {
      if (sb.length()>0)
      {
        sb.append(", ");
      }
      sb.append("Guild recipe"); // I18n
    }
    int cooldown=_recipe.getCooldown();
    if (cooldown>0)
    {
      if (sb.length()>0)
      {
        sb.append(", ");
      }
      sb.append("Cooldown: "); // I18n
      String cooldownStr=Duration.getDurationString(cooldown);
      sb.append(cooldownStr);
    }
    return sb.toString();
  }

  private boolean isAutobestowed(Recipe recipe)
  {
    boolean ret=false;
    Profession profession=recipe.getProfession();
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
    GridBagConstraints c;
    // Attributes
    JPanel attributesPanel=buildVersionAttributesPanel(version);
    if (attributesPanel!=null)
    {
      c=new GridBagConstraints(0,0,1,1,1,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,3,0,0),0,0);
      versionPanel.add(attributesPanel,c);
    }
    // Ingredients
    JPanel ingredientsPanel=buildIngredientsPanel(version);
    ingredientsPanel.setBorder(GuiFactory.buildTitledBorder("Ingredients")); // I18n
    c=new GridBagConstraints(0,1,1,1,1,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(2,2,2,2),0,0);
    versionPanel.add(ingredientsPanel,c);
    // Results
    JPanel resultsPanel=buildResultsPanel(version);
    resultsPanel.setBorder(GuiFactory.buildTitledBorder("Results")); // I18n
    c=new GridBagConstraints(0,2,1,1,1,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(2,2,2,2),0,0);
    versionPanel.add(resultsPanel,c);
    return versionPanel;
  }

  private JPanel buildVersionAttributesPanel(RecipeVersion version)
  {
    Integer baseCriticalChance=version.getBaseCriticalChance();
    if (baseCriticalChance==null)
    {
      return null;
    }
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    String attributesStr="Base critical chance: "+baseCriticalChance+"%";
    JLabel attributesLabel=GuiFactory.buildLabel(attributesStr);
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,1,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(2,2,2,2),0,0);
    panel.add(attributesLabel,c);
    return panel;
  }

  private JPanel buildIngredientsPanel(RecipeVersion version)
  {
    List<ItemDisplayGadgets> ingredientsGadgets=initIngredientsGadgets(version);
    _itemIcons.addAll(ingredientsGadgets);
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
      Item item=ingredient.getItem();
      if (item==null)
      {
        continue;
      }
      int itemId=item.getIdentifier();
      int quantity=ingredient.getQuantity();
      // Comment
      String comment="";
      boolean optional=ingredient.isOptional();
      if (optional)
      {
        comment="Optional"; // I18n
        Integer critBonus=ingredient.getCriticalChanceBonus();
        if (critBonus!=null)
        {
          comment=comment+", gives +"+critBonus.toString()+"% critical chance"; // I18n
        }
      }
      ItemDisplayGadgets gadgets=new ItemDisplayGadgets(_parent,itemId,quantity,comment);
      ret.add(gadgets);
    }
    return ret;
  }

  private JPanel buildResultsPanel(RecipeVersion version)
  {
    List<ItemDisplayGadgets> resultGadgets=initResultGadgets(version);
    _itemIcons.addAll(resultGadgets);
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
    Item item=result.getItem();
    int itemId=item.getIdentifier();
    int count=result.getQuantity();
    String comment=result.isCriticalResult()?"Critical result":"Regular result"; // I18n
    int itemLevel=result.getItemLevel();
    if (itemLevel>0)
    {
      comment=comment+" (item level "+itemLevel+")";
    }
    comment=comment+": ";
    ItemDisplayGadgets ret=new ItemDisplayGadgets(_parent,itemId,count,comment);
    if (itemLevel>0)
    {
      ItemIconController ctrl=(ItemIconController)ret.getIconController();
      PageIdentifier pageID=ctrl.getPageIdentifier();
      pageID.setParameter(NavigationParameters.ITEM_LEVEL_PARAMETER,String.valueOf(itemLevel));
      NavigationHyperLink navigationLink=(NavigationHyperLink)ret.getHyperLinkController();
      pageID=navigationLink.getPageIdentifier();
      pageID.setParameter(NavigationParameters.ITEM_LEVEL_PARAMETER,String.valueOf(itemLevel));
    }
    return ret;
  }

  @Override
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
    // Controllers
    _parent=null;
    if (_itemIcons!=null)
    {
      for(ItemDisplayGadgets itemIcon : _itemIcons)
      {
        itemIcon.dispose();
      }
      _itemIcons.clear();
      _itemIcons=null;
    }
    if (_recipeItemIcon!=null)
    {
      _recipeItemIcon.dispose();
      _recipeItemIcon=null;
    }
    if (_recipeItemLabel!=null)
    {
      _recipeItemLabel.dispose();
      _recipeItemLabel=null;
    }
    if (_ingredientPackIcon!=null)
    {
      _ingredientPackIcon.dispose();
      _ingredientPackIcon=null;
    }
    if (_ingredientPackLabel!=null)
    {
      _ingredientPackLabel.dispose();
      _ingredientPackLabel=null;
    }
  }
}
