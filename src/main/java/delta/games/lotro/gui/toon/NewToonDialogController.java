package delta.games.lotro.gui.toon;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import delta.games.lotro.Config;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.CharactersManager;
import delta.games.lotro.gui.utils.GuiFactory;
import delta.games.lotro.gui.utils.OKCancelPanelController;
import delta.games.lotro.utils.TypedProperties;

/**
 * Controller for the "new toon" dialog.
 * @author DAM
 */
public class NewToonDialogController implements ActionListener
{
  private static final int TOON_NAME_SIZE=32;
  private JDialog _dialog;
  private OKCancelPanelController _okCancelController;
  private JTextField _toonName;
  private JComboBox _server;

  /**
   * Show the managed dialog.
   */
  public void show()
  {
    JDialog dialog=getDialog();
    dialog.setVisible(true);
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

  private JDialog build()
  {
    JPanel panel=GuiFactory.buildPanel(new BorderLayout());
    JPanel dataPanel=buildNewToonPanel();
    TitledBorder pathsBorder=GuiFactory.buildTitledBorder("Toon");
    dataPanel.setBorder(pathsBorder);
    panel.add(dataPanel,BorderLayout.CENTER);
    _okCancelController=new OKCancelPanelController();
    JPanel okCancelPanel=_okCancelController.getPanel();
    panel.add(okCancelPanel,BorderLayout.SOUTH);
    _okCancelController.getOKButton().addActionListener(this);
    _okCancelController.getCancelButton().addActionListener(this);
    JDialog dialog=new JDialog();
    dialog.setContentPane(panel);
    dialog.setTitle("New toon...");
    dialog.pack();
    return dialog;
  }

  private JPanel buildNewToonPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    _toonName=GuiFactory.buildTextField("");
    _toonName.setColumns(TOON_NAME_SIZE);
    List<String> servers=Config.getInstance().getServerNames();
    _server=GuiFactory.buildComboBox();
    String[] serverItems=servers.toArray(new String[servers.size()]);
    DefaultComboBoxModel model=new DefaultComboBoxModel(serverItems);
    _server.setModel(model);
    TypedProperties props=Config.getInstance().getParameters();
    String defaultServer=props.getStringProperty("default.server",null);
    if (defaultServer!=null)
    {
      model.setSelectedItem(defaultServer);
    }
    Insets insets=new Insets(5,5,5,5);
    GridBagConstraints gbc=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,insets,0,0);
    panel.add(GuiFactory.buildLabel("Name:"),gbc);
    gbc.gridx=0; gbc.gridy=1;
    panel.add(GuiFactory.buildLabel("Server:"),gbc);
    gbc.gridx=1; gbc.gridy=0; 
    gbc.weightx=1.0; gbc.fill=GridBagConstraints.HORIZONTAL;
    panel.add(_toonName,gbc);
    gbc.gridx=1; gbc.gridy=1;
    panel.add(_server,gbc);
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
    String toonName=_toonName.getText();
    String server=(String)_server.getSelectedItem();
    CharactersManager manager=CharactersManager.getInstance();
    CharacterFile toon=manager.addToon(server,toonName);
    if (toon!=null)
    {
      dispose();
    }
    else
    {
      // todo error?
    }
    /*
    if (toon!=null)
    {
      CharacterLogsManager logManager=new CharacterLogsManager(toon);
      boolean ok=logManager.updateLog();
      System.out.println(ok);
    }
    */
  }

  private void cancel()
  {
    dispose();
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
    if (_okCancelController!=null)
    {
      _okCancelController.dispose();
      _okCancelController=null;
    }
    _toonName=null;
    _server=null;
  }
}
