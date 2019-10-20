package delta.games.lotro.gui.items;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.gui.LotroIconsManager;
import delta.games.lotro.gui.items.essences.EssencesSetDisplayController;
import delta.games.lotro.gui.items.legendary.LegendaryInstanceDisplayPanelController;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemInstance;
import delta.games.lotro.lore.items.legendary.LegendaryInstance;
import delta.games.lotro.utils.gui.StatsPanel;

/**
 * Controller for an item instance edition panel.
 * @author DAM
 */
public class ItemInstanceDisplayPanelController
{
  // Data
  private ItemInstance<? extends Item> _itemInstance;
  // GUI
  private JPanel _panel;
  private WindowController _parent;
  // - Item identification (icon+name)
  private JLabel _icon;
  private JLabel _name;
  // - Stats
  private JPanel _stats;
  // Child panel controllers
  private ItemInstanceMainAttrsDisplayPanelController _mainAttrs;
  private EssencesSetDisplayController _essences;
  private LegendaryInstanceDisplayPanelController _legendary;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param itemInstance Item instance.
   */
  public ItemInstanceDisplayPanelController(WindowController parent, ItemInstance<? extends Item> itemInstance)
  {
    _parent=parent;
    _itemInstance=itemInstance;
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  public JPanel getPanel()
  {
    if (_panel==null)
    {
      _panel=build();
    }
    return _panel;
  }

  private void initGadgets()
  {
    // Item identification (icon+name)
    // - icon
    _icon=GuiFactory.buildIconLabel(null);
    // - name
    _name=GuiFactory.buildLabel("");
    _name.setFont(_name.getFont().deriveFont(16f).deriveFont(Font.BOLD));
  }

  private JPanel build()
  {
    initGadgets();
    // Stats
    _stats=GuiFactory.buildPanel(new GridBagLayout());
    _stats.setBorder(GuiFactory.buildTitledBorder("Stats"));
    // Main attributes
    _mainAttrs=new ItemInstanceMainAttrsDisplayPanelController(_itemInstance);
    _mainAttrs.getPanel().setBorder(GuiFactory.buildTitledBorder("Characteristics"));
    // Essences
    int nbEssences=_itemInstance.getReference().getEssenceSlots();
    if (nbEssences>0)
    {
      _essences=new EssencesSetDisplayController(_itemInstance.getEssences());
      _essences.getPanel().setBorder(GuiFactory.buildTitledBorder("Essences"));
    }
    // Legendary stuff
    boolean isLegendary=(_itemInstance instanceof LegendaryInstance);
    if (isLegendary)
    {
      _legendary=new LegendaryInstanceDisplayPanelController();
      _legendary.getPanel().setBorder(GuiFactory.buildTitledBorder("Legendary"));
    }
    return buildPanel();
  }

  private JPanel buildPanel()
  {
    JPanel ret=GuiFactory.buildBackgroundPanel(new GridBagLayout());
    // ID
    JPanel idPanel=buildIdentificationPanel();
    GridBagConstraints c=new GridBagConstraints(0,0,3,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    ret.add(idPanel,c);
    // Main attributes
    c=new GridBagConstraints(0,1,1,1,1.0,0.0,GridBagConstraints.NORTHWEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    ret.add(_mainAttrs.getPanel(),c);
    // Stats
    c=new GridBagConstraints(1,1,1,1,1.0,0.0,GridBagConstraints.NORTHWEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    ret.add(_stats,c);
    // Essences
    if (_essences!=null)
    {
      c=new GridBagConstraints(2,1,1,1,1.0,0.0,GridBagConstraints.NORTHWEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
      ret.add(_essences.getPanel(),c);
    }
    // Legendary
    if (_legendary!=null)
    {
      c=new GridBagConstraints(0,2,3,1,1.0,0.0,GridBagConstraints.NORTHWEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
      ret.add(_legendary.getPanel(),c);
    }
    _panel=ret;
    setItem();
    return ret;
  }

  private JPanel buildIdentificationPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    JPanel panelLine=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
    panel.add(panelLine,c);
    c.gridy++;
    // Icon
    panelLine.add(_icon);
    // Name
    panelLine.add(_name);
    return panel;
  }

  /**
   * Set the item to display.
   */
  private void setItem()
  {
    Item item=_itemInstance.getReference();
    // Title
    String name=item.getName();
    if (_parent!=null)
    {
      _parent.setTitle(name);
    }
    // Item identification (icon+name)
    // - icon
    String iconPath=item.getIcon();
    ImageIcon icon=LotroIconsManager.getItemIcon(iconPath);
    _icon.setIcon(icon);
    // - name
    _name.setText(name);
    // Stats
    StatsPanel.fillStatsPanel(_stats,_itemInstance.getStats(),null);
    // Main attributes
    // Essences
    if (_essences!=null)
    {
      _essences.update();
    }
    // Legendary
    if (_legendary!=null)
    {
      Integer itemLevelInt=_itemInstance.getEffectiveItemLevel();
      int itemLevel=(itemLevelInt!=null)?itemLevelInt.intValue():1;
      LegendaryInstance legendaryInstance=(LegendaryInstance)_itemInstance;
      _legendary.setData(itemLevel,legendaryInstance.getLegendaryAttributes());
    }
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Data
    _itemInstance=null;
    // UI/controllers
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    _parent=null;
    // - Item identification (icon+name)
    _icon=null;
    _name=null;
  }
}
