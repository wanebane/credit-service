package com.rivaldy.creditservices.model.dto;

import com.rivaldy.creditservices.util.FormatData;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InstallmentDto {

    private Integer year;
    private Double monthlyAmount;
    private Double rate;

    @Override
    public String toString() {
        return String.format(
                "Tahun %d: %s/month | Suku Bunga: %s",
                year,
                FormatData.currencyFormat(monthlyAmount),
                FormatData.percentFormat(rate)
        );
    }
}
