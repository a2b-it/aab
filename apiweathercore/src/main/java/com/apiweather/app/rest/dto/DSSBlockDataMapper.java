/**
 * 
 */
package com.apiweather.app.rest.dto;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.apiweather.app.biz.model.DSSBlock;
import com.apiweather.app.biz.model.DSSBlock.DSSBlockData;
import com.apiweather.app.tools.exception.BusinessException;
import com.apiweather.app.tools.rest.ModelMapper;

/**
 * @author a.bouabidi
 *
 */
public class DSSBlockDataMapper implements ModelMapper<DSSBlock, WeatherPreciptByDayDTO[]>{

	
	//TODO transfer the logic to a dedicated service
	@Override
	public DSSBlock mapDtoToModel(WeatherPreciptByDayDTO[] tw) throws BusinessException {
		int i=0;
		DSSBlock block;
		try {
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat fs = new SimpleDateFormat("ddMMMyyyy", new Locale("en", "EN"));
		block = new DSSBlock();
		DSSBlockData[] tb = new DSSBlockData[tw.length];
		Date firstDate = null;
		for (WeatherPreciptByDayDTO w : tw) {
			firstDate =(firstDate==null)?f.parse(w.getId()):firstDate;
			if(firstDate!=null && firstDate.after(f.parse(w.getId()))) {
				firstDate=f.parse(w.getId());
			}
			tb[i] = block.new DSSBlockData ();
			tb[i].setDate(f.parse(w.getId()));
			tb[i].setIndex(i);
			tb[i].setValue(w.getSumByDay());
			i++;
		}
		block.setDssBlockDatas(tb);
		block.setType("PER-CUM");
		block.setUnits("mm");
		block.setStartDate(fs.format(firstDate));
		}catch(Exception e) {
			throw new BusinessException (e);
		}
		return block;
	}

	@Override
	public WeatherPreciptByDayDTO[] mapModelToDto(DSSBlock m) {
		// TODO Auto-generated method stub
		return null;
	}

}
