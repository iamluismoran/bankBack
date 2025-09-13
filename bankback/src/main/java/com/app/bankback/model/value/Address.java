package com.app.bankback.model.value;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {

    @Column(length = 120, nullable = true)
    private String street;

    @Column(length = 80, nullable = true)
    private String city;

    @Column(length = 30, nullable = true)
    private String country;

    @Column(length = 16, nullable = true)
    private String postalCode;
}
