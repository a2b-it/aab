package com.apiweather.app.biz.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.apiweather.app.biz.model.Output;
import com.apiweather.app.rest.dto.OutputRpDTO;

public interface OutputRepository extends MongoRepository<Output, Long> {
	
	
	@Query("{\"filename\":?0,\"idStation\":?1}")
	public List<Output> findOutputByfilenameAndStation (String filename, Long idStation);
	
	
	public List<Object> findByFilename (String filename);
	
	@Aggregation(pipeline = {"{'$match':{'stationId': ?0 }}","{$group: { '_id': '', time: {$max: $time }}}"} )
	public List <Output> findByMaxValue (long maxValue );
	
	
	@Aggregation(pipeline = {"{'$project': {'dssBlocks':{$filter:{input:'$dssBlocks',as:'t',cond:{$and:[{$eq:['$$t.location',?0]}, {$eq:['$$t.dataParam',?1]}, {$gte:['$$t.dssBlockDatas.value',?2]}]}}}}}"})
	public Output findElementsByMaxValue(String location, String dataParam, double seuil);
	
	@Aggregation(pipeline = {"{'$unwind': { path: '$dssBlocks', preserveNullAndEmptyArrays:false, includeArrayIndex: 'blocId'}}","{'$match':{ $and:[{'dssBlocks.location':?0}, {'dssBlocks.dataParam':?1},  {'dssBlocks.dssBlockDatas.value':{$gte:?2}}]}}"})
	public OutputRpDTO[] findMaxValueByLocationAndDataParam(String location, String dataParam, double seuil);
}
