package ph.kana.grtb.service;

import javafx.concurrent.Task;
import javafx.scene.control.TextArea;
import ph.kana.grtb.exception.GrailsProcessException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ProcessStreamingService {

	public void streamToTextArea(InputStream inputStream, TextArea textArea) {
		textArea.setText("");

		Task bgTask = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				bufferStreamToTextArea(inputStream, textArea);
				return null;
			}
		};
		Thread thread = new Thread(bgTask);
		thread.setDaemon(true);
		thread.start();
	}

	private void bufferStreamToTextArea(InputStream inputStream, TextArea textArea) {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
			String line = reader.readLine();
			while (null != line) {
				textArea.appendText(line);
				textArea.appendText("\n");
				line = reader.readLine();
			}
		} catch (IOException e) {
			throw new GrailsProcessException("Error streaming grails process.", e);
		}
	}
}