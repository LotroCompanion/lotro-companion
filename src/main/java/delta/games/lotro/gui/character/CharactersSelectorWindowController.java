package delta.games.lotro.gui.character;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.gui.utils.OKCancelPanelController;
import delta.games.lotro.utils.gui.DefaultDialogController;
import delta.games.lotro.utils.gui.WindowController;

/**
 * Controller for a "character log" window.
 * @author DAM
 */
public class CharactersSelectorWindowController extends DefaultDialogController
{
  private CharactersSelectorPanelController _controller;
  private List<CharacterFile> _selectedToons;

  /**
   * Constructor.
   * @param parent Parent controller.
   * @param toons Managed toons.
   * @param selectedToons Selected toons.
   */
  private CharactersSelectorWindowController(WindowController parent, List<CharacterFile> toons, List<CharacterFile> selectedToons)
  {
    super(parent);
    _controller=new CharactersSelectorPanelController(toons,selectedToons);
  }

  @Override
  protected JDialog build()
  {
    JDialog dialog=super.build();
    String title="Characters selector";
    dialog.setTitle(title);
    dialog.pack();
    WindowController controller=getParentController();
    if (controller!=null)
    {
      JFrame parentFrame=controller.getFrame();
      dialog.setLocationRelativeTo(parentFrame);
    }
    //dialog.setMinimumSize(new Dimension(400,300));
    return dialog;
  }
  
  @Override
  protected JComponent buildContents()
  {
    JPanel panel=_controller.getPanel();
    return panel;
  }

  /**
   * Show the toon selection dialog.
   * @param parent Parent controller.
   * @param toons Toons to show.
   * @param selectedToons Pre-selected toons.
   * @return A list of selected toons or <code>null</code> if the window was closed or canceled.
   */
  public static List<CharacterFile> selectToons(WindowController parent, List<CharacterFile> toons, List<CharacterFile> selectedToons)
  {
    CharactersSelectorWindowController controller=new CharactersSelectorWindowController(parent,toons,selectedToons);
    List<CharacterFile> newSelectedToons=controller.doShow();
    return newSelectedToons;
  }

  private List<CharacterFile> doShow()
  {
    OKCancelPanelController okCancelController=_controller.getOKCancelController();
    ActionListener al=new ActionListener()
    {
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
        hide();
      }

      private void ok()
      {
        _controller.updateSelection();
        _selectedToons=_controller.getSelectedToons();
      }

      private void cancel()
      {
        _selectedToons=null;
      }
    };
    okCancelController.getOKButton().addActionListener(al);
    okCancelController.getCancelButton().addActionListener(al);
    show(true);
    List<CharacterFile> selectedToons=_selectedToons;
    dispose();
    return selectedToons;
  }

  @Override
  protected void doWindowClosing()
  {
    hide();
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    if (_controller!=null)
    {
      _controller.dispose();
      _controller=null;
    }
    _selectedToons=null;
    super.dispose();
  }
}
