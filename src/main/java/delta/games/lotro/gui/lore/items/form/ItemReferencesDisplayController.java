package delta.games.lotro.gui.lore.items.form;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JEditorPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.common.ui.swing.navigator.PageIdentifier;
import delta.games.lotro.common.Identifiable;
import delta.games.lotro.gui.common.navigation.ReferenceConstants;
import delta.games.lotro.lore.agents.npcs.NpcDescription;
import delta.games.lotro.lore.crafting.Profession;
import delta.games.lotro.lore.crafting.recipes.Recipe;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemsManager;
import delta.games.lotro.lore.items.containers.ItemsContainer;
import delta.games.lotro.lore.items.sets.ItemsSet;
import delta.games.lotro.lore.quests.Achievable;
import delta.games.lotro.lore.quests.QuestDescription;
import delta.games.lotro.lore.relics.melding.RelicMeldingRecipe;
import delta.games.lotro.lore.tasks.Task;
import delta.games.lotro.lore.trade.barter.BarterNpc;
import delta.games.lotro.lore.trade.vendor.VendorNpc;
import delta.games.lotro.lore.xrefs.items.ItemReference;
import delta.games.lotro.lore.xrefs.items.ItemReferencesBuilder;
import delta.games.lotro.lore.xrefs.items.ItemRole;
import delta.games.lotro.utils.gui.HtmlUtils;

/**
 * Controller to display references to an item.
 * @author DAM
 */
public class ItemReferencesDisplayController
{
  // Controllers
  private NavigatorWindowController _parent;
  // UI
  private JEditorPane _display;

  /**
   * Constructor.
   * @param parent Parent window (a navigator).
   * @param itemId Identifier of the item to show.
   */
  public ItemReferencesDisplayController(NavigatorWindowController parent, int itemId)
  {
    _parent=parent;
    _display=buildDetailsPane(itemId);
  }

  /**
   * Get the managed component.
   * @return the managed component.
   */
  public JEditorPane getComponent()
  {
    return _display;
  }

  private JEditorPane buildDetailsPane(int itemId)
  {
    List<ItemReference<?>> references=getReferences(itemId);
    if (references.size()==0)
    {
      return null;
    }
    String html=getHtml(references);
    JEditorPane editor=buildEditor();
    editor.setText(html);
    editor.setCaretPosition(0);
    return editor;
  }

  private JEditorPane buildEditor()
  {
    JEditorPane editor=new JEditorPane("text/html","");
    editor.setEditable(false);
    editor.setPreferredSize(new Dimension(500,300));
    editor.setOpaque(false);
    HyperlinkListener l=new HyperlinkListener()
    {
      @Override
      public void hyperlinkUpdate(HyperlinkEvent e)
      {
        if(e.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
        {
          String reference=e.getDescription();
          PageIdentifier pageId=PageIdentifier.fromString(reference);
          _parent.navigateTo(pageId);
        }
      }
    };
    editor.addHyperlinkListener(l);
    return editor;
  }

  private List<ItemReference<?>> getReferences(int itemId)
  {
    ItemReferencesBuilder builder=new ItemReferencesBuilder();
    List<ItemReference<?>> references=builder.inspectItem(itemId);
    return references;
  }

  private String getHtml(List<ItemReference<?>> references)
  {
    StringBuilder sb=new StringBuilder();
    sb.append("<html><body>");
    buildHtmlForCrafting(sb,references);
    buildHtmlForTaskItems(sb,references);
    buildHtmlForQuestsAndDeeds(sb,references);
    buildHtmlForBarterers(sb,references);
    buildHtmlForVendors(sb,references);
    buildHtmlForSets(sb,references);
    buildHtmlForContainers(sb,references);
    buildHtmlForMeldingRecipes(sb,references);
    sb.append("</body></html>");
    return sb.toString();
  }

  @SuppressWarnings("unchecked")
  private <T extends Identifiable> List<ItemReference<T>> getReferences(List<ItemReference<?>> references, Class<T> clazz)
  {
    List<ItemReference<T>> recipes=new ArrayList<ItemReference<T>>();
    for(ItemReference<?> reference : references)
    {
      Identifiable source=reference.getSource();
      if (clazz.isAssignableFrom(source.getClass()))
      {
        recipes.add((ItemReference<T>)reference);
      }
    }
    return recipes;
  }

  private void buildHtmlForCrafting(StringBuilder sb, List<ItemReference<?>> references)
  {
    List<ItemReference<Recipe>> recipeReferences=getReferences(references,Recipe.class);
    Profession currentProfession=null;
    int currentTier=0;
    if (recipeReferences.size()>0)
    {
      sb.append("<h1>Crafting</h1>");
      for(ItemReference<Recipe> recipeReference : recipeReferences)
      {
        Recipe recipe=recipeReference.getSource();
        Profession profession=recipe.getProfession();
        int tier=recipe.getTier();
        if (!profession.equals(currentProfession))
        {
          sb.append("<h2>").append(profession).append("</h2>");
          currentTier=0;
          currentProfession=profession;
        }
        if (tier!=currentTier)
        {
          sb.append("<h3>Tier ").append(tier).append("</h3>");
          currentTier=tier;
        }
        buildHtmlForRecipeReference(sb,recipe,recipeReference.getRoles());
      }
    }
  }

  private void buildHtmlForRecipeReference(StringBuilder sb, Recipe recipe, List<ItemRole> roles)
  {
    sb.append("<p>Found as ");
    int index=0;
    for(ItemRole role : roles)
    {
      if (index>0)
      {
        sb.append(" / ");
      }
      if (role==ItemRole.RECIPE_INGREDIENT) sb.append("ingredient");
      if (role==ItemRole.RECIPE_CRITICAL_INGREDIENT) sb.append("optional ingredient");
      if (role==ItemRole.RECIPE_RESULT) sb.append("result");
      if (role==ItemRole.RECIPE_CRITICAL_RESULT) sb.append("critical result");
      if (role==ItemRole.RECIPE_PROVIDES_RECIPE) sb.append("provider");
      index++;
    }
    sb.append(" in recipe ");
    sb.append("<b>");
    PageIdentifier to=ReferenceConstants.getRecipeReference(recipe.getIdentifier());
    HtmlUtils.printLink(sb,to.getFullAddress(),recipe.getName());
    sb.append("</b></p>");
  }

  private void buildHtmlForTaskItems(StringBuilder sb, List<ItemReference<?>> references)
  {
    List<ItemReference<Task>> taskReferences=getReferences(references,Task.class);
    if (taskReferences.size()>0)
    {
      sb.append("<h1>Tasks</h1>");
      for(ItemReference<Task> taskReference : taskReferences)
      {
        buildHtmlForTaskReference(sb,taskReference.getSource());
      }
    }
  }

  private void buildHtmlForTaskReference(StringBuilder sb, Task task)
  {
    sb.append("<p>Required for task: <b>");
    PageIdentifier to=ReferenceConstants.getAchievableReference(task.getQuest());
    HtmlUtils.printLink(sb,to.getFullAddress(),task.getQuest().getName());
    sb.append("</b></p>");
  }

  private void buildHtmlForQuestsAndDeeds(StringBuilder sb, List<ItemReference<?>> references)
  {
    List<ItemReference<Achievable>> achievableReferences=getReferences(references,Achievable.class);
    if (achievableReferences.size()>0)
    {
      sb.append("<h1>Quests and deeds</h1>");
      for(ItemReference<Achievable> achievableReference : achievableReferences)
      {
        buildHtmlForAchievableReference(sb,achievableReference.getSource());
      }
    }
  }

  private void buildHtmlForAchievableReference(StringBuilder sb, Achievable achievable)
  {
    sb.append("<p>Reward for ");
    boolean isQuest=(achievable instanceof QuestDescription);
    String type=isQuest?"quest ":"deed ";
    sb.append(type);
    sb.append("<b>");
    PageIdentifier to=ReferenceConstants.getAchievableReference(achievable);
    HtmlUtils.printLink(sb,to.getFullAddress(),achievable.getName());
    sb.append("</b></p>");
  }

  private void buildHtmlForBarterers(StringBuilder sb, List<ItemReference<?>> references)
  {
    List<ItemReference<BarterNpc>> bartererReferences=getReferences(references,BarterNpc.class);
    if (bartererReferences.size()>0)
    {
      sb.append("<h1>Barterers</h1>");
      for(ItemReference<BarterNpc> bartererReference : bartererReferences)
      {
        buildHtmlForBartererReference(sb,bartererReference);
      }
    }
  }

  private void buildHtmlForBartererReference(StringBuilder sb, ItemReference<BarterNpc> bartererReference)
  {
    List<ItemRole> roles=bartererReference.getRoles();
    for(ItemRole role : roles)
    {
      if (role==ItemRole.BARTERER_GIVEN)
      {
        sb.append("<p>Received from barterer ");
      }
      else if (role==ItemRole.BARTERER_RECEIVED)
      {
        sb.append("<p>Accepted by barterer ");
      }
      sb.append("<b>");
      BarterNpc barterer=bartererReference.getSource();
      PageIdentifier to=ReferenceConstants.getBartererReference(barterer.getIdentifier());
      HtmlUtils.printLink(sb,to.getFullAddress(),getNpcLabel(barterer.getNpc()));
      sb.append("</b></p>");
    }
  }

  private void buildHtmlForVendors(StringBuilder sb, List<ItemReference<?>> references)
  {
    List<ItemReference<VendorNpc>> vendorReferences=getReferences(references,VendorNpc.class);
    if (vendorReferences.size()>0)
    {
      sb.append("<h1>Vendors</h1>");
      for(ItemReference<VendorNpc> vendorReference : vendorReferences)
      {
        buildHtmlForVendorReference(sb,vendorReference.getSource());
      }
    }
  }

  private void buildHtmlForVendorReference(StringBuilder sb, VendorNpc vendor)
  {
    sb.append("<p>Sold by ");
    sb.append("<b>");
    PageIdentifier to=ReferenceConstants.getVendorReference(vendor.getIdentifier());
    HtmlUtils.printLink(sb,to.getFullAddress(),getNpcLabel(vendor.getNpc()));
    sb.append("</b></p>");
  }

  private void buildHtmlForSets(StringBuilder sb, List<ItemReference<?>> references)
  {
    List<ItemReference<ItemsSet>> setReferences=getReferences(references,ItemsSet.class);
    if (setReferences.size()>0)
    {
      sb.append("<h1>Sets</h1>");
      for(ItemReference<ItemsSet> setReference : setReferences)
      {
        buildHtmlForSetReference(sb,setReference.getSource());
      }
    }
  }

  private void buildHtmlForSetReference(StringBuilder sb, ItemsSet itemsSet)
  {
    sb.append("<p>Member of ");
    sb.append("<b>");
    PageIdentifier to=ReferenceConstants.getItemsSetReference(itemsSet.getIdentifier());
    HtmlUtils.printLink(sb,to.getFullAddress(),itemsSet.getName());
    sb.append("</b></p>");
  }

  private void buildHtmlForContainers(StringBuilder sb, List<ItemReference<?>> references)
  {
    List<ItemReference<ItemsContainer>> containerReferences=getReferences(references,ItemsContainer.class);
    if (containerReferences.size()>0)
    {
      sb.append("<h1>Containers</h1>");
      for(ItemReference<ItemsContainer> containerReference : containerReferences)
      {
        buildHtmlForContainerReference(sb,containerReference.getSource().getIdentifier());
      }
    }
  }

  private void buildHtmlForContainerReference(StringBuilder sb, int itemId)
  {
    sb.append("<p>Found in ");
    sb.append("<b>");
    PageIdentifier to=ReferenceConstants.getItemReference(itemId);
    Item item=ItemsManager.getInstance().getItem(itemId);
    String itemName=item.getName();
    HtmlUtils.printLink(sb,to.getFullAddress(),itemName);
    sb.append("</b></p>");
  }

  private void buildHtmlForMeldingRecipes(StringBuilder sb, List<ItemReference<?>> references)
  {
    List<ItemReference<RelicMeldingRecipe>> recipeReferences=getReferences(references,RelicMeldingRecipe.class);
    if (recipeReferences.size()>0)
    {
      sb.append("<h1>Melding recipes</h1>");
      for(ItemReference<RelicMeldingRecipe> recipeReference : recipeReferences)
      {
        buildHtmlForMeldingRecipeReference(sb,recipeReference);
      }
    }
  }

  private void buildHtmlForMeldingRecipeReference(StringBuilder sb, ItemReference<RelicMeldingRecipe> recipeReference)
  {
    RelicMeldingRecipe recipe=recipeReference.getSource();
    sb.append("<p>Found as ");
    int index=0;
    for(ItemRole role : recipeReference.getRoles())
    {
      if (index>0)
      {
        sb.append(" / ");
      }
      if (role==ItemRole.RECIPE_RESULT) sb.append("result");
      index++;
    }
    sb.append(" in recipe ");
    sb.append("<b>");
    PageIdentifier to=ReferenceConstants.getMeldingRecipeReference(recipe.getIdentifier());
    String recipeName=recipe.getName();
    HtmlUtils.printLink(sb,to.getFullAddress(),recipeName);
    sb.append("</b></p>");
  }

  private String getNpcLabel(NpcDescription npc)
  {
    String name=npc.getName();
    String label=name;
    String title=npc.getTitle();
    if (title.length()>0)
    {
      label=label+" ("+title+")";
    }
    return label;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    _parent=null;
    _display=null;
  }
}
