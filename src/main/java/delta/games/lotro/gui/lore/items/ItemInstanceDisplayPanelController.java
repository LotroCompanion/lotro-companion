package delta.games.lotro.gui.lore.items;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.common.stats.StatsProvider;
import delta.games.lotro.gui.common.stats.StatsPanel;
import delta.games.lotro.gui.lore.items.essences.EssencesSetDisplayController;
import delta.games.lotro.gui.lore.items.legendary.LegendaryInstanceDisplayPanelController;
import delta.games.lotro.gui.lore.items.legendary2.LegendaryInstance2DisplayPanelController;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemInstance;
import delta.games.lotro.lore.items.legendary.LegendaryInstance;
import delta.games.lotro.lore.items.legendary2.LegendaryInstance2;

/**
 * Controller for an item instance display panel.
 * @author DAM
 */
public class ItemInstanceDisplayPanelController
{
  // Data
  private ItemInstance<? extends Item> _itemInstance;
  // GUI
  private JPanel _panel;
  private WindowController _parent;
  // - Item identification
  private ItemIdentificationPanelController _id;
  // - Stats
  private JPanel _stats;
  // - main attributes
  private ItemInstanceMainAttrsDisplayPanelController _mainAttrs;
  // - essences (optional)
  private EssencesSetDisplayController _essences;
  // - legendary stuff (optional)
  private LegendaryInstanceDisplayPanelController _legendary;
  private LegendaryInstance2DisplayPanelController _legendary2;

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

  private JPanel build()
  {
    // ID
    _id=new ItemIdentificationPanelController(_parent,_itemInstance);
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
      _legendary=new LegendaryInstanceDisplayPanelController(_itemInstance);
    }
    boolean isLegendary2=(_itemInstance instanceof LegendaryInstance2);
    if (isLegendary2)
    {
      _legendary2=new LegendaryInstance2DisplayPanelController(_itemInstance);
    }
    return buildPanel();
  }

  private JPanel buildPanel()
  {
    JPanel ret=GuiFactory.buildBackgroundPanel(new GridBagLayout());
    // ID
    JPanel idPanel=_id.getPanel();
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
    JPanel legendaryPanel=null;
    if (_legendary!=null)
    {
      legendaryPanel=_legendary.getPanel();
    }
    else if (_legendary2!=null)
    {
      legendaryPanel=_legendary2.getPanel();
    }
    if (legendaryPanel!=null)
    {
      JScrollPane scrollPane=GuiFactory.buildScrollPane(legendaryPanel);
      c=new GridBagConstraints(0,2,3,1,1.0,1.0,GridBagConstraints.NORTHWEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
      ret.add(scrollPane,c);
      scrollPane.setBorder(GuiFactory.buildTitledBorder("Legendary"));
    }
    _panel=ret;
    update();
    return ret;
  }

  /**
   * Update display.
   */
  private void update()
  {
    Item item=_itemInstance.getReference();
    // Title
    String name=item.getName();
    if (_parent!=null)
    {
      _parent.setTitle(name);
    }
    // Stats
    StatsProvider provider=item.getStatsProvider();
    StatsPanel.fillStatsPanel(_stats,_itemInstance.getStatsManager().getResult(),provider);
    // Main attributes
    _mainAttrs.update();
    // Essences
    if (_essences!=null)
    {
      _essences.update();
    }
    // Legendary
    if (_legendary!=null)
    {
      _legendary.update();
    }
    if (_legendary2!=null)
    {
      _legendary2.update();
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
    // - item identification
    if (_id!=null)
    {
      _id.dispose();
      _id=null;
    }
    // - stats
    if (_stats!=null)
    {
      _stats.removeAll();
      _stats=null;
    }
    // - main attributes
    if (_mainAttrs!=null)
    {
      _mainAttrs.dispose();
      _mainAttrs=null;
    }
    // - essences
    if (_essences!=null)
    {
      _essences.dispose();
      _essences=null;
    }
    // - legendary
    if (_legendary!=null)
    {
      _legendary.dispose();
      _legendary=null;
    }
  }
}
