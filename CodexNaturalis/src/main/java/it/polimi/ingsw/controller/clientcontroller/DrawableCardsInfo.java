package it.polimi.ingsw.controller.clientcontroller;

import it.polimi.ingsw.model.DrawChoice;

import java.util.Map;

public record DrawableCardsInfo(Map<DrawChoice, CardSideInfo> drawableCards) {}
