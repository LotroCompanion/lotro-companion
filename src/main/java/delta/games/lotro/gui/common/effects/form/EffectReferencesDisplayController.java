package delta.games.lotro.gui.common.effects.form;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JEditorPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.common.ui.swing.navigator.PageIdentifier;
import delta.common.utils.html.HtmlConstants;
import delta.games.lotro.common.Identifiable;
import delta.games.lotro.common.effects.Effect;
import delta.games.lotro.gui.common.navigation.ReferenceConstants;
import delta.games.lotro.lore.xrefs.Reference;
import delta.games.lotro.lore.xrefs.effects.EffectReferencesBuilder;
import delta.games.lotro.lore.xrefs.effects.EffectRole;
import delta.games.lotro.utils.html.HtmlUtils;

/**
 * Controller to display references to an item.
 * @author DAM
 */
public class EffectReferencesDisplayController
{
  // Controllers
  private NavigatorWindowController _parent;
  // UI
  private JEditorPane _display;

  /**
   * Constructor.
   * @param parent Parent window (a navigator).
   * @param effectID Identifier of the effect to use.
   */
  public EffectReferencesDisplayController(NavigatorWindowController parent, int effectID)
  {
    _parent=parent;
    _display=buildDetailsPane(effectID);
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
    List<Reference<?,EffectRole>> references=getReferences(itemId);
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

  private List<Reference<?,EffectRole>> getReferences(int effectID)
  {
    EffectReferencesBuilder builder=new EffectReferencesBuilder();
    List<Reference<?,EffectRole>> references=builder.inspectEffect(effectID);
    return references;
  }

  private String getHtml(List<Reference<?,EffectRole>> references)
  {
    StringBuilder sb=new StringBuilder();
    sb.append("<html><body style='width: 500px'>");
    buildHtmlForEffect(sb,references);
    sb.append("</body></html>");
    return sb.toString();
  }

  private <T extends Identifiable> List<Reference<T,EffectRole>> getReferences(List<Reference<?,EffectRole>> references, Class<T> clazz)
  {
    return getReferences(references,clazz,null);
  }

  @SuppressWarnings("unchecked")
  private <T extends Identifiable> List<Reference<T,EffectRole>> getReferences(List<Reference<?,EffectRole>> references, Class<T> clazz, EffectRole role)
  {
    List<Reference<T,EffectRole>> recipes=new ArrayList<Reference<T,EffectRole>>();
    for(Reference<?,EffectRole> reference : references)
    {
      if ((role==null) || (reference.getRoles().contains(role)))
      {
        Object source=reference.getSource();
        if (clazz.isAssignableFrom(source.getClass()))
        {
          recipes.add((Reference<T,EffectRole>)reference);
        }
      }
    }
    return recipes;
  }

  private void buildHtmlForEffect(StringBuilder sb, List<Reference<?,EffectRole>> references)
  {
    List<Reference<Effect,EffectRole>> effectReferences=getReferences(references,Effect.class);
    if (!effectReferences.isEmpty())
    {
      sb.append("<h1>Effects</h1>");
      for(Reference<Effect,EffectRole> effectReference : effectReferences)
      {
        Effect effect=effectReference.getSource();
        buildHtmlForEffectReference(sb,effect);
      }
    }
  }

  private void buildHtmlForEffectReference(StringBuilder sb, Effect effect)
  {
    sb.append(HtmlConstants.START_PARAGRAPH);
    sb.append("Found in parent effect ");
    sb.append(HtmlConstants.START_BOLD);
    PageIdentifier to=ReferenceConstants.getEffectReference(effect);
    String effectName=effect.getName();
    if (effectName.isEmpty())
    {
      effectName="(no name)";
    }
    HtmlUtils.printLink(sb,to.getFullAddress(),effectName);
    sb.append(HtmlConstants.END_BOLD);
    sb.append(HtmlConstants.END_PARAGRAPH);
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
