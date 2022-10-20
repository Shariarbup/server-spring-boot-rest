package io.gateways.server.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResourceNotfoundException extends RuntimeException{
    String resourceName;
    String fieldName;
    Long fieldValue;

    public ResourceNotfoundException(String resourceName, String fieldName, Long fieldValue){
        super(String.format("%s not found with %s :   %d", resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }
}
