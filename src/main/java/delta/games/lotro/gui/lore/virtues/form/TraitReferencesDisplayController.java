package delta.games.lotro.gui.lore.virtues.form;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JEditorPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.common.ui.swing.navigator.PageIdentifier;
import delta.games.lotro.character.races.RaceDescription;
import delta.games.lotro.common.CharacterClass;
import delta.games.lotro.gui.common.navigation.ReferenceConstants;
import delta.games.lotro.lore.quests.Achievable;
import delta.games.lotro.lore.quests.QuestDescription;
import delta.games.lotro.lore.xrefs.traits.TraitReference;
import delta.games.lotro.lore.xrefs.traits.TraitReferencesBuilder;
import delta.games.lotro.utils.gui.HtmlUtils;

/**
 * Controller to display references to a trait.
 * @author DAM
 */
public class TraitReferencesDisplayController
{
  // Controllers
  private NavigatorWindowController _parent;
  // UI
  private JEditorPane _display;

  /**
   * Constructor.
   * @param parent Parent window (a navigator).
   * @param traitID Identifier of the trait to show.
   */
  public TraitReferencesDisplayController(NavigatorWindowController parent, int traitID)
  {
    _parent=parent;
    _display=buildDetailsPane(traitID);
  }

  /**
   * Get the managed component.
   * @return the managed component.
   */
  public JEditorPane getComponent()
  {
    return _display;
  }

  private JEditorPane buildDetailsPane(int traitID)
  {
    List<TraitReference<?>> references=getReferences(traitID);
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

  private List<TraitReference<?>> getReferences(int traitID)
  {
    TraitReferencesBuilder builder=new TraitReferencesBuilder();
    List<TraitReference<?>> references=builder.inspectTrait(traitID);
    return references;
  }

  private String getHtml(List<TraitReference<?>> references)
  {
    StringBuilder sb=new StringBuilder();
    sb.append("<html><body>");
    buildHtmlForRace(sb,references);
    buildHtmlForClass(sb,references);
    buildHtmlForQuestsAndDeeds(sb,references);
    sb.append("</body></html>");
    return sb.toString();
  }

  @SuppressWarnings("unchecked")
  private <T> List<TraitReference<T>> getReferences(List<TraitReference<?>> references, Class<T> clazz)
  {
    List<TraitReference<T>> ret=new ArrayList<TraitReference<T>>();
    for(TraitReference<?> reference : references)
    {
      Object source=reference.getSource();
      if (clazz.isAssignableFrom(source.getClass()))
      {
        ret.add((TraitReference<T>)reference);
      }
    }
    return ret;
  }

  private void buildHtmlForRace(StringBuilder sb, List<TraitReference<?>> references)
  {
    List<TraitReference<RaceDescription>> raceReferences=getReferences(references,RaceDescription.class);
    if (!raceReferences.isEmpty())
    {
      for(TraitReference<RaceDescription> raceReference : raceReferences)
      {
        RaceDescription race=raceReference.getSource();
        buildHtmlForRaceReference(sb,race);
      }
    }
  }

  private void buildHtmlForRaceReference(StringBuilder sb, RaceDescription race)
  {
    sb.append("<p>Trait for race ");
    sb.append("<b>");
    PageIdentifier to=ReferenceConstants.getRaceReference(race);
    HtmlUtils.printLink(sb,to.getFullAddress(),race.getName());
    sb.append("</b></p>");
  }

  private void buildHtmlForClass(StringBuilder sb, List<TraitReference<?>> references)
  {
    List<TraitReference<CharacterClass>> classReferences=getReferences(references,CharacterClass.class);
    if (!classReferences.isEmpty())
    {
      for(TraitReference<CharacterClass> classReference : classReferences)
      {
        CharacterClass characterClass=classReference.getSource();
        buildHtmlForClassReference(sb,characterClass);
      }
    }
  }

  private void buildHtmlForClassReference(StringBuilder sb, CharacterClass cClass)
  {
    sb.append("<p>Trait for class ");
    sb.append("<b>");
    PageIdentifier to=ReferenceConstants.getClassReference(cClass);
    HtmlUtils.printLink(sb,to.getFullAddress(),cClass.getLabel());
    sb.append("</b></p>");
  }

  private void buildHtmlForQuestsAndDeeds(StringBuilder sb, List<TraitReference<?>> references)
  {
    List<TraitReference<Achievable>> achievableReferences=getReferences(references,Achievable.class);
    if (!achievableReferences.isEmpty())
    {
      sb.append("<h1>Quests and deeds</h1>");
      for(TraitReference<Achievable> achievableReference : achievableReferences)
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
