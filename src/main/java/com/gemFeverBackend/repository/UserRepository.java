package com.gemFeverBackend.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.gemFeverBackend.model.User;

public interface UserRepository extends MongoRepository<User, String> {
	
	@Override
	public List<User> findAll();
}
