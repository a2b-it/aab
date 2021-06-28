package com.apiweather.app.biz.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.apiweather.app.biz.model.Student;




public interface StudentRepository extends MongoRepository<Student, Long> {
	
	Student findFirstByStudentName(String studentName);

	/*Student findByStudentAndAge(String domain, int age);

    //Supports native JSON query string
    @Query("{studentAge:'?0'}")
    Student findCustomByDomain(int studentAge);

   @Query("{domain: { $regex: ?0 } })")
    List<Student> findCustomByRegExDomain(String domain);*/
}
