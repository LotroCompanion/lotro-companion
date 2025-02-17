package delta.games.lotro.gui.character.status.housing;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.panels.AbstractPanelController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.status.housing.AccountHousingData;
import delta.games.lotro.character.status.housing.HouseIdentifier;
import delta.games.lotro.character.status.housing.HouseReference;

/**
 * Panel to display a house status summary.
 * @author DAM
 */
public class CharacterHousingStatusPanelController extends AbstractPanelController
{
  // Data
  private AccountHousingData _data;
  // Controllers
  private List<HouseStatusSummaryPanelController> _houseStatusCtrls;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param data Data to show.
   */
  public CharacterHousingStatusPanelController(WindowController parent, AccountHousingData data)
  {
    super(parent);
    _data=data;
    _houseStatusCtrls=new ArrayList<HouseStatusSummaryPanelController>();
    setPanel(buildPanel());
  }

  private JPanel buildPanel()
  {
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    // Classic house
    HouseReference classicHouse=_data.getClassicHouse();
    if (classicHouse!=null)
    {
      List<HouseReference> houses=new ArrayList<HouseReference>();
      houses.add(classicHouse);
      JPanel classicHousesPanel=buildStatusPanel(houses);
      classicHousesPanel.setBorder(GuiFactory.buildTitledBorder("Classic house"));
      ret.add(classicHousesPanel,c);
      c.gridy++;
    }
    // Premium houses
    List<HouseReference> premiumHouses=_data.getPremiumHouses();
    if (!premiumHouses.isEmpty())
    {
      JPanel premiumHousesPanel=buildStatusPanel(premiumHouses);
      premiumHousesPanel.setBorder(GuiFactory.buildTitledBorder("Premium houses"));
      ret.add(premiumHousesPanel,c);
    }
    return ret;
  }

  private JPanel buildStatusPanel(List<HouseReference> houses)
  {
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    for(HouseReference house : houses)
    {
      WindowController parent=getWindowController();
      HouseIdentifier houseId=new HouseIdentifier(_data.getServer(),house.getAddress());
      HouseStatusSummaryPanelController ctrl=new HouseStatusSummaryPanelController(parent,houseId);
      _houseStatusCtrls.add(ctrl);
      ret.add(ctrl.getPanel(),c);
      c.gridy++;
    }
    return ret;
  }

  @Override
  public void dispose()
  {
    super.dispose();
    if (_houseStatusCtrls!=null)
    {
      for(HouseStatusSummaryPanelController ctrl : _houseStatusCtrls)
      {
        ctrl.dispose();
      }
      _houseStatusCtrls.clear();
      _houseStatusCtrls=null;
    }
  }
}
