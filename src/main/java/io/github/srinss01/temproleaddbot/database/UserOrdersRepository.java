package io.github.srinss01.temproleaddbot.database;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserOrdersRepository extends JpaRepository<UserOrders, String> {
}