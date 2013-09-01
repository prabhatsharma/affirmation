package org.prabhat.ARepeater;

public class ApplicationState {
	int appStarted;
	ButtonsEnabled currentButtons;

	public ButtonsEnabled getCurrentButtons() {
		return currentButtons;
	}

	public void setCurrentButtons(ButtonsEnabled currentButtons) {
		this.currentButtons = currentButtons;
	}

	public int getAppStarted() {
		return appStarted;
	}

	public void setAppStarted(int appStarted) {
		this.appStarted = appStarted;
	}
}
