package delta.games.lotro.gui.lore.collections.pets.form;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Window;

import javax.swing.JDialog;
import javax.swing.JPanel;

import delta.common.ui.swing.windows.DefaultDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.lore.collections.pets.CosmeticPetDescription;

/**
 * Controller for a "pet display" window.
 * @author DAM
 */
public class PetDisplayWindowController extends DefaultDialogController
{
  // Controllers
  private PetDisplayPanelController _controller;
  // Data
  private CosmeticPetDescription _pet;

  /**
   * Constructor.
   * @param parent Parent controller.
   * @param pet Pet to show.
   */
  public PetDisplayWindowController(WindowController parent, CosmeticPetDescription pet)
  {
    super(parent);
    _pet=pet;
    setPet(pet);
  }

  /**
   * Set pet to display.
   * @param pet Pet to display.
   */
  private void setPet(CosmeticPetDescription pet)
  {
    _controller=new PetDisplayPanelController(pet);
    JDialog dialog=getDialog();
    Container container=dialog.getContentPane();
    container.removeAll();
    JPanel panel=_controller.getPanel();
    container.add(panel,BorderLayout.CENTER);
    dialog.setTitle("Pet: "+pet.getName()); // 18n
    dialog.pack();
    WindowController controller=getParentController();
    if (controller!=null)
    {
      Window parentWindow=controller.getWindow();
      dialog.setLocationRelativeTo(parentWindow);
    }
    dialog.setResizable(true);
  }

  @Override
  public String getWindowIdentifier()
  {
    return getId(_pet);
  }

  /**
   * Get the identifier for a pet display window.
   * @param pet Pet to show.
   * @return A window identifier.
   */
  public static String getId(CosmeticPetDescription pet)
  {
    return "PET_DISPLAY#"+pet.getIdentifier();
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    disposePetPanel();
    super.dispose();
  }

  private void disposePetPanel()
  {
    if (_controller!=null)
    {
      _controller.dispose();
      _controller=null;
    }
  }
}
