package com.financialtransaction.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "transaction_audit")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "audit_id")
    private Long auditId;

    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id",
            nullable = false)
    private Transaction transaction;

    @Column(name = "event_type",
            nullable = false)
    private String eventType;

    @Column(nullable = false,
            columnDefinition = "TEXT")
    private String message;

    @Column(name = "created_at",
            nullable = false)
    private LocalDateTime createdAt;
}