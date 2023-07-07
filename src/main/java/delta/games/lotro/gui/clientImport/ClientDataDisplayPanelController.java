package delta.games.lotro.gui.clientImport;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.misc.Disposable;
import delta.games.lotro.memory.facade.data.ImportStatusData;

/**
 * Controller for a panel to display the client data.
 * @author DAM
 */
public class ClientDataDisplayPanelController implements Disposable
{
  // UI
  private JPanel _panel;
  private JLabel _serverName;
  private JLabel _language;
  private JLabel _clientType;

  /**
   * Constructor.
   */
  public ClientDataDisplayPanelController()
  {
    _panel=buildPanel();
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  public JPanel getPanel()
  {
    return _panel;
  }

  private JPanel buildPanel()
  {
    _serverName=GuiFactory.buildLabel("");
    _language=GuiFactory.buildLabel("");
    _clientType=GuiFactory.buildLabel("");

    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,1.0,1.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(2,5,2,0),0,0);
    c.gridx++;
    ret.add(Box.createHorizontalStrut(150),c);
    c.gridx=0;c.gridy++;
    // Server name
    ret.add(GuiFactory.buildLabel("Server Name:"),c); // I18n
    c.gridx++;
    ret.add(_serverName,c);
    c.gridx=0;c.gridy++;
    // Language
    ret.add(GuiFactory.buildLabel("Language:"),c); // I18n
    c.gridx++;
    ret.add(_language,c);
    c.gridx=0;c.gridy++;
    // Client type
    ret.add(GuiFactory.buildLabel("Client type:"),c); // I18n
    c.gridx++;
    ret.add(_clientType,c);
    c.gridx=0;c.gridy++;
    return ret;
  }

  /**
   * Update UI using the given data.
   * @param data Input data.
   */
  public void updateUi(ImportStatusData data)
  {
    // Server name
    String serverName=data.getServer();
    serverName=(serverName!=null)?serverName:"?";
    _serverName.setText(serverName);
    // Language
    String language=data.getLanguage();
    language=(language!=null)?language:"?";
    _language.setText(language);
    // Client type
    String clientType=data.getClientType();
    clientType=(clientType!=null)?clientType:"?";
    _clientType.setText(clientType);
  }

  @Override
  public void dispose()
  {
    _serverName=null;
    _language=null;
    _clientType=null;
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
  }
}
