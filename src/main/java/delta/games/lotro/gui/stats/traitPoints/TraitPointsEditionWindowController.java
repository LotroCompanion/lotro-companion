package delta.games.lotro.gui.stats.traitPoints;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;

import delta.games.lotro.character.CharacterSummary;
import delta.games.lotro.gui.utils.GuiFactory;
import delta.games.lotro.gui.utils.OKCancelPanelController;
import delta.games.lotro.stats.traitPoints.TraitPointsStatus;
import delta.games.lotro.utils.gui.DefaultDialogController;
import delta.games.lotro.utils.gui.WindowController;

/**
 * Controller for a traits points edition window.
 * @author DAM
 */
public class TraitPointsEditionWindowController extends DefaultDialogController implements ActionListener
{
  private TraitPointsEditionPanelController _panelController;
  private OKCancelPanelController _okCancelController;
  private CharacterSummary _summary;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param summary Character summary.
   * @param status Status to edit.
   */
  public TraitPointsEditionWindowController(WindowController parent, CharacterSummary summary, TraitPointsStatus status)
  {
    super(parent);
    _summary=summary;
    _panelController=new TraitPointsEditionPanelController(this,summary,status);
  }

  @Override
  protected JDialog build()
  {
    JDialog dialog=super.build();
    dialog.pack();
    WindowController controller=getParentController();
    if (controller!=null)
    {
      Window parentWindow=controller.getWindow();
      dialog.setLocationRelativeTo(parentWindow);
    }
    dialog.setMinimumSize(new Dimension(400,300));
    String name=_summary.getName();
    int level=_summary.getLevel();
    dialog.setTitle("Trait points for "+name+" ("+level+")");
    return dialog;
  }

  @Override
  public String getWindowIdentifier()
  {
    return "ItemEditor";
  }

  @Override
  protected JComponent buildContents()
  {
    JPanel panel=GuiFactory.buildPanel(new BorderLayout());
    JPanel dataPanel=_panelController.getPanel();
    panel.add(dataPanel,BorderLayout.CENTER);
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
      ok();
    }
    else if (OKCancelPanelController.CANCEL_COMMAND.equals(action))
    {
      cancel();
    }
  }

  private void ok()
  {
    System.out.println("ok");
    // TODO
    dispose();
  }

  private void cancel()
  {
    System.out.println("cancel");
    dispose();
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    super.dispose();
    _summary=null;
    if (_okCancelController!=null)
    {
      _okCancelController.dispose();
      _okCancelController=null;
    }
    if (_panelController!=null)
    {
      _panelController.dispose();
      _panelController=null;
    }
  }
}
