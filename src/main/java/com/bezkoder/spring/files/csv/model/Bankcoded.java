package com.bezkoder.spring.files.csv.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 銀行公會明細檔
 */
@Entity
@Table(name = "bankcoded")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Bankcoded implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqGenCk")
    @SequenceGenerator(name = "seqGenCk")
    private Long id;

    /**
     * 銀行帳號 4 + 107 = 004 0107
     */
    @NotNull
    @Column(name = "bank_account", nullable = false)
    private String bankAccount;

    /**
     * 帳號名稱 
     */
    @NotNull
    @Column(name = "bank_name", nullable = false)
    private String bankName;

    /**
     * 帳號簡稱
     */
    @NotNull
    @Column(name = "bank_short_name", nullable = false)
    private String bankShortName;

    /**
     * 建立人員
     */
    @NotNull
    @Column(name = "create_man", nullable = false)
    private String createMan;

    /**
     * 建立日期
     */
    @NotNull
    @Column(name = "create_date", nullable = false)
    private Instant createDate;

    /**
     * 修改人員
     */
    @NotNull
    @Column(name = "change_man", nullable = false)
    private String changeMan;

    /**
     * 修改日期
     */
    @NotNull
    @Column(name = "change_date", nullable = false)
    private Instant changeDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankShortName() {
        return bankShortName;
    }

    public void setBankShortName(String bankShortName) {
        this.bankShortName = bankShortName;
    }

    public String getCreateMan() {
        return createMan;
    }

    public void setCreateMan(String createMan) {
        this.createMan = createMan;
    }

    public Instant getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

    public String getChangeMan() {
        return changeMan;
    }

    public void setChangeMan(String changeMan) {
        this.changeMan = changeMan;
    }

    public Instant getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(Instant changeDate) {
        this.changeDate = changeDate;
    }

    // @ManyToOne
    // @JsonIgnoreProperties(value = { "bankcodeds" }, allowSetters = true)
    // private Bankcode bankcode;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    
}
