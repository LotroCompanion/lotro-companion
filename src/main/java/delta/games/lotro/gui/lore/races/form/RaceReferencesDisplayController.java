package delta.games.lotro.gui.lore.races.form;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JEditorPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.common.ui.swing.navigator.PageIdentifier;
import delta.games.lotro.character.classes.ClassDescription;
import delta.games.lotro.character.races.RaceDescription;
import delta.games.lotro.gui.common.navigation.ReferenceConstants;
import delta.games.lotro.lore.xrefs.Reference;
import delta.games.lotro.lore.xrefs.races.RaceReferencesBuilder;
import delta.games.lotro.lore.xrefs.races.RaceRole;
import delta.games.lotro.utils.gui.HtmlUtils;

/**
 * Controller to display references to a race.
 * @author DAM
 */
public class RaceReferencesDisplayController
{
  // Controllers
  private NavigatorWindowController _parent;
  // UI
  private JEditorPane _display;

  /**
   * Constructor.
   * @param parent Parent window (a navigator).
   * @param race Race to use.
   */
  public RaceReferencesDisplayController(NavigatorWindowController parent, RaceDescription race)
  {
    _parent=parent;
    _display=buildDetailsPane(race);
  }

  /**
   * Get the managed component.
   * @return the managed component.
   */
  public JEditorPane getComponent()
  {
    return _display;
  }

  private JEditorPane buildDetailsPane(RaceDescription race)
  {
    List<Reference<?,RaceRole>> references=getReferences(race);
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

  private List<Reference<?,RaceRole>> getReferences(RaceDescription race)
  {
    RaceReferencesBuilder builder=new RaceReferencesBuilder();
    List<Reference<?,RaceRole>> references=builder.inspectRace(race);
    return references;
  }

  private String getHtml(List<Reference<?,RaceRole>> references)
  {
    StringBuilder sb=new StringBuilder();
    sb.append("<html><body>");
    buildHtmlForClass(sb,references);
    sb.append("</body></html>");
    return sb.toString();
  }

  @SuppressWarnings("unchecked")
  private <T> List<Reference<T,RaceRole>> getReferences(List<Reference<?,RaceRole>> references, Class<T> clazz)
  {
    List<Reference<T,RaceRole>> ret=new ArrayList<Reference<T,RaceRole>>();
    for(Reference<?,RaceRole> reference : references)
    {
      Object source=reference.getSource();
      if (clazz.isAssignableFrom(source.getClass()))
      {
        ret.add((Reference<T,RaceRole>)reference);
      }
    }
    return ret;
  }

  private void buildHtmlForClass(StringBuilder sb, List<Reference<?,RaceRole>> references)
  {
    List<Reference<ClassDescription,RaceRole>> classReferences=getReferences(references,ClassDescription.class);
    if (!classReferences.isEmpty())
    {
      for(Reference<ClassDescription,RaceRole> classReference : classReferences)
      {
        ClassDescription characterClass=classReference.getSource();
        buildHtmlForClassReference(sb,characterClass);
      }
    }
  }

  private void buildHtmlForClassReference(StringBuilder sb, ClassDescription characterClass)
  {
    sb.append("<p>Allowed class: ");
    sb.append("<b>");
    PageIdentifier to=ReferenceConstants.getClassReference(characterClass);
    HtmlUtils.printLink(sb,to.getFullAddress(),characterClass.getName());
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
