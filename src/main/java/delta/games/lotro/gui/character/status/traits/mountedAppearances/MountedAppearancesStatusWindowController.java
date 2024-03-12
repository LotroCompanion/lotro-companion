package delta.games.lotro.gui.character.status.traits.mountedAppearances;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.DefaultWindowController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.status.traits.mountedAppearances.MountedAppearancesIo;
import delta.games.lotro.character.status.traits.shared.SlottedTraitsStatus;
import delta.games.lotro.character.traits.TraitDescription;
import delta.games.lotro.character.traits.TraitsManager;
import delta.games.lotro.common.enums.LotroEnum;
import delta.games.lotro.common.enums.LotroEnumsRegistry;
import delta.games.lotro.common.enums.TraitNature;
import delta.games.lotro.gui.character.status.traits.TraitsStatusPanelController;

/**
 * Controller for a window that show the status of mounted appearances.
 * @author DAM
 */
public class MountedAppearancesStatusWindowController extends DefaultWindowController
{
  private static final int MIN_HEIGHT=300;
  private static final int INITIAL_HEIGHT=400;

  /**
   * Identifier for this window.
   */
  public static final String IDENTIFIER="MOUNTED_APPEARANCES_STATUS_WINDOW";

  // Controllers
  private MountedAppearancesSlotsDisplayPanelController _slottedPanel;
  private TraitsStatusPanelController _statusPanel;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param toon Character file.
   */
  public MountedAppearancesStatusWindowController(WindowController parent, CharacterFile toon)
  {
    super(parent);
    List<TraitDescription> traits=findMountedAppearancesTraits();
    SlottedTraitsStatus status=MountedAppearancesIo.load(toon);
    _slottedPanel=new MountedAppearancesSlotsDisplayPanelController(this,status.getSlotsStatus());
    _statusPanel=new TraitsStatusPanelController(this,traits,status.getAvailableTraitsStatus());
  }

  private List<TraitDescription> findMountedAppearancesTraits()
  {
    LotroEnum<TraitNature> traitNatureEnum=LotroEnumsRegistry.getInstance().get(TraitNature.class);
    TraitNature traitNature=traitNatureEnum.getEntry(16);
    TraitsManager traitsMgr=TraitsManager.getInstance();
    List<TraitDescription> traits=traitsMgr.getTraitsForNature(traitNature);
    return traits;
  }

  @Override
  protected JFrame build()
  {
    JFrame frame=super.build();
    frame.setTitle("Mounted Appearances"); // I18n
    frame.pack();
    frame.setSize(frame.getWidth()+30,INITIAL_HEIGHT);
    frame.setMinimumSize(new Dimension(frame.getWidth()+30,MIN_HEIGHT));
    return frame;
  }

  @Override
  public String getWindowIdentifier()
  {
    return IDENTIFIER;
  }

  @Override
  public void configureWindow()
  {
    automaticLocationSetup();
  }

  @Override
  protected JPanel buildContents()
  {
    JPanel panel=GuiFactory.buildBackgroundPanel(new GridBagLayout());
    // Slotted traits
    JPanel slottedTraitsPanel=_slottedPanel.getPanel();
    slottedTraitsPanel.setBorder(GuiFactory.buildTitledBorder("Slotted traits"));
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(slottedTraitsPanel,c);
    // Available traits
    JPanel availableTraitsPanel=_statusPanel.getPanel();
    availableTraitsPanel.setBorder(GuiFactory.buildTitledBorder("Available traits"));
    c=new GridBagConstraints(1,0,1,1,1.0,1.0,GridBagConstraints.NORTHWEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
    panel.add(availableTraitsPanel,c);
    return panel;
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    saveBoundsPreferences();
    super.dispose();
    // Controllers
    if (_slottedPanel!=null)
    {
      _slottedPanel.dispose();
      _slottedPanel=null;
    }
    if (_statusPanel!=null)
    {
      _statusPanel.dispose();
      _statusPanel=null;
    }
  }
}
