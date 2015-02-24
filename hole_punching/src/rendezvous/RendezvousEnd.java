package rendezvous;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class RendezvousEnd {

  public static final int RENDEZVOUS_PORT = 54322;
  private Map<String, String> pathSocketInfo;

  public RendezvousEnd() {
    pathSocketInfo = new HashMap<String, String>();
  }

  public void run() throws IOException {
    try (ServerSocket server = new ServerSocket(RENDEZVOUS_PORT)) {
      while (true) {
        Socket socket = server.accept();
        processSocket(socket);
      }
    }
  }

  private void processSocket(Socket socket) throws IOException {
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedWriter writer =
            new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));) {
      String headerLine = reader.readLine();
      if (headerLine.startsWith("GET")) {
        String path = getPath(headerLine);
        if (path != null) {
          // return requested peer info
        } else {
          // return 404
        }
      } else {
        String path = headerLine.split(" ")[0];
        pathSocketInfo.put(path, socket.getRemoteSocketAddress() + " " + socket.getPort());
      }
    }
  }

  private String getPath(String headerLine) {
    String[] parts = headerLine.split(" ");
    if (parts.length == 3) {
      return parts[1];
    } else {
      return null;
    }
  }

  public static void main(String[] args) throws IOException {
    RendezvousEnd server = new RendezvousEnd();
    server.run();
  }

}
