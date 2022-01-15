package io.berbotworks.budgetapp.models;

import java.math.BigDecimal;

import javax.persistence.Entity;
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
@Table(name = "table_entries")
public class TableEntry extends BaseEntity {

    private String date;
    private BigDecimal spent;

    @JsonBackReference
    @ManyToOne
    private Details details;

    public TableEntry(String date, BigDecimal spent) {
        this.date = date;
        this.spent = spent;
    }
}
