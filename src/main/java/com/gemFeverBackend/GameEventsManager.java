package com.gemFeverBackend;

import java.time.LocalDate;

import Items.Item;
import Items.ItemManager;

public class GameEventsManager {
	public static GameEvent[] events = new GameEvent[] {
		new GameEvent("beta", LocalDate.of(2020, 12, 25), LocalDate.of(2021, 1, 20), new Item[] {ItemManager.betaHat},
				"¡Gracias por jugar a la versión de prueba de Gem Fever! Como agradecimiento, ¡te regalamos un gorro exclusivo!",
				"Thanks for playing the trial version of Gem Fever! To thank you, we are giving you an exclusive hat!"),
	};
}
