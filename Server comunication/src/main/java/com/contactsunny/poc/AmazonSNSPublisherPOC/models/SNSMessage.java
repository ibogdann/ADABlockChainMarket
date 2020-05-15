package com.contactsunny.poc.AmazonSNSPublisherPOC.models;

import java.util.HashMap;
import java.util.Map;

import com.amazonaws.services.sns.model.MessageAttributeValue;

public class SNSMessage {

	private String message;
    private Map<String, MessageAttributeValue> messageAttributes;
    public static final String PRODUCT_ID = "product_id";
    public static final String WORLD_WIDE_STOCK = "wwstock";

    public SNSMessage(final String message) {
        this.message = message;
        messageAttributes = new HashMap<>();
    }
    
    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }
    
    public void addAttribute(final String attributeName, final Number attributeValue) {
        final MessageAttributeValue messageAttributeValue = new MessageAttributeValue()
                .withDataType("Number")
                .withStringValue(attributeValue.toString());
        messageAttributes.put(attributeName, messageAttributeValue);
    }
    
    public Map<String, MessageAttributeValue> getMessageAttributes() {
    	return messageAttributes;
    }
    
}
