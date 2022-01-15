package io.berbotworks.budgetapp.models;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = { "details" })
@Entity
@Table(name = "expenses")
public class Expense extends BaseEntity {

    private String iname;
    private BigDecimal price;
    private String date;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "details_id")
    private Details details;

    public Expense(String iname, BigDecimal price, String date) {
        this.iname = iname;
        this.price = price;
        this.date = date;
    }

}
