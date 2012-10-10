package delta.games.lotro.utils;

import java.io.File;

import delta.common.utils.text.EncodingNames;
import delta.downloads.DownloadException;
import delta.downloads.Downloader;

/**
 * Downloading service for LOTRO companion.
 * @author DAM
 */
public class DownloadService
{
  private static DownloadService _instance=new DownloadService();
  private Downloader _downloader;
  
  /**
   * Get the sole instance of this class.
   * @return the sole instance of this class.
   */
  public static final DownloadService getInstance()
  {
    return _instance;
  }

  private DownloadService()
  {
    _downloader=new Downloader();
    _downloader.setCharset(EncodingNames.UTF_8);
    _downloader.setFollowsRedirects(true);
    _downloader.setStoreCookies(false);
  }

  /**
   * Get a page as a string. 
   * @param url Source URL.
   * @return A string or <code>null</code> if not found.
   * @throws DownloadException If an error occurs.
   */
  public String getPage(String url) throws DownloadException
  {
    String ret=_downloader.downloadString(url);
    return ret;
  }

  /**
   * Get a page as a byte buffer. 
   * @param url Source URL.
   * @return A byte buffer or <code>null</code> if not found.
   * @throws DownloadException If an error occurs.
   */
  public byte[] getBuffer(String url) throws DownloadException
  {
    byte[] ret=_downloader.downloadBuffer(url);
    return ret;
  }

  /**
   * Download an URL into a file.
   * @param url Source URL.
   * @param to Target file.
   * @return <code>true</code> if file was successfully written, <code>false</code> otherwise.
   * @throws DownloadException
   */
  public boolean downloadToFile(String url, File to) throws DownloadException
  {
    boolean ret=_downloader.downloadToFile(url,to);
    return ret;
  }
}
