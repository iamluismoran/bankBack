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

    @Column(name = "street", length = 120, nullable = false)
    private String street;

    @Column(name = "city", length = 80, nullable = false)
    private String city;

    @Column(name = "country", length = 30, nullable = false)
    private String country;

    @Column(name = "postal_code", length = 16, nullable = false)
    private String postalCode;
}
