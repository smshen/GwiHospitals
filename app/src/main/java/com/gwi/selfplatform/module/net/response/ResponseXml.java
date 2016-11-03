package com.gwi.selfplatform.module.net.response;

import java.io.Serializable;

public class ResponseXml implements Serializable {
	private Response Response;
	
	public Response getResponse() {
		return Response;
	}

	public void setResponse(Response response) {
		Response = response;
	}

}
