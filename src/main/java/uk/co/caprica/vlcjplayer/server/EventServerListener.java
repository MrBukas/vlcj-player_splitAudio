package uk.co.caprica.vlcjplayer.server;

import uk.co.caprica.vlcjplayer.event.PausedEvent;
import uk.co.caprica.vlcjplayer.event.PlayingEvent;
import uk.co.caprica.vlcjplayer.view.action.mediaplayer.MediaPlayerActions;

import java.awt.event.ActionEvent;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;
import static uk.co.caprica.vlcjplayer.Application.application;


public class EventServerListener implements Runnable{

    private Scanner scanner;
    private MediaPlayerActions mediaPlayerActions;

    public EventServerListener() {
        if (ServerUtils.isListener()) {
            mediaPlayerActions = application().mediaPlayerActions();
            try {
                Socket socket = new Socket();
                socket.connect(new InetSocketAddress(InetAddress.getLocalHost(), EventNames.SERVER_PORT));
                this.scanner = new Scanner(socket.getInputStream());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    private void listenForEvents() {
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();
            System.out.println("Command received: " + command);
            switch (command) {
                case "play": {
                    application().post(PlayingEvent.INSTANCE);
                    playPauseAction();
                    //mediaPlayerActions.playbackPlayAction().actionPerformed(null);
                    break;
                }
                case "pause": {
                    application().post(PausedEvent.INSTANCE);
                    playPauseAction();
                    //mediaPlayerActions.playbackPlayAction().actionPerformed(null);
                    break;
                }
            }
        }
    }

    private void playPauseAction() {
        if (!application().mediaPlayer().status().isPlaying()) {
            application().mediaPlayer().controls().play();
        }
        else {
            application().mediaPlayer().controls().pause();
        }
    }

    @Override
    public void run() {
        listenForEvents();
    }
}
