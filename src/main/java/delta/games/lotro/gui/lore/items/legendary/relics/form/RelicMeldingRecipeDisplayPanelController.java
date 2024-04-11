package delta.games.lotro.gui.lore.items.legendary.relics.form;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.labels.HyperLinkController;
import delta.common.ui.swing.navigator.NavigablePanelController;
import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.games.lotro.common.Named;
import delta.games.lotro.gui.LotroIconsManager;
import delta.games.lotro.gui.lore.items.ItemUiTools;
import delta.games.lotro.gui.lore.items.legendary.relics.RelicUiTools;
import delta.games.lotro.gui.utils.IconController;
import delta.games.lotro.gui.utils.IconControllerFactory;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.legendary.relics.Relic;
import delta.games.lotro.lore.relics.CountedRelic;
import delta.games.lotro.lore.relics.melding.MeldingInput;
import delta.games.lotro.lore.relics.melding.MeldingOutput;
import delta.games.lotro.lore.relics.melding.RelicMeldingOutputEntry;
import delta.games.lotro.lore.relics.melding.RelicMeldingRecipe;

/**
 * Controller for a relic display panel.
 * @author DAM
 */
public class RelicMeldingRecipeDisplayPanelController implements NavigablePanelController
{
  // Data
  private RelicMeldingRecipe _recipe;
  // GUI
  private JPanel _panel;
  // Controllers
  private NavigatorWindowController _parent;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param recipe Recipe to show.
   */
  public RelicMeldingRecipeDisplayPanelController(NavigatorWindowController parent, RelicMeldingRecipe recipe)
  {
    _parent=parent;
    _recipe=recipe;
  }

  @Override
  public String getTitle()
  {
    return "Relic Melding Recipe: "+_recipe.getName();
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

    // Summary panel
    JPanel summaryPanel=buildSummaryPanel();
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,5,0,5),0,0);
    panel.add(summaryPanel,c);
    // Input
    JPanel input=buildInputPanel(_recipe.getInput());
    if (input!=null)
    {
      input.setBorder(GuiFactory.buildTitledBorder("Input"));
      c=new GridBagConstraints(0,1,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,5,0,5),0,0);
      panel.add(input,c);
    }
    // Output
    JPanel output=buildOutputPanel(_recipe.getOutput());
    output.setBorder(GuiFactory.buildTitledBorder("Output"));
    c=new GridBagConstraints(0,2,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    panel.add(output,c);
    return panel;
  }

  private JPanel buildSummaryPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    // Main data line
    {
      JPanel panelLine=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
      // Icon
      Icon icon=getIcon();
      JLabel iconLabel=GuiFactory.buildIconLabel(icon);
      panelLine.add(iconLabel);
      // Name
      String name=_recipe.getName();
      JLabel nameLabel=GuiFactory.buildLabel(name);
      nameLabel.setFont(nameLabel.getFont().deriveFont(16f).deriveFont(Font.BOLD));
      panelLine.add(nameLabel);
      panel.add(panelLine,c);
      c.gridy++;
    }
    // Category
    {
      String category=_recipe.getCategory();
      JPanel panelLine=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
      panel.add(panelLine,c);
      c.gridy++;
      panelLine.add(GuiFactory.buildLabel("Category: "+category));
    }
    // Cost
    {
      int cost=_recipe.getCost();
      JPanel panelLine=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
      panel.add(panelLine,c);
      c.gridy++;
      String label=(cost>1)?"shards":"shard";
      panelLine.add(GuiFactory.buildLabel("Cost: "+cost+" "+label));
    }

    // Padding to push everything on left
    JPanel paddingPanel=GuiFactory.buildPanel(new BorderLayout());
    c.fill=GridBagConstraints.HORIZONTAL;
    c.weightx=1.0;
    panel.add(paddingPanel,c);

    return panel;
  }

  private Icon getIcon()
  {
    int iconId=_recipe.getIconOverride();
    if (iconId==0)
    {
      Named result=_recipe.getOutput().getFirstResult();
      if (result instanceof Relic)
      {
        Relic relic=(Relic)result;
        return LotroIconsManager.getRelicIcon(relic.getIconFilename());
      }
      if (result instanceof Item)
      {
        Item item=(Item)result;
        return LotroIconsManager.getItemIcon(item.getIcon());
      }
      return null;
    }
    return LotroIconsManager.getRelicIcon(iconId+".png");
  }

  private JPanel buildInputPanel(MeldingInput input)
  {
    if (input.hasNoInput())
    {
      return null;
    }
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    // Tiers
    JPanel tiersPanel=buildNeededTiersPanel(input);
    int y=0;
    if (tiersPanel!=null)
    {
      GridBagConstraints c=new GridBagConstraints(0,y,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
      ret.add(tiersPanel,c);
      y++;
    }
    // Specific relics
    JPanel specificRelicsPanel=buildSpecificRelicsPanel(input);
    if (specificRelicsPanel!=null)
    {
      GridBagConstraints c=new GridBagConstraints(0,y,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
      ret.add(specificRelicsPanel,c);
    }
    return ret;
  }

  private JPanel buildNeededTiersPanel(MeldingInput input)
  {
    List<Integer> tiers=input.getNeededTiers();
    if (tiers.isEmpty())
    {
      return null;
    }
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    int y=0;
    for(Integer tier : tiers)
    {
      int count=input.getCountForTier(tier.intValue());
      String label=count+" relic"+((count>1)?"s":"")+" of Tier "+tier;
      GridBagConstraints c=new GridBagConstraints(0,y,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,5,0,5),0,0);
      ret.add(GuiFactory.buildLabel(label),c);
      y++;
    }
    return ret;
  }

  private JPanel buildSpecificRelicsPanel(MeldingInput input)
  {
    List<CountedRelic> relics=input.getNeededRelics();
    if (relics.isEmpty())
    {
      return null;
    }
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    int y=0;
    for(CountedRelic countedRelic : relics)
    {
      int count=countedRelic.getCount();
      Relic relic=countedRelic.getRelic();
      IconController relicIconCtrl=IconControllerFactory.buildRelicIcon(_parent,relic,count);
      HyperLinkController link=RelicUiTools.buildRelicLink(_parent,relic);
      GridBagConstraints c=new GridBagConstraints(0,y,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,5,0,5),0,0);
      ret.add(relicIconCtrl.getIcon(),c);
      c=new GridBagConstraints(1,y,1,1,1,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,5,0,5),0,0);
      ret.add(link.getLabel(),c);
      y++;
    }
    return ret;
  }

  private JPanel buildOutputPanel(MeldingOutput output)
  {
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    int y=0;
    for(RelicMeldingOutputEntry entry : output.getPossibleOutputs())
    {
      IconController iconCtrl=null;
      HyperLinkController link=null;
      Relic relic=entry.getRelic();
      if (relic!=null)
      {
        iconCtrl=IconControllerFactory.buildRelicIcon(_parent,relic,1);
        link=RelicUiTools.buildRelicLink(_parent,relic);
      }
      Item item=entry.getItem();
      if (item!=null)
      {
        iconCtrl=IconControllerFactory.buildItemIcon(_parent,item,1);
        link=ItemUiTools.buildItemLink(_parent,item);
      }
      if ((iconCtrl!=null) && (link!=null))
      {
        GridBagConstraints c=new GridBagConstraints(0,y,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,5,0,5),0,0);
        ret.add(iconCtrl.getIcon(),c);
        c=new GridBagConstraints(1,y,1,1,1,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,5,0,5),0,0);
        ret.add(link.getLabel(),c);
        y++;
      }
    }
    return ret;
  }

  @Override
  public void dispose()
  {
    // Data
    _recipe=null;
    // Controllers
    _parent=null;
    // UI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
  }
}
