package delta.games.lotro.gui.lore.races.form;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JEditorPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.common.ui.swing.navigator.PageIdentifier;
import delta.games.lotro.common.CharacterClass;
import delta.games.lotro.common.Race;
import delta.games.lotro.gui.common.navigation.ReferenceConstants;
import delta.games.lotro.lore.xrefs.races.RaceReference;
import delta.games.lotro.lore.xrefs.races.RaceReferencesBuilder;
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
  public RaceReferencesDisplayController(NavigatorWindowController parent, Race race)
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

  private JEditorPane buildDetailsPane(Race race)
  {
    List<RaceReference<?>> references=getReferences(race);
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

  private List<RaceReference<?>> getReferences(Race race)
  {
    RaceReferencesBuilder builder=new RaceReferencesBuilder();
    List<RaceReference<?>> references=builder.inspectRace(race);
    return references;
  }

  private String getHtml(List<RaceReference<?>> references)
  {
    StringBuilder sb=new StringBuilder();
    sb.append("<html><body>");
    buildHtmlForClass(sb,references);
    sb.append("</body></html>");
    return sb.toString();
  }

  @SuppressWarnings("unchecked")
  private <T> List<RaceReference<T>> getReferences(List<RaceReference<?>> references, Class<T> clazz)
  {
    List<RaceReference<T>> ret=new ArrayList<RaceReference<T>>();
    for(RaceReference<?> reference : references)
    {
      Object source=reference.getSource();
      if (clazz.isAssignableFrom(source.getClass()))
      {
        ret.add((RaceReference<T>)reference);
      }
    }
    return ret;
  }

  private void buildHtmlForClass(StringBuilder sb, List<RaceReference<?>> references)
  {
    List<RaceReference<CharacterClass>> classReferences=getReferences(references,CharacterClass.class);
    if (!classReferences.isEmpty())
    {
      for(RaceReference<CharacterClass> classReference : classReferences)
      {
        CharacterClass characterClass=classReference.getSource();
        buildHtmlForClassReference(sb,characterClass);
      }
    }
  }

  private void buildHtmlForClassReference(StringBuilder sb, CharacterClass cClass)
  {
    sb.append("<p>Allowed class: ");
    sb.append("<b>");
    PageIdentifier to=ReferenceConstants.getClassReference(cClass);
    HtmlUtils.printLink(sb,to.getFullAddress(),cClass.getLabel());
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
