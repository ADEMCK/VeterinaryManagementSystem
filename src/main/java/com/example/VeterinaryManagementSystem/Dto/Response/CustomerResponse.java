package com.example.VeterinaryManagementSystem.Dto.Response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponse {
    Long id;
    String name;
    String phone;
    String email;
    String address;
    String city;

}
