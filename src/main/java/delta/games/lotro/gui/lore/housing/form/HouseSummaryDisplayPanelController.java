package delta.games.lotro.gui.lore.housing.form;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.panels.AbstractPanelController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.status.housing.HouseAddress;
import delta.games.lotro.gui.LotroIconsManager;
import delta.games.lotro.lore.housing.HouseDefinition;
import delta.games.lotro.lore.housing.HousingManager;
import delta.games.lotro.lore.housing.HousingSystem;
import delta.games.lotro.lore.housing.Neighborhood;
import delta.games.lotro.lore.housing.NeighborhoodTemplate;

/**
 * Controller for a panel that shows summary data about a house.
 * @author DAM
 */
public class HouseSummaryDisplayPanelController extends AbstractPanelController
{
  // Data
  private HouseAddress _address;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param address Address of the house to show.
   */
  public HouseSummaryDisplayPanelController(WindowController parent, HouseAddress address)
  {
    super(parent);
    _address=address;
    setPanel(build());
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
    JPanel mainAttrs=buildSummaryAttrsPanel(house);
    c=new GridBagConstraints(1,0,1,1,0.0,0.0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(mainAttrs,c);
    return panel;
  }

  private JPanel buildSummaryAttrsPanel(HouseDefinition house)
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
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
    // Padding to push everything on left and top
    JPanel paddingPanel=GuiFactory.buildPanel(new BorderLayout());
    c.fill=GridBagConstraints.BOTH;
    c.weightx=1.0;
    c.weighty=1.0;
    panel.add(paddingPanel,c);
    return panel;
  }

  private void addAttributeLine(String line, JPanel panel, GridBagConstraints c)
  {
    JPanel panelLine=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
    panel.add(panelLine,c);
    c.gridy++;
    panelLine.add(GuiFactory.buildLabel(line));
  }

  @Override
  public void dispose()
  {
    super.dispose();
    // Data
    _address=null;
  }
}
