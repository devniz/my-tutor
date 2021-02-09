package com.mytutor.bookshop.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class ReportItem {
    private Integer totalSales;
    private BigDecimal bookPrice;
}
