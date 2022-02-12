package delta.games.lotro.gui.character.storage.cosmetics;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.storage.StoredItem;
import delta.games.lotro.character.storage.cosmetics.CosmeticItemsGroup;
import delta.games.lotro.gui.utils.ItemDisplayGadgets;
import delta.games.lotro.lore.items.CountedItem;
import delta.games.lotro.lore.items.ItemProvider;

/**
 * Controller for a panel that shows a cosmetic group.
 * @author DAM
 */
public class SameCosmeticsGroupPanelController
{
  // UI
  private List<ItemDisplayGadgets> _gadgets;
  private JPanel _panel;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param group Group to show.
   */
  public SameCosmeticsGroupPanelController(WindowController parent, CosmeticItemsGroup group)
  {
    init(parent,group);
    _panel=buildPanel();
  }

  private void init(WindowController parent, CosmeticItemsGroup group)
  {
    _gadgets=new ArrayList<ItemDisplayGadgets>();
    for(StoredItem storedItem : group.getItems())
    {
      CountedItem<ItemProvider> countedItem=storedItem.getItem();
      int itemID=countedItem.getId();
      int count=countedItem.getQuantity();
      String comment=storedItem.getLocation().toString();
      ItemDisplayGadgets gadgets=new ItemDisplayGadgets(parent,itemID,count,comment);
      _gadgets.add(gadgets);
    }
  }

  private JPanel buildPanel()
  {
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    int y=0;
    for(ItemDisplayGadgets gadgets : _gadgets)
    {
      GridBagConstraints c=new GridBagConstraints(0,y,1,2,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE, new Insets(0,0,0,0),5,5);
      ret.add(gadgets.getIcon(),c);
      c=new GridBagConstraints(1,y,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL, new Insets(0,0,0,0),5,5);
      ret.add(gadgets.getName(),c);
      y++;
      c=new GridBagConstraints(1,y,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL, new Insets(0,0,0,0),5,5);
      ret.add(gadgets.getComment(),c);
      y++;
    }
    return ret;
  }

  /**
   * Get the managed panel.
   * @return a panel.
   */
  public JPanel getPanel()
  {
    return _panel;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    if (_gadgets!=null)
    {
      for(ItemDisplayGadgets gadgets : _gadgets)
      {
        gadgets.dispose();
      }
      _gadgets=null;
    }
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
  }
}
