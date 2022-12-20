package delta.games.lotro.gui.lore.items.form;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.labels.MultilineLabel2;
import delta.common.ui.swing.navigator.NavigablePanelController;
import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.games.lotro.character.skills.SkillDescription;
import delta.games.lotro.character.stats.BasicStatsSet;
import delta.games.lotro.character.traits.TraitDescription;
import delta.games.lotro.common.enums.EquipmentCategory;
import delta.games.lotro.common.money.Money;
import delta.games.lotro.common.stats.StatUtils;
import delta.games.lotro.common.stats.StatsProvider;
import delta.games.lotro.gui.LotroIconsManager;
import delta.games.lotro.gui.common.money.MoneyDisplayController;
import delta.games.lotro.gui.common.requirements.RequirementsUtils;
import delta.games.lotro.gui.lore.items.containers.form.ContainerDisplayPanelController;
import delta.games.lotro.gui.utils.IconAndLinkPanelController;
import delta.games.lotro.gui.utils.SharedPanels;
import delta.games.lotro.lore.emotes.EmoteDescription;
import delta.games.lotro.lore.items.DamageType;
import delta.games.lotro.lore.items.DisenchantmentManager;
import delta.games.lotro.lore.items.DisenchantmentResult;
import delta.games.lotro.lore.items.EquipmentLocation;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemBinding;
import delta.games.lotro.lore.items.ItemSturdiness;
import delta.games.lotro.lore.items.Weapon;
import delta.games.lotro.lore.items.details.GrantedElement;
import delta.games.lotro.lore.items.details.ItemDetailsManager;
import delta.games.lotro.lore.items.details.ItemReputation;
import delta.games.lotro.lore.items.details.ItemXP;
import delta.games.lotro.lore.items.details.VirtueXP;
import delta.games.lotro.lore.items.legendary2.EnhancementRune;
import delta.games.lotro.lore.items.legendary2.EnhancementRunesManager;
import delta.games.lotro.lore.items.legendary2.TraceriesManager;
import delta.games.lotro.lore.items.legendary2.Tracery;
import delta.games.lotro.lore.reputation.Faction;
import delta.games.lotro.utils.gui.HtmlUtils;

/**
 * Controller for an item display panel.
 * @author DAM
 */
public class ItemDisplayPanelController implements NavigablePanelController
{
  // Data
  private Item _item;
  // GUI
  private JPanel _panel;
  // Controllers
  private NavigatorWindowController _parent;
  private ItemReferencesDisplayController _references;
  private ItemScalableStatsPanelController _scaling;
  private ContainerDisplayPanelController _container;
  private MoneyDisplayController _money;
  private DisenchantmentResultPanelController _disenchantment;
  private List<IconAndLinkPanelController> _grantedCtrls;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param item Item to show.
   */
  public ItemDisplayPanelController(NavigatorWindowController parent, Item item)
  {
    _parent=parent;
    _item=item;
    _references=new ItemReferencesDisplayController(parent,item.getIdentifier());
    _scaling=new ItemScalableStatsPanelController(item);
    _container=new ContainerDisplayPanelController(parent,item);
    _money=new MoneyDisplayController();
    _grantedCtrls=new ArrayList<IconAndLinkPanelController>();
  }

  @Override
  public String getTitle()
  {
    return "Item: "+_item.getName();
  }

  @Override
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
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    // Top panel
    JPanel topPanel=buildTopPanel();
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(topPanel,c);
    // Center
    Component center=buildCenterPanel();
    c=new GridBagConstraints(0,1,1,1,1.0,1.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
    panel.add(center,c);
    return panel;
  }

  private MultilineLabel2 buildStatsDisplay()
  {
    MultilineLabel2 statsLabel=null;
    StatsProvider statsProvider=_item.getStatsProvider();
    BasicStatsSet stats=_item.getStats();
    int nbStats=stats.getStatsCount();
    int nbEffects=0;
    if (statsProvider!=null)
    {
      nbEffects=statsProvider.getSpecialEffects().size();
    }
    if (nbStats+nbEffects>0)
    {
      String[] lines=StatUtils.getFullStatsDisplay(stats,statsProvider);
      statsLabel=new MultilineLabel2();
      statsLabel.setText(lines);
      statsLabel.setBorder(GuiFactory.buildTitledBorder("Stats"));
    }
    return statsLabel;
  }

  private JPanel buildCenterPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    // Build components for tabs
    JPanel mainAttrs=buildMainAttrsPanel();
    JTabbedPane tabbedPane=GuiFactory.buildTabbedPane();
    tabbedPane.add("Main",buildPanelForTab(mainAttrs));
    // - references
    JEditorPane references=_references.getComponent();
    if (references!=null)
    {
      tabbedPane.add("References",buildPanelForTab(references));
    }
    // - scaling
    JPanel scalingPanel=_scaling.getPanel();
    if (scalingPanel!=null)
    {
      tabbedPane.add("Scaling",buildPanelForTab(scalingPanel));
    }
    // - container
    JPanel containerPanel=_container.getPanel();
    if (containerPanel!=null)
    {
      containerPanel=buildContainerPanel(containerPanel);
      JPanel tabPanel=buildPanelForTab(containerPanel);
      tabPanel.setPreferredSize(new Dimension(500,300));
      tabbedPane.add("Contents",tabPanel);
    }
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,1.0,1.0,GridBagConstraints.NORTHWEST,GridBagConstraints.BOTH,new Insets(5,5,5,5),0,0);
    panel.add(tabbedPane,c);
    return panel;
  }

  private JPanel buildContainerPanel(JPanel containerPanel)
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,1.0,0.0,GridBagConstraints.NORTHWEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(containerPanel,c);

    // Padding to push everything on top
    JPanel paddingPanel=GuiFactory.buildPanel(new BorderLayout());
    c=new GridBagConstraints(0,1,1,1,0.0,1.0,GridBagConstraints.NORTHWEST,GridBagConstraints.VERTICAL,new Insets(0,0,0,0),0,0);
    panel.add(paddingPanel,c);
    return panel;
  }

  private JPanel buildPanelForTab(Component contents)
  {
    JPanel wrapper=GuiFactory.buildBackgroundPanel(new BorderLayout());
    JScrollPane scrollPane=GuiFactory.buildScrollPane(contents);
    wrapper.add(scrollPane,BorderLayout.CENTER);
    return wrapper;
  }

  private JPanel buildTopPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    // Icon
    ImageIcon icon=LotroIconsManager.getItemIcon(_item.getIcon());
    JLabel iconLabel=GuiFactory.buildIconLabel(icon);
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,5,0,5),0,0);
    panel.add(iconLabel,c);
    // Name
    String name=_item.getName();
    JLabel nameLabel=GuiFactory.buildLabel(name);
    nameLabel.setFont(nameLabel.getFont().deriveFont(16f).deriveFont(Font.BOLD));
    c=new GridBagConstraints(1,0,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(nameLabel,c);
    return panel;
  }

  private JPanel buildMainAttrsPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    List<String> lines=getAttributesLines();
    for(String line : lines)
    {
      JPanel panelLine=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
      panel.add(panelLine,c);
      c.gridy++;
      panelLine.add(GuiFactory.buildLabel(line));
    }
    c.insets=new Insets(0,5,0,0);
    // Details
    JPanel detailPanel=buildDetailsPanel();
    if (detailPanel!=null)
    {
      panel.add(detailPanel,c);
      c.gridy++;
    }
    // Value
    Money money=_item.getValueAsMoney();
    if (money!=null)
    {
      _money.setMoney(money);
      JPanel moneyPanel=_money.getPanel();
      panel.add(moneyPanel,c);
      c.gridy++;
    }
    int y=c.gridy;
    // Description
    JEditorPane description=buildDescription();
    if (description!=null)
    {
      c=new GridBagConstraints(0,y,1,1,1.0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.HORIZONTAL,new Insets(5,5,5,5),0,0);
      panel.add(description,c);
      y++;
    }
    // Stats
    MultilineLabel2 stats=buildStatsDisplay();
    if (stats!=null)
    {
      c=new GridBagConstraints(0,y,1,1,1.0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.HORIZONTAL,new Insets(5,5,5,5),0,0);
      panel.add(stats,c);
      y++;
    }
    // Disenchantment
    int itemId=_item.getIdentifier();
    DisenchantmentResult disenchantment=DisenchantmentManager.getInstance().getDisenchantmentResults().getItem(itemId);
    if (disenchantment!=null)
    {
      _disenchantment=new DisenchantmentResultPanelController(_parent,disenchantment);
      JPanel disenchantmentPanel=_disenchantment.getPanel();
      disenchantmentPanel.setBorder(GuiFactory.buildTitledBorder("Disenchantment"));
      c=new GridBagConstraints(0,y,1,1,0.0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
      panel.add(disenchantmentPanel,c);
      y++;
    }
    // Padding to push everything on left and top
    JPanel paddingPanel=GuiFactory.buildPanel(new BorderLayout());
    c.fill=GridBagConstraints.BOTH;
    c.weightx=1.0;
    c.weighty=1.0;
    panel.add(paddingPanel,c);
    return panel;
  }

  private List<String> getAttributesLines()
  {
    List<String> ret=new ArrayList<String>();
    // Quality
    // TODO
    // Equipment category
    EquipmentCategory equipmentCategory=_item.getEquipmentCategory();
    if (equipmentCategory!=null)
    {
      ret.add("Equipment Category: "+equipmentCategory.getLabel());
    }
    // Slot
    EquipmentLocation location=_item.getEquipmentLocation();
    if (location!=null)
    {
      ret.add("Location: "+location.getLabel());
    }
    // Category
    if ((equipmentCategory==null) && (location==null))
    {
      String category=_item.getSubCategory();
      if ((category!=null) && (category.length()>0))
      {
        ret.add("Category: "+category);
      }
    }
    if (_item instanceof Weapon)
    {
      List<String> weaponLines=getWeaponAttributeLines((Weapon)_item);
      ret.addAll(weaponLines);
    }
    // Slots
    int nbSlots=_item.getEssenceSlots();
    if (nbSlots>0)
    {
      ret.add("Essence slots: "+nbSlots);
    }
    // Item level
    Integer itemLevel=_item.getItemLevel();
    if (itemLevel!=null)
    {
      ret.add("Item level: "+itemLevel.toString());
    }
    // Tracery complements
    Tracery tracery=TraceriesManager.getInstance().getTracery(_item.getIdentifier());
    if (tracery!=null)
    {
      int maxItemLevel=tracery.getMaxItemLevel();
      int increment=tracery.getLevelUpIncrement();
      String label="Enhancement limit: "+maxItemLevel;
      if (increment>1)
      {
        label=label+" (increment: "+increment+")";
      }
      ret.add(label);
    }
    // Enhancement rune complements
    EnhancementRune enhancementRune=EnhancementRunesManager.getInstance().getEnhancementRune(_item.getIdentifier());
    if (enhancementRune!=null)
    {
      int minItemLevel=enhancementRune.getMinItemLevel();
      int maxItemLevel=enhancementRune.getMaxItemLevel();
      int increment=enhancementRune.getLevelUpIncrement();
      String label="Enhancement item levels: "+minItemLevel+"-"+maxItemLevel;
      if (increment>1)
      {
        label=label+" (increment: "+increment+")";
      }
      ret.add(label);
    }
    // Durability
    Integer durability=_item.getDurability();
    if (durability!=null)
    {
      ret.add("Durability: "+durability.toString());
    }
    // Requirements
    String requirements=RequirementsUtils.buildRequirementString(_item.getUsageRequirements());
    if (requirements.length()>0)
    {
      ret.add("Requirements: "+requirements);
    }
    // Attributes
    {
      String attributes=buildAttributesString();
      ret.add(attributes);
    }
    return ret;
  }

  private List<String> getWeaponAttributeLines(Weapon weapon)
  {
    List<String> ret=new ArrayList<String>();
    // Damage type
    DamageType damageType=weapon.getDamageType();
    if (damageType!=null)
    {
      ret.add("Damage type: "+damageType.getName());
    }
    // Damage range
    int minDamage=weapon.getMinDamage();
    int maxDamage=weapon.getMaxDamage();
    ret.add("Damage: "+minDamage+" - "+maxDamage);
    // DPS
    float dps=weapon.getDPS();
    String dpsStr=String.format("%.1f",Float.valueOf(dps));
    ret.add("DPS: "+dpsStr);
    return ret;
  }

  /**
   * Build an attributes string.
   * @return A string, empty if no requirement.
   */
  private String buildAttributesString()
  {
    StringBuilder sb=new StringBuilder();
    // Unique?
    boolean unique=_item.isUnique();
    if (unique)
    {
      sb.append("Unique");
    }
    // Binding
    ItemBinding binding=_item.getBinding();
    if (binding!=null)
    {
      if (sb.length()>0) sb.append(", ");
      sb.append(binding.toString());
    }
    // Sturdiness
    ItemSturdiness sturdiness=_item.getSturdiness();
    if (sturdiness!=null)
    {
      if (sb.length()>0) sb.append(", ");
      sb.append(sturdiness.toString());
    }
    // Stacks
    Integer stack=_item.getStackMax();
    if ((stack!=null) && (stack.intValue()>1))
    {
      if (sb.length()>0) sb.append(", ");
      sb.append("Stacks to ");
      sb.append(stack);
    }
    String ret=sb.toString();
    return ret;
  }

  private JEditorPane buildDescription()
  {
    JEditorPane editor=null;
    String description=_item.getDescription();
    if (description.length()>0)
    {
      editor=GuiFactory.buildHtmlPanel();
      StringBuilder sb=new StringBuilder();
      sb.append("<html><body style='width: 400px'>");
      sb.append(HtmlUtils.toHtml(description));
      sb.append("</body></html>");
      editor.setText(sb.toString());
    }
    return editor;
  }

  private JPanel buildDetailsPanel()
  {
    ItemDetailsManager mgr=_item.getDetails();
    if (mgr==null)
    {
      return null;
    }
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    int y=0;
    // Granted elements
    @SuppressWarnings("rawtypes")
    List<GrantedElement> grantedElements=mgr.getItemDetails(GrantedElement.class);
    if (grantedElements.size()>0)
    {
      GridBagConstraints c=new GridBagConstraints(0,y,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
      ret.add(GuiFactory.buildLabel("Grants:"),c);
      y++;
      for(GrantedElement<?> grantedElement : grantedElements)
      {
        IconAndLinkPanelController panelCtrl=buildGrantedElementPanel(grantedElement);
        if (panelCtrl!=null)
        {
          _grantedCtrls.add(panelCtrl);
          c=new GridBagConstraints(0,y,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
          ret.add(panelCtrl.getPanel(),c);
          y++;
        }
      }
    }
    // Item XP
    List<ItemXP> itemXPs=mgr.getItemDetails(ItemXP.class);
    if (itemXPs.size()>0)
    {
      for(ItemXP itemXP : itemXPs)
      {
        GridBagConstraints c=new GridBagConstraints(0,y,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
        String label="Gives "+itemXP.getAmount()+" item XP.";
        ret.add(GuiFactory.buildLabel(label),c);
        y++;
      }
    }
    // Virtue XP
    List<VirtueXP> virtueXPs=mgr.getItemDetails(VirtueXP.class);
    if (virtueXPs.size()>0)
    {
      for(VirtueXP virtueXP : virtueXPs)
      {
        GridBagConstraints c=new GridBagConstraints(0,y,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
        String label="Gives "+virtueXP.getAmount()+" virtue XP.";
        ret.add(GuiFactory.buildLabel(label),c);
        y++;
      }
    }
    // Reputation
    List<ItemReputation> reputations=mgr.getItemDetails(ItemReputation.class);
    if (reputations.size()>0)
    {
      for(ItemReputation reputation : reputations)
      {
        GridBagConstraints c=new GridBagConstraints(0,y,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
        Faction faction=reputation.getFaction();
        int amount=reputation.getAmount();
        String verb=(amount>0)?"Gives":"Removes";
        String label=verb+" "+amount+" reputation points in faction "+faction.getName()+".";
        ret.add(GuiFactory.buildLabel(label),c);
        y++;
      }
    }
    return ret;
  }

  private IconAndLinkPanelController buildGrantedElementPanel(GrantedElement<?> grantedElement)
  {
    Object element=grantedElement.getGrantedElement();
    if (element instanceof SkillDescription)
    {
      SkillDescription skill=(SkillDescription)element;
      return SharedPanels.buildSkillPanel(_parent,skill);
    }
    if (element instanceof TraitDescription)
    {
      TraitDescription trait=(TraitDescription)element;
      return SharedPanels.buildTraitPanel(_parent,trait);
    }
    if (element instanceof EmoteDescription)
    {
      EmoteDescription emote=(EmoteDescription)element;
      return SharedPanels.buildEmotePanel(_parent,emote);
    }
    return null;
  }

  @Override
  public void dispose()
  {
    // Data
    _item=null;
    // Controllers
    if (_references!=null)
    {
      _references.dispose();
      _references=null;
    }
    if (_scaling!=null)
    {
      _scaling.dispose();
      _scaling=null;
    }
    if (_container!=null)
    {
      _container.dispose();
      _container=null;
    }
    if (_money!=null)
    {
      _money.dispose();
      _money=null;
    }
    if (_disenchantment!=null)
    {
      _disenchantment.dispose();
      _disenchantment=null;
    }
    if (_grantedCtrls!=null)
    {
      for(IconAndLinkPanelController ctrl : _grantedCtrls)
      {
        ctrl.dispose();
      }
      _grantedCtrls=null;
    }
    _parent=null;
    // UI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
  }
}
