package delta.games.lotro.gui.lore.loot.instances;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.WindowController;
import delta.common.utils.math.Range;
import delta.games.lotro.common.enums.Difficulty;
import delta.games.lotro.common.enums.GroupSize;
import delta.games.lotro.common.enums.LotroEnum;
import delta.games.lotro.common.enums.LotroEnumsRegistry;
import delta.games.lotro.gui.lore.items.FilterUpdateListener;
import delta.games.lotro.gui.lore.items.ItemsListDisplayPanelController;
import delta.games.lotro.gui.lore.loot.instances.filter.InstanceLootParametersConfiguration;
import delta.games.lotro.gui.lore.loot.instances.filter.InstanceParameters;
import delta.games.lotro.gui.lore.loot.instances.filter.InstanceParametersController;
import delta.games.lotro.lore.instances.PrivateEncounter;
import delta.games.lotro.lore.instances.loot.InstanceLootEntry;
import delta.games.lotro.lore.instances.loot.InstanceLootParameters;
import delta.games.lotro.lore.instances.loot.InstanceLootTablesManager;
import delta.games.lotro.lore.instances.loot.InstanceLoots;
import delta.games.lotro.lore.instances.loot.InstanceLootsTable;
import delta.games.lotro.lore.items.Container;
import delta.games.lotro.lore.items.ContainersManager;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemsContainer;
import delta.games.lotro.lore.items.ItemsManager;
import delta.games.lotro.utils.ContextPropertyNames;

/**
 * @author DAM
 */
public class InstanceLootDisplayPanelController implements FilterUpdateListener
{
  // Data
  private InstanceLoots _loots;
  // Controllers
  private WindowController _parent;
  private ItemsListDisplayPanelController _itemsList;
  private InstanceParametersController _parameters;
  // UI
  private JPanel _panel;

  /**
   * Constructor.
   * @param parent Parent window.
   */
  public InstanceLootDisplayPanelController(WindowController parent)
  {
    _parent=parent;
    _itemsList=new ItemsListDisplayPanelController(parent);
    InstanceLootParametersConfiguration cfg=initContext(parent);
    if (cfg!=null)
    {
      InstanceParameters parameters=initParameters(cfg);
      _parameters=new InstanceParametersController(parameters,cfg,this);
    }
    _panel=buildPanel();
  }

  /**
   * Get the managed panel.
   * @return A panel or <code>null</code> if no contents.
   */
  public JPanel getPanel()
  {
    return _panel;
  }

  private InstanceParameters initParameters(InstanceLootParametersConfiguration cfg)
  {
    List<Difficulty> difficulties=cfg.getDifficulties();
    List<GroupSize> groupSize=cfg.getGroupSizes();
    List<Integer> levels=cfg.getLevels();
    if ((difficulties.size()>0) && (groupSize.size()>0) && (levels.size()>0))
    {
      return new InstanceParameters(difficulties.get(0),groupSize.get(0),levels.get(0).intValue());
    }
    return null;
  }

  private InstanceLootParametersConfiguration initContext(WindowController parent)
  {
    ItemsContainer container=getContainer(parent);
    if (container==null)
    {
      return null;
    }
    Integer tableId=container.getCustomSkirmishLootTableId();
    if (tableId==null)
    {
      return null;
    }
    InstanceLootTablesManager mgr=InstanceLootTablesManager.getInstance();
    InstanceLootsTable table=mgr.getTableById(tableId.intValue());
    if (table==null)
    {
      return null;
    }
    PrivateEncounter pe=parent.getContextProperty(ContextPropertyNames.PRIVATE_ENCOUNTER,PrivateEncounter.class);
    if (pe==null)
    {
      return null;
    }
    int instanceId=pe.getIdentifier();
    _loots=table.getInstanceLootsById(instanceId);
    if (_loots==null)
    {
      return null;
    }
    return buildConfig(_loots);
  }

  private InstanceLootParametersConfiguration buildConfig(InstanceLoots loots)
  {
    InstanceLootParametersConfiguration cfg=new InstanceLootParametersConfiguration();
    LotroEnumsRegistry registry=LotroEnumsRegistry.getInstance();
    LotroEnum<Difficulty> difficultiesMgr=registry.get(Difficulty.class);
    LotroEnum<GroupSize> groupSizeMgr=registry.get(GroupSize.class);
    for(InstanceLootEntry entry : loots.getEntries())
    {
      InstanceLootParameters params=entry.getParameters();
      // Difficulties
      Range difficultyRange=params.getDifficultyTier();
      int difficultyMin=difficultyRange.getMin().intValue();
      int difficultyMax=difficultyRange.getMax().intValue();
      for(int difficultyTier=difficultyMin;difficultyTier<=difficultyMax;difficultyTier++)
      {
        Difficulty difficulty=difficultiesMgr.getEntry(difficultyTier);
        cfg.addDifficulty(difficulty);
      }
      // Group sizes
      Range groupSizeRange=params.getGroupSize();
      int sizeMin=groupSizeRange.getMin().intValue();
      int sizeMax=groupSizeRange.getMax().intValue();
      for(int sizeCode=sizeMin;sizeCode<=sizeMax;sizeCode++)
      {
        GroupSize groupSize=groupSizeMgr.getEntry(sizeCode);
        cfg.addGroupSize(groupSize);
      }
      // Levels
      Range levelsRange=params.getLevel();
      int levelMin=levelsRange.getMin().intValue();
      int levelMax=levelsRange.getMax().intValue();
      for(int level=levelMin;level<=levelMax;level++)
      {
        cfg.addLevel(level);
      }
    }
    return cfg;
  }

  private ItemsContainer getContainer(WindowController parent)
  {
    Item item=parent.getContextProperty(ContextPropertyNames.ITEM,Item.class);
    if (item==null)
    {
      return null;
    }
    ContainersManager containersMgr=ContainersManager.getInstance();
    int itemId=item.getIdentifier();
    Container container=containersMgr.getContainerById(itemId);
    if (container instanceof ItemsContainer)
    {
      return (ItemsContainer)container;
    }
    return null;
  }

  private JPanel buildPanel()
  {
    if (_parameters==null)
    {
      return null;
    }
    JPanel panel=GuiFactory.buildPanel(new BorderLayout());
    JPanel parametersPanel=_parameters.getPanel();
    panel.add(parametersPanel,BorderLayout.NORTH);
    JPanel itemsPanel=_itemsList.getPanel();
    JScrollPane scrollPane=GuiFactory.buildScrollPane(itemsPanel);
    panel.add(scrollPane,BorderLayout.CENTER);
    return panel;
  }

  @Override
  public void filterUpdated()
  {
    List<Item> items=getItems();
    _itemsList.setItems(items);
  }

  private List<Item> getItems()
  {
    InstanceParameters params=_parameters.getParameters();
    Difficulty difficulty=params.getDifficulty();
    GroupSize groupSize=params.getSize();
    int level=params.getLevel();
    InstanceLootEntry entry=_loots.getLootTable(difficulty,groupSize,level);
    List<Item> ret=new ArrayList<Item>();
    if (entry!=null)
    {
      Set<Integer> itemIds=entry.getTrophyList().getItemIds();
      for(Integer itemId : itemIds)
      {
        Item item=ItemsManager.getInstance().getItem(itemId.intValue());
        if (item!=null)
        {
          ret.add(item);
        }
      }
    }
    return ret;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    _parent=null;
    if (_itemsList!=null)
    {
      _itemsList.dispose();
      _itemsList=null;
    }
    if (_parameters!=null)
    {
      _parameters.dispose();
      _parameters=null;
    }
  }
}
