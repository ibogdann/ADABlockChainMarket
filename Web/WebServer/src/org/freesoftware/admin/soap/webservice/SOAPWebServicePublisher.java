package org.freesoftware.admin.soap.webservice;

import javax.xml.ws.Endpoint;

import org.freesoftware.admin.soap.webservice.data.WebServiceDataProvider;
import org.freesoftware.admin.soap.webservice.data.impl.WebServiceDataProviderImpl;

public class SOAPWebServicePublisher {

	public static void main(String[] args) {
		WebServiceDataProvider dataProvider = new WebServiceDataProviderImpl();
		populateDataProvider(dataProvider);
		
		Endpoint.publish("http://localhost:8082/admin", new WebServiceImpl(dataProvider));
		System.out.println("---WEB SERVER DEPLOYED---");
	}
	
	public static void populateDataProvider(WebServiceDataProvider dataProvider)
	{
		dataProvider.addProduct("Paine", "Patiserie", 12, 3, "Romania");
		dataProvider.addProduct("Apa", "Alimente", 40, 2, "Romania");
		dataProvider.addProduct("Faina", "Alimente", 80, 3, "Romania");
	}
}
