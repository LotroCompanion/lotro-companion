package delta.games.lotro.gui.lore.items.legendary.relics.form;

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
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemsManager;
import delta.games.lotro.lore.items.legendary.relics.RelicsContainer;
import delta.games.lotro.lore.quests.Achievable;
import delta.games.lotro.lore.quests.QuestDescription;
import delta.games.lotro.lore.relics.melding.RelicMeldingRecipe;
import delta.games.lotro.lore.xrefs.Reference;
import delta.games.lotro.lore.xrefs.relics.RelicReferencesBuilder;
import delta.games.lotro.lore.xrefs.relics.RelicRole;
import delta.games.lotro.utils.gui.HtmlUtils;

/**
 * Controller to display references to a relic.
 * @author DAM
 */
public class RelicReferencesDisplayController
{
  // Controllers
  private NavigatorWindowController _parent;
  // UI
  private JEditorPane _display;

  /**
   * Constructor.
   * @param parent Parent window (a navigator).
   * @param relicId Identifier of the relic to show.
   */
  public RelicReferencesDisplayController(NavigatorWindowController parent, int relicId)
  {
    _parent=parent;
    _display=buildDetailsPane(relicId);
  }

  /**
   * Get the managed component.
   * @return the managed component.
   */
  public JEditorPane getComponent()
  {
    return _display;
  }

  private JEditorPane buildDetailsPane(int relicId)
  {
    List<Reference<?,RelicRole>> references=getReferences(relicId);
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

  private List<Reference<?,RelicRole>> getReferences(int itemId)
  {
    RelicReferencesBuilder builder=new RelicReferencesBuilder();
    List<Reference<?,RelicRole>> references=builder.inspectItem(itemId);
    return references;
  }

  private String getHtml(List<Reference<?,RelicRole>> references)
  {
    StringBuilder sb=new StringBuilder();
    sb.append("<html><body>");
    buildHtmlForQuestsAndDeeds(sb,references);
    buildHtmlForContainers(sb,references);
    buildHtmlForMeldingRecipes(sb,references);
    sb.append("</body></html>");
    return sb.toString();
  }

  @SuppressWarnings("unchecked")
  private <T extends Identifiable> List<Reference<T,RelicRole>> getReferences(List<Reference<?,RelicRole>> references, Class<T> clazz)
  {
    List<Reference<T,RelicRole>> ret=new ArrayList<Reference<T,RelicRole>>();
    for(Reference<?,RelicRole> reference : references)
    {
      Object source=reference.getSource();
      if (clazz.isAssignableFrom(source.getClass()))
      {
        ret.add((Reference<T,RelicRole>)reference);
      }
    }
    return ret;
  }

  private void buildHtmlForQuestsAndDeeds(StringBuilder sb, List<Reference<?,RelicRole>> references)
  {
    List<Reference<Achievable,RelicRole>> achievableReferences=getReferences(references,Achievable.class);
    if (achievableReferences.size()>0)
    {
      sb.append("<h1>Quests and deeds</h1>");
      for(Reference<Achievable,RelicRole> achievableReference : achievableReferences)
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

  private void buildHtmlForContainers(StringBuilder sb, List<Reference<?,RelicRole>> references)
  {
    List<Reference<RelicsContainer,RelicRole>> containerReferences=getReferences(references,RelicsContainer.class);
    if (containerReferences.size()>0)
    {
      sb.append("<h1>Containers</h1>");
      for(Reference<RelicsContainer,RelicRole> setReference : containerReferences)
      {
        buildHtmlForContainerReference(sb,setReference.getSource().getIdentifier());
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

  private void buildHtmlForMeldingRecipes(StringBuilder sb, List<Reference<?,RelicRole>> references)
  {
    List<Reference<RelicMeldingRecipe,RelicRole>> recipeReferences=getReferences(references,RelicMeldingRecipe.class);
    if (recipeReferences.size()>0)
    {
      sb.append("<h1>Melding recipes</h1>");
      for(Reference<RelicMeldingRecipe,RelicRole> recipeReference : recipeReferences)
      {
        buildHtmlForRecipeReference(sb,recipeReference);
      }
    }
  }

  private void buildHtmlForRecipeReference(StringBuilder sb, Reference<RelicMeldingRecipe,RelicRole> recipeReference)
  {
    RelicMeldingRecipe recipe=recipeReference.getSource();
    sb.append("<p>Found as ");
    int index=0;
    for(RelicRole role : recipeReference.getRoles())
    {
      if (index>0)
      {
        sb.append(" / ");
      }
      if (role==RelicRole.RECIPE_INGREDIENT) sb.append("ingredient");
      if (role==RelicRole.RECIPE_RESULT) sb.append("result");
      index++;
    }
    sb.append(" in recipe ");
    sb.append("<b>");
    PageIdentifier to=ReferenceConstants.getMeldingRecipeReference(recipe.getIdentifier());
    String recipeName=recipe.getName();
    HtmlUtils.printLink(sb,to.getFullAddress(),recipeName);
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
