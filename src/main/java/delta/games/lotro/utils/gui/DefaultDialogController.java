package delta.games.lotro.utils.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Image;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import delta.games.lotro.gui.utils.GuiFactory;
import delta.games.lotro.gui.utils.IconUtils;

/**
 * Default dialog controller.
 * @author DAM
 */
public class DefaultDialogController implements WindowController
{
  private JDialog _dialog;
  private WindowController _parent;

  /**
   * Constructor.
   * @param parent Parent controller.
   */
  public DefaultDialogController(WindowController parent)
  {
    _parent=parent;
  }

  /**
   * Compute a window identifier.
   * @return A string that uniquely identifies the managed frame.
   */
  public String getWindowIdentifier()
  {
    return null;
  }

  /**
   * Get the managed dialog.
   * @return the managed dialog.
   */
  public JDialog getDialog()
  {
    if (_dialog==null)
    {
      _dialog=build();
    }
    return _dialog;
  }

  /**
   * Get the managed window.
   * @return the managed window.
   */
  public Window getWindow()
  {
    return getDialog();
  }

  /**
   * Get the parent controller.
   * @return a controller or <code>null</code> if there's none.
   */
  public WindowController getParentController()
  {
    return _parent;
  }

  protected JDialog build()
  {
    Window parentWindow=null;
    if (_parent!=null)
    {
      parentWindow=_parent.getWindow();
    }
    
    JDialog dialog;
    if (parentWindow!=null)
    {
      dialog=new JDialog(parentWindow);
    }
    else
    {
      dialog=new JDialog();
    }
    JPanel backgroundPanel=GuiFactory.buildBackgroundPanel(new BorderLayout());
    dialog.setContentPane(backgroundPanel);
    List<Image> icons=IconUtils.getIcons();
    dialog.setIconImages(icons);
    Container contentPane=dialog.getContentPane();
    JComponent component=buildContents();
    if (component!=null)
    {
      contentPane.add(component,BorderLayout.CENTER);
    }
    dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    WindowAdapter closeWindowAdapter=new WindowAdapter()
    {
      @Override
      public void windowClosing(WindowEvent e)
      {
        doWindowClosing();
      }
    };
    dialog.addWindowListener(closeWindowAdapter);
    return dialog;
  }

  protected JComponent buildContents()
  {
    return new JPanel();
  }

  /**
   * Perform window closing.
   */
  protected void doWindowClosing()
  {
    dispose();
  }

  public void show()
  {
    show(false);
  }

  /**
   * Show the managed window.
   * @param modal Modality of the managed dialog.
   */
  public void show(boolean modal)
  {
    JDialog dialog=getDialog();
    dialog.setModal(modal);
    dialog.setVisible(true);
  }

  /**
   * Hide this dialog.
   */
  public void hide()
  {
    if (_dialog!=null)
    {
      _dialog.setVisible(false);
    }
  }

  /**
   * Bring the managed window to front.
   */
  public void bringToFront()
  {
    JDialog dialog=getDialog();
    dialog.setVisible(true);
    dialog.toFront();
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    if (_dialog!=null)
    {
      _dialog.setVisible(false);
      _dialog.removeAll();
      _dialog.dispose();
      _dialog=null;
    }
    _parent=null;
  }
}
