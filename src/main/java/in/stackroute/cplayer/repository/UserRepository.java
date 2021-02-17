package in.stackroute.cplayer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import in.stackroute.cplayer.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
