package delta.games.lotro.gui.common.rewards.form;

import java.awt.Color;

import javax.swing.Icon;

import delta.common.ui.swing.GuiFactory;
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
   * @param craftingXpReward Crafting XP reward.
   */
  public CraftingXpRewardGadgetsController(CraftingXpReward craftingXpReward)
  {
    // Label
    Profession profession=craftingXpReward.getProfession();
    int tier=craftingXpReward.getTier();
    CraftingLevel level=CraftingLevel.getByTier(tier);
    int xpValue=craftingXpReward.getXp();
    String text=level.getLabel()+" "+profession.getLabel()+": "+xpValue+" XP";
    Color color=Color.WHITE;
    _label=new LabelWithHalo();
    _label.setText(text);
    _label.setOpaque(false);
    _label.setForeground(color);
    // Icon
    Icon lpIcon=IconsManager.getIcon("/resources/gui/rewards/crafting-xp.png");
    _labelIcon=GuiFactory.buildIconLabel(lpIcon);
  }
}
