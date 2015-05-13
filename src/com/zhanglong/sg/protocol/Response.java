package com.zhanglong.sg.protocol;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhanglong.sg.result.Error;
import com.zhanglong.sg.result.ErrorResult;

public class Response {

    public static String marshalSuccess(int id, Object object) throws JsonParseException, JsonMappingException, IOException {

    	Success success = new Success();
    	success.setId(id);
    	success.setResult(object);

    	ObjectMapper mapper = new ObjectMapper();
    	return mapper.writeValueAsString(success);
    }

    public static String marshalError(int id, int code, String message) throws JsonParseException, JsonMappingException, IOException {

    	Error err = new Error();
    	err.setCode(code);
    	err.setMessage(message);
    	
    	ErrorResult errorResult = new ErrorResult();
    	
    	errorResult.setId(id);
    	errorResult.setError(err);

    	ObjectMapper mapper = new ObjectMapper();
    	return mapper.writeValueAsString(errorResult);
    }
}
