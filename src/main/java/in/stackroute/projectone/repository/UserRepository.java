package in.stackroute.projectone.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import in.stackroute.projectone.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
