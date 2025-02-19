package delta.games.lotro.gui.lore.housing.form;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.navigator.AbstractNavigablePanelController;
import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.games.lotro.character.status.housing.HouseAddress;
import delta.games.lotro.common.money.Money;
import delta.games.lotro.gui.LotroIconsManager;
import delta.games.lotro.gui.common.money.MoneyDisplayController;
import delta.games.lotro.lore.housing.HouseDefinition;
import delta.games.lotro.lore.housing.HousingManager;
import delta.games.lotro.lore.housing.HousingSystem;
import delta.games.lotro.lore.housing.Neighborhood;
import delta.games.lotro.lore.housing.NeighborhoodTemplate;
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
    HousingManager mgr=HousingSystem.getInstance().getData();
    int houseID=_address.getHouseID();
    HouseDefinition house=mgr.getHouse(houseID);
    String title="House: "+house.getAddress();
    return title;
  }

  private JPanel build()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    // Icon
    HousingManager mgr=HousingSystem.getInstance().getData();
    int houseID=_address.getHouseID();
    HouseDefinition house=mgr.getHouse(houseID);
    int iconID=house.getInfo().getIconPanoramaID();
    ImageIcon icon=LotroIconsManager.getHousingIcon(iconID);
    JLabel iconLabel=GuiFactory.buildIconLabel(icon);
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,5,0,5),0,0);
    panel.add(iconLabel,c);
    // Main panel
    JPanel mainAttrs=buildMainAttrsPanel(house);
    c=new GridBagConstraints(0,1,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,5,0,5),0,0);
    panel.add(mainAttrs,c);
    return panel;
  }

  private JComponent buildSelectableLabel(String text)
  {
    JTextField f=GuiFactory.buildTextField(text);
    f.setEditable(false);
    f.setBorder(null);
    f.setOpaque(false);
    f.setFont(UIManager.getFont("Label.font"));
    Dimension d=f.getPreferredSize();
    f.setPreferredSize(new Dimension(d.width+5,d.height));
    return f;
  }

  private JPanel buildMainAttrsPanel(HouseDefinition house)
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    addAttributesLines(house,panel,c);
    int y=c.gridy;
    // Description
    JEditorPane description=buildDescription(house.getDescription());
    if (description!=null)
    {
      c=new GridBagConstraints(0,y,1,1,1.0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.HORIZONTAL,new Insets(5,5,5,5),0,0);
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

  private void addAttributesLines(HouseDefinition house, JPanel panel, GridBagConstraints c)
  {
    HousingManager mgr=HousingSystem.getInstance().getData();
    int neightborhoodID=_address.getNeighborhoodID();
    Neighborhood neighborhood=mgr.getNeighborhood(neightborhoodID);
    NeighborhoodTemplate template=neighborhood.getTemplate();
    // Address
    String address=house.getAddress();
    addAttributeLine(address,panel,c);
    // Neighborhood
    String neighborhoodStr=neighborhood.getName()+", "+template.getName();
    addAttributeLine(neighborhoodStr,panel,c);
    // Type
    String type=house.getHouseType().getLabel();
    addAttributeLine(type,panel,c);
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
    panelLine.add(buildSelectableLabel(line));
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
