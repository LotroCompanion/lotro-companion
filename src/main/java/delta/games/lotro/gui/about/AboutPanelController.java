package delta.games.lotro.gui.about;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.icons.IconsManager;
import delta.common.ui.swing.labels.BrowserHyperlinkAction;
import delta.common.ui.swing.labels.HyperLinkController;
import delta.common.ui.swing.labels.MailToHyperlinkAction;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.Config;
import delta.games.lotro.config.LotroCoreConfig;
import delta.games.lotro.gui.utils.l10n.Labels;

/**
 * Controller for a 'about' panel.
 * @author DAM
 */
public class AboutPanelController
{
  private JPanel _panel;
  private HyperLinkController _mail;

  /**
   * Constructor.
   */
  public AboutPanelController()
  {
    // Nothing!
  }

  private JPanel build()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints c;

    int x=0;
    int y=0;

    // Icon (if any)
    {
      Image icon=IconsManager.getImage("/resources/gui/ring/ring48.png");
      c=new GridBagConstraints(x,y,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(10,20,10,5),0,0);
      ImageIcon imageIcon=new ImageIcon(icon);
      JLabel lbIcon=new JLabel(imageIcon);
      panel.add(lbIcon,c);
      x++;
    }

    boolean isLive=LotroCoreConfig.isLive();
    String appName=Labels.getLabel(isLive?"about.app.name.lc":"about.app.name.lld");
    JLabel lbName=new JLabel(appName);
    Font defaultFont=lbName.getFont();
    Font font16=defaultFont.deriveFont(Font.BOLD,16);
    Font font24=defaultFont.deriveFont(Font.BOLD,24);
    // Project name
    {
      lbName.setFont(defaultFont.deriveFont(Font.BOLD,36));
      c=new GridBagConstraints(x,y,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(10,5,10,20),0,0);
      panel.add(lbName,c);
      y++;
    }

    // Project version
    {
      String projectVersion=buildProjectVersion();
      JLabel lbVersion=new JLabel(projectVersion);
      lbVersion.setFont(font24);
      c=new GridBagConstraints(0,y,2,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(10,5,10,5),0,0);
      panel.add(lbVersion,c);
      y++;
    }

    // Project contact
    {
      JPanel contactPanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.CENTER));
      JLabel contactLabel=GuiFactory.buildLabel(Labels.getFieldLabel("about.contact"));
      contactLabel.setFont(font16);
      contactPanel.add(contactLabel);
      String email=isLive?"lotrocompanion@gmail.com":"lotroloredatabase@gmail.com";
      MailToHyperlinkAction mailAction=new MailToHyperlinkAction(email,Labels.getLabel("about.contact"));
      _mail=new HyperLinkController(mailAction);
      JLabel lbEmail=_mail.getLabel();
      lbEmail.setFont(font16);
      contactPanel.add(lbEmail);
      c=new GridBagConstraints(0,y,2,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(10,5,5,5),0,0);
      panel.add(contactPanel,c);
      y++;
    }

    // Source code
    if (isLive)
    {
      JPanel sourcePanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.CENTER));
      JLabel sourceLabel=GuiFactory.buildLabel(Labels.getFieldLabel("about.sourceCode"));
      sourceLabel.setFont(font16);
      sourcePanel.add(sourceLabel);
      BrowserHyperlinkAction githubAction=new BrowserHyperlinkAction("https://github.com/LotroCompanion/lotro-companion","lotro-companion@GitHub");
      HyperLinkController github=new HyperLinkController(githubAction);
      JLabel lbGitHub=github.getLabel();
      lbGitHub.setFont(font16);
      sourcePanel.add(lbGitHub);
      c=new GridBagConstraints(0,y,2,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(0,5,5,5),0,0);
      panel.add(sourcePanel,c);
      y++;
    }

    // Discord
    {
      JPanel discordPanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.CENTER));
      JLabel discordLabel=GuiFactory.buildLabel(Labels.getFieldLabel("about.discord"));
      discordLabel.setFont(font16);
      discordPanel.add(discordLabel);
      String discordText=isLive?"Lotro Companion's corner":"Discord";
      BrowserHyperlinkAction discordAction=new BrowserHyperlinkAction("https://discord.gg/t2J4GDq",discordText);
      HyperLinkController discord=new HyperLinkController(discordAction);
      JLabel lbDiscord=discord.getLabel();
      lbDiscord.setFont(font16);
      discordPanel.add(lbDiscord);
      c=new GridBagConstraints(0,y,2,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(0,5,10,5),0,0);
      panel.add(discordPanel,c);
      y++;
    }

    // User Manual
    {
      BrowserHyperlinkAction userManualAction=new BrowserHyperlinkAction("https://github.com/LotroCompanion/lotro-companion-doc/blob/master/UserManual/index.md","User Manual");
      HyperLinkController userManualLink=new HyperLinkController(userManualAction);
      c=new GridBagConstraints(0,y,2,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(0,5,10,5),0,0);
      JLabel userManualLabel=userManualLink.getLabel();
      userManualLabel.setFont(font16);
      panel.add(userManualLabel,c);
      y++;
    }

    return panel;
  }

  private String buildProjectVersion()
  {
    TypedProperties props=Config.getInstance().getParameters();
    String name=props.getStringProperty("current.version.name","?");
    String date=props.getStringProperty("current.version.date","01/01/1970");
    return "Version "+name+" ("+date+')';
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  public JPanel getPanel()
  {
    if (_panel==null)
    {
      _panel=build();
    }
    return _panel;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    if (_mail!=null)
    {
      _mail.dispose();
      _mail=null;
    }
  }
}
