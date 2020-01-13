package delta.games.lotro.gui.items.form;

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
import delta.games.lotro.lore.crafting.recipes.Recipe;
import delta.games.lotro.lore.quests.Achievable;
import delta.games.lotro.lore.quests.QuestDescription;
import delta.games.lotro.lore.trade.barter.BarterNpc;
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
   */
  public ItemReferencesDisplayController(NavigatorWindowController parent)
  {
    _parent=parent;
    _display=buildDetailsPane();
  }

  /**
   * Get the managed component.
   * @return the managed component.
   */
  public JEditorPane getComponent()
  {
    return _display;
  }

  private JEditorPane buildDetailsPane()
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

  /**
   * Set the item to display.
   * @param itemId Item identifier.
   */
  public void setItem(int itemId)
  {
    String html=getHtml(itemId);
    _display.setText(html);
    _display.setCaretPosition(0);
  }

  private String getHtml(int itemId)
  {
    ItemReferencesBuilder builder=new ItemReferencesBuilder();
    List<ItemReference<?>> references=builder.inspectItem(itemId);
    StringBuilder sb=new StringBuilder();
    sb.append("<html><body>");
    buildHtmlForCrafting(sb,references);
    buildHtmlForQuestsAndDeeds(sb,references);
    buildHtmlForBarterers(sb,references);
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
    String currentProfession=null;
    int currentTier=0;
    if (recipeReferences.size()>0)
    {
      sb.append("<h1>Crafting</h1>");
      for(ItemReference<Recipe> recipeReference : recipeReferences)
      {
        Recipe recipe=recipeReference.getSource();
        String profession=recipe.getProfession();
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
        buildHtmlForBartererReference(sb,bartererReference.getSource());
      }
    }
  }

  private void buildHtmlForBartererReference(StringBuilder sb, BarterNpc barterer)
  {
    sb.append("<p>Bartered by ");
    sb.append("<b>");
    PageIdentifier to=ReferenceConstants.getBartererReference(barterer.getIdentifier());
    HtmlUtils.printLink(sb,to.getFullAddress(),barterer.getNpc().getName());
    sb.append("</b></p>");
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
