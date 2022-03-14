package com.cts.packagingdelivery.exception;

@SuppressWarnings("serial")
public class ComponentTypeNotFoundException extends RuntimeException {
    public ComponentTypeNotFoundException(String msg)
    {
        super(msg);
    }
}
