package com.apiweather.app.tools.rest;

import com.apiweather.app.tools.exception.BusinessException;

public interface ModelMapper<T, M> {
	
	public T mapDtoToModel(M m)  throws BusinessException ;
	
	public M mapModelToDto(T m)  throws BusinessException ;
}
