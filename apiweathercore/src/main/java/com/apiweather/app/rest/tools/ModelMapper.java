package com.apiweather.app.rest.tools;

public interface ModelMapper<T, M> {
	
	public T mapDtoToModel(M m);
	
	public M mapModelToDto(T m);
}
