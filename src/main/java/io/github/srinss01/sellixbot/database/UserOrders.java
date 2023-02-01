package io.github.srinss01.sellixbot.database;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_order_info_db")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserOrders {
    @Id
    @Column(name = "id", nullable = false)
    private String orderId;
    private Long userId;
}