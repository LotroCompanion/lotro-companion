package delta.games.lotro.gui.common.rewards.form;

import java.awt.Color;

import javax.swing.Icon;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.area.AreaController;
import delta.common.ui.swing.icons.IconsManager;
import delta.common.ui.swing.labels.LabelWithHalo;
import delta.games.lotro.common.rewards.CraftingXpReward;
import delta.games.lotro.lore.crafting.CraftingLevel;
import delta.games.lotro.lore.crafting.Profession;

/**
 * Controller for the UI gadgets of a crafting XP reward.
 * @author DAM
 */
public class CraftingXpRewardGadgetsController extends RewardGadgetsController
{
  /**
   * Constructor.
   * @param parent Parent controller.
   * @param craftingXpReward Crafting XP reward.
   */
  public CraftingXpRewardGadgetsController(AreaController parent, CraftingXpReward craftingXpReward)
  {
    super(parent);
    // Label
    Profession profession=craftingXpReward.getProfession();
    int tier=craftingXpReward.getTier();
    CraftingLevel level=profession.getByTier(tier);
    int xpValue=craftingXpReward.getXp();
    String text=level.getName()+" "+profession.getName()+": "+xpValue+" XP"; // I18n
    Color color=Color.WHITE;
    _label=new LabelWithHalo();
    _label.setText(text);
    _label.setOpaque(false);
    _label.setForeground(color);
    // Icon
    Icon lpIcon=IconsManager.getIcon("/resources/gui/rewards/crafting-xp.png");
    _icon=GuiFactory.buildIconLabel(lpIcon);
  }
}
