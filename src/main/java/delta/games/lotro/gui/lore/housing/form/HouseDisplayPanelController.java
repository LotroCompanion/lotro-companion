package delta.games.lotro.gui.lore.housing.form;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JEditorPane;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.navigator.AbstractNavigablePanelController;
import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.status.housing.HouseAddress;
import delta.games.lotro.common.money.Money;
import delta.games.lotro.gui.common.money.MoneyDisplayController;
import delta.games.lotro.lore.housing.HouseDefinition;
import delta.games.lotro.lore.housing.HousingManager;
import delta.games.lotro.lore.housing.HousingSystem;
import delta.games.lotro.utils.html.HtmlUtils;

/**
 * Controller for an item display panel.
 * @author DAM
 */
public class HouseDisplayPanelController extends AbstractNavigablePanelController
{
  // Data
  private HouseAddress _address;
  // Controllers
  private HouseSummaryDisplayPanelController _summary;
  private MoneyDisplayController _price;
  private MoneyDisplayController _upkeep;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param address Address of the house to show.
   */
  public HouseDisplayPanelController(NavigatorWindowController parent, HouseAddress address)
  {
    super(parent);
    _address=address;
    setPanel(build());
  }

  @Override
  public String getTitle()
  {
    HouseDefinition house=getHouse();
    String title="House: "+house.getAddress();
    return title;
  }

  private HouseDefinition getHouse()
  {
    HousingManager mgr=HousingSystem.getInstance().getData();
    int houseID=_address.getHouseID();
    HouseDefinition house=mgr.getHouse(houseID);
    return house;
  }

  private JPanel build()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    // Summary
    WindowController parent=getWindowController();
    _summary=new HouseSummaryDisplayPanelController(parent,_address);
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,5,0,5),0,0);
    panel.add(_summary.getPanel(),c);
    // Main panel
    JPanel mainAttrs=buildMainAttrsPanel();
    c=new GridBagConstraints(0,1,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,5,0,5),0,0);
    panel.add(mainAttrs,c);
    return panel;
  }

  private JPanel buildMainAttrsPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);

    HouseDefinition house=getHouse();
    // Cost
    boolean isPremium=house.isPremium();
    Money price=house.getPrice();
    if (isPremium)
    {
      int mcPrice=price.getInternalValue();
      String line="Price: "+mcPrice+" mithril coins";
      addAttributeLine(line,panel,c);
    }
    else
    {
      _price=new MoneyDisplayController();
      JPanel upkeepPanel=buildMoneyPanel("Price: ",_price,price);
      panel.add(upkeepPanel,c);
      c.gridy++;
    }
    // Upkeep
    Money upkeep=house.getUpkeep();
    _upkeep=new MoneyDisplayController();
    JPanel upkeepPanel=buildMoneyPanel("Upkeep Cost: ",_upkeep,upkeep);
    panel.add(upkeepPanel,c);
    c.gridy++;
    // Description
    JEditorPane description=buildDescription(house.getDescription());
    if (description!=null)
    {
      c=new GridBagConstraints(0,c.gridy,1,1,1.0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.HORIZONTAL,new Insets(5,5,5,5),0,0);
      panel.add(description,c);
    }
    // Padding to push everything on left and top
    JPanel paddingPanel=GuiFactory.buildPanel(new BorderLayout());
    c.fill=GridBagConstraints.BOTH;
    c.weightx=1.0;
    c.weighty=1.0;
    panel.add(paddingPanel,c);
    return panel;
  }

  private JPanel buildMoneyPanel(String prefix, MoneyDisplayController ctrl, Money value)
  {
    ctrl.setMoney(value);
    JPanel ret=GuiFactory.buildPanel(new FlowLayout());
    ret.add(GuiFactory.buildLabel(prefix));
    ret.add(ctrl.getPanel());
    return ret;
  }

  private void addAttributeLine(String line, JPanel panel, GridBagConstraints c)
  {
    JPanel panelLine=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
    panel.add(panelLine,c);
    c.gridy++;
    panelLine.add(GuiFactory.buildLabel(line));
  }

  private JEditorPane buildDescription(String description)
  {
    JEditorPane editor=null;
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

  @Override
  public void dispose()
  {
    super.dispose();
    // Data
    _address=null;
    // Controllers
    if (_summary!=null)
    {
      _summary.dispose();
      _price=null;
    }
    if (_price!=null)
    {
      _price.dispose();
      _price=null;
    }
    if (_upkeep!=null)
    {
      _upkeep.dispose();
      _upkeep=null;
    }
  }
}
