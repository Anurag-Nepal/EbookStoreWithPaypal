package com.anuragnepal.itbooksnepal.Entity.Helpers;

import com.anuragnepal.itbooksnepal.Entity.Users;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table
@Entity
public class OtpService {
    @Id
    private Integer otp;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "uid", referencedColumnName = "id")
    private Users users;
}
