package delta.games.lotro.gui.navigation;

import delta.common.ui.swing.navigator.NavigatorContentsResolver;
import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.gui.lore.billingGroups.BillingGroupPanelFactory;
import delta.games.lotro.gui.lore.crafting.recipes.RecipePanelsFactory;
import delta.games.lotro.gui.lore.deeds.DeedPanelsFactory;
import delta.games.lotro.gui.lore.emotes.EmotePanelFactory;
import delta.games.lotro.gui.lore.hobbies.HobbyPanelFactory;
import delta.games.lotro.gui.lore.items.ItemPanelsFactory;
import delta.games.lotro.gui.lore.nationalities.NationalityPanelsFactory;
import delta.games.lotro.gui.lore.quests.QuestPanelsFactory;
import delta.games.lotro.gui.lore.races.RacePanelsFactory;
import delta.games.lotro.gui.lore.skills.SkillPanelsFactory;
import delta.games.lotro.gui.lore.titles.TitlePanelFactory;
import delta.games.lotro.gui.lore.trade.barter.BartererPanelsFactory;
import delta.games.lotro.gui.lore.trade.vendor.VendorPanelsFactory;
import delta.games.lotro.gui.lore.traits.TraitPanelsFactory;
import delta.games.lotro.gui.lore.virtues.VirtuePanelsFactory;

/**
 * Factory for LOTRO navigator window controllers.
 * @author DAM
 */
public class NavigatorFactory
{
  /**
   * Build a new navigator window controller.
   * @param parent Parent window, if any.
   * @param index Index of the navigator window.
   * @return a new, customized navigator window.
   */
  public static NavigatorWindowController buildNavigator(WindowController parent, int index)
  {
    NavigatorWindowController window=new NavigatorWindowController(parent,index);
    NavigatorContentsResolver resolver=new NavigatorContentsResolver();
    resolver.addFactory(new QuestPanelsFactory(window));
    resolver.addFactory(new DeedPanelsFactory(window));
    resolver.addFactory(new ItemPanelsFactory(window));
    resolver.addFactory(new RecipePanelsFactory(window));
    resolver.addFactory(new BartererPanelsFactory(window));
    resolver.addFactory(new VendorPanelsFactory(window));
    resolver.addFactory(new SkillPanelsFactory(window));
    resolver.addFactory(new TraitPanelsFactory(window));
    resolver.addFactory(new VirtuePanelsFactory(window));
    resolver.addFactory(new TitlePanelFactory(window));
    resolver.addFactory(new RacePanelsFactory(window));
    resolver.addFactory(new BillingGroupPanelFactory(window));
    resolver.addFactory(new EmotePanelFactory(window));
    resolver.addFactory(new NationalityPanelsFactory(window));
    resolver.addFactory(new HobbyPanelFactory(window));
    window.setContentsResolver(resolver);
    return window;
  }
}
