package delta.games.lotro.gui.lore.skills.form;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JEditorPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.common.ui.swing.navigator.PageIdentifier;
import delta.games.lotro.character.classes.ClassDescription;
import delta.games.lotro.character.traits.TraitDescription;
import delta.games.lotro.gui.common.navigation.ReferenceConstants;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.xrefs.Reference;
import delta.games.lotro.lore.xrefs.skills.SkillReferencesBuilder;
import delta.games.lotro.lore.xrefs.skills.SkillRole;
import delta.games.lotro.utils.gui.HtmlUtils;

/**
 * Controller to display references to a skill.
 * @author DAM
 */
public class SkillReferencesDisplayController
{
  // Controllers
  private NavigatorWindowController _parent;
  // UI
  private JEditorPane _display;

  /**
   * Constructor.
   * @param parent Parent window (a navigator).
   * @param skillID Identifier of the skill to show.
   */
  public SkillReferencesDisplayController(NavigatorWindowController parent, int skillID)
  {
    _parent=parent;
    _display=buildDetailsPane(skillID);
  }

  /**
   * Get the managed component.
   * @return the managed component.
   */
  public JEditorPane getComponent()
  {
    return _display;
  }

  private JEditorPane buildDetailsPane(int skillID)
  {
    List<Reference<?,SkillRole>> references=getReferences(skillID);
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

  private List<Reference<?,SkillRole>> getReferences(int skillID)
  {
    SkillReferencesBuilder builder=new SkillReferencesBuilder();
    List<Reference<?,SkillRole>> references=builder.inspectSkill(skillID);
    return references;
  }

  private String getHtml(List<Reference<?,SkillRole>> references)
  {
    StringBuilder sb=new StringBuilder();
    sb.append("<html><body>");
    buildHtmlForClass(sb,references);
    buildHtmlForItems(sb,references);
    buildHtmlForTraits(sb,references);
    sb.append("</body></html>");
    return sb.toString();
  }

  @SuppressWarnings("unchecked")
  private <T> List<Reference<T,SkillRole>> getReferences(List<Reference<?,SkillRole>> references, Class<T> clazz)
  {
    List<Reference<T,SkillRole>> ret=new ArrayList<Reference<T,SkillRole>>();
    for(Reference<?,SkillRole> reference : references)
    {
      Object source=reference.getSource();
      if (clazz.isAssignableFrom(source.getClass()))
      {
        ret.add((Reference<T,SkillRole>)reference);
      }
    }
    return ret;
  }

  private void buildHtmlForClass(StringBuilder sb, List<Reference<?,SkillRole>> references)
  {
    List<Reference<ClassDescription,SkillRole>> classReferences=getReferences(references,ClassDescription.class);
    if (!classReferences.isEmpty())
    {
      for(Reference<ClassDescription,SkillRole> classReference : classReferences)
      {
        ClassDescription characterClass=classReference.getSource();
        buildHtmlForClassReference(sb,characterClass);
      }
    }
  }

  private void buildHtmlForClassReference(StringBuilder sb, ClassDescription characterClass)
  {
    sb.append("<p>Skill for class ");
    sb.append("<b>");
    PageIdentifier to=ReferenceConstants.getClassReference(characterClass);
    HtmlUtils.printLink(sb,to.getFullAddress(),characterClass.getName());
    sb.append("</b></p>");
  }

  private void buildHtmlForItems(StringBuilder sb, List<Reference<?,SkillRole>> references)
  {
    List<Reference<Item,SkillRole>> itemReferences=getReferences(references,Item.class);
    if (itemReferences.size()>0)
    {
      for(Reference<Item,SkillRole> itemReference : itemReferences)
      {
        Item item=itemReference.getSource();
        buildHtmlForItem(sb,item);
      }
    }
  }

  private void buildHtmlForItem(StringBuilder sb, Item item)
  {
    sb.append("<p>Granted by item ");
    sb.append("<b>");
    PageIdentifier to=ReferenceConstants.getItemReference(item.getIdentifier());
    HtmlUtils.printLink(sb,to.getFullAddress(),item.getName());
    sb.append("</b></p>");
  }

  private void buildHtmlForTraits(StringBuilder sb, List<Reference<?,SkillRole>> references)
  {
    List<Reference<TraitDescription,SkillRole>> classReferences=getReferences(references,TraitDescription.class);
    if (classReferences.size()>0)
    {
      for(Reference<TraitDescription,SkillRole> classReference : classReferences)
      {
        TraitDescription trait=classReference.getSource();
        buildHtmlForTraitReference(sb,trait);
      }
    }
  }

  private void buildHtmlForTraitReference(StringBuilder sb, TraitDescription trait)
  {
    sb.append("<p>Granted by trait ");
    sb.append("<b>");
    PageIdentifier to=ReferenceConstants.getTraitReference(trait.getIdentifier());
    HtmlUtils.printLink(sb,to.getFullAddress(),trait.getName());
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
