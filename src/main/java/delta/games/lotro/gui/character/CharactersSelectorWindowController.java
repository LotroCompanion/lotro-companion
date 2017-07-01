package delta.games.lotro.gui.character;

import java.awt.BorderLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;

import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.gui.utils.GuiFactory;
import delta.games.lotro.gui.utils.OKCancelPanelController;
import delta.games.lotro.utils.gui.DefaultDialogController;
import delta.games.lotro.utils.gui.WindowController;

/**
 * Controller for a "character log" window.
 * @author DAM
 */
public class CharactersSelectorWindowController extends DefaultDialogController
{
  // Controllers
  private OKCancelPanelController _okCancelController;
  private CharactersSelectorPanelController _controller;
  // Data
  private List<CharacterFile> _selectedToons;

  /**
   * Constructor.
   * @param parent Parent controller.
   * @param toons Managed toons.
   * @param selectedToons Selected toons.
   * @param enabledToons Enabled toons.
   */
  private CharactersSelectorWindowController(WindowController parent, List<CharacterFile> toons, List<CharacterFile> selectedToons, List<CharacterFile> enabledToons)
  {
    super(parent);
    _controller=new CharactersSelectorPanelController(toons);
    for(CharacterFile toon : selectedToons)
    {
      _controller.setToonSelected(toon,true);
    }
    for(CharacterFile toon : enabledToons)
    {
      _controller.setToonEnabled(toon,true);
    }
    _okCancelController=new OKCancelPanelController();
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
      Window parentWindow=controller.getWindow();
      dialog.setLocationRelativeTo(parentWindow);
    }
    //dialog.setMinimumSize(new Dimension(400,300));
    return dialog;
  }

  @Override
  protected JComponent buildContents()
  {
    JPanel panel=GuiFactory.buildPanel(new BorderLayout());
    JPanel checkBoxesPanel=_controller.getPanel();
    panel.add(checkBoxesPanel,BorderLayout.CENTER);
    JPanel okCancelPanel=_okCancelController.getPanel();
    panel.add(okCancelPanel,BorderLayout.SOUTH);
    return panel;
  }

  /**
   * Show the toon selection dialog.
   * @param parent Parent controller.
   * @param toons Toons to show.
   * @param selectedToons Pre-selected toons.
   * @param enabledToons Enabled toons.
   * @return A list of selected toons or <code>null</code> if the window was closed or canceled.
   */
  public static List<CharacterFile> selectToons(WindowController parent, List<CharacterFile> toons, List<CharacterFile> selectedToons, List<CharacterFile> enabledToons)
  {
    CharactersSelectorWindowController controller=new CharactersSelectorWindowController(parent,toons,selectedToons,enabledToons);
    List<CharacterFile> newSelectedToons=controller.doShow();
    return newSelectedToons;
  }

  private List<CharacterFile> doShow()
  {
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
        _selectedToons=_controller.getSelectedToons();
      }

      private void cancel()
      {
        _selectedToons=null;
      }
    };
    _okCancelController.getOKButton().addActionListener(al);
    _okCancelController.getCancelButton().addActionListener(al);
    WindowController parent=getParentController();
    if (parent!=null)
    {
      Window parentWindow=parent.getWindow();
      JDialog thisDialog=getDialog();
      thisDialog.setLocationRelativeTo(parentWindow);
    }
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
    // Controllers
    if (_okCancelController!=null)
    {
      _okCancelController.dispose();
      _okCancelController=null;
    }
    _selectedToons=null;
    super.dispose();
  }
}
