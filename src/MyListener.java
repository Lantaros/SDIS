import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileInputStream;
import javax.net.ssl.HandshakeCompletedListener;
import java.util.Hashtable;
import javax.net.ssl.SSLSession;
import java.security.cert.Certificate;
import javax.net.ssl.SSLPeerUnverifiedException;

  public class MyListener implements HandshakeCompletedListener
  {
    public void handshakeCompleted(javax.net.ssl.HandshakeCompletedEvent event)
    {
      SSLSession session = event.getSession();
      System.out.println("Handshake Completed with peer " + session.getPeerHost());
      System.out.println("   cipher: " + session.getCipherSuite());
      Certificate[] certs = null;
      try
      {
        certs = session.getPeerCertificates();
      }
      catch (SSLPeerUnverifiedException puv)
      {
        certs = null;
      }
      if  (certs != null)
      {
        System.out.println("    peer certificates:");
        for (int z=0; z<certs.length; z++) 
          System.out.println(" certs[" + z + "]:" + certs[z]);
      }
      else
      {
        System.out.println("No peer certificates presented");
      }
    }
  }
