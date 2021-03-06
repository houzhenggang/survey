package org.survey.repository.user;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.survey.model.user.User;

/**
 * UserRepository reads and writes Users to database. Uses Spring-JPA and
 * CrudRepository to create a UserRepositoryImpl which contains CRUD methods for
 * reading and writing users to database. See JpaConfig for Spring
 * configuration.
 */
@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long> {
    User findByEmail(@Param("email") String email);
    User findByUsername(@Param("username") String username);
}