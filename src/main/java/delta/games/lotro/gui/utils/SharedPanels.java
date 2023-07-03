package delta.games.lotro.gui.utils;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.labels.HyperLinkController;
import delta.common.ui.swing.navigator.PageIdentifier;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.BasicCharacterAttributes;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.classes.ClassDescription;
import delta.games.lotro.character.races.RaceDescription;
import delta.games.lotro.character.skills.SkillDescription;
import delta.games.lotro.character.traits.TraitDescription;
import delta.games.lotro.gui.LotroIconsManager;
import delta.games.lotro.gui.common.navigation.ReferenceConstants;
import delta.games.lotro.lore.emotes.EmoteDescription;
import delta.games.lotro.lore.hobbies.HobbyDescription;
import delta.games.lotro.lore.titles.TitleDescription;
import delta.games.lotro.utils.strings.ContextRendering;

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

  /**
   * Build a hobby link panel controller.
   * @param parent Parent window.
   * @param hobby Hobby to show.
   * @return A panel.
   */
  public static IconAndLinkPanelController buildHobbyPanel(WindowController parent, HobbyDescription hobby)
  {
    if (hobby==null)
    {
      return null;
    }
    // Icon
    IconController iconCtrl=IconControllerFactory.buildHobbyIcon(parent,hobby);
    // Link
    PageIdentifier pageId=ReferenceConstants.getHobbyReference(hobby.getIdentifier());
    String text=hobby.getName();
    HyperLinkController linkCtrl=NavigationUtils.buildNavigationLink(parent,text,pageId);
    return new IconAndLinkPanelController(iconCtrl,linkCtrl);
  }

  /**
   * Build a race link panel controller.
   * @param parent Parent window.
   * @param race Race to show.
   * @return A panel.
   */
  public static IconAndLinkPanelController buildRacePanel(WindowController parent, RaceDescription race)
  {
    if (race==null)
    {
      return null;
    }
    // Icon
    IconController iconCtrl=IconControllerFactory.buildRaceIcon(parent,race);
    // Link
    PageIdentifier pageId=ReferenceConstants.getRaceReference(race);
    String text=race.getName();
    HyperLinkController linkCtrl=NavigationUtils.buildNavigationLink(parent,text,pageId);
    return new IconAndLinkPanelController(iconCtrl,linkCtrl);
  }

  /**
   * Build a title link panel controller.
   * @param parent Parent window.
   * @param title Title to show.
   * @return A panel.
   */
  public static IconAndLinkPanelController buildTitlePanel(WindowController parent, TitleDescription title)
  {
    if (title==null)
    {
      return null;
    }
    // Icon
    IconController iconCtrl=IconControllerFactory.buildTitleIcon(parent,title);
    // Link
    PageIdentifier pageId=ReferenceConstants.getTitleReference(title.getIdentifier());
    String rawTitleName=title.getName();
    String text=ContextRendering.render(parent,rawTitleName);
    HyperLinkController linkCtrl=NavigationUtils.buildNavigationLink(parent,text,pageId);
    return new IconAndLinkPanelController(iconCtrl,linkCtrl);
  }

  /**
   * Build a panel suitable for the header of a character column.
   * @param toon Targeted character.
   * @return A new panel.
   */
  public static JPanel buildToonHeaderPanel(CharacterFile toon)
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    // Class icon
    GridBagConstraints c=new GridBagConstraints(0,0,1,2,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    ImageIcon classIcon=null;
    BasicCharacterAttributes attrs=toon.getSummary();
    if (attrs!=null)
    {
      ClassDescription characterClass=attrs.getCharacterClass();
      classIcon=LotroIconsManager.getClassIcon(characterClass.getIconId());
    }
    JLabel classLabel;
    if (classIcon!=null)
    {
      classLabel=new JLabel(classIcon);
    }
    else
    {
      classLabel=new JLabel("(class)");
    }
    panel.add(classLabel,c);
    // Toon name
    String name=toon.getName();
    JLabel nameLabel=GuiFactory.buildLabel(name,16.0f);
    c=new GridBagConstraints(1,0,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(5,2,2,2),0,0);
    panel.add(nameLabel,c);
    return panel;
  }
}
