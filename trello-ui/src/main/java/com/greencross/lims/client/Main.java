package com.greencross.lims.client;

import com.greencross.lims.client.trello.TrelloElement;
import com.greencross.lims.dto.Query;

public class Main extends AbstractEntryPoint {
	private TrelloElement trello;
	@Override
	public AbstractScene<?>[] elements(Query query) {
		trello = new TrelloElement(query);
		return new AbstractScene[] {trello};
	}

	@Override
	protected AbstractScene<?> prepare(String param) {
		trello.update();
		return trello;
	}

	@Override
	protected String toParentUrl(String param) {
		return "패널검사/Trello";
	}
}
