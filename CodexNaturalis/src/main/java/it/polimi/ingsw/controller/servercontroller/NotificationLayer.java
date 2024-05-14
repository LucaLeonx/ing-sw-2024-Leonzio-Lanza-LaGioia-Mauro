package it.polimi.ingsw.controller.servercontroller;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class NotificationLayer extends InternalServerLayer{

    private final List<NotificationSubscriber> subscribers;

    public NotificationLayer(InternalServerLayer... nextLayers) {
        super(nextLayers);
        this.subscribers = Collections.synchronizedList(new LinkedList<>());
    }
}
