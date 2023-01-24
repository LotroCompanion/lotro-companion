package delta.games.lotro.gui.lore.items;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.BasicCharacterAttributes;
import delta.games.lotro.character.classes.ClassDescription;
import delta.games.lotro.common.stats.StatsProvider;
import delta.games.lotro.gui.common.stats.CustomStatsEditionWindowController;
import delta.games.lotro.gui.common.stats.StatsPanel;
import delta.games.lotro.gui.lore.items.essences.EssencesSetDisplayController;
import delta.games.lotro.gui.lore.items.essences.EssencesSetEditionWindowController;
import delta.games.lotro.gui.lore.items.legendary.LegendaryInstanceDisplayPanelController;
import delta.games.lotro.gui.lore.items.legendary.LegendaryInstanceEditionWindowController;
import delta.games.lotro.gui.lore.items.legendary2.LegendaryInstance2DisplayPanelController;
import delta.games.lotro.gui.lore.items.legendary2.LegendaryInstance2EditionWindowController;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemInstance;
import delta.games.lotro.lore.items.legendary.LegendaryInstance;
import delta.games.lotro.lore.items.legendary2.LegendaryInstance2;

/**
 * Controller for an item instance edition panel.
 * @author DAM
 */
public class ItemInstanceEditionPanelController
{
  /**
   * 
   */
  private static final String EDIT="Edit...";
  // Data
  private BasicCharacterAttributes _attrs;
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
   * @param attrs Attributes of toon to use.
   * @param itemInstance Item instance.
   */
  public ItemInstanceEditionPanelController(WindowController parent, BasicCharacterAttributes attrs, ItemInstance<? extends Item> itemInstance)
  {
    _parent=parent;
    _attrs=attrs;
    _itemInstance=itemInstance;
    _panel=buildPanel();
    update();
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  public JPanel getPanel()
  {
    return _panel;
  }

  private void initGadgets()
  {
    // ID
    _id=new ItemIdentificationPanelController(_parent,_itemInstance);
    // Main attributes
    _mainAttrs=new ItemInstanceMainAttrsDisplayPanelController(_itemInstance);
    // Stats
    _stats=GuiFactory.buildPanel(new GridBagLayout());
    // Essences
    int nbEssences=_itemInstance.getReference().getEssenceSlots();
    if (nbEssences>0)
    {
      _essences=new EssencesSetDisplayController(_itemInstance.getEssences());
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
      _legendary2=new LegendaryInstance2DisplayPanelController(_parent,_itemInstance);
    }
  }

  private JPanel buildPanel()
  {
    initGadgets();
    JPanel ret=GuiFactory.buildBackgroundPanel(new GridBagLayout());
    // ID
    JPanel idPanel=_id.getPanel();
    GridBagConstraints c=new GridBagConstraints(0,0,2,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    ret.add(idPanel,c);
    // Main attributes
    JPanel mainAttrsPanel=buildMainAttrsPanel();
    c=new GridBagConstraints(0,1,1,1,1.0,1.0,GridBagConstraints.NORTHWEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
    ret.add(mainAttrsPanel,c);
    // Stats
    JPanel statsPanel=buildStatsPanel();
    c=new GridBagConstraints(1,1,1,1,0.0,0.0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    ret.add(statsPanel,c);
    // Essences
    if (_essences!=null)
    {
      c=new GridBagConstraints(2,1,1,1,0.0,0.0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
      JPanel essencesPanel=buildEssencesPanel();
      ret.add(essencesPanel,c);
    }
    // Legendary
    JPanel legendaryPanel=null;
    if (_legendary!=null)
    {
      legendaryPanel=buildLegendaryPanel(_legendary.getPanel());
    }
    else if (_legendary2!=null)
    {
      legendaryPanel=buildLegendaryPanel(_legendary2.getPanel());
    }
    if (legendaryPanel!=null)
    {
      c=new GridBagConstraints(0,2,2,1,1.0,1.0,GridBagConstraints.NORTHWEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
      ret.add(legendaryPanel,c);
    }
    return ret;
  }

  private JPanel buildMainAttrsPanel()
  {
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    // Main attributes
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,1.0,1.0,GridBagConstraints.NORTHWEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
    ret.add(_mainAttrs.getPanel(),c);
    // Edit main attrs
    JButton editButton=GuiFactory.buildButton(EDIT);
    ActionListener l=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        editMainAttrs();
      }
    };
    editButton.addActionListener(l);
    c=new GridBagConstraints(0,1,1,1,0.0,0.0,GridBagConstraints.SOUTHEAST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    ret.add(editButton,c);
    // Border
    ret.setBorder(GuiFactory.buildTitledBorder("Characteristics"));
    return ret;
  }

  private JPanel buildStatsPanel()
  {
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    // Stats
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    ret.add(_stats,c);
    // Edit stats
    JButton editButton=GuiFactory.buildButton(EDIT);
    ActionListener l=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        editStats();
      }
    };
    editButton.addActionListener(l);
    c=new GridBagConstraints(0,1,1,1,0.0,0.0,GridBagConstraints.SOUTHEAST,GridBagConstraints.NONE,new Insets(5,0,0,0),0,0);
    ret.add(editButton,c);
    // Border
    ret.setBorder(GuiFactory.buildTitledBorder("Stats"));
    return ret;
  }

  private JPanel buildEssencesPanel()
  {
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    // Essences
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    ret.add(_essences.getPanel(),c);
    // Edit button
    JButton editButton=GuiFactory.buildButton(EDIT);
    ActionListener l=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        editEssences();
      }
    };
    editButton.addActionListener(l);
    c=new GridBagConstraints(0,1,1,1,0.0,0.0,GridBagConstraints.SOUTHEAST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    ret.add(editButton,c);
    // Border
    ret.setBorder(GuiFactory.buildTitledBorder("Essences"));
    return ret;
  }

  private JPanel buildLegendaryPanel(JPanel innerPanel)
  {
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    // Legendary data
    JScrollPane scrollPane=GuiFactory.buildScrollPane(innerPanel);
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,1.0,1.0,GridBagConstraints.NORTHWEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
    ret.add(scrollPane,c);
    // Edit button
    JButton editButton=GuiFactory.buildButton(EDIT);
    ActionListener l=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        editLegendaryStuff();
      }
    };
    editButton.addActionListener(l);
    c=new GridBagConstraints(0,1,1,1,0.0,0.0,GridBagConstraints.SOUTHEAST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    ret.add(editButton,c);
    ret.setBorder(GuiFactory.buildTitledBorder("Legendary"));
    return ret;
  }

  private void editMainAttrs()
  {
    ItemInstanceMainAttrsEditionWindowController editor=new ItemInstanceMainAttrsEditionWindowController(_parent,_itemInstance);
    ItemInstance<? extends Item> updatedItem=editor.editModal();
    if (updatedItem!=null)
    {
      _mainAttrs.update();
      updateStats();
      updateWindow();
    }
  }

  private void editStats()
  {
    CustomStatsEditionWindowController editor=new CustomStatsEditionWindowController(_parent,_itemInstance);
    ItemInstance<? extends Item> updatedItem=editor.editModal();
    if (updatedItem!=null)
    {
      updateStats();
      updateWindow();
    }
  }

  private void editEssences()
  {
    EssencesSetEditionWindowController editor=new EssencesSetEditionWindowController(_parent,_attrs,_itemInstance);
    ItemInstance<? extends Item> updatedItem=editor.editModal();
    if (updatedItem!=null)
    {
      _essences.update();
      updateWindow();
    }
  }

  private void editLegendaryStuff()
  {
    ClassDescription characterClass=_attrs.getCharacterClass();
    if (_legendary!=null)
    {
      LegendaryInstanceEditionWindowController editor=new LegendaryInstanceEditionWindowController(_parent,characterClass,_itemInstance);
      ItemInstance<? extends Item> updatedItem=editor.editModal();
      if (updatedItem!=null)
      {
        _legendary.update();
        updateWindow();
      }
    }
    if (_legendary2!=null)
    {
      LegendaryInstance2EditionWindowController editor=new LegendaryInstance2EditionWindowController(_parent,_itemInstance);
      editor.getWindow().pack();
      ItemInstance<? extends Item> updatedItem=editor.editModal();
      if (updatedItem!=null)
      {
        // Edition may have changed the item level & stats
        // => update the display
        _mainAttrs.update();
        updateStats();
        // Update traceries display
        _legendary2.update();
        updateWindow();
      }
    }
  }

  private void updateWindow()
  {
    _panel.revalidate();
    _panel.repaint();
    Window parent=_parent.getWindow();
    parent.pack();
    parent.setMinimumSize(parent.getPreferredSize());
  }

  /**
   * Update display.
   */
  private void update()
  {
    // Stats
    updateStats();
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
  }

  private void updateStats()
  {
    Item item=_itemInstance.getReference();
    StatsProvider provider=item.getStatsProvider();
    StatsPanel.fillStatsPanel(_stats,_itemInstance.getStatsManager().getResult(),provider);
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
    if (_legendary2!=null)
    {
      _legendary2.dispose();
      _legendary2=null;
    }
  }
}
