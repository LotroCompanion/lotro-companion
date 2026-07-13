package delta.games.lotro.gui.lore.items.form;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JEditorPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.common.ui.swing.navigator.PageIdentifier;
import delta.common.utils.html.HtmlConstants;
import delta.games.lotro.character.classes.ClassDescription;
import delta.games.lotro.character.classes.initialGear.InitialGearElement;
import delta.games.lotro.character.races.RaceDescription;
import delta.games.lotro.common.Identifiable;
import delta.games.lotro.gui.common.navigation.ReferenceConstants;
import delta.games.lotro.gui.utils.l10n.Labels;
import delta.games.lotro.lore.agents.mobs.MobDescription;
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
import delta.games.lotro.lore.rewardsTrack.RewardsTrack;
import delta.games.lotro.lore.tasks.Task;
import delta.games.lotro.lore.trade.barter.BarterNpc;
import delta.games.lotro.lore.trade.vendor.VendorNpc;
import delta.games.lotro.lore.webStore.WebStoreItem;
import delta.games.lotro.lore.xrefs.Reference;
import delta.games.lotro.lore.xrefs.items.ItemReferencesBuilder;
import delta.games.lotro.lore.xrefs.items.ItemRole;
import delta.games.lotro.utils.html.HtmlUtils;

/**
 * Controller to display references to an item.
 * @author DAM
 */
public class ItemReferencesDisplayController
{
  private static final String H1="<h1>";
  private static final String END_H1="</h1>";

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
    List<Reference<?,ItemRole>> references=getReferences(itemId);
    if (references.isEmpty())
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

  private List<Reference<?,ItemRole>> getReferences(int itemId)
  {
    ItemReferencesBuilder builder=new ItemReferencesBuilder();
    List<Reference<?,ItemRole>> references=builder.inspectItem(itemId);
    return references;
  }

  private String getHtml(List<Reference<?,ItemRole>> references)
  {
    StringBuilder sb=new StringBuilder();
    sb.append("<html><body style='width: 500px'>");
    buildHtmlForCrafting(sb,references);
    buildHtmlForTaskItems(sb,references);
    buildHtmlForQuestsAndDeeds(sb,references);
    buildHtmlForRewardTracks(sb,references);
    buildHtmlForBarterers(sb,references);
    buildHtmlForVendors(sb,references);
    buildHtmlForSets(sb,references);
    buildHtmlForContainers(sb,references);
    buildHtmlForMobs(sb,references);
    buildHtmlForMeldingRecipes(sb,references);
    buildHtmlForSameCosmetics(sb,references);
    buildHtmlForWebStoreItems(sb,references);
    buildHtmlForInitialGearReference(sb,references);
    sb.append("</body></html>");
    return sb.toString();
  }

  private <T extends Identifiable> List<Reference<T,ItemRole>> getReferences(List<Reference<?,ItemRole>> references, Class<T> clazz)
  {
    return getReferences(references,clazz,null);
  }

  @SuppressWarnings("unchecked")
  private <T> List<Reference<T,ItemRole>> getReferences(List<Reference<?,ItemRole>> references, Class<T> clazz, ItemRole role)
  {
    List<Reference<T,ItemRole>> ret=new ArrayList<Reference<T,ItemRole>>();
    for(Reference<?,ItemRole> reference : references)
    {
      if ((role==null) || (reference.getRoles().contains(role)))
      {
        Object source=reference.getSource();
        if (clazz.isAssignableFrom(source.getClass()))
        {
          ret.add((Reference<T,ItemRole>)reference);
        }
      }
    }
    return ret;
  }

  private void buildHtmlForCrafting(StringBuilder sb, List<Reference<?,ItemRole>> references)
  {
    List<Reference<Recipe,ItemRole>> recipeReferences=getReferences(references,Recipe.class);
    Profession currentProfession=null;
    int currentTier=0;
    if (!recipeReferences.isEmpty())
    {
      sb.append(H1);
      sb.append(Labels.getLabel("item.form.references.crafting.chapter"));
      sb.append(END_H1);
      for(Reference<Recipe,ItemRole> recipeReference : recipeReferences)
      {
        Recipe recipe=recipeReference.getSource();
        Profession profession=recipe.getProfession();
        int tier=recipe.getTier();
        if (profession!=currentProfession)
        {
          sb.append("<h2>").append(profession).append("</h2>");
          currentTier=0;
          currentProfession=profession;
        }
        if (tier!=currentTier)
        {
          sb.append("<h3>");
          sb.append(Labels.getLabel("item.form.references.crafting.tier",new Object[]{Integer.valueOf(tier)}));
          sb.append("</h3>");
          currentTier=tier;
        }
        buildHtmlForRecipeReference(sb,recipe,recipeReference.getRoles());
      }
    }
  }

  private void buildHtmlForRecipeReference(StringBuilder sb, Recipe recipe, List<ItemRole> roles)
  {
    sb.append(HtmlConstants.START_PARAGRAPH);
    String rolesLabel=buildRolesLabel(roles);
    PageIdentifier to=ReferenceConstants.getRecipeReference(recipe.getIdentifier());
    String link=buildLink(to,recipe.getName());
    String line=Labels.getLabel("item.form.references.crafting.fromRecipe",new Object[]{rolesLabel,link});
    sb.append(line);
    sb.append(HtmlConstants.END_PARAGRAPH);
  }

  private String buildRolesLabel(List<ItemRole> roles)
  {
    StringBuilder sb=new StringBuilder();
    int index=0;
    for(ItemRole role : roles)
    {
      if (index>0)
      {
        sb.append(" / ");
      }
      String roleStr=Labels.getLabel("item.form.references.role."+role.name());
      sb.append(roleStr);
      index++;
    }
    return sb.toString();
  }

  private String buildLink(PageIdentifier to, String label)
  {
    StringBuilder sb=new StringBuilder();
    sb.append(HtmlConstants.START_BOLD);
    HtmlUtils.printLink(sb,to.getFullAddress(),label);
    sb.append(HtmlConstants.END_BOLD);
    return sb.toString();
  }

  private void buildHtmlForTaskItems(StringBuilder sb, List<Reference<?,ItemRole>> references)
  {
    List<Reference<Task,ItemRole>> taskReferences=getReferences(references,Task.class);
    if (!taskReferences.isEmpty())
    {
      sb.append(H1);
      sb.append(Labels.getLabel("item.form.references.tasks.chapter"));
      sb.append(END_H1);
      for(Reference<Task,ItemRole> taskReference : taskReferences)
      {
        buildHtmlForTaskReference(sb,taskReference.getSource());
      }
    }
  }

  private void buildHtmlForTaskReference(StringBuilder sb, Task task)
  {
    sb.append(HtmlConstants.START_PARAGRAPH);
    PageIdentifier to=ReferenceConstants.getAchievableReference(task.getQuest());
    String link=buildLink(to,task.getQuest().getName());
    String line=Labels.getLabel("item.form.references.tasks.required",new Object[]{link});
    sb.append(line);
    sb.append(HtmlConstants.END_PARAGRAPH);
  }

  private void buildHtmlForQuestsAndDeeds(StringBuilder sb, List<Reference<?,ItemRole>> references)
  {
    List<Reference<Achievable,ItemRole>> achievableReferences=getReferences(references,Achievable.class);
    if (!achievableReferences.isEmpty())
    {
      sb.append(H1);
      sb.append(Labels.getLabel("item.form.references.achievables.chapter"));
      sb.append(END_H1);
      for(Reference<Achievable,ItemRole> achievableReference : achievableReferences)
      {
        for(ItemRole role : achievableReference.getRoles())
        {
          buildHtmlForAchievableReference(sb,achievableReference.getSource(),role);
        }
      }
    }
  }

  private void buildHtmlForAchievableReference(StringBuilder sb, Achievable achievable, ItemRole role)
  {
    sb.append(HtmlConstants.START_PARAGRAPH);
    String text=getTextForAchievableReference(achievable,role);
    sb.append(text);
    sb.append(HtmlConstants.END_PARAGRAPH);
  }

  private String getTextForAchievableReference(Achievable achievable, ItemRole role)
  {
    boolean isQuest=(achievable instanceof QuestDescription);
    PageIdentifier to=ReferenceConstants.getAchievableReference(achievable);
    String link=buildLink(to,achievable.getName());
    Integer selector=Integer.valueOf(isQuest?1:0);
    if (role==ItemRole.ACHIEVABLE_REWARD)
    {
      return Labels.getLabel("item.form.references.achievables.reward",new Object[]{selector,link});
    }
    if (role==ItemRole.ACHIEVABLE_INVOLVED)
    {
      return Labels.getLabel("item.form.references.achievables.involved",new Object[]{selector,link});
    }
    return "? for ";
  }

  private void buildHtmlForBarterers(StringBuilder sb, List<Reference<?,ItemRole>> references)
  {
    List<Reference<BarterNpc,ItemRole>> bartererReferences=getReferences(references,BarterNpc.class);
    if (!bartererReferences.isEmpty())
    {
      sb.append(H1);
      sb.append(Labels.getLabel("item.form.references.barterers.chapter"));
      sb.append(END_H1);
      for(Reference<BarterNpc,ItemRole> bartererReference : bartererReferences)
      {
        buildHtmlForBartererReference(sb,bartererReference);
      }
    }
  }

  private void buildHtmlForRewardTracks(StringBuilder sb, List<Reference<?,ItemRole>> references)
  {
    List<Reference<RewardsTrack,ItemRole>> rewardsTrackReferences=getReferences(references,RewardsTrack.class);
    if (!rewardsTrackReferences.isEmpty())
    {
      sb.append(H1);
      sb.append(Labels.getLabel("item.form.references.rewardTracks.chapter"));
      sb.append(END_H1);
      for(Reference<RewardsTrack,ItemRole> rewardsTrackReference : rewardsTrackReferences)
      {
        buildHtmlForRewardTracksReference(sb,rewardsTrackReference.getSource());
      }
    }
  }

  private void buildHtmlForRewardTracksReference(StringBuilder sb, RewardsTrack rewardTrack)
  {
    sb.append(HtmlConstants.START_PARAGRAPH);
    String line=Labels.getLabel("item.form.references.rewardTracks.reward",new Object[]{rewardTrack.getName()});
    sb.append(line);
    sb.append(HtmlConstants.END_PARAGRAPH);
  }

  private void buildHtmlForBartererReference(StringBuilder sb, Reference<BarterNpc,ItemRole> bartererReference)
  {
    List<ItemRole> roles=bartererReference.getRoles();
    for(ItemRole role : roles)
    {
      BarterNpc barterer=bartererReference.getSource();
      PageIdentifier to=ReferenceConstants.getBartererReference(barterer.getIdentifier());
      String npcStr=getNpcLabel(barterer.getNpc());
      String link=buildLink(to,npcStr);
      sb.append(HtmlConstants.START_PARAGRAPH);
      if (role==ItemRole.BARTERER_GIVEN)
      {
        String line=Labels.getLabel("item.form.references.role.BARTERER_GIVEN",new Object[]{link});
        sb.append(line);
      }
      else if (role==ItemRole.BARTERER_RECEIVED)
      {
        String line=Labels.getLabel("item.form.references.role.BARTERER_RECEIVED",new Object[]{link});
        sb.append(line);
      }
      sb.append(HtmlConstants.END_PARAGRAPH);
    }
  }

  private void buildHtmlForVendors(StringBuilder sb, List<Reference<?,ItemRole>> references)
  {
    List<Reference<VendorNpc,ItemRole>> vendorReferences=getReferences(references,VendorNpc.class);
    if (!vendorReferences.isEmpty())
    {
      sb.append(H1);
      sb.append(Labels.getLabel("item.form.references.vendors.chapter"));
      sb.append(END_H1);
      for(Reference<VendorNpc,ItemRole> vendorReference : vendorReferences)
      {
        buildHtmlForVendorReference(sb,vendorReference.getSource());
      }
    }
  }

  private void buildHtmlForVendorReference(StringBuilder sb, VendorNpc vendor)
  {
    sb.append(HtmlConstants.START_PARAGRAPH);
    PageIdentifier to=ReferenceConstants.getVendorReference(vendor.getIdentifier());
    String npcStr=getNpcLabel(vendor.getNpc());
    String link=buildLink(to,npcStr);
    String line=Labels.getLabel("item.form.references.vendors.soldBy",new Object[]{link});
    sb.append(line);
    sb.append(HtmlConstants.END_PARAGRAPH);
  }

  private void buildHtmlForSets(StringBuilder sb, List<Reference<?,ItemRole>> references)
  {
    List<Reference<ItemsSet,ItemRole>> setReferences=getReferences(references,ItemsSet.class);
    if (!setReferences.isEmpty())
    {
      sb.append(H1);
      sb.append(Labels.getLabel("item.form.references.sets.chapter"));
      sb.append(END_H1);
      for(Reference<ItemsSet,ItemRole> setReference : setReferences)
      {
        buildHtmlForSetReference(sb,setReference.getSource());
      }
    }
  }

  private void buildHtmlForSetReference(StringBuilder sb, ItemsSet itemsSet)
  {
    sb.append(HtmlConstants.START_PARAGRAPH);
    PageIdentifier to=ReferenceConstants.getItemsSetReference(itemsSet.getIdentifier());
    String link=buildLink(to,itemsSet.getName());
    String line=Labels.getLabel("item.form.references.sets.memberOf",new Object[]{link});
    sb.append(line);
    sb.append(HtmlConstants.END_PARAGRAPH);
  }

  private void buildHtmlForContainers(StringBuilder sb, List<Reference<?,ItemRole>> references)
  {
    List<Reference<ItemsContainer,ItemRole>> containerReferences=getReferences(references,ItemsContainer.class);
    if (!containerReferences.isEmpty())
    {
      sb.append(H1);
      sb.append(Labels.getLabel("item.form.references.containers.chapter"));
      sb.append(END_H1);
      for(Reference<ItemsContainer,ItemRole> containerReference : containerReferences)
      {
        buildHtmlForContainerReference(sb,containerReference.getSource().getIdentifier());
      }
    }
  }

  private void buildHtmlForContainerReference(StringBuilder sb, int itemId)
  {
    sb.append(HtmlConstants.START_PARAGRAPH);
    PageIdentifier to=ReferenceConstants.getItemReference(itemId);
    Item item=ItemsManager.getInstance().getItem(itemId);
    String itemName=item.getName();
    String link=buildLink(to,itemName);
    String line=Labels.getLabel("item.form.references.containers.foundIn",new Object[]{link});
    sb.append(line);
    sb.append(HtmlConstants.END_PARAGRAPH);
  }

  private void buildHtmlForMobs(StringBuilder sb, List<Reference<?,ItemRole>> references)
  {
    List<Reference<MobDescription,ItemRole>> mobReferences=getReferences(references,MobDescription.class);
    if (!mobReferences.isEmpty())
    {
      sb.append(H1);
      sb.append(Labels.getLabel("item.form.references.mobDrops.chapter"));
      sb.append(END_H1);
      for(Reference<MobDescription,ItemRole> mobReference : mobReferences)
      {
        buildHtmlForMobReference(sb,mobReference.getSource());
      }
    }
  }

  private void buildHtmlForMobReference(StringBuilder sb, MobDescription mob)
  {
    sb.append(HtmlConstants.START_PARAGRAPH);
    PageIdentifier to=ReferenceConstants.getMobReference(mob);
    String link=buildLink(to,mob.getName());
    String line=Labels.getLabel("item.form.references.mobDrops.dropsFrom",new Object[]{link});
    sb.append(line);
    sb.append(HtmlConstants.END_PARAGRAPH);
  }

  private void buildHtmlForMeldingRecipes(StringBuilder sb, List<Reference<?,ItemRole>> references)
  {
    List<Reference<RelicMeldingRecipe,ItemRole>> recipeReferences=getReferences(references,RelicMeldingRecipe.class);
    if (!recipeReferences.isEmpty())
    {
      sb.append(H1);
      sb.append(Labels.getLabel("item.form.references.meldingRecipes.chapter"));
      sb.append(END_H1);
      for(Reference<RelicMeldingRecipe,ItemRole> recipeReference : recipeReferences)
      {
        buildHtmlForMeldingRecipeReference(sb,recipeReference);
      }
    }
  }

  private void buildHtmlForMeldingRecipeReference(StringBuilder sb, Reference<RelicMeldingRecipe,ItemRole> recipeReference)
  {
    sb.append(HtmlConstants.START_PARAGRAPH);
    RelicMeldingRecipe recipe=recipeReference.getSource();
    PageIdentifier to=ReferenceConstants.getMeldingRecipeReference(recipe.getIdentifier());
    String link=buildLink(to,recipe.getName());
    String line=Labels.getLabel("item.form.references.meldingRecipes.foundInRecipe",new Object[]{link});
    sb.append(line);
    sb.append(HtmlConstants.END_PARAGRAPH);
  }

  private String getNpcLabel(NpcDescription npc)
  {
    String name=npc.getName();
    String label=name;
    String title=npc.getTitle();
    if (!title.isEmpty())
    {
      label=label+" ("+title+")";
    }
    return label;
  }

  private void buildHtmlForSameCosmetics(StringBuilder sb, List<Reference<?,ItemRole>> references)
  {
    List<Reference<Item,ItemRole>> sameCosmeticsReferences=getReferences(references,Item.class,ItemRole.SAME_COSMETICS);
    if (!sameCosmeticsReferences.isEmpty())
    {
      sb.append(H1);
      sb.append(Labels.getLabel("item.form.references.sameCosmetics.chapter"));
      sb.append(END_H1);
      for(Reference<Item,ItemRole> sameCosmeticsReference : sameCosmeticsReferences)
      {
        buildHtmlForSameCosmeticsReference(sb,sameCosmeticsReference.getSource());
      }
    }
  }

  private void buildHtmlForSameCosmeticsReference(StringBuilder sb, Item item)
  {
    sb.append(HtmlConstants.START_PARAGRAPH);
    sb.append(HtmlConstants.START_BOLD);
    PageIdentifier to=ReferenceConstants.getItemReference(item.getIdentifier());
    HtmlUtils.printLink(sb,to.getFullAddress(),item.getName());
    sb.append(HtmlConstants.END_BOLD);
    sb.append(HtmlConstants.END_PARAGRAPH);
  }

  private void buildHtmlForWebStoreItems(StringBuilder sb, List<Reference<?,ItemRole>> references)
  {
    List<Reference<WebStoreItem,ItemRole>> webStoreReferences=getReferences(references,WebStoreItem.class,ItemRole.WEB_STORE_ITEM);
    if (!webStoreReferences.isEmpty())
    {
      sb.append(H1);
      sb.append(Labels.getLabel("item.form.references.lotroStore.chapter"));
      sb.append(END_H1);
    }
  }

  private void buildHtmlForInitialGearReference(StringBuilder sb, List<Reference<?,ItemRole>> references)
  {
    List<Reference<InitialGearElement,ItemRole>> webStoreReferences=getReferences(references,InitialGearElement.class,ItemRole.INITIAL_GEAR_FOR_CLASS);
    for(Reference<InitialGearElement,ItemRole> reference : webStoreReferences)
    {
      sb.append(HtmlConstants.START_PARAGRAPH);
      InitialGearElement element=reference.getSource();
      ClassDescription c=element.getClassDescription();
      PageIdentifier to=ReferenceConstants.getClassReference(c);
      String classLink=buildLink(to,c.getName());
      String raceLink="";
      RaceDescription race=element.getRequiredRace();
      if (race!=null)
      {
        PageIdentifier racePageId=ReferenceConstants.getRaceReference(race);
        raceLink=buildLink(racePageId,race.getName());
      }
      Integer hasRace=Integer.valueOf((race!=null)?1:0);
      String line=Labels.getLabel("item.form.references.initialGear",new Object[]{classLink,hasRace,raceLink});
      sb.append(line);
      sb.append(HtmlConstants.END_PARAGRAPH);
    }
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
