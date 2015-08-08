package com.reporting;

import org.openqa.selenium.WebElement;

public class CaptureScreen {
	private boolean captureBrowserPage = false;
	private boolean captureDesktop = false;
	private boolean captureWebElement = false;
	private WebElement element = null;

	public CaptureScreen(WebElement paramWebElement) {
		if (paramWebElement != null) {
			setCaptureWebElement(true);
			setElement(paramWebElement);
		}
	}

	public CaptureScreen(ScreenshotOf paramScreenshotOf) {
		if (paramScreenshotOf == ScreenshotOf.BROWSER_PAGE) {
			setCaptureBrowserPage(true);
		} else if (paramScreenshotOf == ScreenshotOf.DESKTOP) {
			setCaptureDesktop(true);
		}
	}

	public boolean isCaptureBrowserPage() {
		return this.captureBrowserPage;
	}

	public void setCaptureBrowserPage(boolean paramBoolean) {
		this.captureBrowserPage = paramBoolean;
	}

	public boolean isCaptureDesktop() {
		return this.captureDesktop;
	}

	public void setCaptureDesktop(boolean paramBoolean) {
		this.captureDesktop = paramBoolean;
	}

	public boolean isCaptureWebElement() {
		return this.captureWebElement;
	}

	public void setCaptureWebElement(boolean paramBoolean) {
		this.captureWebElement = paramBoolean;
	}

	public WebElement getElement() {
		return this.element;
	}

	public void setElement(WebElement paramWebElement) {
		this.element = paramWebElement;
	}

	public static enum ScreenshotOf {
		BROWSER_PAGE, DESKTOP;

		private ScreenshotOf() {}
	}
}
