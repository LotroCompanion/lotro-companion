package delta.games.lotro.gui.character.status.allegiances.summary;

import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

import delta.common.ui.swing.GuiFactory;
import delta.games.lotro.character.status.allegiances.AllegianceStatus;
import delta.games.lotro.gui.LotroIconsManager;
import delta.games.lotro.lore.allegiances.AllegianceDescription;

/**
 * Controller for the UI items to display a single allegiance status summary.
 * @author DAM
 */
public class SingleAllegianceGadgetsController
{
  // Data
  private AllegianceStatus _status;
  // UI
  // - allegiance icon (small size)
  private JLabel _icon;
  // - allegiance name
  private JLabel _name;
  // - allegiance state
  private JLabel _state;
  // - details button
  private JButton _button;

  /**
   * Constructor.
   * @param status Allegiance status to use.
   */
  public SingleAllegianceGadgetsController(AllegianceStatus status)
  {
    _status=status;
    // Icon
    _icon=GuiFactory.buildIconLabel(null);
    // Name
    _name=GuiFactory.buildLabel("?");
    // State
    _state=GuiFactory.buildLabel("?");
    // Button
    _button=GuiFactory.buildButton("Details..."); // I18n
    // Init
    setAllegiance(status.getAllegiance());
    setAllegianceStatus(status);
  }

  /**
   * Get the managed allegiance.
   * @return an allegiance.
   */
  public AllegianceStatus getStatus()
  {
    return _status;
  }

  /**
   * Get the managed icon.
   * @return the managed icon.
   */
  public JLabel getIcon()
  {
    return _icon;
  }

  /**
   * Get the gadget for the name.
   * @return a label.
   */
  public JLabel getNameGadget()
  {
    return _name;
  }

  /**
   * Get the gadget for the state.
   * @return a label.
   */
  public JLabel getStateGadget()
  {
    return _state;
  }

  /**
   * Get the details button.
   * @return a button.
   */
  public JButton getDetailsButton()
  {
    return _button;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // UI
    _icon=null;
    _name=null;
    _state=null;
  }

  private void setAllegiance(AllegianceDescription allegiance)
  {
    // Icon
    int iconId=allegiance.getIconId();
    Image image=LotroIconsManager.getAllegianceImage(iconId);
    Image scaledImage=image.getScaledInstance(64,64,Image.SCALE_SMOOTH);
    //Image scaledImage=IconsManager.getScaledImage(image,64,64);
    ImageIcon icon=new ImageIcon(scaledImage);
    _icon.setIcon(icon);
    // Name
    String allegianceName=allegiance.getName();
    _name.setText(allegianceName);
  }

  /**
   * Set the data to display.
   * @param status Status to display.
   */
  private void setAllegianceStatus(AllegianceStatus status)
  {
    // State
    String stateLabel=buildStateLabel(status);
    _state.setText(stateLabel);
  }

  private String buildStateLabel(AllegianceStatus status)
  {
    if (status==null)
    {
      return "Unknown"; // I18n
    }
    Integer level=status.getCurrentLevel();
    if (level==null)
    {
      return "Not Started"; // I18n
    }
    int maxLevel=status.getPoints2LevelCurve().getMaxLevel();
    if (level.intValue()>=maxLevel)
    {
      return "Finished"; // I18n
    }
    return level+" / "+maxLevel;
  }
}
