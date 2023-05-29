package uk.co.caprica.vlcjplayer.server;

import uk.co.caprica.vlcjplayer.Application;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import static uk.co.caprica.vlcjplayer.Application.application;

public class EventServerPublisher {

    private ServerSocket serverSocket;
    private Socket publisherSocket;
    private PrintWriter printWriter;


    public EventServerPublisher(){
        if (ServerUtils.isServer()) {
            try {
                this.serverSocket = new ServerSocket(EventNames.SERVER_PORT);
                this.publisherSocket = serverSocket.accept();
                this.printWriter = new PrintWriter(publisherSocket.getOutputStream(), true);
            } catch (Exception e) {
                e.printStackTrace();
//                this.serverSocket = null;
//                this.publisherSocket = null;
//                this.printWriter = null;
            }
        }
    }

    private static final class ApplicationHolder {
        private static final EventServerPublisher INSTANCE = ServerUtils.isServer() ? new EventServerPublisher() : null;
    }



    public static EventServerPublisher eventPublisher() {
        return EventServerPublisher.ApplicationHolder.INSTANCE;
    }

    public void publishPlayEvent() {
        printWriter.println(EventNames.PLAYING_EVENT);
    }

    public void publishStopEvent() {
        printWriter.println(EventNames.STOP_EVENT);
    }

    public void publishPauseEvent() {
        printWriter.println(EventNames.PAUSE_EVENT);
    }

    public void publishEvent(Object event) {
        switch (event.getClass().getSimpleName()) {
            case "PlayingEvent": {
                publishPlayEvent();
                break;
            }
            case "PausedEvent": {
                publishPauseEvent();
                break;
            }
            case "StoppedEvent": {
                publishStopEvent();
                break;
            }

        }
    }

}
