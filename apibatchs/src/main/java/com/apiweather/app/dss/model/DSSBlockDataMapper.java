/**
 * 
 */
package com.apiweather.app.dss.model;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

/**
 * @author a.bouabidi
 *
 */
public class DSSBlockDataMapper  implements FieldSetMapper<DSSBlockData>{

	@Override
	public DSSBlockData mapFieldSet(FieldSet fieldSet) throws BindException {
		DSSBlockData file = new DSSBlockData();
		int n = fieldSet.getFieldCount();
		file.setIndex(fieldSet.readInt(0));
		file.setDate(fieldSet.readDate(1, "dd/MM/yyyy"));
		file.setValue(fieldSet.readDouble(2));
		return file;
	}

}
