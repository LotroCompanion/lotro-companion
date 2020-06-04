package delta.games.lotro.gui.interceptor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.DefaultDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.gui.main.GlobalPreferences;
import delta.games.lotro.interceptor.data.monitoring.InterceptionLog;
import delta.games.lotro.interceptor.data.monitoring.InterceptionLogListener;

/**
 * Basic dialog to control the network capture.
 * @author DAM
 */
public class InterceptorDialogController extends DefaultDialogController implements InterceptionLogListener
{
  /**
   * Identifier of this dialog.
   */
  public static final String IDENTIFIER="INTERCEPTOR";
  /**
   * Interceptor.
   */
  private InterceptorController _interceptorController;
  /**
   * Child UI controllers.
   */
  private InterceptionLogTableController _tableController;

  /**
   * Constructor.
   * @param parent Parent window controller.
   */
  public InterceptorDialogController(WindowController parent)
  {
    super(parent);
    _interceptorController=new InterceptorController(this);
  }

  @Override
  protected JDialog build()
  {
    JDialog dialog=super.build();
    dialog.setModal(false);
    dialog.setTitle("Interceptor");
    dialog.setResizable(true);
    dialog.setPreferredSize(new Dimension(800,300));
    dialog.setMinimumSize(new Dimension(400,200));
    dialog.pack();
    WindowController controller=getParentController();
    if (controller!=null)
    {
      Window parentWindow=controller.getWindow();
      dialog.setLocationRelativeTo(parentWindow);
    }
    return dialog;
  }

  /**
   * Get the window identifier for a given toon.
   * @return A window identifier.
   */
  public static String getIdentifier()
  {
    return IDENTIFIER;
  }

  @Override
  public String getWindowIdentifier()
  {
    return getIdentifier();
  }

  @Override
  protected JComponent buildContents()
  {
    JPanel buttonsPanel=buildButtonsPanel();
    TypedProperties prefs=GlobalPreferences.getGlobalProperties("Interceptor");
    InterceptionLog log=_interceptorController.getLog();
    _tableController=new InterceptionLogTableController(prefs,null,log);
    JPanel ret=GuiFactory.buildBackgroundPanel(new BorderLayout());
    ret.add(buttonsPanel,BorderLayout.NORTH);
    JTable table=_tableController.getTable();
    JScrollPane scroll=GuiFactory.buildScrollPane(table);
    ret.add(scroll,BorderLayout.CENTER);
    return ret;
  }

  private JPanel buildButtonsPanel()
  {
    // Start
    JButton startButton=GuiFactory.buildButton("Start");
    ActionListener alStart=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        _interceptorController.start();
      }
    };
    startButton.addActionListener(alStart);
    // Stop
    JButton stopButton=GuiFactory.buildButton("Stop");
    ActionListener alStop=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        _interceptorController.stop();
      }
    };
    stopButton.addActionListener(alStop);

    JPanel panel=GuiFactory.buildPanel(new FlowLayout());
    panel.add(startButton);
    panel.add(stopButton);
    return panel;
  }

  @Override
  public void logUpdated()
  {
    Runnable r=new Runnable()
    {
      @Override
      public void run()
      {
        _tableController.refresh();
      }
    };
    SwingUtilities.invokeLater(r);
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    super.dispose();
    if (_tableController!=null)
    {
      _tableController.dispose();
      _tableController=null;
    }
    if (_interceptorController!=null)
    {
      _interceptorController.dispose();
      _tableController=null;
    }
  }
}
