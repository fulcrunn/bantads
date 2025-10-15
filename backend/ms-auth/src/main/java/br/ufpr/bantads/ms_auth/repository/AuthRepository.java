package br.ufpr.bantads.ms_auth.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.ufpr.bantads.ms_auth.model.UserAuth;

interface AuthRepository extends MongoRepository<UserAuth, String> {

    Optional<UserAuth> findByEmail(String email);
   
}
