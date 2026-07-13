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

import javax.swing.Icon;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.labels.MultilineLabel2;
import delta.common.ui.swing.navigator.AbstractNavigablePanelController;
import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.common.ui.swing.navigator.PageIdentifier;
import delta.common.utils.l10n.L10n;
import delta.games.lotro.character.skills.SkillDescription;
import delta.games.lotro.character.traits.TraitDescription;
import delta.games.lotro.common.Duration;
import delta.games.lotro.common.enums.EquipmentCategory;
import delta.games.lotro.common.enums.HousingHookCategory;
import delta.games.lotro.common.enums.LotroEnumEntry;
import delta.games.lotro.common.money.Money;
import delta.games.lotro.config.LotroCoreConfig;
import delta.games.lotro.gui.common.money.MoneyDisplayController;
import delta.games.lotro.gui.common.navigation.ReferenceConstants;
import delta.games.lotro.gui.common.requirements.RequirementsUtils;
import delta.games.lotro.gui.lore.items.ItemUiTools;
import delta.games.lotro.gui.lore.items.containers.form.ContainerDisplayPanelController;
import delta.games.lotro.gui.lore.items.essences.EssencesTemplatePanelController;
import delta.games.lotro.gui.utils.IconAndLinkPanelController;
import delta.games.lotro.gui.utils.SharedPanels;
import delta.games.lotro.gui.utils.SharedUiUtils;
import delta.games.lotro.gui.utils.UiConfiguration;
import delta.games.lotro.gui.utils.items.SaveItemIconController;
import delta.games.lotro.gui.utils.l10n.Labels;
import delta.games.lotro.gui.utils.navigation.NavigationHyperLink;
import delta.games.lotro.lore.emotes.EmoteDescription;
import delta.games.lotro.lore.items.DamageType;
import delta.games.lotro.lore.items.DisenchantmentManager;
import delta.games.lotro.lore.items.DisenchantmentResult;
import delta.games.lotro.lore.items.EquipmentLocation;
import delta.games.lotro.lore.items.EquipmentLocations;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemBinding;
import delta.games.lotro.lore.items.ItemQuality;
import delta.games.lotro.lore.items.ItemSturdiness;
import delta.games.lotro.lore.items.ItemUtils;
import delta.games.lotro.lore.items.Weapon;
import delta.games.lotro.lore.items.details.AllegiancePoints;
import delta.games.lotro.lore.items.details.GrantedElement;
import delta.games.lotro.lore.items.details.HousingHooks;
import delta.games.lotro.lore.items.details.ItemDecay;
import delta.games.lotro.lore.items.details.ItemDetailsManager;
import delta.games.lotro.lore.items.details.ItemReputation;
import delta.games.lotro.lore.items.details.ItemUsageCooldown;
import delta.games.lotro.lore.items.details.ItemXP;
import delta.games.lotro.lore.items.details.ProvidesPortraitFrame;
import delta.games.lotro.lore.items.details.VirtueXP;
import delta.games.lotro.lore.items.details.WeaponSlayerInfo;
import delta.games.lotro.lore.items.essences.Essence;
import delta.games.lotro.lore.items.legendary2.EnhancementRune;
import delta.games.lotro.lore.items.legendary2.EnhancementRunesManager;
import delta.games.lotro.lore.items.legendary2.TraceriesManager;
import delta.games.lotro.lore.items.legendary2.Tracery;
import delta.games.lotro.lore.items.weapons.WeaponSpeedEntry;
import delta.games.lotro.lore.portraitFrames.PortraitFrameDescription;
import delta.games.lotro.lore.reputation.Faction;
import delta.games.lotro.utils.html.HtmlUtils;
import delta.games.lotro.utils.strings.ContextRendering;

/**
 * Controller for an item display panel.
 * @author DAM
 */
public class ItemDisplayPanelController extends AbstractNavigablePanelController
{
  // Data
  private Item _item;
  private Integer _itemLevel;
  // Controllers
  private ItemReferencesDisplayController _references;
  private ItemScalableStatsPanelController _scaling;
  private ContainerDisplayPanelController _container;
  private EssencesTemplatePanelController _essences;
  private MoneyDisplayController _money;
  private DisenchantmentResultPanelController _disenchantment;
  private List<IconAndLinkPanelController> _grantedCtrls;
  private SaveItemIconController _saveItemIcon;
  private List<NavigationHyperLink> _links;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param item Item to show.
   * @param itemLevel Item level override.
   */
  public ItemDisplayPanelController(NavigatorWindowController parent, Item item, Integer itemLevel)
  {
    super(parent);
    _item=item;
    _itemLevel=itemLevel;
    _references=new ItemReferencesDisplayController(parent,item.getIdentifier());
    _scaling=new ItemScalableStatsPanelController(item);
    _container=new ContainerDisplayPanelController(parent,item);
    _money=new MoneyDisplayController();
    _grantedCtrls=new ArrayList<IconAndLinkPanelController>();
    _links=new ArrayList<NavigationHyperLink>();
    setPanel(build());
  }

  @Override
  public String getTitle()
  {
    String itemName=_item.getName();
    String title;
    if (_itemLevel!=null)
    {
      title=Labels.getLabel("item.form.window.title.withItemLevel",new Object[] {itemName,_itemLevel});
    }
    else
    {
      title=Labels.getLabel("item.form.window.title.noItemLevel",new Object[] {itemName});
    }
    return title;
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
    List<String> lines=ItemUtils.buildLinesToShowItem(_item,_itemLevel);
    if (!lines.isEmpty())
    {
      statsLabel=new MultilineLabel2();
      String[] linesToShow=lines.toArray(new String[lines.size()]);
      statsLabel.setText(linesToShow);
      statsLabel.setBorder(GuiFactory.buildTitledBorder(Labels.getLabel("item.form.stats.border")));
    }
    return statsLabel;
  }

  private JPanel buildCenterPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    // Build components for tabs
    JPanel mainAttrs=buildMainAttrsPanel();
    JTabbedPane tabbedPane=GuiFactory.buildTabbedPane();
    tabbedPane.add(Labels.getLabel("item.form.main.tab"),buildPanelForTab(mainAttrs));
    // - references
    JEditorPane references=_references.getComponent();
    if (references!=null)
    {
      tabbedPane.add(Labels.getLabel("item.form.references.tab"),buildPanelForTab(references));
    }
    // - scaling
    JPanel scalingPanel=_scaling.getPanel();
    if (scalingPanel!=null)
    {
      tabbedPane.add(Labels.getLabel("item.form.scaling.tab"),buildPanelForTab(scalingPanel));
    }
    // - container
    JPanel containerPanel=_container.getPanel();
    if (containerPanel!=null)
    {
      containerPanel=buildContainerPanel(containerPanel);
      JPanel tabPanel=buildPanelForTab(containerPanel);
      tabPanel.setPreferredSize(new Dimension(500,300));
      tabbedPane.add(Labels.getLabel("item.form.contents.tab"),tabPanel);
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
    Icon icon=ItemUiTools.buildItemIcon(_item);
    JLabel iconLabel=GuiFactory.buildIconLabel(icon);
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,5,0,5),0,0);
    panel.add(iconLabel,c);
    _saveItemIcon=new SaveItemIconController(_item,iconLabel);
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
      panelLine.add(SharedUiUtils.buildSelectableLabel(line));
    }
    c.insets=new Insets(0,5,0,0);
    // Essence slots
    int nbSlots=_item.getEssenceSlots();
    if (nbSlots>0)
    {
      _essences=new EssencesTemplatePanelController(_item.getEssenceSlotsSetup());
      JPanel essencesPanel=_essences.getPanel();
      panel.add(essencesPanel,c);
      c.gridy++;
    }
    // Details
    JPanel detailPanel=buildDetailsPanel();
    if (detailPanel!=null)
    {
      panel.add(detailPanel,c);
      c.gridy++;
    }
    // Value
    Money money=getValue();
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
      _disenchantment=new DisenchantmentResultPanelController(getParent(),disenchantment);
      JPanel disenchantmentPanel=_disenchantment.getPanel();
      disenchantmentPanel.setBorder(GuiFactory.buildTitledBorder(Labels.getLabel("item.form.disenchantment.border")));
      c=new GridBagConstraints(0,y,1,1,0.0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
      panel.add(disenchantmentPanel,c);
    }
    // Padding to push everything on left and top
    JPanel paddingPanel=GuiFactory.buildPanel(new BorderLayout());
    c.fill=GridBagConstraints.BOTH;
    c.weightx=1.0;
    c.weighty=1.0;
    panel.add(paddingPanel,c);
    return panel;
  }

  private Money getValue()
  {
    Money money;
    if (_itemLevel!=null)
    {
      money=_item.getValue(_itemLevel.intValue());
    }
    else
    {
      money=_item.getValueAsMoney();
    }
    return money;
  }

  private List<String> getAttributesLines()
  {
    List<String> ret=new ArrayList<String>();
    if (UiConfiguration.showTechnicalColumns())
    {
      ret.add(Labels.getFieldLabel("item.form.id")+_item.getIdentifier());
    }
    // Quality
    ItemQuality quality=_item.getQuality();
    if (quality!=null)
    {
      ret.add(Labels.getFieldLabel("item.form.quality")+_item.getQuality().getLabel());
    }
    // Equipment category
    EquipmentCategory equipmentCategory=_item.getEquipmentCategory();
    if (equipmentCategory!=null)
    {
      ret.add(Labels.getFieldLabel("item.form.equipmentCategory")+equipmentCategory.getLabel());
    }
    else
    {
      String category=getItemCategory();
      if (!category.isEmpty())
      {
        ret.add(Labels.getFieldLabel("item.form.category")+category);
      }
    }
    // Slot
    EquipmentLocation location=_item.getEquipmentLocation();
    if ((location!=null) && (location!=EquipmentLocations.NONE))
    {
      ret.add(Labels.getFieldLabel("item.form.location")+location.getLabel());
    }
    // Weapon specifics
    if (_item instanceof Weapon)
    {
      List<String> weaponLines=getWeaponAttributeLines((Weapon)_item);
      ret.addAll(weaponLines);
    }
    // Item level
    Integer itemLevel=_item.getItemLevel();
    if (itemLevel!=null)
    {
      if (_itemLevel!=null)
      {
        itemLevel=_itemLevel;
      }
      ret.add(Labels.getFieldLabel("item.form.itemLevel")+itemLevel.toString());
    }
    // Tracery complements
    Tracery tracery=TraceriesManager.getInstance().getTracery(_item.getIdentifier());
    if (tracery!=null)
    {
      int maxItemLevel=tracery.getMaxItemLevel();
      int increment=tracery.getLevelUpIncrement();
      String label=Labels.getLabel("item.form.enhancementLimit",new Object[] {Integer.valueOf(maxItemLevel),Integer.valueOf(increment)});
      ret.add(label);
    }
    // Enhancement rune complements
    EnhancementRune enhancementRune=EnhancementRunesManager.getInstance().getEnhancementRune(_item.getIdentifier());
    if (enhancementRune!=null)
    {
      Integer minItemLevel=Integer.valueOf(enhancementRune.getMinItemLevel());
      Integer maxItemLevel=Integer.valueOf(enhancementRune.getMaxItemLevel());
      Integer increment=Integer.valueOf(enhancementRune.getLevelUpIncrement());
      String label=Labels.getLabel("item.form.enhancementItemLevels",new Object[] {minItemLevel,maxItemLevel,increment});
      ret.add(label);
    }
    // Durability
    Integer durability=_item.getDurability();
    if (durability!=null)
    {
      ret.add(Labels.getFieldLabel("item.form.durability")+durability.toString());
    }
    // Requirements
    String requirements=RequirementsUtils.buildRequirementString(this,_item.getUsageRequirements());
    if (!requirements.isEmpty())
    {
      ret.add(Labels.getFieldLabel("item.form.requirements")+requirements);
    }
    // Attributes
    {
      String attributes=buildAttributesString();
      if (!attributes.isEmpty())
      {
        ret.add(attributes);
      }
    }
    return ret;
  }

  private String getItemCategory()
  {
    String category=_item.getSubCategory();
    // Essence specifics
    boolean isEssence=(_item instanceof Essence);
    if (isEssence)
    {
      Essence essence=(Essence)_item;
      category=essence.getType().getLabel();
    }
    if (category!=null)
    {
      Integer tier=_item.getTier();
      if (tier!=null)
      {
        category=category+", tier "+tier; // I18n
      }
    }
    return (category==null)?"":category;
  }

  private List<String> getWeaponAttributeLines(Weapon weapon)
  {
    List<String> ret=new ArrayList<String>();
    // Damage type
    DamageType damageType=weapon.getDamageType();
    if (damageType!=null)
    {
      ret.add(Labels.getFieldLabel("item.form.damageType")+damageType.getName());
    }
    // Damage range
    int minDamage=getMinDamage(weapon);
    int maxDamage=getMaxDamage(weapon);
    ret.add(Labels.getFieldLabel("item.form.damage")+minDamage+" - "+maxDamage);
    // DPS
    float dps=getDPS(weapon);
    String dpsStr=L10n.getString(dps,1);
    ret.add(Labels.getFieldLabel("item.form.dps")+dpsStr);
    // Speed
    WeaponSpeedEntry speedData=weapon.getSpeed();
    if (speedData!=null)
    {
      float duration=speedData.getBaseActionDuration();
      String durationStr=L10n.getString(duration,1);
      ret.add(Labels.getFieldLabel("item.form.speed")+durationStr);
    }
    return ret;
  }

  private float getDPS(Weapon weapon)
  {
    boolean isLive=LotroCoreConfig.isLive();
    if (isLive)
    {
      Integer itemLevel=(_itemLevel!=null)?_itemLevel:weapon.getItemLevel();
      int baseItemLevel=(itemLevel!=null)?itemLevel.intValue():1;
      return weapon.computeDPS(baseItemLevel);
    }
    return weapon.getDPS();
  }

  private int getMinDamage(Weapon weapon)
  {
    boolean isLive=LotroCoreConfig.isLive();
    if (isLive)
    {
      Integer itemLevel=(_itemLevel!=null)?_itemLevel:weapon.getItemLevel();
      int baseItemLevel=(itemLevel!=null)?itemLevel.intValue():1;
      return Math.round(weapon.computeMinDamage(baseItemLevel));
    }
    return weapon.getMinDamage();
  }

  private int getMaxDamage(Weapon weapon)
  {
    boolean isLive=LotroCoreConfig.isLive();
    if (isLive)
    {
      Integer itemLevel=(_itemLevel!=null)?_itemLevel:weapon.getItemLevel();
      int baseItemLevel=(itemLevel!=null)?itemLevel.intValue():1;
      return Math.round(weapon.computeMaxDamage(baseItemLevel));
    }
    return weapon.getMaxDamage();
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
      sb.append(Labels.getLabel("item.form.unique"));
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
      sb.append(Labels.getLabel("item.form.stacksTo",new Object[]{stack}));
    }
    String ret=sb.toString();
    return ret;
  }

  private JEditorPane buildDescription()
  {
    JEditorPane editor=null;
    String description=_item.getDescription();
    if (!description.isEmpty())
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
    if (!grantedElements.isEmpty())
    {
      GridBagConstraints c=new GridBagConstraints(0,y,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
      ret.add(GuiFactory.buildLabel(Labels.getFieldLabel("item.form.grants")),c);
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
    if (!itemXPs.isEmpty())
    {
      for(ItemXP itemXP : itemXPs)
      {
        GridBagConstraints c=new GridBagConstraints(0,y,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
        String label=Labels.getLabel("item.form.givesItemXP",new Object[]{Integer.valueOf(itemXP.getAmount())});
        ret.add(GuiFactory.buildLabel(label),c);
        y++;
      }
    }
    // Virtue XP
    List<VirtueXP> virtueXPs=mgr.getItemDetails(VirtueXP.class);
    if (!virtueXPs.isEmpty())
    {
      for(VirtueXP virtueXP : virtueXPs)
      {
        GridBagConstraints c=new GridBagConstraints(0,y,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
        int amount=virtueXP.getAmount();
        boolean bonus=virtueXP.isBonus();
        String label=Labels.getLabel("item.form.givesBonusVirtueXP",new Object[]{Integer.valueOf(amount),bonus?Integer.valueOf(1):Integer.valueOf(0)});
        ret.add(GuiFactory.buildLabel(label),c);
        y++;
      }
    }
    // Reputation
    List<ItemReputation> reputations=mgr.getItemDetails(ItemReputation.class);
    if (!reputations.isEmpty())
    {
      for(ItemReputation reputation : reputations)
      {
        GridBagConstraints c=new GridBagConstraints(0,y,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
        Faction faction=reputation.getFaction();
        int amount=reputation.getAmount();
        String rawFactionName=faction.getName();
        String factionName=ContextRendering.render(this,rawFactionName);
        String label=Labels.getLabel("item.form.givesReputation",new Object[]{Integer.valueOf(amount),factionName});
        ret.add(GuiFactory.buildLabel(label),c);
        y++;
      }
    }
    // Weapon slayer
    List<WeaponSlayerInfo> weaponSlayerInfos=mgr.getItemDetails(WeaponSlayerInfo.class);
    if (!weaponSlayerInfos.isEmpty())
    {
      for(WeaponSlayerInfo weaponSlayerInfo : weaponSlayerInfos)
      {
        GridBagConstraints c=new GridBagConstraints(0,y,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
        String label=weaponSlayerInfo.getLabel();
        ret.add(GuiFactory.buildLabel(label),c);
        y++;
      }
    }
    // Usage Cool-down
    y=handleUsageCooldown(mgr,ret,y);
    // Allegiance points
    y=handleAllegiancePoints(mgr,ret,y);
    // Housing hooks
    y=handleHousingHooks(mgr,ret,y);
    // Decay
    y=handleDecay(mgr,ret,y);
    // Portrait frames
    handlePortraitFrames(mgr,ret,y);
    return ret;
  }

  private int handleUsageCooldown(ItemDetailsManager mgr, JPanel panel, int y)
  {
    ItemUsageCooldown cooldown=mgr.getFirstItemDetail(ItemUsageCooldown.class);
    if (cooldown!=null)
    {
      GridBagConstraints c=new GridBagConstraints(0,y,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
      float duration=cooldown.getDuration();
      String durationStr=Duration.getDurationString((int)duration);
      String label="Cooldown: "+durationStr; // I18n
      panel.add(GuiFactory.buildLabel(label),c);
      y++;
    }
    return y;
  }

  private int handleAllegiancePoints(ItemDetailsManager mgr, JPanel panel, int y)
  {
    List<AllegiancePoints> allegiancePoints=mgr.getItemDetails(AllegiancePoints.class);
    if (!allegiancePoints.isEmpty())
    {
      for(AllegiancePoints allegiancePointsEntry : allegiancePoints)
      {
        GridBagConstraints c=new GridBagConstraints(0,y,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
        String allegianceGroup=allegiancePointsEntry.getGroup().getLabel();
        Integer points=Integer.valueOf(allegiancePointsEntry.getPoints());
        String label=Labels.getLabel("item.form.worthAllegiancePoints",new Object[]{points,allegianceGroup});
        panel.add(GuiFactory.buildLabel(label),c);
        y++;
      }
    }
    return y;
  }

  private int handleHousingHooks(ItemDetailsManager mgr, JPanel panel, int y)
  {
    HousingHooks housingHooks=mgr.getFirstItemDetail(HousingHooks.class);
    if (housingHooks!=null)
    {
      GridBagConstraints c=new GridBagConstraints(0,y,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
      List<HousingHookCategory> categories=housingHooks.getHookCategories();
      String categoriesStr=formatEnumEntries(categories);
      String label=Labels.getFieldLabel("item.form.decorationCategory")+categoriesStr;
      panel.add(GuiFactory.buildLabel(label),c);
      y++;
    }
    return y;
  }

  private int handleDecay(ItemDetailsManager mgr, JPanel panel, int y)
  {
    ItemDecay decay=mgr.getFirstItemDetail(ItemDecay.class);
    if (decay!=null)
    {
      GridBagConstraints c=new GridBagConstraints(0,y,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
      float decayDuration=decay.getDuration();
      String durationStr=Duration.getShortDurationString(decayDuration);
      String label=Labels.getFieldLabel("item.form.decay")+durationStr;
      panel.add(GuiFactory.buildLabel(label),c);
      y++;
    }
    return y;
  }

  private int handlePortraitFrames(ItemDetailsManager mgr, JPanel panel, int y)
  {
    List<ProvidesPortraitFrame> providedPortraitFrames=mgr.getItemDetails(ProvidesPortraitFrame.class);
    if (!providedPortraitFrames.isEmpty())
    {
      GridBagConstraints c=new GridBagConstraints(0,y,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
      panel.add(GuiFactory.buildLabel(Labels.getFieldLabel("item.form.provides")),c);
      y++;
      for(ProvidesPortraitFrame providedPortraitFrame : providedPortraitFrames)
      {
        PortraitFrameDescription portraitFrame=providedPortraitFrame.getPortraitFrame();
        PageIdentifier ref=ReferenceConstants.getPortraitFrameReference(portraitFrame.getCode());
        String text=portraitFrame.getName();
        NavigationHyperLink controller=new NavigationHyperLink(getParent(),text,ref);
        _links.add(controller);
        c=new GridBagConstraints(0,y,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,5,0),0,0);
        panel.add(controller.getLabel(),c);
        y++;
      }
    }
    return y;
  }

  private String formatEnumEntries(List<? extends LotroEnumEntry> entries)
  {
    StringBuilder sb=new StringBuilder();
    for(LotroEnumEntry entry : entries)
    {
      if (sb.length()>0) sb.append(", ");
      sb.append(entry.getLabel());
    }
    return sb.toString();
  }

  private IconAndLinkPanelController buildGrantedElementPanel(GrantedElement<?> grantedElement)
  {
    Object element=grantedElement.getGrantedElement();
    if (element instanceof SkillDescription)
    {
      SkillDescription skill=(SkillDescription)element;
      return SharedPanels.buildSkillPanel(getParent(),skill);
    }
    if (element instanceof TraitDescription)
    {
      TraitDescription trait=(TraitDescription)element;
      return SharedPanels.buildTraitPanel(getParent(),trait);
    }
    if (element instanceof EmoteDescription)
    {
      EmoteDescription emote=(EmoteDescription)element;
      return SharedPanels.buildEmotePanel(getParent(),emote);
    }
    return null;
  }

  @Override
  public void dispose()
  {
    super.dispose();
    // Data
    _item=null;
    _itemLevel=null;
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
    if (_essences!=null)
    {
      _essences.dispose();
      _essences=null;
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
    if (_saveItemIcon!=null)
    {
      _saveItemIcon.dispose();
      _saveItemIcon=null;
    }
    if (_links!=null)
    {
      for(NavigationHyperLink link : _links)
      {
        link.dispose();
      }
      _links=null;
    }
  }
}
