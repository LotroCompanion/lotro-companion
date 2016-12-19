package delta.games.lotro.gui.character.virtues;

import java.awt.BorderLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;

import delta.games.lotro.character.stats.virtues.VirtuesSet;
import delta.games.lotro.gui.utils.GuiFactory;
import delta.games.lotro.gui.utils.OKCancelPanelController;
import delta.games.lotro.utils.gui.DefaultDialogController;
import delta.games.lotro.utils.gui.WindowController;

/**
 * Controller for the virtues edition dialog.
 * @author DAM
 */
public class VirtuesEditionDialogController extends DefaultDialogController implements ActionListener
{
  private VirtuesEditionPanelController _virtuesEdition;
  private OKCancelPanelController _okCancelController;
  private VirtuesSet _virtues;

  /**
   * Constructor.
   * @param parentController Parent controller.
   */
  public VirtuesEditionDialogController(WindowController parentController)
  {
    super(parentController);
    _virtuesEdition=new VirtuesEditionPanelController();
  }

  /**
   * Set the virtues to show.
   * @param virtues Virtues to show.
   */
  public void setVirtues(VirtuesSet virtues)
  {
    _virtuesEdition.setVirtues(virtues);
  }

  @Override
  protected JDialog build()
  {
    JDialog dialog=super.build();
    dialog.setTitle("Virtues edition");
    dialog.setResizable(false);
    dialog.pack();
    WindowController controller=getParentController();
    if (controller!=null)
    {
      Window parentWindow=controller.getWindow();
      dialog.setLocationRelativeTo(parentWindow);
    }
    return dialog;
  }

  @Override
  protected JComponent buildContents()
  {
    JPanel panel=GuiFactory.buildPanel(new BorderLayout());
    JPanel virtuesPanel=_virtuesEdition.getPanel();
    panel.add(virtuesPanel,BorderLayout.CENTER);
    _okCancelController=new OKCancelPanelController();
    JPanel okCancelPanel=_okCancelController.getPanel();
    panel.add(okCancelPanel,BorderLayout.SOUTH);
    _okCancelController.getOKButton().addActionListener(this);
    _okCancelController.getCancelButton().addActionListener(this);
    return panel;
  }

  public void actionPerformed(ActionEvent event)
  {
    String action=event.getActionCommand();
    if (OKCancelPanelController.OK_COMMAND.equals(action))
    {
      _virtues=_virtuesEdition.getVirtues();
    }
    else if (OKCancelPanelController.CANCEL_COMMAND.equals(action))
    {
      // Nothing!
    }
    hide();
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    super.dispose();
    if (_okCancelController!=null)
    {
      _okCancelController.dispose();
      _okCancelController=null;
    }
    if (_virtuesEdition!=null)
    {
      _virtuesEdition.dispose();
      _virtuesEdition=null;
    }
    _virtues=null;
  }

  /**
   * Edit virtues.
   * @param parent Parent controller.
   * @param virtues Virtues to show.
   * @return The edited virtues or <code>null</code> if the window was closed or canceled.
   */
  public static VirtuesSet editVirtues(WindowController parent, VirtuesSet virtues)
  {
    VirtuesEditionDialogController controller=new VirtuesEditionDialogController(parent);
    if (parent!=null)
    {
      controller.getWindow().setLocationRelativeTo(parent.getWindow());
    }
    controller.setVirtues(virtues);
    controller.show(true);
    VirtuesSet editedVirtues=controller._virtues;
    controller.dispose();
    return editedVirtues;
  }
}
