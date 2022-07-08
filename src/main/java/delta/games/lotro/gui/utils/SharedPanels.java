package delta.games.lotro.gui.utils;

import delta.common.ui.swing.labels.HyperLinkController;
import delta.common.ui.swing.navigator.PageIdentifier;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.skills.SkillDescription;
import delta.games.lotro.character.traits.TraitDescription;
import delta.games.lotro.gui.common.navigation.ReferenceConstants;
import delta.games.lotro.lore.emotes.EmoteDescription;

/**
 * Factory for shared panels.
 * @author DAM
 */
public class SharedPanels
{
  /**
   * Build a skill link panel controller.
   * @param parent Parent window.
   * @param skill Skill to show.
   * @return A panel.
   */
  public static IconAndLinkPanelController buildSkillPanel(WindowController parent, SkillDescription skill)
  {
    if (skill==null)
    {
      return null;
    }
    // Icon
    IconController iconCtrl=IconControllerFactory.buildSkillIcon(parent,skill);
    // Link
    PageIdentifier pageId=ReferenceConstants.getSkillReference(skill.getIdentifier());
    String text=skill.getName();
    HyperLinkController linkCtrl=NavigationUtils.buildNavigationLink(parent,text,pageId);
    return new IconAndLinkPanelController(iconCtrl,linkCtrl);
  }

  /**
   * Build a trait link panel controller.
   * @param parent Parent window.
   * @param trait Trait to show.
   * @return A panel.
   */
  public static IconAndLinkPanelController buildTraitPanel(WindowController parent, TraitDescription trait)
  {
    if (trait==null)
    {
      return null;
    }
    // Icon
    IconController iconCtrl=IconControllerFactory.buildTraitIcon(parent,trait);
    // Link
    PageIdentifier pageId=ReferenceConstants.getTraitReference(trait.getIdentifier());
    String text=trait.getName();
    HyperLinkController linkCtrl=NavigationUtils.buildNavigationLink(parent,text,pageId);
    return new IconAndLinkPanelController(iconCtrl,linkCtrl);
  }

  /**
   * Build an emote link panel controller.
   * @param parent Parent window.
   * @param emote Emote to show.
   * @return A panel.
   */
  public static IconAndLinkPanelController buildEmotePanel(WindowController parent, EmoteDescription emote)
  {
    if (emote==null)
    {
      return null;
    }
    // Icon
    IconController iconCtrl=IconControllerFactory.buildEmoteIcon(parent,emote);
    // Link
    PageIdentifier pageId=ReferenceConstants.getEmoteReference(emote.getIdentifier());
    String text=emote.getName();
    HyperLinkController linkCtrl=NavigationUtils.buildNavigationLink(parent,text,pageId);
    return new IconAndLinkPanelController(iconCtrl,linkCtrl);
  }
}
