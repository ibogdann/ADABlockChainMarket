package org.freesoftware.admin.soap.webservice;

import javax.xml.ws.Endpoint;

import org.freesoftware.admin.soap.webservice.data.WebServiceDataProvider;

public class SOAPWebServiceProvider {
	
	public void begin(WebServiceDataProvider dataProvider, String url) {
		Endpoint.publish(url, new WebServiceImpl(dataProvider));
	}
}
