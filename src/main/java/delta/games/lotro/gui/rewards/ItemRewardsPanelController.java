package delta.games.lotro.gui.rewards;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.games.lotro.common.objects.ObjectItem;
import delta.games.lotro.common.objects.ObjectsSet;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemsManager;

/**
 * Controller for a panel to display a set of items as found in rewards.
 * @author DAM
 */
public class ItemRewardsPanelController
{
  private JPanel _panel;
  private List<ItemRewardGadgetsController> _rewards;

  /**
   * Constructor.
   * @param objects Items to display.
   */
  public ItemRewardsPanelController(ObjectsSet objects)
  {
    _rewards=new ArrayList<ItemRewardGadgetsController>();
    int nbItems=objects.getNbObjectItems();
    for(int i=0;i<nbItems;i++)
    {
      ObjectItem object=objects.getItem(i);
      int id=object.getItemId();
      int count=objects.getQuantity(i);
      Item item=ItemsManager.getInstance().getItem(id);
      ItemRewardGadgetsController itemReward=new ItemRewardGadgetsController(item,count);
      _rewards.add(itemReward);
    }
    _panel=build();
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  public JPanel getPanel()
  {
    return _panel;
  }

  private JPanel build()
  {
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    for(ItemRewardGadgetsController reward : _rewards)
    {
      c.gridx=0;
      ret.add(reward.getLabelIcon(),c);
      c.gridx++;
      ret.add(reward.getLabel(),c);
      c.gridy++;
    }
    return ret;
  }
}
