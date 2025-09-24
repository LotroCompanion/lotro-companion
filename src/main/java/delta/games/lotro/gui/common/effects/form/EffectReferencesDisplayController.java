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
import delta.games.lotro.character.skills.SkillDescription;
import delta.games.lotro.character.traits.TraitDescription;
import delta.games.lotro.common.Identifiable;
import delta.games.lotro.common.effects.Effect;
import delta.games.lotro.gui.common.navigation.ReferenceConstants;
import delta.games.lotro.lore.agents.mobs.MobDescription;
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
    buildHtmlForEffects(sb,references);
    buildHtmlForSkills(sb,references);
    buildHtmlForTraits(sb,references);
    buildHtmlForMobs(sb,references);
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

  private void buildHtmlForEffects(StringBuilder sb, List<Reference<?,EffectRole>> references)
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
    effectName=fixEmptyName(effectName);
    HtmlUtils.printLink(sb,to.getFullAddress(),effectName);
    sb.append(HtmlConstants.END_BOLD);
    sb.append(HtmlConstants.END_PARAGRAPH);
  }

  private void buildHtmlForSkills(StringBuilder sb, List<Reference<?,EffectRole>> references)
  {
    List<Reference<SkillDescription,EffectRole>> skillReferences=getReferences(references,SkillDescription.class);
    if (!skillReferences.isEmpty())
    {
      sb.append("<h1>Skills</h1>");
      for(Reference<SkillDescription,EffectRole> skillReference : skillReferences)
      {
        SkillDescription skill=skillReference.getSource();
        buildHtmlForSkillReference(sb,skill);
      }
    }
  }

  private void buildHtmlForSkillReference(StringBuilder sb, SkillDescription skill)
  {
    sb.append(HtmlConstants.START_PARAGRAPH);
    sb.append("Found in skill ");
    sb.append(HtmlConstants.START_BOLD);
    PageIdentifier to=ReferenceConstants.getSkillReference(skill.getIdentifier());
    String skillName=skill.getName();
    skillName=fixEmptyName(skillName);
    HtmlUtils.printLink(sb,to.getFullAddress(),skillName);
    sb.append(HtmlConstants.END_BOLD);
    sb.append(HtmlConstants.END_PARAGRAPH);
  }

  private void buildHtmlForTraits(StringBuilder sb, List<Reference<?,EffectRole>> references)
  {
    List<Reference<TraitDescription,EffectRole>> traitReferences=getReferences(references,TraitDescription.class);
    if (!traitReferences.isEmpty())
    {
      sb.append("<h1>Traits</h1>");
      for(Reference<TraitDescription,EffectRole> traitReference : traitReferences)
      {
        TraitDescription trait=traitReference.getSource();
        buildHtmlForTraitReference(sb,trait);
      }
    }
  }

  private void buildHtmlForTraitReference(StringBuilder sb, TraitDescription trait)
  {
    sb.append(HtmlConstants.START_PARAGRAPH);
    sb.append("Found in trait ");
    sb.append(HtmlConstants.START_BOLD);
    PageIdentifier to=ReferenceConstants.getTraitReference(trait.getIdentifier());
    String traitName=trait.getName();
    traitName=fixEmptyName(traitName);
    HtmlUtils.printLink(sb,to.getFullAddress(),traitName);
    sb.append(HtmlConstants.END_BOLD);
    sb.append(HtmlConstants.END_PARAGRAPH);
  }

  private void buildHtmlForMobs(StringBuilder sb, List<Reference<?,EffectRole>> references)
  {
    List<Reference<MobDescription,EffectRole>> mobReferences=getReferences(references,MobDescription.class);
    if (!mobReferences.isEmpty())
    {
      sb.append("<h1>Mobs</h1>");
      for(Reference<MobDescription,EffectRole> mobReference : mobReferences)
      {
        MobDescription mob=mobReference.getSource();
        buildHtmlForMobReference(sb,mob);
      }
    }
  }

  private void buildHtmlForMobReference(StringBuilder sb, MobDescription mob)
  {
    sb.append(HtmlConstants.START_PARAGRAPH);
    sb.append("Used by mob ");
    sb.append(HtmlConstants.START_BOLD);
    PageIdentifier to=ReferenceConstants.getMobReference(mob);
    String mobName=mob.getName();
    mobName=fixEmptyName(mobName);
    HtmlUtils.printLink(sb,to.getFullAddress(),mobName);
    sb.append(HtmlConstants.END_BOLD);
    sb.append(HtmlConstants.END_PARAGRAPH);
  }

  private static String fixEmptyName(String name)
  {
    return name.isEmpty()?"(no name)":name;
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
