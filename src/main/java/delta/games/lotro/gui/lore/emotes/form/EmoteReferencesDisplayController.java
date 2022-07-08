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
import delta.games.lotro.lore.quests.Achievable;
import delta.games.lotro.lore.quests.QuestDescription;
import delta.games.lotro.lore.xrefs.emotes.EmoteReference;
import delta.games.lotro.lore.xrefs.emotes.EmoteReferencesBuilder;
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
    List<EmoteReference<?>> references=getReferences(emoteID);
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

  private List<EmoteReference<?>> getReferences(int emoteID)
  {
    EmoteReferencesBuilder builder=new EmoteReferencesBuilder();
    List<EmoteReference<?>> references=builder.inspectEmote(emoteID);
    return references;
  }

  private String getHtml(List<EmoteReference<?>> references)
  {
    StringBuilder sb=new StringBuilder();
    sb.append("<html><body>");
    buildHtmlForQuestsAndDeeds(sb,references);
    sb.append("</body></html>");
    return sb.toString();
  }

  @SuppressWarnings("unchecked")
  private <T> List<EmoteReference<T>> getReferences(List<EmoteReference<?>> references, Class<T> clazz)
  {
    List<EmoteReference<T>> ret=new ArrayList<EmoteReference<T>>();
    for(EmoteReference<?> reference : references)
    {
      Object source=reference.getSource();
      if (clazz.isAssignableFrom(source.getClass()))
      {
        ret.add((EmoteReference<T>)reference);
      }
    }
    return ret;
  }

  private void buildHtmlForQuestsAndDeeds(StringBuilder sb, List<EmoteReference<?>> references)
  {
    List<EmoteReference<Achievable>> achievableReferences=getReferences(references,Achievable.class);
    if (achievableReferences.size()>0)
    {
      sb.append("<h1>Quests and deeds</h1>");
      for(EmoteReference<Achievable> achievableReference : achievableReferences)
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

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    _parent=null;
    _display=null;
  }
}
