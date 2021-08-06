package com.apiweather.app.rest;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.apiweather.app.tools.exception.EntityNotFoundException;


/**
 * @author a.bouabidi
 *
 * @param <R>
 * @param <T>
 * @param <L>
 */
public class AbstractCommonRessource <R, T, L>{

	private R repo;
		
	@Autowired
	public AbstractCommonRessource(R repo) {
		super();
		this.repo = repo;
	}
	
	@GetMapping("/list")
	@ResponseBody
	public ResponseEntity<T> getElements(@NotNull T t) {				
		List<T> l = (List<T>) ((MongoRepository< T, L>)repo).findAll(Example.of(t));
		return new ResponseEntity(l,HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	@ResponseBody
	public ResponseEntity<T> getElementById(@Valid  @NotNull @PathVariable(name = "id")L id) throws EntityNotFoundException {		
		Optional<T> r =  ((MongoRepository< T, L>)repo).findById(id);
		if (r.isEmpty()) throw new EntityNotFoundException ("Object was not found for parameters {id="+id+"}");
		return new ResponseEntity(r.get(),HttpStatus.OK);
	}

	@PostMapping("/add")
	@ResponseBody
	public ResponseEntity<T> addElement(@Valid @RequestBody(required = true) T t) {		
		T r = (T) ((MongoRepository< T, L>)repo).insert(t);
		return new ResponseEntity(r,HttpStatus.OK);
	}
		
	@PutMapping("/update/{id}")
	@ResponseBody
	public ResponseEntity<T> saveElement(@Valid  @NotNull @PathVariable(name = "id")L id,@Valid @RequestBody T t) throws EntityNotFoundException {	
		Optional<T> r =  ((MongoRepository< T, L>)repo).findById(id);
		T o = null;
		if (r.isPresent()) {
			o = ((MongoRepository< T, L>)repo).save(t);
		}else {
			throw new EntityNotFoundException("Element not found");	
		}
		
		return new ResponseEntity(o,HttpStatus.OK);
	}
	
	@PostMapping("/remove/{id}")
	@ResponseBody
	public ResponseEntity<T> removeElementById(@Valid @NotNull @PathVariable(name = "id")L l) {		
		((MongoRepository< T, L>)repo).deleteById(l);
		return new ResponseEntity("element removed",HttpStatus.OK);

	}
	
	
	protected R getRepository () {
		return this.repo;
	}
}
