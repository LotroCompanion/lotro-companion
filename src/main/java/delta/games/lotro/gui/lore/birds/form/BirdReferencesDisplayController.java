package delta.games.lotro.gui.lore.birds.form;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JEditorPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.common.ui.swing.navigator.PageIdentifier;
import delta.games.lotro.gui.common.navigation.ReferenceConstants;
import delta.games.lotro.lore.deeds.DeedDescription;
import delta.games.lotro.lore.xrefs.Reference;
import delta.games.lotro.lore.xrefs.birds.BirdReferencesBuilder;
import delta.games.lotro.lore.xrefs.birds.BirdRole;
import delta.games.lotro.utils.html.HtmlUtils;

/**
 * Controller to display references to a bird.
 * @author DAM
 */
public class BirdReferencesDisplayController
{
  // Controllers
  private NavigatorWindowController _parent;
  // UI
  private JEditorPane _display;

  /**
   * Constructor.
   * @param parent Parent window (a navigator).
   * @param birdID Identifier of the bird to show.
   */
  public BirdReferencesDisplayController(NavigatorWindowController parent, int birdID)
  {
    _parent=parent;
    _display=buildDetailsPane(birdID);
  }

  /**
   * Get the managed component.
   * @return the managed component.
   */
  public JEditorPane getComponent()
  {
    return _display;
  }

  private JEditorPane buildDetailsPane(int birdID)
  {
    List<Reference<?,BirdRole>> references=getReferences(birdID);
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

  private List<Reference<?,BirdRole>> getReferences(int birdID)
  {
    BirdReferencesBuilder builder=new BirdReferencesBuilder();
    List<Reference<?,BirdRole>> references=builder.inspectBird(birdID);
    return references;
  }

  private String getHtml(List<Reference<?,BirdRole>> references)
  {
    StringBuilder sb=new StringBuilder();
    sb.append("<html><body>");
    buildHtmlForDeeds(sb,references);
    sb.append("</body></html>");
    return sb.toString();
  }

  @SuppressWarnings("unchecked")
  private <T> List<Reference<T,BirdRole>> getReferences(List<Reference<?,BirdRole>> references, Class<T> clazz)
  {
    List<Reference<T,BirdRole>> ret=new ArrayList<Reference<T,BirdRole>>();
    for(Reference<?,BirdRole> reference : references)
    {
      Object source=reference.getSource();
      if (clazz.isAssignableFrom(source.getClass()))
      {
        ret.add((Reference<T,BirdRole>)reference);
      }
    }
    return ret;
  }

  private void buildHtmlForDeeds(StringBuilder sb, List<Reference<?,BirdRole>> references)
  {
    List<Reference<DeedDescription,BirdRole>> achievableReferences=getReferences(references,DeedDescription.class);
    if (!achievableReferences.isEmpty())
    {
      sb.append("<h1>Deeds</h1>");
      for(Reference<DeedDescription,BirdRole> achievableReference : achievableReferences)
      {
        buildHtmlForDeedReference(sb,achievableReference.getSource());
      }
    }
  }

  private void buildHtmlForDeedReference(StringBuilder sb, DeedDescription deed)
  {
    sb.append("<p>Involved in deed <b>");
    PageIdentifier to=ReferenceConstants.getAchievableReference(deed);
    HtmlUtils.printLink(sb,to.getFullAddress(),deed.getName());
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
