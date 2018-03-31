package listeners;

import protocol.Chunk;
import protocol.FileInfo;
import protocol.Message;
import protocol.Peer;

import java.nio.charset.Charset;

public class MDataBackupChannel implements Runnable{
    private Peer peer;

    public MDataBackupChannel(Peer p){
        peer = p;
    }

    @Override
    public void run() {
        while (true) {
            String response = new String(receivePacket.getData(), Charset.forName("ISO_8859_1"));
            Message message = new Message(response);

            if (!message.getVersion().equals(peer.getVersion()))
                continue; //Ignore message

            switch (message.getType()) {
                case PUTCHUNK:
                    if (message.getSenderID() != peer.getId()) {
                        Chunk chunk = new Chunk(message.getFileID(), message.getChunkNum());
                        if (!peer.getstoredChunks().contains(chunk))
                            if (peer.getdiskSpace() - message.getPayload().length >= 0) {
                                peer.setDiskSpace(peer.getDiskSpace() -= message.getPayload().length;)
                                chunk.setData(message.getPayload());
                                FileInfo.saveChunk(peer, chunk);

                            }
                        //Protocol.sendSTORED(message)
                    }
                    break;
            }
        }
    }
}
