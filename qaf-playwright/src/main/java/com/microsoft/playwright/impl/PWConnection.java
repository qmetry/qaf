package com.microsoft.playwright.impl;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.qmetry.qaf.automation.ui.playwright.PlaywrightCommandTracker;
import com.qmetry.qaf.automation.ui.playwright.PlaywrightListenerHandler;

public class PWConnection extends Connection {

	private PlaywrightListenerHandler listener;

	public PWConnection(Transport pipe, Map<String, String> env, LocalUtils localUtils) {
		super(pipe, env, localUtils);
	}
	
	public PWConnection(Transport transport, Map<String, String> env) {
		super(transport, env);
	}

	public PWConnection(InputStream inputStream, OutputStream outputStream, Map<String, String> env) {
		super(new PipeTransport(inputStream, outputStream),env);
	}

	@Override
	public JsonElement sendMessage(String guid, String method, JsonObject params) {
		PlaywrightCommandTracker commandTracker = new PlaywrightCommandTracker(method, params);

		try {
			if(null!=listener) listener.beforeCommand(commandTracker);
			if(commandTracker.getResponce() == null) {
				commandTracker.setResponce(super.sendMessage(guid, commandTracker.getCommand(), commandTracker.getParameters()));
			}
			if(null!=listener) listener.afterCommand(commandTracker);
			return commandTracker.getResponce();

		} catch (RuntimeException e) {
			commandTracker.setException(e);
			if(null!=listener) listener.onFailure(commandTracker);
			if (commandTracker.shouldRetry()) {
				commandTracker.setException(null);
				commandTracker.setResponce(super.sendMessage(guid, commandTracker.getCommand(), commandTracker.getParameters()));
				listener.afterCommand(commandTracker);
			}
			if(commandTracker.hasException()) 
				throw commandTracker.getException();
			
			return commandTracker.getResponce();
		}
	}
	
	
	public PWConnection withListener(PlaywrightListenerHandler listener) {
		this.listener = listener;
		return this;
	}
	
	@Override
	public WaitableResult<JsonElement> sendMessageAsync(String guid, String method, JsonObject params) {
		return super.sendMessageAsync(guid, method, params);
	}
	

}
