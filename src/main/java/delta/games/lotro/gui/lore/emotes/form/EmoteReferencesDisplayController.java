package delta.games.lotro.gui.lore.emotes.form;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JEditorPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.common.ui.swing.navigator.PageIdentifier;
import delta.games.lotro.gui.common.navigation.ReferenceConstants;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.quests.Achievable;
import delta.games.lotro.lore.quests.QuestDescription;
import delta.games.lotro.lore.xrefs.Reference;
import delta.games.lotro.lore.xrefs.emotes.EmoteReferencesBuilder;
import delta.games.lotro.lore.xrefs.emotes.EmoteRole;
import delta.games.lotro.utils.gui.HtmlUtils;

/**
 * Controller to display references to an emote.
 * @author DAM
 */
public class EmoteReferencesDisplayController
{
  // Controllers
  private NavigatorWindowController _parent;
  // UI
  private JEditorPane _display;

  /**
   * Constructor.
   * @param parent Parent window (a navigator).
   * @param emoteID Identifier of the emote to show.
   */
  public EmoteReferencesDisplayController(NavigatorWindowController parent, int emoteID)
  {
    _parent=parent;
    _display=buildDetailsPane(emoteID);
  }

  /**
   * Get the managed component.
   * @return the managed component.
   */
  public JEditorPane getComponent()
  {
    return _display;
  }

  private JEditorPane buildDetailsPane(int emoteID)
  {
    List<Reference<?,EmoteRole>> references=getReferences(emoteID);
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

  private List<Reference<?,EmoteRole>> getReferences(int emoteID)
  {
    EmoteReferencesBuilder builder=new EmoteReferencesBuilder();
    List<Reference<?,EmoteRole>> references=builder.inspectEmote(emoteID);
    return references;
  }

  private String getHtml(List<Reference<?,EmoteRole>> references)
  {
    StringBuilder sb=new StringBuilder();
    sb.append("<html><body>");
    buildHtmlForQuestsAndDeeds(sb,references);
    buildHtmlForItems(sb,references);
    sb.append("</body></html>");
    return sb.toString();
  }

  @SuppressWarnings("unchecked")
  private <T> List<Reference<T,EmoteRole>> getReferences(List<Reference<?,EmoteRole>> references, Class<T> clazz)
  {
    List<Reference<T,EmoteRole>> ret=new ArrayList<Reference<T,EmoteRole>>();
    for(Reference<?,EmoteRole> reference : references)
    {
      Object source=reference.getSource();
      if (clazz.isAssignableFrom(source.getClass()))
      {
        ret.add((Reference<T,EmoteRole>)reference);
      }
    }
    return ret;
  }

  private void buildHtmlForQuestsAndDeeds(StringBuilder sb, List<Reference<?,EmoteRole>> references)
  {
    List<Reference<Achievable,EmoteRole>> achievableReferences=getReferences(references,Achievable.class);
    if (achievableReferences.size()>0)
    {
      sb.append("<h1>Quests and deeds</h1>"); // I18n
      for(Reference<Achievable,EmoteRole> achievableReference : achievableReferences)
      {
        buildHtmlForAchievableReference(sb,achievableReference.getSource());
      }
    }
  }

  private void buildHtmlForAchievableReference(StringBuilder sb, Achievable achievable)
  {
    sb.append("<p>Reward for "); // I18n
    boolean isQuest=(achievable instanceof QuestDescription);
    String type=isQuest?"quest ":"deed "; // I18n
    sb.append(type);
    sb.append("<b>");
    PageIdentifier to=ReferenceConstants.getAchievableReference(achievable);
    HtmlUtils.printLink(sb,to.getFullAddress(),achievable.getName());
    sb.append("</b></p>");
  }

  private void buildHtmlForItems(StringBuilder sb, List<Reference<?,EmoteRole>> references)
  {
    List<Reference<Item,EmoteRole>> itemReferences=getReferences(references,Item.class);
    if (itemReferences.size()>0)
    {
      for(Reference<Item,EmoteRole> itemReference : itemReferences)
      {
        Item item=itemReference.getSource();
        buildHtmlForItem(sb,item);
      }
    }
  }

  private void buildHtmlForItem(StringBuilder sb, Item item)
  {
    sb.append("<p>Granted by item "); // I18n
    sb.append("<b>");
    PageIdentifier to=ReferenceConstants.getItemReference(item.getIdentifier());
    HtmlUtils.printLink(sb,to.getFullAddress(),item.getName());
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
