package io.github.srinss01.sellixbot.database;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserOrdersRepository extends JpaRepository<UserOrders, String> {
}