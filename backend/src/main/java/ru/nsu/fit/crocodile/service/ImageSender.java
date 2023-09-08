package ru.nsu.fit.crocodile.service;

import ru.nsu.fit.crocodile.model.Image;

@FunctionalInterface
public interface ImageSender {
    void send(Image image);
}
